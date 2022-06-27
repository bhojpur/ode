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
import java.util.ArrayList;
import java.util.List;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLService;
import loci.formats.MetadataTools;
import loci.common.DataTools;

import ode.xml.model.enums.DimensionOrder;
import ode.xml.model.enums.EnumerationException;
import ode.xml.model.enums.PixelType;
import ode.xml.model.primitives.PositiveInteger;
import ode.xml.model.primitives.NonNegativeInteger;
import ode.xml.model.enums.NamingConvention;
import ode.units.quantity.Time;
import ode.xml.meta.ODEXMLMetadataRoot;
import ode.xml.model.Image;
import ode.xml.model.Plate;
import ode.xml.model.StructuredAnnotations;
import ode.xml.model.Well;
import ode.xml.model.WellSample;
import ode.xml.model.XMLAnnotation;

/**
 * Example class that shows how to export raw pixel data to ODE-TIFF as a Plate using
 * ODE-Formats version 5.0.3 or later.
 */
public class FileWriteSPW {
  
  private final int pixelType = FormatTools.UINT16;
  private int rows;
  private int cols;
  private int width;
  private int height;
  private int sizet;
  boolean initializationSuccess = false;
  
  private ArrayList<String> delays = null;
  
  private double[] exposureTimes = null;
  
  /** The file writer. */
  private ImageWriter writer = null;

  /** The name of the current output file. */
  private final String outputFile;
  
   /** Description of the plate. */
  private final String plateDescription;
  
  /** ODE metadata **/
  private IMetadata odexml = null;
  
  /** ODEXML service **/
  private ODEXMLService service = null;
  
  /** expected Images array. No of planes that have been written to each Image **/
  private int[] expectedImages;
  
  
 

  /**
   * Construct a new FileWriteSPW that will save to the specified file.
   *
   * @param outputFile the file to which we will export
   * @param plateDescription
   */
  public FileWriteSPW(String outputFile, String plateDescription) {
    this.outputFile = outputFile;    
    this.plateDescription = plateDescription;
   
    File file = new File(outputFile);
 
    // delete file if it exists
    // NB deleting old files seems to be critical 
    if (file.exists())  {
      file.delete();
    }
    this.sizet = 1; // Non-FLIM by default
  }
  
  // Initialisation method for FLIM including  exposure times.
  public boolean init( int[][] nFov, int sizeX, int  sizeY, ArrayList<String> delays, double[] exposureTimes )  {
    
    this.exposureTimes = exposureTimes;
    
    initializationSuccess = init(nFov, sizeX, sizeY, delays);
    
    return initializationSuccess;
  }
  
  // Initialisation method for FLIM without   exposure times.
  public boolean init( int[][] nFov, int sizeX, int  sizeY, ArrayList<String> delays )  {
    
    this.sizet = delays.size();
    setupModulo(delays);
    
    initializationSuccess = init(nFov, sizeX, sizeY);
    
    return initializationSuccess;
    
  }
  
  // Initialisation method for non-FLIM data.
  public boolean init( int[][] nFov, int sizeX, int  sizeY )  {
    this.rows = nFov.length;
    this.cols = nFov[0].length;
    
    width = sizeX;
    height = sizeY;
    
    odexml = initializeMetadata(nFov);
    
    initializationSuccess = initializeWriter(odexml);
    
    return initializationSuccess;
    
  }

  

  /** Save a single Short plane of data.
   * @param plane  data
   * @param series  image no in plate
   * @param index t plane within image
   * @param imageDescription*/
  public void export(short[] plane, int series, int index, String imageDescription) {

    byte[] planeb;
    planeb = DataTools.shortsToBytes(plane, false);
    export(planeb, series, index, imageDescription);

  }
  
  
  /** Save a single byte plane of data.
   * @param plane  data
   * @param series  image no in plate
   * @param index t plane within image*/
  private void export(byte[] plane, int series, int index, String imageDescription) {

    Exception exception = null;

    if (initializationSuccess) {
      if (series != writer.getSeries()) {
        try {
          writer.setSeries(series);
        } catch (FormatException e) {
          exception = e;
        }
      }
      try {
        writer.saveBytes(index, plane);
        if (index == 0) {
          ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexml.getRoot();
          Plate plate = root.getPlate(0);
          Image im = root.getImage(series);
          im.setDescription(imageDescription);
        }
        expectedImages[series]++;
      } catch (FormatException  | IOException e) {
        exception = e;
      }
    }
    if (exception != null) {
      System.err.println("Failed to write data!");
    }
  }
  
