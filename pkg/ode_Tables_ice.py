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
import ode_RTypes_ice
import ode_System_ice
import ode_Collections_ice
import ode_Repositories_ice
import ode_ServerErrors_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'ServiceFactory' not in _M_ode.api.__dict__:
    _M_ode.api._t_ServiceFactory = IcePy.declareClass('::ode::api::ServiceFactory')
    _M_ode.api._t_ServiceFactoryPrx = IcePy.declareProxy('::ode::api::ServiceFactory')

# End of module ode.api

__name__ = 'ode'

# Start of module ode.grid
__name__ = 'ode.grid'

if 'Column' not in _M_ode.grid.__dict__:
    _M_ode.grid.Column = Ice.createTempClass()
    class Column(Ice.Object):
        """
        Base type for dealing working with tabular data. For efficiency,
        data is grouped by type, i.e. column. These value objects are passed
        through the {@code Table} interface.
        """
        def __init__(self, name='', description=''):
            self.name = name
            self.description = description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column')

        def ice_id(self, current=None):
            return '::ode::grid::Column'

        def ice_staticId():
            return '::ode::grid::Column'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Column)

        __repr__ = __str__

    _M_ode.grid.ColumnPrx = Ice.createTempClass()
    class ColumnPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ColumnPrx.ice_checkedCast(proxy, '::ode::grid::Column', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Column'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ColumnPrx = IcePy.defineProxy('::ode::grid::Column', ColumnPrx)

    _M_ode.grid._t_Column = IcePy.defineClass('::ode::grid::Column', Column, -1, (), False, False, None, (), (
        ('name', (), IcePy._t_string, False, 0),
        ('description', (), IcePy._t_string, False, 0)
    ))
    Column._ice_type = _M_ode.grid._t_Column

    _M_ode.grid.Column = Column
    del Column

    _M_ode.grid.ColumnPrx = ColumnPrx
    del ColumnPrx

if 'FileColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.FileColumn = Ice.createTempClass()
    class FileColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::FileColumn')

        def ice_id(self, current=None):
            return '::ode::grid::FileColumn'

        def ice_staticId():
            return '::ode::grid::FileColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_FileColumn)

        __repr__ = __str__

    _M_ode.grid.FileColumnPrx = Ice.createTempClass()
    class FileColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.FileColumnPrx.ice_checkedCast(proxy, '::ode::grid::FileColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.FileColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::FileColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_FileColumnPrx = IcePy.defineProxy('::ode::grid::FileColumn', FileColumnPrx)

    _M_ode.grid._t_FileColumn = IcePy.defineClass('::ode::grid::FileColumn', FileColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    FileColumn._ice_type = _M_ode.grid._t_FileColumn

    _M_ode.grid.FileColumn = FileColumn
    del FileColumn

    _M_ode.grid.FileColumnPrx = FileColumnPrx
    del FileColumnPrx

if 'ImageColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImageColumn = Ice.createTempClass()
    class ImageColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::ImageColumn')

        def ice_id(self, current=None):
            return '::ode::grid::ImageColumn'

        def ice_staticId():
            return '::ode::grid::ImageColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImageColumn)

        __repr__ = __str__

    _M_ode.grid.ImageColumnPrx = Ice.createTempClass()
    class ImageColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImageColumnPrx.ice_checkedCast(proxy, '::ode::grid::ImageColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImageColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImageColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImageColumnPrx = IcePy.defineProxy('::ode::grid::ImageColumn', ImageColumnPrx)

    _M_ode.grid._t_ImageColumn = IcePy.defineClass('::ode::grid::ImageColumn', ImageColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    ImageColumn._ice_type = _M_ode.grid._t_ImageColumn

    _M_ode.grid.ImageColumn = ImageColumn
    del ImageColumn

    _M_ode.grid.ImageColumnPrx = ImageColumnPrx
    del ImageColumnPrx

if 'DatasetColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.DatasetColumn = Ice.createTempClass()
    class DatasetColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::DatasetColumn')

        def ice_id(self, current=None):
            return '::ode::grid::DatasetColumn'

        def ice_staticId():
            return '::ode::grid::DatasetColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_DatasetColumn)

        __repr__ = __str__

    _M_ode.grid.DatasetColumnPrx = Ice.createTempClass()
    class DatasetColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.DatasetColumnPrx.ice_checkedCast(proxy, '::ode::grid::DatasetColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.DatasetColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::DatasetColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_DatasetColumnPrx = IcePy.defineProxy('::ode::grid::DatasetColumn', DatasetColumnPrx)

    _M_ode.grid._t_DatasetColumn = IcePy.defineClass('::ode::grid::DatasetColumn', DatasetColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    DatasetColumn._ice_type = _M_ode.grid._t_DatasetColumn

    _M_ode.grid.DatasetColumn = DatasetColumn
    del DatasetColumn

    _M_ode.grid.DatasetColumnPrx = DatasetColumnPrx
    del DatasetColumnPrx

if 'RoiColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.RoiColumn = Ice.createTempClass()
    class RoiColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::RoiColumn')

        def ice_id(self, current=None):
            return '::ode::grid::RoiColumn'

        def ice_staticId():
            return '::ode::grid::RoiColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_RoiColumn)

        __repr__ = __str__

    _M_ode.grid.RoiColumnPrx = Ice.createTempClass()
    class RoiColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.RoiColumnPrx.ice_checkedCast(proxy, '::ode::grid::RoiColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.RoiColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::RoiColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_RoiColumnPrx = IcePy.defineProxy('::ode::grid::RoiColumn', RoiColumnPrx)

    _M_ode.grid._t_RoiColumn = IcePy.defineClass('::ode::grid::RoiColumn', RoiColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    RoiColumn._ice_type = _M_ode.grid._t_RoiColumn

    _M_ode.grid.RoiColumn = RoiColumn
    del RoiColumn

    _M_ode.grid.RoiColumnPrx = RoiColumnPrx
    del RoiColumnPrx

if 'WellColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.WellColumn = Ice.createTempClass()
    class WellColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::WellColumn')

        def ice_id(self, current=None):
            return '::ode::grid::WellColumn'

        def ice_staticId():
            return '::ode::grid::WellColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_WellColumn)

        __repr__ = __str__

    _M_ode.grid.WellColumnPrx = Ice.createTempClass()
    class WellColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.WellColumnPrx.ice_checkedCast(proxy, '::ode::grid::WellColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.WellColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::WellColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_WellColumnPrx = IcePy.defineProxy('::ode::grid::WellColumn', WellColumnPrx)

    _M_ode.grid._t_WellColumn = IcePy.defineClass('::ode::grid::WellColumn', WellColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    WellColumn._ice_type = _M_ode.grid._t_WellColumn

    _M_ode.grid.WellColumn = WellColumn
    del WellColumn

    _M_ode.grid.WellColumnPrx = WellColumnPrx
    del WellColumnPrx

if 'PlateColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.PlateColumn = Ice.createTempClass()
    class PlateColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::PlateColumn')

        def ice_id(self, current=None):
            return '::ode::grid::PlateColumn'

        def ice_staticId():
            return '::ode::grid::PlateColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_PlateColumn)

        __repr__ = __str__

    _M_ode.grid.PlateColumnPrx = Ice.createTempClass()
    class PlateColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.PlateColumnPrx.ice_checkedCast(proxy, '::ode::grid::PlateColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.PlateColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::PlateColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_PlateColumnPrx = IcePy.defineProxy('::ode::grid::PlateColumn', PlateColumnPrx)

    _M_ode.grid._t_PlateColumn = IcePy.defineClass('::ode::grid::PlateColumn', PlateColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    PlateColumn._ice_type = _M_ode.grid._t_PlateColumn

    _M_ode.grid.PlateColumn = PlateColumn
    del PlateColumn

    _M_ode.grid.PlateColumnPrx = PlateColumnPrx
    del PlateColumnPrx

if 'BoolColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.BoolColumn = Ice.createTempClass()
    class BoolColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::BoolColumn', '::ode::grid::Column')

        def ice_id(self, current=None):
            return '::ode::grid::BoolColumn'

        def ice_staticId():
            return '::ode::grid::BoolColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_BoolColumn)

        __repr__ = __str__

    _M_ode.grid.BoolColumnPrx = Ice.createTempClass()
    class BoolColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.BoolColumnPrx.ice_checkedCast(proxy, '::ode::grid::BoolColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.BoolColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::BoolColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_BoolColumnPrx = IcePy.defineProxy('::ode::grid::BoolColumn', BoolColumnPrx)

    _M_ode.grid._t_BoolColumn = IcePy.defineClass('::ode::grid::BoolColumn', BoolColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_BoolArray, False, 0),))
    BoolColumn._ice_type = _M_ode.grid._t_BoolColumn

    _M_ode.grid.BoolColumn = BoolColumn
    del BoolColumn

    _M_ode.grid.BoolColumnPrx = BoolColumnPrx
    del BoolColumnPrx

if 'DoubleColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.DoubleColumn = Ice.createTempClass()
    class DoubleColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::DoubleColumn')

        def ice_id(self, current=None):
            return '::ode::grid::DoubleColumn'

        def ice_staticId():
            return '::ode::grid::DoubleColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_DoubleColumn)

        __repr__ = __str__

    _M_ode.grid.DoubleColumnPrx = Ice.createTempClass()
    class DoubleColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.DoubleColumnPrx.ice_checkedCast(proxy, '::ode::grid::DoubleColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.DoubleColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::DoubleColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_DoubleColumnPrx = IcePy.defineProxy('::ode::grid::DoubleColumn', DoubleColumnPrx)

    _M_ode.grid._t_DoubleColumn = IcePy.defineClass('::ode::grid::DoubleColumn', DoubleColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_DoubleArray, False, 0),))
    DoubleColumn._ice_type = _M_ode.grid._t_DoubleColumn

    _M_ode.grid.DoubleColumn = DoubleColumn
    del DoubleColumn

    _M_ode.grid.DoubleColumnPrx = DoubleColumnPrx
    del DoubleColumnPrx

if 'LongColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.LongColumn = Ice.createTempClass()
    class LongColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::LongColumn')

        def ice_id(self, current=None):
            return '::ode::grid::LongColumn'

        def ice_staticId():
            return '::ode::grid::LongColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_LongColumn)

        __repr__ = __str__

    _M_ode.grid.LongColumnPrx = Ice.createTempClass()
    class LongColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.LongColumnPrx.ice_checkedCast(proxy, '::ode::grid::LongColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.LongColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::LongColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_LongColumnPrx = IcePy.defineProxy('::ode::grid::LongColumn', LongColumnPrx)

    _M_ode.grid._t_LongColumn = IcePy.defineClass('::ode::grid::LongColumn', LongColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (('values', (), _M_ode.api._t_LongArray, False, 0),))
    LongColumn._ice_type = _M_ode.grid._t_LongColumn

    _M_ode.grid.LongColumn = LongColumn
    del LongColumn

    _M_ode.grid.LongColumnPrx = LongColumnPrx
    del LongColumnPrx

if 'StringColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.StringColumn = Ice.createTempClass()
    class StringColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', size=0, values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.size = size
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::StringColumn')

        def ice_id(self, current=None):
            return '::ode::grid::StringColumn'

        def ice_staticId():
            return '::ode::grid::StringColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_StringColumn)

        __repr__ = __str__

    _M_ode.grid.StringColumnPrx = Ice.createTempClass()
    class StringColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.StringColumnPrx.ice_checkedCast(proxy, '::ode::grid::StringColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.StringColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::StringColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_StringColumnPrx = IcePy.defineProxy('::ode::grid::StringColumn', StringColumnPrx)

    _M_ode.grid._t_StringColumn = IcePy.defineClass('::ode::grid::StringColumn', StringColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (
        ('size', (), IcePy._t_long, False, 0),
        ('values', (), _M_ode.api._t_StringArray, False, 0)
    ))
    StringColumn._ice_type = _M_ode.grid._t_StringColumn

    _M_ode.grid.StringColumn = StringColumn
    del StringColumn

    _M_ode.grid.StringColumnPrx = StringColumnPrx
    del StringColumnPrx

if 'FloatArrayColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.FloatArrayColumn = Ice.createTempClass()
    class FloatArrayColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', size=0, values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.size = size
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::FloatArrayColumn')

        def ice_id(self, current=None):
            return '::ode::grid::FloatArrayColumn'

        def ice_staticId():
            return '::ode::grid::FloatArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_FloatArrayColumn)

        __repr__ = __str__

    _M_ode.grid.FloatArrayColumnPrx = Ice.createTempClass()
    class FloatArrayColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.FloatArrayColumnPrx.ice_checkedCast(proxy, '::ode::grid::FloatArrayColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.FloatArrayColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::FloatArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_FloatArrayColumnPrx = IcePy.defineProxy('::ode::grid::FloatArrayColumn', FloatArrayColumnPrx)

    _M_ode.grid._t_FloatArrayColumn = IcePy.defineClass('::ode::grid::FloatArrayColumn', FloatArrayColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (
        ('size', (), IcePy._t_long, False, 0),
        ('values', (), _M_ode.api._t_FloatArrayArray, False, 0)
    ))
    FloatArrayColumn._ice_type = _M_ode.grid._t_FloatArrayColumn

    _M_ode.grid.FloatArrayColumn = FloatArrayColumn
    del FloatArrayColumn

    _M_ode.grid.FloatArrayColumnPrx = FloatArrayColumnPrx
    del FloatArrayColumnPrx

if 'DoubleArrayColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.DoubleArrayColumn = Ice.createTempClass()
    class DoubleArrayColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', size=0, values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.size = size
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::DoubleArrayColumn')

        def ice_id(self, current=None):
            return '::ode::grid::DoubleArrayColumn'

        def ice_staticId():
            return '::ode::grid::DoubleArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_DoubleArrayColumn)

        __repr__ = __str__

    _M_ode.grid.DoubleArrayColumnPrx = Ice.createTempClass()
    class DoubleArrayColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.DoubleArrayColumnPrx.ice_checkedCast(proxy, '::ode::grid::DoubleArrayColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.DoubleArrayColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::DoubleArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_DoubleArrayColumnPrx = IcePy.defineProxy('::ode::grid::DoubleArrayColumn', DoubleArrayColumnPrx)

    _M_ode.grid._t_DoubleArrayColumn = IcePy.defineClass('::ode::grid::DoubleArrayColumn', DoubleArrayColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (
        ('size', (), IcePy._t_long, False, 0),
        ('values', (), _M_ode.api._t_DoubleArrayArray, False, 0)
    ))
    DoubleArrayColumn._ice_type = _M_ode.grid._t_DoubleArrayColumn

    _M_ode.grid.DoubleArrayColumn = DoubleArrayColumn
    del DoubleArrayColumn

    _M_ode.grid.DoubleArrayColumnPrx = DoubleArrayColumnPrx
    del DoubleArrayColumnPrx

if 'LongArrayColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.LongArrayColumn = Ice.createTempClass()
    class LongArrayColumn(_M_ode.grid.Column):
        def __init__(self, name='', description='', size=0, values=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.size = size
            self.values = values

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::LongArrayColumn')

        def ice_id(self, current=None):
            return '::ode::grid::LongArrayColumn'

        def ice_staticId():
            return '::ode::grid::LongArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_LongArrayColumn)

        __repr__ = __str__

    _M_ode.grid.LongArrayColumnPrx = Ice.createTempClass()
    class LongArrayColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.LongArrayColumnPrx.ice_checkedCast(proxy, '::ode::grid::LongArrayColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.LongArrayColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::LongArrayColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_LongArrayColumnPrx = IcePy.defineProxy('::ode::grid::LongArrayColumn', LongArrayColumnPrx)

    _M_ode.grid._t_LongArrayColumn = IcePy.defineClass('::ode::grid::LongArrayColumn', LongArrayColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (
        ('size', (), IcePy._t_long, False, 0),
        ('values', (), _M_ode.api._t_LongArrayArray, False, 0)
    ))
    LongArrayColumn._ice_type = _M_ode.grid._t_LongArrayColumn

    _M_ode.grid.LongArrayColumn = LongArrayColumn
    del LongArrayColumn

    _M_ode.grid.LongArrayColumnPrx = LongArrayColumnPrx
    del LongArrayColumnPrx

if 'MaskColumn' not in _M_ode.grid.__dict__:
    _M_ode.grid.MaskColumn = Ice.createTempClass()
    class MaskColumn(_M_ode.grid.Column):
        """
        Column requiring special handling.
        """
        def __init__(self, name='', description='', imageId=None, theZ=None, theT=None, x=None, y=None, w=None, h=None, bytes=None):
            _M_ode.grid.Column.__init__(self, name, description)
            self.imageId = imageId
            self.theZ = theZ
            self.theT = theT
            self.x = x
            self.y = y
            self.w = w
            self.h = h
            self.bytes = bytes

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Column', '::ode::grid::MaskColumn')

        def ice_id(self, current=None):
            return '::ode::grid::MaskColumn'

        def ice_staticId():
            return '::ode::grid::MaskColumn'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_MaskColumn)

        __repr__ = __str__

    _M_ode.grid.MaskColumnPrx = Ice.createTempClass()
    class MaskColumnPrx(_M_ode.grid.ColumnPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.MaskColumnPrx.ice_checkedCast(proxy, '::ode::grid::MaskColumn', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.MaskColumnPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::MaskColumn'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_MaskColumnPrx = IcePy.defineProxy('::ode::grid::MaskColumn', MaskColumnPrx)

    _M_ode.grid._t_MaskColumn = IcePy.defineClass('::ode::grid::MaskColumn', MaskColumn, -1, (), False, False, _M_ode.grid._t_Column, (), (
        ('imageId', (), _M_ode.api._t_LongArray, False, 0),
        ('theZ', (), _M_ode.api._t_IntegerArray, False, 0),
        ('theT', (), _M_ode.api._t_IntegerArray, False, 0),
        ('x', (), _M_ode.api._t_DoubleArray, False, 0),
        ('y', (), _M_ode.api._t_DoubleArray, False, 0),
        ('w', (), _M_ode.api._t_DoubleArray, False, 0),
        ('h', (), _M_ode.api._t_DoubleArray, False, 0),
        ('bytes', (), _M_ode.api._t_ByteArrayArray, False, 0)
    ))
    MaskColumn._ice_type = _M_ode.grid._t_MaskColumn

    _M_ode.grid.MaskColumn = MaskColumn
    del MaskColumn

    _M_ode.grid.MaskColumnPrx = MaskColumnPrx
    del MaskColumnPrx

if '_t_ColumnArray' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_ColumnArray = IcePy.defineSequence('::ode::grid::ColumnArray', (), _M_ode.grid._t_Column)

if 'Data' not in _M_ode.grid.__dict__:
    _M_ode.grid.Data = Ice.createTempClass()
    class Data(Ice.Object):
        def __init__(self, lastModification=0, rowNumbers=None, columns=None):
            self.lastModification = lastModification
            self.rowNumbers = rowNumbers
            self.columns = columns

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Data')

        def ice_id(self, current=None):
            return '::ode::grid::Data'

        def ice_staticId():
            return '::ode::grid::Data'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Data)

        __repr__ = __str__

    _M_ode.grid.DataPrx = Ice.createTempClass()
    class DataPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.DataPrx.ice_checkedCast(proxy, '::ode::grid::Data', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.DataPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Data'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_DataPrx = IcePy.defineProxy('::ode::grid::Data', DataPrx)

    _M_ode.grid._t_Data = IcePy.declareClass('::ode::grid::Data')

    _M_ode.grid._t_Data = IcePy.defineClass('::ode::grid::Data', Data, -1, (), False, False, None, (), (
        ('lastModification', (), IcePy._t_long, False, 0),
        ('rowNumbers', (), _M_ode.api._t_LongArray, False, 0),
        ('columns', (), _M_ode.grid._t_ColumnArray, False, 0)
    ))
    Data._ice_type = _M_ode.grid._t_Data

    _M_ode.grid.Data = Data
    del Data

    _M_ode.grid.DataPrx = DataPrx
    del DataPrx

if 'Table' not in _M_ode.grid.__dict__:
    _M_ode.grid.Table = Ice.createTempClass()
    class Table(Ice.Object):
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.Table:
                raise RuntimeError('ode.grid.Table is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Table')

        def ice_id(self, current=None):
            return '::ode::grid::Table'

        def ice_staticId():
            return '::ode::grid::Table'
        ice_staticId = staticmethod(ice_staticId)

        def getOriginalFile(self, current=None):
            pass

        def getHeaders(self, current=None):
            """
            Returns empty columns.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getNumberOfRows(self, current=None):
            pass

        def getWhereList(self, condition, variables, start, stop, step, current=None):
            """
            http://www.pytables.org/docs/manual/apb.html
            Leave all three of start, stop, step to 0 to disable.
            TODO:Test effect of returning a billion rows matching getWhereList()
            Arguments:
            condition -- 
            variables -- 
            start -- 
            stop -- 
            step -- 
            current -- The Current object for the invocation.
            """
            pass

        def readCoordinates(self, rowNumbers, current=None):
            """
            Read the given rows of data.
            Arguments:
            rowNumbers -- must contain at least one element or an ode.ApiUsageException will be thrown.
            current -- The Current object for the invocation.
            """
            pass

        def read(self, colNumbers, start, stop, current=None):
            """
            http://www.pytables.org/docs/manual/ch04.html#Table.read
            Arguments:
            colNumbers -- 
            start -- 
            stop -- 
            current -- The Current object for the invocation.
            """
            pass

        def slice(self, colNumbers, rowNumbers, current=None):
            """
            Simple slice method which will return only the given columns
            and rows in the order supplied.
            If colNumbers or rowNumbers is empty (or None), then all values
            will be returned.
            Python examples:
            data = table.slice(None, None)
            assert len(data.rowNumbers) == table.getNumberOfRows()
            data = table.slice(None, \[3,2,1])
            assert data.rowNumbers == \[3,2,1]
            Arguments:
            colNumbers -- 
            rowNumbers -- 
            current -- The Current object for the invocation.
            """
            pass

        def addData(self, cols, current=None):
            pass

        def update(self, modifiedData, current=None):
            """
            Allows the user to modify a Data instance passed back
            from a query method and have the values modified. It
            is critical that the {@code Data.lastModification} and the
            {@code Data.rowNumbers} fields are properly set. An exception
            will be thrown if the data has since been modified.
            Arguments:
            modifiedData -- 
            current -- The Current object for the invocation.
            """
            pass

        def getAllMetadata(self, current=None):
            pass

        def getMetadata(self, key, current=None):
            pass

        def setAllMetadata(self, dict, current=None):
            pass

        def setMetadata(self, key, value, current=None):
            pass

        def initialize(self, cols, current=None):
            """
            Initializes the structure based on
            Arguments:
            cols -- 
            current -- The Current object for the invocation.
            """
            pass

        def addColumn(self, col, current=None):
            """
            Adds a column and returns the position index of the new column.
            Arguments:
            col -- 
            current -- The Current object for the invocation.
            """
            pass

        def delete(self, current=None):
            pass

        def close(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Table)

        __repr__ = __str__

    _M_ode.grid.TablePrx = Ice.createTempClass()
    class TablePrx(Ice.ObjectPrx):

        def getOriginalFile(self, _ctx=None):
            return _M_ode.grid.Table._op_getOriginalFile.invoke(self, ((), _ctx))

        def begin_getOriginalFile(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getOriginalFile.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getOriginalFile(self, _r):
            return _M_ode.grid.Table._op_getOriginalFile.end(self, _r)

        """
        Returns empty columns.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getHeaders(self, _ctx=None):
            return _M_ode.grid.Table._op_getHeaders.invoke(self, ((), _ctx))

        """
        Returns empty columns.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getHeaders(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getHeaders.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns empty columns.
        Arguments:
        """
        def end_getHeaders(self, _r):
            return _M_ode.grid.Table._op_getHeaders.end(self, _r)

        def getNumberOfRows(self, _ctx=None):
            return _M_ode.grid.Table._op_getNumberOfRows.invoke(self, ((), _ctx))

        def begin_getNumberOfRows(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getNumberOfRows.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getNumberOfRows(self, _r):
            return _M_ode.grid.Table._op_getNumberOfRows.end(self, _r)

        """
        http://www.pytables.org/docs/manual/apb.html
        Leave all three of start, stop, step to 0 to disable.
        TODO:Test effect of returning a billion rows matching getWhereList()
        Arguments:
        condition -- 
        variables -- 
        start -- 
        stop -- 
        step -- 
        _ctx -- The request context for the invocation.
        """
        def getWhereList(self, condition, variables, start, stop, step, _ctx=None):
            return _M_ode.grid.Table._op_getWhereList.invoke(self, ((condition, variables, start, stop, step), _ctx))

        """
        http://www.pytables.org/docs/manual/apb.html
        Leave all three of start, stop, step to 0 to disable.
        TODO:Test effect of returning a billion rows matching getWhereList()
        Arguments:
        condition -- 
        variables -- 
        start -- 
        stop -- 
        step -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getWhereList(self, condition, variables, start, stop, step, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getWhereList.begin(self, ((condition, variables, start, stop, step), _response, _ex, _sent, _ctx))

        """
        http://www.pytables.org/docs/manual/apb.html
        Leave all three of start, stop, step to 0 to disable.
        TODO:Test effect of returning a billion rows matching getWhereList()
        Arguments:
        condition -- 
        variables -- 
        start -- 
        stop -- 
        step -- 
        """
        def end_getWhereList(self, _r):
            return _M_ode.grid.Table._op_getWhereList.end(self, _r)

        """
        Read the given rows of data.
        Arguments:
        rowNumbers -- must contain at least one element or an ode.ApiUsageException will be thrown.
        _ctx -- The request context for the invocation.
        """
        def readCoordinates(self, rowNumbers, _ctx=None):
            return _M_ode.grid.Table._op_readCoordinates.invoke(self, ((rowNumbers, ), _ctx))

        """
        Read the given rows of data.
        Arguments:
        rowNumbers -- must contain at least one element or an ode.ApiUsageException will be thrown.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_readCoordinates(self, rowNumbers, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_readCoordinates.begin(self, ((rowNumbers, ), _response, _ex, _sent, _ctx))

        """
        Read the given rows of data.
        Arguments:
        rowNumbers -- must contain at least one element or an ode.ApiUsageException will be thrown.
        """
        def end_readCoordinates(self, _r):
            return _M_ode.grid.Table._op_readCoordinates.end(self, _r)

        """
        http://www.pytables.org/docs/manual/ch04.html#Table.read
        Arguments:
        colNumbers -- 
        start -- 
        stop -- 
        _ctx -- The request context for the invocation.
        """
        def read(self, colNumbers, start, stop, _ctx=None):
            return _M_ode.grid.Table._op_read.invoke(self, ((colNumbers, start, stop), _ctx))

        """
        http://www.pytables.org/docs/manual/ch04.html#Table.read
        Arguments:
        colNumbers -- 
        start -- 
        stop -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_read(self, colNumbers, start, stop, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_read.begin(self, ((colNumbers, start, stop), _response, _ex, _sent, _ctx))

        """
        http://www.pytables.org/docs/manual/ch04.html#Table.read
        Arguments:
        colNumbers -- 
        start -- 
        stop -- 
        """
        def end_read(self, _r):
            return _M_ode.grid.Table._op_read.end(self, _r)

        """
        Simple slice method which will return only the given columns
        and rows in the order supplied.
        If colNumbers or rowNumbers is empty (or None), then all values
        will be returned.
        Python examples:
        data = table.slice(None, None)
        assert len(data.rowNumbers) == table.getNumberOfRows()
        data = table.slice(None, \[3,2,1])
        assert data.rowNumbers == \[3,2,1]
        Arguments:
        colNumbers -- 
        rowNumbers -- 
        _ctx -- The request context for the invocation.
        """
        def slice(self, colNumbers, rowNumbers, _ctx=None):
            return _M_ode.grid.Table._op_slice.invoke(self, ((colNumbers, rowNumbers), _ctx))

        """
        Simple slice method which will return only the given columns
        and rows in the order supplied.
        If colNumbers or rowNumbers is empty (or None), then all values
        will be returned.
        Python examples:
        data = table.slice(None, None)
        assert len(data.rowNumbers) == table.getNumberOfRows()
        data = table.slice(None, \[3,2,1])
        assert data.rowNumbers == \[3,2,1]
        Arguments:
        colNumbers -- 
        rowNumbers -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_slice(self, colNumbers, rowNumbers, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_slice.begin(self, ((colNumbers, rowNumbers), _response, _ex, _sent, _ctx))

        """
        Simple slice method which will return only the given columns
        and rows in the order supplied.
        If colNumbers or rowNumbers is empty (or None), then all values
        will be returned.
        Python examples:
        data = table.slice(None, None)
        assert len(data.rowNumbers) == table.getNumberOfRows()
        data = table.slice(None, \[3,2,1])
        assert data.rowNumbers == \[3,2,1]
        Arguments:
        colNumbers -- 
        rowNumbers -- 
        """
        def end_slice(self, _r):
            return _M_ode.grid.Table._op_slice.end(self, _r)

        def addData(self, cols, _ctx=None):
            return _M_ode.grid.Table._op_addData.invoke(self, ((cols, ), _ctx))

        def begin_addData(self, cols, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_addData.begin(self, ((cols, ), _response, _ex, _sent, _ctx))

        def end_addData(self, _r):
            return _M_ode.grid.Table._op_addData.end(self, _r)

        """
        Allows the user to modify a Data instance passed back
        from a query method and have the values modified. It
        is critical that the {@code Data.lastModification} and the
        {@code Data.rowNumbers} fields are properly set. An exception
        will be thrown if the data has since been modified.
        Arguments:
        modifiedData -- 
        _ctx -- The request context for the invocation.
        """
        def update(self, modifiedData, _ctx=None):
            return _M_ode.grid.Table._op_update.invoke(self, ((modifiedData, ), _ctx))

        """
        Allows the user to modify a Data instance passed back
        from a query method and have the values modified. It
        is critical that the {@code Data.lastModification} and the
        {@code Data.rowNumbers} fields are properly set. An exception
        will be thrown if the data has since been modified.
        Arguments:
        modifiedData -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_update(self, modifiedData, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_update.begin(self, ((modifiedData, ), _response, _ex, _sent, _ctx))

        """
        Allows the user to modify a Data instance passed back
        from a query method and have the values modified. It
        is critical that the {@code Data.lastModification} and the
        {@code Data.rowNumbers} fields are properly set. An exception
        will be thrown if the data has since been modified.
        Arguments:
        modifiedData -- 
        """
        def end_update(self, _r):
            return _M_ode.grid.Table._op_update.end(self, _r)

        def getAllMetadata(self, _ctx=None):
            return _M_ode.grid.Table._op_getAllMetadata.invoke(self, ((), _ctx))

        def begin_getAllMetadata(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getAllMetadata.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAllMetadata(self, _r):
            return _M_ode.grid.Table._op_getAllMetadata.end(self, _r)

        def getMetadata(self, key, _ctx=None):
            return _M_ode.grid.Table._op_getMetadata.invoke(self, ((key, ), _ctx))

        def begin_getMetadata(self, key, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_getMetadata.begin(self, ((key, ), _response, _ex, _sent, _ctx))

        def end_getMetadata(self, _r):
            return _M_ode.grid.Table._op_getMetadata.end(self, _r)

        def setAllMetadata(self, dict, _ctx=None):
            return _M_ode.grid.Table._op_setAllMetadata.invoke(self, ((dict, ), _ctx))

        def begin_setAllMetadata(self, dict, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_setAllMetadata.begin(self, ((dict, ), _response, _ex, _sent, _ctx))

        def end_setAllMetadata(self, _r):
            return _M_ode.grid.Table._op_setAllMetadata.end(self, _r)

        def setMetadata(self, key, value, _ctx=None):
            return _M_ode.grid.Table._op_setMetadata.invoke(self, ((key, value), _ctx))

        def begin_setMetadata(self, key, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_setMetadata.begin(self, ((key, value), _response, _ex, _sent, _ctx))

        def end_setMetadata(self, _r):
            return _M_ode.grid.Table._op_setMetadata.end(self, _r)

        """
        Initializes the structure based on
        Arguments:
        cols -- 
        _ctx -- The request context for the invocation.
        """
        def initialize(self, cols, _ctx=None):
            return _M_ode.grid.Table._op_initialize.invoke(self, ((cols, ), _ctx))

        """
        Initializes the structure based on
        Arguments:
        cols -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_initialize(self, cols, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_initialize.begin(self, ((cols, ), _response, _ex, _sent, _ctx))

        """
        Initializes the structure based on
        Arguments:
        cols -- 
        """
        def end_initialize(self, _r):
            return _M_ode.grid.Table._op_initialize.end(self, _r)

        """
        Adds a column and returns the position index of the new column.
        Arguments:
        col -- 
        _ctx -- The request context for the invocation.
        """
        def addColumn(self, col, _ctx=None):
            return _M_ode.grid.Table._op_addColumn.invoke(self, ((col, ), _ctx))

        """
        Adds a column and returns the position index of the new column.
        Arguments:
        col -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addColumn(self, col, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_addColumn.begin(self, ((col, ), _response, _ex, _sent, _ctx))

        """
        Adds a column and returns the position index of the new column.
        Arguments:
        col -- 
        """
        def end_addColumn(self, _r):
            return _M_ode.grid.Table._op_addColumn.end(self, _r)

        def delete(self, _ctx=None):
            return _M_ode.grid.Table._op_delete.invoke(self, ((), _ctx))

        def begin_delete(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_delete.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_delete(self, _r):
            return _M_ode.grid.Table._op_delete.end(self, _r)

        def close(self, _ctx=None):
            return _M_ode.grid.Table._op_close.invoke(self, ((), _ctx))

        def begin_close(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Table._op_close.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_close(self, _r):
            return _M_ode.grid.Table._op_close.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.TablePrx.ice_checkedCast(proxy, '::ode::grid::Table', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.TablePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Table'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_TablePrx = IcePy.defineProxy('::ode::grid::Table', TablePrx)

    _M_ode.grid._t_Table = IcePy.defineClass('::ode::grid::Table', Table, -1, (), True, False, None, (), ())
    Table._ice_type = _M_ode.grid._t_Table

    Table._op_getOriginalFile = IcePy.Operation('getOriginalFile', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))
    Table._op_getHeaders = IcePy.Operation('getHeaders', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.grid._t_ColumnArray, False, 0), (_M_ode._t_ServerError,))
    Table._op_getNumberOfRows = IcePy.Operation('getNumberOfRows', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    Table._op_getWhereList = IcePy.Operation('getWhereList', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RTypeDict, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.api._t_LongArray, False, 0), (_M_ode._t_ServerError,))
    Table._op_readCoordinates = IcePy.Operation('readCoordinates', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.api._t_LongArray, False, 0),), (), ((), _M_ode.grid._t_Data, False, 0), (_M_ode._t_ServerError,))
    Table._op_read = IcePy.Operation('read', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.api._t_LongArray, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.grid._t_Data, False, 0), (_M_ode._t_ServerError,))
    Table._op_slice = IcePy.Operation('slice', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.api._t_LongArray, False, 0), ((), _M_ode.api._t_LongArray, False, 0)), (), ((), _M_ode.grid._t_Data, False, 0), (_M_ode._t_ServerError,))
    Table._op_addData = IcePy.Operation('addData', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ColumnArray, False, 0),), (), None, (_M_ode._t_ServerError,))
    Table._op_update = IcePy.Operation('update', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_Data, False, 0),), (), None, (_M_ode._t_ServerError,))
    Table._op_getAllMetadata = IcePy.Operation('getAllMetadata', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode._t_RTypeDict, False, 0), (_M_ode._t_ServerError,))
    Table._op_getMetadata = IcePy.Operation('getMetadata', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode._t_RType, False, 0), (_M_ode._t_ServerError,))
    Table._op_setAllMetadata = IcePy.Operation('setAllMetadata', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode._t_RTypeDict, False, 0),), (), None, (_M_ode._t_ServerError,))
    Table._op_setMetadata = IcePy.Operation('setMetadata', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RType, False, 0)), (), None, (_M_ode._t_ServerError,))
    Table._op_initialize = IcePy.Operation('initialize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ColumnArray, False, 0),), (), None, (_M_ode._t_ServerError,))
    Table._op_addColumn = IcePy.Operation('addColumn', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_Column, False, 0),), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    Table._op_delete = IcePy.Operation('delete', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, (_M_ode._t_ServerError,))
    Table._op_close = IcePy.Operation('close', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, (_M_ode._t_ServerError,))

    _M_ode.grid.Table = Table
    del Table

    _M_ode.grid.TablePrx = TablePrx
    del TablePrx

if 'Tables' not in _M_ode.grid.__dict__:
    _M_ode.grid.Tables = Ice.createTempClass()
    class Tables(Ice.Object):
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.Tables:
                raise RuntimeError('ode.grid.Tables is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Tables')

        def ice_id(self, current=None):
            return '::ode::grid::Tables'

        def ice_staticId():
            return '::ode::grid::Tables'
        ice_staticId = staticmethod(ice_staticId)

        def getRepository(self, current=None):
            """
            Returns the Repository which this Tables service is watching.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getTable(self, file, sf, current=None):
            """
            Returns the Table service for the given ""ODE.tables"" file.
            This service will open the file locally to access the data.
            After any modification, the file will be saved locally and
            the server asked to update the database record. This is done
            via services in the ode.api.ServiceFactory.
            Arguments:
            file -- 
            sf -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Tables)

        __repr__ = __str__

    _M_ode.grid.TablesPrx = Ice.createTempClass()
    class TablesPrx(Ice.ObjectPrx):

        """
        Returns the Repository which this Tables service is watching.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getRepository(self, _ctx=None):
            return _M_ode.grid.Tables._op_getRepository.invoke(self, ((), _ctx))

        """
        Returns the Repository which this Tables service is watching.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRepository(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Tables._op_getRepository.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the Repository which this Tables service is watching.
        Arguments:
        """
        def end_getRepository(self, _r):
            return _M_ode.grid.Tables._op_getRepository.end(self, _r)

        """
        Returns the Table service for the given ""ODE.tables"" file.
        This service will open the file locally to access the data.
        After any modification, the file will be saved locally and
        the server asked to update the database record. This is done
        via services in the ode.api.ServiceFactory.
        Arguments:
        file -- 
        sf -- 
        _ctx -- The request context for the invocation.
        """
        def getTable(self, file, sf, _ctx=None):
            return _M_ode.grid.Tables._op_getTable.invoke(self, ((file, sf), _ctx))

        """
        Returns the Table service for the given ""ODE.tables"" file.
        This service will open the file locally to access the data.
        After any modification, the file will be saved locally and
        the server asked to update the database record. This is done
        via services in the ode.api.ServiceFactory.
        Arguments:
        file -- 
        sf -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTable(self, file, sf, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Tables._op_getTable.begin(self, ((file, sf), _response, _ex, _sent, _ctx))

        """
        Returns the Table service for the given ""ODE.tables"" file.
        This service will open the file locally to access the data.
        After any modification, the file will be saved locally and
        the server asked to update the database record. This is done
        via services in the ode.api.ServiceFactory.
        Arguments:
        file -- 
        sf -- 
        """
        def end_getTable(self, _r):
            return _M_ode.grid.Tables._op_getTable.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.TablesPrx.ice_checkedCast(proxy, '::ode::grid::Tables', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.TablesPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Tables'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_TablesPrx = IcePy.defineProxy('::ode::grid::Tables', TablesPrx)

    _M_ode.grid._t_Tables = IcePy.defineClass('::ode::grid::Tables', Tables, -1, (), True, False, None, (), ())
    Tables._ice_type = _M_ode.grid._t_Tables

    Tables._op_getRepository = IcePy.Operation('getRepository', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.grid._t_RepositoryPrx, False, 0), (_M_ode._t_ServerError,))
    Tables._op_getTable = IcePy.Operation('getTable', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0), ((), _M_ode.api._t_ServiceFactoryPrx, False, 0)), (), ((), _M_ode.grid._t_TablePrx, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.Tables = Tables
    del Tables

    _M_ode.grid.TablesPrx = TablesPrx
    del TablesPrx

# End of module ode.grid

__name__ = 'ode'

# End of module ode
