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

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ModelF_ice
import ode_SystemF_ice
import ode_RTypes_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
_M_ode.api = Ice.openModule('ode.api')
__name__ = 'ode.api'

if '_t_SearchMetadata' not in _M_ode.api.__dict__:
    _M_ode.api._t_SearchMetadata = IcePy.defineDictionary('::ode::api::SearchMetadata', (), IcePy._t_string, _M_ode.model._t_Annotation)

if '_t_StringSet' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringSet = IcePy.defineSequence('::ode::api::StringSet', (), IcePy._t_string)

if '_t_LongList' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongList = IcePy.defineSequence('::ode::api::LongList', (), IcePy._t_long)

if '_t_IntegerList' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerList = IcePy.defineSequence('::ode::api::IntegerList', (), IcePy._t_int)

if '_t_SearchMetadataList' not in _M_ode.api.__dict__:
    _M_ode.api._t_SearchMetadataList = IcePy.defineSequence('::ode::api::SearchMetadataList', (), _M_ode.api._t_SearchMetadata)

if '_t_ExperimenterList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ExperimenterList = IcePy.defineSequence('::ode::api::ExperimenterList', (), _M_ode.model._t_Experimenter)

if '_t_ExperimenterGroupList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ExperimenterGroupList = IcePy.defineSequence('::ode::api::ExperimenterGroupList', (), _M_ode.model._t_ExperimenterGroup)

if '_t_EventList' not in _M_ode.api.__dict__:
    _M_ode.api._t_EventList = IcePy.defineSequence('::ode::api::EventList', (), _M_ode.model._t_Event)

if '_t_EventLogList' not in _M_ode.api.__dict__:
    _M_ode.api._t_EventLogList = IcePy.defineSequence('::ode::api::EventLogList', (), _M_ode.model._t_EventLog)

if '_t_AnnotationList' not in _M_ode.api.__dict__:
    _M_ode.api._t_AnnotationList = IcePy.defineSequence('::ode::api::AnnotationList', (), _M_ode.model._t_Annotation)

if '_t_SessionList' not in _M_ode.api.__dict__:
    _M_ode.api._t_SessionList = IcePy.defineSequence('::ode::api::SessionList', (), _M_ode.model._t_Session)

if '_t_IObjectList' not in _M_ode.api.__dict__:
    _M_ode.api._t_IObjectList = IcePy.defineSequence('::ode::api::IObjectList', (), _M_ode.model._t_IObject)

if '_t_ProjectList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ProjectList = IcePy.defineSequence('::ode::api::ProjectList', (), _M_ode.model._t_Project)

if '_t_DatasetList' not in _M_ode.api.__dict__:
    _M_ode.api._t_DatasetList = IcePy.defineSequence('::ode::api::DatasetList', (), _M_ode.model._t_Dataset)

if '_t_ImageList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ImageList = IcePy.defineSequence('::ode::api::ImageList', (), _M_ode.model._t_Image)

if '_t_LogicalChannelList' not in _M_ode.api.__dict__:
    _M_ode.api._t_LogicalChannelList = IcePy.defineSequence('::ode::api::LogicalChannelList', (), _M_ode.model._t_LogicalChannel)

if '_t_OriginalFileList' not in _M_ode.api.__dict__:
    _M_ode.api._t_OriginalFileList = IcePy.defineSequence('::ode::api::OriginalFileList', (), _M_ode.model._t_OriginalFile)

if '_t_PixelsList' not in _M_ode.api.__dict__:
    _M_ode.api._t_PixelsList = IcePy.defineSequence('::ode::api::PixelsList', (), _M_ode.model._t_Pixels)

if '_t_PixelsTypeList' not in _M_ode.api.__dict__:
    _M_ode.api._t_PixelsTypeList = IcePy.defineSequence('::ode::api::PixelsTypeList', (), _M_ode.model._t_PixelsType)

if '_t_RoiList' not in _M_ode.api.__dict__:
    _M_ode.api._t_RoiList = IcePy.defineSequence('::ode::api::RoiList', (), _M_ode.model._t_Roi)

if '_t_ScriptJobList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ScriptJobList = IcePy.defineSequence('::ode::api::ScriptJobList', (), _M_ode.model._t_ScriptJob)

if '_t_ShapeList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ShapeList = IcePy.defineSequence('::ode::api::ShapeList', (), _M_ode.model._t_Shape)

if '_t_ChecksumAlgorithmList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ChecksumAlgorithmList = IcePy.defineSequence('::ode::api::ChecksumAlgorithmList', (), _M_ode.model._t_ChecksumAlgorithm)

if '_t_NamedValueList' not in _M_ode.api.__dict__:
    _M_ode.api._t_NamedValueList = IcePy.defineSequence('::ode::api::NamedValueList', (), _M_ode.model._t_NamedValue)

if '_t_EventContextList' not in _M_ode.api.__dict__:
    _M_ode.api._t_EventContextList = IcePy.defineSequence('::ode::api::EventContextList', (), _M_ode.sys._t_EventContext)

if '_t_BoolArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_BoolArray = IcePy.defineSequence('::ode::api::BoolArray', (), IcePy._t_bool)

