/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.utests;

import loci.formats.ImageReader;
import loci.formats.in.MetadataOptions;
import loci.formats.in.DynamicMetadataOptions;
import loci.formats.in.MetadataLevel;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class ImageReaderTest {

  public static final String KEY = "test.option";
  public static final String VALUE = "foo";

  @DataProvider(name = "levels")
  public Object[][] createLevels() {
    return new Object[][] {
      {MetadataLevel.MINIMUM},
      {MetadataLevel.NO_OVERLAYS},
      {MetadataLevel.ALL}
    };
  }

  @Test
  public void testOptionsExplicit() throws Exception {
    DynamicMetadataOptions opt = new DynamicMetadataOptions();
    opt.set(KEY, VALUE);
    ImageReader reader = new ImageReader();
    reader.setMetadataOptions(opt);
    reader.setId("test.fake");
    MetadataOptions rOpt = reader.getReader().getMetadataOptions();
    assertTrue(rOpt instanceof DynamicMetadataOptions);
    String v = ((DynamicMetadataOptions) rOpt).get(KEY);
    assertNotNull(v);
    assertEquals(v, VALUE);
    reader.close();
  }

  @Test(dataProvider = "levels")
  public void testOptionsImplicit(MetadataLevel level) throws Exception {
    ImageReader reader = new ImageReader();
    reader.getMetadataOptions().setMetadataLevel(level);
    reader.setId("test.fake");
    MetadataLevel rLevel =
      reader.getReader().getMetadataOptions().getMetadataLevel();
    assertEquals(rLevel, level);
    reader.close();
  }

}
