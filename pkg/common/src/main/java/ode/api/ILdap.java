package ode.api;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.util.List;

import ode.annotations.NotNull;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;

/**
 * Administration interface providing access to admin-only functionality as well
 * as JMX-based server access and selected user functions. Most methods require
 * membership in privileged {@link ExperimenterGroup groups}.
 *
 * Methods which return {@link ode.model.meta.Experimenter} or
 * {@link ode.model.meta.ExperimenterGroup} instances fetch and load all related
 * instances of {@link ode.model.meta.ExperimenterGroup} or
 * {@link ode.model.meta.Experimenter}, respectively.
 */
public interface ILdap extends ServiceInterface {

    /**
     * Searches all {@link ode.model.meta.Experimenter} list on LDAP for
     * attribute objectClass = person.
     *
     * @return all Experimenter list.
     */
    List<Experimenter> searchAll();

    /**
     * Searches Distinguished Name - {@link java.lang.String} in groups
     *
     * @param attr   Name of member attribute. Never null or empty.
     * @param value  User's DN which should be set on value for attribute.
     *               Never null or empty.
     * @return       List of groups which contains DN.
     */
    List<String> searchDnInGroups(@NotNull
    String attr, @NotNull
    String value);

    /**
     * Searches all {@link ode.model.meta.Experimenter} in LDAP for specified
     * attribute
     *
     * @param dn        {@code Distinguished Name} base for search. Never null.
     * @param attribute Name of attribute. Never null or empty.
     * @param value     Expected value of attribute. Never null or empty.
     * @return          List of Experimenters.
     */
    List<Experimenter> searchByAttribute(@NotNull
    String dn, @NotNull
    String attribute, @NotNull
    String value);

    /**
     * Searches all {@link ode.model.meta.Experimenter} in LDAP for specified
     * attributes. Attributes should be specified in String [] and their values
     * should be set in equivalent String [].
     *
     * @param dn         {@code DistinguishedName} base for search. Never null.
     * @param attributes Name of attribute. Never null or empty.
     * @param values     Expected value of attribute. Never null or empty.
     * @return           List of Experimenters.
     */
    List<Experimenter> searchByAttributes(@NotNull
    String dn, @NotNull
    String[] attributes, @NotNull
    String[] values);

    /**
     * Searches one {@link ode.model.meta.Experimenter} in LDAP for specified
     * Distinguished Name - {@link java.lang.String}
     *
     * @param userdn
     *            unique Distinguished Name - {@link java.lang.String} of user,
     *            Never null or empty.
     * @return an Experimenter.
     */
    Experimenter searchByDN(@NotNull
    String userdn);

    /**
     * Searches unique Distinguished Name - {@link java.lang.String} in LDAP for
     * Common Name equals username. Common Name should be unique under the
     * specified base. If list of cn's contains more then one DN will return
     * exception.
     *
     * @param username
     *            Name of the Experimenter equals CommonName.
     * @return an String Distinguished Name. Never null.
     * @throws ode.conditions.ApiUsageException
     *             if more then one 'cn' under the specified base.
     */
    String findDN(@NotNull
    String username);

    /**
     * Looks up the DN for a group.
     *
     * @return an String Distinguished Name. Never null.
     * @throws ode.conditions.ApiUsageException
     *             if more then one 'cn' under the specified base.
     */
    String findGroupDN(@NotNull
    String groupname);

    /**
     * Searches Experimenter by unique Distinguished Name -
     * {@link java.lang.String} in LDAP for Common Name equals username. Common
     * Name should be unique under the specified base. If list of cn's contains
     * more then one DN will return exception.
     *
     * @param username
     *            Name of the Experimenter equals CommonName.
     * @return an Experimenter. Never null.
     * @throws ode.conditions.ApiUsageException
     *             if more then one 'cn' under the specified base.
     */
    Experimenter findExperimenter(@NotNull
    String username);

    /**
     * Looks up a specific {@link ode.model.meta.ExperimenterGroup} in LDAP
     * using the provided group name. It is expected that the group name will be
     * unique in the searched LDAP base tree. If more than one group with the
     * specified name has been found, an exception will be thrown.
     *
     * @param groupname
     * @return an ExperimenterGroup. Never <code>null</null>.
     * @throws ode.conditions.ApiUsageException
     *             if more then one group name matches under the specified base.
     */
    ExperimenterGroup findGroup(@NotNull
    String groupname);

    /**
     * Sets the value of the <code>dn</code> column in the <code>password</code>
     * table to the supplied string, for the supplied
     * {@link ode.model.meta.Experimenter} ID.
     *
     * @param experimenterID
     * @param dn
     * @deprecated As of release 5.1, relevant model objects have the "ldap"
     *             property added to their state.
     */
    @Deprecated
    void setDN(@NotNull
    Long experimenterID, @NotNull
    String dn);

    /**
     * Gets config value from properties.
     *
     * @return boolean
     */
    boolean getSetting();

    /**
     * Creates an {@link ode.model.meta.Experimenter} entry in the Bhojpur ODE DB
     * based on the supplied LDAP username.
     * @param username
     * @return created Experimenter or null
     */
    Experimenter createUser(@NotNull String username);

    /**
     * Discovers and lists {@link ode.model.meta.Experimenter}s who are present
     * in the remote LDAP server and in the local DB but have the
     * <code>ldap</code> property set to <code>false</code>.
     *
     * @return list of Experimenters.
     */
     List<Experimenter> discover();

     /**
     * Discovers and lists {@link ode.model.meta.ExperimenterGroup}s which are
     * present in the remote LDAP server and in the local DB but have the
     * <code>ldap</code> property set to <code>false</code>.
     *
     * @return list of ExperimenterGroups.
     */
     List<ExperimenterGroup> discoverGroups();
}