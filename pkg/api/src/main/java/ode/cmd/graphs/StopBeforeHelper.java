package ode.cmd.graphs;

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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

/**
 * Calculates suggestions for the {@code stopBefore} property of {@link ode.cmd.FindParents} and {@link ode.cmd.FindChildren}.
 * @param <X> the type with which model types are represented
 */
public class StopBeforeHelper<X> {

    /**
     * @return the singleton {@code stopBefore} helper
     */
    static StopBeforeHelper<String> get() {
        return STOP_BEFORE_HELPER;
    }

    /**
     * Represents a model class hierarchy with a sense of direction. The directed graph may be cyclic.
     * @param <X> the type used to represent node identities
     */
    private static class Hierarchy<X> {
        private final SetMultimap<X, X> directlyBeyond = HashMultimap.create();
        private final SetMultimap<X, X> allBeyond = HashMultimap.create();
        private boolean dirty = false;

        /**
         * Add an edge to the hierarchy.
         * @param from the <q>from</q> node
         * @param to the <q>to</q> node
         */
        private void addEdge(X from, X to) {
            if (directlyBeyond.put(from, to)) {
                dirty = true;
            }
        }

        /**
         * Find all nodes beyond the given <q>current</q> nodes. The return value includes those current nodes.
         * @param current the nodes to start from
         * @param all the nodes known to be beyond those nodes
         * @return all nodes beyond the current nodes
         */
        private Set<X> getAllBeyond(Set<X> current, Set<X> all) {
            all.addAll(current);
            final Set<X> next = new HashSet<X>();
            for (final X node : current) {
                next.addAll(directlyBeyond.get(node));
            }
            next.removeAll(all);
            return next.isEmpty() ? all : getAllBeyond(next, all);
        }

        /**
         * Compute {@link allBeyond} as the transitive closure of {@link directlyBeyond}. It is also reflexive.
         */
        private void computeAllBeyond() {
            for (final X from : directlyBeyond.keySet()) {
                for (final X to : getAllBeyond(Collections.singleton(from), new HashSet<X>())) {
                    allBeyond.put(from, to);
                }
            }
            dirty = false;
        }

        /**
         * Get the nodes beyond the given nodes.
         * @param beyonds the map of <q>beyondness</q> to use
         * @param nodes some nodes
         * @return the nodes beyond those nodes
         */
        private Set<X> beyond(SetMultimap<X, X> beyonds, Collection<X> nodes) {
            if (dirty) {
                computeAllBeyond();
            }
            final Set<X> beyond = new HashSet<X>();
            for (final X node : nodes) {
                beyond.addAll(beyonds.get(node));
            }
            return beyond;
        }

        /**
         * Get the nodes immediately beyond the given nodes.
         * @param nodes some nodes
         * @return the next nodes beyond those nodes
         */
        private Set<X> directlyBeyond(Collection<X> nodes) {
            return beyond(directlyBeyond, nodes);
        }

        /**
         * Get all the nodes <em>including</em> and beyond the given nodes, whether directly or indirectly.
         * @param nodes some nodes
         * @return all nodes beyond those nodes
         */
        private Set<X> allBeyond(Collection<X> nodes) {
            return beyond(allBeyond, nodes);
        }
    }

    private final Hierarchy<X> aboves = new Hierarchy<X>();
    private final Hierarchy<X> belows = new Hierarchy<X>();
    private final Set<X> allKnown = new HashSet<X>();

    /**
     * Add an edge to the hierarchy.
     * @param above the node <q>above</q> the other
     * @param below the node <q>below</q> the other
     */
    private void addEdge(X above, X below) {
        belows.addEdge(above, below);
        aboves.addEdge(below, above);
        allKnown.add(above);
        allKnown.add(below);
    }

    /**
     * Check that the given nodes are all known in the hierarchy.
     * @param nodes some nodes
     */
    private void assertNodesKnown(Set<X> nodes) {
        final Set<X> unknown = Sets.difference(nodes, allKnown);
        if (!unknown.isEmpty()) {
            throw new IllegalArgumentException("unknown in hierarchy: " + Joiner.on(',').join(unknown));
        }
    }

    /**
     * Get suggested values for {@link ode.cmd.FindParents#stopBefore}.
     * @param startFrom simple top-level class names for the objects from which to start searching
     * @param sought simple top-level class names for the objects that are sought
     * @return simple top-level class names for objects to ignore in the graph because the subtree beyond contains nothing sought
     */
    Set<X> getStopBeforeParents(Set<X> startFrom, Set<X> sought) {
        assertNodesKnown(startFrom);
        assertNodesKnown(sought);
        final Set<X> pathNodes = aboves.allBeyond(startFrom);
        pathNodes.retainAll(belows.allBeyond(sought));
        final Set<X> beyonds = aboves.directlyBeyond(pathNodes);
        beyonds.removeAll(pathNodes);
        return beyonds;
    }

