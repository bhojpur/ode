/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

package loci.formats.services;

import java.io.IOException;

import loci.common.RandomAccessInputStream;
import loci.common.services.Service;
import loci.common.services.ServiceException;

public interface JPEGTurboService extends Service {

  long[] getRestartMarkers();

  void setRestartMarkers(long[] markers);

  void initialize(RandomAccessInputStream jpeg, int width, int height)
    throws ServiceException, IOException;

  byte[] getTile(byte[] buf, int xCoordinate, int yCoordinate, int width,
    int height)
    throws IOException;

  byte[] getTile(int xTile, int yTile) throws IOException;

  void close() throws IOException;

}
