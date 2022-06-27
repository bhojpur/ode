/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.codec;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.imageio.ImageIO;

import loci.common.DataTools;
import loci.common.RandomAccessInputStream;
import loci.formats.FormatException;
import loci.formats.gui.AWTImageTools;

/**
 * This class implements JPEG compression and decompression.
 */
public class JPEGCodec extends WrappedCodec {
  public JPEGCodec() {
    super(new ode.codecs.JPEGCodec());
  }
}
