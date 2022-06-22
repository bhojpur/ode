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

from ode.testlib import ITest

from ode.callbacks import CmdCallbackI
from ode.cmd import Delete2
from ode.grid import ImportSettings
from ode.model import ChecksumAlgorithmI
from ode.model import FilesetI
from ode.model import FilesetEntryI
from ode.model import ImageI
from ode.model import OriginalFileI
from ode.model import PixelsI
from ode.model import PixelsOriginalFileMapI
from ode.model import UploadJobI
from ode.rtypes import rint
from ode.rtypes import rstring
from ode.rtypes import unwrap
from ode.sys import ParametersI
from ode.util.temp_files import create_path

class TestReimportArchivedFiles(ITest):

    def setup_method(self, method):
        self.pixels = self.client.sf.getPixelsService()
        self.query = self.client.sf.getQueryService()
        self.update = self.client.sf.getUpdateService()

    def duplicateMIF(self, orig_img):
        """
        Use copyAndResizeImage to create a "synthetic" image
        (one without a fileset)
        """
        new_img = self.pixels.copyAndResizeImage(
            orig_img.id.val, rint(16), rint(16), rint(1), rint(1),
            [0], None, True).val
        pix_id = unwrap(self.query.projection(
            "select p.id from Image i join i.pixels p where i.id = :id",
            ParametersI().addId(new_img)))[0][0]
        new_img = ImageI(new_img, False)
        new_pix = PixelsI(pix_id, False)
        return new_img, new_pix

    def copyPixels(self, orig_pix, new_pix):
        orig_source = self.client.sf.createRawPixelsStore()
        new_sink = self.client.sf.createRawPixelsStore()

        try:
            orig_source.setPixelsId(orig_pix.id.val, False)
            new_sink.setPixelsId(new_pix.id.val, False)
            buf = orig_source.getPlane(0, 0, 0)
            new_sink.setPlane(buf, 0, 0, 0)
        finally:
            orig_source.close()
            new_sink.close()

    def copyFiles(self, orig_img, new_img, new_pix):
        # Then attach a copy of each of the used files in the fileset
        # to the synthetic image
        params = ParametersI()
        params.addId(orig_img.id.val)
        rows = unwrap(self.query.projection((
            "select f.id, f.name from Image i "
            "join i.fileset fs join fs.usedFiles uf "
            "join uf.originalFile f where i.id = :id"), params))
        for row in rows:
            file_id = row[0]
            file_name = row[1]
            target = create_path()
            src = OriginalFileI(file_id, False)
            self.client.download(ofile=src, filename=str(target))
            copy = self.client.upload(filename=str(target),
                                      name=file_name)
            link = PixelsOriginalFileMapI()
            link.parent = copy.proxy()
            link.child = new_pix
            self.update.saveObject(link)

    def delete(self, obj_type, obj):
        delete = Delete2(targetObjects={obj_type: [obj.id.val]})
        return self.submit(delete)

    def submit(self, req):
        import ode
        handle = self.client.sf.submit(req)
        cb = CmdCallbackI(self.client, handle)
        try:
            cb.loop(50, 1000)
            rsp = cb.getResponse()
            if isinstance(rsp, ode.cmd.ERR):
                raise Exception(rsp)
            return rsp
        finally:
            cb.close(True)

    def createSynthetic(self):
        """ Create a image with archived files (i.e. pre-FS) """

        from ode.sys import ParametersI

        # Produce an FS image as our template
        orig_img = self.import_fake_file(name="reimport", sizeX=16, sizeY=16,
                                         with_companion=True, skip=None)
        orig_img = orig_img[0]
        orig_pix = self.query.findByQuery(
            "select p from Pixels p where p.image.id = :id",
            ParametersI().addId(orig_img.id.val))
        orig_fs = self.query.findByQuery(
            "select f from Image i join i.fileset f where i.id = :id",
            ParametersI().addId(orig_img.id.val))

        try:
            new_img, new_pix = self.duplicateMIF(orig_img)
            self.copyPixels(orig_pix, new_pix)
            self.copyFiles(orig_img, new_img, new_pix)
            return new_img
        finally:
            self.delete("Fileset", orig_fs)

    def archivedFiles(self, img_obj):
        return \
            self.client.sf.getQueryService().findAllByQuery((
                "select o from Image i join i.pixels p "
                "join p.pixelsFileMaps m join m.parent o "
                "where i.id = :id"),
                ParametersI().addId(img_obj.id.val))

    def startUpload(self, files):
        mrepo = self.client.getManagedRepository()
        fs = FilesetI()
        fs.linkJob(UploadJobI())
        for file in files:
            entry = FilesetEntryI()
            entry.clientPath = rstring("%s/%s" % (
                file.path.val, file.name.val
            ))
            fs.addFilesetEntry(entry)
        settings = ImportSettings()
        settings.checksumAlgorithm = ChecksumAlgorithmI()
        settings.checksumAlgorithm.value = rstring("SHA1-160")
        return mrepo.importFileset(fs, settings)

    def uploadFileset(self, proc, files):
        tmp = create_path()
        hashes = []
        for idx, file in enumerate(files):
            prx = proc.getUploader(idx)
            self.client.download(file, filename=tmp)
            hashes.append(self.client.sha1(tmp))
            with open(tmp, "r") as source:
                self.client.write_stream(source, prx)
            prx.close()
        tmp.remove()
        return proc.verifyUpload(hashes)

    def linkImageToFileset(self, new_img, fs):
        new_img = self.client.sf.getQueryService().get(
            "Image", new_img.id.val, {"ode.group": "-1"})
        new_img.setFileset(fs.proxy())
        self.client.sf.getUpdateService().saveObject(new_img)

    def imageBinaries(self, imageId,
                      togglePixels=False,
                      deletePyramid=False):

        import ode
        req = ode.cmd.ManageImageBinaries()
        req.imageId = imageId
        req.togglePixels = togglePixels
        req.deletePyramid = deletePyramid
        return self.submit(req)

    def assertManageImageBinaries(self, rsp, lenArchived=2,
                                  pixelSize=256, archivedSize=0,
                                  pyramidSize=0, thumbnailSize=0,
                                  pixelsPresent=True, pyramidPresent=False):

        assert lenArchived == len(rsp.archivedFiles)
        assert pixelsPresent == rsp.pixelsPresent
        assert pyramidPresent == rsp.pyramidPresent
        assert archivedSize == rsp.archivedSize
        assert pixelSize == rsp.pixelSize
        assert pyramidSize == rsp.pyramidSize
        assert thumbnailSize == rsp.thumbnailSize

    def testConvertSynthetic(self):
        """
        Convert a pre-FS file to FS
        using a README.txt as the first
        in the list.
        """
        readme_path = create_path()
        readme_path.write_text(
            """
            This file has been inserted into the
            fileset in order to prevent import.
            It can be safely deleted.
            """)
        readme_obj = self.client.upload(str(readme_path),
                                        name="README.txt")

        new_img = self.createSynthetic()
        binaries = self.imageBinaries(new_img.id.val)
        self.assertManageImageBinaries(binaries)

        files = self.archivedFiles(new_img)
        files.insert(0, readme_obj)
        proc = self.startUpload(files)
        handle = self.uploadFileset(proc, files)
        try:
            fs = handle.getRequest().activity.parent
            self.linkImageToFileset(new_img, fs)
            fs = self.client.sf.getQueryService().findByQuery((
                "select fs from Fileset fs "
                "join fetch fs.usedFiles "
                "where fs.id = :id"), ParametersI().addId(fs.id.val))
            used = fs.copyUsedFiles()
            fs.clearUsedFiles()
            for idx in range(1, 3):  # omit readme
                fs.addFilesetEntry(used[idx])
                used[idx].originalFile.unload()
            self.client.sf.getUpdateService().saveObject(fs)
            for file in files:
                self.delete("OriginalFile", file)
            binaries = self.imageBinaries(new_img.id.val)
            self.assertManageImageBinaries(binaries, lenArchived=0)
        finally:
            handle.close()

        binaries = self.imageBinaries(
            new_img.id.val, togglePixels=True)

        self.assertManageImageBinaries(
            binaries, lenArchived=0, pixelsPresent=False)
