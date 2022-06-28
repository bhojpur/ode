package loci.formats.dicom;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of valid DICOM value representations (VRs).
 * See http://dicom.nema.org/medical/dicom/current/output/html/part05.html#sect_6.2
 */
public enum DicomVR {
  AE(0x4145, 1),
  AS(0x4153, 1),
  AT(0x4154, 2),
  CS(0x4353, 1),
  DA(0x4441, 1),
  DS(0x4453, 1),
  DT(0x4454, 1),
  FD(0x4644, 8),
  FL(0x464C, 4),
  IS(0x4953, 1),
  LO(0x4C4F, 1),
  LT(0x4C54, 1),
  OB(0x4F42, 1),
  OD(0x4F44, 8),
  OF(0x4F46, 4),
  OL(0x4F4C, 4),
  OV(0x4F56, 8),
  OW(0x4F57, 2),
  PN(0x504E, 1),
  SH(0x5348, 1),
  SL(0x534C, 4),
  SQ(0x5351, 0),
  SS(0x5353, 2),
  ST(0x5354, 1),
  SV(0x5356, 8),
  TM(0x544D, 1),
  UC(0x5543, 1),
  UI(0x5549, 1),
  UL(0x554C, 4),
  UN(0x554E, 1),
  UR(0x5552, 1),
  US(0x5553, 2),
  UT(0x5554, 1),
  UV(0x5556, 8),
  QQ(0x3F3F, 1),
  IMPLICIT(0x2D2D, 0),
  RESERVED(0xFFFF, 0);

  private int code;
  // number of bytes in one array element
  private int width;

  private static final Map<Integer, DicomVR> lookup = new HashMap<Integer, DicomVR>();

  static {
    for (DicomVR v : EnumSet.allOf(DicomVR.class)) {
      lookup.put(v.getCode(), v);
    }
  }

  private DicomVR(int code, int width) {
    this.code = code;
    this.width = width;
  }

  public int getCode() {
    return code;
  }

  public int getWidth() {
    return width;
  }

  public static DicomVR get(int code) {
    return lookup.get(code);
  }

}
