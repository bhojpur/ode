package ode.dsl;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * represents a SemanticType <b>definition</b>.
 */
public abstract class SemanticType {

    // Patterns for reducing name lengths
    final static private Pattern annPattern = Pattern.compile("annotation");
    final static private Pattern cntPattern = Pattern.compile("FK_count_to");
    final static private Pattern grpPattern = Pattern.compile("experimentergroup");
    final static private Pattern acqPattern = Pattern.compile("screenacquisition");

    final static private String VM_QUOTE = "\\\"";

    public final static Set<String> RESTRICTED_COLUMNS = Collections
    .unmodifiableSet(new HashSet<String>(Arrays.asList("column",
            "constant", "file", "group", "mode", "power", "ref",
            "reverse", "rows", "row", "session", "size")));

    public final static Set<String> RESTRICTED_TABLE = Collections
    .unmodifiableSet(new HashSet<String>(Arrays.asList("session", "share")));

    // TYPE identifiers
    public final static String ABSTRACT = "abstract";

    public final static String TYPE = "type";

    public final static String CONTAINER = "container";

    public final static String RESULT = "result";

    public final static String LINK = "link";

    public final static String ENUM = "enum";

    public final static Set<String> TYPES = new HashSet<>();
    static {
        TYPES.add(ABSTRACT);
        TYPES.add(TYPE);
        TYPES.add(CONTAINER);
        TYPES.add(RESULT);
        TYPES.add(LINK);
        TYPES.add(ENUM);
    }

    public final static Map<String, Class<?>> TYPES2CLASSES = new HashMap<>();
    static {
        TYPES2CLASSES.put(ABSTRACT, AbstractType.class);
        TYPES2CLASSES.put(TYPE, BaseType.class);
        TYPES2CLASSES.put(CONTAINER, ContainerType.class);
        TYPES2CLASSES.put(LINK, LinkType.class);
        TYPES2CLASSES.put(RESULT, ResultType.class);
        TYPES2CLASSES.put(ENUM, EnumType.class);
    }

    /**
     * Database profile, i.e. ${ode.db.profile}.
     */
    public final String profile;

    // all properties
    private List<Property> properties = new ArrayList<Property>();

    /** future class name; required for all types */
    private String id;

    private String table;

    /** optional item */
    private SemanticType superclass;

    private String discriminator;

    // possible interfaces
    private Boolean abstrakt;

    private Boolean annotated;

    private Boolean described;

    private Boolean global;

    private Boolean immutable;

    private Boolean named;

    private Boolean deprecated;

    private final Set<String> uniqueConstraints = new HashSet<String>();

    public final Properties databaseTypes;

    /**
     * sets the the various properties available in attrs USING DEFAULTS IF NOT
     * AVAILABLE. Subclasses may override these values.
     */
    public SemanticType(String profile, Properties attrs, Properties databaseTypes) {
        this.profile = profile;
        this.databaseTypes = databaseTypes;
        setId(attrs.getProperty("id", getId()));
        setTable(typeToColumn(getId()));
        if (null == getId()) {
            throw new IllegalStateException("All types must have an id");
        }

        setSuperclass(attrs.getProperty("superclass"));
        setDiscriminator(attrs.getProperty("discriminator"));

        setAnnotated(Boolean.valueOf(attrs.getProperty("annotated", "false")));
        setAbstract(Boolean.valueOf(attrs.getProperty("abstract", "false")));
        setDescribed(Boolean.valueOf(attrs.getProperty("described", "false")));
        setGlobal(Boolean.valueOf(attrs.getProperty("global", "false")));
        setImmutable(Boolean.valueOf(attrs.getProperty("immutable", "false")));
        setNamed(Boolean.valueOf(attrs.getProperty("named", "false")));
        setDeprecated(Boolean.valueOf(attrs.getProperty("deprecated", "false")));

        // TODO add "UnsupportedOperation for any other properties in attrs.
        // same in Property

    }

    /**
     * A database is "restrictive" if it prevents the use of certain columns
     * and table names, both keywords and lengths.
     */
    public boolean isRestrictive() {
        return ! profile.contains("psql");
    }

    public void validate() {
        // Left empty in-case anyone forgets to override.
    }

    /**
     * creates a new type based on the element-valued key in TYPES2CLASSES. Used
     * mainly by the xml reader
     */
    public static SemanticType makeNew(String profile, String element, Properties attributes, Properties databaseTypes)
            throws IllegalArgumentException, IllegalStateException {
        Class<?> klass = TYPES2CLASSES.get(element);

        if (null == klass) {
            throw new IllegalArgumentException(
                    "TYPES2CLASSES does not contain type " + element);
        }

        SemanticType st;

        try {
            st = (SemanticType) klass.getConstructor(
                    new Class[] { String.class, Properties.class, Properties.class }).newInstance(
                    new Object[] { profile, attributes, databaseTypes });
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cannot instantiate class " + klass, e);
        }

        return st;
    }

