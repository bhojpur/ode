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

import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Arc;
import ode.xml.model.BooleanAnnotation;
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
import ode.xml.model.ListAnnotation;
import ode.xml.model.LongAnnotation;
import ode.xml.model.Objective;
import ode.xml.model.Pixels;
import ode.xml.model.Plate;
import ode.xml.model.ROI;
import ode.xml.model.Rectangle;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.TiffData;
import ode.xml.model.TimestampAnnotation;
import ode.xml.model.UUID;
import ode.xml.model.Union;
import ode.xml.model.Well;
import ode.xml.model.WellSample;
import ode.xml.model.XMLAnnotation;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.primitives.PositiveInteger;
import ode.xml.model.primitives.Timestamp;

import ode.units.quantity.Power;
import ode.units.UNITS;

/**
 */
public class ObjectBasedODEModelMock implements ODEModelMock {

  private ODEXMLMetadataRoot ode;

  private StructuredAnnotations annotations;

  public ObjectBasedODEModelMock() {
    ode = new ODEXMLMetadataRoot();
    annotations = new StructuredAnnotations();
    ode.addImage(makeImage());
    ode.addPlate(makePlate());
    ode.addInstrument(makeInstrument());
    ode.addROI(makeROI());
    ode.setStructuredAnnotations(annotations);
  }

  @Override
  public ODEXMLMetadataRoot getRoot() {
    return ode;
  }

  private Image makeImage() {
    // Create <Image/>
    Image image = new Image();
    image.setID(InOutCurrentTest.IMAGE_ID);
    ListAnnotation listAnnotation = new ListAnnotation();
    listAnnotation.setID(InOutCurrentTest.IMAGE_LIST_ANNOTATION_ID);
    listAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    annotations.addListAnnotation(listAnnotation);
    BooleanAnnotation annotation = new BooleanAnnotation();
    annotation.setID(InOutCurrentTest.IMAGE_ANNOTATION_ID);
    annotation.setValue(InOutCurrentTest.IMAGE_ANNOTATION_VALUE);
    annotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    listAnnotation.linkAnnotation(annotation);
    image.linkAnnotation(listAnnotation);
    annotations.addBooleanAnnotation(annotation);
    // Create <Pixels/>
    Pixels pixels = new Pixels();
    pixels.setID(InOutCurrentTest.PIXELS_ID);
    pixels.setSizeX(new PositiveInteger(InOutCurrentTest.SIZE_X));
    pixels.setSizeY(new PositiveInteger(InOutCurrentTest.SIZE_Y));
    pixels.setSizeZ(new PositiveInteger(InOutCurrentTest.SIZE_Z));
    pixels.setSizeC(new PositiveInteger(InOutCurrentTest.SIZE_C));
    pixels.setSizeT(new PositiveInteger(InOutCurrentTest.SIZE_T));
    pixels.setDimensionOrder(InOutCurrentTest.DIMENSION_ORDER);
    pixels.setType(InOutCurrentTest.PIXEL_TYPE);
    // Create <TiffData/>
    TiffData tiffData = new TiffData();
    // Create <UUID/>
    UUID uuid = new UUID();
    uuid.setValue(InOutCurrentTest.TIFF_DATA_UUID);
    tiffData.setUUID(uuid);
    pixels.addTiffData(tiffData);
    // Create <Channel/> under <Pixels/>
    for (int i = 0; i < InOutCurrentTest.SIZE_C; i++) {
      Channel channel = new Channel();
      channel.setID("Channel:" + i);
      if (i == 0) {
        XMLAnnotation channelAnnotation = new XMLAnnotation();
        channelAnnotation.setID(InOutCurrentTest.CHANNEL_ANNOTATION_ID);
        channelAnnotation.setValue(InOutCurrentTest.CHANNEL_ANNOTATION_VALUE);
        channelAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
        channel.linkAnnotation(channelAnnotation);
        annotations.addXMLAnnotation(channelAnnotation);
      }
      pixels.addChannel(channel);
    }
    // Put <Pixels/> under <Image/>
    image.setPixels(pixels);
    return image;
  }

