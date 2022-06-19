package loci.common.services;

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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runtime instantiation of services.
 */
public class ServiceFactory {

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(ServiceFactory.class);

  /** Default service properties file. */
  private static final String DEFAULT_PROPERTIES_FILE = "services.properties";

  /** Constructor cache. */
  private static Map<Class<? extends Service>, Constructor<? extends Service>>
    constructorCache =
      new HashMap<Class<? extends Service>, Constructor<? extends Service>>();

  /**
   *  Set of available services.
   *
   *  This field is initialized on first usage since if this class has been deserialized,
   *  the transient flag will have led to it being null again.
   */
  private transient Map<Class<? extends Service>, Class<? extends Service>>
    services = null;

  /**
   * Constructor argument passed.
   *
   * If null, then the default constructor was used. This information is used to
   * recreate the services instance.
   */
  private String path;

  /** Default service factory. */
  private static ServiceFactory defaultFactory;

  /**
   * Constructor loading service configuration from the default location.
   * @throws DependencyException If there is an error locating or reading from
   * the default configuration location.
   */
  public ServiceFactory() throws DependencyException {
    this.path = null;
    if (defaultFactory == null) {
      defaultFactory = new ServiceFactory(DEFAULT_PROPERTIES_FILE);
    }
    services();
  }

  /**
   * Constructor loading service configuration from a given location.
   * @param path Location to load service configuration from.
   * @throws DependencyException If there is an error locating or reading from
   * <code>path</code>.
   */
  public ServiceFactory(String path) throws DependencyException {
    this.path = path;
    services();
  }

  /**
   * Common constructor code which dispatches based on the state of
   * the path field. This is *not* called during construction, but
   * rather on the first call to services() since the same logic is
   * needed in the deserialization code path. This way, it's only called
   * once.
   */
  private static void init(String path,
    Map<Class<? extends Service>, Class<? extends Service>> services)
      throws DependencyException {

    // Matches the default constructor
    if (path == null) {
      synchronized (defaultFactory) {
        services.putAll(defaultFactory.services);
      }
      return; // EARLY EXIT
    }

    // Now handle the (String path) constructor.

    InputStream stream = ServiceFactory.class.getResourceAsStream(path);
    Properties properties = new Properties();
    if (stream == null) {
      throw new DependencyException(path + " not found on CLASSPATH");
    }
    try {
      properties.load(stream);
      LOGGER.debug("Loaded properties from: {}", path);
    } catch (Throwable t) {
      throw new DependencyException(t);
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException e) {
        LOGGER.warn("Error closing properties file stream.", e);
      }
    }
    Set<Entry<Object, Object>> entries = properties.entrySet();
    for (Entry<Object, Object> entry : entries) {
      String interfaceName = (String) entry.getKey();
      String implementationName = (String) entry.getValue();
      Class<? extends Service> interfaceClass = null;
      Class<? extends Service> implementationClass = null;
      ClassLoader loader = ServiceFactory.class.getClassLoader();
      try {
        interfaceClass = (Class<? extends Service>)
          Class.forName(interfaceName, false, loader);
      }
      catch (Throwable t) {
        LOGGER.debug("CLASSPATH missing interface: {}", interfaceName, t);
        continue;
      }
      try {
        implementationClass = (Class<? extends Service>)
          Class.forName(implementationName, false, loader);
      }
      catch (Throwable t) {
        LOGGER.debug(
          "CLASSPATH missing implementation or implementation dependency: {}",
          implementationName, t);
      }
      services.put(interfaceClass, implementationClass);
      LOGGER.debug("Added interface {} and implementation {}",
        interfaceClass, implementationClass);
    }
  }

  /**
   * Retrieves an instance of a given service.
   * @param <T> generic service type
   * @param type Interface type of the service.
   * @return A newly instantiated service.
   * @throws DependencyException If there is an error instantiating the
   * service instance requested.
   */
  public <T extends Service> T getInstance(Class<T> type)
    throws DependencyException
  {
    Class<T> impl = (Class<T>) services().get(type);
    if (impl == null && services().containsKey(type)) {
      throw new DependencyException(
          "Unable to instantiate service. Missing implementation or " +
          "implementation dependency", type);
    }
    if (impl == null) {
      throw new DependencyException("Unknown service type: " + type);
    }
    Constructor<T> constructor = getConstructor(impl);
    try {
      return constructor.newInstance();
    } catch (Throwable t) {
      throw new DependencyException("Unable to instantiate service", type, t);
    }
  }

  /**
   * Retrieves a constructor for a given class from the constructor cache if
   * possible.
   * @param klass Class to retrieve a constructor for.
   * @return See above.
   * @throws DependencyException If there is an error retrieving the
   * constructor.
   */
  private <T extends Service> Constructor<T> getConstructor(Class<T> klass)
    throws DependencyException
  {
    synchronized (constructorCache) {
      Constructor<? extends Service> constructor =
        constructorCache.get(klass);
      if (constructor == null) {
        try {
          Class<T> concreteClass = (Class<T>) Class.forName(klass.getName());
          constructor = concreteClass.getDeclaredConstructor();
          constructorCache.put(klass, constructor);
        }
        catch (Throwable t) {
          throw new DependencyException(
              "Unable to retrieve constructor", klass, t);
        }
      }
      return (Constructor<T>) constructor;
    }
  }

  /**
   * Called by all *users* of the services field in order to handle field initialization.
   * @return never null
   * @throws DependencyException
   */
  private Map<Class<? extends Service>, Class<? extends Service>> services() throws DependencyException {

    // double-locking pattern
    Map<Class<? extends Service>, Class<? extends Service>> copy = services;
    if (null == copy) {
      synchronized (this) {
        copy = services; // recheck
        if (copy == null) {
          copy = new HashMap<>();
          init(path, copy);
          services = copy;
        }
      }
    }
    return copy;
  }
}