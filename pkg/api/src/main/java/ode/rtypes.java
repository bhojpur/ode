package ode;

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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ReflectionUtils;

import ode.model.IObject;
import ode.util.IceMapper;
import ode.util.ObjectFactoryRegistry;
import Ice.Current;

/**
 * Abstract class similar to {@link java.util.Arrays} or
 * {@link java.util.Collections} which is responsible for creating RTypes from
 * static factory methods. Where possible, factory methods return cached values
 * (the fly-weight pattern) such that <code>rbool(true) == rbool(true)</code>
 * might hold true.
 *
 * This class is fairly non-traditional Java and instead is more like a Python
 * module or static methods in C++ to keep the three language bindings fairly in
 * step.
 */
public abstract class rtypes {

    // Static state at bottom

    /**
     * Attempts to dispatch to the other ode.rtypes.* static methods
     * to create a proper {@link RType} subclass by checking the type
     * of the input. If null is given, null is returned. Otherwise, where
     * possible an {@link RType} is returned, else {@link ClientError} is
     * thrown.
     */
    public static ode.RType rtype(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof ode.RType) {
            return (ode.RType) obj;
        } else if (obj instanceof Boolean) {
            return rbool((Boolean) obj);
        } else if (obj instanceof Double) {
            return rdouble((Double) obj);
        } else if (obj instanceof Float) {
            return rfloat((Float) obj);
        } else if (obj instanceof Long) {
            return rlong((Long) obj);
        } else if (obj instanceof Integer) {
            return rint((Integer) obj);
        } else if (obj instanceof String) {
            return rstring((String) obj);
        } else if (obj instanceof IObject) {
            return robject((IObject) obj);
        } else if (obj instanceof Internal) {
            return rinternal((Internal) obj);
        } else if (obj instanceof Date) {
            return rtime(((Date) obj).getTime());
        } else if (obj instanceof List) {
            return rlist((List) obj);
        } else if (obj instanceof Set) {
            return rset((Set) obj);
        } else if (obj instanceof Map) {
            return rmap((Map) obj);
        } else {
            throw new ClientError("Cannot handle conversion from: "
                    + obj.getClass());
        }
    }

    /**
     * Descends into data structures wrapping all elements as it goes. 
     * Limitation: All map keys will be converted to strings!
     *
     * Calls {@link #wrap(Object, Map)} with a new cache argument.
     * @param value the value to wrap
     * @return the wrapped value
     */
    public static ode.RType wrap(final Object value) {
        if (value == null) {
            return null;
        }
        Map<Object, RType> cache = new IdentityHashMap<Object, RType>();
        return wrap(value, cache);
    }

    /**
     * Descends into data structures wrapping all elements as it goes. 
     * Limitation: All map keys will be converted to strings!
     *
     * The cache argument is used to prevent cycles.
     * @param value the value to wrap
     * @param cache a cache of wrapped values (to be expanded by misses)
     * @return the wrapped value
     * @throws ode.ClientError if all else fails.
     */
    public static ode.RType wrap(final Object value, final Map<Object, RType> cache) {
        if (cache.containsKey(value)) {
            return cache.get(value);
        } else if (value.getClass().isArray()) {
            final int length = Array.getLength(value);
            final RArray rv = ode.rtypes.rarray();
            cache.put(value,  rv);
            for (int i = 0; i < length; i++) {
                rv.getValue().add(
                        wrap(Array.get(value, i), cache));
            }
            return rv;
        } else if (value instanceof List) {
            final List<?> list = (List<?>) value;
            final RList rv = ode.rtypes.rlist();
            cache.put(value, rv);
            for (int i = 0; i < list.size(); i++) {
                rv.getValue().add(wrap(list.get(i), cache));
            }
            return rv;
        } else if (value instanceof Map) {
            final Map<?, ?> map = (Map<?, ?>) value;
            final RMap rv = ode.rtypes.rmap();
            cache.put(value, rv);
            final Map<String, ode.RType> val = rv.getValue();
            for (final Object key : map.keySet()) {
                val.put(key.toString(), wrap(map.get(key), cache));
            }
            return rv;
        } else if (value instanceof Set) {
            final Set<?> set = (Set<?>) value;
            final RSet rv = ode.rtypes.rset();
            cache.put(value, rv);
            for (final Object element : set) {
                rv.getValue().add(wrap(element, cache));
            }
            return rv;
        } else {
            return ode.rtypes.rtype(value);
        }
    }


    /**
     * Descends into data structures unwrapping all RType objects as it goes.
     * Limitation: RArrays are turned into {@link List} instances!
     *
     * Calls {@link #unwrap(RType, Map)} with a new cache argument.
     * @param value the wrapped value
     * @return the value unwrapped
     */
    public static Object unwrap(final RType value) {
        if (value == null) {
            return null;
        }
        Map<RType, Object> cache = new IdentityHashMap<RType, Object>();
        return unwrap(value, cache);
    }

    /**
     * Descends into data structures wrapping all elements as it goes. 
     * Limitation: RArrays are turned into {@link List} instances!
     *
     * The cache argument is used to prevent cycles.
     * @param value the wrapped value
     * @param cache a cache of unwrapped values (to be expanded by misses)
     * @return the value unwrapped
     */
    public static Object unwrap(final RType value, final Map<RType, Object> cache) {
        if (cache.containsKey(value)) {
            return cache.get(value);
        } else if (value instanceof RArray) {
            List<RType> rtypes = ((RArray) value).getValue();
            List<Object> rv = new ArrayList<Object>(rtypes.size());
            cache.put(value, rv);
            unwrapCollection(rtypes, rv, cache);
            return rv;
        } else if (value instanceof RList) {
            List<RType> rtypes = ((RList) value).getValue();
            List<Object> rv = new ArrayList<Object>(rtypes.size());
            cache.put(value, rv);
            unwrapCollection(rtypes, rv, cache);
            return rv;
        } else if (value instanceof RSet) {
            List<RType> rtypes = ((RSet) value).getValue();
            Set<Object> rv = new HashSet<Object>(rtypes.size());
            cache.put(value, rv);
            unwrapCollection(rtypes, rv, cache);
            return rv;
        } else if (value instanceof RMap) {
            Map<String, RType> map = ((RMap) value).getValue();
            Map<String, Object> rv = new HashMap<String, Object>();
            cache.put(value, rv);
            for (Map.Entry<String, RType> entry : map.entrySet()) {
                rv.put(entry.getKey(), unwrap(entry.getValue(), cache));
            }
            return rv;
        } else {
            Field f = ReflectionUtils.findField(value.getClass(), "val");
            f.setAccessible(true);
            Object rv = ReflectionUtils.getField(f, value);
            cache.put(value, rv);
            return rv;
        }
    }

    protected static void unwrapCollection(final Collection<RType> rtypes,
            final Collection<Object> rv, final Map<RType, Object> cache) {
        for (RType rtype : rtypes) {
            rv.add(unwrap(rtype, cache));
        }
    }

    // =========================================================================

    public static ode.RBool rbool(boolean val) {
        return val ? rtrue : rfalse;
    }

    public static ode.RDouble rdouble(double val) {
        return new RDoubleI(val);
    }

    public static ode.RFloat rfloat(float val) {
        return new RFloatI(val);
    }

    public static ode.RInt rint(int val) {
        if (val == 0) {
            return rint0;
        }
        return new RIntI(val);
    }

    public static ode.RLong rlong(long val) {
        if (val == 0) {
            return rlong0;
        }
        return new RLongI(val);
    }

    public static ode.RTime rtime(long val) {
        return new RTimeI(val);
    }

    public static ode.RTime rtime(Date date) {
        return date == null ? null : new RTimeI(date.getTime());
    }

    public static ode.RTime rtime_min() {
        String tstr = "0001-01-01 00:00:00";
        return rtime_str(tstr);
    }

    public static ode.RTime rtime_max() {
        String tstr = "9999-12-31 23:59:59";
        return rtime_str(tstr);
    }

    public static ode.RTime rtime_str(String tstr) {
        Timestamp t = Timestamp.valueOf(tstr);
        return rtime(t.getTime());
    }

    // Static factory methods (objects)
    // =========================================================================

    public static ode.RInternal rinternal(ode.Internal val) {
        if (val == null) {
            return rnullinternal;
        }
        return new RInternalI(val);
    }

    public static ode.RObject robject(IObject val) {
        if (val == null) {
            return rnullobject;
        }
        return new RObjectI(val);
    }

    public static ode.RClass rclass(String val) {
        if (val == null || val.length() == 0) {
            return remptyclass;
        }
        return new RClassI(val);
    }

    public static ode.RString rstring(String val) {
        if (val == null || val.length() == 0) {
            return remptystr;
        }
        return new RStringI(val);
    }

    // Static factory methods (collections)
    // =========================================================================

    public static ode.RArray rarray(RType... val) {
        return new RArrayI(val);
    }

    public static ode.RArray rarray(Collection<RType> val) {
        return new RArrayI(val);
    }

    public static ode.RList rlist(RType... val) {
        return new RListI(val);
    }

    public static ode.RList rlist(Collection<RType> val) {
        return new RListI(val);
    }

    public static ode.RSet rset(RType... val) {
        return new RSetI(val);
    }

    public static ode.RSet rset(Collection<RType> val) {
        return new RSetI(val);
    }

    public static ode.RMap rmap() {
        return new RMapI(null);
    }

    public static ode.RMap rmap(Map<String, RType> val) {
        return new RMapI(val);
    }

    public static ode.RMap rmap(String key, RType val) {
        ode.RMap map = rmap(null);
        map.put(key, val);
        return map;
    }

    // Implementations (primitives)
    // =========================================================================

    static class RBoolI extends ode.RBool implements Conversion {

        private RBoolI(boolean value) {
            super(value);
        }

        public boolean getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Boolean.class;
        }

        public Object convert(IceMapper mapper) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RBool)) {
                return false;
            }
            ode.RBool rval = (ode.RBool) obj;
            return rval.val == this.val;
        }

        @Override
        public int hashCode() {
            return this.val ? Boolean.TRUE.hashCode() : Boolean.FALSE
                    .hashCode();
        }

    }

    static class RDoubleI extends ode.RDouble implements Conversion {

        private RDoubleI(double value) {
            super(value);
        }

        public double getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Double.class;
        }

        public Object convert(IceMapper mapper) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RDouble)) {
                return false;
            }
            ode.RDouble rval = (ode.RDouble) obj;
            return Double.compare(rval.val, this.val) == 0;
        }

        @Override
        public int hashCode() {
            long f = Double.doubleToLongBits(val);
            return (int) (f ^ (f >>> 32));
        }
    }

    static class RFloatI extends ode.RFloat implements Conversion {

        private RFloatI(float value) {
            super(value);
        }

        public float getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Float.class;
        }

        public Object convert(IceMapper mapper) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RFloat)) {
                return false;
            }
            ode.RFloat rval = (ode.RFloat) obj;
            return Float.compare(rval.val, this.val) == 0;
        }

        @Override
        public int hashCode() {
            return Float.floatToIntBits(val);
        }
    }

    static class RIntI extends ode.RInt implements Conversion {

        private RIntI(int value) {
            super(value);
        }

        public int getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Integer.class;
        }

        public Object convert(IceMapper mapper) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RInt)) {
                return false;
            }
            ode.RInt rval = (ode.RInt) obj;
            return rval.val == this.val;
        }

        @Override
        public int hashCode() {
            return val;
        }
    }

    static class RLongI extends ode.RLong implements Conversion {

        private RLongI(long value) {
            super(value);
        }

        public long getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Long.class;
        }

        public Object convert(IceMapper mapper) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RLong)) {
                return false;
            }
            ode.RLong rval = (ode.RLong) obj;
            return rval.val == this.val;
        }

        @Override
        public int hashCode() {
            return (int) (val ^ (val >>> 32));
        }
    }

    static class RTimeI extends ode.RTime implements Conversion {

        private RTimeI(long value) {
            super(value);
        }

        public long getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Timestamp.class;
        }

        public Object convert(IceMapper mapper) {
            return mapper.convert(this);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RTime)) {
                return false;
            }
            ode.RTime rval = (ode.RTime) obj;
            return rval.val == this.val;
        }

        @Override
        public int hashCode() {
            return (int) (val ^ (val >>> 32));
        }
    }

    // Implementations (objects)
    // =========================================================================

    static class RInternalI extends ode.RInternal implements Conversion {

        private RInternalI(ode.Internal value) {
            super(value);
        }

        public ode.Internal getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return RInternal.class;
        }

        /**
         * Do nothing. RInternal is intended for us with Bhojpur ODE. See Scripts.ice
         * to explain the temporary solution.
         */
        public Object convert(IceMapper mapper) {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RInternal)) {
                return false;
            }
            ode.RInternal rval = (ode.RInternal) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RObjectI extends ode.RObject implements Conversion {

        private RObjectI(ode.model.IObject value) {
            super(value);
        }

        public ode.model.IObject getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return IObject.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return mapper.reverse(val);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RObject)) {
                return false;
            }
            ode.RObject rval = (ode.RObject) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RStringI extends ode.RString implements Conversion {

        private RStringI(String value) {
            super(value);
        }

        public String getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return String.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RString)) {
                return false;
            }
            ode.RString rval = (ode.RString) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RClassI extends ode.RClass implements Conversion {

        private RClassI(String value) {
            super(value);
        }

        public String getValue(Current __current) {
            return val;
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Class.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return mapper.odeClass(val, true);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RClass)) {
                return false;
            }
            ode.RClass rval = (ode.RClass) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    // Implementations (collections)
    // =========================================================================

    /**
     * Guaranteed to never contain an empty list.
     */
    static class RArrayI extends ode.RArray implements Conversion {

        private RArrayI(RType... arg) {
            if (arg == null || arg.length == 0) {
                val = new ArrayList<RType>();
            } else {
                val = Arrays.asList(arg);
            }
        }

        private RArrayI(Collection<RType> arg) {
            if (arg == null || arg.size() == 0) {
                val = new ArrayList<RType>();
            } else {
                val = new ArrayList<RType>(arg);
            }
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return RType[].class; // FIXME not exactly correct.
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            Object rv;
            Collection<?> reversed = mapper.reverse(val);
            // Assuming all the same
            RType first = val.get(0);
            if (first instanceof Conversion) {
                Conversion conv = (Conversion) first;
                Class<?> k = conv.type();
                rv = Array.newInstance(k, val.size());
                rv = reversed.toArray((Object[]) rv);
                return rv;
            } else {
                throw new ode.ApiUsageException(null, null,
                        "First argument not convertible");
            }
        }

        public List<RType> getValue(Current __current) {
            return val;
        }

        public RType get(int index, Ice.Current current) {
            return val.get(index);
        }

        public int size(Ice.Current current) {
            return val.size();
        }

        public void add(RType value, Ice.Current current) {
            val.add(value);
        }

        public void addAll(java.util.List<RType> value, Ice.Current current) {
            val.addAll(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof ode.RArray) {
                return false;
            }
            ode.RArray rval = (ode.RArray) obj;
            return (val == rval.val || val != null && val.equals(rval.val));

        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RListI extends ode.RList implements Conversion {

        private RListI(RType... arg) {
            if (arg == null || arg.length == 0) {
                val = new ArrayList<RType>();
            } else {
                val = Arrays.asList(arg);
            }
        }

        private RListI(Collection<RType> arg) {
            if (arg == null || arg.size() == 0) {
                val = new ArrayList<RType>();
            } else {
                val = new ArrayList<RType>(arg);
            }
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return List.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return mapper.reverse(val, List.class);
        }

        public List<RType> getValue(Current __current) {
            return val;
        }

        public RType get(int index, Ice.Current current) {
            return val.get(index);
        }

        public int size(Ice.Current current) {
            return val.size();
        }

        public void add(RType value, Ice.Current current) {
            val.add(value);
        }

        public void addAll(java.util.List<RType> value, Ice.Current current) {
            val.addAll(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RList)) {
                return false;
            }
            ode.RList rval = (ode.RList) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RSetI extends ode.RSet implements Conversion {

        private RSetI(RType... arg) {
            if (arg == null || arg.length == 0) {
                val = new ArrayList<RType>();
            } else {
                val = Arrays.asList(arg);
            }
        }

        private RSetI(Collection<RType> arg) {
            if (arg == null || arg.size() == 0) {
                val = new ArrayList<RType>();
            } else {
                val = new ArrayList<RType>(arg);
            }
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Set.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return mapper.reverse(val, Set.class);
        }

        public List<RType> getValue(Current __current) {
            return val;
        }

        public RType get(int index, Ice.Current current) {
            return val.get(index);
        }

        public int size(Ice.Current current) {
            return val.size();
        }

        public void add(RType value, Ice.Current current) {
            val.add(value);
        }

        public void addAll(java.util.List<RType> value, Ice.Current current) {
            val.addAll(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RSet)) {
                return false;
            }
            ode.RSet rval = (ode.RSet) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    static class RMapI extends ode.RMap implements Conversion {

        private RMapI(Map<String, RType> arg) {
            if (arg == null || arg.size() == 0) {
                val = new HashMap<String, RType>();
            } else {
                val = new HashMap<String, RType>(arg);

            }
        }

        public int compare(RType rhs, Ice.Current current) {
            throw new UnsupportedOperationException();
        }

        public Class<?> type() {
            return Map.class;
        }

        public Object convert(IceMapper mapper) throws ApiUsageException {
            return mapper.reverse(val);
        }

        public Map<String, RType> getValue(Current __current) {
            return val;
        }

        public RType get(String key, Current __current) {
            return val.get(key);
        }

        public void put(String key, RType value, Current __current) {
            val.put(key, value);
        }

        public int size(Current __current) {
            return val.size();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof ode.RMap)) {
                return false;
            }
            ode.RMap rval = (ode.RMap) obj;
            return (val == rval.val || val != null && val.equals(rval.val));
        }

        @Override
        public int hashCode() {
            return val == null ? 0 : val.hashCode();
        }
    }

    // Helpers
    // ========================================================================

    /**
     * SPI-style interface which helps the Bhojpur ODE server to properly convert
     * ode.RType objects into Java-native objects for use in Hibernate.
     */
    public interface Conversion {

        /**
         * Specifies the type that can be expected from the
         * {@link #convert(IceMapper)} method.
         */
        public Class<?> type();

        /**
         * Convert the "val" field on the given RType instance to an ode.model.*
         * representation.
         */
        public Object convert(IceMapper mapper) throws ApiUsageException;

    }

    // Shared state (flyweight)
    // =========================================================================

    private final static ode.RBool rtrue = new RBoolI(true);

    private final static ode.RBool rfalse = new RBoolI(false);

    private final static ode.RLong rlong0 = new RLongI(0);

    private final static ode.RInt rint0 = new RIntI(0);

    private final static ode.RString remptystr = new RStringI("");

    private final static ode.RClass remptyclass = new RClassI("");

    private final static ode.RInternal rnullinternal = new RInternalI(null);

    private final static ode.RObject rnullobject = new RObjectI(null);

    // ObjectFactoryRegistry
    // =========================================================================

    public static class RTypeObjectFactoryRegistry extends ObjectFactoryRegistry {

        @Override
        public Map<String, ObjectFactory> createFactories(Ice.Communicator ic) {
            Map<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();
            factories.put(RBool.ice_staticId(), new ObjectFactory(RBool.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RBoolI(false);
                }

            });
            factories.put(RDouble.ice_staticId(), new ObjectFactory(RDouble.ice_staticId()) {
                @Override
                public RType create(String name) {
                    return new RDoubleI(0.0);
                }
            });
            factories.put(RFloat.ice_staticId(), new ObjectFactory(RFloat.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RFloatI(0.0f);
                }
            });
            factories.put(RInt.ice_staticId(), new ObjectFactory(RInt.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RIntI(0);
                }
            });
            factories.put(RLong.ice_staticId(), new ObjectFactory(RLong.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RLongI(0L);
                }
            });
            factories.put(RTime.ice_staticId(), new ObjectFactory(RTime.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RTimeI(0L);
                }
            });
            factories.put(RClass.ice_staticId(), new ObjectFactory(RClass.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RClassI("");
                }
            });
            factories.put(RString.ice_staticId(), new ObjectFactory(RString.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RStringI("");
                }
            });
            factories.put(RInternal.ice_staticId(), new ObjectFactory(RInternal
                    .ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RInternalI(null);
                }
            });
            factories.put(RObject.ice_staticId(),
                    new ObjectFactory(RObjectI.ice_staticId()) {

                        @Override
                        public RType create(String name) {
                            return new RObjectI(null);
                        }
                    });
            factories.put(RArray.ice_staticId(), new ObjectFactory(RArray.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RArrayI();
                }
            });
            factories.put(RList.ice_staticId(), new ObjectFactory(RList.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RListI();
                }
            });
            factories.put(RSet.ice_staticId(), new ObjectFactory(RSet.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RSetI();
                }
            });
            factories.put(RMap.ice_staticId(), new ObjectFactory(RMap.ice_staticId()) {

                @Override
                public RType create(String name) {
                    return new RMapI(null);
                }
            });

            return factories;
        }
    }

}