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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * A string option for one of the plugins.
 */
public class StringOption extends Option {

  // -- Constants --

  /** INI key indicating option's possible values. */
  public static final String INI_POSSIBLE = "values";

  // -- Fields --

  /** The option's default value. */
  protected String defaultValue;

  /** List of possible values. */
  protected Vector<String> possibleValues;

  /** The option's current value. */
  protected String value;

  // -- Static utility methods --

  /** Parses a string of comma-separated values into a list of tokens. */
  public static Vector<String> parseList(String s) {
    if (s == null) return null;
    String[] array = s.split(",");
    for (int i=0; i<array.length; i++) array[i] = array[i].trim();
    return new Vector<String>(Arrays.asList(array));
  }

  // -- Constructors --

  /** Constructs a string option with the parameters from the given map. */
  public StringOption(HashMap<String, String> entry) {
    this(entry.get(INI_KEY),
      !"false".equals(entry.get(INI_SAVE)), // default behavior is saved
      entry.get(INI_LABEL),
      entry.get(INI_INFO),
      entry.get(INI_DEFAULT),
      parseList(entry.get(INI_POSSIBLE)));
  }

  /**
   * Constructs a string option with the given parameters.
   * If possible values list is null, any string value is allowed.
   */
  public StringOption(String key, boolean save, String label,
    String info, String defaultValue, Vector<String> possibleValues)
  {
    super(key, save, label, info);
    this.defaultValue = defaultValue;
    this.possibleValues = possibleValues;
    this.value = defaultValue;
  }

  // -- StringOption methods --

  /** Gets the list of possible values. */
  public Vector<String> getPossible() {
    return possibleValues;
  }

  /** Adds a possible value to the list. */
  public void addPossible(String val) {
    possibleValues.add(val);
  }

  /** Removes a possible value from the list. */
  public void removePossible(String val) {
    possibleValues.remove(val);
  }

  /** Gets the default value of the option. */
  public String getDefault() {
    return defaultValue;
  }

  /** Gets the current value of the option. */
  public String getValue() {
    return value;
  }

  /** Sets the current value of the option. */
  public void setValue(String value) {
    if (possibleValues == null) this.value = value;
    else {
      for (String p : possibleValues) {
        if (p.equals(value)) {
          this.value = value;
          return;
        }
      }
      throw new IllegalArgumentException("'" +
        value + "' is not a possible value");
    }
  }

  // -- Option methods --

  /* @see Option#parseOption(String arg) */
  @Override
  public void parseOption(String arg) {
    String keyValue = Macro.getValue(arg, key, value);
    if ((value == null || keyValue.equals(value)) && label != null) {
      value = Macro.getValue(arg, label, value);
    }
    else value = keyValue;
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
