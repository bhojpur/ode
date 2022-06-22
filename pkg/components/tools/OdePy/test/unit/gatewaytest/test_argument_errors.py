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
   gateway tests - argument errors in gateway methods
"""

from ode.gateway import _ServerGateway
import pytest

class TestArgumentErrors(object):

    @classmethod
    @pytest.fixture(autouse=True)
    def setup_class(cls, tmpdir, monkeypatch):
        ice_config = tmpdir / "ice.config"
        ice_config.write("ode.host=localhost\node.port=4064")
        monkeypatch.setenv("ICE_CONFIG", ice_config)
        cls.g = _ServerGateway()

    def test_graphspec_with_plus(self):
        """
        The graph_spec Name+Qualifier is no longer supported.
        """
        with pytest.raises(AttributeError):
            self.g.deleteObjects("Image+Only", ["1"])
        with pytest.raises(AttributeError):
            self.g.chgrpObjects("Image+Only", ["1"], 1)

    @pytest.mark.parametrize("object_ids", ["1", [], None])
    def test_bad_object_ids(self, object_ids):
        """
        object_ids must be a non-zero length list
        """
        with pytest.raises(AttributeError):
            self.g.deleteObjects("Image", object_ids)
        with pytest.raises(AttributeError):
            self.g.chgrpObjects("Image", object_ids, 1)
