package loci.formats.services;

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

import loci.formats.FormatException;

import loci.common.services.Service;

public interface WlzService extends Service {
  
  /**
   * Initializes the service for the given file path.
   * @param     file            File path with which to initialize the service.
   * @throws    IOException
   * @throws     FormatException
   */
  public void open(String file, String rw)
    throws IOException, FormatException;

  /**
   * Gets the text string for when Woolz has not been found.
   */
  public String getNoWlzMsg();

  /**
   * Gets the text string used for the Woolz origin label.
   */
  public String getWlzOrgLabelName();

  /**
   * Gets width in pixels/voxels.
   * @return                    width.
   */
  public int getSizeX();

  /**
   * Gets height in pixels/voxels.
   * @return                    height.
   */
  public int getSizeY();

  /**
   * Gets depth (number of x-y planes) in voxels.
   * @return                    depth.
   */
  public int getSizeZ();

  /**
   * Gets number of channels.
   * @return                    channels.
   */
  public int getSizeC();

  /**
   * Gets number of time samples.
   * @return                    time.
   */
  public int getSizeT();

  /**
   * Gets boolean for whether image is colour or not.
   * @return                    true if image is colour.
   */
  public boolean isRGB();

  /**
   * Gets the image pixel type.
   * @return                    pixel type.
   */
  public int     getPixelType();

  /**
   * Gets voxel width.
   * @return                    voxel width.
   */
  public double getVoxSzX();

  /**
   * Gets voxel height.
   * @return                    voxel height.
   */
  public double getVoxSzY();

  /**
   * Gets voxel depth.
   * @return                    voxel depth.
   */
  public double getVoxSzZ();

  /**
   * Gets column origin.
   * @return                    column origin.
   */
  public double getOrgX();

  /**
   * Gets line origin.
   * @return                    line origin.
   */
  public double getOrgY();

  /**
   * Gets plane origin.
   * @return                    plane origin.
   */
  public double getOrgZ();

  /**
   * Gets supported pixel types.
   * @return                    array of supported pixel types.
   */
  public int[] getSupPixelTypes();

  /**
   * Sets up the service, which must have already been opened for writing.
   * @param     orgX            x origin.
   * @param     orgY            y origin.
   * @param     orgZ            z origin (set to 0 for 2D).
   * @param     pixSzX          width.
   * @param     pixSzY          height.
   * @param     pixSzZ          depth (number of planes, set to 1 for 2D).
   * @param     pixSzC          number of channels.
   * @param     pixSzT          number of time samples.
   * @param     voxSzX          pixel/voxel width.
   * @param     voxSzY          pixel/voxel heigth.
   * @param     voxSzZ          voxel deoth.
   * @param     gType           image value type.
   * @throws    FormatException
   */
  public void setupWrite(int orgX, int orgY, int orgZ,
                         int pixSzX, int pixSzY, int pixSzZ,
                         int pixSzC, int pixSzT,
                         double voxSzX, double voxSzY, double voxSzZ,
                         int gType)
    throws FormatException;

  /**
   * Closes the file.
   */
  public void close()
    throws IOException;

  /**
  * Reads a rectangle of bytes in an x-y plane from the opened Woolz
  * object.
  * @return     Buffer of bytes read.
  * @param      no              plane coordinate (set to 0 for 2D).
  * @param      buf             buffer for bytes.
  * @param      x               rectangle first column.
  * @param      y               rectangle first line.
  * @param      w               rectangle width (in columns).
  * @param      h               rectangle heigth (in lines).
  */
  public byte[] readBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException;

  /**
  * Adds a rectangle of bytes in an x-y plane to the opened Woolz object.
  * @param      no              plane coordinate (set to 0 for 2D).
  * @param      buf             buffer with bytes.
  * @param      x               rectangle first column.
  * @param      y               rectangle first line.
  * @param      w               rectangle width (in columns).
  * @param      h               rectangle heigth (in lines).
  */
  public void saveBytes(int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException;

}
