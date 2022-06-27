package loci.plugins.macro;

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

import ij.IJ;
import ij.macro.ExtensionDescriptor;
import ij.macro.Functions;
import ij.macro.MacroExtension;
import ij.plugin.PlugIn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import loci.plugins.util.LibraryChecker;
import loci.plugins.util.WindowTools;

/**
 * Convenience class that simplifies implemention of ImageJ macro extensions.
 * It uses reflection to create an extension method for each public method in
 * the implementing subclass. See {@link LociFunctions} for an example.
 */
public class MacroFunctions implements PlugIn, MacroExtension {

  // -- Fields --

  protected ExtensionDescriptor[] extensions = buildExtensions();

  // -- PlugIn API methods --

  @Override
  public void run(String arg) {
    if (!LibraryChecker.checkImageJ()) return;
    if (!IJ.macroRunning()) {
      IJ.error("Cannot install extensions from outside a macro.");
      return;
    }
    Functions.registerExtensions(this);
  }

  // -- MacroExtension API methods --

  @Override
  public ExtensionDescriptor[] getExtensionFunctions() {
    return extensions;
  }

  @Override
  public String handleExtension(String name, Object[] args) {
    Class<?>[] c = null;
    if (args != null) {
      c = new Class[args.length];
      for (int i=0; i<args.length; i++) c[i] = args[i].getClass();
    }
    try {
      getClass().getMethod(name, c).invoke(this, args);
    }
    catch (NoSuchMethodException exc) {
      WindowTools.reportException(exc, false, "Macro error");
    }
    catch (IllegalAccessException exc) {
      WindowTools.reportException(exc, false, "Macro error");
    }
    catch (InvocationTargetException exc) {
      WindowTools.reportException(exc, false, "Macro error");
    }
    return null;
  }

  // -- Helper methods --

  /**
   * Builds the list of extensions, using reflection,
   * from public methods of this class.
   */
  protected ExtensionDescriptor[] buildExtensions() {
    Method[] m = getClass().getMethods();
    ExtensionDescriptor[] desc = new ExtensionDescriptor[m.length];
    for (int i=0; i<m.length; i++) {
      Class<?>[] c = m[i].getParameterTypes();
      int[] types = new int[c.length];
      for (int j=0; j<c.length; j++) {
        if (c[j] == String.class) types[j] = ARG_STRING;
        else if (c[j] == Double.class) types[j] = ARG_NUMBER;
        else if (c[j] == Object[].class) types[j] = ARG_ARRAY;
        else if (c[j] == String[].class) types[j] = ARG_OUTPUT + ARG_STRING;
        else if (c[j] == Double[].class) types[j] = ARG_OUTPUT + ARG_NUMBER;
      }
      desc[i] = ExtensionDescriptor.newDescriptor(m[i].getName(), this, types);
    }
    return desc;
  }

}
