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
import java.util.Hashtable;
import java.util.Vector;

import loci.common.services.Service;
import loci.common.services.ServiceException;

/**
 * Utility class for working with NetCDF/HDF files.  Uses reflection to
 * call the NetCDF Java library.
 */
public interface NetCDFService extends Service {

  /**
   * Initializes the service on a given file path.
   * @param file Path to initialize the service with.
   * @throws IOException If there is an error initializing the service
   * with <code>file</code>.
   */
  public void setFile(String file) throws IOException;

  /**
   * Retrieves the current initialized file path.
   * @return Current initialized file path or <code>null</code> if the service
   * has yet to be initialized or is closed.
   */
  public String getFile();

  /**
   * Retrieves an exhaustive list of the HDF paths for all attributes in the
   * HDF document.
   * @return Collection of attribute paths.
   */
  public Vector<String> getAttributeList();

  /**
   * Retrieves an exhaustive list of the HDF paths for all variables in the
   * HDF document.
   * @return Collection of variable paths.
   */
  public Vector<String> getVariableList();

  /**
   * Retrieves an attribute's value.
   * @param path HDF path to the attribute.
   * @return String value or <code>null</code> if the attribute is not a
   * string.
   */
  public String getAttributeValue(String path);

  /**
   * Retrieves a variable's value.
   * @param path HDF path to the variable.
   * @return The Java one-dimensional array representation of the variable's
   * values.
   * @throws ServiceException If there is an error with the range of values
   * or reading from the file.
   * @see ucar.ma2.Array#copyTo1DJavaArray()
   */
  public Object getVariableValue(String path) throws ServiceException;

  /**
   * Retrieves an HDF path's values.
   * @param path HDF path to the values.
   * @param origin Array specifying the starting index. If null, assume
   * all zeroes.
   * @param shape Array specifying the extents in each dimension. This
   * becomes the shape of the returned Array.
   * @return The Java n-dimensional array representation of the path's values.
   * @throws ServiceException If there is an error with the range of values
   * or reading from the file.
   * @see ucar.nc2.Variable#read(int[], int[])
   * @see ucar.ma2.Array#copyToNDJavaArray()
   */
  public Object getArray(String path, int[] origin, int[] shape)
    throws ServiceException;

  /**
   * Retrieves all of a variable's attributes.
   * @param path HDF path to the variable.
   * @return Hash table of attributes to Java one-dimensional array
   * representations of the attribute's values.
   * @see ucar.ma2.Array#copyTo1DJavaArray()
   */
  public Hashtable<String, Object> getVariableAttributes(String path);

  /**
   * Retrieves the length of a dimension.
   * @param path HDF path to the dimension.
   * @return Length of the dimension.
   */
  public int getDimension(String path);

  /**
   * Closes and resets the service.
   * @throws IOException If there is an error closing the file.
   */
  public void close() throws IOException;
}