  /**
   * Set up the file writer.
   *
   * @param odexml the IMetadata object that is to be associated with the writer
   * @return true if the file writer was successfully initialized; false if an
   *   error occurred
   */
  private boolean initializeWriter(IMetadata odexml) {
    // create the file writer and associate the ODE-XML metadata with it
    writer = new ImageWriter();
    writer.setMetadataRetrieve(odexml);

    Exception exception = null;
    try {
      writer.setId(outputFile);
    }
    catch (FormatException | IOException e) {
      exception = e;
    }
    if (exception != null) {
      System.err.println("Failed to initialize file writer.");
    }
    return exception == null;
  }

  /**
   * Populate the minimum amount of metadata required to export a Plate.
   *
   */
  private IMetadata initializeMetadata(int[][] nFovs) {
    Exception exception = null;
    try {
      // create the ODE-XML metadata storage object
      ServiceFactory factory = new ServiceFactory();
      service = factory.getInstance(ODEXMLService.class);
      ODEXMLMetadata meta = service.createODEXMLMetadata();
      //IMetadata meta = service.createODEXMLMetadata();
      meta.createRoot();
    
      int plateIndex = 0;
      int series = 0;     // count of images
      int well = 0;
  
      meta.setPlateDescription(plateDescription,0); 
 
      meta.setPlateID(MetadataTools.createLSID("Plate", 0), 0);
      
      meta.setPlateRowNamingConvention(NamingConvention.LETTER, 0);
      meta.setPlateColumnNamingConvention(NamingConvention.NUMBER, 0);
      meta.setPlateRows(new PositiveInteger(rows), 0);
      meta.setPlateColumns(new PositiveInteger(cols), 0);
      meta.setPlateName("First test Plate", 0);
      
      PositiveInteger pwidth = new PositiveInteger(width);
      PositiveInteger pheight = new PositiveInteger(height);
        
      for (int row = 0; row  < rows; row++) {
        for (int column = 0; column < cols; column++) {
          
          // set up well
          String wellID = MetadataTools.createLSID("Well", well);
          meta.setWellID(wellID, plateIndex, well);
          meta.setWellRow(new NonNegativeInteger(row), plateIndex, well);
          meta.setWellColumn(new NonNegativeInteger(column), plateIndex, well); 
          
          int nFOV= nFovs[row][column];
          
          for(int fov = 0; fov < nFOV ; fov++)  {
            
            // Create Image NB numberng in the Name goes from 1->n not 0-> n-1
            String imageName = FormatTools.getWellRowName(row) + ":" +
              Integer.toString(column + 1) + ":FOV:" + Integer.toString(fov + 1);
            String imageID = MetadataTools.createLSID("Image", well, fov);
            meta.setImageID(imageID, series);
            meta.setImageName(imageName, series);
            
            String pixelsID = MetadataTools.createLSID("Pixels", well, fov);
            meta.setPixelsID(pixelsID, series);
            
            // specify that the pixel data is stored in big-endian format
            // change 'TRUE' to 'FALSE' to specify little-endian format
            meta.setPixelsBigEndian(Boolean.TRUE, series);

            // specify that the image is stored in ZCT order
            meta.setPixelsDimensionOrder(DimensionOrder.XYZCT, series);

            // specify the pixel type of the image
            meta.setPixelsType(PixelType.fromString(FormatTools.getPixelTypeString(pixelType)), series);

            // specify the dimensions of the image
            meta.setPixelsSizeX(pwidth, series);
            meta.setPixelsSizeY(pheight, series);
            meta.setPixelsSizeZ(new PositiveInteger(1), series);
            meta.setPixelsSizeC(new PositiveInteger(1), series);
            meta.setPixelsSizeT(new PositiveInteger(sizet), series);

            // define each channel and specify the number of samples in the channel
            // the number of samples is 3 for RGB images and 1 otherwise
            String channelID = MetadataTools.createLSID("Channel",well, fov);
            meta.setChannelID(channelID, series,0 );
            meta.setChannelSamplesPerPixel(new PositiveInteger(1), series, 0);
           
            // set sample
            String wellSampleID = MetadataTools.createLSID("WellSample",well, fov);
            meta.setWellSampleID(wellSampleID,0,well,fov);
            // NB sampleIndex here == series ie the image No
            meta.setWellSampleIndex(new NonNegativeInteger(series), 0, well, fov);
            meta.setWellSampleImageRef(imageID, 0, well, fov);
             
            if (exposureTimes != null && exposureTimes.length == sizet)  {
              for (int t = 0; t < sizet; t++)  {
                meta.setPlaneTheT(new NonNegativeInteger(t), series, t);
                meta.setPlaneTheC(new NonNegativeInteger(0), series, t);
                meta.setPlaneTheZ(new NonNegativeInteger(0), series, t);
                meta.setPlaneExposureTime(new Time(exposureTimes[t],ode.units.UNITS.SECOND), series, t);
              } 
            }
            
            // add FLIM ModuloAlongT annotation if required 
            if (delays != null)  {
              CoreMetadata modlo = createModuloAnn(meta);
              service.addModuloAlong(meta, modlo, series);
            }
            
            series++;
          }  //end of samples  
          well++;
        }
      }
      
      expectedImages = new int[series];
      
      //String dump = meta.dumpXML();
      //System.out.println("dump = ");
      //System.out.println(dump);
      return meta;
    }
    
    catch (DependencyException | ServiceException | EnumerationException e) {
      exception = e;
    }

    System.err.println("Failed to populate ODE-XML metadata object.");
    return null;    
      
  }
  
  
  /**
   * Setup delays.
   */
  private boolean setupModulo(ArrayList<String> delays) {
    
    boolean success = false;
    if (delays.size() == sizet)  {
      this.delays = delays;
      success = true;
    }
    return success;
  
  }

  
   /**
   * Add ModuloAlong annotation.
   */
  private CoreMetadata createModuloAnn(IMetadata meta) {

    CoreMetadata modlo = new CoreMetadata();

    modlo.moduloT.type = loci.formats.FormatTools.LIFETIME;
    modlo.moduloT.unit = "ps";
    modlo.moduloT.typeDescription = "Gated";

    modlo.moduloT.labels = new String[sizet];

    for (int i = 0; i < sizet; i++) {
      //System.out.println(delays.get(i));
      modlo.moduloT.labels[i] = delays.get(i);
      
    }

    return modlo;
  }

  
  
