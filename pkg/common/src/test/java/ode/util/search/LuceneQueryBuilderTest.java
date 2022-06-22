package ode.util.search;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LuceneQueryBuilderTest {

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    
    @Test
    public void test_buildLuceneQuery() throws InvalidQueryException, ParseException {
        
        String raw, expected;
        List<String> fields = new ArrayList<String>();
        
        // Invalid
        raw = "%"; expected = "";
        checkQuery(fields, raw, expected);
        
        // No fields are provided
        
        raw = "dv";  expected = "dv";
        checkQuery(fields, raw, expected);
        
        raw = "test dv";  expected = "test dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test dv";  expected = "*test dv";
        checkQuery(fields, raw, expected);
        
        raw = "test *dv";  expected = "test *dv";
        checkQuery(fields, raw, expected);
        
        raw = "?test dv";  expected = "?test dv";
        checkQuery(fields, raw, expected);
        
        raw = "test ?dv";  expected = "test ?dv";
        checkQuery(fields, raw, expected);
        
        raw = "test * dv";  expected = "test dv";
        checkQuery(fields, raw, expected);
        
        raw = "test *.dv";  expected = "test dv";
        checkQuery(fields, raw, expected);
        
        raw = "test \"*dv\"";  expected = "test \"*dv\"";
        checkQuery(fields, raw, expected);
        
        raw = "\"?test *dv\"";  expected = "\"?test *dv\"";
        checkQuery(fields, raw, expected);
        
        raw = "test-dv";  expected = "test-dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test_dv";  expected = "*test_dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test-dv";  expected = "*test-dv";
        checkQuery(fields, raw, expected);
          
        raw = "test AND dv";  expected = "((test) AND (dv))";
        checkQuery(fields, raw, expected);
       
        // MapAnnotations specific search terms
        
        raw = "has_key:foo"; expected = "has_key:foo";
        checkQuery(fields, raw, expected);
        
        raw = "foo:bar"; expected = "foo:bar";
        checkQuery(fields, raw, expected);
        
        
        // single field
        fields.add("name");
        
        raw = "dv";  expected = "name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test dv";  expected = "name:test name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test dv";  expected = "name:*test name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test *dv";  expected = "name:test name:*dv";
        checkQuery(fields, raw, expected);
        
        raw = "?test dv";  expected = "name:?test name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test ?dv";  expected = "name:test name:?dv";
        checkQuery(fields, raw, expected);

        raw = "test * dv";  expected = "name:test name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test *.dv";  expected = "name:test name:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test \"*dv\"";  expected = "name:test name:\"*dv\"";
        checkQuery(fields, raw, expected);
        
        raw = "\"?test *.dv\"";  expected = "name:\"?test *.dv\"";
        checkQuery(fields, raw, expected);
        
        raw = "(test-dv}";  expected = "name:test-dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test_dv";  expected = "name:*test_dv";
        checkQuery(fields, raw, expected);
        
        raw = "test AND dv";  expected = "((name:test) AND (name:dv))";
        checkQuery(fields, raw, expected);
        
        // MapAnnotations specific search terms
        
        raw = "has_key:foo"; expected = "has_key:foo";
        checkQuery(fields, raw, expected);
        
        raw = "foo:bar"; expected = "foo:bar";
        checkQuery(fields, raw, expected);
        
        // multiple fields
        fields.add("description");
        
        raw = "dv";  expected = "name:dv description:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test dv";  expected = "name:test description:test name:dv description:dv";
        checkQuery(fields, raw, expected);
        
        raw = "*test dv";  expected = "name:*test description:*test name:dv description:dv";
        checkQuery(fields, raw, expected);
        
        raw = "test *dv";  expected = "name:test description:test name:*dv description:*dv";
        checkQuery(fields, raw, expected);
        
        raw = "a b AND c AND d f";  expected = "name:a description:a name:f description:f ((name:b description:b) AND (name:c description:c) AND (name:d description:d))";
        checkQuery(fields, raw, expected);
        
        // MapAnnotations specific search terms
        
        raw = "has_key:foo"; expected = "has_key:foo";
        checkQuery(fields, raw, expected);
        
        raw = "foo:bar"; expected = "foo:bar";
        checkQuery(fields, raw, expected);
        
        // date search
        // Note: Lucene date range's "to" is exclusive, so there has to be an extra day added to it
        Date from = df.parse("20140701");
        Date to = df.parse("20140702");
        String dateType = LuceneQueryBuilder.DATE_ACQUISITION;
        
        raw = "a b"; expected = "(name:a description:a name:b description:b) AND acquisitionDate:[20140701 TO 20140703]";
        checkQuery(fields, from, to, dateType, raw, expected);
        
        to = null;
        dateType = LuceneQueryBuilder.DATE_IMPORT;
        raw = "a b"; expected = "(name:a description:a name:b description:b) AND details.creationEvent.time:[20140701 TO "+getTomorrowDateString()+"]";
        checkQuery(fields, from, to, dateType, raw, expected);
        
        from = null;
        to = df.parse("20140702");
        dateType = LuceneQueryBuilder.DATE_IMPORT;
        raw = "a b"; expected = "(name:a description:a name:b description:b) AND details.creationEvent.time:[19700101 TO 20140703]";
        checkQuery(fields, from, to, dateType, raw, expected);
        
        // date only search
        from = df.parse("20110701");
        to = df.parse("20110704");
        raw = ""; expected = "details.creationEvent.time:[20110701 TO 20110705]";
        checkQuery(new ArrayList<String>(), from, to, dateType, raw, expected);
    }
    
    private String getTomorrowDateString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return df.format(cal.getTime());
    }
    
    private void checkQuery(List<String> fields, String raw, String expected) throws InvalidQueryException {
        String processed = LuceneQueryBuilder.buildLuceneQuery(fields, raw);
        Assert.assertEquals(expected, processed, "Failed on query: "+raw);
    }
    
    private void checkQuery(List<String> fields, Date from, Date to, String dateType, String raw, String expected) throws InvalidQueryException {
        String processed = LuceneQueryBuilder.buildLuceneQuery(fields, from, to, dateType, raw);
        Assert.assertEquals(expected, processed, "Failed on query: "+raw);
    }
}