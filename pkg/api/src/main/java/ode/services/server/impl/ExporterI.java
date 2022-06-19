package ode.services.server.impl;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.xml.XMLTools;
import loci.formats.FormatTools;
import loci.formats.IFormatWriter;
import loci.formats.meta.IMetadata;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.out.ODETiffWriter;
import loci.formats.services.ODEXMLService;
import loci.formats.tiff.IFD;
import ode.api.RawPixelsStore;
import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.io.nio.PixelsService;
import ode.io.nio.RomioPixelBuffer;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServerOnly;
import ode.services.server.util.ServiceFactoryAware;
import ode.services.db.DatabaseIdentity;
import ode.services.formats.OdeReader;
import ode.services.util.Executor;
import ode.system.ServiceFactory;
import ode.xml.model.MetadataOnly;
import ode.xml.model.ODE;
import ode.ServerError;
import ode.api.AMD_Exporter_addImage;
import ode.api.AMD_Exporter_generateTiff;
import ode.api.AMD_Exporter_generateXml;
import ode.api.AMD_Exporter_read;
import ode.api._ExporterOperations;
import ode.model.Image;
import ode.model.ImageI;
import ode.model.Pixels;
import ode.util.IceMapper;
import ode.util.TempFileManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Ice.Current;

/**
 * Implementation of the Exporter service. This class uses a simple state
 * machine.
 * 
 * <pre>
 *  START -&gt; waiting -&gt; config -&gt; output -&gt; waiting -&gt; config ...
 * </pre>
 */
