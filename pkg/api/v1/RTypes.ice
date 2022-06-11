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

#ifndef ODE_RTYPES_ICE
#define ODE_RTYPES_ICE

//
// Simple type definitions used for remoting purposes.
// See README.ice for a description of the Bhojpur ODE module.
//
module ode {

  /**
   * Simple base ""protected"" class. Essentially abstract.
   **/
  ["protected"] class RType
  {
    /**
     * Equals-like functionality for all RTypes. A return value
     * of 0 means they are equivalent and were almost certainly
     * created by the same constructor call, e.g.
     *
     * <pre>
     *   rbool(true).compare(rbool(true)) == 0
     * </pre>
     *
     * This method was originally added (Oct 2008) to force the
     * base RType class to be abstract in all languages.
     **/
    int compare(RType rhs);
  };

  /**
   * Boolean wrapper.
   **/
  ["protected"] class RBool extends RType
  {
    bool val;
    bool getValue();
  };


  /**
   * Double wrapper.
   **/
  ["protected"] class RDouble extends RType
  {
    double val;
    double getValue();
  };


  /**
   * Float wrapper.
   **/
  ["protected"] class RFloat extends RType
  {
    float val;
    float getValue();
  };


  /**
   * Integer wrapper.
   **/
  ["protected"] class RInt extends RType
  {
    int val;
    int getValue();
  };


  /**
   * Long Wrapper.
   **/
  ["protected"] class RLong extends RType
  {
    long val;
    long getValue();
  };


  /**
   * String wrapper.
   **/
  ["protected"] class RString extends RType
  {
    string val;
    string getValue();
  };

  /**
   * Extends RString and simply provides runtime
   * information to the server that this string
   * is intended as a ""protected"" class parameter. Used especially
   * by {@code ode.system.ParamMap} (ode/System.ice)
   * 
   * Usage:
   * <pre>
   * {@code
   *   ode::RClass c = ...; // from service
   *   if (!c.null && c.val.equals("Image")) { ... }
   * }
   * </pre>
   **/
  ["protected"] class RClass extends RString
  {
  };

  /**
   * A simple Time implementation. The long value is the number
   * of milliseconds since the epoch (January 1, 1970).
   **/
  ["protected"] class RTime extends RType
  {
    long val;
    long getValue();
  };

  // Collections

  /**
   * Simple sequence of {@code RType} instances. Note: when passing
   * an {@code RTypeSeq} over the wire, null sequence is maintained and
   * will be turned into an empty sequence. If nullability is
   * required, see the {@code RCollection} types.
   *
   * @see RCollection
   */
  ["java:type:java.util.ArrayList<ode.RType>:java.util.List<ode.RType>"]
  sequence<RType> RTypeSeq;

  /**
   *
   **/
  ["java:type:java.util.ArrayList<java.util.List<ode.RType>>:java.util.List<java.util.List<ode.RType>>"]
  sequence<RTypeSeq> RTypeSeqSeq;

  /**
   * The collection ""protected"" classes permit the passing of sequences of all
   * other RTypes (including other collections) and it is itself nullable. The
   * allows for similar arguments to collections in languages with a unified
   * inheritance hierarchy (e.g., Java in which all ""protected"" classes extend
   * from java.lang.Object).
   *
   * Unlike the other rtypes which are used internally within the
   * {@code ode.model} classes, these types are mutable since they solely
   * pass through the
   *
   * This flexible mechanism is not used in all API calls because
   * the flexibility brings a performance penalty.
   **/
  ["protected"] class RCollection extends RType
  {
    RTypeSeq val;
    RTypeSeq getValue();
    int size();
    RType get(int index);
    void add(RType value);
    void addAll(RTypeSeq value);
  };

  /**
   * {@code RCollection} mapped to an array on the server of a type given
   * by a random member of the RTypeSeq. Only pass consistent arrays!
   * homogeneous lists.
   **/
  ["protected"] class RArray extends RCollection
  {
  };

  /**
   * {@code RCollection} mapped to a java.util.List on the server
   **/
  ["protected"] class RList extends RCollection
  {
  };

  /**
   * {@code RCollection} mapped to a java.util.HashSet on the server
   **/
  ["protected"] class RSet extends RCollection
  {
  };

  /**
   * Simple dictionary of {@code RType} instances. Note: when passing
   * an RTypeDict over the wire, a null map will not be maintained and
   * will be turned into an empty map. If nullability is
   * required, see the {@code RMap} type.
   **/
  ["java:type:java.util.HashMap<String,ode.RType>"]
  dictionary<string,ode::RType> RTypeDict;

  /**
   * Similar to {@code RCollection}, the {@code RMap} class permits the passing
   * of a possible null {@code RTypeDict} where any other {@code RType} is
   * expected.
   *
   * @see RCollection
   **/
  ["protected"] class RMap extends RType {
    RTypeDict val;
    RTypeDict getValue();
    int size();
    RType get(string key);
    void put(string key, RType value);
  };
};

#endif // ODE_RTYPES_ICE