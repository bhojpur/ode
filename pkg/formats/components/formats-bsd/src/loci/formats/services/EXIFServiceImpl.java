/*
 * BSD implementations of ODE-Formats readers and writers
 */

package loci.formats.services;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import loci.common.RandomAccessInputStream;
import loci.common.services.AbstractService;
import loci.common.services.ServiceException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

/**
 *
 */
public class EXIFServiceImpl extends AbstractService implements EXIFService {

  private ExifSubIFDDirectory directory;

  // -- Constructor --

  public EXIFServiceImpl() {
    // check for metadata-extractor.jar
    checkClassDependency(ImageMetadataReader.class);
    // check for xmpcore.jar
    checkClassDependency(com.adobe.xmp.XMPMeta.class);
  }

  // -- EXIFService API methods --

  @Override
  public void initialize(String file) throws ServiceException, IOException {
    try (RandomAccessInputStream jpegFile = new RandomAccessInputStream(file)) {
      try {
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
        directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
      }
      catch (Throwable e) {
        throw new ServiceException("Could not read EXIF data", e);
      }
    }
  }

  @Override
  public HashMap<String, String> getTags() {
    HashMap<String, String> tagMap = new HashMap<String, String>();
    if (directory != null) {
      for (Tag tag : directory.getTags()) {
        tagMap.put(tag.getTagName(), tag.getDescription());
      }
    }

    return tagMap;
  }

  @Override
  public Date getCreationDate() {
    if (directory != null) {
      return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }
    return null;
  }

  @Override
  public void close() throws IOException {
    directory = null;
  }

}