  private Instrument makeInstrument() {
    // Create <Instrument/>
    Instrument instrument = new Instrument();
    instrument.setID(InOutCurrentTest.INSTRUMENT_ID);
    // Create <Detector/> under <Instrument/>
    Detector detector = new Detector();
    detector.setID(InOutCurrentTest.DETECTOR_ID);
    detector.setModel(InOutCurrentTest.DETECTOR_MODEL);
    CommentAnnotation detectorAnnotation = new CommentAnnotation();
    detectorAnnotation.setID(InOutCurrentTest.DETECTOR_ANNOTATION_ID);
    detectorAnnotation.setValue(InOutCurrentTest.DETECTOR_ANNOTATION_VALUE);
    detectorAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    detector.linkAnnotation(detectorAnnotation);
    annotations.addCommentAnnotation(detectorAnnotation);
    instrument.addDetector(detector);

    // Create <Laser/> under <Instrument/>
    Laser laser = new Laser();
    laser.setID(InOutCurrentTest.LIGHTSOURCE_LASER_ID);
    laser.setModel(InOutCurrentTest.LIGHTSOURCE_LASER_MODEL);
    laser.setType(InOutCurrentTest.LASER_TYPE);
    laser.setPower(new Power(InOutCurrentTest.LIGHTSOURCE_LASER_POWER, UNITS.MILLIWATT));
    CommentAnnotation laserAnnotation = new CommentAnnotation();
    laserAnnotation.setID(InOutCurrentTest.LIGHTSOURCE_LASER_ANNOTATION_ID);
    laserAnnotation.setValue(InOutCurrentTest.LIGHTSOURCE_LASER_ANNOTATION_VALUE);
    laserAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    laser.linkAnnotation(laserAnnotation);
    annotations.addCommentAnnotation(laserAnnotation);
    // with a <Pump/>
    Laser laserPump = new Laser();
    laserPump.setID(InOutCurrentTest.LIGHTSOURCE_PUMP_ID);
    laserPump.setModel(InOutCurrentTest.LIGHTSOURCE_PUMP_MODEL);
    laserPump.setType(InOutCurrentTest.LASER_TYPE);
    laserPump.setPower(new Power(InOutCurrentTest.LIGHTSOURCE_PUMP_POWER, UNITS.MILLIWATT));
    
    laser.linkPump(laserPump);
    
    instrument.addLightSource(laser);
    instrument.addLightSource(laserPump);
    
    // Create <Arc/> under <Instrument/>
    Arc arc = new Arc();
    arc.setID(InOutCurrentTest.LIGHTSOURCE_ARC_ID);
    arc.setModel(InOutCurrentTest.LIGHTSOURCE_ARC_MODEL);
    arc.setType(InOutCurrentTest.ARC_TYPE);
    arc.setPower(new Power(InOutCurrentTest.LIGHTSOURCE_ARC_POWER, UNITS.MILLIWATT));
    CommentAnnotation arcAnnotation = new CommentAnnotation();
    arcAnnotation.setID(InOutCurrentTest.LIGHTSOURCE_ARC_ANNOTATION_ID);
    arcAnnotation.setValue(InOutCurrentTest.LIGHTSOURCE_ARC_ANNOTATION_VALUE);
    arcAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    arc.linkAnnotation(arcAnnotation);
    annotations.addCommentAnnotation(arcAnnotation);
    instrument.addLightSource(arc);
    
    // Create <Filament/> under <Instrument/>
    Filament filament = new Filament();
    filament.setID(InOutCurrentTest.LIGHTSOURCE_FILAMENT_ID);
    filament.setModel(InOutCurrentTest.LIGHTSOURCE_FILAMENT_MODEL);
    filament.setType(InOutCurrentTest.FILAMENT_TYPE);
    filament.setPower(new Power(InOutCurrentTest.LIGHTSOURCE_FILAMENT_POWER, UNITS.MILLIWATT));
    CommentAnnotation filamentAnnotation = new CommentAnnotation();
    filamentAnnotation.setID(InOutCurrentTest.LIGHTSOURCE_FILAMENT_ANNOTATION_ID);
    filamentAnnotation.setValue(InOutCurrentTest.LIGHTSOURCE_FILAMENT_ANNOTATION_VALUE);
    filamentAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    filament.linkAnnotation(filamentAnnotation);
    annotations.addCommentAnnotation(filamentAnnotation);
    instrument.addLightSource(filament);

    // Create <LightEmittingDiode/> under <Instrument/>
    LightEmittingDiode led = new LightEmittingDiode();
    led.setID(InOutCurrentTest.LIGHTSOURCE_LED_ID);
    led.setModel(InOutCurrentTest.LIGHTSOURCE_LED_MODEL);
    led.setPower(new Power(InOutCurrentTest.LIGHTSOURCE_LED_POWER, UNITS.MILLIWATT));
    CommentAnnotation ledAnnotation = new CommentAnnotation();
    ledAnnotation.setID(InOutCurrentTest.LIGHTSOURCE_LED_ANNOTATION_ID);
    ledAnnotation.setValue(InOutCurrentTest.LIGHTSOURCE_LED_ANNOTATION_VALUE);
    ledAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    led.linkAnnotation(ledAnnotation);
    annotations.addCommentAnnotation(ledAnnotation);
    instrument.addLightSource(led);

    // Create <Dichroic/> under <Instrument/>
    Dichroic dichroic = new Dichroic();
    dichroic.setID(InOutCurrentTest.DICHROIC_ID);
    dichroic.setSerialNumber(InOutCurrentTest.DICHROIC_SN);
    CommentAnnotation dichroicAnnotation = new CommentAnnotation();
    dichroicAnnotation.setID(InOutCurrentTest.DICHROIC_ANNOTATION_ID);
    dichroicAnnotation.setValue(InOutCurrentTest.DICHROIC_ANNOTATION_VALUE);
    dichroicAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    dichroic.linkAnnotation(dichroicAnnotation);
    annotations.addCommentAnnotation(dichroicAnnotation);
    // Create <FilterSet/> under <Dichroic/>
    FilterSet filterSet = new FilterSet();
    filterSet.setID(InOutCurrentTest.FILTERSET_ID);
    filterSet.setLotNumber(InOutCurrentTest.FILTERSET_LOT);
    filterSet.linkDichroic(dichroic);

    Filter emFilter = new Filter();
    Filter exFilter = new Filter();
    // Create <Objective/> under <Instrument/>
    Objective objective = new Objective();
    objective.setID(InOutCurrentTest.OBJECTIVE_ID);
    objective.setModel(InOutCurrentTest.OBJECTIVE_MODEL);
    CommentAnnotation objectiveAnnotation = new CommentAnnotation();
    objectiveAnnotation.setID(InOutCurrentTest.OBJECTIVE_ANNOTATION_ID);
    objectiveAnnotation.setValue(InOutCurrentTest.OBJECTIVE_ANNOTATION_VALUE);
    objectiveAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    objective.linkAnnotation(objectiveAnnotation);
    annotations.addCommentAnnotation(objectiveAnnotation);

    emFilter.setID(InOutCurrentTest.EM_FILTER_ID);
    emFilter.setType(InOutCurrentTest.EM_FILTER_TYPE);
    exFilter.setID(InOutCurrentTest.EX_FILTER_ID);
    exFilter.setType(InOutCurrentTest.EX_FILTER_TYPE);
    CommentAnnotation emFilterAnnotation = new CommentAnnotation();
    emFilterAnnotation.setID(InOutCurrentTest.EM_FILTER_ANNOTATION_ID);
    emFilterAnnotation.setValue(InOutCurrentTest.EM_FILTER_ANNOTATION_VALUE);
    emFilterAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    emFilter.linkAnnotation(emFilterAnnotation);
    annotations.addCommentAnnotation(emFilterAnnotation);

    instrument.addFilter(emFilter);
    instrument.addFilter(exFilter);
    instrument.addObjective(objective);

    filterSet.linkEmissionFilter(emFilter);
    filterSet.linkExcitationFilter(exFilter);
    filterSet.linkDichroic(dichroic);
    instrument.addFilterSet(filterSet);
    instrument.addDichroic(dichroic);

    CommentAnnotation instrumentAnnotation = new CommentAnnotation();
    instrumentAnnotation.setID(InOutCurrentTest.INSTRUMENT_ANNOTATION_ID);
    instrumentAnnotation.setValue(InOutCurrentTest.INSTRUMENT_ANNOTATION_VALUE);
    instrumentAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    instrument.linkAnnotation(instrumentAnnotation);
    annotations.addCommentAnnotation(instrumentAnnotation);

    // link Instrument to the first Image
    Image image = ode.getImage(0);
    image.linkInstrument(instrument);

    return instrument;
  }