if '_t_ByteArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_ByteArray = IcePy.defineSequence('::ode::api::ByteArray', (), IcePy._t_byte)

if '_t_ShortArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_ShortArray = IcePy.defineSequence('::ode::api::ShortArray', (), IcePy._t_short)

if '_t_IntegerArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerArray = IcePy.defineSequence('::ode::api::IntegerArray', (), IcePy._t_int)

if '_t_LongArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongArray = IcePy.defineSequence('::ode::api::LongArray', (), IcePy._t_long)

if '_t_FloatArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_FloatArray = IcePy.defineSequence('::ode::api::FloatArray', (), IcePy._t_float)

if '_t_DoubleArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_DoubleArray = IcePy.defineSequence('::ode::api::DoubleArray', (), IcePy._t_double)

if '_t_StringArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringArray = IcePy.defineSequence('::ode::api::StringArray', (), IcePy._t_string)

if '_t_ByteArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_ByteArrayArray = IcePy.defineSequence('::ode::api::ByteArrayArray', (), _M_ode.api._t_ByteArray)

if '_t_ShortArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_ShortArrayArray = IcePy.defineSequence('::ode::api::ShortArrayArray', (), _M_ode.api._t_ShortArray)

if '_t_IntegerArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerArrayArray = IcePy.defineSequence('::ode::api::IntegerArrayArray', (), _M_ode.api._t_IntegerArray)

if '_t_IntegerArrayArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerArrayArrayArray = IcePy.defineSequence('::ode::api::IntegerArrayArrayArray', (), _M_ode.api._t_IntegerArrayArray)

if '_t_LongArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongArrayArray = IcePy.defineSequence('::ode::api::LongArrayArray', (), _M_ode.api._t_LongArray)

if '_t_FloatArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_FloatArrayArray = IcePy.defineSequence('::ode::api::FloatArrayArray', (), _M_ode.api._t_FloatArray)

if '_t_FloatArrayArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_FloatArrayArrayArray = IcePy.defineSequence('::ode::api::FloatArrayArrayArray', (), _M_ode.api._t_FloatArrayArray)

if '_t_DoubleArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_DoubleArrayArray = IcePy.defineSequence('::ode::api::DoubleArrayArray', (), _M_ode.api._t_DoubleArray)

if '_t_DoubleArrayArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_DoubleArrayArrayArray = IcePy.defineSequence('::ode::api::DoubleArrayArrayArray', (), _M_ode.api._t_DoubleArrayArray)

if '_t_StringArrayArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringArrayArray = IcePy.defineSequence('::ode::api::StringArrayArray', (), _M_ode.api._t_StringArray)

if '_t_RTypeDictArray' not in _M_ode.api.__dict__:
    _M_ode.api._t_RTypeDictArray = IcePy.defineSequence('::ode::api::RTypeDictArray', (), _M_ode._t_RTypeDict)

if '_t_LongStringMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongStringMap = IcePy.defineDictionary('::ode::api::LongStringMap', (), IcePy._t_long, IcePy._t_string)

if '_t_LongIntMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongIntMap = IcePy.defineDictionary('::ode::api::LongIntMap', (), IcePy._t_long, IcePy._t_int)

if '_t_LongByteArrayMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongByteArrayMap = IcePy.defineDictionary('::ode::api::LongByteArrayMap', (), IcePy._t_long, _M_ode.api._t_ByteArray)

if '_t_LongPixelsMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongPixelsMap = IcePy.defineDictionary('::ode::api::LongPixelsMap', (), IcePy._t_long, _M_ode.model._t_Pixels)

if '_t_IntStringMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntStringMap = IcePy.defineDictionary('::ode::api::IntStringMap', (), IcePy._t_int, IcePy._t_string)

if '_t_IntegerIntegerArrayMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerIntegerArrayMap = IcePy.defineDictionary('::ode::api::IntegerIntegerArrayMap', (), IcePy._t_int, _M_ode.api._t_IntegerArray)

if '_t_IntegerDoubleArrayMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntegerDoubleArrayMap = IcePy.defineDictionary('::ode::api::IntegerDoubleArrayMap', (), IcePy._t_int, _M_ode.api._t_DoubleArray)

if '_t_StringRTypeMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringRTypeMap = IcePy.defineDictionary('::ode::api::StringRTypeMap', (), IcePy._t_string, _M_ode._t_RType)

if '_t_UserMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_UserMap = IcePy.defineDictionary('::ode::api::UserMap', (), IcePy._t_string, _M_ode.model._t_Experimenter)

if '_t_OriginalFileMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_OriginalFileMap = IcePy.defineDictionary('::ode::api::OriginalFileMap', (), IcePy._t_string, _M_ode.model._t_OriginalFile)

if '_t_StringStringMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringStringMap = IcePy.defineDictionary('::ode::api::StringStringMap', (), IcePy._t_string, IcePy._t_string)

if '_t_StringRStringMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringRStringMap = IcePy.defineDictionary('::ode::api::StringRStringMap', (), IcePy._t_string, _M_ode._t_RString)

