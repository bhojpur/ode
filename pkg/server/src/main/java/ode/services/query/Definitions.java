package ode.services.query;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * container for {@link ode.services.query.QueryParameterDef} instances.
 * Typically created as a static variable in a Query and passed to the super
 * constructor {@link ode.services.query.Query#Query(Definitions, Parameters)}
 */
public class Definitions {

    /**
     * internal storage for the {@link QueryParameterDef}s. Should not change
     * after construction.
     */
    final private Map<String, QueryParameterDef> defs = new HashMap<String, QueryParameterDef>();

    /* no default constructor */
    private Definitions() {
    }

    public Definitions(QueryParameterDef... parameterDefs) {
        if (parameterDefs != null) {
            for (QueryParameterDef def : parameterDefs) {
                defs.put(def.name, def);
            }
        }
    }

    public boolean containsKey(Object key) {
        return defs.containsKey(key);
    }

    public boolean isEmpty() {
        return defs.isEmpty();
    }

    public Set<String> keySet() {
        return defs.keySet();
    }

    public int size() {
        return defs.size();
    }

    public QueryParameterDef get(Object key) {
        return defs.get(key);
    }

}
