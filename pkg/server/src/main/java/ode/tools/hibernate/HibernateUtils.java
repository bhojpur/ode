package ode.tools.hibernate;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Hibernate;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;

import ode.conditions.InternalException;
import ode.model.IObject;
import ode.model.core.Image;
import ode.model.internal.Details;
import ode.model.meta.Experimenter;
import ode.tools.lsid.LsidUtils;

/**
 * contains methods for reloading {@link IObject#unload() unloaded} entities and
 * nulled collections as well as determining the index of certain properties in
 * a dehydrated Hibernate array.
 */
public abstract class HibernateUtils {

    private static Logger log = LoggerFactory.getLogger(HibernateUtils.class);

    // using Image as an example. All details fields are named the same.
    private static String DETAILS = LsidUtils.parseField(Image.DETAILS);

    // ~ Static methods
    // =========================================================================

    // TODO isUnloaded/nullSafe*Id actually belong in ModelUtils.

    public static boolean isUnloaded(Object original) {
        if (original != null && original instanceof IObject
                && !((IObject) original).isLoaded()) {
            return true;
        }
        return false;
    }

    /**
     * returns the id of the {@link Experimenter owner} of this entity, or null
     * if: (1) the object is null, (2) the {@link Details} is null, (3) the
     * owner is null.
     * 
     * @param iobject
     *            Can be null.
     * @return the id or null.
     */
    public static Long nullSafeOwnerId(IObject iobject) {
        if (iobject == null) {
            return null;
        }
        if (iobject.getDetails() == null) {
            return null;
        }
        if (iobject.getDetails().getOwner() == null) {
            return null;
        }
        return iobject.getDetails().getOwner().getId();
    }

    /**
     * returns the id of the {@link ode.model.meta.ExperimenterGroup group} of this entity,
     * or null if: (1) the object is null, (2) the {@link Details} is null, (3) the
     * group is null.
     * 
     * @param iobject
     *            Can be null.
     * @return the id or null.
     */
    public static Long nullSafeGroupId(IObject iobject) {
        if (iobject == null) {
            return null;
        }
        if (iobject.getDetails() == null) {
            return null;
        }
        if (iobject.getDetails().getGroup() == null) {
            return null;
        }
        return iobject.getDetails().getGroup().getId();
    }

    /**
     * loads collections which have been filtered or nulled by the user
     */
    public static void fixNulledOrFilteredCollections(IObject entity,
            IObject target, EntityPersister persister, SessionImplementor source) {

        Object[] currentState = persister.getPropertyValues(entity, source
                .getEntityMode());
        Object[] previousState = persister.getPropertyValues(target, source
                .getEntityMode());
        String[] propertyNames = persister.getPropertyNames();
        Type[] types = persister.getPropertyTypes();

        int detailsIndex = detailsIndex(propertyNames);
        Details d = (Details) currentState[detailsIndex];
        if (d != null) {
            Set<String> s = d.filteredSet();
            for (String string : s) {
                string = LsidUtils.parseField(string);
                int idx = index(string, propertyNames);
                Object previous = previousState[idx];
                if (!(previous instanceof Collection)) // implies
                // not null
                {
                    throw new InternalException(String.format(
                            "Invalid collection found for filtered "
                                    + "field %s in previous state for %s",
                            string, entity));
                }
                log("Copying filtered collection ", string);
                Collection copy = copy(((Collection) previous));
                persister.setPropertyValue(entity, idx, copy, source
                        .getEntityMode());
            }
        }

        for (int i = 0; i < types.length; i++) {
            Type t = types[i];
            if (t.isCollectionType() && null == currentState[i]) {
                Object previous = previousState[i];
                if (previous == null) {
                    // ignore. If the system gave it to us, it can handle it.
                } else if (previous instanceof Collection) {
                    if (!Hibernate.isInitialized(previous)) {
                        log("Skipping uninitialized collection: ", propertyNames[i]);
                        persister.setPropertyValue(entity, i, previous, source
                                .getEntityMode());
                    } else {
                        log("Copying nulled collection: ", propertyNames[i]);
                        Collection copy = copy(((Collection) previous));
                        persister.setPropertyValue(entity, i, copy, source
                                .getEntityMode());
                    }
                } else if (previous instanceof Map) {
                    if (!Hibernate.isInitialized(previous)) {
                        log("Skipping uninitialized map: ", propertyNames[i]);
                        persister.setPropertyValue(entity, i, previous, source
                                .getEntityMode());
                    } else {
                        Map copy = copy((Map) previous);
                        persister.setPropertyValue(entity, i, copy, source
                            .getEntityMode());
                    }
                } else {
                    throw new InternalException(String.format(
                            "Invalid collection found for null "
                                    + "field %s in previous state for %s",
                            propertyNames[i], entity));
                }
            }
        }
    }

