package ode.formats.model;

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
import java.util.HashMap;
import java.util.Map;

import ode.formats.enums.EnumerationProvider;
import ode.formats.model.handler.ModelObjectHandlerFactory;
import ode.model.IObject;

/**
 * An instance provider which uses reflection to fulfill the contract of an
 * InstanceProvider. Its main feature is the delegation of class specific
 * logic to handlers. Bhojpur ODE data model enumeration not-null constraints, for
 * example.
 */
public class ServerInstanceProvider implements InstanceProvider {

  /** Model object handler factory. */
  private ModelObjectHandlerFactory modelObjectHandlerFactory;

  /** Constructor cache. */
  private Map<Class<? extends IObject>,
              Constructor<? extends IObject>> constructorCache =
      new HashMap<Class<? extends IObject>, Constructor<? extends IObject>>();

  /**
   * Default constructor.
   * @param enumProvider Enumeration provider we are to use.
   */
  public ServerInstanceProvider(EnumerationProvider enumProvider) {
    modelObjectHandlerFactory = new ModelObjectHandlerFactory(enumProvider);
  }

  /* (non-Javadoc)
   * @see ode.formats.model.InstanceProvider#getInstance(java.lang.Class)
   */
  public <T extends IObject> T getInstance(Class<T> klass)
      throws ModelException {
    try {
      Constructor<T> constructor = getConstructor(klass);
      IObject o = constructor.newInstance();
      return (T) modelObjectHandlerFactory.getHandler(klass).handle(o);
    } catch (Exception e) {
      String m = "Unable to instantiate object.";
      throw new ModelException(m, klass, e);
    }
  }

  /**
   * Retrieves a constructor for a given class from the constructor cache if
   * possible.
   * @param klass Class to retrieve a constructor for.
   * @return See above.
   * @throws ModelException If there is an error retrieving the constructor.
   */
  private <T extends IObject> Constructor<T> getConstructor(Class<T> klass)
      throws ModelException {
    Constructor<? extends IObject> constructor = constructorCache.get(klass);
    if (constructor == null) {
      try {
        Class<T> concreteClass =
            (Class<T>) Class.forName(klass.getName() + "I");
        constructor = concreteClass.getDeclaredConstructor();
        constructorCache.put(klass, constructor);
      } catch (Exception e) {
        String m = "Unable to retrieve constructor.";
        throw new ModelException(m, klass, e);
      }
    }
    return (Constructor<T>) constructor;
  }

}