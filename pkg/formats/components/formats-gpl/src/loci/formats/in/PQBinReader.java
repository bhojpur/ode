package loci.formats.in;

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

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.MetadataStore;
import ode.units.quantity.Length;

/**
 * PQBinReader is the file format reader for PicoQuant .bin files.
 *
 * Please Note: This format holds FLIM data arranged so that each decay is stored contiguously. 
 * Therefore, as in other FLIM format readers e.g. SDTReader.java, on the first call to openBytes
 * the whole data cube ( x,y,t) (NB actually t not real-time T) is loaded from the file and buffered
 * On further calls to openBytes the appropriate 2D (x,y)plane (timebin) is returned from this buffer.
 * This is in the interest of significantly improved  performance when all the planes are requested one after another.
 * There will be a performance cost if a single plane is requested but this is highly unlikely for FLIM data.
 * In order to limit the size of the buffer, beyond a certain size threshold only a subset of planes (a Block) are 
 * retained in the buffer.
 */
public class PQBinReader extends FormatReader {

  // -- Constants --

  public static final int HEADER_SIZE = 20;

  // -- Fields --
  /*
   * Number of time bins in lifetime histogram. 
   */
  protected int timeBins;

  /*
   * Array to hold re-ordered data for all the timeBins
   */
  protected byte[] dataStore = null;

  /**
   * Block of time bins currently stored for faster loading.
   */
  protected int currentBlock;
  
  /**
   * No of timeBins pre-loaded as a block
  */
  protected int blockLength ;  
  
  
  

  // -- Constructor --
  /**
   * Constructs a new PQBin reader.
   */
  public PQBinReader() {
    super("PicoQuant Bin", "bin");
    domains = new String[]{FormatTools.FLIM_DOMAIN};
    suffixSufficient = false;
  }

  // -- IFormatReader API methods --
  
  /* @see loci.formats.IFormatReader#isThisType(RandomAccessInputStream) */
  @Override
  public boolean isThisType(RandomAccessInputStream stream) throws IOException {
    int bpp = FormatTools.getBytesPerPixel(FormatTools.UINT32);
    stream.order(true);

    // Header
    long fileLength;
    int sizeX;
    int sizeY;
    int sizeT;
    try {
      fileLength = stream.length();
      sizeX = stream.readInt();
      sizeY = stream.readInt();
      stream.readFloat();  // resolution of time axis of every Decay (in ns)
      sizeT = stream.readInt();
    } catch (java.io.EOFException eof) {
      return false;
    }

    return (sizeX * sizeY * sizeT * bpp) + HEADER_SIZE == fileLength;
  }

  /**
   * @see loci.formats.IFormatReader#openBytes(int, byte[], int, int, int, int)
   */
  @Override
  public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    FormatTools.checkPlaneParameters(this, no, buf.length, x, y, w, h);
    
    
    int sizeX = getSizeX();
    int sizeY = getSizeY();
    int bpp = FormatTools.getBytesPerPixel(getPixelType());
    boolean little = isLittleEndian();
    
    int planeSize = sizeX * sizeY * timeBins * bpp;
    int timeBin = no;

    int binSize = sizeX * sizeY  * bpp;  // size in Bytes of a single 2D timebin.
   
    int blockSize = sizeX * sizeY * blockLength * bpp;
    
    // pre-load data for performance
    if (dataStore == null) {
      dataStore = new byte[blockSize];
      currentBlock = -1;
    }
      
