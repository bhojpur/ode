package ode.services.server.util;

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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ode.api.ServiceInterface;
import ode.system.OdeContext;
import ode.ServerError;
import ode.util.IceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * {@link Method}-cache primed either with an {@link ServiceInterface} instance
 * or with a {@link Class} with generic type {@link ServiceInterface}. Actual
 * invocation happens via
 * {@link #invoke(Object, Ice.Current, IceMapper, Object[])}
 * 
 * No reference is held to the initial priming argument in
 * {@link IceMethodInvoker#IceMethodInvoker(ServiceInterface, OdeContext)}
 * just the class.
 * 
 * MAPPING RULES:
 * <ul>
 * <li>Method names exact</li>
 * <li>Collections of the same type only (no arrays)</li>
 * <li>Primitives use Ice primitives (long, int, bool,...)</li>
 * <li>Primitive wrappers all use RTypes (RLong, RInt, RBool,...)</li>
 * </ul>
 * 
 * It is also possible to have this class not handle mapping arguments and
 * return values by passing a return value mapper.
 * 
 * Future:
 * <ul>
 * <li>Currently ignoring
 * {@link ode.annotations.NotNull} annotations
 * </li>
 * </ul>
 */
public class IceMethodInvoker {

    private static Logger log = LoggerFactory.getLogger(IceMethodInvoker.class);

    static class Info {
        Method method;

        Class<?>[] params;

        Class<?> retType;

        int[] switches;
    }

    private final static Map<Class<?>, Map<String, Info>> staticmap = new HashMap<Class<?>, Map<String, Info>>();

    private final Class<?> serviceClass;

    private OdeContext ctx;

    /**
     * Create an {@link IceMethodInvoker} instance using the {@link Class} of
     * the passed argument to call
     * {@link IceMethodInvoker#IceMethodInvoker(Class, OdeContext)}.
     * 
     * @param srv
     *            A Non-null {@link ServiceInterface} instance.
     * @param context
     *            The active {@link OdeContext} instance.
     */
    public IceMethodInvoker(ServiceInterface srv, OdeContext context) {
        this(srv.getClass(), context);
    }

    /**
     * Creates an {@link IceMethodInvoker} instance by using reflection on the
     * {@link Class} argument. All information is cached internally in a static
     * {@link #staticmap map} if the given service class argument has not
     * already been cached.
     * 
     * @param <S>
     *            A type which subclasses {@link ServiceInterface}
     * @param context
     *            A non-null {@link ServiceInterface} {@link Class}
     */
    public <S extends ServiceInterface> IceMethodInvoker(Class<S> k,
            OdeContext context) {

        this.serviceClass = k;
        this.ctx = context;

        if (!staticmap.containsKey(this.serviceClass)) {
            synchronized (staticmap) {
                // Re-check in case already added
                if (!staticmap.containsKey(this.serviceClass)) {
                    Map<String, Info> map = new HashMap<String, Info>();
                    Method[] ms = this.serviceClass.getMethods();
                    for (Method m : ms) {
                        Info i = new Info();
                        i.method = m;
                        i.params = m.getParameterTypes();
                        i.retType = m.getReturnType();
                        map.put(m.getName(), i);
                    }
                    staticmap.put(this.serviceClass, map);
                }
            }
        }
    }

    Map<String, Info> map() {
        return staticmap.get(serviceClass);
    }

    /**
     * Checks for a void return type, which is needed to know what type of
     * ice_response() method to invoke.
     */
    public boolean isVoid(Ice.Current current) {
        Info info = map().get(current.operation);
        return info.retType.equals(void.class);
    }

    /**
     * Calls the method named in {@link Ice.Current#operation} with the
     * arguments provided mapped via the {@link IceMapper} instance. The return
     * value or any method which is thrown is equally mapped and returned.
     * Exceptions are handled by
     * {@link IceMapper#handleException(Throwable, OdeContext)}.
     * 
     * @param obj
     *            Instance for the call to
     *            {@link Method#invoke(Object, Object[])}. Can be null if this
     *            is a static call.
     * @param current
     *            The current Ice operation. Non-null.
     * @param mapper
     *            A non-null mapper.
     * @param args
     *            The proper number of arguments for the method specified in
     *            current.
     * @return Either the return value of the invocation, or the exception if
     *         one was thrown.
     */
    public Object invoke(Object obj, Ice.Current current, IceMapper mapper,
            Object... args) throws Ice.UserException {

        Assert.notNull(mapper, "IceMapper cannot be null");
        Assert.notNull(current, "Ice.Current cannot be null");

        final Info info = map().get(current.operation);
        if (info == null) {
            throw new IllegalArgumentException("Unknown method:"
                    + current.operation);
        }

        final Object[] objs = arguments(current, mapper, info, args);

        Object retVal = null;
        try {
            retVal = info.method.invoke(obj, objs);
        } catch (Throwable t) {
            throw mapper.handleException(t, ctx);
        }

        // Handling case of generics (e.g. Search.next())
        // in which case we cannot properly handle the mapping.
        Class<?> retType = info.retType;
        if (retType == Object.class && retVal != null) {
            retType = retVal.getClass();
        }

        // If we have a returnValueMapper then it's that objects responsibility
        // to convert the return value, otherwise this class must do it.
        if (mapper.canMapReturnValue()) {
            return mapper.mapReturnValue(retVal);
        } else {
            return mapper.handleOutput(retType, retVal);
        }
    }

    /**
     * Maps input parameters from ode.* to ode.* types if the
     * returnValueMapper is non-null. If it is null, then it is assumed that the
     * responsibility falls to this class.
     */
    private Object[] arguments(Ice.Current current, IceMapper mapper,
            Info info, Object... args) throws ServerError {

        if (mapper.canMapReturnValue()) {
            return args; // EARLY EXIT!
        }

        // Alias
        Class<?>[] params = info.params;

        if (params.length != args.length) {
            throw new IllegalArgumentException("Must provide " + params.length
                    + " arguments for " + current.operation + " not "
                    + args.length);
        }

        // The Mapped argument parameters to be passed to the
        // ServiceInterface instance.
        Object[] objs = new Object[params.length];

        // be sure to use our own types
        for (int i = 0; i < params.length; i++) {
            Class<?> p = params[i];
            Object arg = args[i];
            objs[i] = mapper.handleInput(p, arg);
            // This check duplicates what should be in handleInput
            // if (null != objs[i] && !isPrimitive(p) && // FIXME need way
            // to check autoboxing.
            // !p.isAssignableFrom(objs[i].getClass())) {
            // throw new IllegalStateException(String.format(
            // "Cannot assign %s to %s",objs[i],p));
            // }
        }
        return objs;

    }

    /** For testing the cached method. */
    public Method getMethod(String name) {
        return map().get(name).method;
    }

}