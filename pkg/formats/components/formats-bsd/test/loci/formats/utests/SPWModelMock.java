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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ode.xml.model.Arc;
import ode.xml.model.BinData;
import ode.xml.model.Channel;
import ode.xml.model.CommentAnnotation;
import ode.xml.model.Detector;
import ode.xml.model.Dichroic;
import ode.xml.model.Filament;
import ode.xml.model.Filter;
import ode.xml.model.FilterSet;
import ode.xml.model.Image;
import ode.xml.model.Instrument;
import ode.xml.model.Laser;
import ode.xml.model.LightEmittingDiode;
import ode.xml.model.LightPath;
import ode.xml.model.LightSourceSettings;
import ode.xml.model.ODE;
import ode.xml.model.Objective;
import ode.xml.model.ObjectiveSettings;
import ode.xml.model.Pixels;
import ode.xml.model.Plate;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.Well;
import ode.xml.model.WellSample;
import ode.xml.model.enums.ArcType;
import ode.xml.model.enums.Compression;
import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.FilamentType;
import ode.xml.model.enums.FilterType;
import ode.xml.model.enums.LaserType;
import ode.xml.model.enums.NamingConvention;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.NonNegativeLong;
import ode.xml.model.primitives.PositiveInteger;

import ode.units.quantity.Power;
import ode.units.UNITS;

public class SPWModelMock implements ModelMock {

  private ODE ode;

  private StructuredAnnotations annotations;

  public static final String GENERAL_ANNOTATION_NAMESPACE =
    "test-ode-InOut201004-namespace";

  public static final Integer SIZE_X = 24;

  public static final Integer SIZE_Y = 24;

  public static final Integer SIZE_Z = 1;

  public static final Integer SIZE_C = 3;

  public static final Integer SIZE_T = 1;

  public static final DimensionOrder DIMENSION_ORDER = DimensionOrder.XYZCT;

  public static final PixelType PIXEL_TYPE = PixelType.UINT16;

  public static final Integer BYTES_PER_PIXEL = 2;

  public static final String INSTRUMENT_ID = "Instrument:0";

  public static final String PLATE_ID = "Plate:0";

  public static final String PLATE_NAME = "THE Plate";

  public static final String DETECTOR_ID = "Detector:0";

  public static final String LIGHTSOURCE_LASER_ID = "LightSource:0";

  public static final String LIGHTSOURCE_PUMP_ID = "LightSource:1";

  public static final String LIGHTSOURCE_ARC_ID = "LightSource:2";

  public static final String LIGHTSOURCE_FILAMENT_ID = "LightSource:3";

  public static final String LIGHTSOURCE_LED_ID = "LightSource:4";

  public static final String DICHROIC_ID = "Dichroic:0";

  public static final String FILTERSET_ID = "FilterSet:0";

  public static final String EM_FILTER_ID = "Filter:0";

  public static final String EX_FILTER_ID = "Filter:1";

  public static final String OBJECTIVE_ID = "Objective:0";

  public static final String OTF_ID = "OTF:0";

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

  public static final PixelType OTF_PIXELTYPE = PixelType.FLOAT;

  public static final Double LIGHTSOURCE_LASER_POWER = 1000.0;

  public static final Double LIGHTSOURCE_PUMP_POWER = 100.0;

  public static final Double LIGHTSOURCE_ARC_POWER = 500.0;

  public static final Double LIGHTSOURCE_FILAMENT_POWER = 200.0;

  public static final Double LIGHTSOURCE_LED_POWER = 10.0;

  public static final LaserType LASER_TYPE = LaserType.DYE;
  
  public static final ArcType ARC_TYPE = ArcType.HGXE;

  public static final FilamentType FILAMENT_TYPE = FilamentType.HALOGEN;

  public static final Integer OTF_SIZE_X = 512;

  public static final Integer OTF_SIZE_Y = 512;

  public static final Boolean OTF_OPTICAL_AXIS_AVERAGED = Boolean.FALSE;

  public static final PositiveInteger WELL_ROWS = new PositiveInteger(16);

  public static final PositiveInteger WELL_COLS = new PositiveInteger(24);

  public static final NamingConvention WELL_ROW = NamingConvention.LETTER;

  public static final NamingConvention WELL_COL = NamingConvention.NUMBER;

