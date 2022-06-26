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

set -e -u -x

ODEVER=${ODEVER:-latest}
PGVER=${PGVER:-pg11}
ICEVER=${ICEVER:-ice37}

. `dirname $0`/settings.env

bash -eux step01_centos7_init.sh

# install java
bash -eux step01_centos_java_deps.sh

bash -eux step01_centos7_deps.sh

# install ice
bash -eux step01_centos7_ice_deps.sh

# install Postgres
bash -eux step01_centos7_pg_deps.sh

bash -eux step02_all_setup.sh

if [[ "$PGVER" =~ ^(pg10|pg11|pg12)$ ]]; then
    bash -eux step03_all_postgres.sh
fi

bash -eux step01_centos7_ice_venv.sh


# Those steps are valid if an ode-server user exists
# This might not be the case when used in the context of devspace
if [ "$(getent passwd ode-server)" ]; then
    cp settings.env step04_all_ode.sh setup_ode_db.sh ~ode-server

    ODEVER=$ODEVER ICEVER=$ICEVER bash -eux step04_all_ode_install.sh

    su - ode-server -c " bash -eux step04_all_ode.sh"

    su - ode-server -c "bash setup_ode_db.sh"
fi


#If you don't want to use the systemd scripts you can start Bhojpur ODE manually:
#su - ode-server -c ". /home/ode-server/settings.env ode admin start"

bash -eux setup_centos_selinux.sh

PGVER=$PGVER bash -eux step06_centos7_daemon.sh

#systemctl start ode.service