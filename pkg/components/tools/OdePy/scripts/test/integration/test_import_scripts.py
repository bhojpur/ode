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
   Integration test for import scripts.
"""

import ode
import ode.scripts
from script import ScriptTest
from script import run_script
from ode.util.temp_files import create_path
from ode.gateway import ServerGateway

populate_metadata = "/ode/import_scripts/Populate_Metadata.py"

class TestImportScripts(ScriptTest):

    def test_populate_metadata_for_plate(self):
        sid = super(TestImportScripts, self).get_script(populate_metadata)
        assert sid > 0

        client, user = self.new_client_and_user()
        plates = self.import_plates(client, plate_cols=3, plate_rows=1)
        plate = plates[0]
        cvs_file = create_path("test_plate", ".csv")

        # create a file annotation
        with open(cvs_file.abspath(), 'w+') as f:
            f.write("Well,Well Type, Facility-Salt-Batch-ID\n")
            f.write("A01,Treatment,FOOL10041-101-2\n")
            f.write("A02,Control,\n")
            f.write("A03,Treatment,FOOL10041-101-2\n")

        conn = ServerGateway(client_obj=client)
        fa = conn.createFileAnnfromLocalFile(cvs_file, mimetype="text/csv")
        assert fa is not None
        assert fa.id > 0
        link = ode.model.PlateAnnotationLinkI()
        link.setParent(plate)
        link.setChild(ode.model.FileAnnotationI(fa.id, False))
        client.getSession().getUpdateService().saveAndReturnObject(link)
        # run the script
        plate_ids = []
        plate_ids.append(ode.rtypes.rlong(plate.id.val))

        args = {
            "Data_Type": ode.rtypes.rstring("Plate"),
            "IDs": ode.rtypes.rlist(plate_ids),
            "File_Annotation": ode.rtypes.rstring(str(fa.id))
        }
        message = run_script(client, sid, args, "Message")
        assert message is not None
        assert message.getValue().startswith('Table data populated')
        conn.close()

    def test_populate_metadata_for_screen(self):
        sid = super(TestImportScripts, self).get_script(populate_metadata)
        assert sid > 0

        client, user = self.new_client_and_user()
        update_service = client.getSession().getUpdateService()
        plates = self.import_plates(client, plate_cols=3, plate_rows=1)
        plate = plates[0]
        name = plate.name.val
        screen = ode.model.ScreenI()
        screen.name = ode.rtypes.rstring("test_for_screen")
        spl = ode.model.ScreenPlateLinkI()
        spl.setParent(screen)
        spl.setChild(plate)
        spl = update_service.saveAndReturnObject(spl)
        screen_id = spl.getParent().id.val
        assert screen_id > 0
        assert spl.getChild().id.val == plate.id.val

        cvs_file = create_path("test_screen", ".csv")

        # create a file annotation
        with open(cvs_file.abspath(), 'w+') as f:
            f.write("Well,Plate, Well Type, Facility-Salt-Batch-ID\n")
            f.write("A01,%s,Treatment,FOOL10041-101-2\n" % name)
            f.write("A02,%s,Control,\n" % name)
            f.write("A03,%s,Treatment,FOOL10041-101-2\n" % name)

        conn = ServerGateway(client_obj=client)
        fa = conn.createFileAnnfromLocalFile(cvs_file, mimetype="text/csv")
        assert fa is not None
        assert fa.id > 0
        link = ode.model.ScreenAnnotationLinkI()
        link.setParent(ode.model.ScreenI(screen_id, False))
        link.setChild(ode.model.FileAnnotationI(fa.id, False))
        link = update_service.saveAndReturnObject(link)
        assert link.id.val > 0
        # run the script
        screen_ids = []
        screen_ids.append(spl.getParent().id)

        args = {
            "Data_Type": ode.rtypes.rstring("Screen"),
            "IDs": ode.rtypes.rlist(screen_ids),
            "File_Annotation": ode.rtypes.rstring(str(fa.id))
        }
        message = run_script(client, sid, args, "Message")
        assert message is not None
        assert message.getValue().startswith('Table data populated')
        conn.close()