  public static final String PLANE =
    "ZrXEfwslJ9N1nDrbtxxWh4fRHo4w8nZ2N0I74Lgj9oIKN9qrPbBK24z+w+9zYzRQ" +
    "WJXfEwwAKXgV4Z1jCPhE9woGjJaarHTsFwy21nF2IoJDkd3L/zSWMSVk508+jpxV" +
    "4t5p93HE1uE4K34WCVEeeZ1dSRli/b7/6RhF56DjdB6KboGly3zuN2/eZt9uJ2Mh" +
    "HZtktzpjFtn2mhf4i7iggpQyWx74xvFs9VxXQf1QoxN1KcTGXbdfPNoj3qmzz7Wm" +
    "8/iXXw7wpOrC2MRcbt98VH4UaQxFgu6VPer73JAS+r2Kd2C67ZFbweyR/LCoUiic" +
    "h866SrwJk3IrTD9AlnGO6SjHIz27yWVh1omr36H1qOuD4ULSknM2txm4FrB02gxH" +
    "WHbgaJWGT02eT1nwGNXygHe7gdYVP8o6Ms9sT/nBwhoMK8NuQINx7KJP/jTP0p5g" +
    "NjEHZeAN1To9Qp3AF3jaWK2671Dyy/l9BBRMhD3gEqXJ12ZXZ0par2pvqVtMcbpA" +
    "Zk96GKsSWDQP48yDkNYTG7RDBMzRJxiem7eifg1gpUP1rmmaNEu12+0wclsGBUeH" +
    "1d9HiN+rDnppycrVQIgvKbXKlUkQH230IYHDESKnlLCZALLJuRuAT5qsNri5950O" +
    "lphUxeYAnNfUkXYRUHGGnGXw58nmnBCp7iuHDC8AJdCRyK+0wk/xtt6EeADkPs9Q" +
    "q90H2kXvvGVbcL03IV1mb0PkdqWg2ovrkSLXKhLXb65ruPPz43TAT9xv4QJdmFqJ" +
    "baMHta8Wd1Fs9cffChHWJT3RS9U8VrhGlBB5+1D9PMlqLruYtp7ulUpMSJFOKkbo" +
    "yXoECSzJuzknqP2Cj1KWrNk+gSsnAlq5zko6KUyPXWMBVgPGNrXR+ivtIXmyQGu5" +
    "jSTuA+S+ogaPraRPQELmmuQ2wcoWI7O9Vpht1tFmgXkrdqCTD7+JwdXlbHSoRz3t" +
    "i9dpJY+LyKBisuKcDgdxWulwtydNliNSKKyt7qGC2B90VLo+XsYLLEYU+w95l2ZO" +
    "umqBquStdKntlReWtCDu8HfbK6AryfZXL5hqMTdqFubcXl4n5ZfBNtHaru8/LswN" +
    "VGua9VJUsvZV9rMniNwoU7Ev+oLc/0SZkJrwL/r+9Jl5k02DRymhE4XISJ3UXcnt" +
    "2K57w/OmIJK3HzznrIXgPJA9Nq7M6XjXDDXuBF08709iSEfOWWZ0Yz5ySoszOlSO" +
    "0OGoRYv8X9xUeOfWi4oizQeSOj2ZTXegqZLxj/g8Y7ykyDkG4NsMS0Kx2fZvxqKE" +
    "9EdUAXMvDN09X0fKdurqYqPBsRq79Id8YIJhamEP969OjHs9VXIETMmCkoUz2//7" +
    "BfeaCUzv5c61/asdOR6CJ4ANUX7hQA7hlTk8qllaaLIEWQyGeaDoaw9b5xq0Adhw" +
    "OZSeCKNIyQVpApdCOnXYuZVoTBNDdW7/7OPZD2uyS9gZ+7JGmuoV9/gRZT72oAQs" +
    "4++/GpC5h6uOx9Rt5265siOZjfYYX++/qUX8M5Fs9whPwL8NqrJ4qZrUbTYUzQaI";

  /** XML namespace. */
  public static final String XML_NS =
    "http://www.bhojpur.net/Schemas/ODE/2016-06";

  /** XSI namespace. */
  public static final String XSI_NS =
    "http://www.w3.org/2001/XMLSchema-instance";

  /** XML schema location. */
  public static final String SCHEMA_LOCATION =
    "http://www.bhojpur.net/Schemas/ODE/2016-06/ode.xsd";

  public SPWModelMock(boolean makeLightSources) {
    ode = new ODE();
    annotations = new StructuredAnnotations();
    ode.addInstrument(makeInstrument(makeLightSources));
    for (int i = 0; i < WELL_ROWS.getValue() * WELL_COLS.getValue(); i++) {
      ode.addImage(makeImage(i));
    }
    ode.addPlate(makePlate());
    ode.setStructuredAnnotations(annotations);
  }

