@echo off
REM
REM Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
REM
REM Permission is hereby granted, free of charge, to any person obtaining a copy
REM of this software and associated documentation files (the "Software"), to deal
REM in the Software without restriction, including without limitation the rights
REM to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
REM copies of the Software, and to permit persons to whom the Software is
REM furnished to do so, subject to the following conditions:
REM
REM The above copyright notice and this permission notice shall be included in
REM all copies or substantial portions of the Software.
REM
REM THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
REM IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
REM FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
REM AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
REM LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
REM OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
REM THE SOFTWARE.
REM
REM --------------------------------------------------------------------------
REM Environment variables for use by the server. The values listed here will
REM usually be the defaults. These files should be configured by users and then
REM run in the current shell before starting Bhojpur ODE.
REM --------------------------------------------------------------------------

REM Time until secondary servers startup, in milliseconds.
set ODE_STARTUP_WAIT=10000

REM Location for the creation of the Bhojpur ODE temporary directory.
REM If %HOMEPATH% fails, then %TEMP% will be used.
set ODE_TEMPDIR=%HOMEPATH%\.ode

REM ONLY set ODE_HOME if you know what you are doing.
REM ----------------------------------------------------
REM Alternative installation directory to use.
REM set ODE_HOME=c:\ode_dist