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

# Installs Bhojpur ODE requirements

set -e
set -u
set -x

export PATH=/usr/local/bin:/usr/local/opt/python/libexec/bin:$PATH
export LANG=${LANG:-en_US.UTF-8}
export LANGUAGE=${LANGUAGE:-en_US:en}

# Test whether this script is run in a job environment
JOB_NAME=${JOB_NAME:-}
if [[ -n $JOB_NAME ]]; then
    DEFAULT_TESTING_MODE=true
else
    DEFAULT_TESTING_MODE=false
fi
TESTING_MODE=${TESTING_MODE:-$DEFAULT_TESTING_MODE}

###################################################################
# Homebrew installation
###################################################################

# Install Homebrew in /usr/local
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

# Update Homebrew
brew update

# Run brew doctor
brew doctor

# Install git if not already installed
brew list | grep "\bgit\b" || brew install git

# Install PostgreSQL
brew install postgresql

###################################################################
# Python pip installation
###################################################################

# Install Homebrew python
# Alternately, the system Python can be used but installing Python
# dependencies may require sudo
brew install python

# Tap ode-alt library
if [ "$TESTING_MODE" = true ]; then
    brew tap --full ode/alt || echo "Already tapped"

    # Install scc tools
    pip install -U scc || echo "scc installed"

    # Merge homebrew-alt PRs
    cd $(brew --repository)/Library/Taps/ode/homebrew-alt
    scc merge master

    # Repair formula symlinks after merge
    brew tap --repair
else
    brew tap ode/alt || echo "Already tapped"
fi

# Tap homebrew-science library (HDF5)
brew tap homebrew/science || echo "Already tapped"