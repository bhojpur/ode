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
   load, quit, version, help plugins

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.

   The load plugin is used to read in files with ode cli commands
   (omitting the ode). For example,

   ./ode load some/file.osh

   The help, quit, and version plugins are self-explanatory.
"""

from __future__ import print_function

from past.builtins import cmp
from glob import glob
import sys

from collections import defaultdict

from ode.cli import BaseControl
from ode.cli import CLI
from ode.cli import VERSION
from ode.cli import ODEDIR

class QuitControl(BaseControl):

    def _configure(self, parser):
        parser.set_defaults(func=self.__call__)

    def __call__(self, args):
        self.ctx.exit("", newline=False)

class VersionControl(BaseControl):

    def _configure(self, parser):
        parser.set_defaults(func=self.__call__)

    def __call__(self, args):
        self.ctx.err("Bhojpur ODE version:")
        self.ctx.out(VERSION)
        server_version = None
        for line in self.ctx.get_config_property_lines(ODEDIR):
            line = str(line).strip()
            if line.startswith("ode.version="):
                server_version = line[len("ode.version="):]
        if server_version:
            self.ctx.err("Bhojpur ODE server version:")
            self.ctx.err(server_version)

LOAD_HELP = """Load file as if it were sent on standard in.

This can be used as the #! header of a file to make standand-alone script.

Examples:
    #!/usr/bin/env ode load
    login -C root@localhost
    group add new_group
    user add foo some user new_group

 or

    $ ode login       # login can't take place in HERE-document
    $ ode load <<EOF
    user list
    group list
    EOF

"""

class LoadControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument("infile", nargs="*")
        parser.add_argument(
            "-g", "--glob", action="store_true", default=False,
            help=("Input paths are shell globs that should be expanded and "
                  "sorted."))
        parser.add_argument(
            "-k", "--keep-going", action="store_true", default=False,
            help="Continue processing after an error.")
        parser.set_defaults(func=self.__call__)

    def _load_filehandle(self, fh, keep_going):
        for line in fh:
            self.ctx.invoke(line, strict=(not keep_going))

            if self.ctx.rv != 0:
                self.ctx.err("Ignoring error: %s" % line)
                self.ctx.rv = 0

    def __call__(self, args):
        stdout = False
        if args.glob:
            infiles = []
            for fileglob in args.infile:
                infiles.extend(glob(fileglob))
            infiles.sort()
        elif not args.infile:
            stdout = True
        else:
            infiles = args.infile

        if stdout:
            self._load_filehandle(sys.stdin, args.keep_going)
        else:
            for filename in infiles:
                self.ctx.dbg("Loading file %s" % filename)
                with open(filename, 'r') as fh:
                    self._load_filehandle(fh, args.keep_going)

class ShellControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument(
            "--login", action="store_true",
            help="Logins in and sets the 'client' variable")
        parser.add_argument("arg", nargs="*", help="Arguments for IPython.")
        parser.set_defaults(func=self.__call__)

    def __call__(self, args):
        """
        Copied from IPython embed-short example
        """
        import logging
        logging.basicConfig()
        from ode.util.upgrade_check import UpgradeCheck
        check = UpgradeCheck("shell")
        check.run()
        if check.isUpgradeNeeded():
            self.ctx.out("")

        ns = {}
        if args.login:
            import ode
            client = self.ctx.conn(args)
            ns = {"client": client, "ode": ode}

        try:
            # IPython 0.11
            from IPython import embed
            embed(user_ns=ns)
        except ImportError:
            from IPython.Shell import IPShellEmbed
            ipshell = IPShellEmbed(args.arg)
            ipshell(local_ns=ns)

HELP_USAGE = """usage: %(program_name)s <command> [options] args
See 'help <command>' or '<command> -h' for more information on syntax
Type 'quit' to exit

Available commands:
%(commands)s

Other help topics:
%(topics)s

