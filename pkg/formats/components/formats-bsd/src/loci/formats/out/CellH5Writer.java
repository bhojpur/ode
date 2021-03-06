package loci.formats.out;

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

import ch.systemsx.cisd.base.mdarray.MDByteArray;
import ch.systemsx.cisd.base.mdarray.MDIntArray;
import ch.systemsx.cisd.base.mdarray.MDShortArray;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import loci.common.services.DependencyException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.FormatWriter;
import loci.formats.MissingLibraryException;
import loci.formats.in.CellH5Reader;
import loci.formats.in.CellH5Reader.CellH5Constants;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.services.JHDFService;
import loci.formats.services.JHDFServiceImpl;

import ode.xml.model.enums.DimensionOrder;

/**
 * CellH5Writer is the file format writer for CellH5 format.
 */
public class CellH5Writer extends FormatWriter {

  // -- Constants --

  // -- Fields --
  private transient JHDFService jhdf;
  
  private long bpp;
  private String outputPath;
  
  // -- Constructors --

  public CellH5Writer() {
    this("CellH5 File Format", new String[] {"ch5"});
  }

  public CellH5Writer(String format, String[] exts) {
    super(format, exts);
  }

  // -- FormatWriter API methods --

  /* @see loci.formats.FormatWriter#setId(String) */
  @Override
  public void setId(String id) throws FormatException, IOException { 
    if (id.equals(currentId)) return;
    super.setId(id);
    try {
      ServiceFactory factory = new ServiceFactory();
      jhdf = factory.getInstance(JHDFService.class);
      jhdf.setFileForWrite(id);
    } catch (DependencyException e) {
      throw new MissingLibraryException(JHDFServiceImpl.NO_JHDF_MSG, e);
    }
    
    MetadataRetrieve retrieve = getMetadataRetrieve();

    int sizeX = retrieve.getPixelsSizeX(0).getValue();
    int sizeY = retrieve.getPixelsSizeY(0).getValue();
    int sizeZ = retrieve.getPixelsSizeZ(0).getValue();
    int sizeC = retrieve.getPixelsSizeC(0).getValue();
    int sizeT = retrieve.getPixelsSizeT(0).getValue();
    int type = FormatTools.pixelTypeFromString(retrieve.getPixelsType(0).toString());
    bpp = FormatTools.getBytesPerPixel(type);
    
    LOGGER.info("CellH5Writer: Found image with dimensions XYZCT {}x{}x{}x{}x{}, and bits per pixel {}", sizeX, sizeY, sizeZ, sizeC, sizeT, bpp);
    
    String plate = "PLATE_00";
    String well = "WELL_00";
    int site = 1;
    
    if (retrieve.getPlateCount()>0) {
        plate = retrieve.getPlateName(0);
        well = retrieve.getWellExternalIdentifier(0, 0);
        site = retrieve.getWellSampleIndex(0, 0, 0).getValue();
        LOGGER.info("CellH5Writer: Found plate information Plate / Well / Site {} / {} / {}", plate, well, site);
    } else {
        LOGGER.info("CellH5Writer: No plate information found. Using default values...");
    }
    
    jhdf.createGroup(CellH5Constants.DEFINITION + CellH5Reader.CellH5Constants.OBJECT);
    jhdf.createGroup(CellH5Constants.DEFINITION + CellH5Reader.CellH5Constants.FEATURE);
    jhdf.createGroup(CellH5Constants.DEFINITION + CellH5Reader.CellH5Constants.IMAGE);
    
    outputPath = String.format("/sample/0/plate/%s/experiment/%s/position/%d/image/channel", plate, well, site);
    jhdf.initIntArray(outputPath, new long[] {sizeC, sizeT, sizeZ, sizeY, sizeX}, bpp);
  }

