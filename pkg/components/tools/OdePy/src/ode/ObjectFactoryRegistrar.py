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

class AcquisitionModeObjectFactory(Ice.ObjectFactory):

    from ode_model_AcquisitionModeI import AcquisitionModeI

    def create(self, type):
        return self.AcquisitionModeI()

    def destroy(self):
        pass

class AdminPrivilegeObjectFactory(Ice.ObjectFactory):

    from ode_model_AdminPrivilegeI import AdminPrivilegeI

    def create(self, type):
        return self.AdminPrivilegeI()

    def destroy(self):
        pass

class AffineTransformObjectFactory(Ice.ObjectFactory):

    from ode_model_AffineTransformI import AffineTransformI

    def create(self, type):
        return self.AffineTransformI()

    def destroy(self):
        pass

class AnnotationAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_AnnotationAnnotationLinkI import AnnotationAnnotationLinkI

    def create(self, type):
        return self.AnnotationAnnotationLinkI()

    def destroy(self):
        pass

class ArcObjectFactory(Ice.ObjectFactory):

    from ode_model_ArcI import ArcI

    def create(self, type):
        return self.ArcI()

    def destroy(self):
        pass

class ArcTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_ArcTypeI import ArcTypeI

    def create(self, type):
        return self.ArcTypeI()

    def destroy(self):
        pass

class BinningObjectFactory(Ice.ObjectFactory):

    from ode_model_BinningI import BinningI

    def create(self, type):
        return self.BinningI()

    def destroy(self):
        pass

class BooleanAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_BooleanAnnotationI import BooleanAnnotationI

    def create(self, type):
        return self.BooleanAnnotationI()

    def destroy(self):
        pass

class ChannelObjectFactory(Ice.ObjectFactory):

    from ode_model_ChannelI import ChannelI

    def create(self, type):
        return self.ChannelI()

    def destroy(self):
        pass

class ChannelAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ChannelAnnotationLinkI import ChannelAnnotationLinkI

    def create(self, type):
        return self.ChannelAnnotationLinkI()

    def destroy(self):
        pass

class ChannelBindingObjectFactory(Ice.ObjectFactory):

    from ode_model_ChannelBindingI import ChannelBindingI

    def create(self, type):
        return self.ChannelBindingI()

    def destroy(self):
        pass

class ChecksumAlgorithmObjectFactory(Ice.ObjectFactory):

    from ode_model_ChecksumAlgorithmI import ChecksumAlgorithmI

    def create(self, type):
        return self.ChecksumAlgorithmI()

    def destroy(self):
        pass

class CommentAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_CommentAnnotationI import CommentAnnotationI

    def create(self, type):
        return self.CommentAnnotationI()

    def destroy(self):
        pass

class ContrastMethodObjectFactory(Ice.ObjectFactory):

    from ode_model_ContrastMethodI import ContrastMethodI

    def create(self, type):
        return self.ContrastMethodI()

    def destroy(self):
        pass

class ContrastStretchingContextObjectFactory(Ice.ObjectFactory):

    from ode_model_ContrastStretchingContextI import ContrastStretchingContextI

    def create(self, type):
        return self.ContrastStretchingContextI()

    def destroy(self):
        pass

class CorrectionObjectFactory(Ice.ObjectFactory):

    from ode_model_CorrectionI import CorrectionI

    def create(self, type):
        return self.CorrectionI()

    def destroy(self):
        pass

class DBPatchObjectFactory(Ice.ObjectFactory):

    from ode_model_DBPatchI import DBPatchI

    def create(self, type):
        return self.DBPatchI()

    def destroy(self):
        pass

class DatasetObjectFactory(Ice.ObjectFactory):

    from ode_model_DatasetI import DatasetI

    def create(self, type):
        return self.DatasetI()

    def destroy(self):
        pass

class DatasetAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_DatasetAnnotationLinkI import DatasetAnnotationLinkI

    def create(self, type):
        return self.DatasetAnnotationLinkI()

    def destroy(self):
        pass

class DatasetImageLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_DatasetImageLinkI import DatasetImageLinkI

    def create(self, type):
        return self.DatasetImageLinkI()

    def destroy(self):
        pass

class DetectorObjectFactory(Ice.ObjectFactory):

    from ode_model_DetectorI import DetectorI

    def create(self, type):
        return self.DetectorI()

    def destroy(self):
        pass

class DetectorAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_DetectorAnnotationLinkI import DetectorAnnotationLinkI

    def create(self, type):
        return self.DetectorAnnotationLinkI()

    def destroy(self):
        pass

class DetectorSettingsObjectFactory(Ice.ObjectFactory):

    from ode_model_DetectorSettingsI import DetectorSettingsI

    def create(self, type):
        return self.DetectorSettingsI()

    def destroy(self):
        pass

class DetectorTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_DetectorTypeI import DetectorTypeI

    def create(self, type):
        return self.DetectorTypeI()

    def destroy(self):
        pass

class DichroicObjectFactory(Ice.ObjectFactory):

    from ode_model_DichroicI import DichroicI

    def create(self, type):
        return self.DichroicI()

    def destroy(self):
        pass

class DichroicAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_DichroicAnnotationLinkI import DichroicAnnotationLinkI

    def create(self, type):
        return self.DichroicAnnotationLinkI()

    def destroy(self):
        pass

class DimensionOrderObjectFactory(Ice.ObjectFactory):

    from ode_model_DimensionOrderI import DimensionOrderI

    def create(self, type):
        return self.DimensionOrderI()

    def destroy(self):
        pass

class DoubleAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_DoubleAnnotationI import DoubleAnnotationI

    def create(self, type):
        return self.DoubleAnnotationI()

    def destroy(self):
        pass

class EllipseObjectFactory(Ice.ObjectFactory):

    from ode_model_EllipseI import EllipseI

    def create(self, type):
        return self.EllipseI()

    def destroy(self):
        pass

class EventObjectFactory(Ice.ObjectFactory):

    from ode_model_EventI import EventI

    def create(self, type):
        return self.EventI()

    def destroy(self):
        pass

class EventLogObjectFactory(Ice.ObjectFactory):

    from ode_model_EventLogI import EventLogI

    def create(self, type):
        return self.EventLogI()

    def destroy(self):
        pass

class EventTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_EventTypeI import EventTypeI

    def create(self, type):
        return self.EventTypeI()

    def destroy(self):
        pass

class ExperimentObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimentI import ExperimentI

    def create(self, type):
        return self.ExperimentI()

    def destroy(self):
        pass

class ExperimentTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimentTypeI import ExperimentTypeI

    def create(self, type):
        return self.ExperimentTypeI()

    def destroy(self):
        pass

class ExperimenterObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimenterI import ExperimenterI

    def create(self, type):
        return self.ExperimenterI()

    def destroy(self):
        pass

class ExperimenterAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimenterAnnotationLinkI import ExperimenterAnnotationLinkI

    def create(self, type):
        return self.ExperimenterAnnotationLinkI()

    def destroy(self):
        pass

class ExperimenterGroupObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimenterGroupI import ExperimenterGroupI

    def create(self, type):
        return self.ExperimenterGroupI()

    def destroy(self):
        pass

class ExperimenterGroupAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ExperimenterGroupAnnotationLinkI import ExperimenterGroupAnnotationLinkI

    def create(self, type):
        return self.ExperimenterGroupAnnotationLinkI()

    def destroy(self):
        pass

class ExternalInfoObjectFactory(Ice.ObjectFactory):

    from ode_model_ExternalInfoI import ExternalInfoI

    def create(self, type):
        return self.ExternalInfoI()

    def destroy(self):
        pass

class FamilyObjectFactory(Ice.ObjectFactory):

    from ode_model_FamilyI import FamilyI

    def create(self, type):
        return self.FamilyI()

    def destroy(self):
        pass

class FilamentObjectFactory(Ice.ObjectFactory):

    from ode_model_FilamentI import FilamentI

    def create(self, type):
        return self.FilamentI()

    def destroy(self):
        pass

class FilamentTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_FilamentTypeI import FilamentTypeI

    def create(self, type):
        return self.FilamentTypeI()

    def destroy(self):
        pass

class FileAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_FileAnnotationI import FileAnnotationI

    def create(self, type):
        return self.FileAnnotationI()

    def destroy(self):
        pass

class FilesetObjectFactory(Ice.ObjectFactory):

    from ode_model_FilesetI import FilesetI

    def create(self, type):
        return self.FilesetI()

    def destroy(self):
        pass

class FilesetAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FilesetAnnotationLinkI import FilesetAnnotationLinkI

    def create(self, type):
        return self.FilesetAnnotationLinkI()

    def destroy(self):
        pass

class FilesetEntryObjectFactory(Ice.ObjectFactory):

    from ode_model_FilesetEntryI import FilesetEntryI

    def create(self, type):
        return self.FilesetEntryI()

    def destroy(self):
        pass

class FilesetJobLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FilesetJobLinkI import FilesetJobLinkI

    def create(self, type):
        return self.FilesetJobLinkI()

    def destroy(self):
        pass

class FilterObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterI import FilterI

    def create(self, type):
        return self.FilterI()

    def destroy(self):
        pass

class FilterAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterAnnotationLinkI import FilterAnnotationLinkI

    def create(self, type):
        return self.FilterAnnotationLinkI()

    def destroy(self):
        pass

class FilterSetObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterSetI import FilterSetI

    def create(self, type):
        return self.FilterSetI()

    def destroy(self):
        pass

class FilterSetEmissionFilterLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterSetEmissionFilterLinkI import FilterSetEmissionFilterLinkI

    def create(self, type):
        return self.FilterSetEmissionFilterLinkI()

    def destroy(self):
        pass

class FilterSetExcitationFilterLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterSetExcitationFilterLinkI import FilterSetExcitationFilterLinkI

    def create(self, type):
        return self.FilterSetExcitationFilterLinkI()

    def destroy(self):
        pass

class FilterTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_FilterTypeI import FilterTypeI

    def create(self, type):
        return self.FilterTypeI()

    def destroy(self):
        pass

class FolderObjectFactory(Ice.ObjectFactory):

    from ode_model_FolderI import FolderI

    def create(self, type):
        return self.FolderI()

    def destroy(self):
        pass

class FolderAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FolderAnnotationLinkI import FolderAnnotationLinkI

    def create(self, type):
        return self.FolderAnnotationLinkI()

    def destroy(self):
        pass

class FolderImageLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FolderImageLinkI import FolderImageLinkI

    def create(self, type):
        return self.FolderImageLinkI()

    def destroy(self):
        pass

class FolderRoiLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_FolderRoiLinkI import FolderRoiLinkI

    def create(self, type):
        return self.FolderRoiLinkI()

    def destroy(self):
        pass

class FormatObjectFactory(Ice.ObjectFactory):

    from ode_model_FormatI import FormatI

    def create(self, type):
        return self.FormatI()

    def destroy(self):
        pass

class GenericExcitationSourceObjectFactory(Ice.ObjectFactory):

    from ode_model_GenericExcitationSourceI import GenericExcitationSourceI

    def create(self, type):
        return self.GenericExcitationSourceI()

    def destroy(self):
        pass

class GroupExperimenterMapObjectFactory(Ice.ObjectFactory):

    from ode_model_GroupExperimenterMapI import GroupExperimenterMapI

    def create(self, type):
        return self.GroupExperimenterMapI()

    def destroy(self):
        pass

class IlluminationObjectFactory(Ice.ObjectFactory):

    from ode_model_IlluminationI import IlluminationI

    def create(self, type):
        return self.IlluminationI()

    def destroy(self):
        pass

class ImageObjectFactory(Ice.ObjectFactory):

    from ode_model_ImageI import ImageI

    def create(self, type):
        return self.ImageI()

    def destroy(self):
        pass

class ImageAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ImageAnnotationLinkI import ImageAnnotationLinkI

    def create(self, type):
        return self.ImageAnnotationLinkI()

    def destroy(self):
        pass

class ImagingEnvironmentObjectFactory(Ice.ObjectFactory):

    from ode_model_ImagingEnvironmentI import ImagingEnvironmentI

    def create(self, type):
        return self.ImagingEnvironmentI()

    def destroy(self):
        pass

class ImmersionObjectFactory(Ice.ObjectFactory):

    from ode_model_ImmersionI import ImmersionI

    def create(self, type):
        return self.ImmersionI()

    def destroy(self):
        pass

