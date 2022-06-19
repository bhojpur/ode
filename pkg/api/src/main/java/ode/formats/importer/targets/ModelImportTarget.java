package ode.formats.importer.targets;

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

import static ode.rtypes.rlong;
import static ode.rtypes.rstring;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportContainer;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.model.DatasetI;
import ode.model.IObject;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.ProjectI;
import ode.sys.ParametersI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelImportTarget implements ImportTarget {

    private final static Logger log = LoggerFactory.getLogger(ModelImportTarget.class);

    /**
     * Valid ode.model classes for model import target.
     */
    private static final List<String> VALID_TYPES = Arrays.asList(
            "ode.model.Project", "ode.model.Dataset", "ode.model.Screen");

    /**
     * ode.model class which can be used for instantiation.
     */
    private Class<? extends IObject> type;

    /**
     * Object of {@link #type} which is found or created.
     */
    private Long id;

    /**
     * Internal representation of a target deeper in the hierarchy if needed.
     * For example, if this instance represents a Project, then the subTarget
     * would be a Dataset.
     */
    private ModelImportTarget subTarget;

    /**
     * String used for querying the database; must be ode.model based.
     */
    private String typeName;

    private String simpleName;

    private String prefix;

    private String discriminator;

    private String name;

    @SuppressWarnings("unchecked")
    @Override
    public void init(String target) {
        // Builder is responsible for only passing valid files.
        String[] tokens = target.split(":",3);
        this.prefix = tokens[0];
        if (tokens.length == 2) {
            this.name = tokens[1];
            this.discriminator = "id";
        } else {
            this.name = tokens[2];
            this.discriminator = tokens[1];
        }
        type = tryClass(prefix);
        Class<?> k = ode.util.IceMap.ODEROtoODE.get(type);
        typeName = k.getName();
        simpleName = k.getSimpleName();
        // Reversing will take us from an abstract type to one constructible.
        type = ode.util.IceMap.ODEtoODERO.get(k);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends IObject> tryClass(String prefix) {
        Class<? extends IObject> klass = null;
        try {
            klass = (Class<? extends IObject>) Class.forName(prefix);
        } catch (Exception e) {
            try {
                klass = (Class<? extends IObject>) Class.forName("ode.model."+prefix);
            } catch (Exception e1) {
                throw new RuntimeException("Unknown class:" + prefix);
            }
        }
        if (!VALID_TYPES.contains(klass.getName())) {
            throw new RuntimeException("Not a valid container class:" + klass.getName());
        }
        return klass;
    }

    public Class<? extends IObject> getObjectType() {
        if (subTarget != null) {
            return subTarget.getObjectType();
        }
        return type;
    }

    public Long getObjectId() {
        if (subTarget != null) {
            return subTarget.getObjectId();
        }
        return id;
    }

    @Override
    public IObject load(ODEMetadataStoreClient client, ImportContainer ic) throws Exception {
        IQueryPrx query = client.getServiceFactory().getQueryService();
        IUpdatePrx update = client.getServiceFactory().getUpdateService();

        if ("Project".equals(simpleName)) {
            if (!name.contains("/Dataset:")) {
                throw new RuntimeException(String.format("Project name must include '/Dataset:' (%s)", name));
            }
            String[] tokens = name.split("/Dataset:", 2);
            name = tokens[0];
            String subname = "Dataset:" + tokens[1];
            log.info("Creating sub-target: {}", subname);
            subTarget = new ModelImportTarget();
            subTarget.init(subname);
        }

        if (discriminator.matches("^[-+%@]?name$")) {
            IObject obj;
            String order = "desc";
            if (discriminator.startsWith("-")) {
                order = "asc";
            }
            final List<IObject> objs;
            if (discriminator.startsWith("@")) {
                objs = Collections.emptyList();
            } else {
                objs = (List<IObject>) query.findAllByQuery(
                        "select o from " + simpleName + " as o where o.name = :name"
                                + " order by o.id " + order,
                                new ParametersI().add("name", rstring(name)));
                final Iterator<IObject> objIter = objs.iterator();
                while (objIter.hasNext()) {
                    if (!objIter.next().getDetails().getPermissions().canLink()) {
                        objIter.remove();
                    }
                }
            }
            if (objs.isEmpty()) {
                obj = type.newInstance();
                Method m = type.getMethod("setName", ode.RString.class);
                m.invoke(obj, rstring(name));
                obj = update.saveAndReturnObject(obj);
            } else {
                if (discriminator.startsWith("%") && objs.size() > 1) {
                    log.error("No unique {} called {}", simpleName, name);
                    throw new RuntimeException("No unique "+simpleName+" available");
                } else {
                    obj = objs.get(0);
                }
            }
            id = obj.getId().getValue();
        } else if (discriminator.equals("id")) {
            id = Long.valueOf(name);
        } else {
            log.error("Unknown discriminator {}", discriminator);
            throw new RuntimeException("Unknown discriminator "+discriminator);
        }

        if (subTarget != null) {
            // Note: this assumes that the only supertarget is a Project. If that is extended,
            // assumptions will need to be reviewed.
            log.info("Super-target loaded: {}:{}", simpleName, id);
            IObject sub = subTarget.load(client, ic);
            ParametersI params = new ParametersI().add("pid", rlong(id)).add("did", sub.getId());
            List<List<ode.RType>> rv = query.projection(
                    "select pdl.id from ProjectDatasetLink pdl " +
                            "where pdl.parent.id = :pid " +
                            "and pdl.child.id = :did", params);
            if (rv.size() == 0) {
                ProjectDatasetLink pdl = new ProjectDatasetLinkI();
                pdl.setParent(new ProjectI(id, false));
                pdl.setChild(new DatasetI(subTarget.getObjectId(), false));
                update.saveObject(pdl);
                log.info("Linked targets");
            }
            return sub;
        }
        return query.get(type.getSimpleName(), id);
    }
}