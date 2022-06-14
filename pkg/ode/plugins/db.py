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
   Plugin for our managing the Bhojpur ODE database.

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

from __future__ import division

from future.utils import bytes_to_native_str
from future.utils import isbytes
from past.utils import old_div
from ode.cli import BaseControl
from ode.cli import CLI

from argparse import FileType, SUPPRESS

from ode.install.windows_warning import windows_warning, WINDOWS_WARNING

from extras.path import path

import ode.java
import platform
import sys
import time

if sys.version_info >= (3, 0, 0):
    # Keep str behavior on Python 2
    from builtins import str


HELP = """Database tools for creating scripts, setting passwords, etc."""


if platform.system() == 'Windows':
    HELP += ("\n\n%s" % WINDOWS_WARNING)


class DatabaseControl(BaseControl):

    def _configure(self, parser):
        sub = parser.sub()

        script = sub.add_parser(
            "script", help="Generates a DB creation script")
        script.set_defaults(func=self.script)
        try:
            # Python 3
            ft = FileType(mode="w", encoding='utf-8')
        except TypeError:
            ft = FileType(mode="w")
        script.add_argument(
            "-f", "--file", type=ft,
            help="Optional file to save to. Use '-' for stdout.")

        script.add_argument("posversion", nargs="?", help=SUPPRESS)
        script.add_argument("pospatch", nargs="?", help=SUPPRESS)
        script.add_argument("pospassword", nargs="?", help=SUPPRESS)

        script.add_argument("--version", help=SUPPRESS)
        script.add_argument("--patch", help=SUPPRESS)
        script.add_argument("--password", help="Bhojpur ODE root password")

        pw = sub.add_parser(
            "password",
            help="Prints SQL command for updating your root password")
        pw.set_defaults(func=self.password)
        pw_spec = pw.add_mutually_exclusive_group()
        pw_spec.add_argument("password", nargs="?")
        pw_spec.add_argument("--empty", action="store_true",
                             help=("Remove the password, "
                                   "allowing any for login when guest."))
        pw.add_argument("--user-id",
                        help="User ID to salt into the password. "
                        "Defaults to '0', i.e. 'root'",
                        default="0")

        for x in (pw, script):
            x.add_argument(
                "--no-salt", action="store_true",
                help="Disable the salting of passwords")

    def _lookup(self, key, defaults, args):
        """
        Get a value from a flag arg, positional arg, or default properties
        """
        propname = "ode.db." + key
        vdef = defaults.properties.getProperty(propname)
        varg = getattr(args, key)
        vpos = getattr(args, 'pos' + key)
        if varg:
            if vpos:
                self.ctx.die(
                    1, "ERROR: Flag and positional argument given for %s" % key
                    )
            v = varg
        elif vpos:
            v = vpos
        elif vdef:
            v = vdef
        else:
            self.ctx.die(1, "No value found for %s" % propname)
        self.ctx.err("Using %s for %s" % (v, key))
        return v

    def _has_user_id(self, args):
        return args and "user_id" in args and args.user_id is not None

    def _get_password_hash(self, args, root_pass=None, old_prompt=False):

        prompt = " for Bhojpur ODE "
        if self._has_user_id(args) and not old_prompt:
            prompt += "user %s" % args.user_id
        else:
            prompt += "root user"
        root_pass = self._ask_for_password(prompt, root_pass)

        jars = str(self.ctx.dir / "lib" / "server") + "/*"
        cmd = ["ode.security.auth.PasswordUtil", root_pass]
        if not args.no_salt and self._has_user_id(args):
            cmd.append(args.user_id)
        p = ode.java.popen(["-cp", jars] + cmd)
        rc = p.wait()
        if rc != 0:
            out, err = p.communicate()
            self.ctx.die(rc, "PasswordUtil failed: %s" % err.decode(
                errors='replace'))
        value = p.communicate()[0]
        if not value or len(value) == 0:
            self.ctx.die(100, "Encoded password is empty")
        return value.strip().decode()

    def _copy(self, input_path, output, func, cfg=None):
            try:
                input = open(str(input_path), encoding='utf-8')
            except TypeError:
                input = open(str(input_path))
            try:
                for s in input:
                        try:
                            if cfg:
                                output.write(func(s) % cfg)
                            else:
                                output.write(func(s))
                        except Exception as e:
                            self.ctx.dbg(str(e))
                            self.ctx.die(
                                154, "Failed to map line: %s\nError: %s"
                                % (s, e))
            finally:
                input.close()

    def _make_replace(self, root_pass, db_vers, db_patch):
        def fix(str_in):
            if isbytes(str_in):
                str_in = bytes_to_native_str(str_in)
            return str_in
        def replace_method(str_in):
            str_out = str_in.replace("@ROOTPASS@", fix(root_pass))
            str_out = str_out.replace("@DBVERSION@", fix(db_vers))
            str_out = str_out.replace("@DBPATCH@", fix(db_patch))
            return str_out
        return replace_method

    def _db_profile(self):
        from ode.install.config_parser import PropertyParser
        property_lines = self.ctx.get_config_property_lines(self.dir)
        for property in PropertyParser().parse_lines(property_lines):
            if property.key == 'ode.db.profile':
                return property.val
        raise KeyError('Configuration key not set: ode.db.profile')

    def _sql_directory(self, db_vers, db_patch):
        """
        See #2689
        """
        dbprofile = self._db_profile()
        sql_directory = self.ctx.dir / "sql" / dbprofile / \
            ("%s__%s" % (db_vers, db_patch))
        if not sql_directory.exists():
            self.ctx.die(2, "Invalid Database version/patch: %s does not"
                         " exist" % sql_directory)
        return sql_directory

    def _create(self, sql_directory, db_vers, db_patch, password_hash, args,
                location=None):
        sql_directory = self._sql_directory(db_vers, db_patch)
        if not sql_directory.exists():
            self.ctx.die(2, "Invalid Database version/patch: %s does not"
                         " exist" % sql_directory)

        if args and args.file:
            output = args.file
            script = "<filename here>"
        else:
            script = "%s__%s.sql" % (db_vers, db_patch)
            location = old_div(path.getcwd(), script)
            try:
                output = open(location, 'w', encoding='utf-8')
            except TypeError:
                output = open(location, 'w')
            self.ctx.out("Saving to " + location)

        try:
            dbprofile = self._db_profile()
            header = old_div(sql_directory, ("%s-header.sql" % dbprofile))
            footer = old_div(sql_directory, ("%s-footer.sql" % dbprofile))
            if header.exists():
                # 73 multiple DB support. ODE 4.3+
                cfg = {
                    "TIME": time.ctime(time.time()),
                    "DIR": sql_directory,
                    "SCRIPT": script}
                self._copy(header, output, str, cfg)
                self._copy(old_div(sql_directory,"schema.sql"), output, str)
                self._copy(old_div(sql_directory,"views.sql"), output, str)
                self._copy(
                    footer, output,
                    self._make_replace(password_hash, db_vers, db_patch), cfg)
            else:
                # ODE 4.2.x and before
                output.write("""
--
-- GENERATED %s from %s
--
-- This file was created by the `ode db script` command
-- and contains an MD5 version of your Bhojpur ODE root users's password.
-- You should think about deleting it as soon as possible.
--
-- To create your database:
--
--     createdb ode
--     psql ode < %s
--

BEGIN;
                """ % (time.ctime(time.time()), sql_directory, script))
                self._copy(old_div(sql_directory,"schema.sql"), output, str)
                self._copy(
                    old_div(sql_directory,"data.sql"), output,
                    self._make_replace(password_hash, db_vers, db_patch))
                self._copy(old_div(sql_directory,"views.sql"), output, str)
                output.write("COMMIT;\n")

        finally:
            output.flush()
            if output != sys.stdout:
                output.close()

    @windows_warning
    def password(self, args):
        root_pass = None
        user_id = 0
        old_prompt = True
        if self._has_user_id(args):
            user_id = args.user_id
            if user_id != '0':  # For non-root, use new_prompt
                old_prompt = False
        try:
            root_pass = args.password
        except Exception as e:
            self.ctx.dbg("While getting arguments:" + str(e))
        if args.empty:
            password_hash = ""
        else:
            password_hash = self._get_password_hash(args, root_pass,
                                                    old_prompt)
        self.ctx.out("UPDATE password SET hash = '%s' "
                     "WHERE experimenter_id = %s;""" %
                     (password_hash, user_id))

    @windows_warning
    def loaddefaults(self):
        try:
            data2 = self.ctx.initData({})
            output = self.ctx.readDefaults()
            self.ctx.parsePropertyFile(data2, output)
        except Exception as e:
            self.ctx.dbg(str(e))
            data2 = None
        return data2

    @windows_warning
    def script(self, args):
        if args.posversion is not None:
            self.ctx.err("WARNING: Positional arguments are deprecated")
        if platform.system() == 'Windows':
            self.ctx.out("\n%s\n" % WINDOWS_WARNING)

        defaults = self.loaddefaults()
        db_vers = self._lookup("version", defaults, args)
        db_patch = self._lookup("patch", defaults, args)
        root_pass = args.password
        if root_pass:
            if args.pospassword:
                self.ctx.die(
                    1, "ERROR: Flag and positional argument given for password"
                    )
        else:
            root_pass = args.pospassword
        if root_pass:
            self.ctx.err("Using password from commandline")

        args.user_id = "0"
        sql = self._sql_directory(db_vers, db_patch)
        pwhash = self._get_password_hash(args, root_pass, True)
        self._create(sql, db_vers, db_patch, pwhash, args)

try:
    register("db", DatabaseControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("db", DatabaseControl, HELP)
        cli.invoke(sys.argv[1:])