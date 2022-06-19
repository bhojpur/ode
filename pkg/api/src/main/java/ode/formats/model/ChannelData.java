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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ode.formats.Index;
import ode.formats.ODEMetadataStoreClient;
import ode.util.LSID;
import ode.metadatastore.IObjectContainer;
import ode.model.Arc;
import ode.model.Channel;
import ode.model.Filament;
import ode.model.Filter;
import ode.model.FilterSet;
import ode.model.IObject;
import ode.model.Laser;
import ode.model.LightEmittingDiode;
import ode.model.LightPath;
import ode.model.LightSettings;
import ode.model.LightSource;
import ode.model.LogicalChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all the metadata required to make accurate decisions about
 * channel colour and name.
 */
public class ChannelData {

  /** Logger for this class */
  private static final Logger log = LoggerFactory.getLogger(
      ChannelProcessor.class);

  /** Base channel data. */
  private Channel channel;

  /** Index of the channel for the image. */
  private Integer channelIndex;

  /** Channel --> LogicalChannel */
  private LogicalChannel logicalChannel;

  /** ... LogicalChannel --> Filterset */
  private FilterSet filterSet;

  /** ... LogicalChannel --> FilterSet --> Filter (Em) */
  private Filter filterSetEmFilter;

  /** ... LogicalChannel --> FilterSet --> Filter (Ex) */
  private Filter filterSetExFilter;

  /** ... LogicalChannel --> LightPath */
  private LightPath lightPath;

  /** ... LogicalChannel --> LightPath --> Filter (Em) */
  private List<Filter> lightPathEmFilters;

  /** ... LogicalChannel --> LightPath --> Filter (Ex) */
  private  List<Filter> lightPathExFilters;

  /** ... LogicalChannel --> LightSettings */
  private LightSettings lightSourceSettings;

  /** ... LogicalChannel --> LightSettings --> LightSource */
  private LightSource lightSource;

  /** Exhaustive list of valid light source types from the model. */
  private static final Class<?>[] LIGHT_SOURCE_TYPES = new Class<?>[]
    { Arc.class, Filament.class, LightEmittingDiode.class, Laser.class };

