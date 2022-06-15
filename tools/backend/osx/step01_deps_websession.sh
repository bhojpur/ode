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
# Redis installation
###################################################################

# Install redis
brew install redis
brew services start redis

# Install django-cache-redis
pip install "django-redis>=4.4,<4.9"

# Set up redis session backend
ode config set ode.web.session_engine 'django.contrib.sessions.backends.cache'
ode config set ode.web.caches '{"default": {"BACKEND": "django_redis.cache.RedisCache","LOCATION": "redis://127.0.0.1:6379/0"}}'