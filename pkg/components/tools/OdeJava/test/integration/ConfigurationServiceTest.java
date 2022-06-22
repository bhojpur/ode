package integration;

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
import java.util.Set;

import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import ode.api.IConfigPrx;
import ode.api.ITypesPrx;
import ode.model.Format;
import ode.model.IObject;
import ode.sys.ParametersI;

import org.apache.commons.beanutils.PropertyUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Collections of tests for the <code>IConfig</code> service.
 */
public class ConfigurationServiceTest extends AbstractServerTest {

    /** Helper reference to the <code>IConfig</code> service. */
    private IConfigPrx iConfig;

    /**
     * Initializes the various services.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Override
    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        iConfig = factory.getConfigService();
    }

    /**
     * Tests the <code>getServerTime</code> method. Access the method as a non
     * administrator.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testServerTime() throws Exception {
        Assert.assertNotNull(iConfig.getServerTime());
    }

    /**
     * Tests the <code>getDatabaseTime</code> method. Access the method as a non
     * administrator.s
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDatabaseTime() throws Exception {
        Assert.assertNotNull(iConfig.getDatabaseTime());
    }

    /**
     * Tests the <code>getServerTime</code> method. Access the method as an
     * administrator
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testServerTimeAsAdmin() throws Exception {
        IConfigPrx svc = root.getSession().getConfigService();
        Assert.assertNotNull(svc.getServerTime());
    }

    /**
     * Tests the <code>getDatabaseTime</code> method. Access the method as an
     * administrator
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDatabaseTimeAsAdmin() throws Exception {
        IConfigPrx svc = root.getSession().getConfigService();
        Assert.assertNotNull(svc.getDatabaseTime());
    }

    /**
     * Tests the <code>getDatabaseTime</code> method. Access the method as an
     * administrator.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDatabaseUUIDAsAdmin() throws Exception {
        IConfigPrx svc = root.getSession().getConfigService();
        Assert.assertNotNull(svc.getDatabaseUuid());
    }

    /**
     * Tests the <code>getDatabaseUuid</code> method. Access the method as a non
     * administrator.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testDatabaseUUID() throws Exception {
        Assert.assertNotNull(iConfig.getDatabaseUuid());
    }

    /**
     * Tests the <code>setConfigValue</code> method. Access the method as an
     * administrator
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSetConfigValueAsAdmin() throws Exception {
        String key = "test";
        String value = "test2";
        IConfigPrx svc = root.getSession().getConfigService();
        svc.setConfigValue(key, value);
        Assert.assertNotNull(svc.getConfigValue(key));
        Assert.assertEquals(svc.getConfigValue(key), value);
    }

    /**
     * Tests the <code>setConfigValue</code> method. Access the method as a non
     * admin user.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSetConfigValue() throws Exception {
        String key = "test1";
        String value = "test3";
        try {
            iConfig.setConfigValue(key, value);
            Assert.fail("A non admin user cannot configure the server");
        } catch (Exception e) {
        }
    }

    /**
     * Returns the collection of supported formats.
     *
     * @return See above
     * @throws Exception If an error occurred while reading the formats.
     */
    private Set<String> getSupportedFormats() throws Exception
    {
        final String ref = "Reader";
        List<String> toExclude = new ArrayList<String>();
        toExclude.add("Fake");
        Set<String> values = new HashSet<String>();
        try (final ImageReader reader = new ImageReader()) {
            IFormatReader[] readers = reader.getReaders();
            
            for (int i = 0; i < readers.length; i++) {
                IFormatReader r = readers[i];;
                String name = r.getClass().getSimpleName();
                if (name.endsWith(ref)) {
                    name = name.substring(0, name.length() - ref.length());
                    if (!toExclude.contains(name)) {
                        values.add(name);
                        if (r.hasCompanionFiles()) {
                            values.add("Companion/"+name);
                        }
                    }
                }
            }
        }
        return values;
    }

    /**
     * Tests that the list of supported formats in the DB matches
     * what is currently supported by BioFormats.
     * This does not include the <code>FakeReader</code> since it is solely
     * used for testing.
     *
     * @throws Exception
     *             Thrown if an error occurred.
     */
    @Test
    public void testSupportedFormats() throws Exception {
        Set<String> values = getSupportedFormats();
        //Load from DB
        ParametersI param = new ParametersI();
        String sql = "select f from Format as f";
        List<IObject> objects = iQuery.findAllByQuery(sql, param);
        Assert.assertTrue(values.size() <= objects.size());
        for (int i = 0; i < objects.size(); i++) {
            Format o = (Format) objects.get(i);
            values.remove(o.getValue().getValue());
        }
        Assert.assertEquals(0, values.size());
    }