For additional information, see:
https://docs.bhojpur.net/latest/ode/users/cli/index.html
Report bugs at https://forum.image.sc/tag/ode-cli
"""

class HelpControl(BaseControl):
    """
    Defined here since the background loading might be too
    slow to have all help available
    """

    def _configure(self, parser):
        self.__parser__ = parser  # For formatting later
        parser.set_defaults(func=self.__call__)
        parser.add_argument(
            "--recursive", action="store_true",
            help="Also print help for all subcommands")
        group = parser.add_mutually_exclusive_group()
        group.add_argument(
            "--all", action="store_true",
            help="Print help for all commands and topics")
        group.add_argument(
            "--list", action="store_true",
            help="Print list of all commands and subcommands")
        group.add_argument(
            "topic", nargs="?", help="Command or topic for more information")

    def _complete(self, text, line, begidx, endidx):
        """
        This is something of a hack. This should either be a part
        of the context interface, or we should put it somewhere
        in a utility. FIXME.
        """
        return self.ctx.completenames(text, line, begidx, endidx)

    def format_title(self, command, sep="-"):
        """Create heading for command or topic help"""
        self.ctx.out("\n" + command)
        self.ctx.out(sep * len(command) + "\n")

    def print_command_help(self, control, args):
        """Print help for a single command and optionally its subcommand"""
        self.ctx.invoke([control, "-h"])
        if args.recursive:
            subcommands = self.ctx.controls[control].get_subcommands()
            for subcommand in subcommands:
                self.format_title(control + " " + subcommand, sep="^")
                self.ctx.invoke([control, subcommand, "-h"])

    def print_usage(self):
        commands, topics = [
            self.__parser__._format_list(x) for x in
            [sorted(self.ctx.controls), sorted(self.ctx.topics)]]
        key_list = {
            "program_name": sys.argv[0],
            "version": VERSION,
            "commands": commands,
            "topics": topics}
        print(HELP_USAGE % key_list)

    def print_single_command_or_topic(self, args):
        """Print the help for a command or a topic"""
        if args.topic in self.ctx.controls:
            self.print_command_help(args.topic, args)
        elif args.topic in self.ctx.topics:
            self.ctx.out(self.ctx.topics[args.topic])
        else:
            self.ctx.err("Unknown help topic or command: %s" % args.topic)

    def print_all_commands_and_topics(self, args):
        """Print the help for all commands and topics"""

        for control in sorted(self.ctx.controls):
            self.format_title(control)
            self.print_command_help(control, args)

        for topic in sorted(self.ctx.topics):
            self.format_title(topic)
            self.ctx.out(self.ctx.topics[topic])

    def print_commands_list(self, args):
        """Print a list of all commands"""

        for control in sorted(self.ctx.controls):
            subcommands = self.ctx.controls[control].get_subcommands()
            if subcommands:
                self.ctx.out("%s (%s)" % (control, len(subcommands)))
                for subcommand in subcommands:
                    self.ctx.out("\t%s" % subcommand)
            else:
                self.ctx.out("%s" % control)

    def __call__(self, args):

        self.ctx.waitForPlugins()

        # Fail-fast and print usage if no arg is passed
        if not args.all and not args.list and not args.topic:
            self.print_usage()

        if args.all:
            self.print_all_commands_and_topics(args)
        elif args.list:
            self.print_commands_list(args)
        elif args.topic:
            self.print_single_command_or_topic(args)

class ErrorsControl(BaseControl):

    def _configure(self, parser):
        parser.set_defaults(func=self.__call__)
        parser.add_argument("--length", default=50, type=int,
                            help="Length of message to print")
        parser.add_argument("plugins", nargs="*", default=(),
                            help="Limit to these plugins; otherwise all")

    def __call__(self, args):
        arranged = defaultdict(lambda: defaultdict(
            lambda: defaultdict(list)))
        for name, control in list(self.ctx.controls.items()):
            if not args.plugins or name in args.plugins:
                combined = []
                if hasattr(control, "get_errors"):
                    combined.extend(list(control.get_errors().items()))
                    combined.sort(key=lambda x: x[1].rcode)
                    for key, err in combined:
                        arranged[err.rcode][name][key].append(err)

        for rcode, names in sorted(arranged.items()):
            for name, keys in sorted(names.items()):
                for key, errors in sorted(keys.items()):
                    for err in errors:
                        msg = err.msg
                        if len(msg) > (args.length+1):
                            msg = msg[:args.length] + "..."
                        msg = msg.replace("\n", " ")
                        msg = msg.strip()
                        t = (err.rcode, name, key, msg)
                        self.ctx.out("%5d\t%10s\t%10s\t'%s'" % t)

controls = {
    "help": (HelpControl, "Syntax help for all commands"),
    "quit": (QuitControl, "Quit application"),
    "errors": (ErrorsControl, "Display all plugin error codes"),
    "shell": (ShellControl, """Starts an IPython interpreter session

All arguments not understood vi %(prog)s will be passed to the shell.
Use "--" to end parsing, e.g. '%(prog)s -- --help' for IPython help"""),
    "version": (VersionControl, "Version number"),
    "load": (LoadControl, LOAD_HELP)}

try:
    for k, v in list(controls.items()):
        register(k, v[0], v[1])
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        for k, v in list(controls.items()):
            cli.register(k, v[0], v[1])
        cli.invoke(sys.argv[1:])