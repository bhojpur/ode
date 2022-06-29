package ode.xml.utests;

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

import static org.testng.AssertJUnit.*;

import ode.xml.model.primitives.Color;

import org.testng.annotations.Test;

/**
 */
public class ColorTest {

  @Test
  public void testBlue() {
    Color blue = new Color(0, 0, 255, 255);
    assertEquals(blue.getValue().intValue(), 65535);
    assertEquals(blue.getRed(), 0);
    assertEquals(blue.getGreen(), 0);
    assertEquals(blue.getBlue(), 255);
    assertEquals(blue.getAlpha(), 255);
  }

  @Test
  public void testRed() {
    Color red = new Color(255, 0, 0, 255);
    assertEquals(red.getValue().intValue(), -16776961);
    assertEquals(red.getRed(), 255);
    assertEquals(red.getGreen(), 0);
    assertEquals(red.getBlue(), 0);
    assertEquals(red.getAlpha(), 255);
  }

  @Test
  public void testGreen() {
    Color green = new Color(0, 255, 0, 255);
    assertEquals(green.getValue().intValue(), 16711935);
    assertEquals(green.getRed(), 0);
    assertEquals(green.getGreen(), 255);
    assertEquals(green.getBlue(), 0);
    assertEquals(green.getAlpha(), 255);
  }

  @Test
  public void testCyan() {
    Color cyan = new Color(0, 255, 255, 255);
    assertEquals(cyan.getValue().intValue(), 16777215);
    assertEquals(cyan.getRed(), 0);
    assertEquals(cyan.getGreen(), 255);
    assertEquals(cyan.getBlue(), 255);
    assertEquals(cyan.getAlpha(), 255);
  }

  @Test
  public void testMagenta() {
    Color magenta = new Color(255, 0, 255, 255);
    assertEquals(magenta.getValue().intValue(), -16711681);
    assertEquals(magenta.getRed(), 255);
    assertEquals(magenta.getGreen(), 0);
    assertEquals(magenta.getBlue(), 255);
    assertEquals(magenta.getAlpha(), 255);
  }

  @Test
  public void testYellow() {
    Color yellow = new Color(255, 255, 0, 255);
    assertEquals(yellow.getValue().intValue(), -65281);
    assertEquals(yellow.getRed(), 255);
    assertEquals(yellow.getGreen(), 255);
    assertEquals(yellow.getBlue(), 0);
    assertEquals(yellow.getAlpha(), 255);
  }

}