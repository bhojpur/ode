/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.services;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import loci.common.services.Service;
import loci.common.services.ServiceException;

/**
 *
 */
public interface EXIFService extends Service {

  void initialize(String file) throws ServiceException, IOException;

  HashMap<String, String> getTags();

  Date getCreationDate();

  void close() throws IOException;

}
