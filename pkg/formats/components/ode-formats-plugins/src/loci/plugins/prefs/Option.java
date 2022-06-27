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

import loci.common.IniTable;

/**
 * Base class for an option for one of the plugins.
 */
public abstract class Option {

  // -- Constants --

  /** Prefix to use for all plugins preferences. */
  public static final String KEY_PREFIX = "odeformats.";

  /** INI key indicating option's key. */
  public static final String INI_KEY = IniTable.HEADER_KEY;

  /** INI key indicating whether option should be saved to prefs file. */
  public static final String INI_SAVE = "save";

  /** INI key indicating option's label. */
  public static final String INI_LABEL = "label";

  /** INI key indicating option's info. */
  public static final String INI_INFO = "info";

  /** INI key indicating option's default value. */
  public static final String INI_DEFAULT = "default";

  // -- Fields --

  /** Key name for ImageJ preferences file. */
  protected String key;

  /** Whether option state should be saved to ImageJ preferences file. */
  protected boolean save;

  /**
   * Label describing the option, for use with ImageJ dialogs.
   * May contain underscores to produce a better macro variable name.
   */
  protected String label;

  /** Documentation about the option, in HTML. */
  protected String info;

  // -- Constructor --

  /** Constructs an option with the given parameters. */
  public Option(String key, boolean save, String label, String info) {
    this.key = key;
    if (key == null) throw new IllegalArgumentException("Null key");
    this.save = save;
    this.label = label;
    this.info = info;
  }

  // -- Option methods --

  /** Gets the option's key name. */
  public String getKey() {
    return key;
  }

  /** Gets whether option should be saved to ImageJ preferences file. */
  public boolean isSaved() {
    return save;
  }

  /** Gets label describing the option. */
  public String getLabel() {
    return label;
  }

  /** Gets documentation about the option. */
  public String getInfo() {
    return info;
  }

  // -- Abstract Option methods --

  /** Parses the option's value from the given argument string. */
  public abstract void parseOption(String arg);

  /** Loads the option's value from the ImageJ preferences file. */
  public abstract void loadOption();

  /** Saves the option's value to the ImageJ preferences file. */
  public abstract void saveOption();

}
