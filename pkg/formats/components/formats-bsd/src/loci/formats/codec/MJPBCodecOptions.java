/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.codec;

/**
 * Options for compressing and decompressing MJPB data.
 */
public class MJPBCodecOptions extends CodecOptions {

  /** Indicates whether or not channels are interlaced. (READ/WRITE) */
  public boolean interlaced;

}
