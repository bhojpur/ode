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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jamonapi.MonitorFactory;

/**
 * Used as a simple print out of {@link MonitorFactory} results.
 */
public class Report {

    static int LABEL = 0;
    static int HITS = 1;
    static int AVG = 2;
    static int TOTAL = 3;
    static int STDDEV = 4;
    static int LASTVALUE = 5;
    static int MIN = 6;
    static int MAX = 7;
    static int ACTIVE = 8;
    static int AVGACTIVE = 9;
    static int MAXACTIVE = 10;
    static int FIRSTACCESS = 11;
    static int LASTACCESS = 12;

    String[] header;
    Object[][] data;

    /**
     * Saves the current data from {@link MonitorFactory} and then resets all
     * values.
     */
    public Report() {
        header = MonitorFactory.getHeader();
        data = MonitorFactory.getData();
        MonitorFactory.reset();
    }

    @Override
    public String toString() {
        if (header == null) {
            return "====== No report =======";
        }
        int[] labels = new int[] { LABEL, AVG, MIN, MAX, TOTAL, HITS };
        StringBuilder sb = new StringBuilder();
        for (int l : labels) {
            sb.append(header[l]);
            for (int i = 0; i < 8 - header[l].length(); i++) {
                sb.append(" ");
            }
            sb.append("\t");
        }
        sb.append("\n");

        Map<String, String> ordering = new HashMap<String, String>();
        for (int i = 0; i < data.length; i++) {
            StringBuilder line = new StringBuilder();
            for (int l : labels) {
                Object d = data[i][l];
                if (d instanceof Double) {
                    line.append(String.format("%3.2e\t", (Double) d));
                } else {
                    line.append(d + "\t");
                }
            }
            line.append("\n");
            ordering.put((String) data[i][LABEL], line.toString());
        }
        List<String> keys = new ArrayList<String>(ordering.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            sb.append(ordering.get(key));
        }

        return sb.toString();
    }

}