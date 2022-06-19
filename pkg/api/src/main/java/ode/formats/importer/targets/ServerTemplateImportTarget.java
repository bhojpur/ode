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

import static ode.rtypes.rstring;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ode.formats.ODEMetadataStoreClient;
import ode.formats.importer.ImportContainer;
import ode.api.IQueryPrx;
import ode.api.IUpdatePrx;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.IObject;
import ode.model.Screen;
import ode.model.ScreenI;
import ode.sys.Parameters;
import ode.sys.ParametersI;

import org.apache.commons.lang.StringUtils;

public class ServerTemplateImportTarget extends TemplateImportTarget {

    private final String sharedPath;

    public ServerTemplateImportTarget(String sharedPath) {
        this.sharedPath = sharedPath;
    }

    @Override
    public IObject load(ODEMetadataStoreClient client, ImportContainer ic) throws Exception {
        return load(client, ic.getIsSPW());
    }

    public IObject load(ODEMetadataStoreClient client, boolean spw) throws Exception {
        log.info("Checking '{}' against '{}'", sharedPath, getTemplate());
        Pattern pattern = Pattern.compile(getTemplate());
        Matcher m = pattern.matcher(sharedPath);
        if (!m.matches()) {
            log.warn("No match");
            return null;
        }

        if (!getDiscriminator().matches("^[-+%@]?name$")) {
            log.warn("Invalid discriminator: {}", getDiscriminator());
            return null;
        }

        String name = m.group("Container1");
        if (StringUtils.isBlank(name)) {
            log.warn("Empty name");
            return null;
        }

        final List<IObject> objs;
        if (getDiscriminator().startsWith("@")) {
            objs = Collections.emptyList();
        } else {
            final IQueryPrx query = client.getServiceFactory().getQueryService();
            final StringBuilder hql = new StringBuilder();
            hql.append("FROM ");
            hql.append(spw ? "Screen" : "Dataset");
            hql.append(" WHERE name = :name ORDER BY id");
            if (!getDiscriminator().startsWith("-")) {
                hql.append(" DESC");
            }
            final Parameters params = new ParametersI().add("name", rstring(name));
            objs = query.findAllByQuery(hql.toString(), params);
            final Iterator<IObject> objIter = objs.iterator();
            while (objIter.hasNext()) {
                if (!objIter.next().getDetails().getPermissions().canLink()) {
                    objIter.remove();
                }
            }
        }
        if (objs.isEmpty()) {
            final IUpdatePrx update = client.getServiceFactory().getUpdateService();
            if (spw) {
                final Screen screen = new ScreenI();
                screen.setName(rstring(name));
                return update.saveAndReturnObject(screen);
            } else {
                final Dataset dataset = new DatasetI();
                dataset.setName(rstring(name));
                return update.saveAndReturnObject(dataset);
            }
        } else if (getDiscriminator().startsWith("%") && objs.size() > 1) {
            log.warn("No unique {} called {}", spw ? "screen" : "dataset", name);
            return null;
        } else {
            return objs.get(0);
        }
    }
}