  /**
   * Retrieves channel data from an object container store.
   * @param store Store to retrieve the channel data from.
   * @param imageIndex Index of the image to retrieve channel data for.
   * @param channelIndex Index of the channel to retrieve channel data for.
   * @return Populated channel data for the specified channel.
   */
  public static ChannelData fromObjectContainerStore(
      IObjectContainerStore store, int imageIndex, int channelIndex) {
    Map<LSID, List<LSID>> referenceCache = store.getReferenceCache();
    Map<Class<? extends IObject>, Map<String, IObjectContainer>>
      containerCache = store.getAuthoritativeContainerCache();
    ChannelData data = new ChannelData();
    String lsidString;

    // Channel
    data.channel = (Channel) store.getSourceObject(
        new LSID(Channel.class, imageIndex, channelIndex));
    data.channelIndex = channelIndex;

    if (data.channel == null) {
      // Channel is missing, create it.
      LinkedHashMap<Index, Integer> indexes =
        new LinkedHashMap<Index, Integer>();
      indexes.put(Index.IMAGE_INDEX, imageIndex);
      indexes.put(Index.CHANNEL_INDEX, channelIndex);
      IObjectContainer container =
        store.getIObjectContainer(Channel.class, indexes);
      data.channel = (Channel) container.sourceObject;
    }
    // Channel --> LogicalChannel
    LSID logicalChannelLSID =
      new LSID(LogicalChannel.class, imageIndex, channelIndex);
    data.logicalChannel =
      (LogicalChannel) store.getSourceObject(logicalChannelLSID);
    if (data.logicalChannel == null) {
      // Channel is missing, create it.
      LinkedHashMap<Index, Integer> indexes =
        new LinkedHashMap<Index, Integer>();
      indexes.put(Index.IMAGE_INDEX, imageIndex);
      indexes.put(Index.CHANNEL_INDEX, channelIndex);
      IObjectContainer container =
        store.getIObjectContainer(LogicalChannel.class, indexes);
      data.logicalChannel = (LogicalChannel) container.sourceObject;
    }
    List<Filter> lightPathEmFilters = new ArrayList<Filter>();
    List<Filter> lightPathExFilters = new ArrayList<Filter>();
    data.lightPathEmFilters = lightPathEmFilters;
    data.lightPathExFilters = lightPathExFilters;
    // ... LogicalChannel --> FilterSet
    List<LSID> references = referenceCache.get(logicalChannelLSID);

    Map<String, IObjectContainer> filterSetContainers =
      containerCache.get(FilterSet.class);
    Map<String, IObjectContainer> lightPathContainers =
      containerCache.get(LightPath.class);
    Map<String, IObjectContainer> filterContainers =
      containerCache.get(Filter.class);
    if (references != null) {
      for (LSID reference : references) {
        lsidString = reference.toString();
        //filters set
        if (filterSetContainers != null
            && filterSetContainers.containsKey(lsidString)) {
          IObjectContainer filterSetContainer =
            filterSetContainers.get(lsidString);
          LSID filterSetLSID = new LSID(
              FilterSet.class,
              filterSetContainer.indexes.get(
                  Index.INSTRUMENT_INDEX.getValue()
              ),
              filterSetContainer.indexes.get(
                  Index.FILTER_SET_INDEX.getValue()
              )
          );
          data.filterSet = (FilterSet) filterSetContainer.sourceObject;
          // ... LogicalChannel --> FilterSet --> Filter (Em) AND
          // ... LogicalChannel --> FilterSet --> Filter (Ex)
          List<LSID> filterSetReferences = referenceCache.get(filterSetLSID);
          if (filterSetReferences == null) {
            continue;
          }
          for (LSID filterSetReference : filterSetReferences) {
            lsidString = filterSetReference.toString();
            String unsuffixed =
              lsidString.substring(0, lsidString.lastIndexOf(':'));
            if (lsidString.endsWith(
                    ODEMetadataStoreClient.ODE_EMISSION_FILTER_SUFFIX)) {
              data.filterSetEmFilter =
                (Filter) filterContainers.get(unsuffixed).sourceObject;
            }
            if (lsidString.endsWith(
                    ODEMetadataStoreClient.ODE_EXCITATION_FILTER_SUFFIX)) {
              data.filterSetExFilter =
                (Filter) filterContainers.get(unsuffixed).sourceObject;
            }
          }
        }
        /*
        if (lightPathContainers != null
              && lightPathContainers.containsKey(lsidString))
          {
            IObjectContainer lightPathContainer =
              lightPathContainers.get(lsidString);
            LSID lightPathLSID = new LSID(LightPath.class,
                imageIndex, channelIndex);
            data.lightPath =
              (LightPath) lightPathContainer.sourceObject;
            List<LSID> lightPathReferences =
              referenceCache.get(lightPathLSID);
            if (lightPathReferences == null)
            {
              continue;
            }
            for (LSID lightPathReference : lightPathReferences)
            {
              lsidString = lightPathReference.toString();
              String unsuffixed =
                lsidString.substring(0,
                    lsidString.lastIndexOf(':'));
              if (lsidString.endsWith(
                  ODEMetadataStoreClient.ODE_EMISSION_FILTER_SUFFIX))
              {
                lightPathEmFilters.add((Filter)
                filterContainers.get(unsuffixed).sourceObject);
              }
              if (lsidString.endsWith(
                  ODEMetadataStoreClient.ODE_EXCITATION_FILTER_SUFFIX))
              {
                lightPathEmFilters.add((Filter)
                filterContainers.get(unsuffixed).sourceObject);
              }
            }
          }
          */
      }
    }

    //light path
    LSID lightPathLSID = new LSID(LightPath.class, imageIndex, channelIndex);
    data.lightPath = (LightPath) store.getSourceObject(lightPathLSID);
    List<LSID> lightPathReferences = referenceCache.get(lightPathLSID);
    if (lightPathReferences != null) {
      for (LSID lightPathReference : lightPathReferences) {
        lsidString = lightPathReference.toString();
        String unsuffixed = lsidString.substring(
            0, lsidString.lastIndexOf(':'));
        if (lsidString.endsWith(
                ODEMetadataStoreClient.ODE_EMISSION_FILTER_SUFFIX)) {
          IObjectContainer c = filterContainers.get(unsuffixed);
          if (c == null) {
            throw new ModelException("No filter with LSID: " + unsuffixed);
          }
          lightPathEmFilters.add((Filter) c.sourceObject);
        }
        if (lsidString.endsWith(
                ODEMetadataStoreClient.ODE_EXCITATION_FILTER_SUFFIX)) {
          IObjectContainer c = filterContainers.get(unsuffixed);
          if (c == null) {
            throw new ModelException("No filter with LSID: " + unsuffixed);
          }
          lightPathExFilters.add((Filter) c.sourceObject);
        }
      }
    }

    // ... LogicalChannel --> LightSettings
    LSID lightSettingsLSID = new LSID(
        LightSettings.class, imageIndex, channelIndex);
    data.lightSourceSettings =
      (LightSettings) store.getSourceObject(lightSettingsLSID);
    Map<String, IObjectContainer> lightSourceContainers =
      new HashMap<String, IObjectContainer>();
    if (data.lightSourceSettings != null) {
      // Pick up all potential light sources
      for (Class<?> klass : LIGHT_SOURCE_TYPES) {
        Map<String, IObjectContainer> v = containerCache.get(klass);
        if (v != null) {
          lightSourceContainers.putAll(v);
        }
      }
      // ... LogicalChannel --> LightSettings --> LightSource
      references = referenceCache.get(lightSettingsLSID);
      if (references != null) {
        for (LSID reference : references) {
          lsidString = reference.toString();
          if (lightSourceContainers.containsKey(lsidString)) {
            data.lightSource =
              (LightSource) lightSourceContainers .get(lsidString).sourceObject;
          }
        }
      }
    }
    return data;
  }

