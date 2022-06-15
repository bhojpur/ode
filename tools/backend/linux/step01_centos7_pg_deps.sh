#!/bin/bash

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

PGVER=${PGVER:-pg11}

#start-postgresql-installation-general
yum -y install https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
#end-postgresql-installation-general
if [ "$PGVER" = "pg10" ]; then
    yum -y install postgresql10-server postgresql10

    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-10/bin/initdb -D /var/lib/pgsql/10/data --encoding=UTF8"
        echo "listen_addresses='*'" >> /var/lib/pgsql/10/data/postgresql.conf
    else
        PGSETUP_INITDB_OPTIONS=--encoding=UTF8 /usr/pgsql-10/bin/postgresql-10-setup initdb
    fi
    sed -i.bak -re 's/^(host.*)ident/\1md5/' /var/lib/pgsql/10/data/pg_hba.conf
    if [ "${container:-}" = docker ]; then
        sed -i 's/OOMScoreAdjust/#OOMScoreAdjust/' \
        /usr/lib/systemd/system/postgresql-10.service
    fi
    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-10/bin/pg_ctl start -D /var/lib/pgsql/10/data -w"
    else
        systemctl start postgresql-10.service
    fi
    systemctl enable postgresql-10.service
elif [ "$PGVER" = "pg11" ]; then
    #start-recommended
    yum -y install postgresql11-server postgresql11

    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-11/bin/initdb -D /var/lib/pgsql/11/data --encoding=UTF8"
        echo "listen_addresses='*'" >> /var/lib/pgsql/11/data/postgresql.conf
    else
        PGSETUP_INITDB_OPTIONS=--encoding=UTF8 /usr/pgsql-11/bin/postgresql-11-setup initdb
    fi
    sed -i.bak -re 's/^(host.*)ident/\1md5/' /var/lib/pgsql/11/data/pg_hba.conf
    if [ "${container:-}" = docker ]; then
        sed -i 's/OOMScoreAdjust/#OOMScoreAdjust/' \
        /usr/lib/systemd/system/postgresql-11.service
    fi
    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-11/bin/pg_ctl start -D /var/lib/pgsql/11/data -w"
    else
        systemctl start postgresql-11.service
    fi
    systemctl enable postgresql-11.service
    #end-recommended
elif [ "$PGVER" = "pg12" ]; then
    yum -y install postgresql12-server postgresql12

    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-12/bin/initdb -D /var/lib/pgsql/12/data --encoding=UTF8"
        echo "listen_addresses='*'" >> /var/lib/pgsql/12/data/postgresql.conf
    else
        PGSETUP_INITDB_OPTIONS=--encoding=UTF8 /usr/pgsql-12/bin/postgresql-12-setup initdb
    fi
    sed -i.bak -re 's/^(host.*)ident/\1md5/' /var/lib/pgsql/12/data/pg_hba.conf
    if [ "${container:-}" = docker ]; then
        sed -i 's/OOMScoreAdjust/#OOMScoreAdjust/' \
        /usr/lib/systemd/system/postgresql-12.service
    fi
    if [ "${container:-}" = docker ]; then
        su - postgres -c "/usr/pgsql-12/bin/pg_ctl start -D /var/lib/pgsql/12/data -w"
    else
        systemctl start postgresql-12.service
    fi
    systemctl enable postgresql-12.service
fi