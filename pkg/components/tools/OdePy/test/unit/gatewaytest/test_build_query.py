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

"""Gateway tests - buildQuery() as used by conn.getObjects()."""

from ode.gateway import _ServerGateway, ServerObjectWrapper, KNOWN_WRAPPERS
from ode.sys import Parameters, ParametersI, Filter
import pytest
from ode.rtypes import wrap

@pytest.fixture(scope='function')
def gateway():
    """Create a ServerGateway object."""
    return _ServerGateway()

class TestBuildQuery(object):
    """Test the conn.buildQuery() method for all Object Wrappers."""

    @pytest.mark.parametrize("dtype", KNOWN_WRAPPERS.keys())
    def test_no_clauses(self, gateway, dtype):
        """Expect a query with no 'where' clauses."""
        result = gateway.buildQuery(dtype)
        query, params, wrapper = result
        assert isinstance(query, str)
        assert isinstance(params, Parameters)
        assert isinstance(wrapper(), ServerObjectWrapper)
        assert query.startswith("select ")
        assert "where" not in query
        assert 'None' not in query

    @pytest.mark.parametrize("dtype", KNOWN_WRAPPERS.keys())
    def test_filter_by_owner(self, gateway, dtype):
        """Query should filter by owner."""
        p = ParametersI()
        p.theFilter = Filter()
        p.theFilter.ownerId = wrap(2)
        # Test using 'params' argument
        with_params = gateway.buildQuery(dtype, params=p)
        # Test using 'opts' dictionary
        with_opts = gateway.buildQuery(dtype, opts={'owner': 1})
        for result in [with_params, with_opts]:
            query, params, wrapper = result
            assert isinstance(query, str)
            assert isinstance(params, Parameters)
            assert isinstance(wrapper(), ServerObjectWrapper)
            if dtype not in ('experimenter', 'experimentergroup'):
                assert "where owner" in query
            else:
                assert "where owner" not in query

    @pytest.mark.parametrize("dtype", KNOWN_WRAPPERS.keys())
    def test_pagination(self, gateway, dtype):
        """Query should paginate."""
        offset = 1
        limit = 100
        p = ParametersI()
        p.page(offset, limit)
        # Test using 'params' argument
        with_params = gateway.buildQuery(dtype, params=p)
        # Test using 'opts' dictionary
        opts = {'offset': offset, 'limit': limit}
        with_opts = gateway.buildQuery(dtype, opts=opts)
        for result in [with_params, with_opts]:
            query, params, wrapper = result
            assert isinstance(query, str)
            assert isinstance(params, Parameters)
            assert isinstance(wrapper(), ServerObjectWrapper)
            assert params.theFilter.offset.val == offset
            assert params.theFilter.limit.val == limit
