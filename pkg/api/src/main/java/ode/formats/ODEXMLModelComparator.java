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

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Locale;

import com.esotericsoftware.kryo.serializers.FieldSerializer;

import ode.util.LSID;
import ode.model.DetectorSettings;
import ode.model.IObject;
import ode.model.LightPath;
import ode.model.ObjectiveSettings;
import ode.model.Pixels;

/**
 * This comparator takes into account the ODE-XML data model hierarchy
 * and uses that to define equivalence.
 */
public class ODEXMLModelComparator implements Comparator<LSID>
{
    /**
     * The collator that we use to alphabetically sort by class name
     * within a given level of the ODE-XML hierarchy.
     */
    @FieldSerializer.Optional("ODEXmlModelComparator.stringComparator")
    private RuleBasedCollator stringComparator =
        (RuleBasedCollator) Collator.getInstance(Locale.ENGLISH);

    public int compare(LSID x, LSID y)
    {
        // Handle identical LSIDs
        if (x.equals(y))
        {
            return 0;
        }

        // Parse the LSID for hierarchical equivalence tests.
        Class<? extends IObject> xClass = x.getJavaClass();
        Class<? extends IObject> yClass = y.getJavaClass();
        int[] xIndexes = x.getIndexes();
        int[] yIndexes = y.getIndexes();

        // Handle the null class (one or more unparsable internal
        // references) case.
        if (xClass == null || yClass == null)
        {
            return stringComparator.compare(x.toString(), y.toString());
        }

        // Assign values to the classes
        int xVal = getValue(xClass, xIndexes.length);
        int yVal = getValue(yClass, yIndexes.length);

        int retval = xVal - yVal;
        if (retval == 0)
        {
            // Handle different classes at the same level in the hierarchy
            // by string difference. They need to still be different.
            if (!xClass.equals(yClass))
            {
                return stringComparator.compare(x.toString(), y.toString());
            }
            // Handle the same classes at the same level in the hierarchy with
            // differing numbers of indexes by string difference. They also
            // need to still be different.
            if (xIndexes.length != yIndexes.length)
            {
                return stringComparator.compare(x.toString(), y.toString());
            }
            for (int i = 0; i < xIndexes.length; i++)
            {
                int difference = xIndexes[i] - yIndexes[i];
                if (difference != 0)
                {
                    return difference;
                }
            }
            return 0;
        }
        return retval;
    }

    /**
     * Assigns a value to a particular class based on its location in the
     * ODE-XML hierarchy.
     * @param klass Class to assign a value to.
     * @param indexes Number of class indexes that were present in its LSID.
     * @return The value.
     */
    public int getValue(Class<? extends IObject> klass, int indexes)
    {
        // Top-level (Pixels is a special case due to Channel and
        // LogicalChannel containership weirdness).
        if (klass.equals(Pixels.class))
        {
            return 1;
        }

        if (klass.equals(ObjectiveSettings.class)
            || klass.equals(DetectorSettings.class)
            || klass.equals(LightPath.class))
        {
            return 3;
        }

        return indexes;
    }
}