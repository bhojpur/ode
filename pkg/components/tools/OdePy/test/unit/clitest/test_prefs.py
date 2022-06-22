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

"""
   Test of the scripts plugin
"""

import pytest
from ode.cli import CLI, NonZeroReturnCode
from ode.config import ConfigXml
from ode.install.config_parser import PropertyParser
from ode.plugins.prefs import PrefsControl, HELP
from ode.util.temp_files import create_path

@pytest.fixture
def configxml(monkeypatch):
    class MockConfigXml(object):
        def __init__(self, path, **kwargs):
            pass

        def as_map(self):
            return {}

        def close(self):
            pass
    monkeypatch.setattr("ode.config.ConfigXml", MockConfigXml)


class TestPrefs(object):

    def setup_method(self, method):
        self.cli = CLI()
        self.cli.register("config", PrefsControl, HELP)
        self.p = create_path()
        self.args = ["-d1", "config", "--source", "%s" % self.p]

    def config(self):
        return ConfigXml(filename=str(self.p))

    def assertStdoutStderr(self, capsys, out='', err='', strip_warning=False):
        o, e = capsys.readouterr()
        if strip_warning:
            assert(e.startswith('WARNING: '))
            e = '\n'.join(e.split('\n')[1:])
        assert (o.strip() == out and
                e.strip() == err)

    def invoke(self, s):
        if isinstance(s, basestring):
            s = s.split()
        self.cli.invoke(self.args + s, strict=True)

    def testHelp(self):
        self.invoke("-h")
        assert 0 == self.cli.rv

    @pytest.mark.parametrize('subcommand', PrefsControl().get_subcommands())
    def testSubcommandHelp(self, subcommand):
        self.invoke("%s -h" % subcommand)
        assert 0 == self.cli.rv

    def testAll(self, capsys):
        config = self.config()
        config.default("test")
        config.close()
        self.invoke("all")
        self.assertStdoutStderr(capsys, out="test\ndefault")

    def testDefaultInitial(self, capsys):
        self.invoke("def")
        self.assertStdoutStderr(capsys, out="default")

    def testDefaultEnvironment(self, capsys, monkeypatch):
        monkeypatch.setenv("ODE_CONFIG", "testDefaultEnvironment")
        self.invoke("def")
        self.assertStdoutStderr(capsys, out="testDefaultEnvironment")

    def testDefaultSet(self, capsys):
        self.invoke("def x")
        self.assertStdoutStderr(capsys, out="x")
        self.invoke("def")
        self.assertStdoutStderr(capsys, out="x")

    def testGetSet(self, capsys):
        self.invoke("get X")
        self.assertStdoutStderr(capsys)
        self.invoke("set A B")
        self.assertStdoutStderr(capsys)
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='B')
        self.invoke("get")
        self.assertStdoutStderr(capsys, out='A=B')
        self.invoke("set A")
        self.assertStdoutStderr(capsys)
        self.invoke("keys")
        self.assertStdoutStderr(capsys)

    def testGetHidePassword(self, capsys):
        config = {
            "ode.X.mypassword": "long_password",
            "ode.X.pass": "shortpass",
            "ode.X.password": "medium_password",
            "ode.X.regular": "value",
            "ode.Y.MyPassword": "long_password",
            "ode.Y.Pass": "shortpass",
            "ode.Y.Password": "medium_password",
            "ode.Z.mypassword": "",
            "ode.Z.pass": "",
            "ode.Z.password": ""}
        output_hidden_password = (
            'ode.X.mypassword=********\n'
            'ode.X.pass=********\n'
            'ode.X.password=********\n'
            'ode.X.regular=value\n'
            'ode.Y.MyPassword=********\n'
            'ode.Y.Pass=********\n'
            'ode.Y.Password=********\n'
            'ode.Z.mypassword=\n'
            'ode.Z.pass=\n'
            'ode.Z.password=')
        output_with_password = (
            'ode.X.mypassword=long_password\n'
            'ode.X.pass=shortpass\n'
            'ode.X.password=medium_password\n'
            'ode.X.regular=value\n'
            'ode.Y.MyPassword=long_password\n'
            'ode.Y.Pass=shortpass\n'
            'ode.Y.Password=medium_password\n'
            'ode.Z.mypassword=\n'
            'ode.Z.pass=\n'
            'ode.Z.password=')

        for k, v in config.iteritems():
            self.cli.invoke(self.args + ["set", k, v], strict=True)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out=output_hidden_password)
        self.invoke("get --show-password")
        self.assertStdoutStderr(capsys, out=output_with_password)
        self.invoke("list")
        self.assertStdoutStderr(capsys, out=output_hidden_password,
                                strip_warning=True)
        self.invoke("list --show-password")
        self.assertStdoutStderr(capsys, out=output_with_password,
                                strip_warning=True)

    @pytest.mark.parametrize('argument', ['A=B', 'A= B'])
    def testSetFails(self, capsys, argument):
        self.invoke("set %s" % argument)
        self.assertStdoutStderr(
            capsys, err="\"=\" in key name. Did you mean \"...set A B\"?")

    def testKeys(self, capsys):
        self.invoke("keys")
        self.assertStdoutStderr(capsys)
        self.invoke("set A B")
        self.assertStdoutStderr(capsys)
        self.invoke("keys")
        self.assertStdoutStderr(capsys, out="A")
        self.invoke("set C D=E")
        self.assertStdoutStderr(capsys)
        self.invoke("keys")
        self.assertStdoutStderr(capsys, out="A\nC")

    def testVersion(self, capsys):
        self.invoke("version")
        self.assertStdoutStderr(capsys, out=ConfigXml.VERSION)

    def testPath(self, capsys):
        self.invoke("path")
        self.assertStdoutStderr(capsys, out=self.p)

    def testLoad(self, capsys):
        to_load = create_path()
        to_load.write_text("A=B")
        self.invoke("load %s" % to_load)
        self.assertStdoutStderr(capsys)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=B")

        # Same property/value pairs should pass
        self.invoke("load %s" % to_load)

        to_load.write_text("A=C")
        with pytest.raises(NonZeroReturnCode):
            # Different property/value pair should fail
            self.invoke("load %s" % to_load)
        self.assertStdoutStderr(
            capsys, err="Duplicate property: A ('B' => 'C')")

        # Quiet load
        self.invoke("load -q %s" % to_load)
        self.assertStdoutStderr(capsys)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=C")

    def testLoadDoesNotExist(self):
        # ticket:7273
        pytest.raises(NonZeroReturnCode, self.invoke,
                      "load THIS_FILE_SHOULD_NOT_EXIST")

    def testLoadMultiLine(self, capsys):
        to_load = create_path()
        to_load.write_text("A=B\\\nC")
        self.invoke("load %s" % to_load)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=BC")

    @pytest.mark.parametrize(
        'validkeyvalue',
        [('A', 'B'), ('A', 'B=C'), ('A.B', 'C.D'), ('A.B', "'C.D'")])
    def testLoadWhitelist(self, capsys, validkeyvalue):
        valid_key, valid_value = validkeyvalue
        to_load = create_path()
        to_load.write_text("%s=%s\n" % (valid_key, valid_value))
        self.invoke("load %s" % to_load)
        self.invoke("get %s" % valid_key)
        self.assertStdoutStderr(capsys, out=valid_value)

    @pytest.mark.parametrize(
        ('invalidline', 'invalidkey'),
        [('E F G', 'E F G'),
         ('E!F=G', 'E!F'),
         ('E = F', 'E')])
    def testLoadInvalidKey(self, capsys, invalidline, invalidkey):
        self.invoke("set A B")
        self.assertStdoutStderr(capsys)

        to_load = create_path()
        to_load.write_text("C=D\n%s\nH=I\n" % invalidline)
        with pytest.raises(NonZeroReturnCode):
            self.invoke("load %s" % to_load)
        self.assertStdoutStderr(
            capsys, err="Illegal property name: %s" % invalidkey)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=B")

    @pytest.mark.parametrize(
        'valid_key_value',
        [('A', 'B'), ('A', 'B=C'), ('A.B', 'C.D'), ('A.B', "'C.D'")])
    def testSetWhitelist(self, capsys, valid_key_value):
        valid_key, valid_value = valid_key_value
        self.invoke(["set", valid_key, valid_value])
        self.invoke(["get", valid_key])
        self.assertStdoutStderr(capsys, out=valid_value)

    @pytest.mark.parametrize('invalid_key', ['E F', 'E!F', 'E '])
    def testSetInvalidKey(self, capsys, invalid_key):
        with pytest.raises(NonZeroReturnCode):
            self.invoke(["set", invalid_key, "test"])
        self.assertStdoutStderr(
            capsys, err="Illegal property name: %s" % invalid_key.strip())

    def testSetFromFile(self, capsys):
        to_load = create_path()
        to_load.write_text("Test")
        self.invoke("set -f %s A" % to_load)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=Test")

    def testDrop(self, capsys):
        self.invoke("def x")
        self.assertStdoutStderr(capsys, out="x")
        self.invoke("def")
        self.assertStdoutStderr(capsys, out="x")
        self.invoke("all")
        self.assertStdoutStderr(capsys, out="x\ndefault")
        self.invoke("def y")
        self.assertStdoutStderr(capsys, out="y")
        self.invoke("all")
        self.assertStdoutStderr(capsys, out="y\nx\ndefault")
        self.invoke("drop x")
        self.assertStdoutStderr(capsys)
        self.invoke("all")
        self.assertStdoutStderr(capsys, 'y\ndefault')

    def testDropFails(self, capsys):
        self.invoke("drop x")
        self.assertStdoutStderr(capsys, err="Unknown configuration: x")

    def testEdit(self):
        """
        Testing edit is a bit more complex since it wants to
        start another process. Rather than using invoke, we
        manage things ourselves here.
        """
        def fake_edit_path(tmp_file, tmp_text):
            pass
        args = self.cli.parser.parse_args("config edit".split())
        control = self.cli.controls["config"]
        config = self.config()
        try:
            control.edit(args, config, fake_edit_path)
        finally:
            config.close()

    def testNewEnvironment(self, capsys, monkeypatch):
        config = self.config()
        config.default("default")
        config.close()
        monkeypatch.setenv("ODE_CONFIG", "testNewEnvironment")
        self.invoke("set A B")
        self.assertStdoutStderr(capsys)
        self.invoke("get")
        self.assertStdoutStderr(capsys, out="A=B")

    @pytest.mark.parametrize(
        ('initval', 'newval'),
        [('1', '2'), ('\"1\"', '\"2\"'), ('test', 'test')])
    def testAppendFails(self, initval, newval):
        self.invoke("set A %s" % initval)
        with pytest.raises(NonZeroReturnCode):
            self.invoke("append A %s" % newval)

    def testRemoveUnsetPropertyFails(self):
        with pytest.raises(NonZeroReturnCode):
            self.invoke("remove A x")

    @pytest.mark.parametrize(
        ('initval', 'newval'),
        [('1', '1'), ('[\"1\"]', '1'), ('[1]', '\"1\"')])
    def testRemoveFails(self, initval, newval):
        self.invoke("set A %s" % initval)
        with pytest.raises(NonZeroReturnCode):
            self.invoke("remove A %s" % newval)

    @pytest.mark.parametrize('report', ['--report', ''])
    def testAppendRemove(self, report, capsys):
        self.invoke("append %s A 1" % report)
        self.assertReportStdout(report, capsys, 'Appended A:1')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1]')
        self.invoke("append %s A \"y\"" % report)
        self.assertReportStdout(report, capsys, 'Appended A:"y"')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1, "y"]')
        self.invoke("remove %s A \"y\"" % report)
        self.assertReportStdout(report, capsys, 'Removed A:"y"')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1]')
        self.invoke("remove %s A 1" % report)
        self.assertReportStdout(report, capsys, 'Removed A:1')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[]')

    def assertReportStdout(self, report, capsys, out):
        if report and out:
            self.assertStdoutStderr(capsys, out='Changed: %s' % out)
        else:
            self.assertStdoutStderr(capsys, out='')

    @pytest.mark.parametrize('report', ['--report', ''])
    def testAppendSet(self, report, capsys):
        self.invoke("append %s --set A 1" % report)
        self.assertReportStdout(report, capsys, 'Appended A:1')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1]')
        self.invoke("append %s --set A 2" % report)
        self.assertReportStdout(report, capsys, 'Appended A:2')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1, 2]')
        self.invoke("append %s --set A 1" % report)
        self.assertReportStdout(report, capsys, '')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1, 2]')
        self.invoke("append %s A 1" % report)
        self.assertReportStdout(report, capsys, 'Appended A:1')
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1, 2, 1]')

    def testRemoveIdenticalValues(self, capsys):
        self.invoke("set A [1,1]")
        self.invoke("remove A 1")
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[1]')
        self.invoke("remove A 1")
        self.invoke("get A")
        self.assertStdoutStderr(capsys, out='[]')

    @pytest.mark.usefixtures('configxml')
    def testAppendWithDefault(self, monkeypatch, capsys):
        import json
        monkeypatch.setattr("engine.settings.CUSTOM_SETTINGS_MAPPINGS", {
            "ode.web.test": ["TEST", "[1,2,3]", json.loads],
            "ode.web.notalist": ["NOTALIST", "abc", str],
        })

        self.invoke("append ode.web.test 4")
        self.invoke("get ode.web.test")
        self.assertStdoutStderr(capsys, out='[1, 2, 3, 4]')
        self.invoke("append --set ode.web.test 2")
        self.assertStdoutStderr(capsys, out='')
        self.invoke("get ode.web.test")
        self.assertStdoutStderr(capsys, out='[1, 2, 3, 4]')

        self.invoke("append ode.web.unknown 1")
        self.invoke("get ode.web.unknown")
        self.assertStdoutStderr(capsys, out='[1]')
        with pytest.raises(NonZeroReturnCode):
            self.invoke("append ode.web.notalist 1")

    @pytest.mark.usefixtures('configxml')
    def testRemoveWithDefault(self, monkeypatch, capsys):
        import json
        monkeypatch.setattr("engine.settings.CUSTOM_SETTINGS_MAPPINGS", {
            "ode.web.test": ["TEST", "[1,2,3]", json.loads],
        })
        self.invoke("remove ode.web.test 2")
        self.invoke("get ode.web.test")
        self.assertStdoutStderr(capsys, out='[1, 3]')
        self.invoke("remove ode.web.test 1")
        self.invoke("remove ode.web.test 3")
        self.invoke("get ode.web.test")
        self.assertStdoutStderr(capsys, out='[]')

    @pytest.mark.parametrize("data", (
        ({}, ""),
        ({"a": "b"}, "a=b"),
        ({"a": "b", "a": "d"}, "a=d"),
        ({"a": "b", "c": "d"}, "a=b\nc=d"),
        ({"c": "d", "a": "b"}, "a=b\nc=d"),
    ))
    @pytest.mark.usefixtures('configxml')
    def testList(self, data, monkeypatch, capsys):
        for k, v in data[0].items():
            self.invoke("set %s %s" % (k, v))
        self.invoke("list")
        self.assertStdoutStderr(capsys, out=data[1], strip_warning=True)

    @pytest.mark.parametrize("data", (
        ("ode.a=b\node.c=d\n##ignore=me\n",
         "ode.a=b\node.c=d",
         "a (1)\n\t\nc (1)"),
        ("ode.whitelist=\\\nome.foo,\\\nome.bar\n### END",
         "ode.whitelist=ome.foo,ome.bar",
         "whitelist (1)"),
        ("ode.whitelist=\\\nome.foo,\\\nome.bar\n",
         "ode.whitelist=ome.foo,ome.bar",
         "whitelist (1)"),
        ("ode.whitelist=\\\nome.foo,\\\nome.bar",
         "ode.whitelist=ome.foo,ome.bar",
         "whitelist (1)"),
        ("ode.user_mapping=\\\na=b,c=d",
         "ode.user_mapping=a=b,c=d",
         "user_mapping (1)"),
        ("ode.whitelist=ome.foo\nIce.c=d\n",
         "Ice.c=d\node.whitelist=ome.foo",
         "whitelist (1)"),
        ("ode.a=b\node.c=d\node.e=f\n##ignore=me\n",
         "ode.a=b\node.c=d\node.e=f",
         "a (1)\n\t\nc (1)\n\t\ne (1)"),
        ("ode.a=b\node.c=d\node.e=f\n##ignore=me\n",
         "ode.a=b\node.c=d\node.e=f",
         "a (1)\n\t\nc (1)\n\t\ne (1)"),
    ))
    def testFileParsing(self, tmpdir, capsys, data):
        input, defaults, keys = data
        cfg = tmpdir.join("test.cfg")
        cfg.write(input)
        self.invoke("parse --file=%s --no-web" % cfg)
        self.assertStdoutStderr(capsys, out=defaults)
        self.invoke("parse --file=%s --defaults --no-web" % cfg)
        self.assertStdoutStderr(capsys, out=defaults)
        self.invoke("parse --file=%s --keys --no-web" % cfg)
        self.assertStdoutStderr(capsys, out=keys)

    @pytest.mark.parametrize("data", (
        (u"ode.ldap.base=ou=ascii\n", "ascii2"),
        (u"ode.ldap.base=ou=ascii\n", "unicodé"),
        (u"ode.ldap.base=ou=unicodé\n", "ascii"),
        (u"ode.ldap.base=ou=unicodé\n", "unicodé2"),
    ))
    def testUnicode(self, tmpdir, capsys, data):
        input, update = data
        cfg = tmpdir.join("test.cfg")
        cfg.write(input.encode("utf-8"), "wb")
        self.invoke("load %s" % cfg)
        self.invoke("get ode.ldap.base")
        self.invoke("set ode.ldap.base %s" % update)
        self.invoke("get ode.ldap.base")

    @pytest.mark.xfail
    def testConfigPropertyParser(self, tmpdir):
        cfg = tmpdir.join("test.properties")
        s = "a=1\nb.c=a b <!> c\nd.e=line1\\\nline2\nf.g=\\n\n"
        cfg.write(s)
        pp = PropertyParser()
        props = pp.parse_file(str(cfg))

        # Fails, the last two properties are parsed as one:
        # 'd.e' = 'line1line2f.g=\\n'
        assert len(props) == 4
        assert props[0].key == 'a'
        assert props[0].val == '1'
        assert props[1].key == 'b.c'
        assert props[1].val == 'a b <!> c'
        assert props[2].key == 'd.e'
        assert props[2].val == 'line1line2'
        assert props[3].key == 'f.g'
        assert props[3].val == '\\n'
