package ode.services.server.repo.path;

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
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

/**
 * Capture a set of rules by which local files may not be named on the file-system.
 */
public class FilePathRestrictions {
    private static final ImmutableSet<Integer> controlCodePoints;

    private static final Predicate<Integer> isNotControlCodePoint;

    /* the full rules */
    public final ImmutableSetMultimap<Integer, Integer> transformationMatrix;  /* values never empty */
    public final ImmutableSet<String> unsafePrefixes;
    public final ImmutableSet<String> unsafeSuffixes;
    public final ImmutableSet<String> unsafeNames;
    public final ImmutableSet<Character> safeCharacters;  /* never empty */

    /* quick lookups to characters that satisfy the above rules */
    public final char safeCharacter;
    public final ImmutableMap<Integer, Integer> transformationMap;

    static {
        final ImmutableSet.Builder<Integer> controlCodePointsBuilder = ImmutableSet.builder();
        for (int codePoint = 0; codePoint < 0x100; codePoint++) {
            if (Character.getType(codePoint) == Character.CONTROL) {
                controlCodePointsBuilder.add(codePoint);
            }
        }
        controlCodePoints = controlCodePointsBuilder.build();
        isNotControlCodePoint = new Predicate<Integer>() {
            public boolean apply(Integer codePoint) {
                return !controlCodePoints.contains(codePoint);
            }};
    }

    /**
     * Minimally adjust a set of rules to include transformations away from Unicode control characters.
     * @param rules a set of rules
     * @return the given rules with full coverage for preventing control characters
     */
    private static FilePathRestrictions includeControlTransformations(FilePathRestrictions rules) {
        final Set<Character> safeCharacters = new HashSet<Character>(rules.safeCharacters.size());
        final Set<Integer> safeCodePoints = new HashSet<Integer>(rules.safeCharacters.size());
        for (final Character safeCharacter : rules.safeCharacters) {
            final int safeCodePoint = FilePathRestrictionInstance.getCodePoint(safeCharacter);
            if (!controlCodePoints.contains(safeCodePoint)) {
                safeCharacters.add(safeCharacter);
                safeCodePoints.add(safeCodePoint);
            }
        }
        final SetMultimap<Integer, Integer> newTransformationMatrix =
                HashMultimap.create(Multimaps.filterValues(rules.transformationMatrix, isNotControlCodePoint));
        for (final int controlCodePoint : controlCodePoints) {
            if (!newTransformationMatrix.containsKey(controlCodePoint)) {
                if (rules.transformationMatrix.containsKey(controlCodePoint)) {
                    throw new IllegalArgumentException(
                            "only control character mappings available for Unicode code point " + controlCodePoint);
                }
                newTransformationMatrix.putAll(controlCodePoint, safeCodePoints);
            }
        }
        return combineRules(rules, new FilePathRestrictions(newTransformationMatrix, null, null, null, safeCharacters));
    }

