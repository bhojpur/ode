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
#
# General build scripts.

from builtins import str
import os
import sys
import subprocess

BUILD_PY = "-Dbuild.py=true"

def popen(args, stdin=None, stdout=subprocess.PIPE, stderr=subprocess.PIPE):
    copy = os.environ.copy()
    return subprocess.Popen(args,
                            env=copy,
                            stdin=stdin,
                            stdout=stdout,
                            stderr=stderr)


def execute(args):
    p = popen(args, stdout=sys.stdout, stderr=sys.stderr)
    rc = p.wait()
    if rc != 0:
        sys.exit(rc)

def notification(msg, prio):
    """
    Provides UI notification.
    """

    # May want to revert this to be ODE_BUILD_NOTIFICATION, or whatever.
    if "ODE_QUIET" in os.environ or sys.platform == "win32":
        return

    try:
        p = popen(["growlnotify", "-t", "ODE Build Status", "-p",
                   str(prio)], stdin=subprocess.PIPE)
        p.communicate(msg)
        rc = p.wait()
        if rc != 0:
            pass  # growl didn't work
    except OSError:
        pass  # No growlnotify found, may want to use another tool

def java_ode(args):
    command = [find_java()]
    p = os.path.join(os.path.curdir, "lib", "log4j-build.xml")
    command.append("-Dlog4j.configuration=%s" % p)
    command.append(BUILD_PY)
    command.extend(calculate_memory_args())
    command.extend(["ode"])
    if isinstance(args, str):
        command.append(args)
    else:
        command.extend(args)
    execute(command)


def find_java():
    return "java"


def calculate_memory_args():
    return (
        "-Xmx600M",
        "-XX:MaxPermSize=256m",
        "-XX:+IgnoreUnrecognizedVMOptions"
    )

def handle_tools(args):
    _ = os.path.sep.join
    additions = []
    mappings = {
        "-top": _(["build.xml"]),
        "-fs": _(["components", "tools", "OdeFS", "build.xml"]),
        "-java": _(["components", "tools", "OdeJava", "build.xml"]),
        "-py": _(["components", "tools", "OdePy", "build.xml"]),
        "-web": _(["components", "tools", "OdeWeb", "build.xml"]),
    }
    while len(args) > 0 and args[0] in list(mappings.keys())+["-perf"]:
        if args[0] == "-perf":
            args.pop(0)
            A = ["-listener",
                 "net.sf.antcontrib.perf.AntPerformanceListener"]
            additions.extend(A)
        elif args[0] in list(mappings.keys()):
            F = mappings[args.pop(0)]
            A = ["-f", F]
            additions.extend(A)
    return additions + args


def handle_relative(args):
    """
    If no other specific file has been requested,
    then use whatever relative path is needed to
    specify build.xml in the local directory.

    Regardless, os.chdir is called to the top.
    """
    additions = []
    this = os.path.abspath(__file__)
    this_dir = os.path.abspath(os.path.join(this, os.pardir))
    cwd = os.path.abspath(os.getcwd())
    os.chdir(this_dir)
    if "-f" not in args:
        build_xml = os.path.join(cwd, "build.xml")
        if os.path.exists(build_xml):
            additions.append("-f")
            additions.append(build_xml)
    return additions + args


if __name__ == "__main__":
    #
    # use java_ode which will specially configure the build system.
    #
    args = list(sys.argv)
    args.pop(0)

    # Unset CLASSPATH, since this breaks the build
    if os.environ.get('CLASSPATH'):
        del os.environ['CLASSPATH']

    try:
        args = handle_tools(args)
        args = handle_relative(args)
        java_ode(args)
        notification(""" Finished: %s """ % " ".join(args), 0)
    except KeyboardInterrupt:
        sys.stderr.write("\nCancelled by user\n")
        sys.exit(2)
    except SystemExit as se:
        notification(""" Failed: %s """ % " ".join(args), 100)
        sys.exit(se.code)