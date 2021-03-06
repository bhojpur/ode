package loci.formats.tools;

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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.gui.BufferedImageWriter;
import loci.formats.meta.IMetadata;
import loci.formats.out.ODETiffWriter;
import loci.formats.services.ODEXMLService;
import ode.xml.model.enums.EnumerationException;

/**
 * Creates sample Bhojpur ODE-TIFF datasets according to the given parameters.
 */
public class MakeTestOdeTiff {

  public int sizeZsub = 1;
  public int sizeTsub = 1;
  public int sizeCsub = 1;
  public boolean isModulo = false;

  public void makeSamples() throws FormatException, IOException {
    makeOdeTiffExtensions("single-channel", "439", "167", "1", "1", "1", "XYZCT");
    makeOdeTiffExtensions("multi-channel", "439", "167", "1", "3", "1", "XYZCT");
    makeOdeTiffExtensions("z-series", "439", "167", "5", "1", "1", "XYZCT");
    makeOdeTiffExtensions("multi-channel-z-series", "439", "167", "5", "3", "1", "XYZCT");
    makeOdeTiffExtensions("time-series", "439", "167", "1", "1", "7", "XYZCT");
    makeOdeTiffExtensions("multi-channel-time-series", "439", "167", "1", "3", "7",
      "XYZCT");
    makeOdeTiffExtensions("4D-series", "439", "167", "5", "1", "7", "XYZCT");
    makeOdeTiffExtensions("multi-channel-4D-series", "439", "167", "5", "3", "7",
      "XYZCT");
    makeOdeTiffExtensions("modulo-6D-Z", "250", "200", "8", "3", "2",
      "XYZCT", "4", "1", "1");
    makeOdeTiffExtensions("modulo-6D-C", "250", "200", "4", "9", "2",
      "XYZCT", "1", "3", "1");
    makeOdeTiffExtensions("modulo-6D-T", "250", "200", "4", "3", "6",
      "XYZCT", "1", "1", "2");
    makeOdeTiffExtensions("modulo-7D-ZC", "250", "220", "8", "9", "2",
      "XYZCT", "4", "3", "1");
    makeOdeTiffExtensions("modulo-7D-CT", "250", "220", "4", "9", "6",
      "XYZCT", "1", "3", "2");
    makeOdeTiffExtensions("modulo-7D-ZT", "250", "220", "8", "3", "6",
      "XYZCT", "4", "1", "2");
    makeOdeTiffExtensions("modulo-8D", "200", "250", "8", "9", "6",
      "XYZCT", "4", "3", "2");
  }

  public int makeOdeTiff(final String... args) throws FormatException,
    IOException
  {
    if (args == null || args.length == 0) {
      makeSamples();
      return 0;
    }

    // parse command line arguments
    if (args.length != 7 && args.length != 10 ) {
      displayUsage();
      return 1;
    }
    
    if (args.length == 10 ) {
      isModulo = true;
    }

    final String name = args[0];
    final CoreMetadata info = new CoreMetadata();
    info.sizeX = Integer.parseInt(args[1]);
    info.sizeY = Integer.parseInt(args[2]);
    info.sizeZ = Integer.parseInt(args[3]);
    info.sizeC = Integer.parseInt(args[4]);
    info.sizeT = Integer.parseInt(args[5]);
    info.imageCount = info.sizeZ * info.sizeC * info.sizeT;
    info.dimensionOrder = args[6].toUpperCase();

    if (isModulo) {
      sizeZsub = Integer.parseInt(args[7]);
      sizeCsub = Integer.parseInt(args[8]);
      sizeTsub = Integer.parseInt(args[9]);
    }
    
    makeOdeTiff(name, info);
    return 0;
  }

  public void makeOdeTiffExtensions(final String... args) throws FormatException,
    IOException
  {
    final String name = args[0];

    args[0] = name + ".ode.tif";
    makeOdeTiff(args);
    args[0] = name + ".ode.tiff";
    makeOdeTiff(args);
    args[0] = name + ".ode.tf2";
    makeOdeTiff(args);
    args[0] = name + ".ode.tf8";
    makeOdeTiff(args);
    args[0] = name + ".ode.btf";
    makeOdeTiff(args);
  }

  public void makeOdeTiff(final String name, final CoreMetadata info)
    throws FormatException, IOException
  {
    final String id = getId(name);
    try (ODETiffWriter out = createWriter(name, info, id)) {
      writeData(name, info, id, out);
    }
  }

  public static void main(final String[] args) throws FormatException,
    IOException
  {
    final int returnCode = new MakeTestOdeTiff().makeOdeTiff(args);
    System.exit(returnCode);
  }

  // -- Helper methods --

