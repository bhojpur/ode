package loci.plugins.in;

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

import ij.ImagePlus;
import ij.ImageStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic for concatenating multiple images together.
 */
public class Concatenator {

  /** Concatenates the list of images as appropriate. */
  public List<ImagePlus> concatenate(List<ImagePlus> imps) {

    /* TODO : fix this to work in windowlessly
    GenericDialog axisDialog = new GenericDialog("Choose concatenation axis");
    String[] options = new String[] {"T", "Z", "C"};
    axisDialog.addMessage(
      "Choose the axis along which to concatenate image planes:");
    axisDialog.addChoice("Axis", options, options[0]);
    axisDialog.showDialog();

    if (axisDialog.wasCanceled()) {
      return imps;
    }

    String axis = axisDialog.getNextChoice();
    boolean doAppendT = axis.equals("T");
    boolean doAppendZ = axis.equals("Z");
    boolean doAppendC = axis.equals("C");
    */
    boolean doAppendT = true;
    boolean doAppendZ = true;
    boolean doAppendC = true;

    // list of output (possibly concatenated) images
    final List<ImagePlus> outputImps = new ArrayList<ImagePlus>();

    for (ImagePlus imp : imps) {
      final int width = imp.getWidth();
      final int height = imp.getHeight();
      final int type = imp.getType();
      final int cSize = imp.getNChannels();
      final int zSize = imp.getNSlices();
      final int tSize = imp.getNFrames();

      boolean append = false;
      for (int k=0; k<outputImps.size(); k++) {
        final ImagePlus outputImp = outputImps.get(k);
        final int w = outputImp.getWidth();
        final int h = outputImp.getHeight();
        final int outType = outputImp.getType();
        final int c = outputImp.getNChannels();
        final int z = outputImp.getNSlices();
        final int t = outputImp.getNFrames();

        // verify that images are compatible
        if (width != w || height != h) continue; // different XY resolution
        if (type != outType) continue; // different processor type
        final boolean canAppendT = cSize == c && zSize == z;
        final boolean canAppendZ = cSize == c && tSize == t;
        final boolean canAppendC = zSize == z && tSize == t;
        if (!canAppendT && !canAppendZ && !canAppendC) {
          // incompatible dimensions
          continue;
        }

        // concatenate planes onto this output image
        final ImageStack outputStack = outputImp.getStack();
        final ImageStack inputStack = imp.getStack();
        for (int s=0; s<inputStack.getSize(); s++) {
          outputStack.addSlice(inputStack.getSliceLabel(s + 1),
            inputStack.getProcessor(s + 1));
        }
        outputImp.setStack(outputImp.getTitle(), outputStack);

        // update image dimensions

        if (doAppendT && canAppendT) {
          outputImp.setDimensions(c, z, t + tSize);
        }
        else if (doAppendZ && canAppendZ) {
          outputImp.setDimensions(c, z + zSize, t);
        }
        else if (doAppendC && canAppendC) {
          outputImp.setDimensions(c + cSize, z, t);
        }

        append = true;
        break;
      }
      if (!append) {
        // could not concatenate input image to any existing output;
        // append it to the list of outputs directly instead
        outputImps.add(imp);
      }
    }

    return outputImps;
  }

}
