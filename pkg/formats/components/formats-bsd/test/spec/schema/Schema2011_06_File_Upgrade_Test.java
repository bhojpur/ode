package spec.schema;

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

import java.io.InputStream;

import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;

import loci.common.DataTools;
import loci.common.services.ServiceFactory;
import loci.common.xml.XMLTools;
import loci.formats.services.ODEXMLService;

import ode.xml.model.BinData;
import ode.xml.model.Channel;
import ode.xml.model.Dataset;
import ode.xml.model.Experimenter;
import ode.xml.model.ExperimenterGroup;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.Label;
import ode.xml.model.Line;
import ode.xml.model.ODE;
import ode.xml.model.Objective;
import ode.xml.model.Pixels;
import ode.xml.model.Plate;
import ode.xml.model.Point;
import ode.xml.model.Polygon;
import ode.xml.model.Polyline;
import ode.xml.model.Project;
import ode.xml.model.ROI;
import ode.xml.model.Reagent;
import ode.xml.model.Rectangle;
import ode.xml.model.Screen;
import ode.xml.model.Shape;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.Union;
import ode.xml.model.Well;
import ode.xml.model.PlateAcquisition;
import ode.xml.model.WellSample;
import ode.xml.model.XMLAnnotation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * import the schema resolver so I can use it to locate
 * transforms in the specification jar
 */
import ode.specification.SchemaResolver;

/**
 * import the reference strings for the associated sample file
 */
import spec.schema.samples.Upgrade2011_06.ref;

/**
 * Collections of tests.
 * Checks if the upgrade from 2011-06 schema to 2012-06 schema works for
 * the file 2011-06/6x4y1z1t3c8b-swatch-upgrade.ode
 */

@Test(groups = { "all" })
public class Schema2011_06_File_Upgrade_Test {
    private static final Templates UPDATE_201106 =
        XMLTools.getStylesheet("/transforms/2011-06-to-2012-06.xsl",
                SchemaResolver.class);

    private static final String MESSAGE_REMOVED_PATH = "Removed Path";
    private ODE ode;

    private Image image0;
    private Dataset dataset0;
    private Project project0;
    private Plate plate0;
    private Plate plate1;
    private Well well0;
    private Instrument instrument0;
    private Pixels pixels0;
    private StructuredAnnotations annotations;
    private WellSample wellSample0;
    private PlateAcquisition plateAcquisition0;
    private Screen screen0;
    private Screen screen1;
    private Screen screen2;
    private Screen screen3;
    private Experimenter experimenter0;
    private Experimenter experimenter1;
    private Experimenter experimenter2;
    private Experimenter experimenter3;
    private Experimenter experimenter4;
    private Experimenter experimenter5;
    private Experimenter experimenter6;
    private ExperimenterGroup experimenterGroup0;
    private ExperimenterGroup experimenterGroup1;
    private ExperimenterGroup experimenterGroup2;
    private ExperimenterGroup experimenterGroup3;
    private ExperimenterGroup experimenterGroup4;
    private Objective objective0;
    private Channel channel0;
    private Channel channel1;
    private Channel channel2;
    private BinData bindata0;
    private BinData bindata1;
    private BinData bindata2;
    private XMLAnnotation xmlAnnotation0;
    private XMLAnnotation xmlAnnotation1;
    private ROI roi0;
    private ROI roi1;
    private ROI roi2;
    private ROI roi3;
    private ROI roi4;
    private Union union0;
    private Union union1;
    private Union union2;
    private Union union3;
    private Union union4;

    private Shape shape0;
    private Shape shape1;
    private Shape shape2;
    private Shape shape3;
    private Shape shape4;
    private Shape shape5;
    private Shape shape6;
    private Shape shape7;
    private Shape shape8;
    private Shape shape9;
    /* Shape 10 is removed by the upgrade */
    private Shape shape11;
    private Shape shape12;
    private Shape shape13;
    private Reagent reagent0;
    private Reagent reagent1;
    private Reagent reagent2;
    private Reagent reagent3;

    @BeforeClass
    public void setUp() throws Exception {
        InputStream source = this.getClass().getResourceAsStream(ref.FILE_LOCATION);
        byte[] b = new byte[source.available()];
        source.read(b);
        source.close();
        String  xml = new String(b);
        ServiceFactory sf = new ServiceFactory();
        ODEXMLService service = sf.getInstance(ODEXMLService.class);
        ode = (ODE) service.createODEXMLRoot(xml);
    }

    @Test
    public void testOdeNode() {
        Assert.assertNotNull(ode);
        Assert.assertEquals(1, ode.sizeOfDatasetList());
        Assert.assertEquals(5, ode.sizeOfExperimenterGroupList());
        Assert.assertEquals(7, ode.sizeOfExperimenterList());
        Assert.assertEquals(1, ode.sizeOfImageList());
        Assert.assertEquals(1, ode.sizeOfInstrumentList());
        Assert.assertEquals(2, ode.sizeOfPlateList());
        Assert.assertEquals(1, ode.sizeOfProjectList());
        Assert.assertEquals(5, ode.sizeOfROIList());
        Assert.assertEquals(4, ode.sizeOfScreenList());
        Assert.assertNull(ode.getCreator());
        Assert.assertNull(ode.getUUID());
        Assert.assertNotNull(ode.getStructuredAnnotations());
    }

    @Test (groups = {"11-06-u-proj"}, dependsOnMethods = {"testOdeNode"})
    public void testDataset0() {
        Assert.assertNotNull(ode);
        dataset0 = ode.getDataset(0);
        Assert.assertNotNull(dataset0);
    }