  private Plate makePlate() {
    Plate plate = new Plate();
    plate.setID(InOutCurrentTest.PLATE_ID);
    plate.setRows(InOutCurrentTest.WELL_ROWS);
    plate.setColumns(InOutCurrentTest.WELL_COLS);
    plate.setRowNamingConvention(InOutCurrentTest.WELL_ROW);
    plate.setColumnNamingConvention(InOutCurrentTest.WELL_COL);

    TimestampAnnotation plateAnnotation = new TimestampAnnotation();
    plateAnnotation.setID(InOutCurrentTest.PLATE_ANNOTATION_ID);
    plateAnnotation.setValue(new Timestamp(InOutCurrentTest.PLATE_ANNOTATION_VALUE));
    plateAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    plate.linkAnnotation(plateAnnotation);
    annotations.addTimestampAnnotation(plateAnnotation);

    int wellSampleIndex = 0;
    for (int row=0; row<InOutCurrentTest.WELL_ROWS.getValue(); row++) {
      for (int col=0; col<InOutCurrentTest.WELL_COLS.getValue(); col++) {
        Well well = new Well();
        well.setID(String.format("Well:%d_%d", row, col));
        well.setRow(new NonNegativeInteger(row));
        well.setColumn(new NonNegativeInteger(col));

        if (row == 0 && col == 0) {
          LongAnnotation annotation = new LongAnnotation();
          annotation.setID(InOutCurrentTest.WELL_ANNOTATION_ID);
          annotation.setValue(InOutCurrentTest.WELL_ANNOTATION_VALUE);
          annotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
          well.linkAnnotation(annotation);
          annotations.addLongAnnotation(annotation);
        }

        WellSample sample = new WellSample();
        sample.setID(String.format("WellSample:%d_%d", row, col));
        sample.setIndex(new NonNegativeInteger(wellSampleIndex));
        sample.linkImage(ode.getImage(0));
        well.addWellSample(sample);
        plate.addWell(well);
        wellSampleIndex++;
      }
    }

    return plate;
  }

  private ROI makeROI() {
    ROI roi = new ROI();
    roi.setID(InOutCurrentTest.ROI_ID);

    CommentAnnotation roiAnnotation = new CommentAnnotation();
    roiAnnotation.setID(InOutCurrentTest.ROI_ANNOTATION_ID);
    roiAnnotation.setValue(InOutCurrentTest.ROI_ANNOTATION_VALUE);
    roiAnnotation.setNamespace(InOutCurrentTest.GENERAL_ANNOTATION_NAMESPACE);
    roi.linkAnnotation(roiAnnotation);
    annotations.addCommentAnnotation(roiAnnotation);

    Union shapeUnion = new Union();
    Rectangle rect = new Rectangle();
    rect.setID(InOutCurrentTest.SHAPE_ID);
    rect.setX(InOutCurrentTest.RECTANGLE_X);
    rect.setY(InOutCurrentTest.RECTANGLE_Y);
    rect.setWidth(InOutCurrentTest.RECTANGLE_WIDTH);
    rect.setHeight(InOutCurrentTest.RECTANGLE_HEIGHT);

    shapeUnion.addShape(rect);
    roi.setUnion(shapeUnion);

    return roi;
  }
}
