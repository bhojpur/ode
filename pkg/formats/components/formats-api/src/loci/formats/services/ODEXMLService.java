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

import java.util.Hashtable;

import loci.common.services.Service;
import loci.common.services.ServiceException;
import loci.formats.CoreMetadata;
import loci.formats.Modulo;
import loci.formats.meta.MetadataRetrieve;
import loci.formats.meta.MetadataStore;
import loci.formats.ode.ODEXMLMetadata;
import ode.xml.model.ODEModelObject;

/**
 * 
 */
public interface ODEXMLService extends Service {

  /**
   * Retrieves the latest supported version of the ODE-XML schema.
   */
  public String getLatestVersion();

  /**
   * Transforms the given ODE-XML string to the latest supported version of
   * of the ODE-XML schema.
   */
  public String transformToLatestVersion(String xml) throws ServiceException;

  /**
   * Creates an ODE-XML metadata object using reflection, to avoid
   * direct dependencies on the optional {@link loci.formats.ode} package.
   * @return A new instance of {@link loci.formats.ode.AbstractODEXMLMetadata},
   *   or null if the class is not available.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public ODEXMLMetadata createODEXMLMetadata() throws ServiceException;

  /**
   * Creates an ODE-XML metadata object using reflection, to avoid
   * direct dependencies on the optional {@link loci.formats.ode} package,
   * wrapping a DOM representation of the given ODE-XML string.
   * @return A new instance of {@link loci.formats.ode.AbstractODEXMLMetadata},
   *   or null if the class is not available.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public ODEXMLMetadata createODEXMLMetadata(String xml)
    throws ServiceException;

  /**
   * Creates an ODE-XML metadata object using reflection, to avoid
   * direct dependencies on the optional {@link loci.formats.ode} package,
   * wrapping a DOM representation of the given ODE-XML string.
   *
   * @param xml The ODE-XML string to use for initial population of the
   *   metadata object.
   * @param version The ODE-XML version to use (e.g., "2018-FC" or "2007-06").
   *   If the xml and version parameters are both null, the newest version is
   *   used.
   * @return A new instance of {@link loci.formats.ode.AbstractODEXMLMetadata},
   *   or null if the class is not available.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public ODEXMLMetadata createODEXMLMetadata(String xml, String version)
    throws ServiceException;

  /**
   * Constructs an ODE root node.
   * @param xml String of XML to create the root node from.
   * @return An ode.xml.model.ODEModelObject subclass root node.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public ODEModelObject createODEXMLRoot(String xml) throws ServiceException;

  /**
   * Checks whether the given object is an ODE-XML metadata object.
   * @return True if the object is an instance of
   *   {@link loci.formats.ode.ODEXMLMetadata}.
   */
  public boolean isODEXMLMetadata(Object o);

  /**
   * Checks whether the given object is an ODE-XML root object.
   * @return True if the object is an instance of {@link ode.xml.model.ODE}.
   */
  public boolean isODEXMLRoot(Object o);

  /**
   * Gets the schema version for the given ODE-XML metadata or root object
   * (e.g., "2007-06" or "2018-FC").
   * @return ODE-XML schema version, or null if the object is not an instance
   *   of {@link loci.formats.ode.ODEXMLMetadata}.
   */
  public String getODEXMLVersion(Object o);

  /**
   * Returns a {@link loci.formats.ode.ODEXMLMetadata} object with the same
   * contents as the given MetadataRetrieve, converting it if necessary.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public ODEXMLMetadata getODEMetadata(MetadataRetrieve src)
    throws ServiceException;

  /**
   * Extracts an ODE-XML metadata string from the given metadata object,
   * by converting to an ODE-XML metadata object if necessary.
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public String getODEXML(MetadataRetrieve src)
    throws ServiceException;

  /**
   * Attempts to validate the given ODE-XML string using
   * Java's XML validation facility. Requires Java 1.5+.
   *
   * @param xml XML string to validate.
   * @return true if the XML successfully validates.
   */
  public boolean validateODEXML(String xml);

  /**
   * Attempts to validate the given ODE-XML string using
   * Java's XML validation facility. Requires Java 1.5+.
   *
   * @param xml XML string to validate.
   * @param pixelsHack Whether to ignore validation errors
   *   due to childless Pixels elements
   * @return true if the XML successfully validates.
   */
  public boolean validateODEXML(String xml, boolean pixelsHack);

  /**
   * Adds the key/value pairs in the specified Hashtable as new
   * OriginalMetadata annotations in the given ODE-XML metadata object.
   * @param odexmlMeta An object of type
   *   {@link loci.formats.ode.ODEXMLMetadata}.
   * @param metadata A hashtable containing metadata key/value pairs.
   */
  public void populateOriginalMetadata(ODEXMLMetadata odexmlMeta,
    Hashtable<String, Object> metadata);

