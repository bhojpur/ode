/*
 * BSD implementations of ODE-Formats readers and writers
 */
package loci.formats.out;

import java.io.IOException;
import loci.common.Constants;
import loci.common.RandomAccessOutputStream;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.FormatWriter;
import loci.formats.meta.MetadataRetrieve;
import static loci.common.DataTools.unpackBytes;

/**
 * V3DrawWriter writes images in the .v3draw format for rapid I/O in <a
 * href="http://www.alleninstitute.org/what-we-do/brain-science/research/products-tools/vaa3d/">Vaa3D</a>, an open-source 3D visualization and
 * analysis toolkit.
 *
 * <dd><a
 * <a
 */
public class V3DrawWriter extends FormatWriter {

  // -- Fields --
    private long pixelOffset = 43;
    private long lastPlane = -1;
    private RandomAccessOutputStream pixels;

    private String outputOrder = "XYZCT";

  // -- Constructor --
    public V3DrawWriter() {
        super("Vaa3d", new String[]{"v3draw"});

    }

  // -- V3DRAWWriter API methods --
    /**
     * Set the order in which dimensions should be written to the file. Valid
     * values are specified in the documentation for
     * {@link loci.formats.IFormatReader#getDimensionOrder()}
     *
     * By default, the ordering is "XYZCT".
     */
    public void setOutputOrder(String outputOrder) {
        this.outputOrder = outputOrder;
    }

  // -- IFormatWriter API methods --
    /**
     * @see loci.formats.IFormatWriter#saveBytes(int, byte[], int, int, int,
     * int)
     */
    @Override
    public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
            throws FormatException, IOException {
        if (!isFullPlane(x, y, w, h)) {
            throw new FormatException("V3DRawWriter does not support writing tiles");
        }

        final String formatkey = "raw_image_stack_by_hpeng"; // for header  
        byte[] v2 = new byte[2];
        byte[] v4 = new byte[4];

        int[] sz = new int[4];  // new variable for clarity: vaa3d is in xyzct format

        checkParams(no, buf, x, y, w, h);
      // .v3draw format doesn't know anything about x and y offsets, so they are ignored, asssuming
        // each byte [] buf is a full width-height plane.

        if (pixels == null) {
            pixels = new RandomAccessOutputStream(currentId);
        }
        String endianString = "L";
        MetadataRetrieve meta = getMetadataRetrieve();
        boolean bigendian = false;
        if (meta.getPixelsBigEndian(series) != null) {
          bigendian = meta.getPixelsBigEndian(series).booleanValue();
        }
        else if (meta.getPixelsBinDataCount(series) == 0) {
          bigendian = meta.getPixelsBinDataBigEndian(series, 0).booleanValue();
        }
        if (!bigendian) {
            endianString = "L";
        }else{
            endianString = "B";
        }
        int rgbChannels = getSamplesPerPixel();

        String order = meta.getPixelsDimensionOrder(series).getValue();
        int sizeZ = meta.getPixelsSizeZ(series).getValue().intValue();
        int sizeC = meta.getChannelCount(series);
        if (rgbChannels <= sizeC) {
            sizeC /= rgbChannels;
        }
        int sizeT = meta.getPixelsSizeT(series).getValue().intValue();
        int planes = sizeZ * sizeC * sizeT;

        int[] coords
                = FormatTools.getZCTCoords(order, sizeZ, sizeC, sizeT, planes, no);
        int realIndex
                = FormatTools.getIndex(outputOrder, sizeZ, sizeC, sizeT, planes,
                        coords[0], coords[1], coords[2]);

        int sizeX = meta.getPixelsSizeX(series).getValue().intValue();
        int sizeY = meta.getPixelsSizeY(series).getValue().intValue();
        int pixelType
                = FormatTools.pixelTypeFromString(meta.getPixelsType(series).toString());
        int bytesPerPixel = FormatTools.getBytesPerPixel(pixelType);
        long planeSize = sizeX * sizeY * bytesPerPixel * rgbChannels;

//FormatTools.get
        sz[0] = sizeX;
        sz[1] = sizeY;
        sz[2] = sizeZ * sizeT;// temporary aggregate for layer
        sz[3] = sizeC * rgbChannels; // temp aggregate for color
        if (!initialized[series][realIndex]) {
            initialized[series][realIndex] = true;
        }
        try {
//write the header if it's the first time through       
            if (lastPlane == -1) {
                pixels.write(formatkey.getBytes(Constants.ENCODING));             // write format key
                pixels.write(endianString.getBytes(Constants.ENCODING));          // endianness.
                unpackBytes(bytesPerPixel, v2, 0, 2, !bigendian);
                pixels.write(v2);                                // unitSize 
                for (int d : sz) {
                    unpackBytes(d, v4, 0, 4, !bigendian);
                    pixels.write(v4);
                }        // and image dimensions into header 
                pixels.write(buf);
                LOGGER.info("*********   V3DrawWriter.java internal variables  *********");
                LOGGER.info("bytesPerPixel = " + bytesPerPixel);
                LOGGER.info("pixelType = " + pixelType);
                LOGGER.info("rgbChannels =" + rgbChannels);
                LOGGER.info("sizeC = " + sizeC);
                LOGGER.info("sizeZ = " + sizeZ);
                LOGGER.info("sizeT = " + sizeT);
                LOGGER.info("endian= " + endianString);
            } else {
                pixels.seek(planeSize * realIndex + pixelOffset);
                //write the rest of the plane 
                pixels.write(buf);
            }
            lastPlane = realIndex;
        } finally {
            pixels.close();
            pixels = null;
        }
    }

    /* @see loci.formats.IFormatWriter#canDoStacks() */
    @Override
    public boolean canDoStacks() {
        return true;
    }

    /* @see loci.formats.IFormatWriter#getPixelTypes(String) */
    @Override
    public int[] getPixelTypes(String codec) {
        return new int[]{FormatTools.UINT8,
            FormatTools.UINT16,
            FormatTools.FLOAT};
    }

  // -- FormatWriter API methods --

    /* @see loci.formats.FormatWriter#setId(String) */
    @Override
    public void setId(String id) throws FormatException, IOException {
        super.setId(id);
    }

    /* @see loci.formats.FormatWriter#close() */
    @Override
    public void close() throws IOException {
        super.close();
        pixelOffset = 43;
        lastPlane = -1;
        if (pixels != null) {
            pixels.close();
        }
        pixels = null;
    }

  // -- Helper methods --
}
