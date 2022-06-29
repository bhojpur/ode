package loci.formats.utests;

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

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import loci.formats.ode.ODEXMLMetadataImpl;

import static org.testng.AssertJUnit.*;

import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Annotation;
import ode.xml.model.Arc;
import ode.xml.model.BooleanAnnotation;
import ode.xml.model.Filament;
import ode.xml.model.LightEmittingDiode;
import ode.xml.model.ListAnnotation;
import ode.xml.model.LongAnnotation;
import ode.xml.model.ODEModel;
import ode.xml.model.ODEModelImpl;
import ode.xml.model.ODEModelObject;
import ode.xml.model.Objective;
import ode.xml.model.Reference;
import ode.xml.model.CommentAnnotation;
import ode.xml.model.TiffData;
import ode.xml.model.TimestampAnnotation;
import ode.xml.model.UUID;
import ode.xml.model.XMLAnnotation;
import ode.xml.model.Channel;
import ode.xml.model.Dichroic;
import ode.xml.model.Detector;
import ode.xml.model.Filter;
import ode.xml.model.FilterSet;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.Laser;
import ode.xml.model.Pixels;
import ode.xml.model.Plate;
import ode.xml.model.Rectangle;
import ode.xml.model.ROI;
import ode.xml.model.Shape;
import ode.xml.model.Union;
import ode.xml.model.Well;
import ode.xml.model.WellSample;
import ode.xml.model.enums.ArcType;
import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.EnumerationException;
import ode.xml.model.enums.FilamentType;
import ode.xml.model.enums.FilterType;
import ode.xml.model.enums.LaserType;
import ode.xml.model.enums.NamingConvention;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;

import ode.units.UNITS;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 */
@Test(groups={"inOutTests"})
public class InOutCurrentTest {
  public static final String IMAGE_ID = "Image:0";

  public static final String GENERAL_ANNOTATION_NAMESPACE = "test-ode-InOutCurrent-namespace";

  public static final String IMAGE_ANNOTATION_ID = "Annotation:ImageBoolean0";

  public static final String IMAGE_LIST_ANNOTATION_ID = "Annotation:ImageList0";

  public static final String PIXELS_ID = "Pixels:0";

  public static final String CHANNEL_ANNOTATION_ID = "Annotation:ChannelXML0";

  public static final String INSTRUMENT_ID = "Instrument:0";

  public static final String INSTRUMENT_ANNOTATION_ID = "Annotation:InstrumentString1";

  public static final String INSTRUMENT_ANNOTATION_VALUE = "Value:String1";

  public static final String DETECTOR_ID = "Detector:0";

  public static final String DETECTOR_ANNOTATION_ID = "Annotation:DetectorString2";

  public static final String DETECTOR_ANNOTATION_VALUE = "Value:String2";

  public static final String LIGHTSOURCE_LASER_ID = "LightSource:0";

  public static final String LIGHTSOURCE_LASER_ANNOTATION_ID = "Annotation:LightSourceLaserString3";

  public static final String LIGHTSOURCE_LASER_ANNOTATION_VALUE = "Value:String3";

  public static final String LIGHTSOURCE_PUMP_ID = "LightSource:1";

  public static final String LIGHTSOURCE_ARC_ID = "LightSource:2";

  public static final String LIGHTSOURCE_ARC_ANNOTATION_ID = "Annotation:LightSourceArcString4";

  public static final String LIGHTSOURCE_ARC_ANNOTATION_VALUE = "Value:String4";

  public static final String LIGHTSOURCE_FILAMENT_ID = "LightSource:3";

  public static final String LIGHTSOURCE_FILAMENT_ANNOTATION_ID = "Annotation:LightSourceFilamentString5";

  public static final String LIGHTSOURCE_FILAMENT_ANNOTATION_VALUE = "Value:String5";

  public static final String LIGHTSOURCE_LED_ID = "LightSource:4";

  public static final String LIGHTSOURCE_LED_ANNOTATION_ID = "Annotation:LightSourceLEDString6";

  public static final String LIGHTSOURCE_LED_ANNOTATION_VALUE = "Value:String6";

  public static final String DICHROIC_ID = "Dichroic:0";

  public static final String DICHROIC_ANNOTATION_ID = "Annotation:DichroicString7";

