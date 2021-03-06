package ode.services.fulltext.bridges;

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
import java.util.List;

import ode.model.containers.Dataset;
import ode.model.containers.DatasetImageLink;
import ode.model.containers.Project;
import ode.model.containers.ProjectDatasetLink;
import ode.model.core.Image;
import ode.services.fulltext.BridgeHelper;
import ode.services.fulltext.SimpleLuceneOptions;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * Example custom {@link FieldBridge} implementation which parses all
 * {@link Image} names from a {@link Project} and inserts them into the index
 * for that {@link Project}.
 */
@Deprecated
public class ProjectWithImageNameBridge extends BridgeHelper {

    /**
     * If the "value" argument is a {@link Project}, this
     * {@link FieldBridge bridge} gathers all images and adds them to the index
     * with a slightly reduced boost value. The field name of the image name is
     * "image_name" but the values are also added to the
     * {@link BridgeHelper#COMBINED} field via the
     * {@link #add(Document, String, String, LuceneOptions)}
     * method.
     */
    @Override
    public void set(final String name, final Object value,
            final Document document, final LuceneOptions _opts) {

        if (value instanceof Project) {

            logger().info("Indexing all image names for " + value);

            // Copying lucene options with a new boost value
            final float reduced_boost = _opts.getBoost().floatValue() / 2;
            final LuceneOptions opts = new SimpleLuceneOptions(_opts, reduced_boost);

            final Project p = (Project) value;
            for (final ProjectDatasetLink pdl : p.unmodifiableDatasetLinks()) {
                final Dataset d = pdl.child();
                for (final DatasetImageLink dil : d.unmodifiableImageLinks()) {
                    final Image i = dil.child();

                    // Name is never null, but as an example it is important
                    // to always check the value for null, and either simply
                    // not call add() or to use a null token like "null".
                    if (i.getName() != null) {
                        add(document, "image_name", i.getName(), opts);
                    } else {
                        add(document, "image_name", "null", opts);
                    }
                }
            }
        } else if (value instanceof Image) {

            logger().info(
                    "Scheduling all project containers of " + value
                            + " for re-indexing");

            final Image i = (Image) value;
            final List<Project> list = new ArrayList<Project>();

            for (final DatasetImageLink dil : i.unmodifiableDatasetLinks()) {
                final Dataset d = dil.parent();
                for (final ProjectDatasetLink pdl : d
                        .unmodifiableProjectLinks()) {
                    list.add(pdl.parent());
                }
            }
            if (list.size() > 0) {
                // Disabling for the moment.
                // reindexAll(list);
            }
        }
    }
}
