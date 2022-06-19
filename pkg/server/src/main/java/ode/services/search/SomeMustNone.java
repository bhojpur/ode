package ode.services.search;

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

import ode.conditions.ApiUsageException;

import org.apache.lucene.analysis.Analyzer;

/**
 * {@link FullText} subclass which
 * {@link #parse(String[], String[], String[]) parses} 3 arrays of strings into
 * into a single Lucene query. If no text is produced, then an exception will be
 * thrown. Some terms are joined in to "( a OR b OR c)", must terms are turned
 * into "+d +e +f", and none terms are turned into "-g -h -i".
 */
public class SomeMustNone extends FullText {

    private static final long serialVersionUID = 1L;

    private final String[] some;
    private final String[] must;
    private final String[] none;

    public SomeMustNone(SearchValues values, String[] some, String[] must,
            String[] none, Class<? extends Analyzer> analyzer) {
        super(values, parse(some, must, none), analyzer);
        this.some = some;
        this.must = must;
        this.none = none;
    }

    protected static String parse(String[] some, String[] must, String[] none) {
        final StringBuilder sb = new StringBuilder();

        if (some != null && some.length > 0) {
            sb.append("(");
            boolean first = true;
            for (String string : some) {
                if (string.length() > 0) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(" OR ");
                    }
                    sb.append(string);
                }
            }
            sb.append(")");
        }

        if (sb.length() > 0) {
            sb.append(" ");
        }

        if (must != null && must.length > 0) {
            for (String string : must) {
                if (string.length() > 0) {
                    sb.append(" +");
                    sb.append(string);
                }
            }
        }

        if (sb.length() > 0) {
            sb.append(" ");
        }

        if (none != null && none.length > 0) {
            for (String string : none) {
                if (string.length() > 0) {
                    sb.append(" -");
                    sb.append(string);
                }
            }
        }

        if (sb.toString().length() == 0) {
            throw new ApiUsageException(
                    "No search terms provided for SomeMustNone");
        }
        return sb.toString();
    }
}
