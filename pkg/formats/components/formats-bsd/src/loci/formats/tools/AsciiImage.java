/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.tools;

import java.awt.image.BufferedImage;

/**
 * A utility class for outputting a BufferedImage as ASCII text.
 */
public class AsciiImage {

  // -- Constants --

  private static final String NL = System.getProperty("line.separator");
  private static final String CHARS = " .,-+o*O#";

  // -- Fields --

  private BufferedImage img;

  // -- Constructor --

  public AsciiImage(BufferedImage img) {
    this.img = img;
  }

  // -- Object methods --

  @Override
  public String toString() {
    final int width = img.getWidth();
    final int height = img.getHeight();
    final StringBuilder sb = new StringBuilder();
    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        final int pix = img.getRGB(x, y);
        final int a = 0xff & (pix >> 24);
        final int r = 0xff & (pix >> 16);
        final int g = 0xff & (pix >> 8);
        final int b = 0xff & pix;
        final int avg = (r + g + b) / 3;
        sb.append(getChar(avg));
      }
      sb.append(NL);
    }
    return sb.toString();
  }

  // -- Helper methods --

  private char getChar(int value) {
    int index = CHARS.length() * value / 256;
    return CHARS.charAt(index);
  }

}
