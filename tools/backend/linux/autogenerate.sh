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

# installation of the recommended dependencies

set +x

dir=`dirname $0`

remove_docker_workaround () {
l="$(echo -e "${@}" | sed -e 's/^[[:space:]]*//')"
l="$(echo -e "${l}" | sed -e 's/^if.*!.*then//' )"
l="$(echo -e "${l}" | sed -e '/^if.*container.*then/,/else/d' )"
l="$(echo -e "${l}" | sed -e 's/^fi//')"
echo "${l}"
}

# generate the walkthrough for all supported os
function generate_all() {
	values=(centos7 debian10 ubuntu1804 ubuntu2004)
	for os in "${values[@]}"; do
  		echo "${os}"
  		 generate ${os}
	done
}

# generate the specified walkthrough
function generate() {
OS=$1
file=walkthrough_$OS.sh
if [ -e $file ]; then
	rm $file
fi
cat <<EOF > $file
#!/bin/bash
set -e -u -x
EOF

N=$OS
if [[ $OS =~ "debian" ]] || [[ $OS =~ "ubuntu" ]] ; then
	N="ubuntu"
elif [[ $OS =~ "centos" ]]  ; then
    N="centos7"
fi
echo -en '\n' >> $file
echo "#start-step01: As root, install dependencies" >> $file
line=$(sed -n '2,$p' $dir/step01_"$N"_init.sh)
echo "$line" >> $file

# install java
N=$OS
if [[ $OS =~ "centos" ]] ; then
	N="centos"
elif [[ $OS =~ "ubuntu1804" ]]  ; then
	N="ubuntu1804"
elif [[ $OS =~ "ubuntu" ]]  ; then
	N="ubuntu"
fi 
echo -en '\n' >> $file
echo "# install Java" >> $file
number=$(sed -n '/#start-recommended/=' $dir/step01_"$N"_java_deps.sh)
ns=$((number+1))
number=$(sed -n '/#end-recommended/=' $dir/step01_"$N"_java_deps.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step01_"$N"_java_deps.sh)
# remove leading whitespace
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line"  >> $file
echo -en '\n' >> $file

echo "# install dependencies" >> $file
# install dependencies
N=$OS
if [[ $OS =~ "ubuntu" ]]  ; then
    N="ubuntu1804"
fi

line=$(sed -n '2,$p' $dir/step01_"$N"_deps.sh)
echo "$line" >> $file
echo "#end-step01" >> $file


# install ice
echo "# install Ice" >> $file
N=$OS
echo "#start-recommended-ice" >> $file
number=$(sed -n '/#start-recommended/=' $dir/step01_"$N"_ice_deps.sh)
ns=$((number+1))
number=$(sed -n '/#end-recommended/=' $dir/step01_"$N"_ice_deps.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step01_"$N"_ice_deps.sh)
# remove leading whitespace
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line"  >> $file
echo "#end-recommended-ice" >> $file

echo -en '\n' >> $file

# install postgres
N=$OS
echo -en '\n' >> $file
echo "# install Postgres" >> $file
if [[ $OS =~ "centos" ]] ; then
    number=$(sed -n '/#start-postgresql-installation-general/=' $dir/step01_"$N"_pg_deps.sh)
    nrs=$((number+1))
    number=$(sed -n '/#end-postgresql-installation-general/=' $dir/step01_"$N"_pg_deps.sh)
    nre=$((number-1))
    line=$(sed -n ''$nrs','$nre'p' $dir/step01_"$N"_pg_deps.sh)
    line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"

    echo "$line"  >> $file

	number=$(sed -n '/#start-recommended/=' $dir/step01_"$N"_pg_deps.sh)
	nrs=$((number+1))
	number=$(sed -n '/#end-recommended/=' $dir/step01_"$N"_pg_deps.sh)
	nre=$((number-1))
	line=$(sed -n ''$nrs','$nre'p' $dir/step01_"$N"_pg_deps.sh)
	# remove docker conditional
	line=`remove_docker_workaround "${line}"`
else
	number=$(sed -n '/#start-recommended/=' $dir/step01_"$N"_pg_deps.sh)
	ns=$((number+1))
	number=$(sed -n '/#end-recommended/=' $dir/step01_"$N"_pg_deps.sh)
	ne=$((number-1))
	line=$(sed -n ''$ns','$ne'p' $dir/step01_"$N"_pg_deps.sh)
fi
# remove leading whitespace
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"

echo "$line"  >> $file
echo "#end-step01" >> $file

echo -en '\n' >> $file
echo "#start-step02: As root, create a local ode-server system user and directory for the Bhojpur ODE repository" >> $file
number=$(sed -n '/#start-create-user/=' $dir/step02_all_setup.sh)
ns=$((number+1))
number=$(sed -n '/#end-create-user/=' $dir/step02_all_setup.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step02_all_setup.sh)
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line" >> $file
ne=$((number+3))
line=$(sed -n ''$ne',$p' $dir/step02_all_setup.sh)
echo "$line" >> $file
echo "#end-step02" >> $file

# postgres remove section
echo "#start-step03: As root, create a database user and a database" >> $file
#find from where to start copying
start=$(sed -n '/#start-setup/=' $dir/step03_all_postgres.sh)
start=$((start+1))
line=$(sed -n ''$start',$p' $dir/step03_all_postgres.sh)
echo "$line" >> $file
echo "#end-step03" >> $file

# create virtual env and install dependencies
echo -en '\n' >> $file
echo "#start-step03bis: As root, create a virtual env and install dependencies" >> $file


number=$(sed -n '/#start-ice-py/=' $dir/step01_"$OS"_ice_venv.sh)
ns=$((number+1))
number=$(sed -n '/#end-ice-py/=' $dir/step01_"$OS"_ice_venv.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step01_"$OS"_ice_venv.sh)
echo "$line" >> $file
echo "#end-step03bis" >> $file

echo -en '\n' >> $file
echo "#start-step04-pre: As root, download the Bhojpur ODE.server" >> $file
echo "#start-release-ice37" >> $file
number=$(sed -n '/#start-release-ice37/=' $dir/step04_all_ode_install.sh)
ns=$((number+1))
number=$(sed -n '/#end-release-ice37/=' $dir/step04_all_ode_install.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step04_all_ode_install.sh)
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line" >> $file
echo "#end-release-ice37" >> $file
number=$(sed -n '/#start-link/=' $dir/step04_all_ode_install.sh)
ns=$((number+1))
number=$(sed -n '/#end-link/=' $dir/step04_all_ode_install.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step04_all_ode_install.sh)
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line" >> $file

echo "#end-step04-pre" >> $file

echo -en '\n' >> $file
echo "#start-step04: As the ode-server system user, configure it" >> $file
echo "#start-copy-odescript" >> $file
echo "cp settings.env step04_all_ode.sh setup_ode_db.sh ~ode " >> $file
echo "#end-copy-odescript" >> $file

line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
number=$(sed -n '/#configure/=' $dir/step04_all_ode.sh)
ns=$((number+1))
number=$(sed -n '/#start-db/=' $dir/step04_all_ode.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step04_all_ode.sh)
echo "$line" >> $file
number=$(sed -n '/#start-deb-latest/=' $dir/step04_all_ode.sh)
ns=$((number+1))
number=$(sed -n '/#end-deb-latest/=' $dir/step04_all_ode.sh)
ne=$((number-1))
line=$(sed -n ''$ns','$ne'p' $dir/step04_all_ode.sh)
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line" >> $file

number=$(sed -n '/#start-config/=' $dir/setup_ode_db.sh)
ns=$((number+1))
line=$(sed -n ''$ns',$p' $dir/setup_ode_db.sh)
echo "$line" >> $file
echo "#end-step04" >> $file

number=$(sed -n '/#start-seclevel/=' $dir/step04_all_ode.sh)
ns=$((number))
number=$(sed -n '/#end-seclevel/=' $dir/step04_all_ode.sh)
ne=$((number))
line=$(sed -n ''$ns','$ne'p' $dir/step04_all_ode.sh)
line=$(echo -e "${line}" | sed -e "s/\-i.bak/-i/g")
line="$(echo -e "${line}" | sed -e 's/^[[:space:]]*//')"
echo "$line" >> $file

echo -en '\n' >> $file

echo -en '\n' >> $file
echo "#start-step06: As root, run the scripts to start Bhojpur ODE automatically" >> $file

if [ $OS = "centos7" ] ; then
	number=$(sed -n '/#start-recommended/=' $dir/step06_"$OS"_daemon.sh)
	nrs=$((number+1))
	number=$(sed -n '/#end-recommended/=' $dir/step06_"$OS"_daemon.sh)
	nre=$((number-1))
	line=$(sed -n ''$nrs','$nre'p' $dir/step06_"$OS"_daemon.sh)
	# remove docker conditional
	line=`remove_docker_workaround "${line}"`
	echo "$line" >> $file
else
	number=$(sed -n '/#start-recommended/=' $dir/step06_ubuntu_daemon.sh)
	nrs=$((number+1))
	number=$(sed -n '/#end-recommended/=' $dir/step06_ubuntu_daemon.sh)
	nre=$((number-1))
	line=$(sed -n ''$nrs','$nre'p' $dir/step06_ubuntu_daemon.sh)
	echo "$line" >> $file
fi
echo "#end-step06" >> $file

echo -en '\n' >> $file
echo "#start-step07: As root, secure Bhojpur ODE" >> $file
start=$(sed -n '/#start/=' $dir/step07_all_perms.sh)
start=$((start+1))
line=$(sed -n ''$start',$p' $dir/step07_all_perms.sh)
echo "$line" >> $file
echo "#end-step07" >> $file

if [[ $OS =~ "centos" ]]; then
echo "#start-selinux" >> $file
line=$(sed -n '2,$p' $dir/setup_centos_selinux.sh)
echo "$line" >> $file
echo "#end-selinux" >> $file
fi
}

#generate scripts for all os by default.
ALL=${ALL:-true}
OS=${OS:-centos7}

if [ $ALL = true ]; then
	generate_all
else
	generate $OS
fi