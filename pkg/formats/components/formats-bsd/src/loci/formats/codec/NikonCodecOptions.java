/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * Options for compressing and decompressing Nikon NEF data.
 */
public class NikonCodecOptions extends CodecOptions {

  public int[] curve;

  public int[] vPredictor;

  public int split;

}
