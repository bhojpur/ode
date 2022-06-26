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

# /etc/init.d/ode-server
# Subsystem file for "ode" server
#
### BEGIN INIT INFO
# Provides:             ode
# Required-Start:       $local_fs $remote_fs $network $time postgresql
# Required-Stop:        $local_fs $remote_fs $network $time postgresql
# Default-Start:        2 3 4 5
# Default-Stop:         0 1 6
# Short-Description:    ODE.server
### END INIT INFO
#
### Redhat
# chkconfig: - 98 02
# description: Init script for Bhojpur ODE.server
###

RETVAL=0
prog=ode

# Read configuration variable file if it is present
[ -r /etc/default/$prog ] && . /etc/default/$prog

ODE_SERVER=${ODE_SERVER:-/opt/ode/server/ODE.server}
ODE_USER=${ODE_USER:-ode-server}
SETTINGS=${SETTINGS:-/home/ode-server/settings.env}

start() {	
	echo -n $"Starting $prog:"
	su - ${ODE_USER} -c ". ${SETTINGS}; ode admin start" &> /dev/null && echo -n ' ODE.server'
	RETVAL=$?
	[ "$RETVAL" = 0 ]
	echo
}

stop() {
	echo -n $"Stopping $prog:"
	su - ${ODE_USER} -c ". ${SETTINGS}; ode admin stop" &> /dev/null && echo -n ' ODE.server'
	RETVAL=$?
	[ "$RETVAL" = 0 ]
	echo
}

restart() {
	echo -n $"Restarting $prog:"
	su - ${ODE_USER} -c ". ${SETTINGS} ode admin restart" &> /dev/null && echo -n ' ODE.server'
	RETVAL=$?
	[ "$RETVAL" = 0 ]
	echo
}

status() {
	echo -n $"Status $prog:"
	su - ${ODE_USER} -c ". ${SETTINGS} ode admin status" && echo -n ' ODE.server running'
	RETVAL=$?
	echo
}

diagnostics() {
	echo -n $"Diagnostics $prog:"
	su - ${ODE_USER} -c ". ${SETTINGS} ode admin diagnostics"
	RETVAL=$?
	echo
}

case "$1" in
	start)
		start
		;;
	stop)
		stop
		;;
	restart)
		restart
		;;
	status)
		status
		;;
	diagnostics)
		diagnostics
		;;
	*)	
		echo $"Usage: $0 {start|stop|restart|status|diagnostics}"
		RETVAL=1
esac
exit $RETVAL