    /**
     * @param new_d the new details
     * @param old_d the old details
     * @return if the non-permissions fields are the same
     */
    public static boolean onlyPermissionsChanged(Details new_d, Details old_d) {
        if (idEqual(new_d.getOwner(), old_d.getOwner())
                && idEqual(new_d.getGroup(), old_d.getGroup())
                && idEqual(new_d.getCreationEvent(), old_d.getCreationEvent())
                && idEqual(new_d.getUpdateEvent(), old_d.getUpdateEvent())
                && idEqual(new_d.getExternalInfo(), old_d.getExternalInfo())) {
            return true;
        }
        return false;
    }

    /**
     * returns true under the following circumstances:
     * <ul>
     * <li>both arguments are null, or</li>
     * <li>both arguments are identical (==), or</li>
     * <li>both arguments have the same id value(equals)</li>
     * </ul>
     */
    public static boolean idEqual(IObject arg1, IObject arg2) {

        // arg1 is null
        if (arg1 == null) {
            // both are null, therefore equal
            if (arg2 == null) {
                return true;
            }

            // just arg1 is null, can't be equal
            return false;
        }

        // just arg2 is null, also can't be equal
        else if (arg2 == null) {
            return false;
        }

        // neither argument is null,
        // so let's move a level down,
        // but first test reference equality
        // as a performance op.

        if (arg1 == arg2) {
            return true; // OP
        }

        Long arg1_id = arg1.getId();
        Long arg2_id = arg2.getId();

        // arg1_id is null
        if (arg1_id == null) {

            // both are null, and not identical (see OP above)
            // therefore different
            if (arg2_id == null) {
                return false;
            }

            // just arg2_id is null, can't be equal
            return false;
        }

        // just arg2_id null, and also can't be equal
        else if (arg2_id == null) {
            return false;
        } else {
            return arg1_id.equals(arg2_id);
        }
    }

    public static Details getDetails(Object[] state, String[] names) {
        return (Details) state[detailsIndex(names)];
    }

    public static int detailsIndex(String[] propertyNames) {
        return index(DETAILS, propertyNames);
    }

    public static int index(String str, String[] propertyNames) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals(str)) {
                return i;
            }
        }
        throw new InternalException("No \"" + str + "\" property found.");
    }

    @SuppressWarnings("unchecked")
    protected static Map copy(Map m) {
        Map newMap = new HashMap();
        newMap.putAll(m);
        return newMap;
    }

    @SuppressWarnings("unchecked")
    protected static Collection copy(Collection c) {
        if (c instanceof Set) {
            return new HashSet(c);
        }

        else if (c instanceof List) {
            return new ArrayList(c);
        } else {
            throw new InternalException("Unsupported collection type:"
                    + c.getClass().getName());
        }
    }

    private static void log(Object... objects) {
        if (log.isDebugEnabled() && objects != null && objects.length > 0) {
            StringBuilder sb = new StringBuilder(objects.length * 16);
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            log.debug(sb.toString());
        }
    }
}