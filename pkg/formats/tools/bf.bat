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
REM bf.bat: the batch file that actually launches a command line tool

setlocal enabledelayedexpansion
set BF_DIR=%~dp0
if "%BF_DIR:~-1%" == "\" set BF_DIR=%BF_DIR:~0,-1%

rem Include the master configuration file.
call "%BF_DIR%\config.bat"

rem Check that a command to run was specified.
if "%BF_PROG%" == "" (
  echo The command to launch must be set in the BF_PROG environment variable.
  goto end
)

rem Set the max heap size.
if "%BF_MAX_MEM%" == "" (
  rem Set a reasonable default max heap size.
  set BF_MAX_MEM=512m
)
set BF_FLAGS=%BF_FLAGS% -Xmx%BF_MAX_MEM%

rem Skip the update check if the NO_UPDATE_CHECK flag is set.
if not "%NO_UPDATE_CHECK%" == "" (
  set BF_FLAGS=%BF_FLAGS% -Dodeformats_can_do_upgrade_check=false
)

rem Run profiling if the BF_PROFILE flag is set.
if not "%BF_PROFILE%" == "" (
  if "%BF_PROFILE_DEPTH%" == "" (
    rem Set default profiling depth
    set BF_PROFILE_DEPTH=30
  )
  set BF_FLAGS=%BF_FLAGS% -agentlib:hprof=cpu=samples,depth=!BF_PROFILE_DEPTH!,file=%BF_PROG%.hprof
)


rem Use any available proxy settings.
set BF_FLAGS=%BF_FLAGS% -Dhttp.proxyHost=%PROXY_HOST% -Dhttp.proxyPort=%PROXY_PORT%

rem Run the command!
if not "%BF_DEVEL%" == "" (
  rem Developer environment variable set; launch with existing classpath.
  java %BF_FLAGS% %BF_PROG% %*
  goto end
)

rem Developer environment variable unset; add JAR libraries to classpath.
if exist "%BF_JAR_DIR%\odeformats_package.jar" (
    set BF_CP=%BF_CP%;"%BF_JAR_DIR%\odeformats_package.jar"
) else if exist "%BF_JAR_DIR%\formats-gpl.jar" (
    set BF_CP=%BF_CP%;"%BF_JAR_DIR%\formats-gpl.jar";"%BF_JAR_DIR%\ode-formats-tools.jar"
) else (
  rem Libraries not found; issue an error.
  echo Required JAR libraries not found. Please download:
  echo   odeformats_package.jar
  echo from:
  echo   https://downloads.bhojpur.net/latest/ode-formats/artifacts/
  echo and place in the same directory as the command line tools.
  goto end
)

java %BF_FLAGS% -cp "%BF_DIR%";%BF_CP% %BF_PROG% %*

:end
