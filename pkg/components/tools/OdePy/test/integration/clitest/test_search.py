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

import pytest

from datetime import date
from datetime import datetime
from datetime import timedelta

from ode.testlib.cli import CLITest
from ode.cli import NonZeroReturnCode
from ode.model import DatasetI
from ode.plugins.search import SearchControl
from ode.rtypes import rstring

class TestSearch(CLITest):

    def mkimage(self, with_acquisitionDate=False):
        self._uuid = self.uuid().replace("-", "")
        if with_acquisitionDate:
            filename_date = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
            self._image = self.import_fake_file(
                name=self._uuid, acquisitionDate=filename_date)[0]
        else:
            self._image = self.import_fake_file(name=self._uuid)[0]
        self.root.sf.getUpdateService().indexObject(self._image)

    def mkdataset(self):
        self._uuid_ds = self.uuid().replace("-", "")
        self._dataset = DatasetI()
        self._dataset.name = rstring(self._uuid_ds)
        update = self.client.sf.getUpdateService()
        self._dataset = update.saveAndReturnObject(self._dataset)
        self.root.sf.getUpdateService().indexObject(self._dataset)

    def short(self):
        return self._uuid[0:8]

    def days_ago(self, ago=1):
        t = date.today() - timedelta(ago)
        t = t.strftime("%Y-%m-%d")
        return t

    def setup_method(self, method):
        super(TestSearch, self).setup_method(method)
        self.cli.register("search", SearchControl, "TEST")
        self.args += ["search"]
        self.setup_mock()

    def go(self):
        self.cli.invoke(self.args, strict=True)
        return self.cli.get("search.results")

    def assertSearch(self, args, success=True, name=None):
        if name is None:
            name = self._uuid
        self.args.extend(list(args))
        if success:
            results = self.go()
            assert 1 == len(results)
            assert name in results[0].name.val
        else:
            with pytest.raises(NonZeroReturnCode):
                results = self.go()

    def test_search_basic(self):
        self.mkimage()
        self.assertSearch(("Image", self._uuid + "*"))

    def test_search_wildcard(self):
        self.mkimage()
        short = self.short()
        self.assertSearch(("Image", short + "*"))

    def test_search_name_field(self):
        self.mkimage()
        short = self.short()
        self.assertSearch(("Image", short + "*", "--field=name"))

    def test_search_description_field(self):
        self.mkimage()
        short = self.short()
        with pytest.raises(NonZeroReturnCode):
            # Not set on description
            self.assertSearch(("Image", short + "*",
                               "--field=description"))

    def test_search_style(self, capsys):
        self.mkimage()
        short = self.short()
        self.assertSearch(("Image", short + "*", "--style=plain"))
        o, e = capsys.readouterr()
        parts = o.split(",")
        assert "ImageI" == parts[1]
        assert ("%s" % self._image.id.val) == parts[2]

    def test_search_ids_only(self, capsys):
        self.mkimage()
        short = self.short()
        self.assertSearch(("Image", short + "*", "--ids-only"))
        o, e = capsys.readouterr()
        assert ("ImageI:%s" % self._image.id.val) in o

    @pytest.mark.parametrize("data", (
        (1, None, True, True),
        (1, None, False, False),
    ))
    def test_search_acquisition_date(self, data):
        from_ago, to_ago, with_acquisitionDate, success = data
        self.mkimage(with_acquisitionDate=with_acquisitionDate)
        short = self.short()
        args = ["Image", short + "*"]
        if from_ago:
            args += ["--from=%s" % self.days_ago(from_ago)]
        if to_ago:
            args += ["--to=%s" % self.days_ago(to_ago)]
        args += ["--date-type=acquisitionDate"]

        self.assertSearch(args, success=success)

    @pytest.mark.parametrize("data", (
        (1, None, None, True),
        (1, None, "import", True),
        (1, -1, None, True),
        (None, 1, None, False),
        (-1, None, None, False),
    ))
    def test_search_other_dates(self, data):
        from_ago, to_ago, date_type, success = data
        self.mkimage()
        short = self.short()
        args = ["Image", short + "*"]
        if from_ago:
            args += ["--from=%s" % self.days_ago(from_ago)]
        if to_ago:
            args += ["--to=%s" % self.days_ago(to_ago)]
        if date_type:
            args += ["--date-type=%s" % date_type]

        self.assertSearch(args, success=success)

    def test_search_no_parse(self):
        self.mkimage()
        short = self.short()
        args = ["Image", short + "*", "--no-parse"]
        self.assertSearch(args)

    def test_search_dataset_acquisition(self):
        self.mkdataset()
        txt = self._uuid_ds[0:8] + "*"
        _from = "--from=%s" % self.days_ago(1)
        _to = "--to=%s" % self.days_ago(-1)
        args = ["Dataset", txt, _from, _to]
        self.assertSearch(args, name=self._uuid_ds)

    def test_search_index_by_user(self, capsys):
        self.mkimage()
        short = self.short()
        self.args.extend(("Image", short + "*", "--index"))
        self.cli.invoke(self.args, strict=False)
        o, e = capsys.readouterr()
        assert 'Only admin can index object' in str(e)
