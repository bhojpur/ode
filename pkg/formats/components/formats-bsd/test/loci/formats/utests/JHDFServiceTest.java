/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */
package loci.formats.utests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import ch.systemsx.cisd.base.mdarray.MDIntArray;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.services.JHDFService;
import org.testng.Assert;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class JHDFServiceTest {

    // The test file is 'test_jhdf.h5' from:

    private static final String TEST_FILE = "test_jhdf.h5";

    private JHDFService service;

    @BeforeMethod
    public void setUp() throws DependencyException, IOException {
        ServiceFactory sf = new ServiceFactory();
        service = sf.getInstance(JHDFService.class);
        URL file = JHDFServiceTest.class.getResource(TEST_FILE);
        service.setFile(file.getPath());
    }

    @Test
    public void testGetFile() {
        assertEquals(TEST_FILE, new File(service.getFile()).getName());
    }

    @Test
    public void testGetShape() {
        int[] shape = service.getShape("/member_1/member_3/int_matrix");
        assertEquals(shape.length, 3);
        assertEquals(shape[0], 10);
        assertEquals(shape[1], 9);
        assertEquals(shape[2], 8);
    }

    @Test
    public void testReadString() {
        String[] strings = service.readStringArray("/member_2/strings");
        for (int i = 0; i < strings.length; i++) {
            Assert.assertTrue(strings[i].equals(String.format("string_%d", i)));
        }

    }

    @Test
    public void testReadIntMatrix() {
        MDIntArray matrix = service.readIntArray("/member_1/member_3/int_matrix");
        assertEquals(matrix.get(2, 1, 0), 123);
    }

    @Test
    public void testMember() {
        List<String> member = service.getMember("/member_1/");
        assertEquals(member.get(0), "member_3");
    }

    @Test
    public void testReadIntBlockArray() {
        MDIntArray matrix = service.readIntBlockArray("/member_1/member_3/int_matrix",
                new int[]{0, 0, 0},
                new int[]{10, 1, 8});
        assertEquals(matrix.get(6, 0, 4), 178);
    }

}
