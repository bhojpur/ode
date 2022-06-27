/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */
package loci.formats;

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
