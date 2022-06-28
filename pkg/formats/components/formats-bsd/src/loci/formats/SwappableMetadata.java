package loci.formats;

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

/**
 * Extends {@link CoreMetadata} to include the notion of an "input order" for planes:
 * the input order being the order planes are stored on disk.
 * <p>
 * NB: Input order can differ from the {@link CoreMetadata#dimensionOrder} field,
 * the latter being the order dimensions are returned (and thus returned by
 * {@link IFormatReader#getDimensionOrder()}).
 * </p>
 * <p>
 * This class is primarily intended for use by {@link DimensionSwapper} and its children,
 * which introduce the API to separate input and output orders.
 * </p>
 */
public class SwappableMetadata extends CoreMetadata {
  
  // -- Fields --
  
  /**
   * Order in which dimensions are stored.  Must be one of the following:<ul>
   *  <li>XYCZT</li>
   *  <li>XYCTZ</li>
   *  <li>XYZCT</li>
   *  <li>XYZTC</li>
   *  <li>XYTCZ</li>
   *  <li>XYTZC</li>
   * </ul>
   */
  public String inputOrder;
  
  // -- Constructors --
  
  public SwappableMetadata() {
    super();
  }
  
  public SwappableMetadata(IFormatReader r, int seriesNo) {
    super(r, seriesNo);
    inputOrder = dimensionOrder;
  }

  public SwappableMetadata(SwappableMetadata c) {
    super(c);
    inputOrder = dimensionOrder;
  }
  
  @Override
  public Object clone() throws CloneNotSupportedException {
      return super.clone();
  }

  @Override
  public CoreMetadata clone(IFormatReader r, int coreIndex) {
      return new SwappableMetadata(r, coreIndex);
  }

  // -- CoreMetadata methods --
  
}
