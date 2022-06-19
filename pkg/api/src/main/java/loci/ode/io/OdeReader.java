package loci.ode.io;

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

import static ode.formats.model.UnitsFactory.convertLength;
import static ode.formats.model.UnitsFactory.convertTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.io.InputStreamReader;

import loci.common.Constants;
import loci.common.DateTools;
import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.units.UNITS;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.Timestamp;
import ode.RInt;
import ode.RString;
import ode.RTime;
import ode.ServerError;
import ode.api.IAdminPrx;
import ode.api.RawPixelsStorePrx;
import ode.api.RoiOptions;
import ode.api.RoiResult;
import ode.api.ServiceFactoryPrx;
import ode.model.Channel;
import ode.model.EllipseI;
import ode.model.Experimenter;
import ode.model.ExperimenterGroup;
import ode.model.ExperimenterGroupI;
import ode.model.Image;
import ode.model.Label;
import ode.model.Length;
import ode.model.LineI;
import ode.model.LogicalChannel;
import ode.model.Pixels;
import ode.model.PointI;
import ode.model.PolygonI;
import ode.model.PolylineI;
import ode.model.RectangleI;
import ode.model.MaskI;
import ode.model.Roi;
import ode.model.Shape;
import ode.model.Time;
import ode.sys.EventContext;
import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;

/**
 * Implementation of {@link loci.formats.IFormatReader}
 * for use in export from a Bhojpur ODE database.
 */
/**
 * @deprecated  As of release 1.0.0
 */
@Deprecated
public class OdeReader extends FormatReader {

    // -- Constants --

    public static final int DEFAULT_PORT = 4064;

    // -- Fields --

    private String server;
    private String username;
    private String password;
    private int thePort = DEFAULT_PORT;
    private String sessionID;
    private String group;
    private Long groupID = null;
    private boolean encrypted = true;

    /** 
     * The Bhojpur ODE client object, this is the entry point to the
     * Bhojpur ODE Server using a secure connection.
     */
    private ode.client secureClient;

    /** 
     * The client object, this is the entry point to the
     * Bhojpur ODE Server using non secure data transfer
     */
    private ode.client unsecureClient;

    private ServiceFactoryPrx serviceFactory;
    private Image img;
    private Pixels pix;

    private String formatId(String id) {
        String separator = "\n";
        StringBuilder buffer = new StringBuilder();
        Iterable<String> sc = () -> new Scanner(id).useDelimiter(separator);
        for (String line: sc) {
            if (line.startsWith("pass=")) {
                line = "pass=****";
            }
            buffer.append(line);
            buffer.append(separator);
        }
        return buffer.toString();
    }

    // -- Constructors --

    public OdeReader() {
        super("ODE", "*");
    }

    // -- OdeReader methods --

    public void setServer(String server) {
        this.server = server;
    }

