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

import java.util.HashMap;

/**
 * An enumeration of the steps in the import preparation process.
 */
public enum ImportStep {

  // -- Enumeration --

  READER    (0, "Initializing base reader"),
  FILE      (1, "Reading file header"),
  STACK     (2, "Building reader stack"),
  SERIES    (3, "Choosing series"),
  DIM_ORDER (4, "Confirming dimension order"),
  RANGE     (5, "Confirming planar ranges"),
  CROP      (6, "Confirming crop region"),
  COLORS    (7, "Confirming colorization"),
  METADATA  (8, "Initializing metadata"),
  COMPLETE  (9, "Import preparations complete");

  // -- Static fields --

  private static final HashMap<Integer, ImportStep> STEP_TABLE;
  static {
    STEP_TABLE = new HashMap<Integer, ImportStep>();
    for (ImportStep step : values()) {
      STEP_TABLE.put(step.getStep(), step);
    }
  }
  public static ImportStep getStep(int step) {
    return STEP_TABLE.get(step);
  }

  // -- Fields --

  private int step;
  private String message;

  // -- Constructor --

  private ImportStep(int step, String message) {
    this.step = step;
    this.message = message;
  }

  // -- ImportStep methods --

  public int getStep() { return step; }
  public String getMessage() { return message; }

}
