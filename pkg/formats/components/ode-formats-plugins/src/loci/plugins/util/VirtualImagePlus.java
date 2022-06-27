package loci.plugins.util;

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
import ij.process.ImageStatistics;
import ij.process.LUT;

import java.io.IOException;
import java.util.List;

import loci.formats.IFormatReader;
import loci.plugins.util.RecordedImageProcessor.MethodEntry;

/**
 * Extension of {@link ij.ImagePlus} that supports
 * ODE-Formats-driven virtual stacks.
 */
public class VirtualImagePlus extends ImagePlus {

  // -- Fields --

  private IFormatReader r;
  private LUT[] luts;

  // -- Constructor --

  public VirtualImagePlus(String title, ImageStack stack) {
    super(title, stack);
    // call getStatistics() to ensure that single-slice stacks have the
    // correct pixel type
    getStatistics();
  }

  // -- VirtualImagePlus API methods --

  public void setReader(IFormatReader r) {
    this.r = r;
  }

  public void setLUTs(LUT[] luts) {
    this.luts = luts;
  }

  // -- ImagePlus API methods --

  @Override
  public synchronized void setSlice(int index) {
    super.setSlice(index);

    BFVirtualStack stack = null;
    if (getStack() instanceof BFVirtualStack) {
      stack = (BFVirtualStack) getStack();
      RecordedImageProcessor proc = stack.getRecordedProcessor();
      List<MethodEntry> methods = stack.getMethodStack();
      if (methods != null) {
        proc.applyMethodStack(methods);
      }
      // if we call setProcessor(getTitle(), proc), the type will be set
      // to GRAY32 (regardless of the actual processor type)
      setProcessor(getTitle(), proc.getChild());
      int channel = getChannel() - 1;
      if (channel >= 0 && luts != null && channel < luts.length) {
        getProcessor().setColorModel(luts[channel]);
      }
      this.ip = proc;
    }
  }

  @Override
  public void close() {
    super.close();
    try {
      r.close();
    }
    catch (IOException e) { }
  }

  @Override
  public ImageStatistics getStatistics(int mOptions, int nBins,
    double histMin, double histMax)
  {
    if (this.ip instanceof RecordedImageProcessor) {
      RecordedImageProcessor currentProc = (RecordedImageProcessor) this.ip;
      this.ip = currentProc.getChild();
      setProcessor(getTitle(), this.ip);
      ImageStatistics s =
        super.getStatistics(mOptions, nBins, histMin, histMax);
      this.ip = currentProc;
      return s;
    }
    return super.getStatistics(mOptions, nBins, histMin, histMax);
  }

}
