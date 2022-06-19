package ode.formats.enums;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.RLong;
import ode.RString;
import ode.ServerError;
import ode.api.IQueryPrx;
import ode.formats.enums.handler.EnumHandlerFactory;
import ode.formats.enums.handler.EnumerationHandler;
import ode.model.IObject;

/**
 * An enumeration provider which uses IQuery and a cache to fulfill the
 * contract of an EnumerationProvider.
 */
public class IQueryEnumProvider implements EnumerationProvider
{
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(IQueryEnumProvider.class);

    /** Enumeration cache. */
    private Map<Class<? extends IObject>, HashMap<String, IObject>> enumCache =
        new HashMap<Class<? extends IObject>, HashMap<String, IObject>>();

    /** Query service. */
    private IQueryPrx iQuery;

    /** Enumeration handler factory. */
    private EnumHandlerFactory enumHandlerFactory = new EnumHandlerFactory();

    /**
     * Default IQuery based enumeration provider constructor.
     * @param iQuery Bhojpur ODE query service to use for enumeration lookups.
     */
    public IQueryEnumProvider(IQueryPrx iQuery)
    {
        this.iQuery = iQuery;
    }

    /**
     * Creates an unloaded copy of an enumeration object.
     * @param enumeration Enumeration to copy.
     * @return See above.
     */
    private IObject copyEnumeration(IObject enumeration)
    {
        Class<? extends IObject> klass = enumeration.getClass();
        try
        {
            Constructor<? extends IObject> constructor =
                klass.getDeclaredConstructor(
                    new Class[] { RLong.class, boolean.class });
            return (IObject) constructor.newInstance(
                new Object[] { enumeration.getId(), false });
        }
        catch (Exception e)
        {
            String m = "Unable to copy enumeration: " + enumeration;
            log.error(m, e);
            throw new EnumerationException(m, klass, getValue(enumeration));
        }
    }

    private String getValue(IObject enumeration)
    {
        Class<? extends IObject> klass = enumeration.getClass();
        try
        {
            Method method = klass.getMethod("getValue");
            RString value = (RString) method.invoke(enumeration);
            return value.getValue();
        }
        catch (Exception e)
        {
            String m = "Unable to get value of enumeration: " + enumeration;
            log.error(m, e);
            throw new EnumerationException(m, klass, "");
        }
    }

    /* (non-Javadoc)
     * @see ode.formats.enums.EnumerationProvider#getEnumeration(java.lang.Class, java.lang.String, boolean)
     */
    public <T extends IObject> T getEnumeration(Class<T> klass, String value,
                                                boolean loaded)
    {
        if (klass == null)
            throw new NullPointerException("Expecting not-null klass.");
        if (value == null)
        {
            log.warn("Enumeration " + klass + " with value of null.");
        }
        else if (value.length() == 0)
        {
            log.warn("Enumeration " + klass + " with value of zero length.");
        }

        HashMap<String, T> enumerations = getEnumerations(klass);
        EnumerationHandler handler = enumHandlerFactory.getHandler(klass);
        IObject otherEnumeration = enumerations.get("Other");
        // Step 1, check if we've got an exact match for our enumeration value.
        if (enumerations.containsKey(value))
        {
            log.debug(String.format("Returning %s exact match for: %s",
                    klass.toString(), value));
            if (!loaded)
            {
                return (T) copyEnumeration(enumerations.get(value));
            }
            return enumerations.get(value);
        }
        // Step 2, check if our enumeration handler can find a match.
        IObject enumeration = handler.findEnumeration((HashMap<String, IObject>) enumerations, value);
        if (enumeration != null)
        {
            log.debug(String.format("Handler found %s match for: %s",
                    klass.toString(), value));
            if (!loaded)
            {
                return (T) copyEnumeration(enumeration);
            }
            return (T) enumeration;
        }
        // Step 3, fall through to an "Other" enumeration if we have one.
        if (otherEnumeration != null)
        {
            log.warn("Enumeration '" + value + "' does not exist in '"
                     + klass + "' setting to 'Other'");
            return (T) otherEnumeration;
        }
        // Step 4, warn we have no enumeration to return.
        log.warn("Enumeration '" + value + "' does not exist in '"
                + klass + "' returning 'null'");
        return (T) enumeration;
    }

    /* (non-Javadoc)
     * @see ode.formats.enums.EnumerationProvider#getEnumerations(java.lang.Class)
     */
    public <T extends IObject> HashMap<String, T> getEnumerations(Class<T> klass)
    {
        if (!enumCache.containsKey(klass))
        {
            List<IObject> enumerationList;
            try
            {
                enumerationList = (List<IObject>) iQuery.findAll(klass.getName(), null);
            }
            catch (ServerError e)
            {
                throw new RuntimeException(e);
            }

            if (enumerationList == null)
                throw new EnumerationException("Problem finding enumeration: ",
                                               klass, null);

            HashMap<String, IObject> enumerations =
                new HashMap<String, IObject>();
            for (IObject enumeration : enumerationList)
            {
                enumerations.put(getValue(enumeration), enumeration);
            }
            enumCache.put(klass, enumerations);
        }
        return (HashMap<String, T>) enumCache.get(klass);
    }
}