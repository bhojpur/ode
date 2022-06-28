package loci.formats;

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

/**
 * Represents a subdimension of Z, C, or T as needed for supporting Modulo
 * annotations.  See https://docs.bhojpur.net/latest/ode-model/developers/6d-7d-and-8d-storage.html
 */
public class Modulo {

  // -- Fields --

  public String parentDimension;
  public double start = 0;
  public double step = 1;
  public double end = 0;
  public String parentType;
  public String type;
  public String typeDescription;
  public String unit;
  public String[] labels;

  // -- Constructor --

  public Modulo(String dimension) {
    parentDimension = dimension;
  }

  public Modulo(Modulo m) {
    this.parentDimension = m.parentDimension;
    this.start = m.start;
    this.step = m.step;
    this.end = m.end;
    this.parentType = m.parentType;
    this.type = m.type;
    this.typeDescription = m.typeDescription;
    this.unit = m.unit;
    this.labels = m.labels;
  }

  // -- Methods --

  public int length() {
    if (labels != null) {
      return labels.length;
    }
    int len = (int) Math.rint((end - start) / step) + 1;
    return (len < 1) ? 1 : len; // Can't ever be less than 1.
  }

  public String toXMLAnnotation() {
    StringBuffer xml = new StringBuffer("<ModuloAlong");
    xml.append(parentDimension);
    xml.append(" Type=\"");

    if (type != null) {
      type = type.toLowerCase();
    }
    if (type == null || (!type.equals("angle") && !type.equals("phase") &&
      !type.equals("tile") && !type.equals("lifetime") &&
      !type.equals("lambda")))
    {
      if (typeDescription == null) {
        typeDescription = type;
      }
      type = "other";
    }
    xml.append(type);
    xml.append("\"");
    if (typeDescription != null) {
      xml.append(" TypeDescription=\"");
      xml.append(typeDescription);
      xml.append("\"");
    }
    if (unit != null) {
      xml.append(" Unit=\"");
      xml.append(unit);
      xml.append("\"");
    }
    if (end > start) {
      xml.append(" Start=\"");
      xml.append(start);
      xml.append("\" Step=\"");
      xml.append(step);
      xml.append("\" End=\"");
      xml.append(end);
      xml.append("\"");
    }
    if (labels != null) {
      xml.append(">");
      for (String label : labels) {
        xml.append("\n<Label>");
        xml.append(label);
        xml.append("</Label>");
      }
      xml.append("\n</ModuloAlong");
      xml.append(parentDimension);
      xml.append(">");
    }
    else {
      xml.append("/>");
    }

    return xml.toString();
  }

}