    /**
     * Loads the enumeration.
     * @return Object[][] data.
     */
    @DataProvider(name = "loadEnumData")
    public Object[][] loadEnumData() throws Exception {
        List<TestEnum> testParams = new ArrayList<TestEnum>();
        Object[][] data = null;
        ITypesPrx svc =  root.getSession().getTypesService();
        List<IObject> objects = svc.getOriginalEnumerations();
        List<String> types = svc.getEnumerationTypes();
        Assert.assertTrue(types.size() > 0);
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            Set<String> original = new HashSet<String>();
            type = "ode.model."+type+"I";
            for (int j = 0; j < objects.size(); j++) {
                IObject ho = objects.get(j);
                if (ho.getClass().getName().equals(type)) {
                    original.add(getEnumValue(ho));
                }
            }
            testParams.add(new TestEnum(type, original));
        }
        int index = 0;
        Iterator<TestEnum> j = testParams.iterator();
        data = new Object[testParams.size()][1];
        while (j.hasNext()) {
            data[index][0] = j.next();
            index++;
        }
        return data;
    }

    /**
     * Returns the enumeration value.
     *
     * @param object The enumeration object.
     * @return See above.
     */
    private String getEnumValue(IObject object)
    {
        try {
            return (String) PropertyUtils.getNestedProperty(object, "value.value");
        } catch (Exception e) {}
        return null;
    }

    /**
     * Tests the retrieval of the various enumerations.
     * @throws Exception
     */
    @Test(dataProvider = "loadEnumData")
    public void testSupportedEnumerations(TestEnum param) throws Exception {
    
       ITypesPrx svc =  root.getSession().getTypesService();
       String type = param.getType();
       Set<String> original = param.getOriginal();
       List<IObject> fromDB = svc.allEnumerations(type);
       final Iterator<IObject> dbValues = fromDB.iterator();
       while (dbValues.hasNext()) {
           /* Other tests may have added new values. */
           if (getEnumValue(dbValues.next()).startsWith("test_")) {
               dbValues.remove();
           }
       }
       int total = 0;
       if (type.endsWith("EventTypeI")) {
           //Bootstrap event is added to the DB during the init process
           //see psql-footer.vm
           Assert.assertEquals(fromDB.size()-1, original.size());
           for (int i = 0; i < fromDB.size(); i++) {
               String value = getEnumValue(fromDB.get(i));
               Assert.assertNotNull(value);
               if (original.contains(value) && !value.equals("Bootstrap")) {
                   total++;
               }
           }
           Assert.assertEquals(fromDB.size()-1, total);
       } else if (type.endsWith("FormatI")) {
           //original should be 0
           Assert.assertEquals(original.size(), 0);
           Set<String> values = getSupportedFormats();
           Assert.assertTrue(values.size() <= fromDB.size());
           for (int i = 0; i < fromDB.size(); i++) {
               String value = getEnumValue(fromDB.get(i));
               Assert.assertNotNull(value);
               if (values.contains(value)) {
                   total++;
               }
           }
           Assert.assertEquals(values.size(), total);
       } else {
           Assert.assertEquals(fromDB.size(), original.size());
           for (int i = 0; i < fromDB.size(); i++) {
               String value = getEnumValue(fromDB.get(i));
               Assert.assertNotNull(value);
               if (original.contains(value)) {
                   total++;
               }
           }
           Assert.assertEquals(fromDB.size(), total);
       }
    }

    /**
     * Inner class hosting information about object to move.
     *
     */
    class TestEnum {

        /** Hold information about the object to move.*/
        private String type;

        private Set<String> original;
        /**
         * Creates a new instance.
         *
         * @param chgrp Hold information about the object to move.
         * @param user The user to log as.
         * @param password The user's password.
         * @param srcID The identifier of the group to move the data from.
         */
        TestEnum(String type, Set<String> original) {
            this.type = type;
            this.original = original;
        }

        /**
         * Returns the enumeration type.
         *
         * @return See above.
         */
        String getType() {
            return type;
        }

        /**
         * Returns the original values.
         *
         * @return See above.
         */
        Set<String> getOriginal() {
            return original;
        }

        @Override
        public String toString() {
            return String.format("TestEnum(type=%s,original_count=%d)", type, original.size());
        }
    }
}
