package spec.schema;

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

import ode.specification.SchemaResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.w3c.dom.ls.LSInput;

/** Tests for ode.specification.SchemaResolver. */
@Test(groups = { "all" })
public class SchemaResolverTest {

    /** List of published schema versions. */
    private static final String[][] SCHEMAS = {
      {"/XMLschemas/ODE/FC/2018-FC"}, {"/Schemas/2007-06"},
      {"/Schemas/2008-02"}, {"/Schemas/2008-04"}, {"/Schemas/2008-09"},
      {"/Schemas/2009-09"}, {"/Schemas/2010-04"}, {"/Schemas/2010-06"},
      {"/Schemas/2011-06"}, {"/Schemas/2012-06"}, {"/Schemas/2012-06"},
      {"/Schemas/2013-06"}, {"/Schemas/2015-01"}, {"/Schemas/2018-03"}};

    /** Holds the error, info, warning. */
    protected Logger log = LoggerFactory.getLogger(getClass());

    private SchemaResolver resolver = null;

    /**
     * Initializes the various services.
     * @throws Exception Thrown if an error occurred.
     */
    @BeforeClass
    protected void setUp() throws Exception {
      resolver = new SchemaResolver();
    }

    /**
     * Closes the session.
     * @throws Exception Thrown if an error occurred.
     */
    @AfterClass
    public void tearDown() throws Exception {
      resolver = null;
    }

    @DataProvider(name = "schemas")
    public Object[][] getSchemas() {
      return SCHEMAS;
    }

    @Test(dataProvider = "schemas")
    public void testResolution(String schema) {
      String schemaPath = "http://www.bhojpur.net" + schema + "/ode.xsd";
      LSInput resolvedSchema = resolver.resolveResource((String) null,
        (String) null, (String) null, schemaPath, (String) null);
      assert resolvedSchema != null;
    }

}
