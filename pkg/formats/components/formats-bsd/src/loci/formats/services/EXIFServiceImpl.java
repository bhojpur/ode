package loci.formats.services;

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
