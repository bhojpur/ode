#!/usr/bin/env bash

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

# bf.sh: the script that actually launches a command line tool

BF_DIR=`dirname "$0"`

# Include the master configuration file.
source "$BF_DIR/config.sh"

# Check that a command to run was specified.
if [ -z "$BF_PROG" ]
then
  echo The command to launch must be set in the BF_PROG environment variable.
  exit 1
fi

# Prepare the flags.
if [ -z "$BF_FLAGS" ]
then
   BF_FLAGS=""
fi

# Set the max heap size.
if [ -z "$BF_MAX_MEM" ]
then
  # Set a reasonable default max heap size.
  BF_MAX_MEM="512m"
fi
BF_FLAGS="$BF_FLAGS -Xmx$BF_MAX_MEM"

# Skip the update check if the NO_UPDATE_CHECK flag is set.
if [ -n "$NO_UPDATE_CHECK" ]
then
  BF_FLAGS="$BF_FLAGS -Dodeformats_can_do_upgrade_check=false"
fi

# Run profiling if the BF_PROFILE flag is set.
if [ -n "$BF_PROFILE" ]
then
  # Set default profiling depth
  if [ -z "$BF_PROFILE_DEPTH" ]
  then
    BF_PROFILE_DEPTH="30"
  fi
  BF_FLAGS="$BF_FLAGS -agentlib:hprof=cpu=samples,depth=$BF_PROFILE_DEPTH,file=$BF_PROG.hprof"
fi

# Use any available proxy settings.
BF_FLAGS="$BF_FLAGS -Dhttp.proxyHost=$PROXY_HOST -Dhttp.proxyPort=$PROXY_PORT"

# Run the command!
if [ -n "$BF_DEVEL" ]
then
  # Developer environment variable set; launch with existing classpath.
  java $BF_FLAGS $BF_PROG "$@"
else
  # Developer environment variable unset; add JAR libraries to classpath.
  if [ -e "$BF_JAR_DIR/odeformats_package.jar" ]
  then
    BF_CP="$BF_JAR_DIR/odeformats_package.jar:$BF_CP"
  elif [ -e "$BF_JAR_DIR/formats-gpl.jar" ]
  then
    BF_CP="$BF_JAR_DIR/formats-gpl.jar:$BF_JAR_DIR/ode-formats-tools.jar:$BF_CP"
  else
    # Libraries not found; issue an error.
    echo "Required JAR libraries not found. Please download:"
    echo "  odeformats_package.jar"
    echo "from:"
    echo "  https://downloads.bhojpur.net/latest/ode-formats/artifacts/"
    echo "and place in the same directory as the command line tools."
    exit 2
  fi
  java $BF_FLAGS -cp "$BF_DIR:$BF_CP" $BF_PROG "$@"
fi
