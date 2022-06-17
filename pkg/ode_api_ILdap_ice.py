#!/usr/bin/env python3
# -*- coding: utf-8 -*-

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

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ServicesF_ice
import ode_Collections_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'ILdap' not in _M_ode.api.__dict__:
    _M_ode.api.ILdap = Ice.createTempClass()
    class ILdap(_M_ode.api.ServiceInterface):
        """
        Administration interface providing access to admin-only
        functionality as well as JMX-based server access and selected user
        functions. Most methods require membership in privileged
        ode.model.ExperimenterGroup groups.
        Methods which return ode.model.Experimenter or
        ode.model.ExperimenterGroup instances fetch and load all
        related instances of ode.model.ExperimenterGroup or
        ode.model.Experimenter, respectively.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ILdap:
                raise RuntimeError('ode.api.ILdap is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ILdap', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::ILdap'

        def ice_staticId():
            return '::ode::api::ILdap'
        ice_staticId = staticmethod(ice_staticId)

        def searchAll_async(self, _cb, current=None):
            """
            Searches all ode.model.Experimenter list on LDAP
            for attribute objectClass = person.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def searchDnInGroups_async(self, _cb, attr, value, current=None):
            """
            Searches Distinguished Name in groups.
            Arguments:
            _cb -- The asynchronous callback object.
            attr -- Name of member attribute. Never null or empty.
            value -- User's DN which should be set on value for attribute. Never null or empty.
            current -- The Current object for the invocation.
            """
            pass

        def searchByAttribute_async(self, _cb, dn, attribute, value, current=None):
            """
            Searches all ode.model.Experimenter in LDAP for
            specified attribute.
            Arguments:
            _cb -- The asynchronous callback object.
            dn -- Distinguished Name base for search. Never null.
            attribute -- Name of attribute. Never null or empty.
            value -- Expected value of attribute. Never null or empty.
            current -- The Current object for the invocation.
            """
            pass

        def searchByAttributes_async(self, _cb, dn, attributes, values, current=None):
            """
            Searches all ode.model.Experimenter in LDAP for
            specified attributes. Attributes should be specified in
            StringSet and their values should be set in equivalent
            StringSet.
            Arguments:
            _cb -- The asynchronous callback object.
            dn -- Distinguished Name base for search. Never null.
            attributes -- Name of attribute. Never null or empty.
            values -- Expected value of attribute. Never null or empty.
            current -- The Current object for the invocation.
            """
            pass

        def searchByDN_async(self, _cb, userdn, current=None):
            """
            Searches one ode.model.Experimenter in LDAP for
            specified Distinguished Name.
            Arguments:
            _cb -- The asynchronous callback object.
            userdn -- unique Distinguished Name - string of user, Never null or empty.
            current -- The Current object for the invocation.
            """
            pass

        def findDN_async(self, _cb, username, current=None):
            """
            Searches unique Distinguished Name - string in LDAP for
            Common Name equals username. Common Name should be unique
            under the specified base. If list of cn's contains more
            then one DN will return exception.
            Arguments:
            _cb -- The asynchronous callback object.
            username -- Name of the Experimenter equals CommonName.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if more then one 'cn' under the specified base.
            """
            pass

        def findGroupDN_async(self, _cb, groupname, current=None):
            """
            Looks up the DN for a group.
            Arguments:
            _cb -- The asynchronous callback object.
            groupname -- 
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if more then one 'cn' under the specified base.
            """
            pass

        def findExperimenter_async(self, _cb, username, current=None):
            """
            Searches Experimenter by unique Distinguished Name -
            string in LDAP for Common Name equals username. Common
            Name should be unique under the specified base. If list of
            cn's contains more then one DN will return exception.
            Arguments:
            _cb -- The asynchronous callback object.
            username -- Name of the Experimenter equals CommonName.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if more then one 'cn' under the specified base.
            """
            pass

        def findGroup_async(self, _cb, groupname, current=None):
            """
            Looks up a specific ode.model.ExperimenterGroup
            in LDAP using the provided group name. It is expected that
            the group name will be unique in the searched LDAP base
            tree. If more than one group with the specified name has
            been found, an exception will be thrown.
            Arguments:
            _cb -- The asynchronous callback object.
            groupname -- 
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if more then one group name matches under the specified base.
            """
            pass

        def setDN_async(self, _cb, experimenterID, dn, current=None):
            pass

        def getSetting_async(self, _cb, current=None):
            """
            Gets config value from properties.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def discover_async(self, _cb, current=None):
            """
            Discovers and lists ode.model.Experimenters who
            are present in the remote LDAP server and in the local DB
            but have the ldap property set to
            false.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def discoverGroups_async(self, _cb, current=None):
            """
            Discovers and lists ode.model.ExperimenterGroups
            which are present in the remote LDAP server and in the local
            DB but have the ldap property set to
            false.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def createUser_async(self, _cb, username, current=None):
            """
            Creates an ode.model.Experimenter entry in the
            Bhojpur ODE DB based on the supplied LDAP username.
            Arguments:
            _cb -- The asynchronous callback object.
            username -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ILdap)

        __repr__ = __str__

    _M_ode.api.ILdapPrx = Ice.createTempClass()
    class ILdapPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Searches all ode.model.Experimenter list on LDAP
        for attribute objectClass = person.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: all Experimenter list.
        """
        def searchAll(self, _ctx=None):
            return _M_ode.api.ILdap._op_searchAll.invoke(self, ((), _ctx))

        """
        Searches all ode.model.Experimenter list on LDAP
        for attribute objectClass = person.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_searchAll(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_searchAll.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Searches all ode.model.Experimenter list on LDAP
        for attribute objectClass = person.
        Arguments:
        Returns: all Experimenter list.
        """
        def end_searchAll(self, _r):
            return _M_ode.api.ILdap._op_searchAll.end(self, _r)

        """
        Searches Distinguished Name in groups.
        Arguments:
        attr -- Name of member attribute. Never null or empty.
        value -- User's DN which should be set on value for attribute. Never null or empty.
        _ctx -- The request context for the invocation.
        Returns: List of groups which contains DN.
        """
        def searchDnInGroups(self, attr, value, _ctx=None):
            return _M_ode.api.ILdap._op_searchDnInGroups.invoke(self, ((attr, value), _ctx))

        """
        Searches Distinguished Name in groups.
        Arguments:
        attr -- Name of member attribute. Never null or empty.
        value -- User's DN which should be set on value for attribute. Never null or empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_searchDnInGroups(self, attr, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_searchDnInGroups.begin(self, ((attr, value), _response, _ex, _sent, _ctx))

        """
        Searches Distinguished Name in groups.
        Arguments:
        attr -- Name of member attribute. Never null or empty.
        value -- User's DN which should be set on value for attribute. Never null or empty.
        Returns: List of groups which contains DN.
        """
        def end_searchDnInGroups(self, _r):
            return _M_ode.api.ILdap._op_searchDnInGroups.end(self, _r)

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attribute.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attribute -- Name of attribute. Never null or empty.
        value -- Expected value of attribute. Never null or empty.
        _ctx -- The request context for the invocation.
        Returns: List of Experimenters.
        """
        def searchByAttribute(self, dn, attribute, value, _ctx=None):
            return _M_ode.api.ILdap._op_searchByAttribute.invoke(self, ((dn, attribute, value), _ctx))

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attribute.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attribute -- Name of attribute. Never null or empty.
        value -- Expected value of attribute. Never null or empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_searchByAttribute(self, dn, attribute, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_searchByAttribute.begin(self, ((dn, attribute, value), _response, _ex, _sent, _ctx))

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attribute.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attribute -- Name of attribute. Never null or empty.
        value -- Expected value of attribute. Never null or empty.
        Returns: List of Experimenters.
        """
        def end_searchByAttribute(self, _r):
            return _M_ode.api.ILdap._op_searchByAttribute.end(self, _r)

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attributes. Attributes should be specified in
        StringSet and their values should be set in equivalent
        StringSet.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attributes -- Name of attribute. Never null or empty.
        values -- Expected value of attribute. Never null or empty.
        _ctx -- The request context for the invocation.
        Returns: List of Experimenters.
        """
        def searchByAttributes(self, dn, attributes, values, _ctx=None):
            return _M_ode.api.ILdap._op_searchByAttributes.invoke(self, ((dn, attributes, values), _ctx))

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attributes. Attributes should be specified in
        StringSet and their values should be set in equivalent
        StringSet.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attributes -- Name of attribute. Never null or empty.
        values -- Expected value of attribute. Never null or empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_searchByAttributes(self, dn, attributes, values, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_searchByAttributes.begin(self, ((dn, attributes, values), _response, _ex, _sent, _ctx))

        """
        Searches all ode.model.Experimenter in LDAP for
        specified attributes. Attributes should be specified in
        StringSet and their values should be set in equivalent
        StringSet.
        Arguments:
        dn -- Distinguished Name base for search. Never null.
        attributes -- Name of attribute. Never null or empty.
        values -- Expected value of attribute. Never null or empty.
        Returns: List of Experimenters.
        """
        def end_searchByAttributes(self, _r):
            return _M_ode.api.ILdap._op_searchByAttributes.end(self, _r)

        """
        Searches one ode.model.Experimenter in LDAP for
        specified Distinguished Name.
        Arguments:
        userdn -- unique Distinguished Name - string of user, Never null or empty.
        _ctx -- The request context for the invocation.
        Returns: an Experimenter.
        """
        def searchByDN(self, userdn, _ctx=None):
            return _M_ode.api.ILdap._op_searchByDN.invoke(self, ((userdn, ), _ctx))

        """
        Searches one ode.model.Experimenter in LDAP for
        specified Distinguished Name.
        Arguments:
        userdn -- unique Distinguished Name - string of user, Never null or empty.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_searchByDN(self, userdn, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_searchByDN.begin(self, ((userdn, ), _response, _ex, _sent, _ctx))

        """
        Searches one ode.model.Experimenter in LDAP for
        specified Distinguished Name.
        Arguments:
        userdn -- unique Distinguished Name - string of user, Never null or empty.
        Returns: an Experimenter.
        """
        def end_searchByDN(self, _r):
            return _M_ode.api.ILdap._op_searchByDN.end(self, _r)

        """
        Searches unique Distinguished Name - string in LDAP for
        Common Name equals username. Common Name should be unique
        under the specified base. If list of cn's contains more
        then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        _ctx -- The request context for the invocation.
        Returns: a Distinguished Name. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def findDN(self, username, _ctx=None):
            return _M_ode.api.ILdap._op_findDN.invoke(self, ((username, ), _ctx))

        """
        Searches unique Distinguished Name - string in LDAP for
        Common Name equals username. Common Name should be unique
        under the specified base. If list of cn's contains more
        then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findDN(self, username, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_findDN.begin(self, ((username, ), _response, _ex, _sent, _ctx))

        """
        Searches unique Distinguished Name - string in LDAP for
        Common Name equals username. Common Name should be unique
        under the specified base. If list of cn's contains more
        then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        Returns: a Distinguished Name. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def end_findDN(self, _r):
            return _M_ode.api.ILdap._op_findDN.end(self, _r)

        """
        Looks up the DN for a group.
        Arguments:
        groupname -- 
        _ctx -- The request context for the invocation.
        Returns: a Distinguished Name. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def findGroupDN(self, groupname, _ctx=None):
            return _M_ode.api.ILdap._op_findGroupDN.invoke(self, ((groupname, ), _ctx))

        """
        Looks up the DN for a group.
        Arguments:
        groupname -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findGroupDN(self, groupname, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_findGroupDN.begin(self, ((groupname, ), _response, _ex, _sent, _ctx))

        """
        Looks up the DN for a group.
        Arguments:
        groupname -- 
        Returns: a Distinguished Name. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def end_findGroupDN(self, _r):
            return _M_ode.api.ILdap._op_findGroupDN.end(self, _r)

        """
        Searches Experimenter by unique Distinguished Name -
        string in LDAP for Common Name equals username. Common
        Name should be unique under the specified base. If list of
        cn's contains more then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        _ctx -- The request context for the invocation.
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def findExperimenter(self, username, _ctx=None):
            return _M_ode.api.ILdap._op_findExperimenter.invoke(self, ((username, ), _ctx))

        """
        Searches Experimenter by unique Distinguished Name -
        string in LDAP for Common Name equals username. Common
        Name should be unique under the specified base. If list of
        cn's contains more then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findExperimenter(self, username, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_findExperimenter.begin(self, ((username, ), _response, _ex, _sent, _ctx))

        """
        Searches Experimenter by unique Distinguished Name -
        string in LDAP for Common Name equals username. Common
        Name should be unique under the specified base. If list of
        cn's contains more then one DN will return exception.
        Arguments:
        username -- Name of the Experimenter equals CommonName.
        Returns: an Experimenter. Never null.
        Throws:
        ApiUsageException -- if more then one 'cn' under the specified base.
        """
        def end_findExperimenter(self, _r):
            return _M_ode.api.ILdap._op_findExperimenter.end(self, _r)

        """
        Looks up a specific ode.model.ExperimenterGroup
        in LDAP using the provided group name. It is expected that
        the group name will be unique in the searched LDAP base
        tree. If more than one group with the specified name has
        been found, an exception will be thrown.
        Arguments:
        groupname -- 
        _ctx -- The request context for the invocation.
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if more then one group name matches under the specified base.
        """
        def findGroup(self, groupname, _ctx=None):
            return _M_ode.api.ILdap._op_findGroup.invoke(self, ((groupname, ), _ctx))

        """
        Looks up a specific ode.model.ExperimenterGroup
        in LDAP using the provided group name. It is expected that
        the group name will be unique in the searched LDAP base
        tree. If more than one group with the specified name has
        been found, an exception will be thrown.
        Arguments:
        groupname -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findGroup(self, groupname, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_findGroup.begin(self, ((groupname, ), _response, _ex, _sent, _ctx))

        """
        Looks up a specific ode.model.ExperimenterGroup
        in LDAP using the provided group name. It is expected that
        the group name will be unique in the searched LDAP base
        tree. If more than one group with the specified name has
        been found, an exception will be thrown.
        Arguments:
        groupname -- 
        Returns: an ExperimenterGroup. Never null.
        Throws:
        ApiUsageException -- if more then one group name matches under the specified base.
        """
        def end_findGroup(self, _r):
            return _M_ode.api.ILdap._op_findGroup.end(self, _r)

        def setDN(self, experimenterID, dn, _ctx=None):
            return _M_ode.api.ILdap._op_setDN.invoke(self, ((experimenterID, dn), _ctx))

        def begin_setDN(self, experimenterID, dn, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_setDN.begin(self, ((experimenterID, dn), _response, _ex, _sent, _ctx))

        def end_setDN(self, _r):
            return _M_ode.api.ILdap._op_setDN.end(self, _r)

        """
        Gets config value from properties.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: boolean
        """
        def getSetting(self, _ctx=None):
            return _M_ode.api.ILdap._op_getSetting.invoke(self, ((), _ctx))

        """
        Gets config value from properties.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSetting(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_getSetting.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Gets config value from properties.
        Arguments:
        Returns: boolean
        """
        def end_getSetting(self, _r):
            return _M_ode.api.ILdap._op_getSetting.end(self, _r)

        """
        Discovers and lists ode.model.Experimenters who
        are present in the remote LDAP server and in the local DB
        but have the ldap property set to
        false.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: list of Experimenters.
        """
        def discover(self, _ctx=None):
            return _M_ode.api.ILdap._op_discover.invoke(self, ((), _ctx))

        """
        Discovers and lists ode.model.Experimenters who
        are present in the remote LDAP server and in the local DB
        but have the ldap property set to
        false.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_discover(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_discover.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Discovers and lists ode.model.Experimenters who
        are present in the remote LDAP server and in the local DB
        but have the ldap property set to
        false.
        Arguments:
        Returns: list of Experimenters.
        """
        def end_discover(self, _r):
            return _M_ode.api.ILdap._op_discover.end(self, _r)

        """
        Discovers and lists ode.model.ExperimenterGroups
        which are present in the remote LDAP server and in the local
        DB but have the ldap property set to
        false.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: list of ExperimenterGroups.
        """
        def discoverGroups(self, _ctx=None):
            return _M_ode.api.ILdap._op_discoverGroups.invoke(self, ((), _ctx))

        """
        Discovers and lists ode.model.ExperimenterGroups
        which are present in the remote LDAP server and in the local
        DB but have the ldap property set to
        false.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_discoverGroups(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_discoverGroups.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Discovers and lists ode.model.ExperimenterGroups
        which are present in the remote LDAP server and in the local
        DB but have the ldap property set to
        false.
        Arguments:
        Returns: list of ExperimenterGroups.
        """
        def end_discoverGroups(self, _r):
            return _M_ode.api.ILdap._op_discoverGroups.end(self, _r)

        """
        Creates an ode.model.Experimenter entry in the
        Bhojpur ODE DB based on the supplied LDAP username.
        Arguments:
        username -- 
        _ctx -- The request context for the invocation.
        Returns: created Experimenter or null
        """
        def createUser(self, username, _ctx=None):
            return _M_ode.api.ILdap._op_createUser.invoke(self, ((username, ), _ctx))

        """
        Creates an ode.model.Experimenter entry in the
        Bhojpur ODE DB based on the supplied LDAP username.
        Arguments:
        username -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createUser(self, username, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ILdap._op_createUser.begin(self, ((username, ), _response, _ex, _sent, _ctx))

        """
        Creates an ode.model.Experimenter entry in the
        Bhojpur ODE DB based on the supplied LDAP username.
        Arguments:
        username -- 
        Returns: created Experimenter or null
        """
        def end_createUser(self, _r):
            return _M_ode.api.ILdap._op_createUser.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ILdapPrx.ice_checkedCast(proxy, '::ode::api::ILdap', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ILdapPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ILdap'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ILdapPrx = IcePy.defineProxy('::ode::api::ILdap', ILdapPrx)

    _M_ode.api._t_ILdap = IcePy.defineClass('::ode::api::ILdap', ILdap, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    ILdap._ice_type = _M_ode.api._t_ILdap

    ILdap._op_searchAll = IcePy.Operation('searchAll', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_searchDnInGroups = IcePy.Operation('searchDnInGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_searchByAttribute = IcePy.Operation('searchByAttribute', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_searchByAttributes = IcePy.Operation('searchByAttributes', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.api._t_StringSet, False, 0), ((), _M_ode.api._t_StringSet, False, 0)), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_searchByDN = IcePy.Operation('searchByDN', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_Experimenter, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_findDN = IcePy.Operation('findDN', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_findGroupDN = IcePy.Operation('findGroupDN', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_findExperimenter = IcePy.Operation('findExperimenter', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_Experimenter, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_findGroup = IcePy.Operation('findGroup', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_ExperimenterGroup, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_setDN = IcePy.Operation('setDN', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RLong, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    ILdap._op_setDN.deprecate("setDN() is deprecated. Set the LDAP flag on model objects instead.")
    ILdap._op_getSetting = IcePy.Operation('getSetting', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_discover = IcePy.Operation('discover', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ExperimenterList, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_discoverGroups = IcePy.Operation('discoverGroups', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_ExperimenterGroupList, False, 0), (_M_ode._t_ServerError,))
    ILdap._op_createUser = IcePy.Operation('createUser', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.model._t_Experimenter, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.ILdap = ILdap
    del ILdap

    _M_ode.api.ILdapPrx = ILdapPrx
    del ILdapPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
