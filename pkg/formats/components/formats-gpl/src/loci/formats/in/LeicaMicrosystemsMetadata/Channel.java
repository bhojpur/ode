/*
 * Bhojpur ODE-Formats package for reading and converting biological file formats.
 */

package loci.formats.in.LeicaMicrosystemsMetadata;

/**
 * This class represents image channels extracted from LMS image xmls.stems.com
 */
public class Channel {
  // -- Fields --
  public int channelTag;
  public int resolution;
  public double min;
  public double max;
  public String unit;
  public String lutName;
  public long bytesInc;

  public boolean isLutInverted;
  public long bitInc;
  public int dataType;
  public String nameOfMeasuredQuantity;

  public enum ChannelType {
    MONO,
    RED,
    GREEN,
    BLUE,
    // PHOTON_COUNTING,
    // ARRIVAL_TIME
  }

  public ChannelType channelType;

  // -- Constructor --
  public Channel(int channelTag, int resolution, double min, double max, String unit,
      String lutName, long bytesInc) {
    this.channelTag = channelTag;
    this.resolution = resolution;
    this.min = min;
    this.max = max;
    this.unit = unit;
    this.lutName = lutName;
    this.bytesInc = bytesInc;
    setChannelType();
  }

  //-- Methods --
  private void setChannelType(){
    if (channelTag == 0) {
      channelType = ChannelType.MONO;
    } else {
      if (lutName.equals("Red")){
        channelType = ChannelType.RED;
      } else if (lutName.equals("Green")){
        channelType = ChannelType.GREEN;
      } else if (lutName.equals("Blue")){
        channelType = ChannelType.BLUE;
      }
    }
  }
}
