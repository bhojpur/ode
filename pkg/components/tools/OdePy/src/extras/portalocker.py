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

# portalocker.py - Cross-platform (posix/nt) API for flock-style file locking.

"""Cross-platform (posix/nt) API for flock-style file locking.

Synopsis:

   import portalocker
   file = open("somefile", "r+")
   portalocker.lock(file, portalocker.LOCK_EX)
   file.seek(12)
   file.write("foo")
   file.close()

If you know what you're doing, you may choose to

   portalocker.unlock(file)

before closing the file, but why?

Methods:

   lock( file, flags )
   unlock( file )

Constants:

   LOCK_EX
   LOCK_SH
   LOCK_NB

Exceptions:

    LockException

Notes:

For the 'nt' platform, this module requires the Python Extensions for Windows.
Be aware that this may not work as expected on Windows 95/98/ME.
"""

from __future__ import print_function
from __future__ import absolute_import

__all__ = [
    "lock",
    "unlock",
    "LOCK_EX",
    "LOCK_SH",
    "LOCK_NB",
    "LockException",
]

import os

class LockException(Exception):
    # Error codes:
    LOCK_FAILED = 1

if os.name == 'nt':
    import win32con
    import win32file
    import pywintypes
    LOCK_EX = win32con.LOCKFILE_EXCLUSIVE_LOCK
    LOCK_SH = 0 # the default
    LOCK_NB = win32con.LOCKFILE_FAIL_IMMEDIATELY
    # is there any reason not to reuse the following structure?
    __overlapped = pywintypes.OVERLAPPED()
elif os.name == 'posix':
    import fcntl
    LOCK_EX = fcntl.LOCK_EX
    LOCK_SH = fcntl.LOCK_SH
    LOCK_NB = fcntl.LOCK_NB
else:
    raise RuntimeError("PortaLocker only defined for nt and posix platforms")

if os.name == 'nt':

    def lockno(fileno, flags):
        hfile = win32file._get_osfhandle(fileno)
        try:
            win32file.LockFileEx(hfile, flags, 0, -0x10000, __overlapped)
        except pywintypes.error as exc_value:
            # error: (33, 'LockFileEx', 'The process cannot access the file because another process has locked a portion of the file.')
            if exc_value.args[0] == 33:
                raise LockException(
                    LockException.LOCK_FAILED, exc_value.args[2])
            else:
                # Q:  Are there exceptions/codes we should be dealing with here?
                raise

    def unlockno(fileno):
        hfile = win32file._get_osfhandle(fileno)
        try:
            win32file.UnlockFileEx(hfile, 0, -0x10000, __overlapped)
        except pywintypes.error as exc_value:
            if exc_value[0] == 158:
                # error: (158, 'UnlockFileEx', 'The segment is already unlocked.')
                # To match the 'posix' implementation, silently ignore this error
                pass
            else:
                # Q:  Are there exceptions/codes we should be dealing with here?
                raise

elif os.name == 'posix':
    def lockno(fileno, flags):
        try:
            fcntl.flock(fileno, flags)
        except IOError as exc_value:
            #  IOError: [Errno 11] Resource temporarily unavailable
            #  Following added by Bhojpur Consulting Private Limited, India using LOCK_NB|LOCK_EX on Mac 10.4
            #  IOError: [Errno 35] Resource temporarily unavailable
            if exc_value.args[0] == 11 or exc_value.args[0] == 35:
                raise LockException(
                    LockException.LOCK_FAILED, exc_value.args[1])
            else:
                raise

    def unlockno(fileno):
        fcntl.flock(fileno, fcntl.LOCK_UN)

def lock(file, flags):
    lockno(file.fileno(), flags)

def unlock(file):
    unlockno(file.fileno())

if __name__ == '__main__':
    from time import time, strftime, localtime
    import sys
    from . import portalocker

    log = open('log.txt', "a+")
    portalocker.lock(log, portalocker.LOCK_EX)

    timestamp = strftime("%m/%d/%Y %H:%M:%S\n", localtime(time()))
    log.write( timestamp )

    print("Wrote lines. Hit enter to release lock.")
    dummy = sys.stdin.readline()

    log.close()
