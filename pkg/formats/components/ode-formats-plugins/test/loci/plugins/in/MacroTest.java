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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import ij.Macro;

import java.io.IOException;

import org.testng.annotations.Test;

/**
 * Test to ensure that old macro keys are still handled correctly.

 */
public class MacroTest {

  // -- Constants --

  private static final String OLD_MERGE =
    "merge_channels view=Hyperstack stack_order=XYCZT ";
  private static final String NEW_MERGE =
    "color_mode=Composite view=Hyperstack stack_order=XYCZT ";

  private static final String OLD_AUTO_COLORIZE =
    "rgb_colorize view=Hyperstack stack_order=XYCZT ";
  private static final String NEW_AUTO_COLORIZE =
    "color_mode=Colorized view=Hyperstack stack_order=XYCZT ";

  private static final String OLD_CUSTOM_COLORIZE =
    "custom_colorize view=Hyperstack stack_order=XYCZT " +
    "series_0_channel_0_red=231 series_0_channel_0_green=100 " +
    "series_0_channel_0_blue=136 series_0_channel_1_red=143 " +
    "series_0_channel_1_green=214 series_0_channel_1_blue=100 " +
    "series_0_channel_2_red=240 series_0_channel_2_green=200 " +
    "series_0_channel_2_blue=120 ";
  private static final String NEW_CUSTOM_COLORIZE =
    "color_mode=Custom view=Hyperstack stack_order=XYCZT " +
    "series_0_channel_0_red=231 series_0_channel_0_green=100 " +
    "series_0_channel_0_blue=136 series_0_channel_1_red=143 " +
    "series_0_channel_1_green=214 series_0_channel_1_blue=100 " +
    "series_0_channel_2_red=240 series_0_channel_2_green=200 " +
    "series_0_channel_2_blue=120 ";

  private static final String COMPOSITE_MODE = "color_mode=Composite ";
  private static final String COLORIZED_MODE = "color_mode=Colorized ";

  // -- MacroTest methods --

  @Test
  public void testMergeChannels() {
    ImporterOptions oldOptions = getOptions(OLD_MERGE);
    ImporterOptions newOptions = getOptions(NEW_MERGE);
    assertEquals(oldOptions, newOptions);
  }

  @Test
  public void testAutoColorizeChannels() {
    ImporterOptions oldOptions = getOptions(OLD_AUTO_COLORIZE);
    ImporterOptions newOptions = getOptions(NEW_AUTO_COLORIZE);
    assertEquals(oldOptions, newOptions);
  }

  @Test
  public void testCustomColorizeChannels() {
    ImporterOptions oldOptions = getOptions(OLD_CUSTOM_COLORIZE);
    ImporterOptions newOptions = getOptions(NEW_CUSTOM_COLORIZE);
    assertEquals(oldOptions, newOptions);
  }

  @Test
  public void testDisjointColorModes() {
    ImporterOptions composite = getOptions(COMPOSITE_MODE);
    ImporterOptions colorized = getOptions(COLORIZED_MODE);
    assertFalse(composite.equals(colorized));
    assertEquals(composite.getColorMode(), "Composite");
    assertEquals(colorized.getColorMode(), "Colorized");
  }

  @Test
  public void testEqualMacros() {
    ImporterOptions compositeA = getOptions(COMPOSITE_MODE);
    ImporterOptions compositeB = getOptions(COMPOSITE_MODE);
    assertEquals(compositeA, compositeB);
    assertEquals(compositeA.getColorMode(), "Composite");
    assertEquals(compositeB.getColorMode(), "Composite");
  }

  // -- Helper methods --

  private ImporterOptions getOptions(String macro) {
    // Manually set the current thread's name, so that Macro.setOptions and
    // Macro.getOptions will behave as expected.  See the implementation of
    // Macro.getOptions() for more information.
    Thread current = Thread.currentThread();
    current.setName("Run$_" + current.getName());
    try {
      Macro.setOptions(macro);
      ImporterOptions options = new ImporterOptions();
      options.parseArg(macro);
      options.checkObsoleteOptions();
      return options;
    }
    catch (IOException e) {
      fail(e.getMessage());
    }
    return null;
  }

}