class ImportJobObjectFactory(Ice.ObjectFactory):

    from ode_model_ImportJobI import ImportJobI

    def create(self, type):
        return self.ImportJobI()

    def destroy(self):
        pass

class IndexingJobObjectFactory(Ice.ObjectFactory):

    from ode_model_IndexingJobI import IndexingJobI

    def create(self, type):
        return self.IndexingJobI()

    def destroy(self):
        pass

class InstrumentObjectFactory(Ice.ObjectFactory):

    from ode_model_InstrumentI import InstrumentI

    def create(self, type):
        return self.InstrumentI()

    def destroy(self):
        pass

class InstrumentAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_InstrumentAnnotationLinkI import InstrumentAnnotationLinkI

    def create(self, type):
        return self.InstrumentAnnotationLinkI()

    def destroy(self):
        pass

class IntegrityCheckJobObjectFactory(Ice.ObjectFactory):

    from ode_model_IntegrityCheckJobI import IntegrityCheckJobI

    def create(self, type):
        return self.IntegrityCheckJobI()

    def destroy(self):
        pass

class JobOriginalFileLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_JobOriginalFileLinkI import JobOriginalFileLinkI

    def create(self, type):
        return self.JobOriginalFileLinkI()

    def destroy(self):
        pass

class JobStatusObjectFactory(Ice.ObjectFactory):

    from ode_model_JobStatusI import JobStatusI

    def create(self, type):
        return self.JobStatusI()

    def destroy(self):
        pass

class LabelObjectFactory(Ice.ObjectFactory):

    from ode_model_LabelI import LabelI

    def create(self, type):
        return self.LabelI()

    def destroy(self):
        pass

class LaserObjectFactory(Ice.ObjectFactory):

    from ode_model_LaserI import LaserI

    def create(self, type):
        return self.LaserI()

    def destroy(self):
        pass

class LaserMediumObjectFactory(Ice.ObjectFactory):

    from ode_model_LaserMediumI import LaserMediumI

    def create(self, type):
        return self.LaserMediumI()

    def destroy(self):
        pass

class LaserTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_LaserTypeI import LaserTypeI

    def create(self, type):
        return self.LaserTypeI()

    def destroy(self):
        pass

class LightEmittingDiodeObjectFactory(Ice.ObjectFactory):

    from ode_model_LightEmittingDiodeI import LightEmittingDiodeI

    def create(self, type):
        return self.LightEmittingDiodeI()

    def destroy(self):
        pass

class LightPathObjectFactory(Ice.ObjectFactory):

    from ode_model_LightPathI import LightPathI

    def create(self, type):
        return self.LightPathI()

    def destroy(self):
        pass

class LightPathAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_LightPathAnnotationLinkI import LightPathAnnotationLinkI

    def create(self, type):
        return self.LightPathAnnotationLinkI()

    def destroy(self):
        pass

class LightPathEmissionFilterLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_LightPathEmissionFilterLinkI import LightPathEmissionFilterLinkI

    def create(self, type):
        return self.LightPathEmissionFilterLinkI()

    def destroy(self):
        pass

class LightPathExcitationFilterLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_LightPathExcitationFilterLinkI import LightPathExcitationFilterLinkI

    def create(self, type):
        return self.LightPathExcitationFilterLinkI()

    def destroy(self):
        pass

class LightSettingsObjectFactory(Ice.ObjectFactory):

    from ode_model_LightSettingsI import LightSettingsI

    def create(self, type):
        return self.LightSettingsI()

    def destroy(self):
        pass

class LightSourceAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_LightSourceAnnotationLinkI import LightSourceAnnotationLinkI

    def create(self, type):
        return self.LightSourceAnnotationLinkI()

    def destroy(self):
        pass

class LineObjectFactory(Ice.ObjectFactory):

    from ode_model_LineI import LineI

    def create(self, type):
        return self.LineI()

    def destroy(self):
        pass

class LinkObjectFactory(Ice.ObjectFactory):

    from ode_model_LinkI import LinkI

    def create(self, type):
        return self.LinkI()

    def destroy(self):
        pass

class ListAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_ListAnnotationI import ListAnnotationI

    def create(self, type):
        return self.ListAnnotationI()

    def destroy(self):
        pass

class LogicalChannelObjectFactory(Ice.ObjectFactory):

    from ode_model_LogicalChannelI import LogicalChannelI

    def create(self, type):
        return self.LogicalChannelI()

    def destroy(self):
        pass

class LongAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_LongAnnotationI import LongAnnotationI

    def create(self, type):
        return self.LongAnnotationI()

    def destroy(self):
        pass

class MapAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_MapAnnotationI import MapAnnotationI

    def create(self, type):
        return self.MapAnnotationI()

    def destroy(self):
        pass

class MaskObjectFactory(Ice.ObjectFactory):

    from ode_model_MaskI import MaskI

    def create(self, type):
        return self.MaskI()

    def destroy(self):
        pass

class MediumObjectFactory(Ice.ObjectFactory):

    from ode_model_MediumI import MediumI

    def create(self, type):
        return self.MediumI()

    def destroy(self):
        pass

class MetadataImportJobObjectFactory(Ice.ObjectFactory):

    from ode_model_MetadataImportJobI import MetadataImportJobI

    def create(self, type):
        return self.MetadataImportJobI()

    def destroy(self):
        pass

class MicrobeamManipulationObjectFactory(Ice.ObjectFactory):

    from ode_model_MicrobeamManipulationI import MicrobeamManipulationI

    def create(self, type):
        return self.MicrobeamManipulationI()

    def destroy(self):
        pass

class MicrobeamManipulationTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_MicrobeamManipulationTypeI import MicrobeamManipulationTypeI

    def create(self, type):
        return self.MicrobeamManipulationTypeI()

    def destroy(self):
        pass

class MicroscopeObjectFactory(Ice.ObjectFactory):

    from ode_model_MicroscopeI import MicroscopeI

    def create(self, type):
        return self.MicroscopeI()

    def destroy(self):
        pass

class MicroscopeTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_MicroscopeTypeI import MicroscopeTypeI

    def create(self, type):
        return self.MicroscopeTypeI()

    def destroy(self):
        pass

