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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ode.formats.Index;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.DetectorSettings;
import ode.model.LightPath;
import ode.model.LightSettings;
import ode.model.ObjectiveSettings;
import ode.model.WellSample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes the references of an IObjectContainerStore and ensures
 * that container references are consistent with the LSID stored in the
 * IObjectContainer itself. It also keeps track of all LSID references
 * in their string form so that they may be given to the IObjectContainerStore.
 */
public class ReferenceProcessor implements ModelProcessor {

  /** Logger for this class */
  private Logger log = LoggerFactory.getLogger(ReferenceProcessor.class);

  /* (non-Javadoc)
   * @see ode.formats.model.ModelProcessor#process(ode.formats.model.IObjectContainerStore)
   */
  public void process(IObjectContainerStore store) throws ModelException {
    Map<String, String[]> referenceStringCache =
      new HashMap<String, String[]>();
    Map<LSID, List<LSID>> referenceCache = store.getReferenceCache();
    Map<LSID, IObjectContainer> containerCache = store.getContainerCache();
    try {
      for (LSID target : referenceCache.keySet()) {
        IObjectContainer container = containerCache.get(target);
        Class targetClass = target.getJavaClass();
        List<String> references = new ArrayList<String>();
        for (LSID reference : referenceCache.get(target)) {
          if (container == null) {
            // Handle the cases where a "Settings" object has been
            // used to link an element of the Instrument to the
            // Image but there were no acquisition specific
            // settings to record. Hence, the "Settings" object
            // needs to be created as no MetadataStore methods
            // pertaining to the "Settings" object have been entered.
            LinkedHashMap<Index, Integer> indexes =
                new LinkedHashMap<Index, Integer>();
            int[] indexArray = target.getIndexes();
            if (targetClass == null) {
              log.warn("Unknown target class for LSID: " + target);
              references.add(reference.toString());
              continue;
            } else if (targetClass.equals(DetectorSettings.class)) {
              indexes.put(Index.IMAGE_INDEX, indexArray[0]);
              indexes.put(Index.CHANNEL_INDEX, indexArray[1]);
            } else if (targetClass.equals(LightSettings.class)) {
              if (indexArray.length == 2) {
                indexes.put(Index.IMAGE_INDEX, indexArray[0]);
                indexes.put(Index.CHANNEL_INDEX, indexArray[1]);
              } else if (indexArray.length == 3) {
                indexes.put(Index.EXPERIMENT_INDEX, indexArray[0]);
                indexes.put(
                    Index.MICROBEAM_MANIPULATION_INDEX, indexArray[1]);
                indexes.put(Index.LIGHT_SOURCE_SETTINGS_INDEX, indexArray[2]);
              }
            } else if (targetClass.equals(ObjectiveSettings.class)) {
              indexes.put(Index.IMAGE_INDEX, indexArray[0]);
            } else if (targetClass.equals(WellSample.class)) {
              // A WellSample has been used to link an Image to
              // a Well and there was no acquisition specific
              // metadata to record about the WellSample. We now
              // need to create it.
              indexes.put(Index.PLATE_INDEX, indexArray[0]);
              indexes.put(Index.WELL_INDEX, indexArray[1]);
              indexes.put(Index.WELL_SAMPLE_INDEX, indexArray[2]);
            } else if (targetClass.equals(LightPath.class)) {
              // A LightPath has been used to link emission or
              // excition filters and / or a dichroic to an
              // image. We now need to create it.
              indexes.put(Index.IMAGE_INDEX, indexArray[0]);
              indexes.put(Index.CHANNEL_INDEX, indexArray[1]);
            } else {
              throw new RuntimeException(
                  String.format("Unable to synchronize reference %s --> %s",
                                reference, target));
            }
            container = store.getIObjectContainer(targetClass, indexes);
          }
          // Add our LSIDs to the string based reference cache.
          references.add(reference.toString());
        }
        String lsid = targetClass == null? target.toString() : container.LSID;
        // We don't want to overwrite any existing references that may
        // have come from other LSID mappings (such as a generated
        // LSID) so add any existing LSIDs to the list of references.
        if (referenceStringCache.containsKey(lsid)) {
          String[] existing = referenceStringCache.get(lsid);
          references.addAll(Arrays.asList(existing));
        }
        String[] referencesAsString =
            references.toArray(new String[references.size()]);
        referenceStringCache.put(lsid, referencesAsString);
      }
      store.setReferenceStringCache(referenceStringCache);
    }
    catch (Exception e) {
      throw new ModelException("Error processing references.", null, e);
    }
  }
}