  public static final String DICHROIC_ANNOTATION_VALUE = "Value:String7";

  public static final String FILTERSET_ID = "FilterSet:0";

  public static final String EM_FILTER_ID = "Filter:0";

  public static final String EM_FILTER_ANNOTATION_ID = "Annotation:EmFilterString8";

  public static final String EM_FILTER_ANNOTATION_VALUE = "Value:String8";

  public static final String EX_FILTER_ID = "Filter:1";

  public static final String OBJECTIVE_ID = "Objective:0";

  public static final String OBJECTIVE_ANNOTATION_ID = "Annotation:ObjectiveString9";

  public static final String OBJECTIVE_ANNOTATION_VALUE = "Value:String9";

  public static final String PLATE_ID = "Plate:0";

  public static final String PLATE_ANNOTATION_ID = "Annotation:PlateTimestamp0";

  public static final String WELL_ANNOTATION_ID = "Annotation:WellLong0";

  public static final String ROI_ID = "ROI:5";

  public static final String ROI_ANNOTATION_ID = "Annotation:ROIString0";

  public static final String SHAPE_ID = "Shape:0";

  public static final String SHAPE_ANNOTATION_ID = "Annotation:ShapeString10";

  public static final String SHAPE_ANNOTATION_VALUE = "Value:String10";

  public static final DimensionOrder DIMENSION_ORDER = DimensionOrder.XYZCT;

  public static final PixelType PIXEL_TYPE = PixelType.UINT16;

  public static final Integer SIZE_X = 512;

  public static final Integer SIZE_Y = 512;

  public static final Integer SIZE_Z = 64;

  public static final Integer SIZE_C = 3;

  public static final Integer SIZE_T = 50;

  public static final String DETECTOR_MODEL = "ReallySensitive!";

  public static final String LIGHTSOURCE_LASER_MODEL = "ReallyBrightLaser!";

  public static final String LIGHTSOURCE_PUMP_MODEL = "ReallyBrightPump!";

  public static final String LIGHTSOURCE_ARC_MODEL = "ReallyBrightArc!";

  public static final String LIGHTSOURCE_FILAMENT_MODEL = "ReallyBrightFilament!";

  public static final String LIGHTSOURCE_LED_MODEL = "ReallyBrightLED!";

  public static final String OBJECTIVE_MODEL = "ReallyClear!";

  public static final String DICHROIC_SN = "0123456789";

  public static final String FILTERSET_LOT = "RandomLot";

  public static final FilterType EM_FILTER_TYPE = FilterType.LONGPASS;

  public static final FilterType EX_FILTER_TYPE = FilterType.NEUTRALDENSITY;

  public static final Double LIGHTSOURCE_LASER_POWER = 1000.0;

  public static final Double LIGHTSOURCE_PUMP_POWER = 100.0;

  public static final Double LIGHTSOURCE_ARC_POWER = 500.0;

  public static final Double LIGHTSOURCE_FILAMENT_POWER = 200.0;

  public static final Double LIGHTSOURCE_LED_POWER = 10.0;

  public static final LaserType LASER_TYPE = LaserType.DYE;
  
  public static final ArcType ARC_TYPE = ArcType.HGXE;

  public static final FilamentType FILAMENT_TYPE = FilamentType.HALOGEN;

  public static final Boolean IMAGE_ANNOTATION_VALUE = Boolean.FALSE;

  public static final String CHANNEL_ANNOTATION_VALUE =
  "<TestData><key>foo</key><value>bar</value></TestData>";

  public static final Double PIXELS_ANNOTATION_VALUE = 3.14;

  public static final String PLATE_ANNOTATION_VALUE = "1970-01-01T00:00:00";

  public static final Long WELL_ANNOTATION_VALUE = 262144L;

  public static final PositiveInteger WELL_ROWS = new PositiveInteger(3);

  public static final PositiveInteger WELL_COLS = new PositiveInteger(2);

  public static final NamingConvention WELL_ROW = NamingConvention.LETTER;

  public static final NamingConvention WELL_COL = NamingConvention.NUMBER;

  public static final String ROI_ANNOTATION_VALUE = "Some extra ROI data";

  public static final Double RECTANGLE_X = 10.0;

  public static final Double RECTANGLE_Y = 20.0;

