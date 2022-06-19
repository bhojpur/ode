package ode.formats.model;

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

import static ode.rtypes.rstring;

import static ode.formats.model.UnitsFactory.makeLength;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import loci.formats.IFormatReader;
import ode.formats.Index;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.Annotation;
import ode.model.Image;
import ode.model.Pixels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the pixels sets of an IObjectContainerStore and ensures
 * that the physical pixel dimensions, Image name and description are
 * updated to the corresponding user-provided values, if present.
 */
public class PixelsProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(PixelsProcessor.class);

  /** First file importer **/
  private Timestamp earliestMTime;

  private IFormatReader reader;

  /**
   * Processes the Bhojpur ODE client side metadata store.
   * @param store Bhojpur ODE metadata store to process.
   * @throws ModelException If there is an error during processing.
   */
  public void process(IObjectContainerStore store) throws ModelException {
    reader = store.getReader();

    List<IObjectContainer> containers =
        store.getIObjectContainers(Pixels.class);
    for (IObjectContainer container : containers) {
      Integer imageIndex = container.indexes.get(Index.IMAGE_INDEX.getValue());
      LSID imageLSID = new LSID(Image.class, imageIndex);
      Image image = (Image) store.getSourceObject(imageLSID);
      Pixels pixels = (Pixels) container.sourceObject;
      Double[] physicalPixelSizes =
          store.getUserSpecifiedPhysicalPixelSizes();
      List<Annotation> annotations = store.getUserSpecifiedAnnotations();
      if (annotations == null) {
        annotations = new ArrayList<Annotation>();
      }

      // If we have user specified annotations
      Map<LSID, IObjectContainer> containerCache = store.getContainerCache();
      for (int i = 0; i < annotations.size(); i++) {
        LSID annotationLSID = new LSID("UserSpecifiedAnnotation:" + i);
        IObjectContainer annotationContainer = new IObjectContainer();
        annotationContainer.LSID = annotationLSID.toString();
        annotationContainer.sourceObject = annotations.get(i);
        containerCache.put(annotationLSID, annotationContainer);
        store.addReference(imageLSID, annotationLSID);
      }

      // If we have user specified physical pixel sizes
      if (physicalPixelSizes != null) {
        if (physicalPixelSizes[0] != null &&
            pixels.getPhysicalSizeX() == null) {
          pixels.setPhysicalSizeX(makeLength(
              physicalPixelSizes[0], UnitsFactory.Pixels_PhysicalSizeX));
        }
        if (physicalPixelSizes[1] != null &&
            pixels.getPhysicalSizeY() == null) {
          pixels.setPhysicalSizeY(makeLength(
              physicalPixelSizes[1], UnitsFactory.Pixels_PhysicalSizeY));
        }
        if (physicalPixelSizes[2] != null &&
            pixels.getPhysicalSizeZ() == null) {
          pixels.setPhysicalSizeZ(makeLength(
              physicalPixelSizes[2], UnitsFactory.Pixels_PhysicalSizeZ));
        }
      }

      // If image is missing
      if (image == null) {
        LinkedHashMap<Index, Integer> indexes =
            new LinkedHashMap<Index, Integer>();
        indexes.put(Index.IMAGE_INDEX, imageIndex);
        container = store.getIObjectContainer(Image.class, indexes);
        image = (Image) container.sourceObject;
      }

      // Ensure that the Image name is set
      String userSpecifiedName = store.getUserSpecifiedName();
      if (userSpecifiedName != null) {
        userSpecifiedName = userSpecifiedName.trim();
        if (userSpecifiedName.isEmpty()) {
          userSpecifiedName = null;
        }
      }
      if (userSpecifiedName == null) {
        File originalFile = new File(reader.getCurrentFile());
        userSpecifiedName = originalFile.getName();
      }
      String saveName = "";
      String imageName;
      if (image.getName() != null && image.getName().getValue() != null) {
        imageName = image.getName().getValue().trim();
        if (imageName.isEmpty()) {
          imageName = null;
        }
      } else {
        imageName = null;
      }
      if (userSpecifiedName != null) {
        saveName = userSpecifiedName;

        if (reader.getSeriesCount() > 1) {
          if (imageName == null) {
            imageName = Integer.toString(imageIndex);
          }
          saveName += " [" + imageName + "]";
        }
      } else {
        saveName = imageName;
      }
      if (saveName != null && saveName.length() > 255) {
        saveName = '…' + saveName.substring(saveName.length() - 254);
      }
      image.setName(rstring(saveName));

      // Set the Image description if one was supplied by the user
      String userSpecifiedDescription = store.getUserSpecifiedDescription();
      if (userSpecifiedDescription != null) {
        image.setDescription(rstring(userSpecifiedDescription));
      }
    }
  }
}