    @Override
    public String toString() {
        String result = "\n" + getId();
        for (Iterator<Property> it = getProperties().iterator(); it.hasNext();) {
            Property element = it.next();
            result += "\n  " + element.toString();
        }
        return result;
    }

    public static String typeToColumn(String type) {
        return type.substring(type.lastIndexOf(".") + 1).toLowerCase();
    }

    //
    // Getters and Setters
    //

    public void setAbstract(Boolean abstrakt) {
        this.abstrakt = abstrakt;
    }

    public Boolean getAbstract() {
        return abstrakt;
    }

    public void setAnnotated(Boolean annotated) {
        this.annotated = annotated;
    }

    public Boolean getAnnotated() {
        return annotated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getLastDotInId() {
        int idx = id.lastIndexOf(".");
        if (idx < 0) {
            throw new RuntimeException(id + " doesn't have a package. "
                    + "Use of default package prohibited.");
        }
        return idx;
    }

    /**
     * Read-only property
     */
    public String getPackage() {
        return id.substring(0, getLastDotInId());
    }

    /**
     * Read-only property
     */
    public String getShortname() {
        return id.substring(getLastDotInId() + 1, id.length());
    }

    /**
     * Read-only property. Introduced during ticket:73 in order to handle
     * databases with relation name length restrictions.
     */
    public String countName(Property p) {
        String countName = String.format(
                "count_%s_%s_by_owner",
                getShortname(),
                p.getName());
        return reduce(replace(countName));
    }

    /**
     * Read-only property. Introduced during ticket:73 in order to handle
     * databases with relation name length restrictions.
     */
    public String indexName(Property p) {
        String indexName = String.format(
                "i_%s_%s",
                getShortname(),
                p.getName());
        return reduce(replace(indexName));
    }

    public String columnName(Property p) {
        return columnName(p, VM_QUOTE);
    }

    public String columnName(Property p, String quote) {
        String columnName = p.getName();
        if (RESTRICTED_COLUMNS.contains(columnName)) {
            columnName = quote(columnName, quote);
        }
        return columnName;
    }

    public String tableName() {
        SemanticType base = this;
        while (base.superclass != null && base.getDiscriminator() != null) {
            base = base.superclass;
        }
        String tableName = base.getTable();
        if (isRestrictive() && RESTRICTED_TABLE.contains(tableName)) {
            tableName = tableName + "_";
        }
        return reduce(tableName);
    }

    public String inverse(Property p) {
        String inverse = p.getInverse();
        if (RESTRICTED_COLUMNS.contains(inverse)) {
            inverse = quote(inverse);
        }
        return inverse;
    }

    public String typeAnnotation(Property p) {
        String typeAnnotation = p.getTypeAnnotation();
        typeAnnotation = typeAnnotation.replace("@PROFILE@", profile);
        if (isRestrictive()) {
            if (typeAnnotation.contains("TextType")) {
                typeAnnotation = "@org.hibernate.annotations.ColumnTransformer(read=\"to_char("+
                columnName(p) + ")\")";
            } else if ("java.lang.String".equals(p.getType()) && ! p.getNullable().booleanValue()) {
                // See ticket:3884
                typeAnnotation = "@org.hibernate.annotations.ColumnTransformer(write=\"coalesce(?, ' ')\")";
            }
        }

        return typeAnnotation;
    }

    public String propName(Property p) {
        String name = p.getName();
        if (RESTRICTED_COLUMNS.contains(name)) {
            name = quote(name);
        }
        return name;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public String getSequenceName() {
        // Since in the restrictive databases that we have at the moment
        // sequences aren't objects but rows in a table, we can keep this
        // as it is.
        return getTable();
    }

    public String fk(String fkvalue) {
        return reduce(replace(fkvalue));
    }

    private String replace(String name) {

        if (isRestrictive()) {
            name = annPattern.matcher(name).replaceAll("ann");
            name = cntPattern.matcher(name).replaceAll("FK_cnt_");
            name = grpPattern.matcher(name).replaceAll("group");
            name = acqPattern.matcher(name).replaceAll("scr_acq");
        }
        return name;
    }

    private String reduce(String name) {

        if (isRestrictive()) {
            if (name.length() > 30) {
                String keep = name.substring(0, 27);
                String reduce = name.substring(28);
                name = keep + reduce.length();
            }
        }
        return name;
    }

    private String quote(String name) {
        return quote(name, VM_QUOTE);
    }

    private String quote(String name, String quote) {
        name = quote + name + quote;
        return name;
    }

    /*
     * Here the finalized ST instance is passed back into this object by the
     * post-processing step for calculating property closures and similar.
     */
    public void setActualSuperClass(SemanticType st) {
        this.superclass = st;
    }

    public SemanticType getActualSuperClass() {
        return this.superclass;
    }

    public void setSuperclass(String superclass) {
        if (superclass != null) {
            Properties p = new Properties();
            p.setProperty("id", superclass);
            this.superclass = new SemanticType(profile, p, databaseTypes) {
            };
        }
    }

    public String getSuperclass() {
        return superclass == null ? null : superclass.getId();
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setNamed(Boolean named) {
        this.named = named;
    }

    public Boolean getNamed() {
        return named;
    }

    public void setDescribed(Boolean described) {
        this.described = described;
    }

    public Boolean getDescribed() {
        return described;
    }

    public void setImmutable(Boolean immutable) {
        this.immutable = immutable;
    }

    public Boolean getImmutable() {
        return immutable;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Property getProperty(String name) {
        for (Property p : getProperties()) {
            String fn = p.getName();
            if (fn.equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Read-only method which currently filters out {@link EntryField}
     */
    public List<Property> getClassProperties() {
        List<Property> rv = new ArrayList<Property>();
        for (Property property : getProperties()) {
            if (!(property instanceof EntryField)) {
                rv.add(property);
            }
        }
        return rv;
    }

    /**
     * Read-only method which returns a set of this class's
     * {@link #getClassProperties()} as well as those of the entire inheritance
     * hierarchy.
     */
    public List<Property> getPropertyClosure() {
        List<Property> rv = getClassProperties();
        if (superclass != null) {
            rv.addAll(superclass.getPropertyClosure());
        }
        return rv;
    }

    public List<Property> getRequiredSingleProperties() {
        List<Property> rv = new ArrayList<Property>();
        for (Property property : getClassProperties()) {
            boolean req = property.getRequired() == null ? false : property
                    .getRequired().booleanValue();
            boolean col = property.getOne2Many() == null ? false : property
                    .getOne2Many().booleanValue();
            if (req && !col) {
                rv.add(property);
            }
        }
        return rv;
    }

    public String getCheck() {
        return "";
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public Boolean getGlobal() {
        return global;
    }

    /**
     * Read-only field
     */
    public String getDetails() {
        if (!getGlobal().booleanValue()) {
            if (!getImmutable().booleanValue()) {
                return "MutableDetails";
            }
            return "Details";
        }
        return "GlobalDetails";
    }

    /**
     * Read-only property to be overwritten by subclasses
     */
    public boolean getIsLink() {
        return false;
    }

    public boolean getIsAnnotationLink() {
        return false;
    }

    /**
     * Read-only property to be overwritten by subclasses
     */
    public boolean getIsEnum() {
        return false;
    }

    public Set<String> getUniqueConstraints() {
        return uniqueConstraints;
    }

    /**
     * Helper method
     */
    public static String unqualify(String klass) {
        if (klass == null) {
            return null;
        }
        int idx = klass.lastIndexOf(".");
        if (idx < 0) {
            return klass;
        } else {
            return klass.substring(idx + 1, klass.length());
        }
    }
}

//
//
// Subclasses which implement specific logic. (Essentially aliases for multiple
// properties)
//
//

class BaseType extends SemanticType {
    public BaseType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
    }
}

class AbstractType extends SemanticType {
    public AbstractType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
        this.setAbstract(Boolean.TRUE);
    }
}

class ContainerType extends SemanticType {
    public ContainerType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
        // TODO
    }
}

class LinkType extends SemanticType {
    public LinkType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
    }

    @Override
    public boolean getIsLink() {
        return true;
    }

    @Override
    public boolean getIsAnnotationLink() {
        Property p = getProperty("child");
        String t = p.getType();
        if (t.equals("ode.model.annotations.Annotation")) {
            return true;
        }
        return false;
    }
}

class ResultType extends SemanticType {
    public ResultType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
        // TODO
    }
}

class EnumType extends SemanticType {

    public EnumType(String profile, Properties attrs, Properties databaseTypes) {
        super(profile, attrs, databaseTypes);
        setGlobal(Boolean.TRUE);
        setImmutable(Boolean.TRUE);
        Properties props = new Properties();
        props.setProperty("name", "value");
        props.setProperty("type", "string");
        props.setProperty("unique", "true");
        RequiredField value = new RequiredField(this, props, databaseTypes);
        getProperties().add(value);
    }

    @Override
    public boolean getIsEnum() {
        return true;
    }
    // TODO: only value? at least value?
    // public void validate(){
    // for (Iterator it = getProperties().iterator(); it.hasNext();) {
    // if (it.next().getClass()!=EntryField.class){
    // throw new IllegalStateException("EnumTypes can only contain
    // EntryProperties.");
    // }
    // }
    // super.validate();
    // }
}