  public static final Double RECTANGLE_WIDTH = 128.0;

  public static final Double RECTANGLE_HEIGHT = 256.0;

  public static final String TIFF_DATA_UUID = 
    "6DFA2954-FA9B-4447-A26C-82F9580D9425";

  /** XML namespace. */
  public static final String XML_NS =
    "http://www.bhojpur.net/Schemas/ODE/2018-03";

  /** XSI namespace. */
  public static final String XSI_NS =
    "http://www.w3.org/2001/XMLSchema-instance";

  /** XML schema location. */
  public static final String SCHEMA_LOCATION =
    "http://www.bhojpur.net/Schemas/ODE/2018-03/ode.xsd";

  private Document document;

  public String asString;

  public ODEModelMock mock;

  public ODEXMLMetadataRoot ode;

  public ODEModel model;

  public ODEXMLMetadataImpl metadata;

  @Parameters({"mockClassName"})
  @BeforeClass
  public void setUp(@Optional String mockClassName) throws Exception {
    if (mockClassName == null) {
      mockClassName = "loci.formats.utests.ObjectBasedODEModelMock";
    }
    Class mockClass = Class.forName(mockClassName);
    Constructor constructor = mockClass.getDeclaredConstructor();
    mock = (ODEModelMock) constructor.newInstance();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    document = parser.newDocument();
    ode = (ODEXMLMetadataRoot) mock.getRoot();
    // Produce a valid ODE DOM element hierarchy
    Element root = ode.asXMLElement(document);
    root.setAttribute("xmlns", XML_NS);
    root.setAttribute("xmlns:xsi", XSI_NS);
    root.setAttribute("xsi:schemaLocation", XML_NS + " " + SCHEMA_LOCATION);
    document.appendChild(root);
    // Produce string XML
    asString = asString();
  }

  @Test
  public void testValidODENode() throws EnumerationException {
    model = new ODEModelImpl();
    // Read string XML in as a DOM tree and parse into the object hierarchy
    ode = new ODEXMLMetadataRoot(document.getDocumentElement(), model);
    model.resolveReferences();
    assertNotNull(ode);
    assertEquals(1, ode.sizeOfImageList());
  }

  @Test
  public void testValidMetadataRoot() {
    metadata = new ODEXMLMetadataImpl();
    metadata.setRoot(ode);
    assertEquals(ode, metadata.getRoot());
  }

  @Test(dependsOnMethods={"testValidODENode"})
  public void testValidImageNode() {
    Image image = ode.getImage(0);
    assertNotNull(image);
    assertEquals(IMAGE_ID, image.getID());
  }

  @Test(dependsOnMethods={"testValidMetadataRoot"})
  public void testValidImageMetadata() {
    assertEquals(1, metadata.getImageCount());
    assertEquals(IMAGE_ID, metadata.getImageID(0));
  }