public class ExporterI extends AbstractCloseableAmdServant implements
        _ExporterOperations, ServiceFactoryAware, ServerOnly {

    private final static Logger log = LoggerFactory.getLogger(ExporterI.class);

    private final static int MAX_SIZE = 1024 * 1024;

    /**
     * The size above which a big tiff should be written as opposed to
     * a normal tiff. This value is checked against the data size PLUS
     * various metadata sizes.
     *
     * @see #getMetadataBytes(OdeReader)
     * @see #getDataBytes(OdeReader)
     */
    private final static long BIG_TIFF_SIZE = 2L * Integer.MAX_VALUE;


    /**
     * Utility enum for asserting the state of Exporter instances.
     */
    private enum State {
        config, output;
        static State check(ExporterI self) {

            if (self.file != null && self.retrieve != null) {
                throw new InternalException("Doing 2 things at once");
            }

            if (self.retrieve == null) {
                return output;
            }

            return config;

        }
    }

    private/* final */ServiceFactoryI factory;

    private volatile OdeMetadata retrieve;

    /**
     * Reference to the temporary file which is currently being output. If null,
     * then no generate method has been called.
     */
    private volatile File file;

    /**
     * Encapsulates the logic for creating new LSIDs and comparing existing ones
     * to the internal value for this DB.
     */
    private final DatabaseIdentity databaseIdentity;

    /** LOCI ODE-XML service for working with ODE-XML. */
    private final ODEXMLService service;

    /** Access to information about big images to prevent exporting large
     * files since there is currently no generally readable tiff support
     * for our tiles.
     */
    private final PixelsService pixelsService;

    public ExporterI(ServerExecutor be, DatabaseIdentity databaseIdentity,
            PixelsService pixelsService)
        throws DependencyException {
        super(null, be);
        this.databaseIdentity = databaseIdentity;
        this.pixelsService = pixelsService;
        retrieve = new OdeMetadata(databaseIdentity);
        loci.common.services.ServiceFactory sf =
            new loci.common.services.ServiceFactory();
        service = sf.getInstance(ODEXMLService.class);
    }

    public void setServiceFactory(ServiceFactoryI sf) throws ServerError {
        this.factory = sf;
    }

    // Interface methods
    // =========================================================================

    public void addImage_async(AMD_Exporter_addImage __cb, final long id,
            Current __current) throws ServerError {

        State state = State.check(this);
        ServerError se = assertConfig(state);
        if (se != null) {
            __cb.ice_exception(se);
        }

        retrieve.addImage(new ImageI(id, false));
        __cb.ice_response();
        return;

    }

    /**
     * Generate XML and return the length
     */
    public void generateXml_async(AMD_Exporter_generateXml __cb,
            Current __current) throws ServerError {

        State state = State.check(this);
        ServerError se = assertConfig(state);
        if (se != null) {
            __cb.ice_exception(se);
        }
        // sets retrieve, then file, then unsets retrieve
        do_xml(__cb);
        return;
    }

    /**
     * 
     */
    public void generateTiff_async(AMD_Exporter_generateTiff __cb,
            Current __current) throws ServerError {

        State state = State.check(this);
        ServerError se = assertConfig(state);
        if (se != null) {
            __cb.ice_exception(se);
        }
        // sets retrieve, then file, then unsets retrieve
        do_tiff(__cb);
        return;
    }

    public void read_async(AMD_Exporter_read __cb, long pos, int size,
            Current __current) throws ServerError {

        ode.ApiUsageException aue;
        State state = State.check(this);
        switch (state) {
        case config:
            aue = new ode.ApiUsageException(null,
                    null, "Call a generate method first");
            __cb.ice_exception(aue);
            return;
        case output:
            try {
                __cb.ice_response(read(pos, size));
            } catch (Exception e) {
                if (e instanceof ServerError) {
                    __cb.ice_exception(e);
                } else {
                    ode.InternalException ie = new ode.InternalException(null,
                            null, "Error during read");
                    IceMapper.fillServerError(ie, e);
                    __cb.ice_exception(ie);
                }
            }
            return;
        default:
            throw new InternalException("Unknown state: " + state);
        }
    }
    
    // State methods
    // =========================================================================

    /**
     * Transition from waiting to config
     */
    private ServerError assertConfig(State state) {
        switch (state) {
        case config:
            return null;
        case output:
            return new ode.ApiUsageException(null, null,
                    "Cannot configure during output");
        default:
            return new ode.InternalException(null, null, "Unknown state: "
                    + state);
        }
    }

    /**
     * Transition from waiting to config
     */
    private void startConfig() {
        if (file != null) {
            file.delete();
            file = null;
        }
        
    }

    /**
     * Transitions from config to output.
     */
    private void do_xml(final AMD_Exporter_generateXml __cb) {
        try {
            factory.getExecutor().execute(factory.getPrincipal(),
                    new Executor.SimpleWork<Void>(this, "generateXml") {
                        @Transactional(readOnly = true)
                        public Void doWork(Session session, ServiceFactory sf) {
                            retrieve.initialize(session);
                            IMetadata xmlMetadata = null;
                            try {
                                 xmlMetadata = convertXml(retrieve);
                            } catch (ServiceException e) {
                                log.error(e.toString()); // slf4j migration: toString()
                                return null;
                            }
                            if (xmlMetadata != null) {
                                Object root = xmlMetadata.getRoot();
                                if (root instanceof ODE) {
                                    ODE node = (ODE) root;
                                    //add metadata only so we have valid xml.
                                    List<ode.xml.model.Image> images = node.copyImageList();
                                    for (ode.xml.model.Image img : images) {
                                        img.getPixels().setMetadataOnly(new MetadataOnly());
                                    }
                                    try {
                                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                        DocumentBuilder parser = factory.newDocumentBuilder();
                                        Document document = parser.newDocument();
                                        Element element = node.asXMLElement(document);
                                        document.appendChild(element);

                                        file = TempFileManager.create_path(
                                                "__ode_export__", ".ode.xml");
                                        file.deleteOnExit();
                                        FileOutputStream fos = new FileOutputStream(
                                                file);
                                        XMLTools.writeXML(fos, document);
                                        fos.close();
                                        retrieve = null;
                                        __cb.ice_response(file.length());
                                        return null; // ONLY VALID EXIT

                                    } catch (IOException ioe) {
                                        log.error(ioe.toString()); // slf4j migration: toString()

                                    } catch (TransformerException e) {
                                        log.error(e.toString()); // slf4j migration: toString()

                                    } catch (ParserConfigurationException e) {
                                        log.error(e.toString()); // slf4j migration: toString()
                                    }
                                }
                            }
                            return null;
                        }
                    });
        } catch (Exception e) {
            IceMapper mapper = new IceMapper();
            Ice.UserException ue = mapper.handleException(e, factory.getExecutor().getContext());
            __cb.ice_exception(ue);
        }
    }

    /**
     * Transitions from config to output.
     */
    private void do_tiff(final AMD_Exporter_generateTiff __cb) {
        try {
            factory.executor.execute(factory.principal,
                    new Executor.SimpleWork<Void>(this, "generateTiff") {
                        @Transactional(readOnly = true)
                        public Void doWork(Session session, ServiceFactory sf) {
                            retrieve.initialize(session);

                            int num = retrieve.sizeImages();
                            if (num != 1) {
                                ode.ApiUsageException a = new ode.ApiUsageException(
                                        null, null,
                                        "Only one image supported for TIFF, not "+num);
                                __cb.ice_exception(a);
                                return null;
                            }

                            RawPixelsStore raw = null;
                            OdeReader reader = null;
                            ODETiffWriter writer = null;
                            try {

                                Image image = retrieve.getImage(0);
                                Pixels pix = image.getPixels(0);
                                if (requiresPyramid(sf, pix.getId().getValue())) {
                                    long id = image.getId().getValue();
                                    int x = pix.getSizeX().getValue();
                                    int y = pix.getSizeY().getValue();
                                    throw new ode.ApiUsageException(
                                            null, null, String.format(
                                            "Image:%s is too large for export (sizeX=%s, sizeY=%s)",
                                            id, x, y));
                                }

                                file = TempFileManager.create_path("__ode_export__",
                                        ".ode.tiff");

                                raw = sf.createRawPixelsStore();
                                raw.setPixelsId(pix.getId().getValue(), true);

                                reader = new OdeReader(raw, pix);
                                reader.setId("ODE");

                                writer = new ODETiffWriter();
                                writer.setMetadataRetrieve(retrieve);
                                writer.setWriteSequentially(true); //
                                long mSize = getMetadataBytes(reader);
                                long dSize = getDataBytes(reader);
                                final boolean bigtiff =
                                    ( ( mSize + dSize ) > BIG_TIFF_SIZE );
                                if (bigtiff) {
                                    writer.setBigTiff(true);
                                }
                                writer.setId(file.getAbsolutePath());

                                int planeCount = reader.planes;
                                int planeSize = RomioPixelBuffer.safeLongToInteger(
                                        raw.getPlaneSize());
                                log.info(String.format(
                                            "Using big TIFF? %s mSize=%d " +
                                            "dSize=%d planeCount=%d " +
                                            "planeSize=%d",
                                            bigtiff, mSize, dSize,
                                            planeCount, planeSize));
                                byte[] plane = new byte[planeSize];
                                for (int i = 0; i < planeCount; i++) {
                                    int[] zct = FormatTools.getZCTCoords(
                                        retrieve.getPixelsDimensionOrder(0).getValue(),
                                        reader.getSizeZ(), reader.getSizeC(), reader.getSizeT(),
                                        planeCount, i);
                                    int readerIndex = reader.getIndex(zct[0], zct[1], zct[2]);
                                    reader.openBytes(readerIndex, plane);


                                    IFD ifd = new IFD();
                                    ifd.put(IFD.TILE_WIDTH, 128);
                                    ifd.put(IFD.TILE_LENGTH, 128);

                                    writer.saveBytes(i, plane, ifd);
                                }
                                retrieve = null;

                                try {
                                    writer.close();
                                } finally {
                                    // Nulling to prevent another exception
                                    writer = null;
                                }

                                    __cb.ice_response(file.length());
                                } catch (Exception e) {
                                    ode.InternalException ie = new ode.InternalException(
                                            null, null,
                                            "Error during TIFF generation");
                                    IceMapper.fillServerError(ie, e);
                                    __cb.ice_exception(ie);
                                } finally {
                                    cleanup(raw, reader, writer);
                                }

                            return null; // see calls to __cb above
                        }

                        private void cleanup(RawPixelsStore raw,
                                OdeReader reader, IFormatWriter writer) {
                            try {
                                if (raw != null) {
                                    raw.close();
                                }
                            } catch (Exception e) {
                                log.error("Error closing pix", e);
                            }
                            try {
                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (Exception e) {
                                log.error("Error closing reader", e);
                            }
                            try {
                                if (writer != null) {
                                    writer.close();
                                }
                            } catch (Exception e) {
                                log.error("Error closing writer", e);
                            }
                        }
                    });
        } catch (Exception e) {
            IceMapper mapper = new IceMapper();
            Ice.UserException ue = mapper.handleException(e, factory.getExecutor().getContext());
            __cb.ice_exception(ue);
        }
    }

    /**
     * Read size bytes, and transition to "waiting" If any exception is thrown,
     * the offset for the current file will not be updated.
     */
    private byte[] read(long pos, int size) throws ServerError {
        if (size > MAX_SIZE) {
            throw new ApiUsageException("Max read size is: " + MAX_SIZE);
        }

        byte[] buf = new byte[size];

        RandomAccessFile ra = null;
        try {
            ra = new RandomAccessFile(file, "r");

            long l = ra.length();
            if (pos + size > l) {
                size  = (int) (l - pos);
            }

            ra.seek(pos);
            int read = ra.read(buf);

            // Handle end of file
            if (read < 0) {
                buf = new byte[0];
            } else if (read < size) {
                byte[] newBuf = new byte[read];
                System.arraycopy(buf, 0, newBuf, 0, read);
                buf = newBuf;
            }

        } catch (IOException io) {
            throw new RuntimeException(io);
        } finally {

            if (ra != null) {
                try {
                    ra.close();
                } catch (IOException e) {
                    log.warn("IOException on file close");
                }
            }

        }

        return buf;
    }

    // XML Generation (public for testing)
    // =========================================================================

    public IMetadata convertXml(MetadataRetrieve retrieve)
        throws ServiceException {
        IMetadata xmlMeta = service.createODEXMLMetadata();
        xmlMeta.createRoot();
        service.convertMetadata(retrieve, xmlMeta);
        return xmlMeta;
    }

    public String generateXml(MetadataRetrieve retrieve)
        throws ServiceException {
        IMetadata xmlMeta = convertXml(retrieve);
        return service.getODEXML(xmlMeta);
    }

    // Stateful interface methods
    // =========================================================================

    @Override
    protected void preClose(Ice.Current current) {
        retrieve = null;
        if (file != null) {
            file.delete();
            file = null;
        }
    }

    @Override
    protected void postClose(Current current) {
        // no-op
    }

    // Misc. helpers.
    // =========================================================================

    private long getMetadataBytes(OdeReader reader)
            throws DependencyException, ServiceException {

        String xml = service.getODEXML(retrieve);

        long xmlbytes;
        try {
            xmlbytes = xml.getBytes("UTF8").length;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to convert to UTF-8", e);
        }
        long planebytes = reader.planes * 512;
        return planebytes + xmlbytes;
    }

    private long getDataBytes(OdeReader reader) {
        return (long) reader.planes * reader.sizeX * reader.sizeY *
            FormatTools.getBytesPerPixel(reader.getPixelType());
    }

    private boolean requiresPyramid(ServiceFactory sf, long id) {
        ode.model.core.Pixels _p =
            sf.getQueryService().get(ode.model.core.Pixels.class, id);
        return pixelsService.requiresPixelsPyramid(_p);
    }


}