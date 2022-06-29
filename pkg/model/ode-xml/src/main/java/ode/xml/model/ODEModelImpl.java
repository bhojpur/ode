package ode.xml.model;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODEModelImpl implements ODEModel {

  private Map<String, ODEModelObject> modelObjects = 
    new HashMap<String, ODEModelObject>();

  private Map<ODEModelObject, List<Reference>> references =
    new HashMap<ODEModelObject, List<Reference>>();

  /** Logger for this class. */
  private static final Logger LOGGER =
    LoggerFactory.getLogger(ODEModelImpl.class);

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#removeModelObject(java.lang.String)
   */
  @Override
  public ODEModelObject removeModelObject(String id) {
    return modelObjects.remove(id);
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#addModelObject(java.lang.String, ode.xml.model.ODEModelObject)
   */
  @Override
  public ODEModelObject addModelObject(String id, ODEModelObject object) {
    if (Reference.class.isAssignableFrom(object.getClass())) {
      return object;
    }
    return modelObjects.put(id, object);
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#getModelObject(java.lang.String)
   */
  @Override
  public ODEModelObject getModelObject(String id) {
    return modelObjects.get(id);
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#getModelObjects()
   */
  @Override
  public Map<String, ODEModelObject> getModelObjects() {
    return modelObjects;
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#addReference(java.lang.String, ode.xml.model.Reference)
   */
  @Override
  public boolean addReference(ODEModelObject a, Reference b) {
    if (b == null) {
      return false;
    }
    List<Reference> bList = references.get(a);
    if (bList == null) {
      bList = new ArrayList<Reference>();
      references.put(a, bList);
    }
    return bList.add(b);
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#getReferences()
   */
  @Override
  public Map<ODEModelObject, List<Reference>> getReferences() {
    return references;
  }

  /* (non-Javadoc)
   * @see ode.xml.model.ODEModel#resolveReferences()
   */
  @Override
  public int resolveReferences() {
    int unhandledReferences = 0;
    for (Entry<ODEModelObject, List<Reference>> entry : references.entrySet())
    {
      ODEModelObject a = entry.getKey();
      if (a == null) {
        List<Reference> references = entry.getValue();
        if (references == null) {
          LOGGER.error("Null reference to null object, continuing.");
          continue;
        }
        LOGGER.error("Null reference to {} objects, continuing.",
                     references.size());
        unhandledReferences += references.size();
        continue;
      }
      for (Reference reference : entry.getValue()) {
        String referenceID = reference.getID();
        ODEModelObject b = getModelObject(referenceID);
        if (b == null) {
          LOGGER.warn("{} reference to {} missing from object hierarchy.",
                      a, referenceID);
          unhandledReferences++;
          continue;
        }
        a.link(reference, b);
      }
    }
    return unhandledReferences;
  }

}