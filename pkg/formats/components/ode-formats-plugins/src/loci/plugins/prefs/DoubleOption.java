package loci.plugins.prefs;

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

import ij.Macro;
import ij.Prefs;

import java.util.HashMap;

/**
 * A double option for one of the plugins.
 */
public class DoubleOption extends Option {

  // -- Fields --

  /** The option's default value. */
  protected double defaultValue;

  /** The option's current value. */
  protected double value;

  // -- Constructors --

  /** Constructs a double option with the parameters from the given map. */
  public DoubleOption(HashMap<String, String> entry) {
    this(entry.get(INI_KEY),
      !"false".equals(entry.get(INI_SAVE)), // default behavior is saved
      entry.get(INI_LABEL),
      entry.get(INI_INFO),
      0); // default value is 0
  }

  /** Constructs a double option with the given parameters. */
  public DoubleOption(String key, boolean save, String label,
    String info, double defaultValue)
  {
    super(key, save, label, info);
    this.defaultValue = defaultValue;
    this.value = defaultValue;
  }

  // -- DoubleOption methods --

  /** Gets the default value of the option. */
  public double getDefault() {
    return defaultValue;
  }

  /** Gets the current value of the option. */
  public double getValue() {
    return value;
  }

  /** Sets the current value of the option. */
  public void setValue(double value) {
    this.value = value;
  }

  // -- Option methods --

  /* @see Option#parseOption(String arg) */
  @Override
  public void parseOption(String arg) {
    String s = Macro.getValue(arg, key, null);
    if (s != null) value = Double.parseDouble(s);
    else if (label != null) {
      s = Macro.getValue(arg, label, null);
      if (s != null) value = Double.parseDouble(s);
    }
  }

  /* @see Option#loadOption() */
  @Override
  public void loadOption() {
    value = Prefs.get(KEY_PREFIX + key, defaultValue);
  }

  /* @see Option#saveOption() */
  @Override
  public void saveOption() {
    if (save) Prefs.set(KEY_PREFIX + key, value);
  }

}
