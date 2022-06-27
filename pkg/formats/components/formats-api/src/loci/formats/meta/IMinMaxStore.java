/*
 * Top-level reader and writer APIs
 */

package loci.formats.meta;

/**
 * Interface for consumers of minima and maxima pixel intensities across a
 * given wavelength.
 */
public interface IMinMaxStore {

  /**
   * Populates the channel global minimum and maximum.
   * @param channel Channel index to populate.
   * @param minimum Minimum global pixel intensity.
   * @param maximum Maximum global pixel intensity.
   * @param series Image series.
   */
  void setChannelGlobalMinMax(int channel, double minimum, double maximum,
    int series);

}
