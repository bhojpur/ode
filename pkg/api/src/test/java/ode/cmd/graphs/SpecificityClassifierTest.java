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

import java.util.Arrays;

import ode.cmd.graphs.SpecificityClassifier;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Test the specificity classifier by classifying creatures by their favorite room.
 */
@Test
public class SpecificityClassifierTest {

    private enum Creature { MAMMAL, CAT, DOG, DACHSHUND, FISH, COD, HAKE, BIRD, FINCH, GREENFINCH, MAGPIE, LEVIATHAN };
    private enum Room { KITCHEN, BEDROOM, DRAWING_ROOM, BATHROOM };

    private SpecificityClassifier<Creature, Room> classifier;

    private final SetMultimap<Creature, Creature> subclasses = HashMultimap.create();

    /**
     * Populate the taxonomy, providing the transitive closure manually.
     */
    @BeforeClass
    public void populateSubclasses() {
        subclasses.putAll(Creature.MAMMAL, Arrays.asList(Creature.CAT, Creature.DOG, Creature.DACHSHUND));
        subclasses.putAll(Creature.DOG, Arrays.asList(Creature.DACHSHUND));
        subclasses.putAll(Creature.FISH, Arrays.asList(Creature.COD, Creature.HAKE));
        subclasses.putAll(Creature.BIRD, Arrays.asList(Creature.FINCH, Creature.GREENFINCH, Creature.MAGPIE));
        subclasses.putAll(Creature.FINCH, Arrays.asList(Creature.GREENFINCH));
    }

    /**
     * Create an empty classifier for each new test.
     */
    @BeforeMethod
    public void refreshClassifier() {
        classifier = new SpecificityClassifier<Creature, Room>(new SpecificityClassifier.ContainmentTester<Creature>() {
            @Override
            public boolean isProperSupersetOf(Creature parent, Creature child) {
                return subclasses.containsEntry(parent, child);
            }});
    }

    /**
     * Test that an unknown group is not classified.
     */
    @Test
    public void testUnknown() {
        Assert.assertNull(classifier.getClass(Creature.LEVIATHAN));
    }

    /**
     * Test that classification succeeds when exactly the known group is queried.
     */
    @Test
    public void testExactClass() {
        classifier.addClass(Room.DRAWING_ROOM, Arrays.asList(Creature.FINCH));
        Assert.assertEquals(classifier.getClass(Creature.FINCH), Room.DRAWING_ROOM);
    }

    /**
     * Test that classification succeeds when a subclass of the known group is queried.
     */
    @Test
    public void testSuperclass() {
        classifier.addClass(Room.DRAWING_ROOM, Arrays.asList(Creature.BIRD));
        Assert.assertEquals(classifier.getClass(Creature.FINCH), Room.DRAWING_ROOM);
    }

    /**
     * Test that the most specific classification is used when exactly the more specific group is queried.
     */
    @Test
    public void testOverriddenDirectSuperclass() {
        classifier.addClass(Room.BATHROOM, Arrays.asList(Creature.BIRD));
        classifier.addClass(Room.BEDROOM, Arrays.asList(Creature.FINCH));
        Assert.assertEquals(classifier.getClass(Creature.BIRD), Room.BATHROOM);
        Assert.assertEquals(classifier.getClass(Creature.FINCH), Room.BEDROOM);
        Assert.assertEquals(classifier.getClass(Creature.GREENFINCH), Room.BEDROOM);
    }

    /**
     * Test that the most specific classification is used when a group of intermediate specificity is queried.
     */
    @Test
    public void testOverriddenIndirectSuperclass() {
        classifier.addClass(Room.BATHROOM, Arrays.asList(Creature.BIRD));
        classifier.addClass(Room.BEDROOM, Arrays.asList(Creature.GREENFINCH));
        Assert.assertEquals(classifier.getClass(Creature.BIRD), Room.BATHROOM);
        Assert.assertEquals(classifier.getClass(Creature.FINCH), Room.BATHROOM);
        Assert.assertEquals(classifier.getClass(Creature.GREENFINCH), Room.BEDROOM);
    }

    /**
     * Test that the same classification may be asserted repeatedly for a group.
     */
    @Test
    public void testSameConsistent() {
        classifier.addClass(Room.KITCHEN, Arrays.asList(Creature.COD));
        classifier.addClass(Room.KITCHEN, Arrays.asList(Creature.COD));
        Assert.assertEquals(classifier.getClass(Creature.COD), Room.KITCHEN);
    }

    /**
     * Test that different classifications may not be asserted for a group.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSameInconsistent() {
        classifier.addClass(Room.KITCHEN, Arrays.asList(Creature.COD));
        classifier.addClass(Room.BATHROOM, Arrays.asList(Creature.COD));
    }

    /**
     * Test a more complex classification scenario.
     */
    @Test
    public void testComplex() {
        classifier.addClass(Room.KITCHEN, Arrays.asList(Creature.MAMMAL, Creature.DACHSHUND, Creature.HAKE));
        classifier.addClass(Room.BEDROOM, Arrays.asList(Creature.FISH, Creature.DOG));
        classifier.addClass(Room.DRAWING_ROOM, Arrays.asList(Creature.FINCH, Creature.LEVIATHAN));
        classifier.addClass(Room.BATHROOM, Arrays.asList(Creature.BIRD));
        Assert.assertEquals(classifier.getClass(Creature.MAMMAL), Room.KITCHEN);
        Assert.assertEquals(classifier.getClass(Creature.CAT), Room.KITCHEN);
        Assert.assertEquals(classifier.getClass(Creature.DOG), Room.BEDROOM);
        Assert.assertEquals(classifier.getClass(Creature.DACHSHUND), Room.KITCHEN);
        Assert.assertEquals(classifier.getClass(Creature.FISH), Room.BEDROOM);
        Assert.assertEquals(classifier.getClass(Creature.COD), Room.BEDROOM);
        Assert.assertEquals(classifier.getClass(Creature.HAKE), Room.KITCHEN);
        Assert.assertEquals(classifier.getClass(Creature.BIRD), Room.BATHROOM);
        Assert.assertEquals(classifier.getClass(Creature.FINCH), Room.DRAWING_ROOM);
        Assert.assertEquals(classifier.getClass(Creature.GREENFINCH), Room.DRAWING_ROOM);
        Assert.assertEquals(classifier.getClass(Creature.MAGPIE), Room.BATHROOM);
        Assert.assertEquals(classifier.getClass(Creature.LEVIATHAN), Room.DRAWING_ROOM);
    }
}