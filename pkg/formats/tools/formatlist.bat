@echo off

rem formatlist.bat: a batch file for listing supported formats in ODE-Formats

rem Required JARs: odeformats_package.jar

setlocal
set BF_DIR=%~dp0
if "%BF_DIR:~-1%" == "\" set BF_DIR=%BF_DIR:~0,-1%

set BF_PROG=loci.formats.tools.PrintFormatTable
call "%BF_DIR%\bf.bat" %*
