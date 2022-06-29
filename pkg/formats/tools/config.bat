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
REM config.bat: master configuration file for the batch files

rem Running this command directly has no effect,
rem but you can tweak the settings to your liking.

rem Set the amount of RAM available to the command line tools.
rem Use "m" suffix for megabytes, "g" for gigabytes; e.g., 2g = 2GB.
rem set BF_MAX_MEM=1g

rem Set the NO_UPDATE_CHECK flag to skip the update check.
rem set NO_UPDATE_CHECK=1

rem If you are behind a proxy server, the host name and port must be set.
rem set PROXY_HOST=
rem set PROXY_PORT=

rem If your CLASSPATH already includes the needed classes,
rem you can set the BF_DEVEL environment variable to
rem disable the required JAR library checks.
rem set BF_DEVEL=1

rem Set the directory containing the JAR libraries.
if "%BF_JAR_DIR%" == "" (
  if exist "%BF_DIR%\..\artifacts" (
    rem Batch files reside in a git working copy.
    rem Look for JARs in the artifacts directory.
    set BF_JAR_DIR=%BF_DIR%\..\artifacts
  ) else (
    rem Batch files reside in a standalone distribution.
    rem Look for JARs in the same directory as the batch files.
    set BF_JAR_DIR=%BF_DIR%
  )
)
