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

# Start of module ode
_M_ode = Ice.openModule('ode')
__name__ = 'ode'

# Start of module ode.model
_M_ode.model = Ice.openModule('ode.model')
__name__ = 'ode.model'

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Arc' not in _M_ode.model.__dict__:
    _M_ode.model._t_Arc = IcePy.declareClass('::ode::model::Arc')
    _M_ode.model._t_ArcPrx = IcePy.declareProxy('::ode::model::Arc')

if 'BooleanAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_BooleanAnnotation = IcePy.declareClass('::ode::model::BooleanAnnotation')
    _M_ode.model._t_BooleanAnnotationPrx = IcePy.declareProxy('::ode::model::BooleanAnnotation')

if 'ChecksumAlgorithm' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChecksumAlgorithm = IcePy.declareClass('::ode::model::ChecksumAlgorithm')
    _M_ode.model._t_ChecksumAlgorithmPrx = IcePy.declareProxy('::ode::model::ChecksumAlgorithm')

if 'Dataset' not in _M_ode.model.__dict__:
    _M_ode.model._t_Dataset = IcePy.declareClass('::ode::model::Dataset')
    _M_ode.model._t_DatasetPrx = IcePy.declareProxy('::ode::model::Dataset')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if 'Event' not in _M_ode.model.__dict__:
    _M_ode.model._t_Event = IcePy.declareClass('::ode::model::Event')
    _M_ode.model._t_EventPrx = IcePy.declareProxy('::ode::model::Event')

if 'EventLog' not in _M_ode.model.__dict__:
    _M_ode.model._t_EventLog = IcePy.declareClass('::ode::model::EventLog')
    _M_ode.model._t_EventLogPrx = IcePy.declareProxy('::ode::model::EventLog')

if 'Experimenter' not in _M_ode.model.__dict__:
    _M_ode.model._t_Experimenter = IcePy.declareClass('::ode::model::Experimenter')
    _M_ode.model._t_ExperimenterPrx = IcePy.declareProxy('::ode::model::Experimenter')

if 'ExperimenterGroup' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExperimenterGroup = IcePy.declareClass('::ode::model::ExperimenterGroup')
    _M_ode.model._t_ExperimenterGroupPrx = IcePy.declareProxy('::ode::model::ExperimenterGroup')

if 'ExternalInfo' not in _M_ode.model.__dict__:
    _M_ode.model._t_ExternalInfo = IcePy.declareClass('::ode::model::ExternalInfo')
    _M_ode.model._t_ExternalInfoPrx = IcePy.declareProxy('::ode::model::ExternalInfo')

if 'Family' not in _M_ode.model.__dict__:
    _M_ode.model._t_Family = IcePy.declareClass('::ode::model::Family')
    _M_ode.model._t_FamilyPrx = IcePy.declareProxy('::ode::model::Family')

if 'Filament' not in _M_ode.model.__dict__:
    _M_ode.model._t_Filament = IcePy.declareClass('::ode::model::Filament')
    _M_ode.model._t_FilamentPrx = IcePy.declareProxy('::ode::model::Filament')

if 'Fileset' not in _M_ode.model.__dict__:
    _M_ode.model._t_Fileset = IcePy.declareClass('::ode::model::Fileset')
    _M_ode.model._t_FilesetPrx = IcePy.declareProxy('::ode::model::Fileset')

if 'FilesetJobLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetJobLink = IcePy.declareClass('::ode::model::FilesetJobLink')
    _M_ode.model._t_FilesetJobLinkPrx = IcePy.declareProxy('::ode::model::FilesetJobLink')

if 'Format' not in _M_ode.model.__dict__:
    _M_ode.model._t_Format = IcePy.declareClass('::ode::model::Format')
    _M_ode.model._t_FormatPrx = IcePy.declareProxy('::ode::model::Format')

if 'IObject' not in _M_ode.model.__dict__:
    _M_ode.model._t_IObject = IcePy.declareClass('::ode::model::IObject')
    _M_ode.model._t_IObjectPrx = IcePy.declareProxy('::ode::model::IObject')

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'Instrument' not in _M_ode.model.__dict__:
    _M_ode.model._t_Instrument = IcePy.declareClass('::ode::model::Instrument')
    _M_ode.model._t_InstrumentPrx = IcePy.declareProxy('::ode::model::Instrument')

if 'Job' not in _M_ode.model.__dict__:
    _M_ode.model._t_Job = IcePy.declareClass('::ode::model::Job')
    _M_ode.model._t_JobPrx = IcePy.declareProxy('::ode::model::Job')

if 'JobStatus' not in _M_ode.model.__dict__:
    _M_ode.model._t_JobStatus = IcePy.declareClass('::ode::model::JobStatus')
    _M_ode.model._t_JobStatusPrx = IcePy.declareProxy('::ode::model::JobStatus')