  /**
   * Saves the given image to the specified (possibly already open) file.
   */
  @Override
  public void saveBytes(int no, byte[] buf)
    throws IOException, FormatException
  {
    LOGGER.info("CellH5Writer: Save image to HDF5 path: " + outputPath);
    MetadataRetrieve r = getMetadataRetrieve();
    
    int sizeX = r.getPixelsSizeX(series).getValue();
    int sizeY = r.getPixelsSizeY(series).getValue();
    int sizeC = r.getPixelsSizeC(series).getValue();
    int sizeT = r.getPixelsSizeT(series).getValue();
    int sizeZ = r.getPixelsSizeZ(series).getValue();
    
    DimensionOrder dimo = r.getPixelsDimensionOrder(0);
    int c, z, t;
    if (dimo.equals(DimensionOrder.XYCZT)) {
        c = no % sizeC;
        z = ((no - c) / sizeC) % sizeZ;
        t = (((no - c) / sizeC)) / sizeZ;
    } else if (dimo.equals(DimensionOrder.XYCTZ)){
        c = no % sizeC;
        t = ((no - c) / sizeC) % sizeT;
        z = (((no - c) / sizeC)) / sizeT;
        
    } else if (dimo.equals(DimensionOrder.XYZTC)){
        z = no % sizeZ;
        t = ((no - z) / sizeZ) % sizeT;
        c = (((no - z) / sizeZ)) / sizeT;
        
    } else {
        throw new FormatException("CellH5Writer: Dimension order not understood: " + dimo.getValue());
    }
    
    LOGGER.info("CellH5Writer.saveBytes(): Current c, t, z == {} {} {}", c,t,z);
    LOGGER.info("CellH5Writer.saveBytes(): bpp {} byte buffer len {}", bpp, buf.length);
    
    if (bpp==1) {
        MDByteArray image = new MDByteArray(new int[] {1, 1, 1, sizeY, sizeX});
        for (int x_i=0; x_i < sizeX; x_i++) {
            for (int y_i=0; y_i < sizeY; y_i++) {
                byte value = (byte) buf[y_i*sizeX + x_i];
                image.set(value, 0, 0, 0, y_i, x_i );
            }
        }
        jhdf.writeArraySlice(outputPath, image, new long[] {c,t,z, 0, 0});
    } else if (bpp==2) {
        ByteBuffer bb = ByteBuffer.wrap(buf);
        ShortBuffer sb = bb.asShortBuffer();
        MDShortArray image = new MDShortArray(new int[] {1, 1, 1, sizeY, sizeX});
        for (int x_i=0; x_i < sizeX; x_i++) {
            for (int y_i=0; y_i < sizeY; y_i++) {
                short value = sb.get(y_i*sizeX + x_i);
                image.set(value, 0, 0, 0, y_i, x_i );
            }
        }
        jhdf.writeArraySlice(outputPath, image, new long[] {c,t,z, 0, 0});
    } else if (bpp==4) {
        ByteBuffer bb = ByteBuffer.wrap(buf);
        IntBuffer ib = bb.asIntBuffer();
        MDIntArray image = new MDIntArray(new int[] {1, 1, 1, sizeY, sizeX});
        for (int x_i=0; x_i < sizeX; x_i++) {
            for (int y_i=0; y_i < sizeY; y_i++) {
                int value = (int) ib.get(y_i*sizeX + x_i);
                image.set(value, 0, 0, 0, y_i, x_i );
            }
        }
        jhdf.writeArraySlice(outputPath, image, new long[] {c,t,z, 0, 0});
    } else {
        throw new FormatException("CellH5Writer: Pixel type not supported");
    }
  }

  // -- FormatWriter API methods --

  /* (non-Javadoc)
   * @see loci.formats.FormatWriter#close()
   */
  @Override
  public void close() throws IOException {
    jhdf.close();
    super.close();
  }

  /* @see loci.formats.FormatWriter#getPlaneCount() */
  @Override
  public int getPlaneCount() {
    MetadataRetrieve retrieve = getMetadataRetrieve();
    int c = getSamplesPerPixel();
    int type = FormatTools.pixelTypeFromString(
      retrieve.getPixelsType(series).toString());
    int bytesPerPixel = FormatTools.getBytesPerPixel(type);

    if (bytesPerPixel > 1 && c != 1 && c != 3) {
      return super.getPlaneCount() * c;
    }
    return super.getPlaneCount();
  }

  // -- IFormatWriter API methods --

  /**
   * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int, int)
   */
  @Override
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
      // always save to full image
      saveBytes(no, buf);
  }
  
  /* @see loci.formats.IFormatWriter#canDoStacks(String) */
  @Override
  public boolean canDoStacks() { return true; }

  /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
  @Override
  public int[] getPixelTypes(String codec) {
    return new int[] {FormatTools.UINT8, FormatTools.UINT16, FormatTools.INT32};
  }
}
