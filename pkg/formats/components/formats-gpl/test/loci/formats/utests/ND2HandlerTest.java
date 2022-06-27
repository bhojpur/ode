/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.utests;

import java.util.List;
import java.util.ArrayList;

import loci.formats.CoreMetadata;
import loci.formats.CoreMetadataList;
import loci.formats.in.ND2Handler;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link ND2Handler}.
 */
public class ND2HandlerTest {

  private CoreMetadataList coreList;
  private ND2Handler handler;

  @BeforeMethod
  public void setUp() {
    coreList = new CoreMetadataList();
    coreList.add(new CoreMetadata());
    handler = new ND2Handler(coreList, 1);
  }

  @DataProvider(name = "pixelsSizeKey")
  public Object[][] createPixelsSizeKey() {
    return new Object[][] {
      {"dZStep", ".1", .1},
      {"- Step .1 ", "", .1},
      {"- Step .1", "", .1},
      {"- Step ,1 ", "", .1},
      {"- Step", "", 0.0},
      {"- Step ", "", 0.0},
      {"- Step d", "", 0.0},
    };
  }
  
  @Test(dataProvider="pixelsSizeKey")
  public void testParsePixelsSizeZ(String key, String value, double pixelSizeZ)
  {
    handler.parseKeyAndValue(key, value, "");
    assertEquals(handler.getPixelSizeZ(), pixelSizeZ);
  }
}
