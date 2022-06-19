package ode.util;

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

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import com.google.common.collect.Maps;

/**
 * Efficiently applies prefix transformations from a set thereof.
 * @param <X> the type of sequences that have prefixes needing substituting
 */
public class PrefixSubstituter<X extends Comparable<X>> {

    private final BiFunction<X, X, X> addPrefix, delPrefix;
    private final SortedMap<X, Entry<X, X>> lookup;

    /**
     * Construct a new prefix substituter.
     * @param isPrefixOf test if the first argument prefixes the second
     * @param addPrefix prefix the first argument to the second
     * @param delPrefix remove the first argument from the start of the second
     */
    public PrefixSubstituter(BiPredicate<X, X> isPrefixOf, BiFunction<X, X, X> addPrefix, BiFunction<X, X, X> delPrefix) {
        this.addPrefix = addPrefix;
        this.delPrefix = delPrefix;
        this.lookup = new TreeMap<>(new Comparator<X>() {
            /* Note: This anonymous class has a natural ordering that is inconsistent with Object.equals. */
            @Override
            public int compare(X p, X q) {
                if (isPrefixOf.test(p, q) || isPrefixOf.test(q, p)) {
                    return 0;
                } else {
                    return p.compareTo(q);
                }
            }
        });
    }

    /**
     * Add a prefix substitution to the applicable set.
     * @param from the prefix to match
     * @param to the replacement prefix
     */
    public void put(X from, X to) {
        if (lookup.put(from, Maps.immutableEntry(from, to)) != null) {
            throw new IllegalArgumentException("can add only prefixes that are not a prefix of another");
        }
    }

    /**
     * Apply a prefix substitution if any applies.
     * @param item the sequence whose prefix may need substituting, not {@code null}
     * @return the given sequence, has prefix substituted if any applied
     */
    public X apply(X item) {
        final Entry<X, X> substitution = lookup.get(item);
        if (substitution == null) {
            return item;
        }
        final X prefixFrom = substitution.getKey();
        final X prefixTo = substitution.getValue();
        final X suffix = delPrefix.apply(prefixFrom, item);
        return addPrefix.apply(prefixTo, suffix);
    }
}