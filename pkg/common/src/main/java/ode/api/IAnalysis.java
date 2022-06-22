package ode.api;

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

import java.util.Set;

import ode.model.IObject;
import ode.model.containers.Dataset;
import ode.model.containers.Project;
import ode.model.core.Image;

/**
 * Provides access to the model objects involved in analysis.
 */
public interface IAnalysis extends ServiceInterface {

    // TODO plural arguments for each
    public Set<Project> getProjectsForUser(long experimenterId); // TODO or
                                                                    // map?

    public Set<Dataset> getDatasetsForProject(long projectId);

    public Set<Dataset> getAllDatasets();

    public Set<Project> getProjectsForDataset(long datasetId);

    public Set<Image> getImagesForDataset(long datasetId);

    public Set<IObject> getAllForImage(long imageId);
    // TODO public Set getChainExecutionsForDataset(long datasetId);

    // 1) all of the projects for a user,
    // 2) for each prioject, a list of all datasets.
    // 3) all datasets,
    // 4) for each dataset, a list of projects
    // 5) for each datasets , a list of images.
    // 6) for an image, all of the datasets that contain it, thumbnails,
    // and metadata.
    // 7) for each datasets, a list of chain executions.
    //	
    // analysis_chains
    //
    // with id, name, description, locked
    // analysis_chain_nodes
    // node_id, chain_id, module_id
    // modules
    // module_id, name, category, description
    // analysis_chain_links
    // link id, chain_id, from_node, to_node, from_output,to_input
    // formal inputs
    // formal input id, semantic type id,name, module id, description
    // formal_outptuts
    // formal output id, st id, name, module id, description
    // analysis_chain_executions
    // analysis chain exeuction id, analysis_chain_execution_id, dataset id,
    // timestamp, experimenter id.
    // module_executions.
    // module execution id,image id, dataset id, timestamp, virtual mex
    // sts
    // semantic_type_id, name, description.
    //     

}