  /**
   * Retrieve the ModuloAlongZ annotation in the given Image in the given
   * ODE-XML metadata object.
   * If no ModuloAlongZ annotation is present, return null.
   *
   * @param odexmlMeta An object of type
   *  {@link loci.formats.ode.ODEXMLMetadata}
   * @param image the index of the Image to which the Annotation is linked
   */
  public Modulo getModuloAlongZ(ODEXMLMetadata odexmlMeta, int image);

  /**
   * Retrieve the ModuloAlongC annotation in the given Image in the given
   * ODE-XML metadata object.
   * If no ModuloAlongC annotation is present, return null.
   *
   * @param odexmlMeta An object of type
   *  {@link loci.formats.ode.ODEXMLMetadata}
   * @param image the index of the Image to which the Annotation is linked
   */
  public Modulo getModuloAlongC(ODEXMLMetadata odexmlMeta, int image);

  /**
   * Retrieve the ModuloAlongT annotation in the given Image in the given
   * ODE-XML metadata object.
   * If no ModuloAlongT annotation is present, return null.
   *
   * @param odexmlMeta An object of type
   *  {@link loci.formats.ode.ODEXMLMetadata}
   * @param image the index of the Image to which the Annotation is linked
   */
  public Modulo getModuloAlongT(ODEXMLMetadata odexmlMeta, int image);

  /**
   * Parse any OriginalMetadata annotations from the given ODE-XML metadata
   * object and store them in a Hashtable.
   *
   * @param odexmlMeta An object of type
   *   {@link loci.formats.ode.ODEXMLMetadata}.
   */
  public Hashtable getOriginalMetadata(ODEXMLMetadata odexmlMeta);

  /**
   * Adds the specified key/value pair as a new OriginalMetadata node
   * to the given ODE-XML metadata object.
   * @param odexmlMeta An object of type
   *   {@link loci.formats.ode.ODEXMLMetadata}.
   * @param key Metadata key to populate.
   * @param value Metadata value corresponding to the specified key.
   */
  public void populateOriginalMetadata(ODEXMLMetadata odexmlMeta,
    String key, String value);

  /**
   * Adds ModuloAlong* annotations to the given ODE-XML metadata object,
   * using the given CoreMetadata object to determine modulo dimensions.
   *
   * @param odexmlMeta An object of type {@link loci.formats.ode.ODEXMLMetadata}
   * @param core A fully populated object of type
   *   {@link loci.formats.CoreMetadata}
   * @param image Index of the Image to which the annotation should be linked.
   */
  public void addModuloAlong(ODEXMLMetadata odexmlMeta, CoreMetadata core,
    int image);

  /**
   * Converts information from an ODE-XML string (source)
   * into a metadata store (destination).
   * @throws ServiceException If there is an error creating the ODE-XML
   * metadata object.
   */
  public void convertMetadata(String xml, MetadataStore dest)
    throws ServiceException;

  /**
   * Copies information from a metadata retrieval object
   * (source) into a metadata store (destination).
   */
  public void convertMetadata(MetadataRetrieve src, MetadataStore dest);

  /**
   * Remove all of the BinData elements from the given ODE-XML metadata object.
   */
  public void removeBinData(ODEXMLMetadata odexmlMeta);

  /**
   * Remove all of the TiffData elements from the given ODE-XML metadata object.
   */
  public void removeTiffData(ODEXMLMetadata odexmlMeta);

  /**
   * Remove all but the first sizeC valid Channel elements from the given
   * ODE-XML metadata object.
   */
  public void removeChannels(ODEXMLMetadata odexmlMeta, int image, int sizeC);

  /**
   * Insert a MetadataOnly element under the Image specified by 'index' in the
   * given ODE-XML metadata object.
   */
  public void addMetadataOnly(ODEXMLMetadata odexmlMeta, int image);

  /**
   * Insert a MetadataOnly element under the Image specified by 'index' in the
   * given ODE-XML metadata object.  If the 'resolve' flag is set, references
   * in the ODE-XML metadata object will be resolved before the new element is
   * inserted.
   */
  public void addMetadataOnly(ODEXMLMetadata odexmlMeta, int image, boolean resolve);

  /**
   * Determine whether or not two ODEXMLMetadata objects are equal.
   * Equality is defined as:
   *
   *  * having the same object graph (without regard to specific ID values)
   *  * having the exact same attribute values on every node (with the exception
   *    of 'ID' attributes)
   *
   * Note that StructuredAnnotations are ignored, i.e. the two ODEXMLMetadata
   * objects may have wildly different things under StructuredAnnotations and
   * still be considered equal.
   */
  public boolean isEqual(ODEXMLMetadata src1, ODEXMLMetadata src2);

  // -- Utility methods - casting --

  /**
   * Gets the given {@link MetadataRetrieve} object as a {@link MetadataStore}.
   * Returns null if the object is incompatible and cannot be casted.
   */
  public MetadataStore asStore(MetadataRetrieve meta);

  /**
   * Gets the given {@link MetadataStore} object as a {@link MetadataRetrieve}.
   * Returns null if the object is incompatible and cannot be casted.
   */
  public MetadataRetrieve asRetrieve(MetadataStore meta);

}
