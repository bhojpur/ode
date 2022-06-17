package ode.logic;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ode.api.ITypes;
import ode.api.ServiceInterface;
import ode.api.local.LocalUpdate;
import ode.annotations.RolesAllowed;
import ode.conditions.ApiUsageException;
import ode.model.IAnnotated;
import ode.model.IEnum;
import ode.model.IObject;
import ode.model.internal.Details;
import ode.model.internal.Permissions;
import ode.model.internal.Permissions.Right;
import ode.model.internal.Permissions.Role;
import ode.security.SecureAction;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

/**
 * implementation of the ITypes service interface.
 */
@Transactional(readOnly = true)
public class TypesImpl extends AbstractLevel2Service implements ITypes {

    protected transient SessionFactory sf;

    /** injector for usage by the container. Not for general use 
     * @param sessions the session factory
     */
    public final void setSessionFactory(SessionFactory sessions) {
        getBeanHelper().throwIfAlreadySet(this.sf, sessions);
        sf = sessions;
    }

    public final Class<? extends ServiceInterface> getServiceInterface() {
        return ITypes.class;
    }

    // ~ Service methods
    // =========================================================================

    @RolesAllowed("user")
    @Transactional(readOnly = false)
    public <T extends IEnum> T createEnumeration(T newEnum) {
        final LocalUpdate up = iUpdate;

        // TODO should this belong to root?
        Details d = getSecuritySystem().newTransientDetails(newEnum);
        newEnum.getDetails().copy(d);
        worldReadable(newEnum);
        return getSecuritySystem().doAction(new SecureAction() {
            public IObject updateObject(IObject... iObjects) {
                return up.saveAndReturnObject(iObjects[0]);
            }
        }, newEnum);
    }

    @RolesAllowed("system")
    @Transactional(readOnly = false)
    public <T extends IEnum> T updateEnumeration(T oEnum) {
        return iUpdate.saveAndReturnObject(oEnum);
    }

    @RolesAllowed("system")
    @Transactional(readOnly = false)
    public <T extends IEnum> void updateEnumerations(List<T> listEnum) {
        // should be changed to saveAndReturnCollection(Collection graph)
        // when method is implemented

        Collection<IObject> colEnum = new ArrayList<IObject>();
        for (Object o : listEnum) {
            IObject obj = (IObject) o;
            colEnum.add(obj);
        }
        iUpdate.saveCollection(colEnum);
    }

    @RolesAllowed("system")
    @Transactional(readOnly = false)
    public <T extends IEnum> void deleteEnumeration(T oEnum) {
        iUpdate.deleteObject(oEnum);
    }

    @RolesAllowed("user")
    public <T extends IEnum> List<T> allEnumerations(Class<T> k) {
        return iQuery.findAll(k, null);
    }

    @RolesAllowed("user")
    public <T extends IEnum> T getEnumeration(Class<T> k, String string) {
        IEnum e = iQuery.findByString(k, "value", string);
        iQuery.initialize(e);
        if (e == null) {
            throw new ApiUsageException(String.format(
                    "An %s enum does not exist with the value: %s",
                    k.getName(), string));
        }
        return k.cast(e);
    }

    @RolesAllowed("user")
    public <T extends IEnum> List<Class<T>> getEnumerationTypes() {

        List<Class<T>> list = new ArrayList<Class<T>>();

        Map<String, ClassMetadata> m = sf.getAllClassMetadata();
        for (String key : m.keySet()) {
            try {
                Class klass = Class.forName(m.get(key).getEntityName());
                boolean r = IEnum.class.isAssignableFrom(klass);
                if (r) {
                    list.add(klass);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found. Exception: "
                        + e.getMessage());
            }
        }
        return list;
    }

    @RolesAllowed("user")
    public <T extends IEnum> Map<Class<T>, List<T>> getEnumerationsWithEntries() {
        Map<Class<T>, List<T>> map = new HashMap<Class<T>, List<T>>();
        for (Class klass : getEnumerationTypes()) {
            List<T> entryList = allEnumerations(klass);
            map.put(klass, entryList);
        }
        return map;
    }

