#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

import pytest
from difflib import unified_diff
import re
import os
from path import path
import getpass
import Ice
import ode.cli
from ode.plugins.web import (
    APACHE_MOD_WSGI_ERR,
    WebControl
)
from engine import settings

class TestWeb(object):

    def setup_method(self, method):
        self.cli = ode.cli.CLI()
        self.cli.register("web", WebControl, "TEST")
        self.args = ["web"]

    def set_templates_dir(self, monkeypatch):

        dist_dir = path(__file__) / ".." / ".." / ".." / ".." / ".." / ".." /\
            ".." / "dist"  # FIXME: should not be hard-coded
        dist_dir = dist_dir.abspath()
        monkeypatch.setattr(WebControl, '_get_web_templates_dir',
                            lambda x: dist_dir / "etc" / "templates" / "web")

    def set_python_path(self, monkeypatch, python_path=None):
        if python_path:
            monkeypatch.setenv('PYTHONPATH', python_path)
        else:
            monkeypatch.delenv('PYTHONPATH')

    def set_python_dir(self, monkeypatch):

        dist_dir = path(__file__) / ".." / ".." / ".." / ".." / ".." /\
            "target"  # FIXME: should not be hard-coded
        dist_dir = dist_dir.abspath()
        monkeypatch.setattr(WebControl, '_get_python_dir',
                            lambda x: dist_dir / "lib" / "python")

    def mock_os_kill(self, monkeypatch, error=False):
        def os_kill(pid, signal):
            if error:
                raise OSError()
            return None
        monkeypatch.setattr(os, 'kill', os_kill)

    def mock_subprocess_popen(self, monkeypatch):
        def subprocess_popen(*args, **kwargs):
            return 0
        monkeypatch.setattr(ode.cli.CLI, 'popen', subprocess_popen)

    def mock_subprocess_call(self, monkeypatch):
        def subprocess_call(*args, **kwargs):
            return 0
        monkeypatch.setattr(ode.cli.CLI, 'call', subprocess_call)

    def check_django_pid(self, monkeypatch, error=False):
        def check_pid(self, pid, path):
            return not error
        monkeypatch.setattr(WebControl, '_check_pid', check_pid)

    def set_django_pid(self, monkeypatch, pid=None):
        def django_pid(self, path):
            return pid
        monkeypatch.setattr(WebControl, '_get_django_pid', django_pid)

    def add_prefix(self, prefix, monkeypatch):

        def _get_default_value(x):
            return settings.CUSTOM_SETTINGS_MAPPINGS[x][1]
        if prefix:
            static_prefix = prefix + '-static/'
        else:
            prefix = _get_default_value('ode.web.prefix')
            static_prefix = _get_default_value('ode.web.static_url')
        monkeypatch.setattr(settings, 'STATIC_URL', static_prefix,
                            raising=False)
        monkeypatch.setattr(settings, 'FORCE_SCRIPT_NAME', prefix,
                            raising=False)
        return static_prefix

    def mock_django_setting(self, setting_name, setting_val, monkeypatch):
        if setting_val:
            monkeypatch.setattr(settings, setting_name, setting_val,
                                raising=False)
        return setting_val

    def add_upstream_name(self, prefix, monkeypath):
        if prefix:
            name = "engine_%s" % re.sub(r'\W+', '', prefix)
        else:
            name = "engine"
        return name

    def add_hostport(self, host, port, monkeypatch):
        if host:
            monkeypatch.setattr(settings, 'APPLICATION_SERVER_HOST', host,
                                raising=False)
        if port:
            monkeypatch.setattr(settings, 'APPLICATION_SERVER_PORT', port,
                                raising=False)
        return '%s:%s' % (host or '127.0.0.1', port or '4080')

    def clean_generated_file(self, txt):
        assert "%(" not in txt   # Make sure all markers have been replaced
        lines = [line.strip() for line in txt.split('\n')]
        lines = [line for line in lines if line and not line.startswith('#')]
        return lines

    def required_lines_in(self, required, lines):
        # Checks that all lines in required are present in the same order,
        # but not necessarily consecutively
        def compare(req, line):
            if isinstance(req, tuple):
                return line.startswith(req[0]) and line.endswith(req[1])
            return req == line

        n = 0
        for req in required:
            while not compare(req, lines[n]):
                n += 1
                if n == len(lines):
                    return req
        return None

    def normalise_generated(self, s):
        serverdir = self.cli.dir
        s = re.sub('\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}.\d{6}',
                   '0000-00-00 00:00:00.000000', s)
        s = s.replace(serverdir, '/home/bhojpur/ODE.server')
        s = s.replace(os.path.dirname(Ice.__file__), '/home/bhojpur/ice/python')
        s = s.replace('user=%s' % getpass.getuser(), 'user=ode')
        return s

    def compare_with_reference(self, refname, generated):
        reffile = path(__file__).dirname() / 'reference_templates' / refname
        generated = generated.split('\n')
        # reffile.write_lines(generated)
        ref = reffile.lines(retain=False)
        d = '\n'.join(unified_diff(ref, generated))
        return d

    def testHelp(self):
        self.args += ["-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('subcommand', WebControl().get_subcommands())
    def testSubcommandHelp(self, subcommand):
        self.args += [subcommand, "-h"]
        self.cli.invoke(self.args, strict=True)

    @pytest.mark.parametrize('app_server', ['wsgi', 'wsgi-tcp', 'development'])
    def testWebStart(self, app_server, monkeypatch, capsys):
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_subprocess_popen(monkeypatch)
        self.mock_subprocess_call(monkeypatch)
        self.set_django_pid(monkeypatch)
        self.args += ["start"]
        self.cli.invoke(self.args, strict=(app_server not in ('wsgi',)))
        o, e = capsys.readouterr()
        csout = "Clearing expired sessions. This may take some time... [OK]"
        assert csout == o.split(os.linesep)[0]
        if app_server in ('wsgi',):
            assert APACHE_MOD_WSGI_ERR == e.split(os.linesep)[0]
            assert 1 == len(e.split(os.linesep))-1
        elif app_server in ('wsgi-tcp',):
            startout = "Starting Bhojpur ODE.web... [OK]"
            assert startout == o.split(os.linesep)[1]
            assert 2 == len(o.split(os.linesep))-1
        elif app_server in ('development',):
            startout = "Starting Bhojpur ODE.web... [OK]"
            assert startout == o.split(os.linesep)[1]
            assert 2 == len(o.split(os.linesep))-1

    @pytest.mark.parametrize('app_server', ['wsgi', 'wsgi-tcp', 'development'])
    def testWebRestart(self, app_server, monkeypatch, capsys):
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_subprocess_popen(monkeypatch)
        self.set_django_pid(monkeypatch)
        self.args += ["restart"]
        self.cli.invoke(self.args, strict=True)
        o, e = capsys.readouterr()
        if app_server in ('wsgi',):
            assert APACHE_MOD_WSGI_ERR in e.split(os.linesep)[0]
            assert 1 == len(e.split(os.linesep))-1
        elif app_server in ('wsgi-tcp',):
            stdout = (
                "Stopping Bhojpur ODE.web... [NOT STARTED]",
                "Clearing expired sessions. This may take some time... [OK]",
                "Starting Bhojpur ODE.web... [OK]"
            )
            for msg in stdout:
                assert msg in o.split(os.linesep)
            assert 3 == len(o.split(os.linesep))-1
        elif app_server in ('development',):
            stderr = "DEVELOPMENT: You will have to kill processes by hand!"
            assert stderr in e.split(os.linesep)[0]
            assert 1 == len(e.split(os.linesep))-1

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    def testWebStop(self, app_server, monkeypatch, capsys):
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_os_kill(monkeypatch)
        self.check_django_pid(monkeypatch)
        self.set_django_pid(monkeypatch, -999999)
        self.args += ["stop"]
        self.cli.invoke(self.args, strict=True)
        o, e = capsys.readouterr()
        stdout = ("Stopping Bhojpur ODE.web... [OK]",
                  "Bhojpur ODE.web WSGI workers (PID -999999) killed.")
        assert 2 == len(o.split(os.linesep))-1
        for msg in stdout:
            assert msg in o.split(os.linesep)

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    def testWebBadRestart(self, app_server, monkeypatch, capsys):
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_os_kill(monkeypatch)
        self.set_django_pid(monkeypatch, -999999)
        self.args += ["restart"]
        self.cli.invoke(self.args, strict=False)
        o, e = capsys.readouterr()
        stdout = (
            "Stopping Bhojpur ODE.web... [OK]",
            "Bhojpur ODE.web WSGI workers (PID -999999) killed.",
            "Clearing expired sessions. This may take some time... [OK]",
            "Starting Bhojpur ODE.web... "
        )
        for msg in stdout:
            assert msg in o.split(os.linesep)
        assert 4 == len(o.split(os.linesep))
        stderr = ("[FAILED] Bhojpur ODE.web already started. "
                  "%s/var/django.pid exists (PID: -999999)! "
                  "Use 'web stop or restart' first.") % str(self.cli.dir)
        assert stderr == e.split(os.linesep)[0]
        assert 1 == len(e.split(os.linesep))-1

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    def testWebStale(self, app_server, monkeypatch, capsys):
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_os_kill(monkeypatch, error=True)
        self.set_django_pid(monkeypatch, -999999)
        self.args += ["stop"]
        self.cli.invoke(self.args, strict=False)
        o, e = capsys.readouterr()

        stdout = (
            "Stopping Bhojpur ODE.web... ",
        )
        for msg in stdout:
            assert msg in o.split(os.linesep)
        stderr = ("[ERROR] Bhojpur ODE.web workers (PID -999999) - no such process."
                  " Use `ps aux | grep %s/var/django.pid` and kill stale"
                  " processes by hand.") % str(self.cli.dir)
        assert stderr == e.split(os.linesep)[0]

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    @pytest.mark.parametrize('wsgi_args', [None, "", '--reload'])
    def testWebWsgiArgs(self, app_server, wsgi_args, monkeypatch, capsys):
        self.mock_django_setting('WSGI_ARGS', wsgi_args, monkeypatch)
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_subprocess_popen(monkeypatch)
        self.mock_subprocess_call(monkeypatch)
        self.set_django_pid(monkeypatch)
        start_cmd = ["start"]
        if wsgi_args is not None:
            start_cmd.append("--wsgi-args='%s'" % wsgi_args)
        self.cli.invoke(self.args + start_cmd, strict=True)
        o, e = capsys.readouterr()
        if wsgi_args is not None:
            startout = ("Starting Bhojpur ODE.web...  `--wsgi-args` is deprecated"
                        " and overwritten by `ode.web.wsgi_args`. [OK]")
        else:
            startout = "Starting Bhojpur ODE.web... [OK]"
        assert startout == o.split(os.linesep)[1]
        assert 2 == len(o.split(os.linesep))-1

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    @pytest.mark.parametrize('wsgi_workers', [None, 1])
    def testWebWorkers(self, app_server, wsgi_workers, monkeypatch, capsys):
        self.mock_django_setting('WSGI_WORKERS', wsgi_workers, monkeypatch)
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_subprocess_popen(monkeypatch)
        self.mock_subprocess_call(monkeypatch)
        self.set_django_pid(monkeypatch)
        start_cmd = ["start"]
        if wsgi_workers is not None:
            start_cmd.append("--workers=%r" % wsgi_workers)
        self.cli.invoke(self.args + start_cmd, strict=True)
        o, e = capsys.readouterr()
        if wsgi_workers is not None:
            startout = ("Starting Bhojpur ODE.web...  `--workers` is deprecated"
                        " and overwritten by `ode.web.wsgi_workers`. [OK]")
        else:
            startout = "Starting Bhojpur ODE.web... [OK]"
        assert startout == o.split(os.linesep)[1]
        assert 2 == len(o.split(os.linesep))-1

    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    @pytest.mark.parametrize('wsgi_worker_conn', [None, 1])
    def testWebWorkerConnections(self, app_server, wsgi_worker_conn,
                                 monkeypatch, capsys):
        self.mock_django_setting('WSGI_WORKER_CONNECTIONS', wsgi_worker_conn,
                                 monkeypatch)
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.set_python_dir(monkeypatch)
        self.mock_subprocess_popen(monkeypatch)
        self.mock_subprocess_call(monkeypatch)
        self.set_django_pid(monkeypatch)
        start_cmd = ["start"]
        if wsgi_worker_conn is not None:
            start_cmd.append("--worker-connections=%r" % wsgi_worker_conn)
        self.cli.invoke(self.args + start_cmd, strict=True)
        o, e = capsys.readouterr()
        if wsgi_worker_conn is not None:
            startout = ("Starting Bhojpur ODE.web...  `--worker-connections` is"
                        " deprecated and overwritten by "
                        "`ode.web.wsgi_worker_connections`. [OK]")
        else:
            startout = "Starting Bhojpur ODE.web... [OK]"
        assert startout == o.split(os.linesep)[1]
        assert 2 == len(o.split(os.linesep))-1

    @pytest.mark.parametrize('max_body_size', [None, '0', '1m'])
    @pytest.mark.parametrize('server_type', [
        "nginx", "nginx-development"])
    @pytest.mark.parametrize('http', [False, 8081])
    @pytest.mark.parametrize('servername', [False, "engine.host"])
    @pytest.mark.parametrize('prefix', [None, '/test'])
    @pytest.mark.parametrize('app_server', ['wsgi-tcp'])
    @pytest.mark.parametrize('cgihost', [None, '0.0.0.0'])
    @pytest.mark.parametrize('cgiport', [None, '12345'])
    def testNginxGunicornConfig(self, server_type, http, servername, prefix,
                                app_server, cgihost, cgiport, max_body_size,
                                capsys, monkeypatch):

        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        static_prefix = self.add_prefix(prefix, monkeypatch)
        upstream_name = self.add_upstream_name(prefix, monkeypatch)
        expected_cgi = self.add_hostport(cgihost, cgiport, monkeypatch)

        self.args += ["config"]
        self.args += server_type.split()
        if http:
            self.args += ["--http", str(http)]
        if servername:
            self.args += ["--servername", str(servername)]
        if max_body_size:
            self.args += ["--max-body-size", max_body_size]
        self.set_templates_dir(monkeypatch)
        self.cli.invoke(self.args, strict=True)
        o, e = capsys.readouterr()
        lines = self.clean_generated_file(o)

        if "development" in server_type:
            missing = self.required_lines_in([
                "upstream %s {" % upstream_name,
                "server %s fail_timeout=0;" % expected_cgi,
                "server {",
                "listen %s;" % (http or 8080),
                "server_name _;",
                "client_max_body_size %s;" % (max_body_size or '0'),
                "location %s {" % static_prefix[:-1],
                "location %s {" % (prefix or "/"),
                ], lines)
        else:
            missing = self.required_lines_in([
                "upstream %s {" % upstream_name,
                "server %s fail_timeout=0;" % expected_cgi,
                "server {",
                "listen %s;" % (http or 80),
                "server_name %s;" % (servername or "$hostname"),
                "client_max_body_size %s;" % (max_body_size or '0'),
                "location %s {" % static_prefix[:-1],
                "location %s {" % (prefix or "/"),
                ], lines)
        assert not missing, 'Line not found: ' + str(missing)

    @pytest.mark.parametrize('server_type', [
        ["nginx", 'wsgi-tcp'],
        ["nginx-development", 'wsgi-tcp'],
        ["nginx-location", 'wsgi-tcp'],
    ])
    @pytest.mark.parametrize('static_root', [
        '/home/bhojpur/ODE.server/lib/python/engine/static'])
    def testFullTemplateDefaults(self, server_type, static_root,
                                 capsys, monkeypatch):
        app_server = server_type[-1]
        del server_type[-1]
        self.mock_django_setting('STATIC_ROOT', static_root, monkeypatch)
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.args += ["config"] + server_type
        self.set_templates_dir(monkeypatch)
        self.set_python_path(monkeypatch)
        self.cli.invoke(self.args, strict=True)

        o, e = capsys.readouterr()
        o = self.normalise_generated(o)
        d = self.compare_with_reference(server_type[0] + '.conf', o)
        assert not d, 'Files are different:\n' + d

    @pytest.mark.parametrize('server_type', [
        ['nginx', '--http', '1234',
         '--servername', 'engine.host',
         '--max-body-size', '2m', 'wsgi-tcp'],
        ['nginx-development', '--http', '1234',
         '--servername', 'engine.host',
         '--max-body-size', '2m', 'wsgi-tcp'],
        ['nginx-location', '--http', '1234',
         '--servername', 'engine.host',
         '--max-body-size', '2m', 'wsgi-tcp'],
    ])
    @pytest.mark.parametrize('static_root', [
        '/home/bhojpur/ODE.server/lib/python/engine/static'])
    def testFullTemplateWithOptions(self, server_type, static_root,
                                    capsys, monkeypatch):
        prefix = '/test'
        cgihost = '0.0.0.0'
        cgiport = '12345'
        app_server = server_type[-1]
        del server_type[-1]
        self.mock_django_setting('STATIC_ROOT', static_root, monkeypatch)
        self.mock_django_setting('APPLICATION_SERVER', app_server, monkeypatch)
        self.add_prefix(prefix, monkeypatch)
        self.add_hostport(cgihost, cgiport, monkeypatch)

        self.args += ["config"] + server_type
        self.set_templates_dir(monkeypatch)
        self.set_python_path(monkeypatch)
        self.cli.invoke(self.args, strict=True)

        o, e = capsys.readouterr()
        o = self.normalise_generated(o)
        d = self.compare_with_reference(
            server_type[0] + '-withoptions.conf', o)
        assert not d, 'Files are different:\n' + d

    def testNginxLocationComment(self):
        """
        Check the example comment in nginx-location matches the recommended
        nginx configuration
        """

        def clean(refname):
            fn = path(__file__).dirname() / 'reference_templates' / refname
            out = []
            with open(fn) as f:
                for line in f:
                    if re.match('##\s*\w', line):
                        out.append(line[2:].strip())
                    elif re.match('[^#]\s*\w', line):
                        out.append(line.strip())
            return out

        diffs = list(unified_diff(
            clean('nginx.conf'), clean('nginx-location.conf'), n=0))
        assert diffs == [
            '--- \n',
            '+++ \n',
            '@@ -1,2 +0,0 @@\n',
            '-upstream engine {',
            '-server 127.0.0.1:4080 fail_timeout=0;',
            '@@ -7,0 +6 @@\n',
            '+include /opt/bhojpur/web/ode-web-location.include;',
            '@@ -19 +18 @@\n',
            '-proxy_pass http://engine;',
            '+proxy_pass http://127.0.0.1:4080;'
        ]
