package ode.services.formats;

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

import static ode.xml.model.Pixels.getPhysicalSizeXUnitXsdDefault;
import static ode.xml.model.Pixels.getPhysicalSizeYUnitXsdDefault;
import static ode.xml.model.Pixels.getPhysicalSizeZUnitXsdDefault;
import static ode.formats.model.UnitsFactory.makeLengthXML;

import java.io.IOException;
import java.util.StringTokenizer;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.api.RawPixelsStore;
import ode.model.Image;
import ode.model.Pixels;
import ode.api.RawPixelsStorePrx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link IFormatReader} for use in export.
 */
public class OdeReader extends FormatReader {

    private final static Logger log = LoggerFactory.getLogger(OdeReader.class);

    private final RawPixelsStore raw;

    private final RawPixelsStorePrx prx;

    private final Pixels pix;

    public final int sizeX, sizeY, sizeZ, sizeT, sizeC, planes;

    private OdeReader(Pixels pix, RawPixelsStore raw, RawPixelsStorePrx prx) {
        super("ODE", "*");
        this.pix = pix;
        this.prx = prx;
        this.raw = raw;
        sizeX = pix.getSizeX().getValue();
        sizeY = pix.getSizeY().getValue();
        sizeZ = pix.getSizeZ().getValue();
        sizeC = pix.getSizeC().getValue();
        sizeT = pix.getSizeT().getValue();
        planes = sizeZ * sizeC * sizeT;
        if ( (this.raw == null && this.prx == null) ||
             (this.raw != null && this.prx != null)) {
            throw new RuntimeException("Improperly configured");
        }
    }

    public OdeReader(RawPixelsStore raw, Pixels pix) {
        this(pix, raw, null);
    }

    public OdeReader(RawPixelsStorePrx prx, Pixels pix) {
        this(pix, null, prx);
    }

    public boolean isThisType(String name, boolean open) {
        StringTokenizer st = new StringTokenizer(name, "\n");
        return st.countTokens() == 5; // TODO what is this?
    }

    public boolean isThisType(RandomAccessInputStream stream)
            throws IOException {
        return true; // TODO reading from an input stream?
    }

    public byte[] openBytes(int no, byte[] buf, int x1, int y1, int w1, int h1)
            throws FormatException, IOException {

        FormatTools.assertId(currentId, true, 1);
        FormatTools.checkPlaneNumber(this, no);
        FormatTools.checkBufferSize(this, buf.length);

        int[] zct = FormatTools.getZCTCoords(this, no);

        byte[] plane = null;
        if (raw != null) {
            plane = raw.getPlane(zct[0], zct[1], zct[2]);
        } else if (prx != null) {
            try {
                plane = prx.getPlane(zct[0], zct[1], zct[2]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Improperly configured");
        }

        int len = getSizeX() * getSizeY()
                * FormatTools.getBytesPerPixel(getPixelType());
        System.arraycopy((byte[]) plane, 0, buf, 0, len);

        return buf;
    }

    public void close() throws IOException {
        super.close();
    }

    protected void initFile(String id) throws FormatException, IOException {
        log.debug("OdeReader.initFile(" + id + ")");

        super.initFile(id);

        String ptype = pix.getPixelsType().getValue().getValue();
        String dorder = pix.getDimensionOrder().getValue().getValue();
        CoreMetadata ms0 = core.get(0);

        ms0.sizeX = sizeX;
        ms0.sizeY = sizeY;
        ms0.sizeZ = sizeZ;
        ms0.sizeC = sizeC;
        ms0.sizeT = sizeT;
        ms0.rgb = false;
        ms0.littleEndian = false;
        ms0.dimensionOrder = dorder;
        ms0.imageCount = planes;
        ms0.pixelType = FormatTools.pixelTypeFromString(ptype);

        double px = pix.getSizeX().getValue();
        double py = pix.getSizeY().getValue();
        double pz = pix.getSizeZ().getValue();

        Image image = pix.getImage();

        String name = image.getName().getValue();
        String description = null;
        if (image.getDescription() != null) {
            description = image.getDescription().getValue();
        }

        MetadataStore store = getMetadataStore();
        store.setImageName(name, 0);
        store.setImageDescription(description, 0);
        MetadataTools.populatePixels(store, this);

        store.setPixelsPhysicalSizeX(makeLengthXML(px, getPhysicalSizeXUnitXsdDefault()), 0);
        store.setPixelsPhysicalSizeY(makeLengthXML(py, getPhysicalSizeYUnitXsdDefault()), 0);
        store.setPixelsPhysicalSizeZ(makeLengthXML(pz, getPhysicalSizeZUnitXsdDefault()), 0);
    }

}