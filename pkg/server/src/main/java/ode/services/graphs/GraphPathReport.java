package ode.services.graphs;

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

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.type.CustomType;
import org.hibernate.type.EnumType;
import org.hibernate.type.ListType;
import org.hibernate.type.MapType;
import org.hibernate.type.Type;
import org.hibernate.usertype.UserType;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import ode.model.units.GenericEnumType;
import ode.model.units.Unit;
import ode.services.scheduler.ThreadPool;
import ode.system.OdeContext;
import ode.tools.hibernate.ListAsSQLArrayUserType;

/**
 * A standalone tool for producing a summary of the Hibernate object mapping for our Sphinx documentation. One may invoke it with
 * <code>java -cp lib/server/\* `bin/ode config get | awk '{print"-D"$1}'` ode.services.graphs.GraphPathReport EveryObject.rst</code>.
 * If not using {@code |} prefixes then one may transform the output via {@code fold -sw72}.
 *
 */
public class GraphPathReport {

    private static GraphPathBean model;
    private static Writer out;

    /**
     * Trim the package name off a full class name.
     * @param fullName the full class name
     * @return the class' simple name
     */
    private static String getSimpleName(String fullName) {
        return fullName.substring(1 + fullName.lastIndexOf('.'));
    }

    /**
     * @param className a class name
     * @return a Sphinx label for that class
     */
    private static String labelFor(String className) {
        return "Bhojpur ODE model class " + className;
    }

    /**
     * @param className a class name
     * @param propertyName a property name
     * @return a Sphinx label for that class property
     */
    @SuppressWarnings("unused")
    private static String labelFor(String className, String propertyName) {
        return "Bhojpur ODE model property " + className + '.' + propertyName;
    }

    /**
     * @param className a class name
     * @return a Sphinx link to that class
     */
    private static String linkTo(String className) {
        final StringBuffer sb = new StringBuffer();
        sb.append(":ref:");
        sb.append('`');
        sb.append(className);
        sb.append(' ');
        sb.append('<');
        sb.append(labelFor(className));
        sb.append('>');
        sb.append('`');
        return sb.toString();
    }

    /**
     * @param className a class name
     * @param propertyName a property name
     * @return a Sphinx link to that class property
     */
    private static String linkTo(String className, String propertyName) {
        final StringBuffer sb = new StringBuffer();
        sb.append(":ref:");
        sb.append('`');
        sb.append(className);
        sb.append('.');
        sb.append(propertyName);
        sb.append(' ');
        sb.append('<');
        sb.append(labelFor(className/*, propertyName*/));
        sb.append('>');
        sb.append('`');
        return sb.toString();
    }

    /**
     * @param className the name of a Bhojpur ODE model Java class
     * @return a Sphinx link to that class' documentation
     */
    private static String linkToJavadoc(String className) {
        final StringBuffer sb = new StringBuffer();
        sb.append(":javadoc_model:");
        sb.append('`');
        sb.append(getSimpleName(className));
        sb.append(' ');
        sb.append('<');
        sb.append(className.replace('.', '/'));
        sb.append(".html");
        sb.append('>');
        sb.append('`');
        return sb.toString();
    }

