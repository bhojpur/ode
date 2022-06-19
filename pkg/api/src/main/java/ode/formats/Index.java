package ode.formats;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Index implements StringEnumeration
{
    // Index values
    BOOLEAN_ANNOTATION_INDEX("booleanAnnotationIndex"),
    CHANNEL_INDEX("channelIndex"),
    DATASET_INDEX("datasetIndex"),
    DETECTOR_INDEX("detectorIndex"),
    DICHROIC_INDEX("dichroicIndex"),
    DOUBLE_ANNOTATION_INDEX("doubleAnnotationIndex"),
    EXPERIMENT_INDEX("experimentIndex"),
    EXPERIMENTER_INDEX("experimenterIndex"),
    FILE_ANNOTATION_INDEX("fileAnnotationIndex"),
    FILTER_INDEX("filterIndex"),
    FILTER_SET_INDEX("filterSetIndex"),
    FOLDER_INDEX("folderIndex"),
    GROUP_INDEX("groupIndex"),
    IMAGE_INDEX("imageIndex"),
    INSTRUMENT_INDEX("instrumentIndex"),
    LIGHT_SOURCE_INDEX("lightSourceIndex"),
    LIGHT_SOURCE_SETTINGS_INDEX("lightSourceSettingsIndex"),
    LIST_ANNOTATION_INDEX("listAnnotationIndex"),
    LONG_ANNOTATION_INDEX("longAnnotationIndex"),
    MAP_ANNOTATION_INDEX("mapAnnotationIndex"),
    MICROBEAM_MANIPULATION_INDEX("microbeamManipulationIndex"),
    OBJECTIVE_INDEX("objectiveIndex"),
    ORIGINAL_FILE_INDEX("originalFileIndex"),
    PLANE_INDEX("planeIndex"),
    PLATE_ACQUISITION_INDEX("plateAcquisitionIndex"),
    PLATE_INDEX("plateIndex"),
    PROJECT_INDEX("projectIndex"),
    REAGENT_INDEX("reagentIndex"),
    ROI_INDEX("roiIndex"),
    SCREEN_INDEX("screenIndex"),
    SHAPE_INDEX("shapeIndex"),
    TAG_ANNOTATION_INDEX("tagAnnotationIndex"),
    TERM_ANNOTATION_INDEX("termAnnotationIndex"),
    COMMENT_ANNOTATION_INDEX("commentAnnotationIndex"),
    TIMESTAMP_ANNOTATION_INDEX("timestampAnnotationIndex"),
    WELL_INDEX("wellIndex"),
    WELL_SAMPLE_INDEX("wellSampleIndex"),
    XML_ANNOTATION_INDEX("xmlAnnotationIndex");

    /** The string-wise "value" of the Index. */
    private String value;

    /** Reverse lookup map. */
    private static final Map<String, Index> lookup =
        new HashMap<String, Index>();

    /** Initialize the reverse lookup map. */
    static
    {
        for (Index v : EnumSet.allOf(Index.class))
        {
            lookup.put(v.getValue(), v);
        }
    }

    /**
     * Default constructor.
     * @param value The index value. (For example: "imageIndex" for IMAGE_INDEX).
     */
    private Index(String value)
    {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see ode.formats.StringEnumeration#getValue()
     */
    public String getValue()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return value;
    }

    /**
     * Retrieves an Index by reverse lookup of its stringwise "index".
     * @param value The stringwise "index" to look up.
     * @return The <code>Index</code> instance for the <code>value</code>.
     * @throws IllegalArgumentException If <code>value</code> cannot be
     * found in the reverse lookup table.
     */
    static Index get(String value)
    {
        Index toReturn = lookup.get(value);
        if (toReturn == null)
        {
            throw new IllegalArgumentException(
                    "Unable to find Index with value: " + value);
        }
        return toReturn;
    }
}