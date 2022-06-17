package ode.tools;

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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ode.model.IObject;
import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.core.Image;
import ode.util.CBlock;

public class HierarchyTransformations {

    /**
     * 
     * @param imagesAll
     * @param block
     * @return a Set with {@link Image}, {@link Dataset}, and {@link Project},
     *         instances.
     */
    public static <T extends IObject> Set<T> invertPDI(Set<Image> imagesAll,
            CBlock<T> block) {

        Set<T> cleared = new HashSet<T>();
        Set<T> hierarchies = new HashSet<T>();
        Iterator<Image> i = imagesAll.iterator();
        while (i.hasNext()) {
            Image img = (Image) block.call(i.next());

            // Copy needed to prevent ConcurrentModificationExceptions
            List<Dataset> d_list = img.linkedDatasetList();
            Iterator<Dataset> d = d_list.iterator();
            if (!d.hasNext()) {
                hierarchies.add((T) img);
            } else {
                while (d.hasNext()) {
                    Dataset ds = (Dataset) block.call(d.next());

                    if (!cleared.contains(ds)) {
                        // ds.clearImageLinks();
                        ds.putAt(Dataset.IMAGELINKS, new HashSet());
                        cleared.add((T) ds);
                    }
                    ds.linkImage(img);

                    // Copy needed to prevent ConcurrentModificationExceptions
                    List<Project> p_list = ds.linkedProjectList();
                    Iterator<Project> p = p_list.iterator();
                    if (!p.hasNext()) {
                        hierarchies.add((T) ds);
                    } else {
                        while (p.hasNext()) {
                            Project prj = (Project) block.call(p.next());

                            if (!cleared.contains(prj)) {
                                // prj.clearDatasetLinks();
                                prj.putAt(Project.DATASETLINKS, new HashSet());
                                cleared.add((T) prj);
                            }
                            prj.linkDataset(ds);

                            hierarchies.add((T) prj);
                        }
                    }

                }
            }
        }
        return hierarchies;
    }

}