  /**
   * Returns the channel this channel data is for.
   * @return See above.
   */
  public Channel getChannel() {
    return channel;
  }

  /**
   * Returns the index of the channel this channel data is for.
   * @return See above.
   */
  public int getChannelIndex() {
    return channelIndex;
  }

  /**
   * Returns the logical channel for this channel data.
   * @return See above.
   */
  public LogicalChannel getLogicalChannel() {
    return logicalChannel;
  }

  /**
   * Returns the filter set for the logical channel of this channel data.
   * @return See above.
   */
  public FilterSet getFilterSet() {
    return filterSet;
  }

  /**
   * Returns the filter set's emission filter for the logical channel of
   * this channel data.
   * @return See above.
   */
  public Filter getFilterSetEmissionFilter() {
    return filterSetEmFilter;
  }

  /**
   * Returns the filter set's excitation filter for the logical channel of
   * this channel data.
   * @return See above.
   */
  public Filter getFilterSetExcitationFilter() {
    return filterSetExFilter;
  }

  /**
   * Returns the collection of emission filters in the light path.
   *
   * @return See above.
   */
  public List<Filter> getLightPathEmissionFilters() {
    return lightPathEmFilters;
  }

  /**
   * Returns the collection of excitation filters in the light path.
   *
   * @return See above.
   */
  public List<Filter> getLightPathExcitationFilters() {
    return  lightPathExFilters;
  }

  /**
   * Returns the light source settings for the logical channel of this
   * channel data.
   * @return See above.
   */
  public LightSettings getLightSourceSettings() {
    return lightSourceSettings;
  }

  /**
   * Returns the light source for the light source settings of this channel
   * data.
   * @return See above.
   */
  public LightSource getLightSource() {
    return lightSource;
  }

}