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

import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import ij.process.LUT;
import java.io.IOException;
import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.plugins.util.ImageProcessorReader;
import loci.plugins.util.LociPrefs;

/**
 * An ImageJ plugin that uses ODE-Formats to build up an {@link ImageStack},
 * reading image planes one by one.
 */
public class Read_Image implements PlugIn {
  public void run(String arg) {
    OpenDialog od = new OpenDialog("Open Image File...", arg);
    String dir = od.getDirectory();
    String name = od.getFileName();
    String id = dir + name;
    ImageProcessorReader r = new ImageProcessorReader(
      new ChannelSeparator(LociPrefs.makeImageReader()));
    try {
      IJ.showStatus("Examining file " + name);
      r.setId(id);
      int num = r.getImageCount();
      int width = r.getSizeX();
      int height = r.getSizeY();
      ImageStack stack = new ImageStack(width, height);
      byte[][][] lookupTable = new byte[r.getSizeC()][][];
      for (int i=0; i<num; i++) {
        IJ.showStatus("Reading image plane #" + (i + 1) + "/" + num);
        ImageProcessor ip = r.openProcessors(i)[0];
        stack.addSlice("" + (i + 1), ip);
        int channel = r.getZCTCoords(i)[1];
        lookupTable[channel] = r.get8BitLookupTable();
      }
      IJ.showStatus("Constructing image");
      ImagePlus imp = new ImagePlus(name, stack);

      ImagePlus colorizedImage = applyLookupTables(r, imp, lookupTable);
      r.close();

      colorizedImage.show();
      IJ.showStatus("");
    }
    catch (FormatException exc) {
      IJ.error("Sorry, an error occurred: " + exc.getMessage());
    }
    catch (IOException exc) {
      IJ.error("Sorry, an error occurred: " + exc.getMessage());
    }
  }

  private ImagePlus applyLookupTables(IFormatReader r, ImagePlus imp,
    byte[][][] lookupTable)
  {
    // apply color lookup tables, if present
    // this requires ImageJ v1.39 or higher
    if (r.isIndexed()) {
      CompositeImage composite =
        new CompositeImage(imp, CompositeImage.COLOR);
      for (int c=0; c<r.getSizeC(); c++) {
        composite.setPosition(c + 1, 1, 1);
        LUT lut =
          new LUT(lookupTable[c][0], lookupTable[c][1], lookupTable[c][2]);
        composite.setChannelLut(lut);
      }
      composite.setPosition(1, 1, 1);
      return composite;
    }
    return imp;
  }
}