    @Test (groups = {"11-06-u-proj"}, dependsOnMethods = {"testOdeNode"})
    public void testProject0() {
        Assert.assertNotNull(ode);
        project0 = ode.getProject(0);
        Assert.assertNotNull(project0);
     }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testPlate0() {
        Assert.assertNotNull(ode);
        plate0 = ode.getPlate(0);
        Assert.assertNotNull(plate0);
        Assert.assertEquals(ref.Plate0Description, plate0.getDescription());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testPlate0"})
    public void testPlateAcquisition0Description() {
        Assert.assertNotNull(plate0);
        Assert.assertEquals(1, plate0.sizeOfPlateAcquisitionList());
        plateAcquisition0 = plate0.getPlateAcquisition(0);
        Assert.assertNotNull(plateAcquisition0);
        Assert.assertEquals(ref.Plate0PlateAcquisition0Description, plateAcquisition0.getDescription());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testPlate0"})
    public void testPlate0Well0() {
        Assert.assertNotNull(plate0);
        Assert.assertEquals(1, plate0.sizeOfWellList());
        well0 = plate0.getWell(0);
        Assert.assertNotNull(well0);
        Assert.assertEquals(ref.Plate0Well0Column, well0.getColumn());
        Assert.assertEquals(ref.Plate0Well0Row, well0.getRow());
        Assert.assertEquals(ref.Plate0Well0Color, well0.getColor());
        Assert.assertEquals(ref.Plate0Well0Status, well0.getType());
    }
    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testPlate0Well0"})
    public void testPlate0WellSample0() {
        Assert.assertNotNull(well0);
        Assert.assertEquals(1, well0.sizeOfWellSampleList());
        wellSample0 = well0.getWellSample(0);
        Assert.assertNotNull(wellSample0);
        Assert.assertEquals(ref.Plate0Well0WellSample0Index, wellSample0.getIndex());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testPlate1Description() {
        Assert.assertNotNull(ode);
        plate1 = ode.getPlate(1);
        Assert.assertNotNull(plate1);
        Assert.assertNull(plate1.getDescription());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testScreen0() {
        Assert.assertNotNull(ode);
        screen0 = ode.getScreen(0);
        Assert.assertNotNull(screen0);
        Assert.assertEquals(ref.Screen0Name, screen0.getName());
        Assert.assertEquals(ref.Screen0Description, screen0.getDescription());
        Assert.assertEquals(ref.Screen0ProtocolDescription, screen0.getProtocolDescription());
        Assert.assertEquals(ref.Screen0ProtocolIdentifier, screen0.getProtocolIdentifier());
        Assert.assertEquals(ref.Screen0ReagentSetDescription, screen0.getReagentSetDescription());
        Assert.assertEquals(ref.Screen0ReagentSetIdentifier, screen0.getReagentSetIdentifier());
        Assert.assertEquals(ref.Screen0Type, screen0.getType());
        Assert.assertEquals(1, screen0.sizeOfReagentList());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testScreen1() {
        Assert.assertNotNull(ode);
        screen1 = ode.getScreen(1);
        Assert.assertNotNull(screen1);
        Assert.assertEquals(ref.Screen1Name, screen1.getName());
        Assert.assertEquals(ref.Screen1Description, screen1.getDescription());
        Assert.assertEquals(ref.Screen1ProtocolDescription, screen1.getProtocolDescription());
        Assert.assertEquals(ref.Screen1ProtocolIdentifier, screen1.getProtocolIdentifier());
        Assert.assertEquals(ref.Screen1ReagentSetDescription, screen1.getReagentSetDescription());
        Assert.assertEquals(ref.Screen1ReagentSetIdentifier, screen1.getReagentSetIdentifier());
        Assert.assertEquals(ref.Screen1Type, screen1.getType());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testScreen2() {
        Assert.assertNotNull(ode);
        screen2 = ode.getScreen(2);
        Assert.assertNotNull(screen2);
        Assert.assertEquals(ref.Screen2Name, screen2.getName());
        Assert.assertEquals(ref.Screen2Description, screen2.getDescription());
        Assert.assertEquals(ref.Screen2ProtocolDescription, screen2.getProtocolDescription());
        Assert.assertEquals(ref.Screen2ProtocolIdentifier, screen2.getProtocolIdentifier());
        Assert.assertEquals(ref.Screen2ReagentSetDescription, screen2.getReagentSetDescription());
        Assert.assertEquals(ref.Screen2ReagentSetIdentifier, screen2.getReagentSetIdentifier());
        Assert.assertEquals(ref.Screen2Type, screen2.getType());
    }
    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testOdeNode"})
    public void testScreen3() {
        Assert.assertNotNull(ode);
        screen3 = ode.getScreen(3);
        Assert.assertNotNull(screen3);
        Assert.assertEquals(ref.Screen3Name, screen3.getName());
        Assert.assertEquals(ref.Screen3Description, screen3.getDescription());
        Assert.assertEquals(ref.Screen3ProtocolDescription, screen3.getProtocolDescription());
        Assert.assertEquals(ref.Screen3ProtocolIdentifier, screen3.getProtocolIdentifier());
        Assert.assertEquals(ref.Screen3ReagentSetDescription, screen3.getReagentSetDescription());
        Assert.assertEquals(ref.Screen3ReagentSetIdentifier, screen3.getReagentSetIdentifier());
        Assert.assertEquals(ref.Screen3Type, screen3.getType());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testScreen0"})
    public void testReagent0() {
        Assert.assertNotNull(screen0);
        reagent0 = screen0.getReagent(0);
        Assert.assertNotNull(reagent0);
        Assert.assertNull(reagent0.getName());
        Assert.assertNull(reagent0.getReagentIdentifier());
        Assert.assertNull(reagent0.getName());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testScreen1"})
    public void testReagent1() {
        Assert.assertNotNull(screen1);
        reagent1 = screen1.getReagent(0);
        Assert.assertNotNull(reagent1);
        Assert.assertNull(reagent1.getName());
        Assert.assertNull(reagent1.getReagentIdentifier());
        Assert.assertNull(reagent1.getName());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testScreen2"})
    public void testReagent2() {
        Assert.assertNotNull(screen2);
        reagent2 = screen2.getReagent(0);
        Assert.assertNotNull(reagent2);
        Assert.assertNull(reagent2.getName());
        Assert.assertNull(reagent2.getReagentIdentifier());
        Assert.assertNull(reagent2.getName());
    }

    @Test (groups = {"11-06-u-spw"}, dependsOnMethods = {"testScreen3"})
    public void testReagent3() {
        Assert.assertNotNull(screen3);
        reagent3 = screen3.getReagent(0);
        Assert.assertNotNull(reagent3);
        Assert.assertNull(reagent3.getName());
        Assert.assertNull(reagent3.getReagentIdentifier());
        Assert.assertNull(reagent3.getName());
    }


    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter0() {
        Assert.assertNotNull(ode);
        experimenter0 = ode.getExperimenter(0);
        Assert.assertNotNull(experimenter0);
        Assert.assertNull(experimenter0.getFirstName());
        Assert.assertNull(experimenter0.getMiddleName());
        Assert.assertNull(experimenter0.getLastName());
        Assert.assertNull(experimenter0.getUserName());
        Assert.assertNull(experimenter0.getInstitution());
        Assert.assertNull(experimenter0.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter1() {
        Assert.assertNotNull(ode);
        experimenter1 = ode.getExperimenter(1);
        Assert.assertNotNull(experimenter1);
        Assert.assertEquals(ref.Experimenter1FirstName, experimenter1.getFirstName());
        Assert.assertEquals(ref.Experimenter1MiddleName, experimenter1.getMiddleName());
        Assert.assertEquals(ref.Experimenter1LastName, experimenter1.getLastName());
        Assert.assertNull(experimenter1.getUserName());
        Assert.assertNull(experimenter1.getInstitution());
        Assert.assertEquals(ref.Experimenter1Email, experimenter1.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter2() {
        Assert.assertNotNull(ode);
        experimenter2 = ode.getExperimenter(2);
        Assert.assertNotNull(experimenter2);
        Assert.assertNull(experimenter2.getFirstName());
        Assert.assertNull(experimenter2.getMiddleName());
        Assert.assertNull(experimenter2.getLastName());
        Assert.assertNull(experimenter2.getUserName());
        Assert.assertNull(experimenter2.getInstitution());
        Assert.assertNull(experimenter2.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter3() {
        Assert.assertNotNull(ode);
        experimenter3 = ode.getExperimenter(3);
        Assert.assertNotNull(experimenter3);
        Assert.assertNull(experimenter3.getFirstName());
        Assert.assertNull(experimenter3.getMiddleName());
        Assert.assertNull(experimenter3.getLastName());
        Assert.assertNull(experimenter3.getUserName());
        Assert.assertNull(experimenter3.getInstitution());
        Assert.assertNull(experimenter3.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter4() {
        Assert.assertNotNull(ode);
        experimenter4 = ode.getExperimenter(4);
        Assert.assertNotNull(experimenter4);
        Assert.assertNull(experimenter4.getFirstName());
        Assert.assertNull(experimenter4.getMiddleName());
        Assert.assertNull(experimenter4.getLastName());
        Assert.assertNull(experimenter4.getUserName());
        Assert.assertNull(experimenter4.getInstitution());
        Assert.assertNull(experimenter4.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter5() {
        Assert.assertNotNull(ode);
        experimenter5 = ode.getExperimenter(5);
        Assert.assertNotNull(experimenter5);
        Assert.assertNull(experimenter5.getFirstName());
        Assert.assertNull(experimenter5.getMiddleName());
        Assert.assertNull(experimenter5.getLastName());
        Assert.assertNull(experimenter5.getUserName());
        Assert.assertNull(experimenter5.getInstitution());
        Assert.assertNull(experimenter5.getEmail());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenter6() {
        Assert.assertNotNull(ode);
        experimenter6 = ode.getExperimenter(6);
        Assert.assertNotNull(experimenter6);
        Assert.assertNull(experimenter6.getFirstName());
        Assert.assertNull(experimenter6.getMiddleName());
        Assert.assertNull(experimenter6.getLastName());
        Assert.assertNull(experimenter6.getUserName());
        Assert.assertNull(experimenter6.getInstitution());
        Assert.assertNull(experimenter6.getEmail());
    }

    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenterGroup0() {
        Assert.assertNotNull(ode);
        experimenterGroup0 = ode.getExperimenterGroup(0);
        Assert.assertNotNull(experimenterGroup0);
        Assert.assertEquals(ref.Group0Name, experimenterGroup0.getName());
        Assert.assertEquals(ref.Group0Description, experimenterGroup0.getDescription());
    }

    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenterGroup1() {
        Assert.assertNotNull(ode);
        experimenterGroup1 = ode.getExperimenterGroup(1);
        Assert.assertNotNull(experimenterGroup1);
        Assert.assertEquals(ref.Group1Name, experimenterGroup1.getName());
        Assert.assertEquals(ref.Group1Description, experimenterGroup1.getDescription());
    }
    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenterGroup2() {
        Assert.assertNotNull(ode);
        experimenterGroup2 = ode.getExperimenterGroup(2);
        Assert.assertNotNull(experimenterGroup2);
        Assert.assertEquals(ref.Group2Name, experimenterGroup2.getName());
        Assert.assertEquals(ref.Group2Description, experimenterGroup2.getDescription());
    }

    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenterGroup3() {
        Assert.assertNotNull(ode);
        experimenterGroup3 = ode.getExperimenterGroup(3);
        Assert.assertNotNull(experimenterGroup3);
        Assert.assertEquals(ref.Group3Name, experimenterGroup3.getName());
        Assert.assertEquals(ref.Group3Description, experimenterGroup3.getDescription());
    }

    @Test (groups = {"11-06-u-exper"}, dependsOnMethods = {"testOdeNode"})
    public void testExperimenterGroup4() {
        Assert.assertNotNull(ode);
        experimenterGroup4 = ode.getExperimenterGroup(4);
        Assert.assertNotNull(experimenterGroup4);
        Assert.assertEquals(ref.Group4Name, experimenterGroup4.getName());
        Assert.assertEquals(ref.Group4Description, experimenterGroup4.getDescription());
    }

    @Test (groups = {"11-06-u-instrument"}, dependsOnMethods = {"testOdeNode"})
    public void testInstrument0() {
        Assert.assertNotNull(ode);
        instrument0 = ode.getInstrument(0);
        Assert.assertNotNull(instrument0);
        Assert.assertEquals(0, instrument0.sizeOfDetectorList());
        Assert.assertEquals(0, instrument0.sizeOfDichroicList());
        Assert.assertEquals(0, instrument0.sizeOfFilterList());
        Assert.assertEquals(0, instrument0.sizeOfFilterSetList());
        Assert.assertEquals(0, instrument0.sizeOfLightSourceList());
        /* Perhaps: Assert.assertEquals(1, instrument1()); */
    }

    @Test (groups = {"11-06-u-instrument"}, dependsOnMethods = {"testOdeNode"})
    public void testObjective0() {
        Assert.assertNotNull(ode);
        objective0 = instrument0.getObjective(0);
        Assert.assertNotNull(objective0);
        Assert.assertEquals(ref.Instrument0Objective0CalibratedMagnification, objective0.getCalibratedMagnification());
        Assert.assertEquals(ref.Instrument0Objective0LotNumber, objective0.getLotNumber());
        Assert.assertEquals(ref.Instrument0Objective0Manufacturer, objective0.getManufacturer());
        Assert.assertEquals(ref.Instrument0Objective0NominalMagnification, objective0.getNominalMagnification());
        Assert.assertNull(objective0.getCorrection());
        Assert.assertNull(objective0.getImmersion());
        Assert.assertNull(objective0.getIris());
        Assert.assertNull(objective0.getLensNA());
        Assert.assertNull(objective0.getModel());
        Assert.assertNull(objective0.getSerialNumber());
        Assert.assertNull(objective0.getWorkingDistance());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testOdeNode"})
    public void testImage0Name() {
        Assert.assertNotNull(ode);
        Assert.assertEquals(1, ode.sizeOfImageList());
        image0 = ode.getImage(0);
        Assert.assertNotNull(image0);
        Assert.assertEquals(ref.Image0Name, image0.getName());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testImage0Name"})
    public void testImage0Date() {
        Assert.assertNotNull(image0);
        Assert.assertEquals(ref.Image0AcquiredDate, image0.getAcquisitionDate());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testImage0Name"})
    public void testPixels0() {
        Assert.assertNotNull(image0);
        pixels0 = image0.getPixels();
        Assert.assertEquals(3, pixels0.sizeOfBinDataList());
        Assert.assertEquals(3, pixels0.sizeOfChannelList());
        Assert.assertEquals(0, pixels0.sizeOfPlaneList());
        Assert.assertEquals(0, pixels0.sizeOfTiffDataList());
        Assert.assertEquals(ref.Image0Pixels0_0DimensionOrder, pixels0.getDimensionOrder());
        Assert.assertEquals(ref.Image0Pixels0_0PhysicalSizeX, pixels0.getPhysicalSizeX());
        Assert.assertEquals(ref.Image0Pixels0_0PhysicalSizeY, pixels0.getPhysicalSizeY());
        Assert.assertEquals(ref.Image0Pixels0_0Type, pixels0.getType());
        Assert.assertEquals(ref.Image0Pixels0_0SizeC, pixels0.getSizeC());
        Assert.assertEquals(ref.Image0Pixels0_0SizeT, pixels0.getSizeT());
        Assert.assertEquals(ref.Image0Pixels0_0SizeX, pixels0.getSizeX());
        Assert.assertEquals(ref.Image0Pixels0_0SizeY, pixels0.getSizeY());
        Assert.assertEquals(ref.Image0Pixels0_0SizeZ, pixels0.getSizeZ());
        Assert.assertNull(pixels0.getMetadataOnly());
        Assert.assertNull(pixels0.getPhysicalSizeZ());
        Assert.assertNull(pixels0.getTimeIncrement());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testChannel0() {
        Assert.assertNotNull(pixels0);
        channel0 = pixels0.getChannel(0);
        Assert.assertEquals(ref.Image0Pixels0_0Channel0AcquisitionMode, channel0.getAcquisitionMode());
        Assert.assertEquals(ref.Image0Pixels0_0Channel0Color, channel0.getColor());
        Assert.assertNull(channel0.getContrastMethod());
        Assert.assertNull(channel0.getDetectorSettings());
        Assert.assertNull(channel0.getEmissionWavelength());
        Assert.assertNull(channel0.getExcitationWavelength());
        Assert.assertNull(channel0.getFluor());
        Assert.assertNull(channel0.getIlluminationType());
        Assert.assertNull(channel0.getLightPath());
        Assert.assertNull(channel0.getLightSourceSettings());
        Assert.assertNull(channel0.getName());
        Assert.assertNull(channel0.getNDFilter());
        Assert.assertNull(channel0.getPinholeSize());
        Assert.assertNull(channel0.getPockelCellSetting());
        Assert.assertNull(channel0.getSamplesPerPixel());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testChannel1() {
        Assert.assertNotNull(pixels0);
        channel1 = pixels0.getChannel(1);
        Assert.assertEquals(ref.Image0Pixels0_0Channel1AcquisitionMode, channel1.getAcquisitionMode());
        Assert.assertEquals(ref.Image0Pixels0_0Channel1Color, channel1.getColor());
        Assert.assertNull(channel1.getContrastMethod());
        Assert.assertNull(channel1.getDetectorSettings());
        Assert.assertNull(channel1.getEmissionWavelength());
        Assert.assertNull(channel1.getExcitationWavelength());
        Assert.assertNull(channel1.getFluor());
        Assert.assertNull(channel1.getIlluminationType());
        Assert.assertNull(channel1.getLightPath());
        Assert.assertNull(channel1.getLightSourceSettings());
        Assert.assertNull(channel1.getName());
        Assert.assertNull(channel1.getNDFilter());
        Assert.assertNull(channel1.getPinholeSize());
        Assert.assertNull(channel1.getPockelCellSetting());
        Assert.assertNull(channel1.getSamplesPerPixel());
    }
    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testChannel2() {
        Assert.assertNotNull(pixels0);
        channel2 = pixels0.getChannel(2);
        Assert.assertEquals(ref.Image0Pixels0_0Channel2AcquisitionMode, channel2.getAcquisitionMode());
        Assert.assertEquals(ref.Image0Pixels0_0Channel2Color, channel2.getColor());
        Assert.assertNull(channel2.getContrastMethod());
        Assert.assertNull(channel2.getDetectorSettings());
        Assert.assertNull(channel2.getEmissionWavelength());
        Assert.assertNull(channel2.getExcitationWavelength());
        Assert.assertNull(channel2.getFluor());
        Assert.assertNull(channel2.getIlluminationType());
        Assert.assertNull(channel2.getLightPath());
        Assert.assertNull(channel2.getLightSourceSettings());
        Assert.assertNull(channel2.getName());
        Assert.assertNull(channel2.getNDFilter());
        Assert.assertNull(channel2.getPinholeSize());
        Assert.assertNull(channel2.getPockelCellSetting());
        Assert.assertNull(channel2.getSamplesPerPixel());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testBinData0() {
        Assert.assertNotNull(pixels0);
        bindata0 = pixels0.getBinData(0);
        Assert.assertEquals(ref.Image0Pixels0_0Bindata0Length, bindata0.getLength());
        Assert.assertEquals(ref.Image0Pixels0_0Bindata0BigEndian, bindata0.getBigEndian());
        Assert.assertNull(bindata0.getCompression());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testBinData1() {
        Assert.assertNotNull(pixels0);
        bindata1 = pixels0.getBinData(1);
        Assert.assertEquals(ref.Image0Pixels0_0Bindata1Length, bindata1.getLength());
        Assert.assertEquals(ref.Image0Pixels0_0Bindata1BigEndian, bindata1.getBigEndian());
        Assert.assertNull(bindata1.getCompression());
    }

    @Test (groups = {"11-06-u-image"}, dependsOnMethods = {"testPixels0"})
    public void testBinData2() {
        Assert.assertNotNull(pixels0);
        bindata2 = pixels0.getBinData(2);
        Assert.assertEquals(ref.Image0Pixels0_0Bindata2Length, bindata2.getLength());
        Assert.assertEquals(ref.Image0Pixels0_0Bindata2BigEndian, bindata2.getBigEndian());
        Assert.assertNull(bindata2.getCompression());
    }

    @Test (groups = {"11-06-u-annotation"}, dependsOnMethods = {"testOdeNode"})
    public void testAnnotations() {
        Assert.assertNotNull(ode);
        annotations = ode.getStructuredAnnotations();
        Assert.assertEquals(2, annotations.sizeOfXMLAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfBooleanAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfCommentAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfDoubleAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfFileAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfListAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfLongAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfTagAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfTermAnnotationList());
        Assert.assertEquals(0, annotations.sizeOfTimestampAnnotationList());
    }

    @Test (groups = {"11-06-u-annotation"}, dependsOnMethods = {"testAnnotations"})
    public void testXMLAnnotation0() {
        Assert.assertNotNull(annotations);
        xmlAnnotation0 = annotations.getXMLAnnotation(0);
        Assert.assertEquals(ref.Annotation1Value, xmlAnnotation0.getValue());
        Assert.assertNull(xmlAnnotation0.getNamespace());
        Assert.assertNull(xmlAnnotation0.getDescription());
    }

    @Test (groups = {"11-06-u-annotation"}, dependsOnMethods = {"testAnnotations"})
    public void testXMLAnnotation1() {
        Assert.assertNotNull(annotations);
        xmlAnnotation1 = annotations.getXMLAnnotation(1);
        Assert.assertEquals(ref.Annotation2Value, xmlAnnotation1.getValue());
        Assert.assertNull(xmlAnnotation1.getNamespace());
        Assert.assertNull(xmlAnnotation1.getDescription());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testOdeNode"})
    public void testROI0AndUnion() {
        Assert.assertNotNull(ode);
        roi0 = ode.getROI(0);
        Assert.assertNull(roi0.getDescription());
        Assert.assertNull(roi0.getName());
        union0 = roi0.getUnion();
        Assert.assertNotNull(union0);
        Assert.assertEquals(1, union0.sizeOfShapeList());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testOdeNode"})
    public void testROI1AndUnion() {
        Assert.assertNotNull(ode);
        roi1 = ode.getROI(1);
        Assert.assertEquals(ref.ROI1Description, roi1.getDescription());
        Assert.assertNull(roi1.getName());
        union1 = roi1.getUnion();
        Assert.assertNotNull(union1);
        Assert.assertEquals(1, union1.sizeOfShapeList());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testOdeNode"})
    public void testROI2AndUnion() {
        Assert.assertNotNull(ode);
        roi2 = ode.getROI(2);
        Assert.assertNull(roi2.getDescription());
        Assert.assertNull(roi2.getName());
        union2 = roi2.getUnion();
        Assert.assertNotNull(union2);
        Assert.assertEquals(8, union2.sizeOfShapeList());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testOdeNode"})
    public void testROI3AndUnion() {
        Assert.assertNotNull(ode);
        roi3  = ode.getROI(3);
        Assert.assertNull(roi3.getDescription());
        Assert.assertNull(roi3.getName());
        union3 = roi3.getUnion();
        Assert.assertNotNull(union3);
        Assert.assertEquals(1, union3.sizeOfShapeList());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testOdeNode"})
    public void testROI4AndUnion() {
        Assert.assertNotNull(ode);
        roi4 = ode.getROI(4);
        Assert.assertNull(roi4.getDescription());
        Assert.assertNull(roi4.getName());
        union4 = roi4.getUnion();
        Assert.assertNotNull(union4);
        Assert.assertEquals(2, union4.sizeOfShapeList());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI0AndUnion"})
    public void testShape0() {
        Assert.assertNotNull(union0);
        shape0 = union0.getShape(0);
        Assert.assertEquals(Point.class.getName(), shape0.getClass().getName());
        Point point0 = (Point) shape0;
        Assert.assertEquals(ref.ROI0Shape0TheC, point0.getTheC());
        Assert.assertEquals(ref.ROI0Shape0PointX, point0.getX());
        Assert.assertEquals(ref.ROI0Shape0PointY, point0.getY());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI1AndUnion"})
    public void testShape1() {
        Assert.assertNotNull(union1);
        shape1 = union1.getShape(0);
        Assert.assertEquals(Point.class.getName(), shape1.getClass().getName());
        Point point1 = (Point) shape1;
        Assert.assertEquals(ref.ROI1Shape1TheC, point1.getTheC());
        Assert.assertEquals(ref.ROI1Shape1PointX, point1.getX());
        Assert.assertEquals(ref.ROI1Shape1PointY, point1.getY());
        Assert.assertEquals(ref.ROI1Shape1FillRule, point1.getFillRule());
        Assert.assertEquals(ref.ROI1Shape1FontFamily, point1.getFontFamily());
        Assert.assertEquals(ref.ROI1Shape1FontSize, point1.getFontSize());
        Assert.assertEquals(ref.ROI1Shape1FontStyle, point1.getFontStyle());
        Assert.assertEquals(ref.ROI1Shape1StrokeDashArray, point1.getStrokeDashArray());
        Assert.assertEquals(ref.ROI1Shape1StrokeWidth, point1.getStrokeWidth());
        Assert.assertEquals(ref.ROI1Shape1Fill, point1.getFillColor());
        Assert.assertEquals(ref.ROI1Shape1Stroke, point1.getStrokeColor());
        Assert.assertEquals(ref.ROI1Shape1Label, point1.getText());
    }

    @Test (enabled=false, groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape2() {
        Assert.assertNotNull(union2);
        shape2 = union2.getShape(0);
        Assert.assertEquals(Rectangle.class.getName(), shape2.getClass().getName());
        Rectangle rectangle2 = (Rectangle) shape2;
        Assert.assertEquals(ref.ROI2Shape2FillRule, rectangle2.getFillRule());
        Assert.assertEquals(ref.ROI2Shape2Label, rectangle2.getText());
        Assert.assertEquals(ref.ROI2Shape2RectangleX, rectangle2.getX());
        Assert.assertEquals(ref.ROI2Shape2RectangleY, rectangle2.getY());
        Assert.assertEquals(ref.ROI2Shape2RectangleWidth, rectangle2.getWidth());
        Assert.assertEquals(ref.ROI2Shape2RectangleHeight, rectangle2.getHeight());
        Assert.assertNotNull(rectangle2.getTransform());
        Assert.assertEquals(ref.ROI2Shape2TransformA00, rectangle2.getTransform().getA00());
        Assert.assertEquals(ref.ROI2Shape2TransformA01, rectangle2.getTransform().getA01());
        Assert.assertEquals(ref.ROI2Shape2TransformA02, rectangle2.getTransform().getA02());
        Assert.assertEquals(ref.ROI2Shape2TransformA10, rectangle2.getTransform().getA10());
        Assert.assertEquals(ref.ROI2Shape2TransformA11, rectangle2.getTransform().getA11());
        Assert.assertEquals(ref.ROI2Shape2TransformA12, rectangle2.getTransform().getA12());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape3() {
        Assert.assertNotNull(union2);
        shape3 = union2.getShape(0);
        Assert.assertEquals(Label.class.getName(), shape3.getClass().getName());
        Label label3 = (Label) shape3;
        Assert.assertEquals(ref.ROI2Shape3FillRule, label3.getFillRule());
        Assert.assertEquals(ref.ROI2Shape3FontFamily, label3.getFontFamily());
        Assert.assertEquals(ref.ROI2Shape3FontStyle, label3.getFontStyle());
        Assert.assertEquals(ref.ROI2Shape3TextX, label3.getX());
        Assert.assertEquals(ref.ROI2Shape3TextY, label3.getY());
        Assert.assertEquals(ref.ROI2Shape3TextValue, label3.getText());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape4() {
        Assert.assertNotNull(union2);
        shape4 = union2.getShape(4);
        Assert.assertEquals(Polygon.class.getName(), shape4.getClass().getName());
        Polygon polygon4 = (Polygon) shape4;
        Assert.assertEquals(ref.ROI2Shape4Stroke, polygon4.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape4StrokeWidth, polygon4.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape4PolylinePoints, polygon4.getPoints());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape5() {
        Assert.assertNotNull(union2);
        shape5 = union2.getShape(5);
        Assert.assertEquals(Polyline.class.getName(), shape5.getClass().getName());
        Polyline polyline5 = (Polyline) shape5;
        Assert.assertEquals(ref.ROI2Shape5Stroke, polyline5.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape5StrokeWidth, polyline5.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape5PolylinePoints, polyline5.getPoints());
        Assert.assertEquals(ref.ROI2Shape5MarkerStart, polyline5.getMarkerStart());
        Assert.assertEquals(ref.ROI2Shape5MarkerEnd, polyline5.getMarkerEnd());
   }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape6() {
        Assert.assertNotNull(union2);
        shape6 = union2.getShape(6);
        Assert.assertEquals(Polyline.class.getName(), shape6.getClass().getName());
        Polyline polyline6 = (Polyline) shape6;
        Assert.assertEquals(ref.ROI2Shape6Stroke, polyline6.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape6StrokeWidth, polyline6.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape6PolylinePoints, polyline6.getPoints());
        Assert.assertEquals(ref.ROI2Shape6MarkerStart, polyline6.getMarkerStart());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape7() {
        Assert.assertNotNull(union2);
        shape7 = union2.getShape(1);
        Assert.assertEquals(Line.class.getName(), shape7.getClass().getName());
        Line line7 = (Line) shape7;
        Assert.assertEquals(ref.ROI2Shape7Stroke, line7.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape7StrokeWidth, line7.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape7MarkerStart, line7.getMarkerStart());
        Assert.assertEquals(ref.ROI2Shape7MarkerEnd, line7.getMarkerEnd());
        Assert.assertEquals(ref.ROI2Shape7LineX1, line7.getX1());
        Assert.assertEquals(ref.ROI2Shape7LineY1, line7.getY1());
        Assert.assertEquals(ref.ROI2Shape7LineX2, line7.getX2());
        Assert.assertEquals(ref.ROI2Shape7LineY2, line7.getY2());
   }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape8() {
        Assert.assertNotNull(union2);
        shape8 = union2.getShape(2);
        Assert.assertEquals(Line.class.getName(), shape8.getClass().getName());
        Line line8 = (Line) shape8;
        Assert.assertEquals(ref.ROI2Shape8Stroke, line8.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape8StrokeWidth, line8.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape8MarkerEnd, line8.getMarkerEnd());
        Assert.assertEquals(ref.ROI2Shape8LineX1, line8.getX1());
        Assert.assertEquals(ref.ROI2Shape8LineY1, line8.getY1());
        Assert.assertEquals(ref.ROI2Shape8LineX2, line8.getX2());
        Assert.assertEquals(ref.ROI2Shape8LineY2, line8.getY2());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI2AndUnion"})
    public void testShape9() {
        Assert.assertNotNull(union2);
        shape9 = union2.getShape(3);
        Assert.assertEquals(Line.class.getName(), shape9.getClass().getName());
        Line line9 = (Line) shape9;
        Assert.assertEquals(ref.ROI2Shape9Stroke, line9.getStrokeColor());
        Assert.assertEquals(ref.ROI2Shape9StrokeWidth, line9.getStrokeWidth());
        Assert.assertEquals(ref.ROI2Shape9MarkerEnd, line9.getMarkerEnd());
        Assert.assertEquals(ref.ROI2Shape9LineX1, line9.getX1());
        Assert.assertEquals(ref.ROI2Shape9LineY1, line9.getY1());
        Assert.assertEquals(ref.ROI2Shape9LineX2, line9.getX2());
        Assert.assertEquals(ref.ROI2Shape9LineY2, line9.getY2());
   }

    @Test (groups = {"11-06-u-roi-extra"}, dependsOnGroups = {"11-06-u-roi"})
    public void testShape10() {
        /* this path should be striped, if all other roi tests pass it must not be present */
   }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI3AndUnion"})
    public void testShape11() {
        Assert.assertNotNull(union3);
        shape11 = union3.getShape(0);
        Assert.assertEquals(Label.class.getName(), shape11.getClass().getName());
        Label label11 = (Label) shape11;
        Assert.assertEquals(MESSAGE_REMOVED_PATH, label11.getText());
        Assert.assertEquals(Double.valueOf(0), label11.getX());
        Assert.assertEquals(Double.valueOf(0), label11.getY());
    }

    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI4AndUnion"})
    public void testShape12() {
        Assert.assertNotNull(union4);
        shape12 = union4.getShape(0);
        Assert.assertEquals(Label.class.getName(), shape12.getClass().getName());
        Label label12 = (Label) shape12;
        Assert.assertEquals(MESSAGE_REMOVED_PATH, label12.getText());
        Assert.assertEquals(Double.valueOf(0), label12.getX());
        Assert.assertEquals(Double.valueOf(0), label12.getY());
    }
    @Test (groups = {"11-06-u-roi"}, dependsOnMethods = {"testROI4AndUnion"})
    public void testShape13() {
        Assert.assertNotNull(union4);
        shape13 = union4.getShape(1);
        Assert.assertEquals(Label.class.getName(), shape13.getClass().getName());
        Label label13 = (Label) shape13;
        Assert.assertEquals(MESSAGE_REMOVED_PATH, label13.getText());
        Assert.assertEquals(Double.valueOf(0), label13.getX());
        Assert.assertEquals(Double.valueOf(0), label13.getY());
    }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testPlate0Linkage() {
        Assert.assertNotNull(plate0);
        Assert.assertEquals(1, plate0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(xmlAnnotation0, plate0.getLinkedAnnotation(0));
    }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testPlate1Linkage() {
        Assert.assertNotNull(plate1);
        Assert.assertEquals(0, plate1.sizeOfLinkedAnnotationList());
    }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testPlateAcquisition0Linkage() {
        Assert.assertNotNull(plateAcquisition0);
        Assert.assertEquals(1, plateAcquisition0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(xmlAnnotation1, plateAcquisition0.getLinkedAnnotation(0));
    }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-image"})
    public void testWellSample0Linkage() {
        Assert.assertNotNull(wellSample0);
        Assert.assertEquals(image0, wellSample0.getLinkedImage());
    }

    @Test (groups = {"links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testScreen0Linkage() {
        Assert.assertNotNull(screen0);
        Assert.assertEquals(1, screen0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(1, screen0.sizeOfLinkedPlateList());
        Assert.assertEquals(plate0, screen0.getLinkedPlate(0));
        Assert.assertEquals(xmlAnnotation1, screen0.getLinkedAnnotation(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testScreen1Linkage() {
        Assert.assertNotNull(screen1);
        Assert.assertEquals(1, screen1.sizeOfLinkedAnnotationList());
        Assert.assertEquals(2, screen1.sizeOfLinkedPlateList());
        Assert.assertEquals(plate1, screen1.getLinkedPlate(0));
        Assert.assertEquals(plate0, screen1.getLinkedPlate(1));
        Assert.assertEquals(xmlAnnotation1, screen1.getLinkedAnnotation(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testScreen2Linkage() {
        Assert.assertNotNull(screen2);
        Assert.assertEquals(1, screen2.sizeOfLinkedAnnotationList());
        Assert.assertEquals(0, screen2.sizeOfLinkedPlateList());
        Assert.assertEquals(xmlAnnotation1, screen2.getLinkedAnnotation(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-spw", "11-06-u-annotation"})
    public void testScreen3Linkage() {
        Assert.assertNotNull(screen3);
        Assert.assertEquals(1, screen3.sizeOfLinkedAnnotationList());
        Assert.assertEquals(1, screen3.sizeOfLinkedPlateList());
        Assert.assertEquals(plate0, screen3.getLinkedPlate(0));
        Assert.assertEquals(xmlAnnotation1, screen3.getLinkedAnnotation(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-exper"})
    public void testExperimenterGroup0Linkage() {
        Assert.assertNotNull(experimenterGroup0);
        Assert.assertEquals(1, experimenterGroup0.sizeOfLinkedExperimenterList());
        Assert.assertEquals(1, experimenterGroup0.sizeOfLinkedLeaderList());
        Assert.assertEquals(experimenter5, experimenterGroup0.getLinkedExperimenter(0));
        Assert.assertEquals(experimenter0, experimenterGroup0.getLinkedLeader(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-exper"})
    public void testExperimenterGroup1Linkage() {
        Assert.assertNotNull(experimenterGroup1);
        Assert.assertEquals(2, experimenterGroup1.sizeOfLinkedExperimenterList());
        Assert.assertEquals(2, experimenterGroup1.sizeOfLinkedLeaderList());
        Assert.assertEquals(experimenter2, experimenterGroup1.getLinkedExperimenter(0));
        Assert.assertEquals(experimenter3, experimenterGroup1.getLinkedExperimenter(1));
        Assert.assertEquals(experimenter0, experimenterGroup1.getLinkedLeader(0));
        Assert.assertEquals(experimenter1, experimenterGroup1.getLinkedLeader(1));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-exper"})
    public void testExperimenterGroup2Linkage() {
        Assert.assertNotNull(experimenterGroup2);
        Assert.assertEquals(1, experimenterGroup2.sizeOfLinkedExperimenterList());
        Assert.assertEquals(1, experimenterGroup2.sizeOfLinkedLeaderList());
        Assert.assertEquals(experimenter4, experimenterGroup2.getLinkedExperimenter(0));
        Assert.assertEquals(experimenter6, experimenterGroup2.getLinkedLeader(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-exper"})
    public void testExperimenterGroup3Linkage() {
        Assert.assertNotNull(experimenterGroup3);
        Assert.assertEquals(0, experimenterGroup3.sizeOfLinkedExperimenterList());
        Assert.assertEquals(1, experimenterGroup3.sizeOfLinkedLeaderList());
        Assert.assertEquals(experimenter0, experimenterGroup3.getLinkedLeader(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-exper"})
    public void testExperimenterGroup4Linkage() {
        Assert.assertNotNull(experimenterGroup4);
        Assert.assertEquals(0, experimenterGroup4.sizeOfLinkedExperimenterList());
        Assert.assertEquals(1, experimenterGroup4.sizeOfLinkedLeaderList());
        Assert.assertEquals(experimenter0, experimenterGroup4.getLinkedLeader(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-image", "11-06-u-exper", "11-06-u-roi", "11-06-u-annotation"})
    public void testImage0Linkage() {
        Assert.assertNotNull(image0);
        Assert.assertEquals(0, image0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(3, image0.sizeOfLinkedROIList());
        Assert.assertEquals(experimenter1, image0.getLinkedExperimenter());
        Assert.assertEquals(roi0, image0.getLinkedROI(0));
        Assert.assertEquals(roi1, image0.getLinkedROI(1));
        Assert.assertEquals(roi2, image0.getLinkedROI(2));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-proj", "11-06-u-annotation"})
    public void testProject0Linkage() {
        Assert.assertNotNull(project0);
        Assert.assertEquals(1, project0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(1, project0.sizeOfLinkedDatasetList());
        Assert.assertEquals(dataset0, project0.getLinkedDataset(0));
        Assert.assertEquals(xmlAnnotation0, project0.getLinkedAnnotation(0));
     }

    @Test (groups = {"11-06-u-links"}, dependsOnGroups = {"11-06-u-image", "11-06-u-proj", "11-06-u-annotation"})
    public void testDataset0Linkage() {
        Assert.assertNotNull(dataset0);
        Assert.assertEquals(1, dataset0.sizeOfLinkedAnnotationList());
        Assert.assertEquals(1, dataset0.sizeOfLinkedImageList());
        Assert.assertEquals(image0, dataset0.getLinkedImage(0));
        Assert.assertEquals(xmlAnnotation1, dataset0.getLinkedAnnotation(0));
     }
}