class NamespaceObjectFactory(Ice.ObjectFactory):

    from ode_model_NamespaceI import NamespaceI

    def create(self, type):
        return self.NamespaceI()

    def destroy(self):
        pass

class NamespaceAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_NamespaceAnnotationLinkI import NamespaceAnnotationLinkI

    def create(self, type):
        return self.NamespaceAnnotationLinkI()

    def destroy(self):
        pass

class NodeObjectFactory(Ice.ObjectFactory):

    from ode_model_NodeI import NodeI

    def create(self, type):
        return self.NodeI()

    def destroy(self):
        pass

class NodeAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_NodeAnnotationLinkI import NodeAnnotationLinkI

    def create(self, type):
        return self.NodeAnnotationLinkI()

    def destroy(self):
        pass

class OTFObjectFactory(Ice.ObjectFactory):

    from ode_model_OTFI import OTFI

    def create(self, type):
        return self.OTFI()

    def destroy(self):
        pass

class ObjectiveObjectFactory(Ice.ObjectFactory):

    from ode_model_ObjectiveI import ObjectiveI

    def create(self, type):
        return self.ObjectiveI()

    def destroy(self):
        pass

class ObjectiveAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ObjectiveAnnotationLinkI import ObjectiveAnnotationLinkI

    def create(self, type):
        return self.ObjectiveAnnotationLinkI()

    def destroy(self):
        pass

class ObjectiveSettingsObjectFactory(Ice.ObjectFactory):

    from ode_model_ObjectiveSettingsI import ObjectiveSettingsI

    def create(self, type):
        return self.ObjectiveSettingsI()

    def destroy(self):
        pass

class OriginalFileObjectFactory(Ice.ObjectFactory):

    from ode_model_OriginalFileI import OriginalFileI

    def create(self, type):
        return self.OriginalFileI()

    def destroy(self):
        pass

class OriginalFileAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_OriginalFileAnnotationLinkI import OriginalFileAnnotationLinkI

    def create(self, type):
        return self.OriginalFileAnnotationLinkI()

    def destroy(self):
        pass

class ParseJobObjectFactory(Ice.ObjectFactory):

    from ode_model_ParseJobI import ParseJobI

    def create(self, type):
        return self.ParseJobI()

    def destroy(self):
        pass

class PathObjectFactory(Ice.ObjectFactory):

    from ode_model_PathI import PathI

    def create(self, type):
        return self.PathI()

    def destroy(self):
        pass

class PhotometricInterpretationObjectFactory(Ice.ObjectFactory):

    from ode_model_PhotometricInterpretationI import PhotometricInterpretationI

    def create(self, type):
        return self.PhotometricInterpretationI()

    def destroy(self):
        pass

class PixelDataJobObjectFactory(Ice.ObjectFactory):

    from ode_model_PixelDataJobI import PixelDataJobI

    def create(self, type):
        return self.PixelDataJobI()

    def destroy(self):
        pass

class PixelsObjectFactory(Ice.ObjectFactory):

    from ode_model_PixelsI import PixelsI

    def create(self, type):
        return self.PixelsI()

    def destroy(self):
        pass

class PixelsOriginalFileMapObjectFactory(Ice.ObjectFactory):

    from ode_model_PixelsOriginalFileMapI import PixelsOriginalFileMapI

    def create(self, type):
        return self.PixelsOriginalFileMapI()

    def destroy(self):
        pass

class PixelsTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_PixelsTypeI import PixelsTypeI

    def create(self, type):
        return self.PixelsTypeI()

    def destroy(self):
        pass

class PlaneInfoObjectFactory(Ice.ObjectFactory):

    from ode_model_PlaneInfoI import PlaneInfoI

    def create(self, type):
        return self.PlaneInfoI()

    def destroy(self):
        pass

class PlaneInfoAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_PlaneInfoAnnotationLinkI import PlaneInfoAnnotationLinkI

    def create(self, type):
        return self.PlaneInfoAnnotationLinkI()

    def destroy(self):
        pass

class PlaneSlicingContextObjectFactory(Ice.ObjectFactory):

    from ode_model_PlaneSlicingContextI import PlaneSlicingContextI

    def create(self, type):
        return self.PlaneSlicingContextI()

    def destroy(self):
        pass

class PlateObjectFactory(Ice.ObjectFactory):

    from ode_model_PlateI import PlateI

    def create(self, type):
        return self.PlateI()

    def destroy(self):
        pass

class PlateAcquisitionObjectFactory(Ice.ObjectFactory):

    from ode_model_PlateAcquisitionI import PlateAcquisitionI

    def create(self, type):
        return self.PlateAcquisitionI()

    def destroy(self):
        pass

class PlateAcquisitionAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_PlateAcquisitionAnnotationLinkI import PlateAcquisitionAnnotationLinkI

    def create(self, type):
        return self.PlateAcquisitionAnnotationLinkI()

    def destroy(self):
        pass

class PlateAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_PlateAnnotationLinkI import PlateAnnotationLinkI

    def create(self, type):
        return self.PlateAnnotationLinkI()

    def destroy(self):
        pass

class PointObjectFactory(Ice.ObjectFactory):

    from ode_model_PointI import PointI

    def create(self, type):
        return self.PointI()

    def destroy(self):
        pass

class PolygonObjectFactory(Ice.ObjectFactory):

    from ode_model_PolygonI import PolygonI

    def create(self, type):
        return self.PolygonI()

    def destroy(self):
        pass

class PolylineObjectFactory(Ice.ObjectFactory):

    from ode_model_PolylineI import PolylineI

    def create(self, type):
        return self.PolylineI()

    def destroy(self):
        pass

class ProjectObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectI import ProjectI

    def create(self, type):
        return self.ProjectI()

    def destroy(self):
        pass

class ProjectAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectAnnotationLinkI import ProjectAnnotationLinkI

    def create(self, type):
        return self.ProjectAnnotationLinkI()

    def destroy(self):
        pass

class ProjectDatasetLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectDatasetLinkI import ProjectDatasetLinkI

    def create(self, type):
        return self.ProjectDatasetLinkI()

    def destroy(self):
        pass

class ProjectionAxisObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectionAxisI import ProjectionAxisI

    def create(self, type):
        return self.ProjectionAxisI()

    def destroy(self):
        pass

class ProjectionDefObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectionDefI import ProjectionDefI

    def create(self, type):
        return self.ProjectionDefI()

    def destroy(self):
        pass

class ProjectionTypeObjectFactory(Ice.ObjectFactory):

    from ode_model_ProjectionTypeI import ProjectionTypeI

    def create(self, type):
        return self.ProjectionTypeI()

    def destroy(self):
        pass

class PulseObjectFactory(Ice.ObjectFactory):

    from ode_model_PulseI import PulseI

    def create(self, type):
        return self.PulseI()

    def destroy(self):
        pass

class QuantumDefObjectFactory(Ice.ObjectFactory):

    from ode_model_QuantumDefI import QuantumDefI

    def create(self, type):
        return self.QuantumDefI()

    def destroy(self):
        pass

class ReagentObjectFactory(Ice.ObjectFactory):

    from ode_model_ReagentI import ReagentI

    def create(self, type):
        return self.ReagentI()

    def destroy(self):
        pass

class ReagentAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ReagentAnnotationLinkI import ReagentAnnotationLinkI

    def create(self, type):
        return self.ReagentAnnotationLinkI()

    def destroy(self):
        pass

class RectangleObjectFactory(Ice.ObjectFactory):

    from ode_model_RectangleI import RectangleI

    def create(self, type):
        return self.RectangleI()

    def destroy(self):
        pass

class RenderingDefObjectFactory(Ice.ObjectFactory):

    from ode_model_RenderingDefI import RenderingDefI

    def create(self, type):
        return self.RenderingDefI()

    def destroy(self):
        pass

class RenderingModelObjectFactory(Ice.ObjectFactory):

    from ode_model_RenderingModelI import RenderingModelI

    def create(self, type):
        return self.RenderingModelI()

    def destroy(self):
        pass

class ReverseIntensityContextObjectFactory(Ice.ObjectFactory):

    from ode_model_ReverseIntensityContextI import ReverseIntensityContextI

    def create(self, type):
        return self.ReverseIntensityContextI()

    def destroy(self):
        pass

class RoiObjectFactory(Ice.ObjectFactory):

    from ode_model_RoiI import RoiI

    def create(self, type):
        return self.RoiI()

    def destroy(self):
        pass

class RoiAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_RoiAnnotationLinkI import RoiAnnotationLinkI

    def create(self, type):
        return self.RoiAnnotationLinkI()

    def destroy(self):
        pass

class ScreenObjectFactory(Ice.ObjectFactory):

    from ode_model_ScreenI import ScreenI

    def create(self, type):
        return self.ScreenI()

    def destroy(self):
        pass

class ScreenAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ScreenAnnotationLinkI import ScreenAnnotationLinkI

    def create(self, type):
        return self.ScreenAnnotationLinkI()

    def destroy(self):
        pass

class ScreenPlateLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ScreenPlateLinkI import ScreenPlateLinkI

    def create(self, type):
        return self.ScreenPlateLinkI()

    def destroy(self):
        pass

class ScriptJobObjectFactory(Ice.ObjectFactory):

    from ode_model_ScriptJobI import ScriptJobI

    def create(self, type):
        return self.ScriptJobI()

    def destroy(self):
        pass

class SessionObjectFactory(Ice.ObjectFactory):

    from ode_model_SessionI import SessionI

    def create(self, type):
        return self.SessionI()

    def destroy(self):
        pass

class SessionAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_SessionAnnotationLinkI import SessionAnnotationLinkI

    def create(self, type):
        return self.SessionAnnotationLinkI()

    def destroy(self):
        pass

class ShapeAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_ShapeAnnotationLinkI import ShapeAnnotationLinkI

    def create(self, type):
        return self.ShapeAnnotationLinkI()

    def destroy(self):
        pass

class ShareObjectFactory(Ice.ObjectFactory):

    from ode_model_ShareI import ShareI

    def create(self, type):
        return self.ShareI()

    def destroy(self):
        pass

class ShareMemberObjectFactory(Ice.ObjectFactory):

    from ode_model_ShareMemberI import ShareMemberI

    def create(self, type):
        return self.ShareMemberI()

    def destroy(self):
        pass

class StageLabelObjectFactory(Ice.ObjectFactory):

    from ode_model_StageLabelI import StageLabelI

    def create(self, type):
        return self.StageLabelI()

    def destroy(self):
        pass

class StatsInfoObjectFactory(Ice.ObjectFactory):

    from ode_model_StatsInfoI import StatsInfoI

    def create(self, type):
        return self.StatsInfoI()

    def destroy(self):
        pass

class TagAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_TagAnnotationI import TagAnnotationI

    def create(self, type):
        return self.TagAnnotationI()

    def destroy(self):
        pass

class TermAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_TermAnnotationI import TermAnnotationI

    def create(self, type):
        return self.TermAnnotationI()

    def destroy(self):
        pass

class ThumbnailObjectFactory(Ice.ObjectFactory):

    from ode_model_ThumbnailI import ThumbnailI

    def create(self, type):
        return self.ThumbnailI()

    def destroy(self):
        pass

class ThumbnailGenerationJobObjectFactory(Ice.ObjectFactory):

    from ode_model_ThumbnailGenerationJobI import ThumbnailGenerationJobI

    def create(self, type):
        return self.ThumbnailGenerationJobI()

    def destroy(self):
        pass

class TimestampAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_TimestampAnnotationI import TimestampAnnotationI

    def create(self, type):
        return self.TimestampAnnotationI()

    def destroy(self):
        pass

class TransmittanceRangeObjectFactory(Ice.ObjectFactory):

    from ode_model_TransmittanceRangeI import TransmittanceRangeI

    def create(self, type):
        return self.TransmittanceRangeI()

    def destroy(self):
        pass

class UploadJobObjectFactory(Ice.ObjectFactory):

    from ode_model_UploadJobI import UploadJobI

    def create(self, type):
        return self.UploadJobI()

    def destroy(self):
        pass

class WellObjectFactory(Ice.ObjectFactory):

    from ode_model_WellI import WellI

    def create(self, type):
        return self.WellI()

    def destroy(self):
        pass

class WellAnnotationLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_WellAnnotationLinkI import WellAnnotationLinkI

    def create(self, type):
        return self.WellAnnotationLinkI()

    def destroy(self):
        pass

class WellReagentLinkObjectFactory(Ice.ObjectFactory):

    from ode_model_WellReagentLinkI import WellReagentLinkI

    def create(self, type):
        return self.WellReagentLinkI()

    def destroy(self):
        pass

class WellSampleObjectFactory(Ice.ObjectFactory):

    from ode_model_WellSampleI import WellSampleI

    def create(self, type):
        return self.WellSampleI()

    def destroy(self):
        pass

class XmlAnnotationObjectFactory(Ice.ObjectFactory):

    from ode_model_XmlAnnotationI import XmlAnnotationI

    def create(self, type):
        return self.XmlAnnotationI()

    def destroy(self):
        pass

class PermissionsObjectFactory(Ice.ObjectFactory):

    from ode_model_PermissionsI import PermissionsI

    def create(self, type):
        return self.PermissionsI()

    def destroy(self):
        pass

class DetailsObjectFactory(Ice.ObjectFactory):

    from ode_model_DetailsI import DetailsI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.DetailsI(self.client)

    def destroy(self):
        pass

class ElectricPotentialObjectFactory(Ice.ObjectFactory):

    from ode_model_ElectricPotentialI import ElectricPotentialI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.ElectricPotentialI()

    def destroy(self):
        pass

class FrequencyObjectFactory(Ice.ObjectFactory):

    from ode_model_FrequencyI import FrequencyI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.FrequencyI()

    def destroy(self):
        pass

class LengthObjectFactory(Ice.ObjectFactory):

    from ode_model_LengthI import LengthI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.LengthI()

    def destroy(self):
        pass

class PowerObjectFactory(Ice.ObjectFactory):

    from ode_model_PowerI import PowerI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.PowerI()

    def destroy(self):
        pass

class PressureObjectFactory(Ice.ObjectFactory):

    from ode_model_PressureI import PressureI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.PressureI()

    def destroy(self):
        pass

class TemperatureObjectFactory(Ice.ObjectFactory):

    from ode_model_TemperatureI import TemperatureI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.TemperatureI()

    def destroy(self):
        pass

class TimeObjectFactory(Ice.ObjectFactory):

    from ode_model_TimeI import TimeI

    def __init__(self, client = None):
        self.client = client

    def create(self, type):
        return self.TimeI()

    def destroy(self):
        pass

def conditionalAdd(name, ic, of):
    if not ic.findObjectFactory(name):
        ic.addObjectFactory(of, name)

