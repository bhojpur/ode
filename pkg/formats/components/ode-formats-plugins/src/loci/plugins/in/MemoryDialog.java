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

import ij.gui.GenericDialog;

/**
 * ODE-Formats Importer memory usage confirmation dialog box.
 */
public class MemoryDialog extends ImporterDialog {

  // -- Constants --

  /** Minimum amount of wiggle room for available memory, in bytes. */
  private static final long MINIMUM_MEMORY_PADDING = 20 * 1024 * 1024; // 20 MB

  // -- Fields --

  /** Required memory for the dataset, in bytes. */
  private long needMem;

  /** Remaining memory for the JVM. */
  private long availMem;

  // -- Constructor --

  /** Creates a memory confirmation dialog for the ODE-Formats Importer. */
  public MemoryDialog(ImportProcess process) {
    super(process);
  }

  // -- ImporterDialog methods --

  @Override
  protected boolean needPrompt() {
    if (process.isWindowless()) return false;
    if (process.getOptions().getStackFormat().equals(ImporterOptions.VIEW_NONE))
    {
      return false;
    }
    needMem = process.getMemoryUsage();
    availMem = getAvailableMemory();
    // NB: Prompt if dataset will leave too little memory available.
    return availMem - needMem < MINIMUM_MEMORY_PADDING;
  }

  @Override
  protected GenericDialog constructDialog() {
    final long needMB = needMem / 1048576;
    final long availMB = availMem / 1048576;
    GenericDialog gd = new GenericDialog("ODE-Formats Memory Usage");
    gd.addMessage("Warning: It will require approximately " + needMB +
      " MB to open this dataset.\nHowever, only " + availMB +
      " MB is currently available. You may receive an error\n" +
      "message about insufficient memory. Are you sure you want to proceed?");
    return gd;
  }

  @Override
  protected boolean harvestResults(GenericDialog gd) {
    return gd.wasOKed();
  }

  // -- Helper methods --

  private long getAvailableMemory() {
    final Runtime r = Runtime.getRuntime();
    final long usedMem = r.totalMemory() - r.freeMemory();
    return r.maxMemory() - usedMem;
  }

}
