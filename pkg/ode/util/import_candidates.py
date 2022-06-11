#!/usr/bin/env python
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
   Utility method for calling the equivalent of "ode import -f".
   Results are parsed when using as_dictionary.
"""

from builtins import str
import ode

from ode.util.temp_files import create_path, remove_path
from ode.cli import CLI

def _to_list(path):
    """
    Guarantees that a list of strings will be returned.
    Handles unicode caused by "%s" % path.path.
    """
    if isinstance(path, str) or isinstance(path, str):
        path = [str(path)]
        return path
    else:
        path = [str(x) for x in path]
        return path

def as_stdout(path, readers="", extra_args=None):
    """Returns the import candidates for the given path.

    you can pass more arguments to the `import` command through the
    extra_args argument in the form of a list.

    ..code ::

    >>> as_stdout("/my/dir/with_tifs", extra_args=["--depth", "6"])

    """
    if extra_args is None:
        extra_args = []
    path = _to_list(path)
    readers = str(readers)
    cli = CLI()
    cli.loadplugins()
    if readers:
        cli.invoke(["import", "-l"] + [readers,] + extra_args + ["-f"] + path)
    else:
        cli.invoke(["import"] + extra_args + ["-f"] + path)
    if cli.rv != 0:
        raise ode.InternalException(
            None, None, "'import -f' exited with a rc=%s. "
            "See console for more information" % cli.rv)

def as_dictionary(path, readers="", extra_args=None):
    """Run as_stdout, parses the output and returns a dictionary of the form::

    {
    some_file_in_group :
    [
    some_file_in_group
    some_other_file_in_group
    ...
    last_file_in_group
    ],
    some_file_in_second_group : ...
    }

    you can pass more arguments to the `import` command through the
    extra_args argument in the form of a list.

    to_import = as_dictionary("/my/dir/with_tifs", extra_args=["--depth", "6"])

    will go through the directories with a depth level of 6 instead of
    the default 4.  Arguments of interest might be: `debugging`,
    `report`, `logback`. Note that the command runs locally so options
    such as `--exclude` will not work as they need information from the server.
    """
    t = create_path("candidates", "err")

    path = _to_list(path)
    path.insert(0, "---file=%s" % t)
    try:
        as_stdout(path, readers=readers, extra_args=extra_args)
        f = open(str(t), "r")
        output = f.readlines()
        f.close()
    finally:
        remove_path(t)

    gline = -1
    key = None
    groups = {}
    for line in output:
        line = line.strip()
        if len(line) == 0:
            continue
        if line.startswith("#"):
            gline = -1
        else:
            if gline == -1:
                gline = 1
                key = line
                groups[key] = [line]
            else:
                groups[key].append(line)

    return groups

if __name__ == "__main__":
    import sys
    as_stdout(sys.argv[1:])