def registerObjectFactory(ic, client = None):
    conditionalAdd("::ode::model::Permissions", ic, PermissionsObjectFactory())
    conditionalAdd("::ode::model::Details", ic, DetailsObjectFactory(client))
    conditionalAdd("::ode::model::ElectricPotential", ic, ElectricPotentialObjectFactory(client))
    conditionalAdd("::ode::model::Frequency", ic, FrequencyObjectFactory(client))
    conditionalAdd("::ode::model::Length", ic, LengthObjectFactory(client))
    conditionalAdd("::ode::model::Power", ic, PowerObjectFactory(client))
    conditionalAdd("::ode::model::Pressure", ic, PressureObjectFactory(client))
    conditionalAdd("::ode::model::Temperature", ic, TemperatureObjectFactory(client))
    conditionalAdd("::ode::model::Time", ic, TimeObjectFactory(client))
    conditionalAdd("::ode::model::AcquisitionMode", ic,  AcquisitionModeObjectFactory())
    conditionalAdd("::ode::model::AdminPrivilege", ic,  AdminPrivilegeObjectFactory())
    conditionalAdd("::ode::model::AffineTransform", ic,  AffineTransformObjectFactory())
    conditionalAdd("::ode::model::AnnotationAnnotationLink", ic,  AnnotationAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Arc", ic,  ArcObjectFactory())
    conditionalAdd("::ode::model::ArcType", ic,  ArcTypeObjectFactory())
    conditionalAdd("::ode::model::Binning", ic,  BinningObjectFactory())
    conditionalAdd("::ode::model::BooleanAnnotation", ic,  BooleanAnnotationObjectFactory())
    conditionalAdd("::ode::model::Channel", ic,  ChannelObjectFactory())
    conditionalAdd("::ode::model::ChannelAnnotationLink", ic,  ChannelAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ChannelBinding", ic,  ChannelBindingObjectFactory())
    conditionalAdd("::ode::model::ChecksumAlgorithm", ic,  ChecksumAlgorithmObjectFactory())
    conditionalAdd("::ode::model::CommentAnnotation", ic,  CommentAnnotationObjectFactory())
    conditionalAdd("::ode::model::ContrastMethod", ic,  ContrastMethodObjectFactory())
    conditionalAdd("::ode::model::ContrastStretchingContext", ic,  ContrastStretchingContextObjectFactory())
    conditionalAdd("::ode::model::Correction", ic,  CorrectionObjectFactory())
    conditionalAdd("::ode::model::DBPatch", ic,  DBPatchObjectFactory())
    conditionalAdd("::ode::model::Dataset", ic,  DatasetObjectFactory())
    conditionalAdd("::ode::model::DatasetAnnotationLink", ic,  DatasetAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::DatasetImageLink", ic,  DatasetImageLinkObjectFactory())
    conditionalAdd("::ode::model::Detector", ic,  DetectorObjectFactory())
    conditionalAdd("::ode::model::DetectorAnnotationLink", ic,  DetectorAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::DetectorSettings", ic,  DetectorSettingsObjectFactory())
    conditionalAdd("::ode::model::DetectorType", ic,  DetectorTypeObjectFactory())
    conditionalAdd("::ode::model::Dichroic", ic,  DichroicObjectFactory())
    conditionalAdd("::ode::model::DichroicAnnotationLink", ic,  DichroicAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::DimensionOrder", ic,  DimensionOrderObjectFactory())
    conditionalAdd("::ode::model::DoubleAnnotation", ic,  DoubleAnnotationObjectFactory())
    conditionalAdd("::ode::model::Ellipse", ic,  EllipseObjectFactory())
    conditionalAdd("::ode::model::Event", ic,  EventObjectFactory())
    conditionalAdd("::ode::model::EventLog", ic,  EventLogObjectFactory())
    conditionalAdd("::ode::model::EventType", ic,  EventTypeObjectFactory())
    conditionalAdd("::ode::model::Experiment", ic,  ExperimentObjectFactory())
    conditionalAdd("::ode::model::ExperimentType", ic,  ExperimentTypeObjectFactory())
    conditionalAdd("::ode::model::Experimenter", ic,  ExperimenterObjectFactory())
    conditionalAdd("::ode::model::ExperimenterAnnotationLink", ic,  ExperimenterAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ExperimenterGroup", ic,  ExperimenterGroupObjectFactory())
    conditionalAdd("::ode::model::ExperimenterGroupAnnotationLink", ic,  ExperimenterGroupAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ExternalInfo", ic,  ExternalInfoObjectFactory())
    conditionalAdd("::ode::model::Family", ic,  FamilyObjectFactory())
    conditionalAdd("::ode::model::Filament", ic,  FilamentObjectFactory())
    conditionalAdd("::ode::model::FilamentType", ic,  FilamentTypeObjectFactory())
    conditionalAdd("::ode::model::FileAnnotation", ic,  FileAnnotationObjectFactory())
    conditionalAdd("::ode::model::Fileset", ic,  FilesetObjectFactory())
    conditionalAdd("::ode::model::FilesetAnnotationLink", ic,  FilesetAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::FilesetEntry", ic,  FilesetEntryObjectFactory())
    conditionalAdd("::ode::model::FilesetJobLink", ic,  FilesetJobLinkObjectFactory())
    conditionalAdd("::ode::model::Filter", ic,  FilterObjectFactory())
    conditionalAdd("::ode::model::FilterAnnotationLink", ic,  FilterAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::FilterSet", ic,  FilterSetObjectFactory())
    conditionalAdd("::ode::model::FilterSetEmissionFilterLink", ic,  FilterSetEmissionFilterLinkObjectFactory())
    conditionalAdd("::ode::model::FilterSetExcitationFilterLink", ic,  FilterSetExcitationFilterLinkObjectFactory())
    conditionalAdd("::ode::model::FilterType", ic,  FilterTypeObjectFactory())
    conditionalAdd("::ode::model::Folder", ic,  FolderObjectFactory())
    conditionalAdd("::ode::model::FolderAnnotationLink", ic,  FolderAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::FolderImageLink", ic,  FolderImageLinkObjectFactory())
    conditionalAdd("::ode::model::FolderRoiLink", ic,  FolderRoiLinkObjectFactory())
    conditionalAdd("::ode::model::Format", ic,  FormatObjectFactory())
    conditionalAdd("::ode::model::GenericExcitationSource", ic,  GenericExcitationSourceObjectFactory())
    conditionalAdd("::ode::model::GroupExperimenterMap", ic,  GroupExperimenterMapObjectFactory())
    conditionalAdd("::ode::model::Illumination", ic,  IlluminationObjectFactory())
    conditionalAdd("::ode::model::Image", ic,  ImageObjectFactory())
    conditionalAdd("::ode::model::ImageAnnotationLink", ic,  ImageAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ImagingEnvironment", ic,  ImagingEnvironmentObjectFactory())
    conditionalAdd("::ode::model::Immersion", ic,  ImmersionObjectFactory())
    conditionalAdd("::ode::model::ImportJob", ic,  ImportJobObjectFactory())
    conditionalAdd("::ode::model::IndexingJob", ic,  IndexingJobObjectFactory())
    conditionalAdd("::ode::model::Instrument", ic,  InstrumentObjectFactory())
    conditionalAdd("::ode::model::InstrumentAnnotationLink", ic,  InstrumentAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::IntegrityCheckJob", ic,  IntegrityCheckJobObjectFactory())
    conditionalAdd("::ode::model::JobOriginalFileLink", ic,  JobOriginalFileLinkObjectFactory())
    conditionalAdd("::ode::model::JobStatus", ic,  JobStatusObjectFactory())
    conditionalAdd("::ode::model::Label", ic,  LabelObjectFactory())
    conditionalAdd("::ode::model::Laser", ic,  LaserObjectFactory())
    conditionalAdd("::ode::model::LaserMedium", ic,  LaserMediumObjectFactory())
    conditionalAdd("::ode::model::LaserType", ic,  LaserTypeObjectFactory())
    conditionalAdd("::ode::model::LightEmittingDiode", ic,  LightEmittingDiodeObjectFactory())
    conditionalAdd("::ode::model::LightPath", ic,  LightPathObjectFactory())
    conditionalAdd("::ode::model::LightPathAnnotationLink", ic,  LightPathAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::LightPathEmissionFilterLink", ic,  LightPathEmissionFilterLinkObjectFactory())
    conditionalAdd("::ode::model::LightPathExcitationFilterLink", ic,  LightPathExcitationFilterLinkObjectFactory())
    conditionalAdd("::ode::model::LightSettings", ic,  LightSettingsObjectFactory())
    conditionalAdd("::ode::model::LightSourceAnnotationLink", ic,  LightSourceAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Line", ic,  LineObjectFactory())
    conditionalAdd("::ode::model::Link", ic,  LinkObjectFactory())
    conditionalAdd("::ode::model::ListAnnotation", ic,  ListAnnotationObjectFactory())
    conditionalAdd("::ode::model::LogicalChannel", ic,  LogicalChannelObjectFactory())
    conditionalAdd("::ode::model::LongAnnotation", ic,  LongAnnotationObjectFactory())
    conditionalAdd("::ode::model::MapAnnotation", ic,  MapAnnotationObjectFactory())
    conditionalAdd("::ode::model::Mask", ic,  MaskObjectFactory())
    conditionalAdd("::ode::model::Medium", ic,  MediumObjectFactory())
    conditionalAdd("::ode::model::MetadataImportJob", ic,  MetadataImportJobObjectFactory())
    conditionalAdd("::ode::model::MicrobeamManipulation", ic,  MicrobeamManipulationObjectFactory())
    conditionalAdd("::ode::model::MicrobeamManipulationType", ic,  MicrobeamManipulationTypeObjectFactory())
    conditionalAdd("::ode::model::Microscope", ic,  MicroscopeObjectFactory())
    conditionalAdd("::ode::model::MicroscopeType", ic,  MicroscopeTypeObjectFactory())
    conditionalAdd("::ode::model::Namespace", ic,  NamespaceObjectFactory())
    conditionalAdd("::ode::model::NamespaceAnnotationLink", ic,  NamespaceAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Node", ic,  NodeObjectFactory())
    conditionalAdd("::ode::model::NodeAnnotationLink", ic,  NodeAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::OTF", ic,  OTFObjectFactory())
    conditionalAdd("::ode::model::Objective", ic,  ObjectiveObjectFactory())
    conditionalAdd("::ode::model::ObjectiveAnnotationLink", ic,  ObjectiveAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ObjectiveSettings", ic,  ObjectiveSettingsObjectFactory())
    conditionalAdd("::ode::model::OriginalFile", ic,  OriginalFileObjectFactory())
    conditionalAdd("::ode::model::OriginalFileAnnotationLink", ic,  OriginalFileAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ParseJob", ic,  ParseJobObjectFactory())
    conditionalAdd("::ode::model::Path", ic,  PathObjectFactory())
    conditionalAdd("::ode::model::PhotometricInterpretation", ic,  PhotometricInterpretationObjectFactory())
    conditionalAdd("::ode::model::PixelDataJob", ic,  PixelDataJobObjectFactory())
    conditionalAdd("::ode::model::Pixels", ic,  PixelsObjectFactory())
    conditionalAdd("::ode::model::PixelsOriginalFileMap", ic,  PixelsOriginalFileMapObjectFactory())
    conditionalAdd("::ode::model::PixelsType", ic,  PixelsTypeObjectFactory())
    conditionalAdd("::ode::model::PlaneInfo", ic,  PlaneInfoObjectFactory())
    conditionalAdd("::ode::model::PlaneInfoAnnotationLink", ic,  PlaneInfoAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::PlaneSlicingContext", ic,  PlaneSlicingContextObjectFactory())
    conditionalAdd("::ode::model::Plate", ic,  PlateObjectFactory())
    conditionalAdd("::ode::model::PlateAcquisition", ic,  PlateAcquisitionObjectFactory())
    conditionalAdd("::ode::model::PlateAcquisitionAnnotationLink", ic,  PlateAcquisitionAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::PlateAnnotationLink", ic,  PlateAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Point", ic,  PointObjectFactory())
    conditionalAdd("::ode::model::Polygon", ic,  PolygonObjectFactory())
    conditionalAdd("::ode::model::Polyline", ic,  PolylineObjectFactory())
    conditionalAdd("::ode::model::Project", ic,  ProjectObjectFactory())
    conditionalAdd("::ode::model::ProjectAnnotationLink", ic,  ProjectAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ProjectDatasetLink", ic,  ProjectDatasetLinkObjectFactory())
    conditionalAdd("::ode::model::ProjectionAxis", ic,  ProjectionAxisObjectFactory())
    conditionalAdd("::ode::model::ProjectionDef", ic,  ProjectionDefObjectFactory())
    conditionalAdd("::ode::model::ProjectionType", ic,  ProjectionTypeObjectFactory())
    conditionalAdd("::ode::model::Pulse", ic,  PulseObjectFactory())
    conditionalAdd("::ode::model::QuantumDef", ic,  QuantumDefObjectFactory())
    conditionalAdd("::ode::model::Reagent", ic,  ReagentObjectFactory())
    conditionalAdd("::ode::model::ReagentAnnotationLink", ic,  ReagentAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Rectangle", ic,  RectangleObjectFactory())
    conditionalAdd("::ode::model::RenderingDef", ic,  RenderingDefObjectFactory())
    conditionalAdd("::ode::model::RenderingModel", ic,  RenderingModelObjectFactory())
    conditionalAdd("::ode::model::ReverseIntensityContext", ic,  ReverseIntensityContextObjectFactory())
    conditionalAdd("::ode::model::Roi", ic,  RoiObjectFactory())
    conditionalAdd("::ode::model::RoiAnnotationLink", ic,  RoiAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Screen", ic,  ScreenObjectFactory())
    conditionalAdd("::ode::model::ScreenAnnotationLink", ic,  ScreenAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ScreenPlateLink", ic,  ScreenPlateLinkObjectFactory())
    conditionalAdd("::ode::model::ScriptJob", ic,  ScriptJobObjectFactory())
    conditionalAdd("::ode::model::Session", ic,  SessionObjectFactory())
    conditionalAdd("::ode::model::SessionAnnotationLink", ic,  SessionAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::ShapeAnnotationLink", ic,  ShapeAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::Share", ic,  ShareObjectFactory())
    conditionalAdd("::ode::model::ShareMember", ic,  ShareMemberObjectFactory())
    conditionalAdd("::ode::model::StageLabel", ic,  StageLabelObjectFactory())
    conditionalAdd("::ode::model::StatsInfo", ic,  StatsInfoObjectFactory())
    conditionalAdd("::ode::model::TagAnnotation", ic,  TagAnnotationObjectFactory())
    conditionalAdd("::ode::model::TermAnnotation", ic,  TermAnnotationObjectFactory())
    conditionalAdd("::ode::model::Thumbnail", ic,  ThumbnailObjectFactory())
    conditionalAdd("::ode::model::ThumbnailGenerationJob", ic,  ThumbnailGenerationJobObjectFactory())
    conditionalAdd("::ode::model::TimestampAnnotation", ic,  TimestampAnnotationObjectFactory())
    conditionalAdd("::ode::model::TransmittanceRange", ic,  TransmittanceRangeObjectFactory())
    conditionalAdd("::ode::model::UploadJob", ic,  UploadJobObjectFactory())
    conditionalAdd("::ode::model::Well", ic,  WellObjectFactory())
    conditionalAdd("::ode::model::WellAnnotationLink", ic,  WellAnnotationLinkObjectFactory())
    conditionalAdd("::ode::model::WellReagentLink", ic,  WellReagentLinkObjectFactory())
    conditionalAdd("::ode::model::WellSample", ic,  WellSampleObjectFactory())
    conditionalAdd("::ode::model::XmlAnnotation", ic,  XmlAnnotationObjectFactory())