    /**
     * Find a Sphinx representation for a mapped property value type.
     * @param type a Hibernate type
     * @return a reportable representation of that type
     */
    private static String reportType(String className, String propertyName) {
        final Type type = model.getPropertyType(className, propertyName);
        final UserType userType;
        if (type instanceof CustomType) {
            userType = ((CustomType) type).getUserType();
        } else {
            userType = null;
        }
        if (type instanceof EnumType) {
            return "enumeration";
        } else if (userType instanceof GenericEnumType) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            final Class<? extends Unit> unitQuantityClass = ((GenericEnumType) userType).getQuantityClass();
            return "enumeration of " + linkToJavadoc(unitQuantityClass.getName());
        } else if (type instanceof ListType || userType instanceof ListAsSQLArrayUserType) {
            return "list";
        } else if (type instanceof MapType) {
            return "map";
        } else {
            return "``" + type.getName() + "``";
        }
    }

    /**
     * Check if the given class has a {@code @Deprecated} annotation.
     * @param className the name of a class
     * @return if the class is deprecated
     */
    private static boolean isDeprecated(String className) {
        final Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("failed to review " + className + " for deprecation", e);
        }
        return clazz.getAnnotation(Deprecated.class) != null;
    }

    /**
     * Check if the given property's getter has a {@code @Deprecated} annotation.
     * @param className the name of a class
     * @param propertyName the name of one of the given class' properties
     * @return if the property's getter is deprecated
     */
    private static boolean isDeprecated(String className, String propertyName) {
        if (propertyName.startsWith("details.")) {
            return false;
        }
        final String propertyNameCap = propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
        final Collection<String> getterNames = ImmutableSet.of("is" + propertyNameCap, "get" + propertyNameCap);
        final Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("failed to review " + className + " for deprecation", e);
        }
        for (final Method method : clazz.getMethods()) {
            if (getterNames.contains(method.getName()) && method.getAnnotation(Deprecated.class) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Process the Hibernate domain object model and write a report of the mapped objects.
     * @throws IOException if there was a problem in writing to the output file
     */
    private static void report() throws IOException {
        /* the information is gathered, now write the report */
        out.write(".. Content for this page is generated using\n");
        out.write(".. https://github.com/bhojpur/ode/blob/master/");
        out.write("pkg/src/main/java/ode/services/graphs/GraphPathReport.java\n\n");
        out.write("Glossary of all Bhojpur ODE Model Objects\n");
        out.write("===================================\n\n");
        out.write("Overview\n");
        out.write("--------\n\n");
        out.write(".. include:: EveryObjectOverview.inc\n\n");
        out.write("Reference\n");
        out.write("---------\n\n");
        final SortedMap<String, String> classNames = new TreeMap<String, String>();
        for (final String className : model.getAllClasses()) {
            classNames.put(getSimpleName(className), className);
        }
        for (final Map.Entry<String, String> classNamePair : classNames.entrySet()) {
            final String simpleName = classNamePair.getKey();
            final String className = classNamePair.getValue();
            /* label the class heading */
            out.write(".. _" + labelFor(simpleName) + ":\n\n");
            out.write(simpleName + "\n");
            final char[] underline = new char[simpleName.length()];
            for (int i = 0; i < underline.length; i++) {
                underline[i] = '"';
            }
            out.write(underline);
            out.write("\n\n");
            /* note the class' relationships */
            final SortedSet<String> superclassOf = new TreeSet<String>();
            for (final String subclass : model.getDirectSubclassesOf(className)) {
                superclassOf.add(linkTo(getSimpleName(subclass)));
            }
            final SortedSet<String> linkerText = new TreeSet<String>();
            for (final Map.Entry<String, String> linker : model.getLinkedBy(className)) {
                linkerText.add(linkTo(getSimpleName(linker.getKey()), linker.getValue()));
            }
            if (!(superclassOf.isEmpty() && linkerText.isEmpty())) {
                /* write the class' relationships */
                if (!superclassOf.isEmpty()) {
                    out.write("Subclasses: " + Joiner.on(", ").join(superclassOf) + "\n\n");
                }
                if (!linkerText.isEmpty()) {
                    out.write("Used by: " + Joiner.on(", ").join(linkerText) + "\n\n");
                }
            }
            /* write the class' properties */
            out.write("Properties:\n");
            final SortedMap<String, String> declaredBy = new TreeMap<String, String>();
            final Map<String, String> valueText = new HashMap<String, String>();
            for (final String superclassName : model.getSuperclassesOfReflexive(className)) {
                for (final Map.Entry<String, String> classAndPropertyNames : model.getLinkedTo(superclassName)) {
                    final String valueClassName = classAndPropertyNames.getKey();
                    final String propertyName = classAndPropertyNames.getValue();
                    declaredBy.put(propertyName, superclassName);
                    valueText.put(propertyName, linkTo(getSimpleName(valueClassName)));
                }
                for (final String propertyName : model.getSimpleProperties(superclassName, true)) {
                    declaredBy.put(propertyName, superclassName);
                    valueText.put(propertyName, reportType(superclassName, propertyName));
                }
            }
            int deprecatedPropertyCount = 0;
            for (final Map.Entry<String, String> propertyAndDeclarerNames : declaredBy.entrySet()) {
                final String propertyName = propertyAndDeclarerNames.getKey();
                final String declarerName = propertyAndDeclarerNames.getValue();
                out.write("  | " + propertyName + ": " + valueText.get(propertyName));
                switch (model.getPropertyKind(declarerName, propertyName)) {
                case OPTIONAL:
                    out.write(" (optional)");
                    break;
                case REQUIRED:
                    break;
                case COLLECTION:
                    out.write(" (multiple)");
                    break;
                }
                if (isDeprecated(className, propertyName)) {
                    deprecatedPropertyCount++;
                    out.write(" (deprecated)");
                }
                if (!declarerName.equals(className)) {
                    out.write(" from " + linkTo(getSimpleName(declarerName)));
                } else if (!propertyName.startsWith("details.")) {
                    final String interfaceName = model.getInterfaceImplemented(className, propertyName);
                    if (interfaceName != null) {
                        out.write(", see " + linkToJavadoc(interfaceName));
                    }
                }
                out.write("\n");
            }
            out.write("\n");
            if (isDeprecated(className)) {
                out.write(".. warning:: This model object is deprecated.\n\n");
            } else if (deprecatedPropertyCount == 1) {
                out.write(".. warning:: This model object has a deprecated property.\n\n");
            } else if (deprecatedPropertyCount > 1) {
                out.write(".. warning:: This model object has deprecated properties.\n\n");
            }
        }
    }

    /**
     * Generate a Sphinx report of Bhojpur ODE Hibernate entities.
     * @param argv the output filename
     * @throws IOException if the report cannot be written to the file
     */
    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("must give output filename as single argument");
            System.exit(1);
        }
        out = new FileWriter(argv[0]);
        final OdeContext context = OdeContext.getManagedServerContext();
        model = context.getBean("graphPathBean", GraphPathBean.class);
        report();
        out.close();
        context.getBean("threadPool", ThreadPool.class).getExecutor().shutdown();
        context.closeAll();
    }
}
