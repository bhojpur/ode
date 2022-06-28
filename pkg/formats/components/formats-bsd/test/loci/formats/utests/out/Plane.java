package loci.formats.utests.out;

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import loci.common.Constants;
import loci.formats.FormatTools;

public class Plane {
  public ByteBuffer backingBuffer;
  public boolean rgbPlanar;
  public int rgbChannels;
  public String pixelType;

  public Plane(byte[] buffer, boolean littleEndian, boolean planar,
    int rgbChannels, String pixelType)
  {
    backingBuffer = ByteBuffer.wrap(buffer);
    backingBuffer.order(
      littleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
    this.rgbPlanar = planar;
    this.pixelType = pixelType;
    this.rgbChannels = rgbChannels;
  }

  public boolean equals(Plane other) {
    backingBuffer.position(0);
    other.backingBuffer.position(0);

    int bytes = FormatTools.getBytesPerPixel(pixelType);
    boolean fp =
      FormatTools.isFloatingPoint(FormatTools.pixelTypeFromString(pixelType));

    while (backingBuffer.position() < backingBuffer.capacity()) {
      int otherPos = backingBuffer.position();
      if (rgbPlanar != other.rgbPlanar) {
        int channel = -1;
        int pixel = -1;

        int pos = backingBuffer.position() / bytes;
        int capacity = backingBuffer.capacity();

        if (rgbPlanar) {
          pixel = pos % (capacity / (rgbChannels * bytes));
          channel = pos / (capacity / (rgbChannels * bytes));
        }
        else {
          channel = pos % rgbChannels;
          pixel = pos / rgbChannels;
        }

        if (other.rgbPlanar) {
          otherPos = channel * (capacity / rgbChannels) + pixel * bytes;
        }
        else {
          otherPos = (pixel * rgbChannels + channel) * bytes;
        }
      }

      if (otherPos >= other.backingBuffer.capacity()) {
        break;
      }

      other.backingBuffer.position(otherPos);

      switch (bytes) {
        case 1:
          byte thisB = backingBuffer.get();
          byte otherB = other.backingBuffer.get();

          if (thisB != otherB) {
            if (!pixelType.equals(other.pixelType)) {
              if ((byte) (thisB - 128) != otherB) {
                return false;
              }
            }
            else {
              return false;
            }
          }
          break;
        case 2:
          short thisS = backingBuffer.getShort();
          short otherS = other.backingBuffer.getShort();

          if (thisS != otherS) {
            if (!pixelType.equals(other.pixelType)) {
              if ((short) (thisS - 32768) != otherS) {
                return false;
              }
            }
            else {
              return false;
            }
          }
          break;
        case 4:
          if (fp) {
            float thisF = backingBuffer.getFloat();
            float otherF = other.backingBuffer.getFloat();

            if (Math.abs(thisF - otherF) > Constants.EPSILON) {
              return false;
            }
          }
          else {
            int thisI = backingBuffer.getInt();
            int otherI = other.backingBuffer.getInt();

            if (thisI != otherI) {
              return false;
            }
          }
          break;
        case 8:
          if (fp) {
            double thisD = backingBuffer.getDouble();
            double otherD = other.backingBuffer.getDouble();

            if (Math.abs(thisD - otherD) > Constants.EPSILON) {
              return false;
            }
          }
          else {
            long thisL = backingBuffer.getLong();
            long otherL = other.backingBuffer.getLong();

            if (thisL != otherL) {
              return false;
            }
          }
          break;
      }
    }

    return true;
  }
}
