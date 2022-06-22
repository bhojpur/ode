package ode.testing;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.conditions.ApiUsageException;

/**
 * utility class to test CGC paths. Specifying just what should and what should
 * not be included in a possibly mutually-exclusive (de)classification is quite
 * tricky, and is encapsulated here.
 */
public class Paths {

    private static Logger log = LoggerFactory.getLogger(Paths.class);

    public final static Long EXISTS = new Long(-3);

    public final static Long WILDCARD = new Long(-2);

    public final static Long NULL_IMAGE = new Long(-1);

    public final static Long CG = new Long(0);

    public final static Long C = new Long(1);

    public final static Long I = new Long(2);

    List cg = new ArrayList(), c = new ArrayList(), i = new ArrayList(),
            removed = new ArrayList();

    Long[] singlePath = null;

    /**
     * 
     * @param data
     *            List of the form {@code List<Map<String,Long>>} where the keys of
     *            the map are "cg", "c", and "i". Data of this form can be
     *            retrieved from {@link ODEData#get(String)} with the string
     *            "CGCPaths.all" TODO
     */
    public Paths(List data) {
        if (data == null) {
            throw new ApiUsageException(
                    "Data argument to Paths constructor may not be null.");
        }

        List cgciPaths = data;
        for (Iterator it = cgciPaths.iterator(); it.hasNext();) {
            Map m = (Map) it.next();
            Long cg = (Long) m.get("cg"), c = (Long) m.get("c");
            Long i = m.get("i") == null ? Paths.NULL_IMAGE : (Long) m.get("i");
            add(cg, c, i);

            if (singlePath == null && i != Paths.NULL_IMAGE) {
                singlePath = get(new Long(size() - 1));
            }

        }
        log.info(toString());
        if (singlePath == null) {
            log.warn("No path found with non-null image.");
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < cg.size(); t++) {
            if (!removed.contains(new Long(t))) {
                sb.append(t + ":\t");
                sb.append(cg.get(t));
                sb.append("/");
                sb.append(c.get(t));
                sb.append("/");
                sb.append((NULL_IMAGE.equals(i.get(t)) ? "EMPTY" : i
                        .get(t))
                        + "\n");
            } else {
                sb.append(t + ":\tREMOVED");
            }
        }
        return sb.toString();
    }

    // ~ Mutators

    public void add(Long newCg, Long newC, Long newI) {
        this.cg.add(newCg);
        this.c.add(newC);
        this.i.add(newI);
    }

    public boolean remove(Long t) {
        removed.add(t);
        return true;
    }

    public boolean remove(Long removeCg, Long removeC, Long removeI) {

        Set n = find(removeCg, removeC, removeI);
        return n.size() < 1 ? false : removed.addAll(n);
    }

    // ~Views

    public Set find(Long testCg, Long testC, Long testI) {
        Set result = new HashSet();
        for (int n = 0; n < cg.size(); n++) {
            Long N = new Long(n);
            if ((cg.get(n).equals(testCg) || testCg == WILDCARD || testCg == EXISTS)
                    && (c.get(n).equals(testC) || testC == WILDCARD || testC == EXISTS)
                    && (i.get(n).equals(testI) || testI == WILDCARD || testI == EXISTS && i
                            .get(n) != NULL_IMAGE) && !removed.contains(N)) {
                result.add(N);
            }
        }
        return result;
    }

    public Long[] get(Long n) {
        Long[] values = new Long[3];
        values[CG.intValue()] = (Long) cg.get(n.intValue());
        values[C.intValue()] = (Long) c.get(n.intValue());
        values[I.intValue()] = (Long) i.get(n.intValue());
        return values;
    }

    public int size() {
        return cg.size() - removed.size();
    }

    public Set uniqueGroups() {
        return new HashSet(cg);
    }

    public Set uniqueCats() {
        return new HashSet(c);
    }

    public Set uniqueImages() {
        return new HashSet(i); // TODO remove negatives?
    }

    public Set unique(Long which, Long testCg, Long testC, Long testI) {
        if (which.equals(CG) || which.equals(C) || which.equals(I)) {
            Set retVal = find(testCg, testC, testI);
            Set collect = new HashSet();
            for (Iterator it = retVal.iterator(); it.hasNext();) {
                Long idx = (Long) it.next();
                Long[] values = get(idx);
                collect.add(values[which.intValue()]);
            }
            return collect;
        } else {
            throw new ApiUsageException(which + " is an unknown index.");
        }
    }

    public Long[] singlePath() {
        Long[] tmp = new Long[singlePath.length];
        System.arraycopy(singlePath, 0, tmp, 0, tmp.length);
        return tmp;
    }

}