package ode.formats.importer;

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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import loci.formats.ChannelFiller;
import loci.formats.ChannelSeparator;
import loci.formats.ClassList;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.Memoizer;
import loci.formats.MinMaxCalculator;
import loci.formats.in.LeicaReader;
import loci.formats.in.MetadataLevel;
import loci.formats.in.MetadataOptions;
import loci.formats.in.ZipReader;
import loci.formats.meta.MetadataStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.formats.ODEXMLModelComparator;
import ode.io.nio.ReaderSecurityCheck;
import ode.util.PixelData;

import ode.model.Channel;
import ode.model.Pixels;

public class ODEWrapper extends MinMaxCalculator {

    @SuppressWarnings("unused")
    private final static Logger log = LoggerFactory.getLogger(ODEWrapper.class);

    private ChannelSeparator separator;
    private ChannelFiller filler;
    private Memoizer memoizer;
    public Boolean minMaxSet = null;

    /**
     * Reference copy of <i>reader</i> so that we can be compatible with the
     * IFormatReader/ReaderWrapper interface but still maintain functionality
     * that we require.
     */
    private ImageReader iReader;


    private ImportConfig config;

    /**
     * Wrapper for bio-formats
     *
     * @param config - ImportConfit
     */
    public ODEWrapper(ImportConfig config)
    {
        this(config, -1, null); // Disabled memoization
    }

    @Deprecated
    public ODEWrapper(ImportConfig config, long elapsedTime, File cacheDirectory) {
        this(config, elapsedTime, cacheDirectory, new ReaderSecurityCheck() {
            @Override
            public void assertUsedFilesReadable(IFormatReader reader) {
            }
        });
    }

    public ODEWrapper(ImportConfig config, long elapsedTime, File cacheDirectory, ReaderSecurityCheck readerSecurityCheck) {
        super(createReader(config, readerSecurityCheck));
        this.config = config;
        this.iReader = (ImageReader) reader; // Save old value
        this.reader = null;
        filler = new ChannelFiller(iReader);
        separator  = new ChannelSeparator(filler);
        memoizer = new Memoizer(separator, elapsedTime, cacheDirectory) {
            public Deser getDeser() {
                KryoDeser k = new KryoDeser();
                k.kryo.register(ODEXMLModelComparator.class);
                return k;
            }
        };
        reader = memoizer;

        // Force unreadable characters to be removed from metadata key/value pairs
        iReader.setMetadataFiltered(true);
        filler.setMetadataFiltered(true);
        separator.setMetadataFiltered(true);
        // Force images with multiple sub-resolutions to not "duplicate" their
        // series.
        iReader.setFlattenedResolutions(false);
    };

    private static ImageReader createReader(ImportConfig config, ReaderSecurityCheck readerSecurityCheck)
    {
        if (config == null) {
            throw new IllegalArgumentException(
                    "An ImportConfig must be instantitated \n " +
                    "in order to properly configure all readers.");
        }
        String readersPath = config.readersPath.get();
        // Since we now use all readers apart from the ZipReader, just
        // initialize in this manner which helps us by not requiring
        // us to keep up with all changes in readers.txt and removes
        // the requirement for importer_readers.txt (See #2859).
        ClassList<IFormatReader> readers =
            ImageReader.getDefaultReaderClasses();
        readers.removeClass(ZipReader.class);
        if (readersPath != null)
        {
            Class<?> k = ODEWrapper.class;
            if (new File(readersPath).exists()) {
                k = null;
            }
            try
            {
                return new ImageReader(new ClassList<IFormatReader>(
                    readersPath, IFormatReader.class, k)) {
                    @Override
                    public void setId(String id) throws FormatException, IOException {
                        super.setId(id);
                        readerSecurityCheck.assertUsedFilesReadable(this);
                    }
                };
            } catch (IOException e)
            {
                throw new RuntimeException("Unable to load readers.txt.");
            }
        }
        return new ImageReader() {
            @Override
            public void setId(String id) throws FormatException, IOException {
                super.setId(id);
                readerSecurityCheck.assertUsedFilesReadable(this);
            }
        };
    }

    public ImportConfig getConfig() {
        return this.config;
    }

    /**
     * Obtains an object which represents a given sub-image of a plane within
     * the file.
     * @param id The path to the file.
     * @param planeNumber The plane or section within the file to obtain.
     * @param buf Pre-allocated buffer which has a <i>length</i> that can fit
     * the entire plane.
     * @return an object which represents the plane.
     * @throws FormatException If there is an error parsing the file.
     * @throws IOException If there is an error reading from the file or
     * acquiring permissions to read the file.
     */
    public PixelData openPlane2D(String id, int planeNumber, byte[] buf)
            throws FormatException, IOException
    {
        return openPlane2D(id, planeNumber, buf, 0, 0, getSizeX(), getSizeY());
    }