if 'Laser' not in _M_ode.model.__dict__:
    _M_ode.model._t_Laser = IcePy.declareClass('::ode::model::Laser')
    _M_ode.model._t_LaserPrx = IcePy.declareProxy('::ode::model::Laser')

if 'LogicalChannel' not in _M_ode.model.__dict__:
    _M_ode.model._t_LogicalChannel = IcePy.declareClass('::ode::model::LogicalChannel')
    _M_ode.model._t_LogicalChannelPrx = IcePy.declareProxy('::ode::model::LogicalChannel')

if 'NamedValue' not in _M_ode.model.__dict__:
    _M_ode.model._t_NamedValue = IcePy.declareClass('::ode::model::NamedValue')
    _M_ode.model._t_NamedValuePrx = IcePy.declareProxy('::ode::model::NamedValue')

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'Permissions' not in _M_ode.model.__dict__:
    _M_ode.model._t_Permissions = IcePy.declareClass('::ode::model::Permissions')
    _M_ode.model._t_PermissionsPrx = IcePy.declareProxy('::ode::model::Permissions')

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'PixelsType' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsType = IcePy.declareClass('::ode::model::PixelsType')
    _M_ode.model._t_PixelsTypePrx = IcePy.declareProxy('::ode::model::PixelsType')

if 'PlaneInfo' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlaneInfo = IcePy.declareClass('::ode::model::PlaneInfo')
    _M_ode.model._t_PlaneInfoPrx = IcePy.declareProxy('::ode::model::PlaneInfo')

if 'Plate' not in _M_ode.model.__dict__:
    _M_ode.model._t_Plate = IcePy.declareClass('::ode::model::Plate')
    _M_ode.model._t_PlatePrx = IcePy.declareProxy('::ode::model::Plate')

if 'Project' not in _M_ode.model.__dict__:
    _M_ode.model._t_Project = IcePy.declareClass('::ode::model::Project')
    _M_ode.model._t_ProjectPrx = IcePy.declareProxy('::ode::model::Project')

if 'QuantumDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_QuantumDef = IcePy.declareClass('::ode::model::QuantumDef')
    _M_ode.model._t_QuantumDefPrx = IcePy.declareProxy('::ode::model::QuantumDef')

if 'RenderingDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDef = IcePy.declareClass('::ode::model::RenderingDef')
    _M_ode.model._t_RenderingDefPrx = IcePy.declareProxy('::ode::model::RenderingDef')

if 'RenderingModel' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingModel = IcePy.declareClass('::ode::model::RenderingModel')
    _M_ode.model._t_RenderingModelPrx = IcePy.declareProxy('::ode::model::RenderingModel')

if 'Roi' not in _M_ode.model.__dict__:
    _M_ode.model._t_Roi = IcePy.declareClass('::ode::model::Roi')
    _M_ode.model._t_RoiPrx = IcePy.declareProxy('::ode::model::Roi')

if 'Screen' not in _M_ode.model.__dict__:
    _M_ode.model._t_Screen = IcePy.declareClass('::ode::model::Screen')
    _M_ode.model._t_ScreenPrx = IcePy.declareProxy('::ode::model::Screen')

if 'ScriptJob' not in _M_ode.model.__dict__:
    _M_ode.model._t_ScriptJob = IcePy.declareClass('::ode::model::ScriptJob')
    _M_ode.model._t_ScriptJobPrx = IcePy.declareProxy('::ode::model::ScriptJob')

if 'Shape' not in _M_ode.model.__dict__:
    _M_ode.model._t_Shape = IcePy.declareClass('::ode::model::Shape')
    _M_ode.model._t_ShapePrx = IcePy.declareProxy('::ode::model::Shape')

if 'Session' not in _M_ode.model.__dict__:
    _M_ode.model._t_Session = IcePy.declareClass('::ode::model::Session')
    _M_ode.model._t_SessionPrx = IcePy.declareProxy('::ode::model::Session')

if 'Share' not in _M_ode.model.__dict__:
    _M_ode.model._t_Share = IcePy.declareClass('::ode::model::Share')
    _M_ode.model._t_SharePrx = IcePy.declareProxy('::ode::model::Share')

if 'TextAnnotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_TextAnnotation = IcePy.declareClass('::ode::model::TextAnnotation')
    _M_ode.model._t_TextAnnotationPrx = IcePy.declareProxy('::ode::model::TextAnnotation')

if 'Well' not in _M_ode.model.__dict__:
    _M_ode.model._t_Well = IcePy.declareClass('::ode::model::Well')
    _M_ode.model._t_WellPrx = IcePy.declareProxy('::ode::model::Well')

# End of module ode.model

__name__ = 'ode'

# End of module ode
