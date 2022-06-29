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

import ode.xml.model.Detector;
import ode.xml.model.Instrument;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 */
public class SingularBackReferenceTest {

  public static final String DETECTOR_ID = "Detector:1";

  public static final String INSTRUMENT_ID = "Instrument:1";

  private Instrument instrument;

  private Detector detector;

  @BeforeMethod
  public void setUp() {
    instrument = new Instrument();
    instrument.setID(INSTRUMENT_ID);
    detector = new Detector();
    detector.setID("DETECTOR_ID");
    instrument.addDetector(detector);
  }

  @Test
  public void testDetectorInstrumentBackReference() {
    Instrument backReference = detector.getInstrument();
    assertEquals(1, instrument.sizeOfDetectorList());
    Detector forwardReference = instrument.getDetector(0);
    assertNotNull(forwardReference);
    assertNotNull(backReference);
    assertEquals(INSTRUMENT_ID, backReference.getID());
  }
}