    /**
     * Obtains an object which represents a given sub-image of a plane within
     * the file.
     * @param id The path to the file.
     * @param planeNumber The plane or section within the file to obtain.
     * @param buf Pre-allocated buffer which has a <i>length</i> that can fit
     * the byte count of the sub-image.
     * @param x X coordinate of the upper-left corner of the sub-image
     * @param y Y coordinate of the upper-left corner of the sub-image
     * @param w width of the sub-image
     * @param h height of the sub-image
     * @return an object which represents the sub-image of the plane.
     * @throws FormatException If there is an error parsing the file.
     * @throws IOException If there is an error reading from the file or
     * acquiring permissions to read the file.
     */
    public PixelData openPlane2D(String id, int planeNumber, byte[] buf,
                               int x, int y, int w, int h)
            throws FormatException, IOException
    {
        // FIXME: HACK! The ChannelSeparator isn't exactly what one would call
        // "complete" so we have to work around the fact that it still copies
        // all of the plane data (all three channels) from the file if the file
        // is RGB.
        ByteBuffer plane;
        if (iReader.isRGB() || isLeicaReader()) {
            // System.err.println("RGB, not using cached buffer.");
            byte[] bytePlane = openBytes(planeNumber, x, y, w, h);
            plane = ByteBuffer.wrap(bytePlane);
        } else {
            // System.err.println("Not RGB, using cached buffer.");
            plane = ByteBuffer.wrap(openBytes(planeNumber, buf, x, y, w, h));
        }
        plane.order(isLittleEndian()? ByteOrder.LITTLE_ENDIAN
                : ByteOrder.BIG_ENDIAN);
        return new PixelData(
                FormatTools.getPixelTypeString(getPixelType()), plane);
    }

    /**
     * @return true if reader being used is LeicaReader
     */
    public boolean isLeicaReader() {
        return iReader.getReader() instanceof LeicaReader;
    }

    /**
     * @return true if min-max is set
     * @throws FormatException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public boolean isMinMaxSet() throws FormatException, IOException {
        if (minMaxSet == null) {
            MetadataStore store = reader.getMetadataStore();
            int series = reader.getSeries();
            List<Pixels> pixels = (List<Pixels>) store.getRoot();
            if (pixels == null) {
                return minMaxSet = true;
            }
            Pixels p = pixels.get(series);
            Channel c = p.getChannel(p.getSizeC().getValue() - 1);
            minMaxSet =  c.getStatsInfo() != null;
        }
        return minMaxSet;
    }

    /* (non-Javadoc)
     * @see loci.formats.MinMaxCalculator#updateMinMax(int, byte[], int)
     */
    @Override
    protected void updateMinMax(int no, byte[] buf, int len)
        throws FormatException, IOException {
        if (!isMinMaxSet())
            super.updateMinMax(no, buf, len);
    }

    /* (non-Javadoc)
     * @see loci.formats.ReaderWrapper#close()
     */
    public void close() throws IOException {
        minMaxSet = null;
        super.close(false);
    }

    /**
     * Return the base image reader
     *
     * @return See above.
     */
    public ImageReader getImageReader() {
        return iReader;
    }

    /**
     * @return true if using SPW reader
     */
    public boolean isSPWReader()
    {
        String[] domains = reader.getDomains();
        return Arrays.asList(domains).contains(FormatTools.HCS_DOMAIN);
    }

    /* (non-Javadoc)
     * @see loci.formats.ReaderWrapper#getMetadataOptions()
     */
    @Override
    public MetadataOptions getMetadataOptions()
    {
        return iReader.getMetadataOptions();
    }

    /* (non-Javadoc)
     * @see loci.formats.ReaderWrapper#setMetadataOptions(loci.formats.in.MetadataOptions)
     */
    @Override
    public void setMetadataOptions(MetadataOptions options)
    {
        iReader.setMetadataOptions(options);
    }

    /* (non-Javadoc)
     * @see loci.formats.ReaderWrapper#getSupportedMetadataLevels()
     */
    @Override
    public Set<MetadataLevel> getSupportedMetadataLevels()
    {
        return iReader.getSupportedMetadataLevels();
    }
}

class ReaderInvocationHandler implements InvocationHandler {

    private final IFormatReader reader;

    public ReaderInvocationHandler(IFormatReader reader) {
        reader.toString(); // NPE
        this.reader = reader;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if ("equals".equals(method.getName())) {
            throw new UnsupportedOperationException();
        } else if ("hashCode".equals(method.getName())) {
            return Integer.valueOf(reader.hashCode());
        } else if ("toString".equals(method.getName())) {
            return "ReaderHandler [" + reader + "]";
        } else {
            try {
                return method.invoke(proxy, args);
            } catch (Exception e) {
                throw new FormatException(e);
            }
        }
    }
}