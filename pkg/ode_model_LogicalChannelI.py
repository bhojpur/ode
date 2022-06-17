#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

import Ice
import IceImport
import ode
IceImport.load("ode_model_DetailsI")
IceImport.load("ode_model_LogicalChannel_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class LogicalChannelI(_ode_model.LogicalChannel):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "name",
          "pinHoleSize",
          "illumination",
          "contrastMethod",
          "excitationWave",
          "emissionWave",
          "fluor",
          "ndFilter",
          "otf",
          "detectorSettings",
          "lightSourceSettings",
          "filterSet",
          "samplesPerPixel",
          "photometricInterpretation",
          "mode",
          "pockelCellSetting",
          "channels",
          "lightPath",
          "details",
      ])
      _field_info = _field_info_type(
          name=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          pinHoleSize=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          illumination=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          contrastMethod=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          excitationWave=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          emissionWave=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          fluor=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          ndFilter=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          otf=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          detectorSettings=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          lightSourceSettings=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          filterSet=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          samplesPerPixel=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          photometricInterpretation=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          mode=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          pockelCellSetting=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          channels=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          lightPath=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      NAME =  "ode.model.core.LogicalChannel_name"
      PINHOLESIZE =  "ode.model.core.LogicalChannel_pinHoleSize"
      ILLUMINATION =  "ode.model.core.LogicalChannel_illumination"
      CONTRASTMETHOD =  "ode.model.core.LogicalChannel_contrastMethod"
      EXCITATIONWAVE =  "ode.model.core.LogicalChannel_excitationWave"
      EMISSIONWAVE =  "ode.model.core.LogicalChannel_emissionWave"
      FLUOR =  "ode.model.core.LogicalChannel_fluor"
      NDFILTER =  "ode.model.core.LogicalChannel_ndFilter"
      OTF =  "ode.model.core.LogicalChannel_otf"
      DETECTORSETTINGS =  "ode.model.core.LogicalChannel_detectorSettings"
      LIGHTSOURCESETTINGS =  "ode.model.core.LogicalChannel_lightSourceSettings"
      FILTERSET =  "ode.model.core.LogicalChannel_filterSet"
      SAMPLESPERPIXEL =  "ode.model.core.LogicalChannel_samplesPerPixel"
      PHOTOMETRICINTERPRETATION =  "ode.model.core.LogicalChannel_photometricInterpretation"
      MODE =  "ode.model.core.LogicalChannel_mode"
      POCKELCELLSETTING =  "ode.model.core.LogicalChannel_pockelCellSetting"
      CHANNELS =  "ode.model.core.LogicalChannel_channels"
      LIGHTPATH =  "ode.model.core.LogicalChannel_lightPath"
      DETAILS =  "ode.model.core.LogicalChannel_details"
      def errorIfUnloaded(self):
          if not self._loaded:
              raise _ode.UnloadedEntityException("Object unloaded:"+str(self))

      def throwNullCollectionException(self,propertyName):
          raise _ode.UnloadedEntityException(""+
          "Error updating collection:" + propertyName +"\n"+
          "Collection is currently null. This can be seen\n" +
          "by testing \""+ propertyName +"Loaded\". This implies\n"+
          "that this collection was unloaded. Please refresh this object\n"+
          "in order to update this collection.\n")

      def _toggleCollectionsLoaded(self, load):
          if load:
              self._channelsSeq = []
              self._channelsLoaded = True;
          else:
              self._channelsSeq = []
              self._channelsLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(LogicalChannelI, self).__init__()
          if id is not None and isinstance(id, (str, unicode)) and ":" in id:
              parts = id.split(":")
              if len(parts) != 2:
                  raise Exception("Invalid proxy string: %s", id)
              if parts[0] != self.__class__.__name__ and \
                 parts[0]+"I" != self.__class__.__name__:
                  raise Exception("Proxy class mismatch: %s<>%s" %
                  (self.__class__.__name__, parts[0]))
              self._id = rlong(parts[1])
              if loaded is None:
                  # If no loadedness was requested with
                  # a proxy string, then assume False.
                  loaded = False
          else:
              # Relying on ode.rtypes.rlong's error-handling
              self._id = rlong(id)
              if loaded is None:
                  loaded = True  # Assume true as previously
          self._loaded = loaded
          if self._loaded:
             self._details = _ode_model.DetailsI()
             self._toggleCollectionsLoaded(True)

      def unload(self, current = None):
          self._loaded = False
          self.unloadName( )
          self.unloadPinHoleSize( )
          self.unloadIllumination( )
          self.unloadContrastMethod( )
          self.unloadExcitationWave( )
          self.unloadEmissionWave( )
          self.unloadFluor( )
          self.unloadNdFilter( )
          self.unloadOtf( )
          self.unloadDetectorSettings( )
          self.unloadLightSourceSettings( )
          self.unloadFilterSet( )
          self.unloadSamplesPerPixel( )
          self.unloadPhotometricInterpretation( )
          self.unloadMode( )
          self.unloadPockelCellSetting( )
          self.unloadChannels( )
          self.unloadLightPath( )
          self.unloadDetails( )

      def isLoaded(self, current = None):
          return self._loaded
      def unloadCollections(self, current = None):
          self._toggleCollectionsLoaded( False )
      def isGlobal(self, current = None):
          return  False ;
      def isMutable(self, current = None):
          return  True ;
      def isAnnotated(self, current = None):
          return  False ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = LogicalChannelI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return LogicalChannelI( self._id.getValue(), False )

      def getDetails(self, current = None):
          self.errorIfUnloaded()
          return self._details

      def unloadDetails(self, current = None):
          self._details = None

      def getId(self, current = None):
          return self._id

      def setId(self, _id, current = None):
          self._id = _id

      def checkUnloadedProperty(self, value, loadedField):
          if value == None:
              self.__dict__[loadedField] = False
          else:
              self.__dict__[loadedField] = True

      def getVersion(self, current = None):
          self.errorIfUnloaded()
          return self._version

      def setVersion(self, version, current = None):
          self.errorIfUnloaded()
          self._version = version

      def unloadName(self, ):
          self._nameLoaded = False
          self._name = None;

      def getName(self, current = None):
          self.errorIfUnloaded()
          return self._name

      def setName(self, _name, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.name.wrapper is not None:
              if _name is not None:
                  _name = self._field_info.name.wrapper(_name)
          self._name = _name
          pass

      def unloadPinHoleSize(self, ):
          self._pinHoleSizeLoaded = False
          self._pinHoleSize = None;

      def getPinHoleSize(self, current = None):
          self.errorIfUnloaded()
          return self._pinHoleSize

      def setPinHoleSize(self, _pinHoleSize, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pinHoleSize.wrapper is not None:
              if _pinHoleSize is not None:
                  _pinHoleSize = self._field_info.pinHoleSize.wrapper(_pinHoleSize)
          self._pinHoleSize = _pinHoleSize
          pass

      def unloadIllumination(self, ):
          self._illuminationLoaded = False
          self._illumination = None;

      def getIllumination(self, current = None):
          self.errorIfUnloaded()
          return self._illumination

      def setIllumination(self, _illumination, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.illumination.wrapper is not None:
              if _illumination is not None:
                  _illumination = self._field_info.illumination.wrapper(_illumination)
          self._illumination = _illumination
          pass

      def unloadContrastMethod(self, ):
          self._contrastMethodLoaded = False
          self._contrastMethod = None;

      def getContrastMethod(self, current = None):
          self.errorIfUnloaded()
          return self._contrastMethod

      def setContrastMethod(self, _contrastMethod, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.contrastMethod.wrapper is not None:
              if _contrastMethod is not None:
                  _contrastMethod = self._field_info.contrastMethod.wrapper(_contrastMethod)
          self._contrastMethod = _contrastMethod
          pass

      def unloadExcitationWave(self, ):
          self._excitationWaveLoaded = False
          self._excitationWave = None;

      def getExcitationWave(self, current = None):
          self.errorIfUnloaded()
          return self._excitationWave

      def setExcitationWave(self, _excitationWave, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.excitationWave.wrapper is not None:
              if _excitationWave is not None:
                  _excitationWave = self._field_info.excitationWave.wrapper(_excitationWave)
          self._excitationWave = _excitationWave
          pass

      def unloadEmissionWave(self, ):
          self._emissionWaveLoaded = False
          self._emissionWave = None;

      def getEmissionWave(self, current = None):
          self.errorIfUnloaded()
          return self._emissionWave

      def setEmissionWave(self, _emissionWave, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.emissionWave.wrapper is not None:
              if _emissionWave is not None:
                  _emissionWave = self._field_info.emissionWave.wrapper(_emissionWave)
          self._emissionWave = _emissionWave
          pass

      def unloadFluor(self, ):
          self._fluorLoaded = False
          self._fluor = None;

      def getFluor(self, current = None):
          self.errorIfUnloaded()
          return self._fluor

      def setFluor(self, _fluor, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.fluor.wrapper is not None:
              if _fluor is not None:
                  _fluor = self._field_info.fluor.wrapper(_fluor)
          self._fluor = _fluor
          pass

      def unloadNdFilter(self, ):
          self._ndFilterLoaded = False
          self._ndFilter = None;

      def getNdFilter(self, current = None):
          self.errorIfUnloaded()
          return self._ndFilter

      def setNdFilter(self, _ndFilter, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.ndFilter.wrapper is not None:
              if _ndFilter is not None:
                  _ndFilter = self._field_info.ndFilter.wrapper(_ndFilter)
          self._ndFilter = _ndFilter
          pass

      def unloadOtf(self, ):
          self._otfLoaded = False
          self._otf = None;

      def getOtf(self, current = None):
          self.errorIfUnloaded()
          return self._otf

      def setOtf(self, _otf, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.otf.wrapper is not None:
              if _otf is not None:
                  _otf = self._field_info.otf.wrapper(_otf)
          self._otf = _otf
          pass

      def unloadDetectorSettings(self, ):
          self._detectorSettingsLoaded = False
          self._detectorSettings = None;

      def getDetectorSettings(self, current = None):
          self.errorIfUnloaded()
          return self._detectorSettings

      def setDetectorSettings(self, _detectorSettings, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.detectorSettings.wrapper is not None:
              if _detectorSettings is not None:
                  _detectorSettings = self._field_info.detectorSettings.wrapper(_detectorSettings)
          self._detectorSettings = _detectorSettings
          pass

      def unloadLightSourceSettings(self, ):
          self._lightSourceSettingsLoaded = False
          self._lightSourceSettings = None;

      def getLightSourceSettings(self, current = None):
          self.errorIfUnloaded()
          return self._lightSourceSettings

      def setLightSourceSettings(self, _lightSourceSettings, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lightSourceSettings.wrapper is not None:
              if _lightSourceSettings is not None:
                  _lightSourceSettings = self._field_info.lightSourceSettings.wrapper(_lightSourceSettings)
          self._lightSourceSettings = _lightSourceSettings
          pass

      def unloadFilterSet(self, ):
          self._filterSetLoaded = False
          self._filterSet = None;

      def getFilterSet(self, current = None):
          self.errorIfUnloaded()
          return self._filterSet

      def setFilterSet(self, _filterSet, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.filterSet.wrapper is not None:
              if _filterSet is not None:
                  _filterSet = self._field_info.filterSet.wrapper(_filterSet)
          self._filterSet = _filterSet
          pass

      def unloadSamplesPerPixel(self, ):
          self._samplesPerPixelLoaded = False
          self._samplesPerPixel = None;

      def getSamplesPerPixel(self, current = None):
          self.errorIfUnloaded()
          return self._samplesPerPixel

      def setSamplesPerPixel(self, _samplesPerPixel, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.samplesPerPixel.wrapper is not None:
              if _samplesPerPixel is not None:
                  _samplesPerPixel = self._field_info.samplesPerPixel.wrapper(_samplesPerPixel)
          self._samplesPerPixel = _samplesPerPixel
          pass

      def unloadPhotometricInterpretation(self, ):
          self._photometricInterpretationLoaded = False
          self._photometricInterpretation = None;

      def getPhotometricInterpretation(self, current = None):
          self.errorIfUnloaded()
          return self._photometricInterpretation

      def setPhotometricInterpretation(self, _photometricInterpretation, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.photometricInterpretation.wrapper is not None:
              if _photometricInterpretation is not None:
                  _photometricInterpretation = self._field_info.photometricInterpretation.wrapper(_photometricInterpretation)
          self._photometricInterpretation = _photometricInterpretation
          pass

      def unloadMode(self, ):
          self._modeLoaded = False
          self._mode = None;

      def getMode(self, current = None):
          self.errorIfUnloaded()
          return self._mode

      def setMode(self, _mode, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.mode.wrapper is not None:
              if _mode is not None:
                  _mode = self._field_info.mode.wrapper(_mode)
          self._mode = _mode
          pass

      def unloadPockelCellSetting(self, ):
          self._pockelCellSettingLoaded = False
          self._pockelCellSetting = None;

      def getPockelCellSetting(self, current = None):
          self.errorIfUnloaded()
          return self._pockelCellSetting

      def setPockelCellSetting(self, _pockelCellSetting, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pockelCellSetting.wrapper is not None:
              if _pockelCellSetting is not None:
                  _pockelCellSetting = self._field_info.pockelCellSetting.wrapper(_pockelCellSetting)
          self._pockelCellSetting = _pockelCellSetting
          pass

      def unloadChannels(self, current = None):
          self._channelsLoaded = False
          self._channelsSeq = None;

      def _getChannels(self, current = None):
          self.errorIfUnloaded()
          return self._channelsSeq

      def _setChannels(self, _channels, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.channelsSeq.wrapper is not None:
              if _channels is not None:
                  _channels = self._field_info.channelsSeq.wrapper(_channels)
          self._channelsSeq = _channels
          self.checkUnloadedProperty(_channels,'channelsLoaded')

      def isChannelsLoaded(self):
          return self._channelsLoaded

      def sizeOfChannels(self, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: return -1
          return len(self._channelsSeq)

      def copyChannels(self, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          return list(self._channelsSeq)

      def iterateChannels(self):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          return iter(self._channelsSeq)

      def addChannel(self, target, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          self._channelsSeq.append( target );
          target.setLogicalChannel( self )

      def addAllChannelSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          self._channelsSeq.extend( targets )
          for target in targets:
              target.setLogicalChannel( self )

      def removeChannel(self, target, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          self._channelsSeq.remove( target )
          target.setLogicalChannel( None )

      def removeAllChannelSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          for elt in targets:
              elt.setLogicalChannel( None )
              self._channelsSeq.remove( elt )

      def clearChannels(self, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          for elt in self._channelsSeq:
              elt.setLogicalChannel( None )
          self._channelsSeq = list()

      def reloadChannels(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._channelsLoaded:
              raise ode.ClientError("Cannot reload active collection: channelsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyChannels() # May also throw
          for elt in copy:
              elt.setLogicalChannel( self )
          self._channelsSeq = copy
          toCopy.unloadChannels()
          self._channelsLoaded = True

      def unloadLightPath(self, ):
          self._lightPathLoaded = False
          self._lightPath = None;

      def getLightPath(self, current = None):
          self.errorIfUnloaded()
          return self._lightPath

      def setLightPath(self, _lightPath, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lightPath.wrapper is not None:
              if _lightPath is not None:
                  _lightPath = self._field_info.lightPath.wrapper(_lightPath)
          self._lightPath = _lightPath
          pass


      def ice_postUnmarshal(self):
          """
          Provides additional initialization once all data loaded
          """
          pass # Currently unused


      def ice_preMarshal(self):
          """
          Provides additional validation before data is sent
          """
          pass # Currently unused

      def __getattr__(self, name):
          import __builtin__
          """
          Reroutes all access to object.field through object.getField() or object.isField()
          """
          if "_" in name:  # Ice disallows underscores, so these should be treated normally.
              return object.__getattribute__(self, name)
          field  = "_" + name
          capitalized = name[0].capitalize() + name[1:]
          getter = "get" + capitalized
          questn = "is" + capitalized
          try:
              self.__dict__[field]
              if hasattr(self, getter):
                  method = getattr(self, getter)
                  return method()
              elif hasattr(self, questn):
                  method = getattr(self, questn)
                  return method()
          except:
              pass
          raise AttributeError("'%s' object has no attribute '%s' or '%s'" % (self.__class__.__name__, getter, questn))

      def __setattr__(self, name, value):
          """
          Reroutes all access to object.field through object.getField(), with the caveat
          that all sets on variables starting with "_" are permitted directly.
          """
          if name.startswith("_"):
              self.__dict__[name] = value
              return
          else:
              field  = "_" + name
              setter = "set" + name[0].capitalize() + name[1:]
              if hasattr(self, field) and hasattr(self, setter):
                  method = getattr(self, setter)
                  return method(value)
          raise AttributeError("'%s' object has no attribute '%s'" % (self.__class__.__name__, setter))

_ode_model.LogicalChannelI = LogicalChannelI