  /** Close the file writer. */
  public void cleanup() {
    
    int validPlanes = 1;  // No of planes expected for each image = 1 if not FLIM
    if (delays != null)  {
      validPlanes = sizet; 
    }
    
    ODEXMLMetadataRoot root = (ODEXMLMetadataRoot) odexml.getRoot();
    
     Plate plate = root.getPlate(0);
     StructuredAnnotations anns = root.getStructuredAnnotations();
     
     ArrayList<Image> invalidImages = new ArrayList<>();
    
    // Check that all expected Images have received the correct no of timepoints.
    // if not record those images as being invalid
    for(int i = 0; i < expectedImages.length; i++)  {
      if (expectedImages[i] < validPlanes)  {
        Image im = root.getImage(i);
        invalidImages.add(im);
        // remove modulo Annotation if FLIM
        if (delays != null)  {
          XMLAnnotation ann = (XMLAnnotation) im.getLinkedAnnotation(0);
          anns.removeXMLAnnotation(ann);
        }
      }      
    }
    
  
    // Now remove all limked wellSnmples and then invalid images 
    for(int i = 0; i < invalidImages.size(); i++)  {
      Image im = invalidImages.get(i);
      List<WellSample> list = im.copyLinkedWellSampleList();
      if (!list.isEmpty())  {
        WellSample wellSample = im.getLinkedWellSample(0);
        Well well = wellSample.getWell();
        well.removeWellSample(wellSample);
      }
      root.removeImage(im);
    }
    
 
  
    if (writer != null)  {
      try {
        writer.close();
      }
      catch (IOException e) {
        System.err.println("Failed to close file writer.");
      }
    }
  }
  
}
