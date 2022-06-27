/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

/**
 * This class represents image dimensions extracted from LMS image xmls.
 */
public class Dimension {

  // -- Fields --
  public DimensionKey key;
  public int size;
  public long bytesInc;
  public String unit = null;
  private Double length = 0d;
  private Double offByOneLength = 0d;
  public boolean oldPhysicalSize = false;
  public int frameIndex = 0;

  public enum DimensionKey {
    X(1, 'X'),
    Y(2, 'Y'),
    Z(3, 'Z'),
    T(4, 'T'),
    C(5, 'C'),
    S(10, 'S');

    public int id;
    public char token;

    DimensionKey(int id, char token) {
      this.id = id;
      this.token = token;
    }

    public static DimensionKey with(int id) {
      for (DimensionKey key : DimensionKey.values()) {
        if (key.id == id) {
          return key;
        }
      }
      return null;
    }
  }

  private static final long METER_MULTIPLY = 1000000;

  // -- Constructors --

  public Dimension(DimensionKey key, int size, long bytesInc, String unit, Double length, boolean oldPhysicalSize) {
    this.key = key;
    this.size = size;
    this.bytesInc = bytesInc;
    this.unit = unit;
    this.oldPhysicalSize = oldPhysicalSize;
    setLength(length);
  }

  private Dimension() {
  }

  public static Dimension createChannelDimension(int channelNumber, long bytesInc) {
    Dimension dimension = new Dimension();
    dimension.bytesInc = bytesInc;
    dimension.size = channelNumber;
    dimension.key = DimensionKey.C;
    return dimension;
  }

  // -- Methods --
  public void setLength(Double length) {
    this.length = length;
    offByOneLength = 0d;
    if (size > 1) {
      offByOneLength = this.length / size;
      this.length /= (size - 1); // length per pixel
    } else {
      this.length = 0d;
    }

    if (unit.equals("Ks")) {
      this.length /= 1000;
      offByOneLength /= 1000;
    } else if (unit.equals("m")) {
      this.length *= METER_MULTIPLY;
      offByOneLength *= METER_MULTIPLY;
    }
  }

  public Double getLength() {
    if (key == DimensionKey.X || key == DimensionKey.Y) {
      return oldPhysicalSize ? offByOneLength : length;
    } else {
      return length;
    }
  }
}
