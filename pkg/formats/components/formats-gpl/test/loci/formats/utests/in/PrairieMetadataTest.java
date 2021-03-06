package loci.formats.utests.in;

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

import static org.testng.AssertJUnit.assertEquals;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import loci.common.xml.XMLTools;
import loci.formats.in.PrairieMetadata;
import loci.formats.in.PrairieMetadata.Value;
import loci.formats.in.PrairieMetadata.ValueTable;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Unit tests for {@link PrairieMetadata}.
 */
public class PrairieMetadataTest {

  private static final String OLD_XML =
    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
    "<PVScan version=\"4.0.0.53\" date=\"3/23/2012 9:47:15 AM\" notes=\"\">" +
    "  <Sequence type=\"TSeries ZSeries Element\" cycle=\"1\" >" +
    "    <Frame relativeTime=\"0\" absoluteTime=\"2.06929400000001\" index=\"1\" label=\"CurrentSettings\">" +
    "      <File channel=\"1\" channelName=\"Ch1\" preAmpID=\"0\" filename=\"TSeries-03232012-0934-006_Cycle001_CurrentSettings_Ch1_000001.tif\" />" +
    "      <File channel=\"2\" channelName=\"Ch2\" preAmpID=\"1\" filename=\"TSeries-03232012-0934-006_Cycle001_CurrentSettings_Ch2_000001.tif\" />" +
    "      <ExtraParameters validData=\"True\" />" +
    "      <PVStateShard>" +
    "        <Key key=\"linesPerFrame\" permissions=\"Read, Write, Save\" value=\"186\" />" +
    "        <Key key=\"pmtGain_0\" permissions=\"Write, Save\" value=\"605\" />" +
    "        <Key key=\"pmtGain_1\" permissions=\"Write, Save\" value=\"604\" />" +
    "        <Key key=\"pmtGain_2\" permissions=\"Write, Save\" value=\"0\" />" +
    "        <Key key=\"positionCurrent_XAxis\" permissions=\"Write, Save\" value=\"0.95\" />" +
    "        <Key key=\"positionCurrent_YAxis\" permissions=\"Write, Save\" value=\"-4.45\" />" +
    "        <Key key=\"positionCurrent_ZAxis\" permissions=\"Write, Save\" value=\"-9,62.45\" />" +
    "      </PVStateShard>" +
    "    </Frame>" +
    "  </Sequence>" +
    "</PVScan>";

  private static final String NEW_XML =
    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
    "<PVScan version=\"5.2.64.400\" date=\"9/23/2014 3:03:37 PM\" notes=\"\">" +
    "  <PVStateShard>" +
    "    <PVStateValue key=\"linesPerFrame\" value=\"186\" />" +
    "    <PVStateValue key=\"pmtGain\">" +
    "        <IndexedValue index=\"0\" value=\"605\" description=\"Ch1 High Voltage\" />" +
    "        <IndexedValue index=\"1\" value=\"604\" description=\"Ch2 High Voltage\" />" +
    "        <IndexedValue index=\"2\" value=\"0\" description=\"Ch3 High Voltage\" />" +
    "    </PVStateValue>" +
    "    <PVStateValue key=\"positionCurrent\">" +
    "        <SubindexedValues index=\"XAxis\">" +
    "            <SubindexedValue subindex=\"0\" value=\"0.95\" />" +
    "        </SubindexedValues>" +
    "        <SubindexedValues index=\"YAxis\">" +
    "            <SubindexedValue subindex=\"0\" value=\"-4.45\" />" +
    "        </SubindexedValues>" +
    "        <SubindexedValues index=\"ZAxis\">" +
    "            <SubindexedValue subindex=\"0\" value=\"-9\" description=\"Focus\" />" +
    "            <SubindexedValue subindex=\"1\" value=\"62.45\" description=\"Piezo\" />" +
    "        </SubindexedValues>" +
    "    </PVStateValue>" +
    "  </PVStateShard>" +
    "  <Sequence type=\"TSeries Timed Element\" cycle=\"1\" time=\"15:03:38.0036337\">" +
    "    <PVStateShard>" +
    "      <PVStateValue key=\"positionCurrent\">" +
    "        <SubindexedValues index=\"XAxis\">" +
    "          <SubindexedValue subindex=\"0\" value=\"-621.412879412341\" />" +
    "        </SubindexedValues>" +
    "        <SubindexedValues index=\"YAxis\">" +
    "          <SubindexedValue subindex=\"0\" value=\"255.652372573538\" />" +
    "        </SubindexedValues>" +
    "        <SubindexedValues index=\"ZAxis\">" +
    "          <SubindexedValue subindex=\"0\" value=\"28.15\" description=\"Z\" />" +
    "          <SubindexedValue subindex=\"1\" value=\"111.23\" description=\"ZPiezo\" />" +
    "        </SubindexedValues>" +
    "      </PVStateValue>" +
    "    </PVStateShard>" +
    "    <Frame relativeTime=\"0\" absoluteTime=\"1.2349999999999\" index=\"1\" parameterSet=\"CurrentSettings\">" +
    "      <File channel=\"1\" channelName=\"Ch1 Red\" filename=\"TSeries-09232014-1445-011_Cycle00001_Ch1_000001.ode.tif\" />" +
    "      <ExtraParameters lastGoodFrame=\"0\" />" +
    "      <PVStateShard />" +
    "    </Frame>" +
    "  </Sequence>" +
    "</PVScan>";

  @Test
  public void testParseOldXML() throws ParserConfigurationException,
    SAXException, IOException
  {
    final Document xml = XMLTools.parseDOM(OLD_XML);
    final PrairieMetadata meta = new PrairieMetadata(xml, null, null);

    final Value positionCurrent =
      meta.getSequence(1).getFrame(1).getValue("positionCurrent");

    final Value xAxis = positionCurrent.get("XAxis");
    assertEquals("0.95", xAxis.value());

    final Value yAxis = positionCurrent.get("YAxis");
    assertEquals("-4.45", yAxis.value());

    final ValueTable zAxis = (ValueTable) positionCurrent.get("ZAxis");
    assertEquals("-9", zAxis.get(0).value());
    assertEquals("62.45", zAxis.get(1).value());
  }

  @Test
  public void testParseNewXML() throws ParserConfigurationException,
    SAXException, IOException
  {
    final Document xml = XMLTools.parseDOM(NEW_XML);
    final PrairieMetadata meta = new PrairieMetadata(xml, null, null);

    final ValueTable positionCurrent = (ValueTable)
      meta.getSequence(1).getFrame(1).getValue("positionCurrent");

    final ValueTable xAxis = (ValueTable) positionCurrent.get("XAxis");
    assertEquals("-621.412879412341", xAxis.value());

    final ValueTable yAxis = (ValueTable) positionCurrent.get("YAxis");
    assertEquals("255.652372573538", yAxis.value());

    final ValueTable zAxis = (ValueTable) positionCurrent.get("ZAxis");
    assertEquals("28.15", zAxis.get(0).value());
    assertEquals("111.23", zAxis.get(1).value());
  }
}