    /**
     * Get suggested values for {@link ode.cmd.FindChildren#stopBefore}.
     * @param startFrom simple top-level class names for the objects from which to start searching
     * @param sought simple top-level class names for the objects that are sought
     * @return simple top-level class names for objects to ignore in the graph because the subtree beyond contains nothing sought
     */
    Set<X> getStopBeforeChildren(Set<X> startFrom, Set<X> sought) {
        assertNodesKnown(startFrom);
        assertNodesKnown(sought);
        final Set<X> pathNodes = belows.allBeyond(startFrom);
        pathNodes.retainAll(aboves.allBeyond(sought));
        final Set<X> beyonds = belows.directlyBeyond(pathNodes);
        beyonds.removeAll(pathNodes);
        return beyonds;
    }

    /**
     * The model graph containment hierarchy, each subarray being a parent followed by directly contained children.
     * Multiple lines for the same parent are combined. This names only top-level classes, not any of their subclasses.
     * The hierarchy reflects the policy rules encoded in <tt>server-contained-rules.xml</tt>.
     */
    private static String[][] HIERARCHY = new String[][]{
        {"Instrument", "Detector", "Dichroic", "Filter", "FilterSet", "LightSource", "Microscope", "Objective", "OTF"},
        {"Filter", "TransmittanceRange"},
        {"FilterSet", "Dichroic"},
        {"LightSource", "LightSource"},
        {"LogicalChannel", "DetectorSettings", "LightSettings"},
        {"Image", "ObjectiveSettings"},
        {"LightSettings", "LightSource"},
        {"DetectorSettings", "Detector"},
        {"ObjectiveSettings", "Objective"},
        {"Detector", "Instrument"},
        {"LightSource", "Instrument"},
        {"Objective", "Instrument"},
        {"OTF", "FilterSet", "Objective"},
        {"FilterSet", "FilterSetEmissionFilterLink", "FilterSetExcitationFilterLink"},
        {"LightPath", "LightPathEmissionFilterLink", "LightPathExcitationFilterLink"},
        {"Project", "ProjectDatasetLink"},
        {"Dataset", "DatasetImageLink"},
        {"Folder", "Folder", "FolderImageLink", "FolderRoiLink"},
        {"DatasetImageLink", "Image"},
        {"ProjectDatasetLink", "Dataset"},
        {"FolderImageLink", "Image"},
        {"FolderRoiLink", "Roi"},
        {"Image", "ImagingEnvironment", "Instrument", "ObjectiveSettings", "Pixels", "StageLabel"},
        {"Pixels", "Channel", "PixelsOriginalFileMap","PlaneInfo", "RenderingDef", "Thumbnail"},
        {"LogicalChannel", "Channel", "LightPath"},
        {"Channel", "LogicalChannel"},
        {"Annotation", "OriginalFile"},
        {"FilesetEntry", "OriginalFile"},
        {"PixelsOriginalFileMap", "OriginalFile"},
        {"Roi", "OriginalFile"},
        {"RenderingDef", "QuantumDef", "CodomainMapContext", "ChannelBinding", "ProjectionDef"},
        {"Image", "Experiment"},
        {"Experiment", "MicrobeamManipulation"},
        {"Fileset", "Image", "FilesetEntry", "FilesetJobLink"},
        {"FilesetJobLink", "Job"},
        {"Job", "JobOriginalFileLink"},
        {"JobOriginalFileLink", "OriginalFile"},
        {"Image", "Roi"},
        {"Roi", "Shape"},
        {"Shape", "Image"},
        {"Screen", "Reagent"},
        {"Well", "WellReagentLink"},
        {"WellReagentLink", "Reagent"},
        {"Screen", "ScreenPlateLink"},
        {"ScreenPlateLink", "Plate"},
        {"Plate", "Well", "PlateAcquisition"},
        {"PlateAcquisition", "WellSample"},
        {"Well", "WellSample"},
        {"WellSample", "Image"},
        {"Channel", "StatsInfo"}
    };

    /**
     * The singleton instance of the helper.
     */
    private static final StopBeforeHelper<String> STOP_BEFORE_HELPER;

    /* populate the singleton instance with the hierarchy, plus annotation subgraphs */
    static {
        STOP_BEFORE_HELPER = new StopBeforeHelper<String>();
        for (final String[] parentThenChildren : HIERARCHY) {
            for (int index = 1; index < parentThenChildren.length; index++) {
                STOP_BEFORE_HELPER.addEdge(parentThenChildren[0], parentThenChildren[index]);
            }
        }
        for (final String annotatable : new String[]{
                "Annotation", "Channel", "Dataset", "Detector", "Dichroic", "Fileset", "Filter", "Folder", "Image",
                "Instrument", "LightPath", "LightSource", "Objective", "OriginalFile", "PlaneInfo", "PlateAcquisition", "Plate",
                "Project", "Reagent", "Roi", "Screen", "Shape", "Well"}) {
            final String link = annotatable + "AnnotationLink";
            STOP_BEFORE_HELPER.addEdge(annotatable, link);
            STOP_BEFORE_HELPER.addEdge(link, "Annotation");
        }
    }
}