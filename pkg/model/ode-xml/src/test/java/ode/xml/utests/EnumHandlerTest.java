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

import ode.xml.model.enums.Binning;
import ode.xml.model.enums.Correction;
import ode.xml.model.enums.EnumerationException;
import ode.xml.model.enums.Immersion;
import ode.xml.model.enums.handlers.BinningEnumHandler;
import ode.xml.model.enums.handlers.CorrectionEnumHandler;
import ode.xml.model.enums.handlers.ImmersionEnumHandler;
import ode.xml.model.primitives.NonNegativeInteger;

import org.testng.annotations.Test;

/**
 */
public class EnumHandlerTest {

  @Test
  public void testImmersionOI() throws EnumerationException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    Immersion v = (Immersion) handler.getEnumeration("OI");
    assertEquals(Immersion.OIL, v);
  }

  @Test
  public void testImmersionOIWithWhitespace() throws EnumerationException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    Immersion v = (Immersion) handler.getEnumeration("   OI  ");
    assertEquals(Immersion.OIL, v);
  }

  @Test
  public void testImmersionDRY() throws EnumerationException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    Immersion v = (Immersion) handler.getEnumeration("DRY");
    assertEquals(Immersion.AIR, v);
  }

  @Test
  public void testImmersionDRYWithWhitespace() throws EnumerationException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    Immersion v = (Immersion) handler.getEnumeration("   DRY  ");
    assertEquals(Immersion.AIR, v);
  }

  @Test
  public void testImmersionWl() throws EnumerationException {
    ImmersionEnumHandler handler = new ImmersionEnumHandler();
    Immersion v = (Immersion) handler.getEnumeration("Wl");
    assertEquals(Immersion.WATER, v);
  }

  @Test
  public void testBinning1x1WithWhitespace() throws EnumerationException {
    BinningEnumHandler handler = new BinningEnumHandler();
    Binning v = (Binning) handler.getEnumeration("   1 x 1  ");
    assertEquals(Binning.ONEBYONE, v);
  }

  @Test
  public void testBinning2x2WithWhitespace() throws EnumerationException {
    BinningEnumHandler handler = new BinningEnumHandler();
    Binning v = (Binning) handler.getEnumeration("   2 x 2  ");
    assertEquals(Binning.TWOBYTWO, v);
  }

  @Test
  public void testBinning4x4WithWhitespace() throws EnumerationException {
    BinningEnumHandler handler = new BinningEnumHandler();
    Binning v = (Binning) handler.getEnumeration("   4 x 4  ");
    assertEquals(Binning.FOURBYFOUR, v);
  }

  @Test
  public void testBinning8x8WithWhitespace() throws EnumerationException {
    BinningEnumHandler handler = new BinningEnumHandler();
    Binning v = (Binning) handler.getEnumeration("   8 x 8  ");
    assertEquals(Binning.EIGHTBYEIGHT, v);
  }
}