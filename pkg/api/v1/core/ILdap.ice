/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CORE_ILDAP_ICE
#define ODE_CORE_ILDAP_ICE

#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

module ode {

    module core {
        /**
         * Administration interface providing access to admin-only
         * functionality as well as JMX-based server access and selected user
         * functions. Most methods require membership in privileged
         * {@link ode.model.ExperimenterGroup} groups.
         *
         * Methods which return {@link ode.model.Experimenter} or
         * {@link ode.model.ExperimenterGroup} instances fetch and load all
         * related instances of {@link ode.model.ExperimenterGroup} or
         * {@link ode.model.Experimenter}, respectively.
         */
        ["ami", "amd"] interface ILdap extends ServiceInterface
            {
                /**
                 * Searches all {@link ode.model.Experimenter} list on LDAP
                 * for attribute objectClass = person.
                 *
                 * @return all Experimenter list.
                 */
                idempotent ExperimenterList searchAll() throws ServerError;

                /**
                 * Searches Distinguished Name in groups.
                 *
                 * @param attr   Name of member attribute. Never null or empty.
                 * @param value  User's DN which should be set on value for
                 *               attribute. Never null or empty.
                 * @return       List of groups which contains DN.
                 */
                idempotent StringSet searchDnInGroups(string attr, string value) throws ServerError;

                /**
                 * Searches all {@link ode.model.Experimenter} in LDAP for
                 * specified attribute.
                 *
                 * @param dn        Distinguished Name base for search. Never
                 *                  null.
                 * @param attribute Name of attribute. Never null or empty.
                 * @param value     Expected value of attribute. Never null or
                 *                  empty.
                 * @return          List of Experimenters.
                 */
                idempotent ExperimenterList searchByAttribute(string dn, string attribute, string value) throws ServerError;

                /**
                 * Searches all {@link ode.model.Experimenter} in LDAP for
                 * specified attributes. Attributes should be specified in
                 * StringSet and their values should be set in equivalent
                 * StringSet.
                 *
                 * @param dn         Distinguished Name base for search. Never
                 *                   null.
                 * @param attributes Name of attribute. Never null or empty.
                 * @param values     Expected value of attribute. Never null
                 *                   or empty.
                 * @return           List of Experimenters.
                 */
                idempotent ExperimenterList searchByAttributes(string dn, StringSet attributes, StringSet values) throws ServerError;

                /**
                 * Searches one {@link ode.model.Experimenter} in LDAP for
                 * specified Distinguished Name.
                 *
                 * @param userdn
                 *            unique Distinguished Name - string of user,
                 *            Never null or empty.
                 * @return an Experimenter.
                 */
                idempotent ode::model::Experimenter searchByDN(string userdn) throws ServerError;

                /**
                 * Searches unique Distinguished Name - string in LDAP for
                 * Common Name equals username. Common Name should be unique
                 * under the specified base. If list of cn's contains more
                 * then one DN will return exception.
                 *
                 * @param username
                 *            Name of the Experimenter equals CommonName.
                 * @return a Distinguished Name. Never null.
                 * @throws ApiUsageException
                 *             if more then one 'cn' under the specified base.
                 */
                idempotent string findDN(string username) throws ServerError;

                /**
                 * Looks up the DN for a group.
                 *
                 * @return a Distinguished Name. Never null.
                 * @throws ApiUsageException
                 *             if more then one 'cn' under the specified base.
                 */
                idempotent string findGroupDN(string groupname) throws ServerError;

                /**
                 * Searches Experimenter by unique Distinguished Name -
                 * string in LDAP for Common Name equals username. Common
                 * Name should be unique under the specified base. If list of
                 * cn's contains more then one DN will return exception.
                 *
                 * @param username
                 *            Name of the Experimenter equals CommonName.
                 * @return an Experimenter. Never null.
                 * @throws ApiUsageException
                 *             if more then one 'cn' under the specified base.
                 */
                idempotent ode::model::Experimenter findExperimenter(string username) throws ServerError;

                /**
                 * Looks up a specific {@link ode.model.ExperimenterGroup}
                 * in LDAP using the provided group name. It is expected that
                 * the group name will be unique in the searched LDAP base
                 * tree. If more than one group with the specified name has
                 * been found, an exception will be thrown.
                 *
                 * @param groupname
                 * @return an ExperimenterGroup. Never <code>null</null>.
                 * @throws ApiUsageException
                 *             if more then one group name matches under the
                 *             specified base.
                 */
                idempotent ode::model::ExperimenterGroup findGroup(string groupname) throws ServerError;

                ["deprecate:setDN() is deprecated. Set the LDAP flag on model objects instead."]
                idempotent void setDN(ode::RLong experimenterID, string dn) throws ServerError;

                /**
                 * Gets config value from properties.
                 *
                 * @return boolean
                 */
                idempotent bool getSetting() throws ServerError;

                /**
                 * Discovers and lists {@link ode.model.Experimenter}s who
                 * are present in the remote LDAP server and in the local DB
                 * but have the <code>ldap</code> property set to
                 * <code>false</code>.
                 *
                 * @return list of Experimenters.
                 */
                idempotent ExperimenterList discover() throws ServerError;

                /**
                * Discovers and lists {@link ode.model.ExperimenterGroup}s
                * which are present in the remote LDAP server and in the local
                * DB but have the <code>ldap</code> property set to
                * <code>false</code>.
                *
                * @return list of ExperimenterGroups.
                */
                idempotent ExperimenterGroupList discoverGroups() throws ServerError;

                /**
                 * Creates an {@link ode.model.Experimenter} entry in the
                 * ODE DB based on the supplied LDAP username.
                 * @param username
                 * @return created Experimenter or null
                 */
                ode::model::Experimenter createUser(string username) throws ServerError;
            };

    };
};

#endif