if '_t_StringStringArrayMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringStringArrayMap = IcePy.defineDictionary('::ode::api::StringStringArrayMap', (), IcePy._t_string, _M_ode.api._t_StringArray)

if '_t_StringLongMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringLongMap = IcePy.defineDictionary('::ode::api::StringLongMap', (), IcePy._t_string, IcePy._t_long)

if '_t_StringIntMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringIntMap = IcePy.defineDictionary('::ode::api::StringIntMap', (), IcePy._t_string, IcePy._t_int)

if 'LongPair' not in _M_ode.api.__dict__:
    _M_ode.api.LongPair = Ice.createTempClass()
    class LongPair(object):
        def __init__(self, first=0, second=0):
            self.first = first
            self.second = second

        def __hash__(self):
            _h = 0
            _h = 5 * _h + Ice.getHash(self.first)
            _h = 5 * _h + Ice.getHash(self.second)
            return _h % 0x7fffffff

        def __compare(self, other):
            if other is None:
                return 1
            elif not isinstance(other, _M_ode.api.LongPair):
                return NotImplemented
            else:
                if self.first is None or other.first is None:
                    if self.first != other.first:
                        return (-1 if self.first is None else 1)
                else:
                    if self.first < other.first:
                        return -1
                    elif self.first > other.first:
                        return 1
                if self.second is None or other.second is None:
                    if self.second != other.second:
                        return (-1 if self.second is None else 1)
                else:
                    if self.second < other.second:
                        return -1
                    elif self.second > other.second:
                        return 1
                return 0

        def __lt__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r < 0

        def __le__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r <= 0

        def __gt__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r > 0

        def __ge__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r >= 0

        def __eq__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r == 0

        def __ne__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r != 0

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_LongPair)

        __repr__ = __str__

    _M_ode.api._t_LongPair = IcePy.defineStruct('::ode::api::LongPair', LongPair, (), (
        ('first', (), IcePy._t_long),
        ('second', (), IcePy._t_long)
    ))

    _M_ode.api.LongPair = LongPair
    del LongPair

if '_t_LongPairLongMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongPairLongMap = IcePy.defineDictionary('::ode::api::LongPairLongMap', (), _M_ode.api._t_LongPair, IcePy._t_long)

if '_t_LongPairIntMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongPairIntMap = IcePy.defineDictionary('::ode::api::LongPairIntMap', (), _M_ode.api._t_LongPair, IcePy._t_int)

if '_t_LongPairToStringLongMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongPairToStringLongMap = IcePy.defineDictionary('::ode::api::LongPairToStringLongMap', (), _M_ode.api._t_LongPair, _M_ode.api._t_StringLongMap)

if '_t_LongPairToStringIntMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongPairToStringIntMap = IcePy.defineDictionary('::ode::api::LongPairToStringIntMap', (), _M_ode.api._t_LongPair, _M_ode.api._t_StringIntMap)

if '_t_IdListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IdListMap = IcePy.defineDictionary('::ode::api::IdListMap', (), IcePy._t_string, _M_Ice._t_LongSeq)

if '_t_StringLongListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringLongListMap = IcePy.defineDictionary('::ode::api::StringLongListMap', (), IcePy._t_string, _M_ode.api._t_LongList)

if '_t_BooleanLongListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_BooleanLongListMap = IcePy.defineDictionary('::ode::api::BooleanLongListMap', (), IcePy._t_bool, _M_ode.api._t_LongList)

if '_t_BooleanIdListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_BooleanIdListMap = IcePy.defineDictionary('::ode::api::BooleanIdListMap', (), IcePy._t_bool, _M_ode.sys._t_LongList)

if '_t_IObjectListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IObjectListMap = IcePy.defineDictionary('::ode::api::IObjectListMap', (), IcePy._t_string, _M_ode.api._t_IObjectList)

if '_t_LongIObjectListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongIObjectListMap = IcePy.defineDictionary('::ode::api::LongIObjectListMap', (), IcePy._t_long, _M_ode.api._t_IObjectList)

if '_t_StringShapeListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_StringShapeListMap = IcePy.defineDictionary('::ode::api::StringShapeListMap', (), IcePy._t_string, _M_ode.api._t_ShapeList)

if '_t_LongShapeListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongShapeListMap = IcePy.defineDictionary('::ode::api::LongShapeListMap', (), IcePy._t_long, _M_ode.api._t_ShapeList)

if '_t_IntShapeListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IntShapeListMap = IcePy.defineDictionary('::ode::api::IntShapeListMap', (), IcePy._t_int, _M_ode.api._t_ShapeList)

if '_t_LongAnnotationListMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongAnnotationListMap = IcePy.defineDictionary('::ode::api::LongAnnotationListMap', (), IcePy._t_long, _M_ode.api._t_AnnotationList)

if '_t_IdBooleanLongListMapMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_IdBooleanLongListMapMap = IcePy.defineDictionary('::ode::api::IdBooleanLongListMapMap', (), IcePy._t_long, _M_ode.api._t_BooleanLongListMap)

# End of module ode.api

__name__ = 'ode'

# End of module ode
