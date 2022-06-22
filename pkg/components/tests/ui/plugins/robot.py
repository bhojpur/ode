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

from ode.cli import BaseControl, CLI
import sys
import os

HELP = "Robot framework configuration/deployment tools"

class RobotControl(BaseControl):

    def _configure(self, parser):
        sub = parser.sub()

        config = parser.add(
            sub, self.config,
            "Output a config template for the Robot framework")
        config.add_argument(
            "--config-file", type=str,
            help="Path to an ICE configuration file. Default: ICE_CONFIG")
        config.add_argument(
            "--protocol", type=str, default="http",
            help="Protocol to use for the Bhojpur ODE.web robot tests."
            " Default: http")
        config.add_argument(
            "--webhost", type=str,
            help="Web host to use for the Bhojpur ODE.web robot tests.")
        config.add_argument(
            "--remoteurl", type=str, default="",
            help="The url for a remote selenium server for example"
            " http://selenium_server_host/wd/hub. Default: False")
        config.add_argument(
            "--dc", type=str, default="",
            help="If you specify a value for remote you can also specify"
            " 'desired_capabilities' which is a string in the form"
            " key1:val1,key2:val2 that will be used to specify"
            " desired_capabilities to the remote server. This is useful"
            " for doing things like specify a proxy server or for specify"
            " browser and os.")

    def config(self, args):
        """Generate a configuration file for the Robot framework tests"""

        if args.config_file:
            init_args = ["--Ice.Config=%s" % args.config_file]
        else:
            init_args = ["--Ice.Config=%s" % os.environ.get('ICE_CONFIG', '')]
        import Ice
        p = Ice.initialize(init_args).getProperties()

        # Create dictionary of substitutions from ice.config file
        d = {
            "PORT": p.getPropertyWithDefault("ode.port", '4064'),
            "HOST": p.getPropertyWithDefault("ode.host", "localhost"),
            "USER": p.getPropertyWithDefault("ode.user", "root"),
            "PASS": p.getPropertyWithDefault("ode.pass", "ode"),
            "ROOTPASS": p.getPropertyWithDefault("ode.rootpass", "ode"),
            "PROTOCOL": args.protocol,
            "REMOTEURL": args.remoteurl,
            "DC": args.dc,
        }

        # Add ODE.web substitutions
        import urllib
        from odeweb import settings
        static_prefix = getattr(settings, 'FORCE_SCRIPT_NAME', '')
        d["WEBPREFIX"] = static_prefix
        d["QWEBPREFIX"] = urllib.quote(static_prefix, '')
        d["QSEP"] = urllib.quote('/', '')
        if args.webhost:
            d["WEBHOST"] = args.webhost
        elif settings.APPLICATION_SERVER not in settings.WSGI_TYPES:
            d["WEBHOST"] = "%s:%s" % (settings.APPLICATION_SERVER_HOST,
                                      settings.APPLICATION_SERVER_PORT)
        else:
            d["WEBHOST"] = d["HOST"]

        # Read robot.template file and substitute keywords
        c = file(self.ctx.dir / "etc" / "templates" / "robot.template").read()
        self.ctx.out(c % d)


try:
    register("robot", RobotControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("robot", RobotControl, HELP)
        cli.invoke(sys.argv[1:])
