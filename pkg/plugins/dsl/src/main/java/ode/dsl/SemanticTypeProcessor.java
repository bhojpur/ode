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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

public class SemanticTypeProcessor implements Callable<Collection<SemanticType>> {

    // For handling
    private final Map<String, SemanticType> types;

    private final String profile;

    private final Properties dbTypes;

    public SemanticTypeProcessor(String profile, Map<String, SemanticType> types, Properties dbTypes) {
        this.profile = profile;
        this.types = types;
        this.dbTypes = dbTypes;
    }

    @Override
    public List<SemanticType> call() {
        /*
         * Handles the various link ups for annotations. (Possibly temporary)
         * This creates new types and therefore should come first.
         */
        Set<SemanticType> additions = new HashSet<>();
        for (String id : types.keySet()) {
            SemanticType t = types.get(id);
            if (t.getAnnotated() != null && t.getAnnotated()) {

                String newId = "ode.model.annotations." + t.getShortname()
                        + "AnnotationLink";
                SemanticType ann = types
                        .get("ode.model.annotations.Annotation");

                // Create link
                Properties linkP = new Properties();
                linkP.setProperty("id", newId);
                LinkType l = new LinkType(profile, linkP, dbTypes);

                Properties parentP = new Properties();
                parentP.setProperty("type", t.getId());
                LinkParent lp = new LinkParent(l, parentP, dbTypes);

                lp.validate();
                l.getProperties().add(lp);

                Properties childP = new Properties();
                childP.setProperty("type", ann.getId());
                LinkChild lc = new LinkChild(l, childP, dbTypes);

                lc.validate();
                l.getProperties().add(lc);

                l.validate();
                additions.add(l);

                // And now create the links to the link
                Properties clP = new Properties();
                clP.setProperty("name", "annotationLinks");
                clP.setProperty("type", newId);
                clP.setProperty("target", ann.getId());
                ChildLink cl = new ChildLink(t, clP, dbTypes);
                cl.setBidirectional(false);

                cl.validate();
                t.getProperties().add(cl);
            }
        }
        for (SemanticType semanticType : additions) {
            types.put(semanticType.getId(), semanticType);
        }

        /**
         * Now handling the named and described attributes in the
         * code-generation to free up the templates from the responsibility
         */
        for (SemanticType namedOrDescribed : types.values()) {
            Boolean named = namedOrDescribed.getNamed();
            Boolean descrd = namedOrDescribed.getDescribed();
            if (named != null && named) {
                Properties p = new Properties();
                p.setProperty("name", "name");
                p.setProperty("type", "text");
                RequiredField r = new RequiredField(namedOrDescribed, p, dbTypes);
                namedOrDescribed.getProperties().add(r);
            }
            if (descrd != null && descrd) {
                Properties p = new Properties();
                p.setProperty("name", "description");
                p.setProperty("type", "text");
                OptionalField o = new OptionalField(namedOrDescribed, p, dbTypes);
                namedOrDescribed.getProperties().add(o);
            }
        }

        /*
         * Example: Pixels: <zeromany name="thumbnails"
         * type="ode.model.display.Thumbnail" inverse="pixels"/> Thumnail:
         * <required name="pixels" type="ode.model.core.Pixels"/>
         *
         * We want Thumbnail.pixels to be given the inverse "thumbnails"
         *
         * This only holds so long as there is only one link from a given type
         * to another given type, which does *not* hold true for
         * AnnotationAnnotationLinks. Therefore here we have a WORKAROUND.
         */
        for (String id : types.keySet()) { // "ode...Pixels"
            SemanticType t = types.get(id); // Pixels
            for (Property p : t.getProperties()) { // thumbnails
                if (!handleLink(p)) { // WORKAROUND
                    String rev = p.getType(); // "ode...Thumbnail"
                    String inv = p.getInverse(); // "pixels"
                    if (inv != null) {
                        if (types.containsKey(rev)) {
                            SemanticType reverse = types.get(rev); // Thumbnail
                            for (Property inverse : reverse.getProperties()) {
                                if (inverse.getType().equals(id)) { // "ode...Pixels"
                                    inverse.setInverse(p.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
         * Another post-processing step, which checks links for bidirectionality
         */
        for (String id : types.keySet()) {
            SemanticType t = types.get(id);
            for (Property p : t.getProperties()) { // thumbnails
                if (p instanceof AbstractLink) {
                    AbstractLink link = (AbstractLink) p;
                    String targetId = link.getTarget();
                    SemanticType target = types.get(targetId);
                    if (target == null) {
                        throw new RuntimeException("No type " + targetId
                                + " found as target of " + link);
                    }
                    boolean found = false;
                    for (Property p2 : target.getProperties()) {
                        if (id.equals(p2.getTarget())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        link.setBidirectional(Boolean.FALSE);
                    }
                }
            }
        }

        /*
         * Check for all ordered relationships and apply unique constraints
         */
        for (String id : types.keySet()) {
            SemanticType t = types.get(id);
            for (Property property : t.getClassProperties()) {
                if (property instanceof ManyZeroField) {
                    Boolean ord = property.getOrdered();
                    if (ord != null && ord) {
                        String name = property.getName();
                        t.getUniqueConstraints().add(
                                String
                                        .format("\"%s\",\"%s_index\"", name,
                                                name));
                    }
                }
            }
        }

        /*
         * Similarly apply UNIQUE (parent, child) to all links
         */
        for (String id : types.keySet()) {
            SemanticType t = types.get(id);
            if (t instanceof LinkType) {
                LinkType link = (LinkType) t;
                if (link.getGlobal()) {
                    link.getUniqueConstraints().add("\"parent\",\"child\"");
                } else {
                    link.getUniqueConstraints().add("\"parent\",\"child\",\"owner_id\"");
                }
            }
        }

        /*
         * Each property is given its an actual
         * {@link SemanticType implementation which it belongs to and points to
         */
        for (SemanticType semanticType : types.values()) {
            for (Property property : semanticType.getPropertyClosure()) {

                SemanticType target = types.get(property.getType());
                property.setActualTarget(target);

                SemanticType currentType = semanticType;
                SemanticType actualType = semanticType;

                while (currentType != null) {
                    List<Property> classProperties = currentType.getClassProperties();
                    if (classProperties.contains(property)) {
                        actualType = currentType;
                        break;
                    }
                    String superclass = currentType.getSuperclass();
                    currentType = superclass == null ? null : types.get(currentType.getSuperclass());
                }

                property.setActualType(actualType);
            }
        }

        /*
         * Final post-processing step. Each semantic type should be given it's
         * finalized superclass instance as well as its Details property.
         */
        for (String id : types.keySet()) {
            SemanticType t = types.get(id);
            String superclass = t.getSuperclass();
            if (superclass != null) {
                SemanticType s = types.get(superclass);
                t.setActualSuperClass(s);
            } else {
                t.getProperties().add(new DetailsField(t, new Properties(), dbTypes));
            }
        }

        return new ArrayList<>(types.values());
    }

    private boolean handleLink(Property p) {
        if (!p.getIsLink()) {
            return false;
        }
        String name = p.getName();
        if (!name.equals("child") && name.equals("parent")) {
            return false;
        }

        // For links, the inverse was already set by the constructor
        return true;
    }

}