    if (timeBin/blockLength != currentBlock)  {
      
      currentBlock = timeBin/blockLength;
      
      // A subset of whole timebins (a Block) is  copied into storage
      // to allow different sub-plane sizes to be used for different timebins
      
      byte[] rowBuf = new byte[bpp * timeBins * sizeX];
      in.seek(HEADER_SIZE);
      
      int endOfBlock =  (currentBlock + 1) * blockLength;
      int storeLength;
      if (endOfBlock > timeBins) {
        storeLength = timeBins - (currentBlock * blockLength);
      }
      else {
        storeLength = blockLength;
      }
              
      for (int row = 0; row < sizeY; row++) {
        in.read(rowBuf);
        for (int col = 0; col < sizeX; col++) {
          // set output to first pixel of this row in 2D plane
          // corresponding to zeroth timeBin
          int output = ((row * sizeX) + col) * bpp;
          int input = ((col * timeBins) + (currentBlock * blockLength)) * bpp;
          // copy subset of decay into buffer.
          for (int t = 0; t < storeLength; t++) {
            for (int bb = 0; bb < bpp; bb++) {
              dataStore[output + bb] = rowBuf[input + bb];
            }
            output += binSize;
            input += bpp;
          }
        }
      }
    }
    // dataStore loaded

    // copy 2D plane  from dataStore  into buf
    int iLineSize = sizeX * bpp;
    int oLineSize = w * bpp;
    // offset to correct timebin yth line and xth pixel

    int binInStore = timeBin - (currentBlock * blockLength);
    
    int input = (binSize * binInStore) + (y * iLineSize) + (x * bpp);
    int output = 0;

    for (int row = 0; row < h; row++) {
      System.arraycopy(dataStore, input, buf, output, oLineSize);
      input += iLineSize;
      output += oLineSize;
    }

    return buf;
  }

  /* @see loci.formats.IFormatReader#close(boolean) */
  @Override
  public void close(boolean fileOnly) throws IOException {
    super.close(fileOnly);
    if (!fileOnly) {
      // init preLoading
      dataStore = null;
      timeBins = 0;
      currentBlock = -1;
     
    }
  }

  // -- Internal FormatReader API methods --

  /* @see loci.formats.FormatReader#initFile(String) */
  @Override
  protected void initFile(String id) throws FormatException, IOException {
    super.initFile(id);
    in = new RandomAccessInputStream(id);
    
    CoreMetadata m = core.get(0);

    m.littleEndian = true;
    in.order(isLittleEndian());

    LOGGER.info("Reading header PQBin");
   
    // Header
    int sizeX = in.readInt();
    int sizeY = in.readInt();
    m.sizeY = sizeY;
    m.sizeX = sizeX;
    
    float pixResol = in.readFloat();    // Resolution of every Pixel in Image (in ??m)
    m.sizeT = in.readInt();             // Number of DataPoints per Decay
    
    float timeResol = in.readFloat();   // resolution of time axis of every Decay (in ns)
    
    timeBins = m.sizeT;

    m.sizeZ = 1;
    m.sizeC = 1;
    m.dimensionOrder = "XYZCT";
    
    m.pixelType = FormatTools.UINT32;
    m.rgb = false;
    m.imageCount = m.sizeT;
    m.indexed = false;
    m.falseColor = false;
    m.metadataComplete = true;

    m.moduloT.type = FormatTools.LIFETIME;
    m.moduloT.parentType = FormatTools.SPECTRA;
    m.moduloT.typeDescription = "TCSPC";
      
    m.moduloT.start = 0;
    m.moduloT.step = timeResol * 1000;  // Convert to ps
    m.moduloT.end = m.moduloT.step * (m.sizeT - 1);
    m.moduloT.unit = "ps";
    
    int sizeThreshold = 128 * 128 * 1024;  // Arbitararily chosen size limit for buffer
    blockLength = 2048;   //default No of bins in buffer
    
    while (blockLength * sizeX * sizeY > sizeThreshold)
      blockLength = blockLength/2;
    
    if (blockLength > timeBins)
      blockLength = timeBins;
    
    MetadataStore store = makeFilterMetadata();
    MetadataTools.populatePixels(store, this);
    
    Length pRpf = FormatTools.getPhysicalSizeX((double)pixResol);
    store.setPixelsPhysicalSizeX(pRpf, 0);
    store.setPixelsPhysicalSizeY(pRpf, 0);
    
  }

}