    @RolesAllowed("system")
    public <T extends IEnum> List<T> getOriginalEnumerations() {
        List<IEnum> original = new ArrayList<IEnum>();
        InputStream in = null;
        JarFile jarFile = null;
        try {
            URL file = ResourceUtils.getURL("classpath:enums.properties");
            URL jar = ResourceUtils.extractJarFileURL(file);
            jarFile = new JarFile(jar.getPath());
            JarEntry entry = jarFile.getJarEntry("enums.properties");
            in = jarFile.getInputStream(entry);
            Properties property = new Properties();
            property.load(in);
            Set keys = property.keySet();
            for (Iterator it = keys.iterator(); it.hasNext();) {
                String key = (String) it.next();
                String[] keySplit = key.split("\\.");
                String className = "";
                for (int i = 0; i < keySplit.length - 1; i++) {
                    className = className + keySplit[i] + ".";
                }
                className = className.substring(0, (className.length() - 1));
                String val = property.getProperty(key);
                Class klass = Class.forName(className);
                IEnum originalEntry = (IEnum) klass.getConstructor(String.class)
                        .newInstance(val);
                original.add(originalEntry);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found. " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found. " + e.getMessage());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL. " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("IO exception. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Illegal argument. " + e.getMessage());
        } catch (SecurityException e) {
            throw new RuntimeException("Security exception. " + e.getMessage());
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation exception. "
                    + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access. " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation Target. " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method. " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (jarFile != null) jarFile.close();
            } catch (IOException e) {
            }
        }
        return (List<T>) original;
    }

    @RolesAllowed("system")
    @Transactional(readOnly = false)
    public <T extends IEnum> void resetEnumerations(Class<T> klass) {
        InputStream in = null;
        try {
            URL file = ResourceUtils.getURL("classpath:enums.properties");
            URL jar = ResourceUtils.extractJarFileURL(file);
            JarFile jarFile = new JarFile(jar.getPath());
            JarEntry entry = jarFile.getJarEntry("enums.properties");
            in = jarFile.getInputStream(entry);
            Properties property = new Properties();
            property.load(in);
            Properties newProp = new Properties();
            Set keys = property.keySet();
            for (Iterator it = keys.iterator(); it.hasNext();) {
                String key = (String) it.next();
                if (key.contains(klass.getName())) {
                    newProp.setProperty(key, property.getProperty(key));
                }
            }
            property.clear();

            List<IEnum> listOnDB = (List<IEnum>) allEnumerations(klass);
            List<IEnum> listToDel = new ArrayList<IEnum>();
            List<IEnum> newList = new ArrayList<IEnum>();

            int nps = newProp.size();
            int lod = listOnDB.size();
            
            for (Long i = 1L; i < nps + 1; i++) {
        		String val = newProp.getProperty(klass.getName() + "."
                        + i.toString());
        		IEnum newEntry = klass.getConstructor(String.class).newInstance(val);
        		newList.add(i.intValue() - 1, newEntry);
        	}
            
            if (lod > nps) {
            	for (int j = nps-1; j < lod; j++) {
               		IEnum oldOb = listOnDB.get(j - 1);    
                  	listToDel.add(oldOb);
                }
            }
            
            for (IEnum en : listToDel) {
                deleteEnumeration(en);
            }

            if (newList.size() > 0) {
                updateEnumerations(newList);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found. " + e.getMessage());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL. " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("IO exception. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Illegal argument. " + e.getMessage());
        } catch (SecurityException e) {
            throw new RuntimeException("Security exception. " + e.getMessage());
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation exception. "
                    + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access. " + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation Target. " + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method. " + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    @RolesAllowed("user")
    public List<Class<IAnnotated>> getAnnotationTypes() {
        return new ArrayList<Class<IAnnotated>>(this.metadata
                .getAnnotatableTypes());
    }

    // Removed from interface
    // =======================================

    @RolesAllowed("user")
    public <T extends IObject> List<Class<T>> getResultTypes() {
        // TODO Auto-generated method stub
        return null;

    }

    @RolesAllowed("user")
    public <T extends IObject> List<Class<T>> getContainerTypes() {
        // TODO Auto-generated method stub
        return null;

    }

    @RolesAllowed("user")
    public <T extends IObject> List<Class<T>> getPojoTypes() {
        // TODO Auto-generated method stub
        return null;

    }

    @RolesAllowed("user")
    public <T extends IObject> List<Class<T>> getImportTypes() {
        // TODO Auto-generated method stub
        return null;

    }

    @RolesAllowed("user")
    public <T extends IObject> Permissions permissions(Class<T> k) {
        // TODO Auto-generated method stub
        return null;

    }
    
    /**
     * @see 
     */
    private void worldReadable(IObject obj) {
        Permissions p = obj.getDetails().getPermissions();
        if (p == null) {
            p = new Permissions(Permissions.WORLD_IMMUTABLE);
            obj.getDetails().setPermissions(p);
        } else {
            p.grant(Role.GROUP, Right.READ);
            p.grant(Role.WORLD, Right.READ);
        }
    }

}