    public void setPort(int port) {
        thePort = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public void setGroupName(String group) {
        this.group = group;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    // -- IFormatReader methods --

    @Override
    public boolean isThisType(String name, boolean open) {
        return name.startsWith("ode:");
    }

    @Override
    public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
            throws FormatException, IOException
    {
        FormatTools.assertId(currentId, true, 1);
        FormatTools.checkPlaneNumber(this, no);
        FormatTools.checkBufferSize(this, buf.length, w, h);

        final int[] zct = FormatTools.getZCTCoords(this, no);

        final byte[] plane;
        RawPixelsStorePrx store = null;
        try {
            store = serviceFactory.createRawPixelsStore();
            store.setPixelsId(pix.getId().getValue(), false);
            plane = store.getTile(zct[0], zct[1], zct[2], x, y, w, h);
        }
        catch (ServerError e) {
            throw new FormatException(e);
        } finally {
            // cannot use try-with-resources
            // RawPixelsStorePrx does not implement autocloseable
            if (store != null) {
                try {
                    store.close();
                } catch (Exception ex) {
                    throw new FormatException(ex);
                }
            }
        }

        System.arraycopy(plane, 0, buf, 0, plane.length);
        return buf;
    }

    @Override
    public void close(boolean fileOnly) throws IOException {
        super.close(fileOnly);
        if (!fileOnly) {
            if (secureClient != null) {
                secureClient.closeSession();
            }
            if (unsecureClient != null) {
                unsecureClient.closeSession();
            }
        }
    }

    @Override
    protected void initFile(String id) throws FormatException, IOException {
        String formattedId = formatId(id);

        super.initFile(formattedId);

        if (!id.startsWith("ode:")) {
            throw new IllegalArgumentException("Not a Bhojpur ODE id: " + formattedId);
        }

        // parse credentials from id string

        LOGGER.info("Parsing credentials");

        String address = server, user = username, pass = password;
        int port = thePort;
        long iid = -1;

        final String[] tokens = id.substring(6).split("\n");
        for (String token : tokens) {
            final int equals = token.indexOf("=");
            if (equals < 0) continue;
            final String key = token.substring(0, equals);
            final String val = token.substring(equals + 1);
            if (key.equals("server")) address = val;
            else if (key.equals("user")) user = val;
            else if (key.equals("pass")) pass = val;
            else if (key.equals("port")) {
                try {
                    port = Integer.parseInt(val);
                }
                catch (NumberFormatException exc) { }
            }
            else if (key.equals("session")) {
                sessionID = val;
            }
            else if (key.equals("groupName")) {
                group = val;
            }
            else if (key.equals("groupID")) {
                groupID = new Long(val);
            }
            else if (key.equals("iid")) {
                try {
                    iid = Long.parseLong(val);
                }
                catch (NumberFormatException exc) { }
            }
        }

        if (address == null) {
            throw new FormatException("Invalid server address");
        }
        if (user == null && sessionID == null) {
            throw new FormatException("Invalid username");
        }
        if (pass == null && sessionID == null) {
            throw new FormatException("Invalid password");
        }
        if (iid < 0) {
            throw new FormatException("Invalid image ID");
        }

        try {
            // authenticate with Bhojpur ODE server

            LOGGER.info("Logging in");

            secureClient = new ode.client(address, port);
            serviceFactory = null;
            if (user != null && pass != null) {
                serviceFactory = secureClient.createSession(user, pass);
            }
            else {
                serviceFactory = secureClient.createSession(sessionID, sessionID);
            }

            if (!encrypted) {
                unsecureClient = secureClient.createClient(false);
                serviceFactory = unsecureClient.getSession();
            }

            IAdminPrx iAdmin = serviceFactory.getAdminService();
            EventContext eventContext = iAdmin.getEventContext();

            if (group != null || groupID != null) {

                ExperimenterGroup defaultGroup =
                        iAdmin.getDefaultGroup(eventContext.userId);
                if (!defaultGroup.getName().getValue().equals(group) &&
                        !new Long(defaultGroup.getId().getValue()).equals(groupID))
                {
                    Experimenter exp = iAdmin.getExperimenter(eventContext.userId);
                    List<Long> groupList = iAdmin.getMemberOfGroupIds(exp);

                    Iterator<Long> i = groupList.iterator();

                    Long g = null;

                    boolean in = false;
                    while (i.hasNext()) {
                        g = i.next();
                        if (g.equals(groupID))
                        {
                            in = true;
                            groupID = g;
                            break;
                        }
                    }
                    if (in) {
                        iAdmin.setDefaultGroup(exp, iAdmin.getGroup(groupID));
                        serviceFactory.setSecurityContext(
                                new ExperimenterGroupI(groupID, false));
                    }
                }
            }

            img = (Image) serviceFactory.getContainerService()
                    .getImages("Image", Arrays.asList(iid), null).get(0);

            if (img == null) {
                throw new FormatException("Could not find Image with ID=" + iid +
                        " in group '" + group + "'.");
            }

            long pixelsId = img.getPixels(0).getId().getValue();

            pix = serviceFactory.getPixelsService().retrievePixDescription(pixelsId);

            final int sizeX = pix.getSizeX().getValue();
            final int sizeY = pix.getSizeY().getValue();
            final int sizeZ = pix.getSizeZ().getValue();
            final int sizeC = pix.getSizeC().getValue();
            final int sizeT = pix.getSizeT().getValue();
            final String pixelType = pix.getPixelsType().getValue().getValue();

            // populate metadata

            LOGGER.info("Populating metadata");

            CoreMetadata m = core.get(0);
            m.sizeX = sizeX;
            m.sizeY = sizeY;
            m.sizeZ = sizeZ;
            m.sizeC = sizeC;
            m.sizeT = sizeT;
            m.rgb = false;
            m.littleEndian = false;
            m.dimensionOrder = ode.model.enums.DimensionOrderXYZCT.value;
            m.imageCount = sizeZ * sizeC * sizeT;
            m.pixelType = FormatTools.pixelTypeFromString(pixelType);

            Length x = pix.getPhysicalSizeX();
            Length y = pix.getPhysicalSizeY();
            Length z = pix.getPhysicalSizeZ();
            Time t = pix.getTimeIncrement();

            ode.units.quantity.Time t2 = convertTime(t);
            ode.units.quantity.Length px = convertLength(x);
            ode.units.quantity.Length py = convertLength(y);
            ode.units.quantity.Length pz = convertLength(z);

            RString imageName = img.getName();
            String name = imageName == null ? null : imageName.getValue();

            if (name != null) {
                currentId = name;
            }
            else {
                currentId = "Image ID " + iid;
            }

            RString imgDescription = img.getDescription();
            String description =
                    imgDescription == null ? null : imgDescription.getValue();
            RTime date = img.getAcquisitionDate();

            MetadataStore store = getMetadataStore();

            MetadataTools.populatePixels(store, this);
            store.setImageName(name, 0);
            store.setImageDescription(description, 0);
            if (date != null) {
                store.setImageAcquisitionDate(new Timestamp(
                        DateTools.convertDate(date.getValue(), (int) DateTools.UNIX_EPOCH)),
                        0);
            }

            if (px != null && px.value().doubleValue() > 0) {
                store.setPixelsPhysicalSizeX(px, 0);
            }
            if (py != null && py.value().doubleValue() > 0) {
                store.setPixelsPhysicalSizeY(py, 0);
            }
            if (pz != null && pz.value().doubleValue() > 0) {
                store.setPixelsPhysicalSizeZ(pz, 0);
            }
            if (t2 != null) {
                store.setPixelsTimeIncrement(t2, 0);
            }

            List<Channel> channels = pix.copyChannels();
            for (int c=0; c<channels.size(); c++) {
                LogicalChannel channel = channels.get(c).getLogicalChannel();

                Length emWave = channel.getEmissionWave();
                Length exWave = channel.getExcitationWave();
                Length pinholeSize = channel.getPinHoleSize();
                RString cname = channel.getName();

                ode.units.quantity.Length emission = convertLength(emWave);
                ode.units.quantity.Length excitation = convertLength(exWave);
                String channelName = cname == null ? null : cname.getValue();
                ode.units.quantity.Length pinhole = convertLength(pinholeSize);

                if (channelName != null) {
                    store.setChannelName(channelName, 0, c);
                }
                if (pinholeSize != null) {
                    store.setChannelPinholeSize(pinhole, 0, c);
                }
                if (emission != null && emission.value().doubleValue() > 0) {
                    store.setChannelEmissionWavelength( emission, 0, c);
                }
                if (excitation != null && excitation.value().doubleValue() > 0) {
                    store.setChannelExcitationWavelength(excitation, 0, c);
                }
            }

            //Load ROIs to the img -->
            RoiOptions options = new RoiOptions();
            options.userId = ode.rtypes.rlong(iAdmin.getEventContext().userId);
            RoiResult r = serviceFactory.getRoiService().findByImage(img.getId().getValue(), new RoiOptions());
            if (r != null){
                List<Roi> rois = r.rois;

                int n = rois.size();
                if (n != 0){
                    saveOdeRoiToMetadataStore(rois,store);
                }
            }
        }
        catch (CannotCreateSessionException|PermissionDeniedException|ServerError e) {
            throw new FormatException(e);
        }
    }

    /** A simple command line tool for downloading images from Bhojpur ODE. */
    public static void main(String[] args) throws Exception {
        try (BufferedReader con = new BufferedReader(
                new InputStreamReader(System.in, Constants.ENCODING));
            OdeReader odeReader = new OdeReader()) {
            // parse Bhojpur ODE credentials
            System.out.print("Server? ");
            final String server = con.readLine();

            System.out.printf("Port [%d]? ", DEFAULT_PORT);
            final String portString = con.readLine();
            final int port = portString.equals("") ? DEFAULT_PORT :
                Integer.parseInt(portString);

            System.out.print("Username? ");
            final String user = con.readLine();

            System.out.print("Password? ");
            final String pass = new String(con.readLine());

            System.out.print("Group? ");
            final String group = con.readLine();

            System.out.print("Image ID? ");
            final int imageId = Integer.parseInt(con.readLine());
            System.out.print("\n\n");
            // construct the Bhojpur ODE reader
            final String id = "ode:iid=" + imageId;
            odeReader.setUsername(user);
            odeReader.setPassword(pass);
            odeReader.setServer(server);
            odeReader.setPort(port);
            odeReader.setGroupName(group);
            odeReader.setId(id);
        } catch (Exception e) {
            throw e;
        }
    }
    /** Converts ode.model.Roi to ode.xml.model.* and updates the MetadataStore */
    public static void saveOdeRoiToMetadataStore(List<ode.model.Roi> rois,
            MetadataStore store) {

        int n = rois.size();

        for (int thisROI=0  ; thisROI<n ; thisROI++){
            ode.model.Roi roi = rois.get(thisROI);
            int numShapes = roi.sizeOfShapes();
            int roiNum = thisROI;
            String roiID;
            roiID = MetadataTools.createLSID("ROI", roiNum, 0);
            for(int ns=0 ; ns<numShapes ; ns++){
                ode.model.Shape shape = roi.getShape(ns);
                int shapeNum= ns;

                if(shape instanceof LineI){
                    storeOdeLine(shape,store, roiNum, shapeNum);
                }
                else if(shape instanceof PointI){
                    storeOdePoint(shape,store, roiNum, shapeNum);
                }
                else if(shape instanceof EllipseI){
                    storeOdeEllipse(shape,store, roiNum, shapeNum);
                }
                else if(shape instanceof RectangleI){
                    storeOdeRect(shape,store, roiNum, shapeNum);
                }
                else if(shape instanceof PolygonI || shape instanceof PolylineI) {
                    storeOdePolygon(shape,store, roiNum, shapeNum);
                }
                else if (shape instanceof Label){
                    //add support for TextROI's
                    storeOdeLabel(shape,store, roiNum, shapeNum);
                }
                else if (shape instanceof MaskI){
                    storeOdeMask(shape,store, roiNum, shapeNum);
                }

            }
            if (roiID!=null){
                store.setROIID(roiID, roiNum);
                store. setImageROIRef(roiID, 0, roiNum);
            }
        }

    }

    /** Converts ode.model.Shape (ode.model.Label in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdeLabel(Shape shape, MetadataStore store,
            int roiNum, int shapeNum) {

        Label shape1 = (Label) shape;

        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setLabelID(polylineID, roiNum, shapeNum);
        if (shape1.getTextValue() != null){
            store.setLabelText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        store.setLabelX(shape1.getX().getValue(), roiNum, shapeNum);
        store.setLabelY(shape1.getY().getValue(), roiNum, shapeNum);
        
        store.setLabelTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setLabelTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setLabelTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getStrokeWidth() != null) {
            store.setLabelStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setLabelStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setLabelFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }

        if (shape1.getFontSize() != null){
            ode.units.quantity.Length labelSize = new ode.units.quantity.Length(shape1.getFontSize().getValue(), UNITS.PIXEL);
            store.setLabelFontSize(labelSize , roiNum, shapeNum);
        }

    }

    private static NonNegativeInteger unwrap(RInt r) {

        if (r == null) {
            return null;
        }
        return new NonNegativeInteger(r.getValue());
    }

    /** Converts ode.model.Shape (ode.model.RectangleI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdeRect(ode.model.Shape shape,
            MetadataStore store, int roiNum, int shapeNum) {

        RectangleI shape1 = (RectangleI) shape;

        double x1 = shape1.getX().getValue();
        double y1 = shape1.getY().getValue();
        double width = shape1.getWidth().getValue();
        double height = shape1.getHeight().getValue();

        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setRectangleID(polylineID, roiNum, shapeNum);
        store.setRectangleX(x1, roiNum, shapeNum);
        store.setRectangleY(y1, roiNum, shapeNum);
        store.setRectangleWidth(width, roiNum, shapeNum);
        store.setRectangleHeight(height, roiNum, shapeNum);
        store.setRectangleTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setRectangleTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setRectangleTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getTextValue() != null){
            store.setRectangleText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        if (shape1.getStrokeWidth() != null) {
            store.setRectangleStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setRectangleStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setRectangleFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }

    }
    /** Converts ode.model.Shape (ode.model.EllipseI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdeEllipse(ode.model.Shape shape,
            MetadataStore store, int roiNum, int shapeNum) {

        EllipseI shape1 = (EllipseI) shape;

        double x1 = shape1.getX().getValue();
        double y1 = shape1.getY().getValue();
        double width = shape1.getRadiusX().getValue();
        double height = shape1.getRadiusY().getValue();

        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setEllipseID(polylineID, roiNum, shapeNum);
        store.setEllipseX(x1, roiNum, shapeNum);
        store.setEllipseY(y1, roiNum, shapeNum);
        store.setEllipseRadiusX(width, roiNum, shapeNum);
        store.setEllipseRadiusY(height, roiNum, shapeNum);
        store.setEllipseTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setEllipseTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setEllipseTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getTextValue() != null){
            store.setEllipseText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        if (shape1.getStrokeWidth() != null) {
            store.setEllipseStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setEllipseStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setEllipseFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }

    }
    /** Converts ode.model.Shape (ode.model.PointI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdePoint(ode.model.Shape shape,
            MetadataStore store, int roiNum, int shapeNum) {

        PointI shape1 = (PointI) shape;
        double ox1 = shape1.getX().getValue();
        double oy1 = shape1.getY().getValue();

        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setPointID(polylineID, roiNum, shapeNum);
        store.setPointX(ox1, roiNum, shapeNum);
        store.setPointY(oy1, roiNum, shapeNum);
        store.setPointTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setPointTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setPointTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getTextValue() != null){
            store.setPointText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        if (shape1.getStrokeWidth() != null) {
            store.setPointStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setPointStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setPointFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }

    }
    /** Converts ode.model.Shape (ode.model.LineI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdeLine(ode.model.Shape shape,
            MetadataStore store, int roiNum, int shapeNum) {

        LineI shape1 = (LineI) shape;
        double x1 = shape1.getX1().getValue();
        double y1 = shape1.getY1().getValue();
        double x2 = shape1.getX2().getValue();
        double y2 = shape1.getY2().getValue();

        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setLineID(polylineID, roiNum, shapeNum);

        store.setLineX1(new Double(x1), roiNum, shapeNum);
        store.setLineX2(new Double(x2), roiNum, shapeNum);
        store.setLineY1(new Double(y1), roiNum, shapeNum);
        store.setLineY2(new Double(y2), roiNum, shapeNum);
        store.setLineTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setLineTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setLineTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getTextValue() != null){
            store.setLineText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        if (shape1.getStrokeWidth() != null) {
            store.setLineStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setLineStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setLineFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }


    }
    /** Converts ode.model.Shape (ode.model.PolygonI/ode.model.PolylineI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdePolygon(ode.model.Shape shape, MetadataStore store,
            int roiNum, int shapeNum){

        String points=null;
        String polylineID = MetadataTools.createLSID("Shape", roiNum, shapeNum);

        if(shape instanceof PolygonI){
            PolygonI shape1 = (PolygonI) shape;
            points = shape1.getPoints().getValue();
            String points2d = parsePoints(convertPoints(points, "points"));

            store.setPolygonID(polylineID, roiNum, shapeNum);
            store.setPolygonPoints(points2d, roiNum, shapeNum);

            if (shape1.getTextValue() != null){
                store.setPolygonText(shape1.getTextValue().getValue(), roiNum, shapeNum);
            }
            if (shape1.getStrokeWidth() != null) {
                store.setPolygonStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
            }
            if (shape1.getStrokeColor() != null){
                store.setPolygonStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
            }
            if (shape1.getFillColor() != null){
                store.setPolygonFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
            }

            store.setPolygonTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
            store.setPolygonTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
            store.setPolygonTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);
        }else{
            PolylineI shape1 = (PolylineI) shape;
            points = shape1.getPoints().getValue();
            String points2d = parsePoints(convertPoints(points, "points"));

            store.setPolylineID(polylineID, roiNum, shapeNum);
            store.setPolylinePoints(points2d, roiNum, shapeNum);

            if (shape1.getTextValue() != null){
                store.setPolylineText(shape1.getTextValue().getValue(), roiNum, shapeNum);
            }
            if (shape1.getStrokeWidth() != null) {
                store.setPolylineStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
            }
            if (shape1.getStrokeColor() != null){
                store.setPolylineStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
            }
            if (shape1.getFillColor() != null){
                store.setPolylineFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
            }
            store.setPolylineTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
            store.setPolylineTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
            store.setPolylineTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);
        }

    }
    /** Converts ode.model.Shape (ode.model.MaskI in this case) to ode.xml.model.* and updates the MetadataStore */
    private static void storeOdeMask(ode.model.Shape shape,
            MetadataStore store, int roiNum, int shapeNum) {

        MaskI shape1 = (MaskI) shape;
        double x1 = shape1.getX().getValue();
        double y1 = shape1.getY().getValue();
        double width = shape1.getWidth().getValue();
        double height = shape1.getHeight().getValue();

        String maskID = MetadataTools.createLSID("Shape", roiNum, shapeNum);
        store.setMaskID(maskID, roiNum, shapeNum);
        store.setMaskX(x1, roiNum, shapeNum);
        store.setMaskY(y1, roiNum, shapeNum);
        store.setMaskWidth(width, roiNum, shapeNum);
        store.setMaskHeight(height, roiNum, shapeNum);
        store.setMaskTheC(unwrap(shape1.getTheC()), roiNum, shapeNum);
        store.setMaskTheZ(unwrap(shape1.getTheZ()), roiNum, shapeNum);
        store.setMaskTheT(unwrap(shape1.getTheT()), roiNum, shapeNum);

        if (shape1.getTextValue() != null){
            store.setMaskText(shape1.getTextValue().getValue(), roiNum, shapeNum);
        }
        if (shape1.getStrokeWidth() != null) {
            store.setMaskStrokeWidth(new ode.units.quantity.Length(shape1.getStrokeWidth().getValue(), UNITS.PIXEL), roiNum, shapeNum);
        }
        if (shape1.getStrokeColor() != null){
            store.setMaskStrokeColor(new ode.xml.model.primitives.Color(shape1.getStrokeColor().getValue()), roiNum, shapeNum);
        }
        if (shape1.getFillColor() != null){
            store.setMaskFillColor(new ode.xml.model.primitives.Color(shape1.getFillColor().getValue()), roiNum, shapeNum);
        }

    }
    /** Parse Points String obtained from convertPoints and extract Coordinates as String*/
    protected static String parsePoints(String str)
    {
        String points = null;

        if (str == null) return points;

        List<Float> x = new ArrayList<Float>();
        List<Float> y = new ArrayList<Float>();
        StringTokenizer tt = new StringTokenizer(str, " ");
        int numTokens = tt.countTokens();
        StringTokenizer t;
        int total;
        for (int i = 0; i < numTokens; i++) {
            t = new StringTokenizer(tt.nextToken(), ",");
            total = t.countTokens()/2;
            for (int j = 0; j < total; j++) {
                x.add(new Float(t.nextToken()));
                y.add(new Float(t.nextToken()));
            }
        }

        for (int i=0 ; i<x.size() ; i++){

            if(i==0){
                points = (x.get(i) + "," + y.get(i));
            }else{
                points= (points + " " + x.get(i) + "," + y.get(i));
            }
        }

        return points.toString();
    }
    /** Split Points coordinates and obtain information for the first plane alone */
    private static String convertPoints(String pts, String type)
    {
        if (pts.length() == 0) return "";
        if (!pts.contains(type)) {//data inserted following the schema
            return pts;
        }
        String exp = type+'[';
        int typeStr = pts.indexOf(exp, 0);
        int start = pts.indexOf('[', typeStr);
        int end = pts.indexOf(']', start);
        return pts.substring(start+1,end);
    }

}