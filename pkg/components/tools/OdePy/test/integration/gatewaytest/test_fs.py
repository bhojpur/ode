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

"""
   gateway tests - Testing the gateway image wrapper

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import pytest

from ode.model import ImageI, PixelsI, FilesetI, FilesetEntryI, \
    OriginalFileI, DimensionOrderI, PixelsTypeI, CommentAnnotationI, \
    LongAnnotationI
from ode.rtypes import rstring, rlong, rint, rtime
from uuid import uuid4

def uuid():
    return str(uuid4())

def create_image(image_index):
    image = ImageI()
    image.name = rstring('%s_%d' % (uuid(), image_index))
    image.acquisitionDate = rtime(0)
    pixels = PixelsI()
    pixels.sha1 = rstring('')
    pixels.sizeX = rint(1)
    pixels.sizeY = rint(1)
    pixels.sizeZ = rint(1)
    pixels.sizeC = rint(1)
    pixels.sizeT = rint(1)
    pixels.dimensionOrder = DimensionOrderI(1, False)  # XYZCT
    pixels.pixelsType = PixelsTypeI(1, False)  # bit
    image.addPixels(pixels)
    return image

@pytest.fixture()
def images_with_original_files(request, gatewaywrapper):
    """Creates Images with associated OriginalFiles."""
    gatewaywrapper.loginAsAuthor()
    gw = gatewaywrapper.gateway
    update_service = gw.getUpdateService()
    original_files = list()
    for original_file_index in range(2):
        original_file = OriginalFileI()
        original_file.name = rstring(
            'filename_%d.ext' % original_file_index
        )
        original_file.path = rstring('/server/path/')
        original_file.size = rlong(50)
        original_files.append(original_file)
    images = list()
    for image_index in range(2):
        image = create_image(image_index)
        for original_file in original_files:
            image.getPrimaryPixels().linkOriginalFile(original_file)
        images.append(image)
    image_ids = update_service.saveAndReturnIds(images)
    return [gw.getObject('Image', image_id) for image_id in image_ids]

def create_fileset():
    """Creates and returns a Fileset with associated Images."""
    fileset = FilesetI()
    fileset.templatePrefix = rstring('')
    for image_index in range(2):
        image = create_image(image_index)
        for fileset_index in range(2):
            fileset_entry = FilesetEntryI()
            fileset_entry.clientPath = rstring(
                '/client/path/filename_%d.ext' % fileset_index
            )
            original_file = OriginalFileI()
            original_file.name = rstring('filename_%d.ext' % fileset_index)
            original_file.path = rstring('/server/path/')
            original_file.size = rlong(50)
            fileset_entry.originalFile = original_file
            fileset.addFilesetEntry(fileset_entry)
        fileset.addImage(image)
    return fileset

@pytest.fixture()
def fileset_with_images(request, gatewaywrapper):
    """Creates and returns a Fileset with associated Images."""
    gatewaywrapper.loginAsAuthor()
    update_service = gatewaywrapper.gateway.getUpdateService()
    fileset = create_fileset()
    fileset = update_service.saveAndReturnObject(fileset)
    return gatewaywrapper.gateway.getObject('Fileset', fileset.id.val)

@pytest.fixture()
def fileset_with_images_and_annotations(request, gatewaywrapper):
    gatewaywrapper.loginAsAuthor()
    update_service = gatewaywrapper.gateway.getUpdateService()
    fileset = create_fileset()
    comment_annotation = CommentAnnotationI()
    comment_annotation.ns = rstring('comment_annotation')
    comment_annotation.textValue = rstring('textValue')
    long_annotation = LongAnnotationI()
    long_annotation.ns = rstring('long_annotation')
    long_annotation.longValue = rlong(1)
    fileset.linkAnnotation(comment_annotation)
    fileset.linkAnnotation(long_annotation)
    fileset = update_service.saveAndReturnObject(fileset)
    return gatewaywrapper.gateway.getObject('Fileset', fileset.id.val)

class TestFileset(object):

    def assertFilesetFilesInfo(self, files_info):
        assert files_info['fileset'] is True
        assert files_info['count'] == 4
        assert files_info['size'] == 200

    def assertFilesetFilesInfoAnnotations(self, files_info):
        assert len(files_info['annotations']) == 2
        for annotation in files_info['annotations']:
            ns = annotation['ns']
            if ns == 'comment_annotation':
                assert annotation['value'] == 'textValue'
            elif ns == 'long_annotation':
                pass
            else:
                pytest.fail('Unexpected namespace: %r' % ns)

    def testCountArchivedFiles(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            assert image.countArchivedFiles() == 0

    def testCountFilesetFiles(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            assert image.countFilesetFiles() == 4

    def testCountImportedImageFiles(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            assert image.countImportedImageFiles() == 4

    def testGetImportedFilesInfo(self, gatewaywrapper, fileset_with_images):
        for image in fileset_with_images.copyImages():
            files_info = image.getImportedFilesInfo()
            self.assertFilesetFilesInfo(files_info)
            assert len(files_info['annotations']) == 0

    def testGetImportedFilesInfoWithAnnotations(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            files_info = image.getImportedFilesInfo()
            self.assertFilesetFilesInfo(files_info)
            self.assertFilesetFilesInfoAnnotations(files_info)

    def testGetArchivedFiles(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            len(list(image.getArchivedFiles())) == 4

    def testGetImportedImageFiles(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            len(list(image.getImportedImageFiles())) == 4

    def testGetArchivedFilesInfo(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        gw = gatewaywrapper.gateway
        for image in fileset_with_images_and_annotations.copyImages():
            files_info = gw.getArchivedFilesInfo([image.id])
            assert files_info == {'fileset': False, 'count': 0, 'size': 0}

    def testGetFilesetFilesInfo(self, gatewaywrapper, fileset_with_images):
        gw = gatewaywrapper.gateway
        for image in fileset_with_images.copyImages():
            files_info = gw.getFilesetFilesInfo([image.id])
            self.assertFilesetFilesInfo(files_info)
            assert len(files_info['annotations']) == 0

    def testGetFilesetFilesInfoWithAnnotations(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        gw = gatewaywrapper.gateway
        for image in fileset_with_images_and_annotations.copyImages():
            files_info = gw.getFilesetFilesInfo([image.id])
            self.assertFilesetFilesInfo(files_info)
            self.assertFilesetFilesInfoAnnotations(files_info)

    def testGetFilesetFilesInfoMultiple(
            self, gatewaywrapper, fileset_with_images):
        gw = gatewaywrapper.gateway
        image_ids = [
            v.id for v in fileset_with_images.copyImages()
        ]
        files_info = gw.getFilesetFilesInfo(image_ids)
        self.assertFilesetFilesInfo(files_info)
        assert len(files_info['annotations']) == 0

    def testGetFilesetFilesInfoMultipleWithAnnotations(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        gw = gatewaywrapper.gateway
        image_ids = [
            v.id for v in fileset_with_images_and_annotations.copyImages()
        ]
        files_info = gw.getFilesetFilesInfo(image_ids)
        self.assertFilesetFilesInfo(files_info)
        self.assertFilesetFilesInfoAnnotations(files_info)

    def testGetFileset(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            assert image.getFileset() is not None

    def testGetImportedImageFilePaths(
            self, gatewaywrapper, fileset_with_images_and_annotations):
        for image in fileset_with_images_and_annotations.copyImages():
            paths = image.getImportedImageFilePaths()
            paths['server_paths'].sort()
            assert paths['server_paths'] == [
                '/server/path/filename_0.ext',
                '/server/path/filename_0.ext',
                '/server/path/filename_1.ext',
                '/server/path/filename_1.ext'
            ]
            paths['client_paths'].sort()
            assert paths['client_paths'] == [
                '/client/path/filename_0.ext',
                '/client/path/filename_0.ext',
                '/client/path/filename_1.ext',
                '/client/path/filename_1.ext'
            ]

class TestArchivedOriginalFiles(object):

    def assertArchivedFilesInfo(self, files_info):
        assert files_info['fileset'] is False
        assert files_info['count'] == 2
        assert files_info['size'] == 100

    def testCountArchivedFiles(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            assert image.countArchivedFiles() == 2

    def testCountFilesetFiles(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            assert image.countFilesetFiles() == 0

    def testCountImportedImageFiles(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            assert image.countImportedImageFiles() == 2

    def testGetImportedFilesInfo(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            files_info = image.getImportedFilesInfo()
            self.assertArchivedFilesInfo(files_info)

    def testGetArchivedFiles(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            len(list(image.getArchivedFiles())) == 2

    def testGetImportedImageFiles(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            len(list(image.getImportedImageFiles())) == 2

    def testGetArchivedFilesInfo(
            self, gatewaywrapper, images_with_original_files):
        gw = gatewaywrapper.gateway
        for image in images_with_original_files:
            files_info = gw.getArchivedFilesInfo([image.id])
            self.assertArchivedFilesInfo(files_info)

    def testGetFilesetFilesInfo(
            self, gatewaywrapper, images_with_original_files):
        gw = gatewaywrapper.gateway
        for image in images_with_original_files:
            files_info = gw.getFilesetFilesInfo([image.id])
            assert files_info == {
                'annotations': list(), 'fileset': True, 'count': 0, 'size': 0
            }

    def testGetFilesetFilesInfoMultiple(
            self, gatewaywrapper, images_with_original_files):
        gw = gatewaywrapper.gateway
        image_ids = [v.id for v in images_with_original_files]
        files_info = gw.getFilesetFilesInfo(image_ids)
        assert files_info == {
            'annotations': list(), 'fileset': True, 'count': 0, 'size': 0
        }

    def testGetFileset(self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            assert image.getFileset() is None

    def testGetImportedImageFilePaths(
            self, gatewaywrapper, images_with_original_files):
        for image in images_with_original_files:
            paths = image.getImportedImageFilePaths()
            paths['server_paths'].sort()
            assert paths['server_paths'] == [
                '/server/path/filename_0.ext',
                '/server/path/filename_1.ext',
            ]
            assert len(paths['client_paths']) == 0
