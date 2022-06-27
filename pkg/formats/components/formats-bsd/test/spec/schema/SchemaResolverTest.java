/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package spec.schema;

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
      {"/XMLschemas/ODE/FC/2003-FC"}, {"/Schemas/2007-06"},
      {"/Schemas/2008-02"}, {"/Schemas/2008-04"}, {"/Schemas/2008-09"},
      {"/Schemas/2009-09"}, {"/Schemas/2010-04"}, {"/Schemas/2010-06"},
      {"/Schemas/2011-06"}, {"/Schemas/2012-06"}, {"/Schemas/2012-06"},
      {"/Schemas/2013-06"}, {"/Schemas/2015-01"}, {"/Schemas/2016-06"}};

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
