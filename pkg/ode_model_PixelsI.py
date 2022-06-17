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
IceImport.load("ode_model_Pixels_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class PixelsI(_ode_model.Pixels):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "image",
          "relatedTo",
          "pixelsType",
          "significantBits",
          "sizeX",
          "sizeY",
          "sizeZ",
          "sizeC",
          "sizeT",
          "sha1",
          "dimensionOrder",
          "physicalSizeX",
          "physicalSizeY",
          "physicalSizeZ",
          "waveStart",
          "waveIncrement",
          "timeIncrement",
          "methodology",
          "planeInfo",
          "pixelsFileMaps",
          "channels",
          "settings",
          "thumbnails",
          "details",
      ])
      _field_info = _field_info_type(
          image=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          relatedTo=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          pixelsType=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          significantBits=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          sizeX=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sizeY=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sizeZ=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sizeC=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sizeT=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sha1=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          dimensionOrder=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          physicalSizeX=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          physicalSizeY=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          physicalSizeZ=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          waveStart=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          waveIncrement=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          timeIncrement=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          methodology=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          planeInfo=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          pixelsFileMaps=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          channels=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          settings=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          thumbnails=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      IMAGE =  "ode.model.core.Pixels_image"
      RELATEDTO =  "ode.model.core.Pixels_relatedTo"
      PIXELSTYPE =  "ode.model.core.Pixels_pixelsType"
      SIGNIFICANTBITS =  "ode.model.core.Pixels_significantBits"
      SIZEX =  "ode.model.core.Pixels_sizeX"
      SIZEY =  "ode.model.core.Pixels_sizeY"
      SIZEZ =  "ode.model.core.Pixels_sizeZ"
      SIZEC =  "ode.model.core.Pixels_sizeC"
      SIZET =  "ode.model.core.Pixels_sizeT"
      SHA1 =  "ode.model.core.Pixels_sha1"
      DIMENSIONORDER =  "ode.model.core.Pixels_dimensionOrder"
      PHYSICALSIZEX =  "ode.model.core.Pixels_physicalSizeX"
      PHYSICALSIZEY =  "ode.model.core.Pixels_physicalSizeY"
      PHYSICALSIZEZ =  "ode.model.core.Pixels_physicalSizeZ"
      WAVESTART =  "ode.model.core.Pixels_waveStart"
      WAVEINCREMENT =  "ode.model.core.Pixels_waveIncrement"
      TIMEINCREMENT =  "ode.model.core.Pixels_timeIncrement"
      METHODOLOGY =  "ode.model.core.Pixels_methodology"
      PLANEINFO =  "ode.model.core.Pixels_planeInfo"
      PIXELSFILEMAPS =  "ode.model.core.Pixels_pixelsFileMaps"
      CHANNELS =  "ode.model.core.Pixels_channels"
      SETTINGS =  "ode.model.core.Pixels_settings"
      THUMBNAILS =  "ode.model.core.Pixels_thumbnails"
      DETAILS =  "ode.model.core.Pixels_details"
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
              self._planeInfoSeq = []
              self._planeInfoLoaded = True;
          else:
              self._planeInfoSeq = []
              self._planeInfoLoaded = False;

          if load:
              self._pixelsFileMapsSeq = []
              self._pixelsFileMapsLoaded = True;
          else:
              self._pixelsFileMapsSeq = []
              self._pixelsFileMapsLoaded = False;

          if load:
              self._channelsSeq = []
              self._channelsLoaded = True;
          else:
              self._channelsSeq = []
              self._channelsLoaded = False;

          if load:
              self._settingsSeq = []
              self._settingsLoaded = True;
          else:
              self._settingsSeq = []
              self._settingsLoaded = False;

          if load:
              self._thumbnailsSeq = []
              self._thumbnailsLoaded = True;
          else:
              self._thumbnailsSeq = []
              self._thumbnailsLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(PixelsI, self).__init__()
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
          self.unloadImage( )
          self.unloadRelatedTo( )
          self.unloadPixelsType( )
          self.unloadSignificantBits( )
          self.unloadSizeX( )
          self.unloadSizeY( )
          self.unloadSizeZ( )
          self.unloadSizeC( )
          self.unloadSizeT( )
          self.unloadSha1( )
          self.unloadDimensionOrder( )
          self.unloadPhysicalSizeX( )
          self.unloadPhysicalSizeY( )
          self.unloadPhysicalSizeZ( )
          self.unloadWaveStart( )
          self.unloadWaveIncrement( )
          self.unloadTimeIncrement( )
          self.unloadMethodology( )
          self.unloadPlaneInfo( )
          self.unloadPixelsFileMaps( )
          self.unloadChannels( )
          self.unloadSettings( )
          self.unloadThumbnails( )
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
            copy = PixelsI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return PixelsI( self._id.getValue(), False )

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

      def unloadImage(self, ):
          self._imageLoaded = False
          self._image = None;

      def getImage(self, current = None):
          self.errorIfUnloaded()
          return self._image

      def setImage(self, _image, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.image.wrapper is not None:
              if _image is not None:
                  _image = self._field_info.image.wrapper(_image)
          self._image = _image
          pass

      def unloadRelatedTo(self, ):
          self._relatedToLoaded = False
          self._relatedTo = None;

      def getRelatedTo(self, current = None):
          self.errorIfUnloaded()
          return self._relatedTo

      def setRelatedTo(self, _relatedTo, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.relatedTo.wrapper is not None:
              if _relatedTo is not None:
                  _relatedTo = self._field_info.relatedTo.wrapper(_relatedTo)
          self._relatedTo = _relatedTo
          pass

      def unloadPixelsType(self, ):
          self._pixelsTypeLoaded = False
          self._pixelsType = None;

      def getPixelsType(self, current = None):
          self.errorIfUnloaded()
          return self._pixelsType

      def setPixelsType(self, _pixelsType, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pixelsType.wrapper is not None:
              if _pixelsType is not None:
                  _pixelsType = self._field_info.pixelsType.wrapper(_pixelsType)
          self._pixelsType = _pixelsType
          pass

      def unloadSignificantBits(self, ):
          self._significantBitsLoaded = False
          self._significantBits = None;

      def getSignificantBits(self, current = None):
          self.errorIfUnloaded()
          return self._significantBits

      def setSignificantBits(self, _significantBits, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.significantBits.wrapper is not None:
              if _significantBits is not None:
                  _significantBits = self._field_info.significantBits.wrapper(_significantBits)
          self._significantBits = _significantBits
          pass

      def unloadSizeX(self, ):
          self._sizeXLoaded = False
          self._sizeX = None;

      def getSizeX(self, current = None):
          self.errorIfUnloaded()
          return self._sizeX

      def setSizeX(self, _sizeX, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeX.wrapper is not None:
              if _sizeX is not None:
                  _sizeX = self._field_info.sizeX.wrapper(_sizeX)
          self._sizeX = _sizeX
          pass

      def unloadSizeY(self, ):
          self._sizeYLoaded = False
          self._sizeY = None;

      def getSizeY(self, current = None):
          self.errorIfUnloaded()
          return self._sizeY

      def setSizeY(self, _sizeY, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeY.wrapper is not None:
              if _sizeY is not None:
                  _sizeY = self._field_info.sizeY.wrapper(_sizeY)
          self._sizeY = _sizeY
          pass

      def unloadSizeZ(self, ):
          self._sizeZLoaded = False
          self._sizeZ = None;

      def getSizeZ(self, current = None):
          self.errorIfUnloaded()
          return self._sizeZ

      def setSizeZ(self, _sizeZ, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeZ.wrapper is not None:
              if _sizeZ is not None:
                  _sizeZ = self._field_info.sizeZ.wrapper(_sizeZ)
          self._sizeZ = _sizeZ
          pass

      def unloadSizeC(self, ):
          self._sizeCLoaded = False
          self._sizeC = None;

      def getSizeC(self, current = None):
          self.errorIfUnloaded()
          return self._sizeC

      def setSizeC(self, _sizeC, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeC.wrapper is not None:
              if _sizeC is not None:
                  _sizeC = self._field_info.sizeC.wrapper(_sizeC)
          self._sizeC = _sizeC
          pass

      def unloadSizeT(self, ):
          self._sizeTLoaded = False
          self._sizeT = None;

      def getSizeT(self, current = None):
          self.errorIfUnloaded()
          return self._sizeT

      def setSizeT(self, _sizeT, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeT.wrapper is not None:
              if _sizeT is not None:
                  _sizeT = self._field_info.sizeT.wrapper(_sizeT)
          self._sizeT = _sizeT
          pass

      def unloadSha1(self, ):
          self._sha1Loaded = False
          self._sha1 = None;

      def getSha1(self, current = None):
          self.errorIfUnloaded()
          return self._sha1

      def setSha1(self, _sha1, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sha1.wrapper is not None:
              if _sha1 is not None:
                  _sha1 = self._field_info.sha1.wrapper(_sha1)
          self._sha1 = _sha1
          pass

      def unloadDimensionOrder(self, ):
          self._dimensionOrderLoaded = False
          self._dimensionOrder = None;

      def getDimensionOrder(self, current = None):
          self.errorIfUnloaded()
          return self._dimensionOrder

      def setDimensionOrder(self, _dimensionOrder, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.dimensionOrder.wrapper is not None:
              if _dimensionOrder is not None:
                  _dimensionOrder = self._field_info.dimensionOrder.wrapper(_dimensionOrder)
          self._dimensionOrder = _dimensionOrder
          pass

      def unloadPhysicalSizeX(self, ):
          self._physicalSizeXLoaded = False
          self._physicalSizeX = None;

      def getPhysicalSizeX(self, current = None):
          self.errorIfUnloaded()
          return self._physicalSizeX

      def setPhysicalSizeX(self, _physicalSizeX, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.physicalSizeX.wrapper is not None:
              if _physicalSizeX is not None:
                  _physicalSizeX = self._field_info.physicalSizeX.wrapper(_physicalSizeX)
          self._physicalSizeX = _physicalSizeX
          pass

      def unloadPhysicalSizeY(self, ):
          self._physicalSizeYLoaded = False
          self._physicalSizeY = None;

      def getPhysicalSizeY(self, current = None):
          self.errorIfUnloaded()
          return self._physicalSizeY

      def setPhysicalSizeY(self, _physicalSizeY, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.physicalSizeY.wrapper is not None:
              if _physicalSizeY is not None:
                  _physicalSizeY = self._field_info.physicalSizeY.wrapper(_physicalSizeY)
          self._physicalSizeY = _physicalSizeY
          pass

      def unloadPhysicalSizeZ(self, ):
          self._physicalSizeZLoaded = False
          self._physicalSizeZ = None;

      def getPhysicalSizeZ(self, current = None):
          self.errorIfUnloaded()
          return self._physicalSizeZ

      def setPhysicalSizeZ(self, _physicalSizeZ, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.physicalSizeZ.wrapper is not None:
              if _physicalSizeZ is not None:
                  _physicalSizeZ = self._field_info.physicalSizeZ.wrapper(_physicalSizeZ)
          self._physicalSizeZ = _physicalSizeZ
          pass

      def unloadWaveStart(self, ):
          self._waveStartLoaded = False
          self._waveStart = None;

      def getWaveStart(self, current = None):
          self.errorIfUnloaded()
          return self._waveStart

      def setWaveStart(self, _waveStart, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.waveStart.wrapper is not None:
              if _waveStart is not None:
                  _waveStart = self._field_info.waveStart.wrapper(_waveStart)
          self._waveStart = _waveStart
          pass

      def unloadWaveIncrement(self, ):
          self._waveIncrementLoaded = False
          self._waveIncrement = None;

      def getWaveIncrement(self, current = None):
          self.errorIfUnloaded()
          return self._waveIncrement

      def setWaveIncrement(self, _waveIncrement, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.waveIncrement.wrapper is not None:
              if _waveIncrement is not None:
                  _waveIncrement = self._field_info.waveIncrement.wrapper(_waveIncrement)
          self._waveIncrement = _waveIncrement
          pass

      def unloadTimeIncrement(self, ):
          self._timeIncrementLoaded = False
          self._timeIncrement = None;

      def getTimeIncrement(self, current = None):
          self.errorIfUnloaded()
          return self._timeIncrement

      def setTimeIncrement(self, _timeIncrement, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.timeIncrement.wrapper is not None:
              if _timeIncrement is not None:
                  _timeIncrement = self._field_info.timeIncrement.wrapper(_timeIncrement)
          self._timeIncrement = _timeIncrement
          pass

      def unloadMethodology(self, ):
          self._methodologyLoaded = False
          self._methodology = None;

      def getMethodology(self, current = None):
          self.errorIfUnloaded()
          return self._methodology

      def setMethodology(self, _methodology, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.methodology.wrapper is not None:
              if _methodology is not None:
                  _methodology = self._field_info.methodology.wrapper(_methodology)
          self._methodology = _methodology
          pass

      def unloadPlaneInfo(self, current = None):
          self._planeInfoLoaded = False
          self._planeInfoSeq = None;

      def _getPlaneInfo(self, current = None):
          self.errorIfUnloaded()
          return self._planeInfoSeq

      def _setPlaneInfo(self, _planeInfo, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.planeInfoSeq.wrapper is not None:
              if _planeInfo is not None:
                  _planeInfo = self._field_info.planeInfoSeq.wrapper(_planeInfo)
          self._planeInfoSeq = _planeInfo
          self.checkUnloadedProperty(_planeInfo,'planeInfoLoaded')

      def isPlaneInfoLoaded(self):
          return self._planeInfoLoaded

      def sizeOfPlaneInfo(self, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: return -1
          return len(self._planeInfoSeq)

      def copyPlaneInfo(self, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          return list(self._planeInfoSeq)

      def iteratePlaneInfo(self):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          return iter(self._planeInfoSeq)

      def addPlaneInfo(self, target, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          self._planeInfoSeq.append( target );
          target.setPixels( self )

      def addAllPlaneInfoSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          self._planeInfoSeq.extend( targets )
          for target in targets:
              target.setPixels( self )

      def removePlaneInfo(self, target, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          self._planeInfoSeq.remove( target )
          target.setPixels( None )

      def removeAllPlaneInfoSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          for elt in targets:
              elt.setPixels( None )
              self._planeInfoSeq.remove( elt )

      def clearPlaneInfo(self, current = None):
          self.errorIfUnloaded()
          if not self._planeInfoLoaded: self.throwNullCollectionException("planeInfoSeq")
          for elt in self._planeInfoSeq:
              elt.setPixels( None )
          self._planeInfoSeq = list()

      def reloadPlaneInfo(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._planeInfoLoaded:
              raise ode.ClientError("Cannot reload active collection: planeInfoSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyPlaneInfo() # May also throw
          for elt in copy:
              elt.setPixels( self )
          self._planeInfoSeq = copy
          toCopy.unloadPlaneInfo()
          self._planeInfoLoaded = True

      def unloadPixelsFileMaps(self, current = None):
          self._pixelsFileMapsLoaded = False
          self._pixelsFileMapsSeq = None;

      def _getPixelsFileMaps(self, current = None):
          self.errorIfUnloaded()
          return self._pixelsFileMapsSeq

      def _setPixelsFileMaps(self, _pixelsFileMaps, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pixelsFileMapsSeq.wrapper is not None:
              if _pixelsFileMaps is not None:
                  _pixelsFileMaps = self._field_info.pixelsFileMapsSeq.wrapper(_pixelsFileMaps)
          self._pixelsFileMapsSeq = _pixelsFileMaps
          self.checkUnloadedProperty(_pixelsFileMaps,'pixelsFileMapsLoaded')

      def isPixelsFileMapsLoaded(self):
          return self._pixelsFileMapsLoaded

      def sizeOfPixelsFileMaps(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: return -1
          return len(self._pixelsFileMapsSeq)

      def copyPixelsFileMaps(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          return list(self._pixelsFileMapsSeq)

      def iteratePixelsFileMaps(self):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          return iter(self._pixelsFileMapsSeq)

      def addPixelsOriginalFileMap(self, target, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          self._pixelsFileMapsSeq.append( target );
          target.setChild( self )

      def addAllPixelsOriginalFileMapSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          self._pixelsFileMapsSeq.extend( targets )
          for target in targets:
              target.setChild( self )

      def removePixelsOriginalFileMap(self, target, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          self._pixelsFileMapsSeq.remove( target )
          target.setChild( None )

      def removeAllPixelsOriginalFileMapSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          for elt in targets:
              elt.setChild( None )
              self._pixelsFileMapsSeq.remove( elt )

      def clearPixelsFileMaps(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          for elt in self._pixelsFileMapsSeq:
              elt.setChild( None )
          self._pixelsFileMapsSeq = list()

      def reloadPixelsFileMaps(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._pixelsFileMapsLoaded:
              raise ode.ClientError("Cannot reload active collection: pixelsFileMapsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyPixelsFileMaps() # May also throw
          for elt in copy:
              elt.setChild( self )
          self._pixelsFileMapsSeq = copy
          toCopy.unloadPixelsFileMaps()
          self._pixelsFileMapsLoaded = True

      def getPixelsFileMapsCountPerOwner(self, current = None):
          return self._pixelsFileMapsCountPerOwner

      def linkOriginalFile(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          link = _ode_model.PixelsOriginalFileMapI()
          link.link( addition, self );
          self.addPixelsOriginalFileMapToBoth( link, True )
          return link

      def addPixelsOriginalFileMapToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          self._pixelsFileMapsSeq.append( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().addPixelsOriginalFileMapToBoth( link, False )

      def findPixelsOriginalFileMap(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          result = list()
          for link in self._pixelsFileMapsSeq:
              if link.getParent() == removal: result.append(link)
          return result

      def unlinkOriginalFile(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          toRemove = self.findPixelsOriginalFileMap(removal)
          for next in toRemove:
              self.removePixelsOriginalFileMapFromBoth( next, True )

      def removePixelsOriginalFileMapFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._pixelsFileMapsLoaded: self.throwNullCollectionException("pixelsFileMapsSeq")
          self._pixelsFileMapsSeq.remove( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().removePixelsOriginalFileMapFromBoth(link, False)

      def linkedOriginalFileList(self, current = None):
          self.errorIfUnloaded()
          if not self.pixelsFileMapsLoaded: self.throwNullCollectionException("PixelsFileMaps")
          linked = []
          for link in self._pixelsFileMapsSeq:
              linked.append( link.getParent() )
          return linked

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
          target.setPixels( self )

      def addAllChannelSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          self._channelsSeq.extend( targets )
          for target in targets:
              target.setPixels( self )

      def removeChannel(self, target, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          self._channelsSeq.remove( target )
          target.setPixels( None )

      def removeAllChannelSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          for elt in targets:
              elt.setPixels( None )
              self._channelsSeq.remove( elt )

      def clearChannels(self, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          for elt in self._channelsSeq:
              elt.setPixels( None )
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
              elt.setPixels( self )
          self._channelsSeq = copy
          toCopy.unloadChannels()
          self._channelsLoaded = True

      def getChannel(self, index, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          return self._channelsSeq[index]

      def setChannel(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          old = self._channelsSeq[index]
          if wrap and self._field_info.channelsSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.channelsSeq.wrapper(_channels)
          self._channelsSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setPixels( self )
          return old

      def getPrimaryChannel(self, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          return self._channelsSeq[0]

      def setPrimaryChannel(self, element, current = None):
          self.errorIfUnloaded()
          if not self._channelsLoaded: self.throwNullCollectionException("channelsSeq")
          index = self._channelsSeq.index(element)
          old = self._channelsSeq[0]
          self._channelsSeq[index] = old
          self._channelsSeq[0] = element
          return old

      def unloadSettings(self, current = None):
          self._settingsLoaded = False
          self._settingsSeq = None;

      def _getSettings(self, current = None):
          self.errorIfUnloaded()
          return self._settingsSeq

      def _setSettings(self, _settings, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.settingsSeq.wrapper is not None:
              if _settings is not None:
                  _settings = self._field_info.settingsSeq.wrapper(_settings)
          self._settingsSeq = _settings
          self.checkUnloadedProperty(_settings,'settingsLoaded')

      def isSettingsLoaded(self):
          return self._settingsLoaded

      def sizeOfSettings(self, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: return -1
          return len(self._settingsSeq)

      def copySettings(self, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          return list(self._settingsSeq)

      def iterateSettings(self):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          return iter(self._settingsSeq)

      def addRenderingDef(self, target, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          self._settingsSeq.append( target );
          target.setPixels( self )

      def addAllRenderingDefSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          self._settingsSeq.extend( targets )
          for target in targets:
              target.setPixels( self )

      def removeRenderingDef(self, target, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          self._settingsSeq.remove( target )
          target.setPixels( None )

      def removeAllRenderingDefSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          for elt in targets:
              elt.setPixels( None )
              self._settingsSeq.remove( elt )

      def clearSettings(self, current = None):
          self.errorIfUnloaded()
          if not self._settingsLoaded: self.throwNullCollectionException("settingsSeq")
          for elt in self._settingsSeq:
              elt.setPixels( None )
          self._settingsSeq = list()

      def reloadSettings(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._settingsLoaded:
              raise ode.ClientError("Cannot reload active collection: settingsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copySettings() # May also throw
          for elt in copy:
              elt.setPixels( self )
          self._settingsSeq = copy
          toCopy.unloadSettings()
          self._settingsLoaded = True

      def unloadThumbnails(self, current = None):
          self._thumbnailsLoaded = False
          self._thumbnailsSeq = None;

      def _getThumbnails(self, current = None):
          self.errorIfUnloaded()
          return self._thumbnailsSeq

      def _setThumbnails(self, _thumbnails, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.thumbnailsSeq.wrapper is not None:
              if _thumbnails is not None:
                  _thumbnails = self._field_info.thumbnailsSeq.wrapper(_thumbnails)
          self._thumbnailsSeq = _thumbnails
          self.checkUnloadedProperty(_thumbnails,'thumbnailsLoaded')

      def isThumbnailsLoaded(self):
          return self._thumbnailsLoaded

      def sizeOfThumbnails(self, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: return -1
          return len(self._thumbnailsSeq)

      def copyThumbnails(self, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          return list(self._thumbnailsSeq)

      def iterateThumbnails(self):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          return iter(self._thumbnailsSeq)

      def addThumbnail(self, target, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          self._thumbnailsSeq.append( target );
          target.setPixels( self )

      def addAllThumbnailSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          self._thumbnailsSeq.extend( targets )
          for target in targets:
              target.setPixels( self )

      def removeThumbnail(self, target, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          self._thumbnailsSeq.remove( target )
          target.setPixels( None )

      def removeAllThumbnailSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          for elt in targets:
              elt.setPixels( None )
              self._thumbnailsSeq.remove( elt )

      def clearThumbnails(self, current = None):
          self.errorIfUnloaded()
          if not self._thumbnailsLoaded: self.throwNullCollectionException("thumbnailsSeq")
          for elt in self._thumbnailsSeq:
              elt.setPixels( None )
          self._thumbnailsSeq = list()

      def reloadThumbnails(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._thumbnailsLoaded:
              raise ode.ClientError("Cannot reload active collection: thumbnailsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyThumbnails() # May also throw
          for elt in copy:
              elt.setPixels( self )
          self._thumbnailsSeq = copy
          toCopy.unloadThumbnails()
          self._thumbnailsLoaded = True


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

_ode_model.PixelsI = PixelsI
