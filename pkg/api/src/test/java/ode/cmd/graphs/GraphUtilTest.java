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

import java.util.Map.Entry;

import ode.services.graphs.GraphTraversal;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Tests the static utility methods for model graph operations.
 */
@Deprecated
@Test
public class GraphUtilTest {

    /**
     * Test that {@link GraphUtil#trimPackageNames(SetMultimap)} correctly trims package names from map keys.
     */
    public void testTrimPackageNames() {
        final ImmutableSetMultimap.Builder<String, Integer> builderArgument = ImmutableSetMultimap.builder();
        final ImmutableSetMultimap.Builder<String, Integer> builderExpected = ImmutableSetMultimap.builder();
        for (final Class<?> realClass : ImmutableList.of(GraphTraversal.class, GraphUtil.class, String.class, Integer.class)) {
            builderArgument.put(realClass.getName(), realClass.getSimpleName().length());
            builderExpected.put(realClass.getSimpleName(), realClass.getSimpleName().length());
        }
        final SetMultimap<String, Integer> expected = builderExpected.build();
        final SetMultimap<String, Integer> actual = GraphUtil.trimPackageNames(builderArgument.build());
        Assert.assertEquals(actual.size(), expected.size(),
                "after name trimming there should still be the same number of entries");
        for (final Entry<String, Integer> actualEntry : actual.entries()) {
            Assert.assertTrue(expected.containsEntry(actualEntry.getKey(), actualEntry.getValue()),
                    "every returned entry should be expected");
        }
    }

    /**
     * Generate test data for {@link #testGetFirstClassName(String, String)}.
     * @return pairs of type paths and the first class name from each path
     * @deprecated no longer used
     */
    @Deprecated
    @DataProvider(name = "type paths")
    public String[][] getTypePaths() {
        return new String[][] {
                new String[] {"/Image", "Image"},
                new String[] {"/Dataset/Image", "Dataset"},
                new String[] {"/Plate/PlateAcquisition/Well", "Plate"}
        };
    }
}