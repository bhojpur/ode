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

import java.awt.image.*;
import loci.formats.*;
import loci.formats.gui.BufferedImageReader;
import loci.formats.gui.BufferedImageWriter;

/**
 * Sums together the image planes from the given file,
 * and saves the result to a 16-bit TIFF.
 */
public class SumPlanes {

  public static void main(String[] args) throws Exception {
    String id = args[0];
    BufferedImageReader r = new BufferedImageReader();
    System.out.print("Reading " + id);
    r.setId(id);
    int imageCount = r.getImageCount();
    BufferedImage[] images = new BufferedImage[imageCount];
    for (int i=0; i<imageCount; i++) {
      System.out.print(".");
      images[i] = r.openImage(i);
    }
    r.close();
    System.out.println(" [done]");

    String outId = id + ".tif";
    BufferedImageWriter w = new BufferedImageWriter();
    System.out.print("Writing " + outId);
    w.setId(outId);
    w.saveImage(0, sum(images));
    w.close();
    System.out.println(" [done]");
  }

   public static BufferedImage sum(BufferedImage[] images) {
    // Assuming that all images have the same dimensions and type
    int w = images[0].getWidth();
    int h = images[0].getHeight();
    //int type = images[0].getType();

    BufferedImage result = new BufferedImage(w, h,
      BufferedImage.TYPE_USHORT_GRAY); // type == 0 for some reason...
    WritableRaster raster = result.getRaster().createCompatibleWritableRaster();
    int bands = raster.getNumBands();

    for (int y=0; y<h; y++) {
      for (int x=0; x<w; x++) {
        for (int b=0; b<bands; b++) {
          float sum = 0;
          for (int i=0; i<images.length; i++) {
            sum = sum + images[i].getRaster().getSample(x, y, b);
          }
          raster.setSample(x, y, b, sum);
        }
      }
    }
    result.setData(raster);
    return result;
  }

}
