package loci.formats.meta;

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

import ode.xml.meta.MetadataStore;
import ode.xml.meta.MetadataRetrieve;

/**
 * A utility class containing a method for piping a source
 * {@link MetadataRetrieve} object into a destination {@link MetadataStore}.
 *
 * <p>This technique allows conversion between two different storage media.
 * For example, it can be used to convert an <code>ODEMetadataStore</code>
 * (Bhojpur ODE's metadata store implementation) into an
 * {@link loci.formats.ode.ODEXMLMetadata}, thus generating ODE-XML from
 * information in a Bhojpur ODE database.
 *
 * @deprecated Use ode.xml.meta.MetadataConverter.
 */
@Deprecated
public final class MetadataConverter {

  // -- Constructor --

  /**
   * Private constructor; all methods in MetadataConverter
   * are static, so this should not be called.
   */
  private MetadataConverter() { }

  // -- MetadataConverter API methods --

  /**
   * Copies information from a metadata retrieval object
   * (source) into a metadata store (destination).
   */
  public static void convertMetadata(MetadataRetrieve src, MetadataStore dest) {
    ode.xml.meta.MetadataConverter.convertMetadata(src, dest);
  }

}