    /**
     * Combine sets of rules to form a set that satisfies them all.
     * @param rules at least one set of rules
     * @return the intersection of the given rules
     */
    private static FilePathRestrictions combineRules(FilePathRestrictions... rules) {
        if (rules.length == 0) {
            throw new IllegalArgumentException("cannot combine an empty list of rules");
        }

        int index = 0;
        FilePathRestrictions product = rules[index++];

        while (index < rules.length) {
            final FilePathRestrictions toCombine = rules[index++];

            final Set<Character> safeCharacters = Sets.intersection(product.safeCharacters, toCombine.safeCharacters);

            if (safeCharacters.isEmpty()) {
                throw new IllegalArgumentException("cannot combine safe characters");
            }

            final Set<Integer> allKeys = Sets.union(product.transformationMatrix.keySet(), toCombine.transformationMatrix.keySet());
            final ImmutableMap<Integer, Collection<Integer>> productMatrixMap = product.transformationMatrix.asMap();
            final ImmutableMap<Integer, Collection<Integer>> toCombineMatrixMap = toCombine.transformationMatrix.asMap();
            final SetMultimap<Integer, Integer> newTransformationMatrix = HashMultimap.create();

            for (final Integer key : allKeys) {
                final Collection<Integer> values;
                if (!productMatrixMap.containsKey(key)) {
                    values = toCombineMatrixMap.get(key);
                } else if (!toCombineMatrixMap.containsKey(key)) {
                    values = productMatrixMap.get(key);
                } else {
                    final Set<Integer> valuesSet = new HashSet<Integer>(productMatrixMap.get(key));
                    valuesSet.retainAll(toCombineMatrixMap.get(key));
                    if (valuesSet.isEmpty()) {
                        throw new IllegalArgumentException("cannot combine transformations for Unicode code point " + key);
                    }
                    values = valuesSet;
                }
                for (final Integer value : values) {
                    newTransformationMatrix.put(key, value);
                }
            }

            final SetMultimap<Integer, Integer> entriesRemoved = HashMultimap.create();
            boolean transitiveClosing;
            do {
                transitiveClosing = false;
                for (final Entry<Integer, Integer> transformation : newTransformationMatrix.entries()) {
                    final int to = transformation.getValue();
                    if (newTransformationMatrix.containsKey(to)) {
                        final int from = transformation.getKey();
                        if (!entriesRemoved.put(from, to)) {
                            throw new IllegalArgumentException("cyclic transformation involving Unicode code point " + from);
                        }
                        newTransformationMatrix.remove(from, to);
                        newTransformationMatrix.putAll(from, newTransformationMatrix.get(to));
                        transitiveClosing = true;
                        break;
                    }
                }
            } while (transitiveClosing);

            product = new FilePathRestrictions(newTransformationMatrix,
                    Sets.union(product.unsafePrefixes, toCombine.unsafePrefixes),
                    Sets.union(product.unsafeSuffixes, toCombine.unsafeSuffixes),
                    Sets.union(product.unsafeNames, toCombine.unsafeNames),
                    safeCharacters);
        }
        return product;
    }

    /**
     * Combine sets of rules to form a set that satisfies them all and that
     * include transformations away from Unicode control characters.
     * @param rules at least one set of rules
     * @return the intersection of the given rules, with full coverage for preventing control characters
     */
    public static FilePathRestrictions combineFilePathRestrictions(FilePathRestrictions... rules) {
        return includeControlTransformations(combineRules(rules));
    }

    /**
     * Construct a set of rules by which local files may not be named on the file-system.
     * @param transformationMatrix how to make specific characters safe, may be null
     * @param unsafePrefixes which name prefixes are proscribed, may be null
     * @param unsafeSuffixes which name suffixes are proscribed, may be null
     * @param unsafeNames which names are proscribed, may be null
     * @param safeCharacters safe characters that may be used in making file names safe, may <em>not</em> be null
     */
    public FilePathRestrictions(SetMultimap<Integer, Integer> transformationMatrix,
            Set<String> unsafePrefixes, Set<String> unsafeSuffixes, Set<String> unsafeNames,
            Set<Character> safeCharacters) {
        this.transformationMatrix = transformationMatrix == null ? ImmutableSetMultimap.<Integer, Integer>of() 
                                                                 : ImmutableSetMultimap.copyOf(transformationMatrix);
        this.unsafePrefixes = unsafePrefixes == null ? ImmutableSet.<String>of() : ImmutableSet.copyOf(unsafePrefixes);
        this.unsafeSuffixes = unsafeSuffixes == null ? ImmutableSet.<String>of() : ImmutableSet.copyOf(unsafeSuffixes);
        this.unsafeNames    = unsafeNames    == null ? ImmutableSet.<String>of() : ImmutableSet.copyOf(unsafeNames);
        this.safeCharacters = ImmutableSet.copyOf(safeCharacters);

        this.safeCharacter = this.safeCharacters.iterator().next();
        int safeCodePoint = FilePathRestrictionInstance.getCodePoint(this.safeCharacter);
        final ImmutableMap.Builder<Integer, Integer> transformationMapBuilder = ImmutableMap.builder();
        for (final Entry<Integer, Collection<Integer>> transformation : this.transformationMatrix.asMap().entrySet()) {
            final Collection<Integer> values = transformation.getValue();
            final Integer selectedValue = values.contains(safeCodePoint) ? safeCodePoint : values.iterator().next();
            transformationMapBuilder.put(transformation.getKey(), selectedValue);
        }
        this.transformationMap = transformationMapBuilder.build();
    }
}