  @Test(dependsOnMethods={"testValidImageNode"})
  public void testValidImageAnnotation() {
    Annotation n = ode.getImage(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(ListAnnotation.class, n.getClass());
    ListAnnotation l = (ListAnnotation) n;
    assertEquals(l.getID(), IMAGE_LIST_ANNOTATION_ID);
    assertEquals(l.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    Annotation n2 = l.getLinkedAnnotation(0);
    assertEquals(BooleanAnnotation.class, n2.getClass());
    BooleanAnnotation b = (BooleanAnnotation) n2;
    assertEquals(b.getValue(), IMAGE_ANNOTATION_VALUE);
    assertEquals(b.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertEquals(b.getID(), IMAGE_ANNOTATION_ID);
  }

  @Test(dependsOnMethods={"testValidImageMetadata"})
  public void testValidImageAnnotationMetadata() {
    assertEquals(1, metadata.getListAnnotationCount());
    assertEquals(1, metadata.getBooleanAnnotationCount());
    assertEquals(1, metadata.getImageAnnotationRefCount(0));
    assertEquals(IMAGE_LIST_ANNOTATION_ID, metadata.getListAnnotationID(0));
    assertEquals(IMAGE_ANNOTATION_VALUE, metadata.getBooleanAnnotationValue(0));
    assertEquals(GENERAL_ANNOTATION_NAMESPACE,
                 metadata.getBooleanAnnotationNamespace(0));
    assertEquals(IMAGE_ANNOTATION_ID, metadata.getBooleanAnnotationID(0));
  }

  @Test(dependsOnMethods={"testValidImageNode"})
  public void testValidPixelsNode() {
    Pixels pixels = ode.getImage(0).getPixels();
    assertEquals(SIZE_X, pixels.getSizeX().getValue());
    assertEquals(SIZE_Y, pixels.getSizeY().getValue());
    assertEquals(SIZE_Z, pixels.getSizeZ().getValue());
    assertEquals(SIZE_C, pixels.getSizeC().getValue());
    assertEquals(SIZE_T, pixels.getSizeT().getValue());
    assertEquals(DIMENSION_ORDER, pixels.getDimensionOrder());
    assertEquals(PIXEL_TYPE, pixels.getType());
    assertEquals(1, pixels.sizeOfTiffDataList());
  }

  @Test(dependsOnMethods={"testValidImageMetadata"})
  public void testValidPixelsMetadata() {
    assertEquals(SIZE_X, metadata.getPixelsSizeX(0).getValue());
    assertEquals(SIZE_Y, metadata.getPixelsSizeY(0).getValue());
    assertEquals(SIZE_Z, metadata.getPixelsSizeZ(0).getValue());
    assertEquals(SIZE_C, metadata.getPixelsSizeC(0).getValue());
    assertEquals(SIZE_T, metadata.getPixelsSizeT(0).getValue());
    assertEquals(DIMENSION_ORDER, metadata.getPixelsDimensionOrder(0));
    assertEquals(PIXEL_TYPE, metadata.getPixelsType(0));
  }

  @Test(dependsOnMethods={"testValidPixelsNode"})
  public void testValidTiffDataNode() {
    TiffData tiffData = ode.getImage(0).getPixels().getTiffData(0);
    UUID uuid = tiffData.getUUID();
    assertEquals(TIFF_DATA_UUID, uuid.getValue());
  }

  @Test(dependsOnMethods={"testValidPixelsMetadata"})
  public void testValidTiffDataMetadata() {
    // TODO: Implement
  }

  @Test(dependsOnMethods={"testValidPixelsNode"})
  public void testValidChannelNode() {
    Pixels pixels = ode.getImage(0).getPixels();
    assertEquals(3, pixels.sizeOfChannelList());
    for (Channel channel : pixels.copyChannelList()) {
      assertNotNull(channel.getID());
    }
  }

  @Test(dependsOnMethods={"testValidPixelsMetadata"})
  public void testValidChannelMetadata() {
    assertEquals(3, metadata.getChannelCount(0));
    for (int i = 0; i < 3; i++) {
      assertNotNull(metadata.getChannelID(0, i));
    }
  }

  @Test(dependsOnMethods={"testValidChannelNode"})
  public void testValidChannelAnnotation() {
    Channel c = ode.getImage(0).getPixels().getChannel(0);
    Annotation n = c.getLinkedAnnotation(0);
    assertNotNull(n);
    assertTrue(n instanceof XMLAnnotation);
    assertEquals(CHANNEL_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertEquals(CHANNEL_ANNOTATION_VALUE, ((XMLAnnotation)n).getValue());
  }

  @Test(dependsOnMethods={"testValidODENode"})
  public void testValidInstrumentNode() {
    Instrument instrument = ode.getInstrument(0);
    assertNotNull(instrument);
    assertEquals(INSTRUMENT_ID, instrument.getID());
  }

  @Test(dependsOnMethods={"testValidMetadataRoot"})
  public void testValidInstrumentMetadata() {
    assertEquals(1, metadata.getInstrumentCount());
    assertEquals(INSTRUMENT_ID, metadata.getInstrumentID(0));
    assertEquals(5, metadata.getLightSourceCount(0));
    assertEquals(1, metadata.getDetectorCount(0));
    assertEquals(2, metadata.getFilterCount(0));
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"})
  public void testValidInstrumentAnnotation() {
    Annotation n = ode.getInstrument(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(INSTRUMENT_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(INSTRUMENT_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"})
  public void testValidDetectorNode() {
    Detector detector = ode.getInstrument(0).getDetector(0);
    assertNotNull(detector);
    assertEquals(DETECTOR_ID, detector.getID());
    assertEquals(DETECTOR_MODEL, detector.getModel());
  }

  @Test(dependsOnMethods={"testValidDetectorNode"})
  public void testValidDetectorMetadata() {
    assertEquals(1, metadata.getDetectorCount(0));
    assertEquals(DETECTOR_ID, metadata.getDetectorID(0, 0));
    assertEquals(DETECTOR_MODEL, metadata.getDetectorModel(0, 0));
  }

  @Test(dependsOnMethods={"testValidDetectorNode"})
  public void testValidDetectorAnnotation() {
    Annotation n = ode.getInstrument(0).getDetector(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(DETECTOR_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(DETECTOR_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"},enabled=false)
  public void testValidLaserNode() {
    Laser laser = (Laser) ode.getInstrument(0).getLightSource(0);
    assertNotNull(laser);
    assertEquals(LIGHTSOURCE_LASER_ID, laser.getID());
    assertEquals(LIGHTSOURCE_LASER_MODEL, laser.getModel());
    assertEquals(LIGHTSOURCE_LASER_POWER, laser.getPower().value(UNITS.MILLIWATT).doubleValue());
    assertEquals(LASER_TYPE, laser.getType());
  }

  @Test(dependsOnMethods={"testValidLaserNode"},enabled=false)
  public void testValidLaserMetadata() {
    assertEquals(LIGHTSOURCE_LASER_ID, metadata.getLaserID(0, 0));
    assertEquals(LIGHTSOURCE_LASER_MODEL, metadata.getLaserModel(0, 0));
    assertEquals(LIGHTSOURCE_LASER_POWER, metadata.getLaserPower(0, 0).value(UNITS.MILLIWATT).doubleValue());
    assertEquals(LASER_TYPE, metadata.getLaserType(0, 0));
  }

  @Test(dependsOnMethods={"testValidLaserMetadata"},enabled=false)
  public void testValidLaserAnnotation() {
    Annotation n = ode.getInstrument(0).getLightSource(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(LIGHTSOURCE_LASER_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(LIGHTSOURCE_LASER_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidLaserNode"},enabled=false)
  public void testValidPumpNode() {
    Laser laser = (Laser) ode.getInstrument(0).getLightSource(0);
    Laser laserPump = (Laser) ode.getInstrument(0).getLightSource(1);
    assertNotNull(laserPump);
    assertEquals(LIGHTSOURCE_PUMP_ID, laserPump.getID());
    assertEquals(LIGHTSOURCE_PUMP_MODEL, laserPump.getModel());
    assertEquals(LIGHTSOURCE_PUMP_POWER, laserPump.getPower().value(UNITS.MILLIWATT).doubleValue());
    assertEquals(LASER_TYPE, laserPump.getType());
    assertEquals(laser.getLinkedPump(),laserPump);
  }

  @Test(dependsOnMethods={"testValidLaserMetadata"},enabled=false)
  public void testValidPumpMetadata() {
    assertEquals(LIGHTSOURCE_PUMP_ID, metadata.getLaserID(0, 1));
    assertEquals(LIGHTSOURCE_PUMP_MODEL, metadata.getLaserModel(0, 1));
    assertEquals(LIGHTSOURCE_PUMP_POWER, metadata.getLaserPower(0, 1).value(UNITS.MILLIWATT).doubleValue());
    assertEquals(LASER_TYPE, metadata.getLaserType(0, 1));
    assertEquals(LIGHTSOURCE_PUMP_ID, metadata.getLaserPump(0, 0));
  }

  // Create <Arc/> under <Instrument/>
  @Test(dependsOnMethods={"testValidInstrumentNode"},enabled=false)
  public void testValidArcNode() {
    Arc arc = (Arc) ode.getInstrument(0).getLightSource(2);
    assertNotNull(arc);
    assertEquals(LIGHTSOURCE_ARC_ID, arc.getID());
    assertEquals(LIGHTSOURCE_ARC_MODEL, arc.getModel());
    assertEquals(LIGHTSOURCE_ARC_POWER, arc.getPower().value(UNITS.MILLIWATT).doubleValue());
    assertEquals(ARC_TYPE, arc.getType());
  }

  @Test(dependsOnMethods={"testValidArcNode"},enabled=false)
  public void testValidArcMetadata() {
    assertEquals(LIGHTSOURCE_ARC_ID, metadata.getArcID(0, 2));
    assertEquals(LIGHTSOURCE_ARC_MODEL, metadata.getArcModel(0, 2));
    assertEquals(LIGHTSOURCE_ARC_POWER, metadata.getArcPower(0, 2).value(UNITS.MILLIWATT).doubleValue());
    assertEquals(ARC_TYPE, metadata.getArcType(0, 2));
  }

  @Test(dependsOnMethods={"testValidArcNode"},enabled=false)
  public void testValidArcAnnotation() {
    Annotation n = ode.getInstrument(0).getLightSource(2).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(LIGHTSOURCE_ARC_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(LIGHTSOURCE_ARC_ANNOTATION_VALUE, string.getValue());
  }

  // Create <Filament/> under <Instrument/>
  @Test(dependsOnMethods={"testValidInstrumentNode"},enabled=false)
  public void testValidFilamentNode() {
    Filament filament = (Filament) ode.getInstrument(0).getLightSource(3);
    assertNotNull(filament);
    assertEquals(LIGHTSOURCE_FILAMENT_ID, filament.getID());
    assertEquals(LIGHTSOURCE_FILAMENT_MODEL, filament.getModel());
    assertEquals(LIGHTSOURCE_FILAMENT_POWER, filament.getPower().value(UNITS.MILLIWATT).doubleValue());
    assertEquals(FILAMENT_TYPE, filament.getType());
  }

  // Create <Filament/> under <Instrument/>
  @Test(dependsOnMethods={"testValidFilamentNode"},enabled=false)
  public void testValidFilamentMetadata() {
    assertEquals(LIGHTSOURCE_FILAMENT_ID, metadata.getFilamentID(0, 3));
    assertEquals(LIGHTSOURCE_FILAMENT_MODEL, metadata.getFilamentModel(0, 3));
    assertEquals(LIGHTSOURCE_FILAMENT_POWER, metadata.getFilamentPower(0, 3).value(UNITS.MILLIWATT).doubleValue());
    assertEquals(FILAMENT_TYPE, metadata.getFilamentType(0, 3));
  }

  @Test(dependsOnMethods={"testValidFilamentNode"},enabled=false)
  public void testValidFilamentAnnotation() {
    Annotation n = ode.getInstrument(0).getLightSource(3).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(LIGHTSOURCE_FILAMENT_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(LIGHTSOURCE_FILAMENT_ANNOTATION_VALUE, string.getValue());
  }

  // Create <LightEmittingDiode/> under <Instrument/>
  @Test(dependsOnMethods={"testValidInstrumentNode"},enabled=false)
  public void testValidLightEmittingDiodeNode() {
    LightEmittingDiode led = (LightEmittingDiode) ode.getInstrument(0).getLightSource(4);
    assertNotNull(led);
    assertEquals(LIGHTSOURCE_LED_ID, led.getID());
    assertEquals(LIGHTSOURCE_LED_MODEL, led.getModel());
    assertEquals(LIGHTSOURCE_LED_POWER, led.getPower().value(UNITS.MILLIWATT).doubleValue());
  }

  @Test(dependsOnMethods={"testValidLightEmittingDiodeNode"},enabled=false)
  public void testValidLightEmittingDiodeAnnotation() {
    LightEmittingDiode led = (LightEmittingDiode) ode.getInstrument(0).getLightSource(4);
    Annotation n = led.getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(LIGHTSOURCE_LED_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(LIGHTSOURCE_LED_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"})
  public void testValidDichroicNode() {
    Dichroic dichroic = ode.getInstrument(0).getDichroic(0);
    assertNotNull(dichroic);
    assertEquals(DICHROIC_ID, dichroic.getID());
    assertEquals(DICHROIC_SN, dichroic.getSerialNumber());
  }

  @Test(dependsOnMethods={"testValidDichroicNode"})
  public void testValidDichroicAnnotation() {
    Annotation n = ode.getInstrument(0).getDichroic(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(DICHROIC_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(DICHROIC_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode"})
  public void testValidObjectiveNode()
  {
    Objective objective = ode.getInstrument(0).getObjective(0);
    assertNotNull(objective);
    assertEquals(OBJECTIVE_ID, objective.getID());
    assertEquals(OBJECTIVE_MODEL, objective.getModel());
  }

  @Test(dependsOnMethods={"testValidObjectiveNode"})
  public void testValidObjectiveAnnotation() {
    Annotation n = ode.getInstrument(0).getObjective(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(OBJECTIVE_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(OBJECTIVE_ANNOTATION_VALUE, string.getValue());
  }

  @Test(dependsOnMethods={"testValidDichroicNode"})
  public void testValidFilterSetNode() {
    Dichroic dichroic = ode.getInstrument(0).getDichroic(0);
    assertEquals(1, dichroic.sizeOfLinkedFilterSetList());
    FilterSet filterSet = dichroic.getLinkedFilterSet(0);
    assertNotNull(filterSet);
    assertEquals(FILTERSET_ID, filterSet.getID());
    assertEquals(FILTERSET_LOT, filterSet.getLotNumber());
    assertEquals(filterSet.getLinkedDichroic().getID(), dichroic.getID());
  }

  @Test(dependsOnMethods={"testValidFilterSetNode"})
  public void testValidEmissionFilterNode() {
    Filter emFilter = ode.getInstrument(0).getFilter(0);
    assertNotNull(emFilter);
    assertEquals(EM_FILTER_ID, emFilter.getID());
    assertEquals(EM_FILTER_TYPE, emFilter.getType());
    FilterSet filterSet = ode.getInstrument(0).getDichroic(0).getLinkedFilterSet(0);
    assertEquals(EM_FILTER_ID, filterSet.getLinkedEmissionFilter(0).getID());
  }

  @Test(dependsOnMethods={"testValidEmissionFilterNode"})
  public void testValidEmissionFilterAnnotation() {
    Annotation n = ode.getInstrument(0).getFilter(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(EM_FILTER_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(EM_FILTER_ANNOTATION_VALUE, string.getValue());
  }
  @Test(dependsOnMethods={"testValidFilterSetNode"})
  public void testValidExcitationFilter() {
    Filter exFilter = ode.getInstrument(0).getFilter(1);
    assertNotNull(exFilter);
    assertEquals(EX_FILTER_ID, exFilter.getID());
    assertEquals(EX_FILTER_TYPE, exFilter.getType());
    FilterSet filterSet = ode.getInstrument(0).getDichroic(0).getLinkedFilterSet(0);
    assertEquals(EX_FILTER_ID, filterSet.getLinkedExcitationFilter(0).getID());
  }

  @Test(dependsOnMethods={"testValidInstrumentNode", "testValidImageNode"})
  public void testImageInstrumentLinkage() {
    Instrument instrument = ode.getInstrument(0);
    Image image = ode.getImage(0);

    Instrument linkedInstrument = image.getLinkedInstrument();
    assertNotNull(linkedInstrument);
    assertEquals(instrument.getID(), linkedInstrument.getID());

    assertEquals(1, instrument.sizeOfLinkedImageList());
    Image linkedImage = instrument.getLinkedImage(0);
    assertNotNull(linkedImage);
    assertEquals(image.getID(), linkedImage.getID());
  }

  @Test(dependsOnMethods={"testValidODENode"})
  public void testValidPlateNode() {
    Plate plate = ode.getPlate(0);
    assertNotNull(plate);
    assertEquals(PLATE_ID, plate.getID());
    assertEquals(plate.getRows(), WELL_ROWS);
    assertEquals(plate.getColumns(), WELL_COLS);
    assertEquals(plate.getRowNamingConvention(), WELL_ROW);
    assertEquals(plate.getColumnNamingConvention(), WELL_COL);
    assertEquals(plate.sizeOfWellList(), WELL_ROWS.getValue() * WELL_COLS.getValue());
    for (Integer row=0; row<WELL_ROWS.getValue(); row++) {
      for (Integer col=0; col<WELL_COLS.getValue(); col++) {
        Well well = plate.getWell(row * WELL_COLS.getValue() + col);
        assertNotNull(well);
        assertEquals(String.format("Well:%d_%d", row, col), well.getID());
        assertEquals(well.getRow(), row);
        assertEquals(well.getColumn(), col);
      }
    }
  }

  @Test(dependsOnMethods={"testValidPlateNode"})
  public void testValidPlateAnnotation() {
    Annotation n = ode.getPlate(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(PLATE_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof TimestampAnnotation);
    TimestampAnnotation timestamp = (TimestampAnnotation) n;
    assertEquals(timestamp.getValue(), PLATE_ANNOTATION_VALUE);
  }

  @Test(dependsOnMethods={"testValidPlateNode"})
  public void testValidWellSamples() {
    Plate plate = ode.getPlate(0);
    Integer wellSampleIndex = 0;
    for (int row=0; row<plate.getRows().getValue(); row++) {
      for (int col=0; col<plate.getColumns().getValue(); col++) {
        Well well = plate.getWell(row * plate.getColumns().getValue() + col);
        assertEquals(1, well.sizeOfWellSampleList());
        WellSample sample = well.getWellSample(0);
        assertNotNull(sample);
        assertEquals(String.format("WellSample:%d_%d", row, col),
          sample.getID());
        assertEquals(wellSampleIndex, sample.getIndex().getValue());
        Image image = sample.getLinkedImage();
        assertNotNull(image);
        assertEquals(IMAGE_ID, image.getID());
        wellSampleIndex++;
      }
    }
  }

  @Test(dependsOnMethods={"testValidWellSamples"})
  public void testValidWellAnnotation() {
    Annotation n = ode.getPlate(0).getWell(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(WELL_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof LongAnnotation);
    LongAnnotation longAnnotation = (LongAnnotation) n;
    assertEquals(longAnnotation.getValue(), WELL_ANNOTATION_VALUE);
  }

  @Test(dependsOnMethods={"testValidODENode"})
  public void testValidROINode() {
    ROI roi = ode.getROI(0);
    assertNotNull(roi);
    assertEquals(ROI_ID, roi.getID());

    Union shapeUnion = roi.getUnion();
    assertNotNull(shapeUnion);
    assertEquals(1, shapeUnion.sizeOfShapeList());
    Shape s = shapeUnion.getShape(0);
    assertNotNull(s);
    assertEquals(SHAPE_ID, s.getID());
    assertTrue(s instanceof Rectangle);

    Rectangle rect = (Rectangle) s;
    assertEquals(RECTANGLE_X, rect.getX());
    assertEquals(RECTANGLE_Y, rect.getY());
    assertEquals(RECTANGLE_WIDTH, rect.getWidth());
    assertEquals(RECTANGLE_HEIGHT, rect.getHeight());
  }

  @Test(dependsOnMethods={"testValidROINode"})
  public void testValidROIAnnotation() {
    Annotation n = ode.getROI(0).getLinkedAnnotation(0);
    assertNotNull(n);
    assertEquals(ROI_ANNOTATION_ID, n.getID());
    assertEquals(n.getNamespace(), GENERAL_ANNOTATION_NAMESPACE);
    assertTrue(n instanceof CommentAnnotation);
    CommentAnnotation string = (CommentAnnotation) n;
    assertEquals(ROI_ANNOTATION_VALUE, string.getValue());
  }

  private String asString()
  throws TransformerException, UnsupportedEncodingException {
    TransformerFactory transformerFactory =
      TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    //Setup indenting to "pretty print"
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "4");
    Source source = new DOMSource(document);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    Result result = new StreamResult(new OutputStreamWriter(os, "utf-8"));
    transformer.transform(source, result);
    return os.toString();
  }

  @Test(groups={"disabled"})
  public static void main(String[] args) throws Exception {
    InOutCurrentTest t = new InOutCurrentTest();
    t.setUp("loci.formats.utests.ObjectBasedODEModelMock");
    System.out.println("###\n### XML\n###");
    System.out.println(t.asString);
    t.testValidODENode();
    System.out.println("###\n### Model Objects\n###");
    Map<String, ODEModelObject> objects = t.model.getModelObjects();
    for (Entry<String, ODEModelObject> entry : objects.entrySet())
    {
      System.out.println(String.format(
          "%s -- %s", entry.getKey(), entry.getValue().toString()));
    }
    System.out.println("###\n### References\n###");
    Map<ODEModelObject, List<Reference>> references = t.model.getReferences();
    for (Entry<ODEModelObject, List<Reference>> entry : references.entrySet())
    {
      System.out.println(String.format(
          "%s -- %s", entry.getKey(), entry.getValue().toString()));
    }
  }
}
