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

. settings.env

bash -eux step01_ubuntu_init.sh

# install java
bash -eux step01_debian10_java_deps.sh

bash -eux step01_debian10_deps.sh

# install ice
bash -eux step01_debian10_ice_deps.sh

if [ "$ICEVER" = "ice37" ]; then		
	cat ode-ice37.env >> /etc/profile		
fi

# install Postgres
bash -eux step01_debian10_pg_deps.sh

bash -eux step02_all_setup.sh

if [[ "$PGVER" =~ ^(pg11|pg12)$ ]]; then
    bash -eux step03_all_postgres.sh
fi
cp settings.env step04_all_ode.sh setup_ode_db.sh ~ode-server

bash -x step01_debian10_ice_venv.sh
bash -eux step04_all_ode_install.sh

su - ode-server -c "ODEVER=$ODEVER ICEVER=$ICEVER bash -x step04_all_ode.sh"
su - ode-server -c "bash setup_ode_db.sh"

#If you don't want to use the init.d scripts you can start Bhojpur ODE manually:
#su - ode-server -c ". /home/ode-server/settings.env ode admin start"

bash -eux step06_ubuntu_daemon.sh

bash -eux step07_all_perms.sh

#service ode start