/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.utests.tiff;

import static org.testng.AssertJUnit.*;

import java.io.File;

import loci.common.services.ServiceFactory;
import loci.formats.ImageWriter;
import loci.formats.in.TiffReader;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;

import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ODETiffWriterLargeImageWidthTest {

  public static final int SIZE_X = 139256;

  public static final int SIZE_Y = 4;

  public static final int SIZE_Z = 100;

  public static final int SIZE_C = 1;

  public static final int SIZE_T = 20;

  private static final byte[] buf = new byte[] {
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
    0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15
  };

  private File target;

  private ODEXMLMetadata ms;

  @BeforeClass
  public void setUp() throws Exception {
    target = File.createTempFile("ODETiffWriterTest", ".ode.tiff");

    ServiceFactory sf = new ServiceFactory();
    ODEXMLService service = sf.getInstance(ODEXMLService.class);
    ms = service.createODEXMLMetadata();
    ms.setImageID("Image:1", 0);
    ms.setPixelsID("Pixels:1", 0);
    ms.setPixelsDimensionOrder(DimensionOrder.XYZCT, 0);
    ms.setPixelsSizeX(new PositiveInteger(SIZE_X), 0);
    ms.setPixelsSizeY(new PositiveInteger(SIZE_Y), 0);
    ms.setPixelsSizeZ(new PositiveInteger(SIZE_Z), 0);
    ms.setPixelsSizeC(new PositiveInteger(SIZE_C), 0);
    ms.setPixelsSizeT(new PositiveInteger(SIZE_T), 0);
    ms.setPixelsType(PixelType.UINT8, 0);
    ms.setPixelsBinDataBigEndian(true, 0, 0);
    ms.setChannelID("Channel:1", 0, 0);
    ms.setChannelSamplesPerPixel(new PositiveInteger(1), 0, 0);
  }

  @AfterClass
  public void tearDown() throws Exception {
    target.delete();
  }

  @Test
  public void testImageWidthWrittenCorrectly() throws Exception {
    ImageWriter writer = new ImageWriter();
    writer.setMetadataRetrieve(ms);
    writer.setId(target.getAbsolutePath());
    writer.saveBytes(0, buf, 0, 0, buf.length, 1);
    writer.close();
    TiffReader reader = new TiffReader();
    reader.setId(target.getAbsolutePath());
    assertEquals(SIZE_X, reader.getSizeX());
    assertEquals(SIZE_Y, reader.getSizeY());
  }
}