  @Override
  public ODE getRoot() {
    return ode;
  }

  private Image makeImage(int index) {
    // <Instrument/> for later linking, etc.
    Instrument instrument = ode.getInstrument(0);
    // Create <Image/>
    Image image = new Image();
    image.setID("Image:" + index);
    CommentAnnotation commentAnnotation = new CommentAnnotation();
    commentAnnotation.setID("Annotation:" + index);
    commentAnnotation.setNamespace(GENERAL_ANNOTATION_NAMESPACE);
    commentAnnotation.setValue("Image:" + index + " annotation.");
    annotations.addCommentAnnotation(commentAnnotation);
    image.linkAnnotation(commentAnnotation);
    // Create <Pixels/>
    Pixels pixels = new Pixels();
    pixels.setID("Pixels:" + index);
    pixels.setSizeX(new PositiveInteger(SIZE_X));
    pixels.setSizeY(new PositiveInteger(SIZE_Y));
    pixels.setSizeZ(new PositiveInteger(SIZE_Z));
    pixels.setSizeC(new PositiveInteger(SIZE_C));
    pixels.setSizeT(new PositiveInteger(SIZE_T));
    pixels.setDimensionOrder(DIMENSION_ORDER);
    pixels.setType(PIXEL_TYPE);

    // Create <BinData/>
    for (int i = 0; i < SIZE_Z * SIZE_C * SIZE_T; i++) {
      BinData binData = new BinData();
      binData.setBigEndian(false);
      binData.setCompression(Compression.NONE);
      binData.setLength(new NonNegativeLong(
          (long) (SIZE_X * SIZE_Y * BYTES_PER_PIXEL)));
      pixels.addBinData(binData);
    }
    // Create <Channel/> under <Pixels/>
    for (int i = 0; i < SIZE_C; i++) {
      Channel channel = new Channel();
      channel.setID("Channel:" + i);
      // Create <LightSourceSettings/> and link to <Channel/>
      LightSourceSettings settings = new LightSourceSettings();
      settings.setID(LIGHTSOURCE_LASER_ID);
      channel.setLightSourceSettings(settings);
      // Create <LightPath/> and link to <Channel/>
      LightPath lightPath = new LightPath();
      lightPath.linkEmissionFilter(instrument.getFilter(0));
      lightPath.linkExcitationFilter(instrument.getFilter(1));
      channel.setLightPath(lightPath);
      pixels.addChannel(channel);
    }
    // Put <Pixels/> under <Image/>
    image.setPixels(pixels);

    // Link <Instrument/> to <Image/>
    image.linkInstrument(instrument);
    // Create <ObjectiveSettings/> and link to <Image/>
    ObjectiveSettings settings = new ObjectiveSettings();
    settings.setID(OBJECTIVE_ID);
    image.setObjectiveSettings(settings);
    return image;
  }

