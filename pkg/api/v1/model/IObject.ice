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

#ifndef ODE_MODEL_IOBJECT
#define ODE_MODEL_IOBJECT

#include <ode/RTypes.ice>
#include <ode/ModelF.ice>

module ode {

  module model {

    /**
     * Base class of all model types. On the
     * server, the interface ode.model.IObject
     * unifies the model. In Ice, interfaces have
     * a more remote connotation.
     */
    ["protected"] class IObject
    {
      /**
       * The database id for this entity. Of RLong value
       * so that transient entities can have a null id.
       */
      ode::RLong id;

      /**
       * Internal details (permissions, owner, etc.) for
       * this entity. All entities have Details, and even
       * a newly created object will have a non-null
       * Details instance. (In the ODE provided mapping!)
       */
      ode::model::Details details;

      /**
       * An unloaded object contains no state other than id. An
       * exception will be raised if any field other than id is
       * accessed via the ODE-generated methods. Unloaded objects
       * are useful as pointers or proxies to server-side state.
       */
      bool loaded;

      // METHODS
      // =====================================================

      // Accessors

      ode::RLong getId();

      void setId(ode::RLong id);

      ode::model::Details getDetails();

      /**
       * Return another instance of the same type as this instance
       * constructed as if by: new InstanceI( this.id.val, false );
       */
      IObject proxy();

      /**
       * Return another instance of the same type as this instance
       * with all single-value entities unloaded and all members of
       * collections also unloaded.
       */
      IObject shallowCopy();

      /**
       * Sets the loaded boolean to false and empties all state
       * from this entity to make sending it over the network
       * less costly.
       */
      void unload();

      /**
       * Each collection can also be unloaded, independently
       * of the object itself. To unload all collections, use:
       *
       *    object.unloadCollections();
       *
       * This is useful when it is possible that a collection no
       * longer represents the state in the database, and passing the
       * collections back to the server might delete some entities.
       *
       * Sending back empty collections can also save a significant
       * amount of bandwidth, when working with large data graphs.
       */
      void unloadCollections();

      /**
       * As with collections, the objects under details can link
       * to many other objects. Unloading the details can same
       * bandwidth and simplify the server logic.
       */
      void unloadDetails();

      /**
       * Tests if the objects are loaded or not. If this value is false, then
       * any method call on this instance other than getId
       * or setId will result in an exception.
       **/
      bool isLoaded();

      // INTERFACE METHODS
      // =====================================================
      // The following methods are a replacement for interfaces
      // so that all language bindings have access to the type
      // safety available in Java. Making these into IObject
      // subclasses would not work, since slice does not support
      // multiple inheritance.

      /**
       * Marker interface which means that special rules apply
       * for both reading and writing these instances.
       */
      bool isGlobal();

      /**
       * A link between two other types.
       * Methods provided:
       *
       *   - getParent()
       *   - getChild()
       */
      bool isLink();

      /**
       * The server will persist changes made to these types.
       * Methods provided:
       *
       *   - getVersion()
       *   - setVersion()
       *
       */
      bool isMutable();

      /**
       * Allows for the attachment of any ode.model.Annotation
       * subclasses. Methods provided are:
       *
       *   - linkAnnotation(Annotation)
       *   -
       */
      bool isAnnotated();


    };
  };
};
#endif