  private void displayUsage() {
    System.out.println("Usage: java loci.formats.tools.MakeTestOdeTiff name");
    System.out.println("           SizeX SizeY SizeZ SizeC SizeT DimOrder");
    System.out.println();
    System.out.println("  name: output filename");
    System.out.println("  SizeX: width of image planes");
    System.out.println("  SizeY: height of image planes");
    System.out.println("  SizeZ: number of focal planes");
    System.out.println("  SizeC: number of channels");
    System.out.println("  SizeT: number of time points");
    System.out.println("  DimOrder: planar ordering:");
    System.out.println("    XYZCT, XYZTC, XYCZT, XYCTZ, XYTZC, or XYTCZ");
    System.out.println();
    System.out.println("Example:");
    System.out.println("  java loci.formats.tools.MakeTestOdeTiff test \\");
    System.out.println("    517 239 5 3 4 XYCZT");
    System.out.println();
    System.out.println("Optional Usage: java loci.formats.tools.MakeTestOdeTiff name");
    System.out.println("           SizeX SizeY SizeZ SizeC SizeT DimOrder SubZ SubC SubT");
    System.out.println("This creates a 6D, 7D, or 8D file using the Modulo extension");
    System.out.println();
    System.out.println("  SubZ: splits of Z planes into extra dimension");
    System.out.println("  SubC: splits of channels into extra dimension");
    System.out.println("  SubT: splits of time points into extra dimension");
    System.out.println("A value of 1 means no split");
    System.out.println();
    System.out.println("Example:");
    System.out.println("  java loci.formats.tools.MakeTestOdeTiff test8D \\");
    System.out.println("    200 250 6 4 8 XYCZT 3 2 2");
  }

  private String getId(final String name) {
    final String id;
    if (name.toLowerCase().endsWith(".ode.tiff") ||
        name.toLowerCase().endsWith(".ode.tif") ||
        name.toLowerCase().endsWith(".ode.tf2") ||
        name.toLowerCase().endsWith(".ode.tf8") ||
        name.toLowerCase().endsWith(".ode.btf")) {
        id = name;
    } else {
        id = name + ".ode.tiff";
    }
    return id;
  }

  private ODETiffWriter createWriter(final String name,
    final CoreMetadata info, final String id) throws FormatException,
    IOException
  {
    final ODETiffWriter out = new ODETiffWriter();
    try {
      out.setMetadataRetrieve(createMetadata(name, info));
    }
    catch (final DependencyException e) {
      throw new FormatException(e);
    }
    catch (final ServiceException e) {
      throw new FormatException(e);
    }
    catch (final EnumerationException e) {
      throw new FormatException(e);
    }
    ensureNonExisting(id);
    out.setId(id);
    return out;
  }

  private void writeData(final String name, final CoreMetadata info,
    final String id, final ODETiffWriter out) throws FormatException,
    IOException
  {
    System.out.print(id);
    for (int i = 0; i < info.imageCount; i++) {
      final BufferedImage plane = createPlane(name, info, i);
      out.saveBytes(i, BufferedImageWriter.toBytes(plane, out));
      System.out.print(".");
    }
    System.out.println();
    out.close();
  }

  private void ensureNonExisting(final String id) {
    final File idFile = new File(id);
    if (idFile.exists()) idFile.delete();
  }

  private IMetadata createMetadata(final String name, final CoreMetadata info)
    throws DependencyException, ServiceException, EnumerationException
  {
    final ServiceFactory serviceFactory = new ServiceFactory();
    final ODEXMLService odexmlService =
      serviceFactory.getInstance(ODEXMLService.class);
    final IMetadata meta = odexmlService.createODEXMLMetadata();
    MetadataTools.populateMetadata(meta, 0, name, info);
    
    if (isModulo) {
      meta.setXMLAnnotationID("Annotation:Modulo:0", 0);
      meta.setXMLAnnotationNamespace("bhojpur.net/ode/dimension/modulo", 0);
      meta.setXMLAnnotationDescription("For a description of how 6D, 7D, and 8D data is stored using the Modulo extension see https://docs.bhojpur.net/latest/ode-model/developers/6d-7d-and-8d-storage.html", 0);
      StringBuilder moduloBlock = new StringBuilder();
      
      moduloBlock.append("<Modulo namespace=\"http://www.bhojpur.net/Schemas/Additions/2011-09\">");
      if (sizeZsub != 1) {
        moduloBlock.append("<ModuloAlongZ Type=\"other\" TypeDescription=\"Example Data Over Z-Plane\" Start=\"0\" Step=\"1\" End=\"");
        moduloBlock.append(sizeZsub);
        moduloBlock.append("\"/>");
      }
      if (sizeTsub != 1) {
        moduloBlock.append("<ModuloAlongT Type=\"other\" TypeDescription=\"Example Data Over Time \" Start=\"0\" Step=\"1\" End=\"");
        moduloBlock.append(sizeTsub);
        moduloBlock.append("\"/>");
      }
      if (sizeCsub != 1) {
        moduloBlock.append("<ModuloAlongC Type=\"other\" TypeDescription=\"Example Data Over Channel\" Start=\"0\" Step=\"1\" End=\"");
        moduloBlock.append(sizeCsub);
        moduloBlock.append("\"/>");
      }
      moduloBlock.append("</Modulo>");
      
      meta.setXMLAnnotationValue(moduloBlock.toString(), 0);

      meta.setImageAnnotationRef("Annotation:Modulo:0", 0, 0);
    }
    return meta;
  }