  private Instrument makeInstrument(boolean makeLightSources) {
    // Create <Instrument/>
    Instrument instrument = new Instrument();
    instrument.setID(INSTRUMENT_ID);
    // Create <Detector/> under <Instrument/>
    Detector detector = new Detector();
    detector.setID(DETECTOR_ID);
    detector.setModel(DETECTOR_MODEL);
    instrument.addDetector(detector);

    if (makeLightSources) {
      // Create <Laser/> under <Instrument/>
      Laser laser = new Laser();
      laser.setID(LIGHTSOURCE_LASER_ID);
      laser.setModel(LIGHTSOURCE_LASER_MODEL);
      laser.setType(LASER_TYPE);
      laser.setPower(new Power(LIGHTSOURCE_LASER_POWER, UNITS.MILLIWATT));
      // with a <Pump/>
      Laser laserPump = new Laser();
      laserPump.setID(LIGHTSOURCE_PUMP_ID);
      laserPump.setModel(LIGHTSOURCE_PUMP_MODEL);
      laserPump.setType(LASER_TYPE);
      laserPump.setPower(new Power(LIGHTSOURCE_PUMP_POWER, UNITS.MILLIWATT));

      laser.linkPump(laserPump);

      instrument.addLightSource(laser);
      instrument.addLightSource(laserPump);

      // Create <Arc/> under <Instrument/>
      Arc arc = new Arc();
      arc.setID(LIGHTSOURCE_ARC_ID);
      arc.setModel(LIGHTSOURCE_ARC_MODEL);
      arc.setType(ARC_TYPE);
      arc.setPower(new Power(LIGHTSOURCE_ARC_POWER, UNITS.MILLIWATT));
      instrument.addLightSource(arc);

      // Create <Filament/> under <Instrument/>
      Filament filament = new Filament();
      filament.setID(LIGHTSOURCE_FILAMENT_ID);
      filament.setModel(LIGHTSOURCE_FILAMENT_MODEL);
      filament.setType(FILAMENT_TYPE);
      filament.setPower(new Power(LIGHTSOURCE_FILAMENT_POWER, UNITS.MILLIWATT));
      instrument.addLightSource(filament);

      // Create <LightEmittingDiode/> under <Instrument/>
      LightEmittingDiode led = new LightEmittingDiode();
      led.setID(LIGHTSOURCE_LED_ID);
      led.setModel(LIGHTSOURCE_LED_MODEL);
      led.setPower(new Power(LIGHTSOURCE_LED_POWER, UNITS.MILLIWATT));
      instrument.addLightSource(led);
    }

    // Create <Dichroic/> under <Instrument/>
    Dichroic dichroic = new Dichroic();
    dichroic.setID(DICHROIC_ID);
    dichroic.setSerialNumber(DICHROIC_SN);
    // Create <FilterSet/> under <Dichroic/>
    FilterSet filterSet = new FilterSet();
    filterSet.setID(FILTERSET_ID);
    filterSet.setLotNumber(FILTERSET_LOT);
    filterSet.linkDichroic(dichroic);

    Filter emFilter = new Filter();
    Filter exFilter = new Filter();
    // Create <Objective/> under <Instrument/>
    Objective objective = new Objective();
    objective.setID(OBJECTIVE_ID);
    objective.setModel(OBJECTIVE_MODEL);

    emFilter.setID(EM_FILTER_ID);
    emFilter.setType(EM_FILTER_TYPE);
    exFilter.setID(EX_FILTER_ID);
    exFilter.setType(EX_FILTER_TYPE);

    instrument.addFilter(emFilter);
    instrument.addFilter(exFilter);
    instrument.addObjective(objective);

    filterSet.linkEmissionFilter(emFilter);
    filterSet.linkExcitationFilter(exFilter);
    filterSet.linkDichroic(dichroic);
    instrument.addFilterSet(filterSet);
    instrument.addDichroic(dichroic);

    return instrument;
  }

  private Plate makePlate() {
    Plate plate = new Plate();
    plate.setName(PLATE_NAME);
    plate.setID(PLATE_ID);
    plate.setRows(WELL_ROWS);
    plate.setColumns(WELL_COLS);
    plate.setRowNamingConvention(WELL_ROW);
    plate.setColumnNamingConvention(WELL_COL);

    int wellSampleIndex = 0;
    for (int row=0; row<WELL_ROWS.getValue(); row++) {
      for (int col=0; col<WELL_COLS.getValue(); col++) {
        Well well = new Well();
        well.setID(String.format("Well:%d_%d", row, col));
        well.setRow(new NonNegativeInteger(row));
        well.setColumn(new NonNegativeInteger(col));

        WellSample sample = new WellSample();
        sample.setID(String.format("WellSample:%d_%d", row, col));
        sample.setIndex(new NonNegativeInteger(wellSampleIndex));
        sample.linkImage(ode.getImage(wellSampleIndex));
        well.addWellSample(sample);
        plate.addWell(well);
        wellSampleIndex++;
      }
    }

    return plate;
  }

  public static String asString(Document document)
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

  public static void postProcess(Element root, Document document,
                                 boolean withBinData) {
    root.setAttribute("xmlns", XML_NS);
    root.setAttribute("xmlns:xsi", XSI_NS);
    root.setAttribute("xsi:schemaLocation", XML_NS + " " + SCHEMA_LOCATION);
    document.appendChild(root);
    // Put the planar data into each <BinData/>
    if (withBinData) {
      NodeList binDataNodes = document.getElementsByTagName("BinData");
      for (int i = 0; i < binDataNodes.getLength(); i++) {
        Node binDataNode = binDataNodes.item(i);
        binDataNode.setTextContent(PLANE);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    SPWModelMock mock = new SPWModelMock(false);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Document document = parser.newDocument();
    // Produce a valid ODE DOM element hierarchy
    Element root = mock.ode.asXMLElement(document);
    SPWModelMock.postProcess(root, document, true);
    // Produce string XML
    OutputStream outputStream = new FileOutputStream(args[0]);
    outputStream.write(SPWModelMock.asString(document).getBytes());
  }

}
