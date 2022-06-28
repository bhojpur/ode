package loci.formats.in.LeicaMicrosystemsMetadata;

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