  private BufferedImage createPlane(final String name, final CoreMetadata info,
    final int no)
  {
    final int[] zct =
      FormatTools.getZCTCoords(info.dimensionOrder, info.sizeZ, info.sizeC,
        info.sizeT, info.imageCount, no);

    final BufferedImage plane =
      new BufferedImage(info.sizeX, info.sizeY, BufferedImage.TYPE_BYTE_GRAY);
    final Graphics2D g = plane.createGraphics();

    // draw gradient
    final int type = 0;
    for (int y = 0; y < info.sizeY; y++) {
      final int v = gradient(type, y, info.sizeY);
      g.setColor(new Color(v, v, v));
      g.drawLine(0, y, info.sizeX, y);
    }

    // build list of text lines from planar information
    final ArrayList<TextLine> lines = new ArrayList<TextLine>();
    final Font font = g.getFont();
    lines.add(new TextLine(name, font.deriveFont(32f), 5, -5));
    lines.add(new TextLine(info.sizeX + " x " + info.sizeY, font.deriveFont(
      Font.ITALIC, 16f), 20, 10));
    lines.add(new TextLine(info.dimensionOrder, font.deriveFont(Font.ITALIC,
      14f), 30, 5));
    int space = 5;
    if (info.sizeZ > 1) {
      lines.add(new TextLine(
        "Focal plane = " + (zct[0] + 1) + "/" + info.sizeZ, font, 20, space));
      space = 2;
    }
    if (info.sizeC > 1) {
      lines.add(new TextLine("Channel = " + (zct[1] + 1) + "/" + info.sizeC,
        font, 20, space));
      space = 2;
    }
    if (info.sizeT > 1) {
      lines.add(new TextLine("Time point = " + (zct[2] + 1) + "/" + info.sizeT,
        font, 20, space));
      space = 2;
    }

    if (isModulo) {
      if (sizeZsub > 1) {
        lines.add(new TextLine("True-Z point = " + ((zct[0]/sizeZsub) + 1) + "/" + info.sizeZ/sizeZsub,
          font, 20, space));
        space = 2;
      }
      if (sizeZsub > 1) {
        lines.add(new TextLine("Sub-Z = " + ((zct[0]%sizeZsub) + 1) + "/" + sizeZsub,
          font, 20, space));
        space = 2;
      }
      if (sizeCsub > 1) {
        lines.add(new TextLine("True Channel = " + ((zct[1]/sizeCsub) + 1) + "/" + info.sizeC/sizeCsub,
          font, 20, space));
        space = 2;
      }
      if (sizeCsub > 1) {
        lines.add(new TextLine("Sub Channel = " + ((zct[1]%sizeCsub) + 1) + "/" + sizeCsub,
          font, 20, space));
        space = 2;
      }
      if (sizeTsub > 1) {
        lines.add(new TextLine("True-T point = " + ((zct[2]/sizeTsub) + 1) + "/" + info.sizeT/sizeTsub,
          font, 20, space));
        space = 2;
      }
      if (sizeTsub > 1) {
        lines.add(new TextLine("Sub-T = " + ((zct[2]%sizeTsub) + 1) + "/" + sizeTsub,
          font, 20, space));
        space = 2;
      }
    }
    // draw text lines to image
    g.setColor(Color.white);
    int yoff = 0;
    for (int l = 0; l < lines.size(); l++) {
      final TextLine text = lines.get(l);
      g.setFont(text.font);
      final Rectangle2D r =
        g.getFont().getStringBounds(text.line, g.getFontRenderContext());
      yoff += r.getHeight() + text.ypad;
      g.drawString(text.line, text.xoff, yoff);
    }
    g.dispose();

    return plane;
  }

  private int gradient(final int type, final int num, final int total) {
    final int max = 96;
    final int split = type / 2 + 1;
    final boolean reverse = type % 2 == 0;
    int v = max;
    final int splitTotal = total / split;
    for (int i = 1; i <= split + 1; i++) {
      if (num < i * splitTotal) {
        if (i % 2 == 0) v = max * (num % splitTotal) / splitTotal;
        else v = max * (splitTotal - num % splitTotal) / splitTotal;
        break;
      }
    }
    if (reverse) v = max - v;
    return v;
  }

  // -- Helper classes --

  private static class TextLine {

    final String line;
    final Font font;
    final int xoff;
    final int ypad;

    TextLine(final String line, final Font font, final int xoff, final int ypad)
    {
      this.line = line;
      this.font = font;
      this.xoff = xoff;
      this.ypad = ypad;
    }

  }

}
