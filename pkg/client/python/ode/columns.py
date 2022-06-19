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
::
    /*
     *   $Id$
     */
"""

"""
Concrete implementations of the ode.grid.Column
type hierarchy which know how to convert themselves
to PyTables types.
"""

from builtins import zip
from builtins import range
from builtins import object
from future.utils import native, bytes_to_native_str, isbytes
import ode
import Ice
import IceImport

IceImport.load("ode_Tables_ice")
python_sys = __import__("sys")  # Python sys

try:
    import numpy
    tables = __import__("tables")  # Pytables
    has_pytables = True
    if hasattr(tables, "open_file"):
        has_pytables3 = True
    else:
        has_pytables3 = False
except ImportError:
    has_pytables = False

def columns2definition(cols):
    """
    Takes a list of columns and converts them into a map
    from names to tables.* column descriptors
    """
    definition = {}
    for i in range(len(cols)):
        column = cols[i]
        instance = column.descriptor(pos=i)
        if column.name in definition:
            raise ode.ApiUsageException(
                None, None, "Duplicate column name: %s" % column.name)
        definition[column.name] = instance
        # Descriptions are handled separately
    return definition

class AbstractColumn(object):
    """
    Base logic for all columns
    """

    def __init__(self):
        # Note: don't rely on any properties such as self.name being set if
        # this has been called through Ice
        d = self.descriptor(None)
        if isinstance(d, tables.IsDescription):
            cols = d.columns
            try:
                del cols["_v_pos"]
            except KeyError:
                pass
            self._types = [None] * len(cols)
            self._subnames = [None] * len(cols)
            for k, v in list(cols.items()):
                self._types[v._v_pos] = v.recarrtype
                self._subnames[v._v_pos] = "/" + k

        else:
            self._types = [d.recarrtype]
            self._subnames = [""]

    def settable(self, tbl):
        """
        Called by tables.py when first initializing columns.
        Can be used to complete further initialization.
        """
        self.__table = tbl

    def append(self, tbl):
        """
        Called by tables.py to give columns. By default, does nothing.
        """
        pass

    def readCoordinates(self, tbl, rowNumbers):
        if rowNumbers is None or len(rowNumbers) == 0:
            rows = tbl.read()
        else:
            if has_pytables3:
                rows = tbl.read_coordinates(rowNumbers, field=self.name)
            else:
                rows = tbl.readCoordinates(rowNumbers, field=self.name)
        self.fromrows(rows, field_only=True)

    def read(self, tbl, start, stop):
        rows = tbl.read(start, stop, field=self.name)
        self.fromrows(rows, field_only=True)

    def getsize(self):
        """
        Any method which does not use the "values" field
        will need to override this method.
        """
        if self.values is None:
            return None
        else:
            return len(self.values)

    def setsize(self, size):
        """
        Any method which does not use the "values" field
        will need to override this method.
        """
        if size is None:
            self.values = None
        else:
            self.values = [None for x in range(size)]

    def arrays(self):
        """
        Any method which does not use the "values" field
        will need to override this method.
        """
        return [self.values]

    def dtypes(self):
        """
        Override this method if descriptor() doesn't return the correct data
        type/size at initialisation- this is mostly a problem for array types
        """
        names = [self.name + sn for sn in self._subnames]
        return list(zip(names, self._types))

    def fromrows(self, rows, field_only=False):
        """
        Any method which does not use the "values" field
        will need to override this method.
        """

        if not field_only:
            rows = rows[self.name]

        self.values = rows

        # WORKAROUND:
        # d = self.recarrtypes[0][1]
        # Disabled until Ice 3.4
        # if isinstance(d, str):
        #     d = numpy.dtype(d)
        # if d.kind == "S" or (d.kind == "i" and d.itemsize == "8"):
        self.values = self.values.tolist()

class FileColumnI(AbstractColumn, ode.grid.FileColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.FileColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class ImageColumnI(AbstractColumn, ode.grid.ImageColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.ImageColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class WellColumnI(AbstractColumn, ode.grid.WellColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.WellColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class PlateColumnI(AbstractColumn, ode.grid.PlateColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.PlateColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class DatasetColumnI(AbstractColumn, ode.grid.DatasetColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.DatasetColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class RoiColumnI(AbstractColumn, ode.grid.RoiColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.RoiColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class BoolColumnI(AbstractColumn, ode.grid.BoolColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.BoolColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.BoolCol(pos=pos)

class DoubleColumnI(AbstractColumn, ode.grid.DoubleColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.DoubleColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Float64Col(pos=pos)

class LongColumnI(AbstractColumn, ode.grid.LongColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.LongColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def descriptor(self, pos):
        return tables.Int64Col(pos=pos)

class StringColumnI(AbstractColumn, ode.grid.StringColumn):
    """
    StringColumns are actually numpy dtype 'S':
      "zero-terminated bytes (not recommended)"
    https://docs.scipy.org/doc/numpy-1.15.1/reference/arrays.dtypes.html#specifying-and-constructing-data-types

    In any case HDF5 doesn't seem to properly support unicode,
    and numexpr doesn't even pretend to support it:
      https://github.com/PyTables/PyTables/issues/499
      https://github.com/pydata/numexpr/issues/142
      https://github.com/pydata/numexpr/issues/150
      https://github.com/pydata/numexpr/issues/263
      https://github.com/pydata/numexpr/blob/v2.7.0/numexpr/necompiler.py#L340-L341

    > import numexpr
    > a = "£"
    > numexpr.evaluate('a=="£"')
    ValueError: unknown type str32
    > b = "£".encode()
    > numexpr.evaluate('b=="£"')
    UnicodeEncodeError: 'ascii' codec can't encode character '\xa3'
        in position 0: ordinal not in range(128)

    You should be able to store/load unicode data but you can't use
    unicode in a where condition
    """

    def __init__(self, name="Unknown", *args):
        ode.grid.StringColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def settable(self, tbl):
        AbstractColumn.settable(self, tbl)
        self.size = getattr(tbl.cols, self.name).dtype.itemsize

    def arrays(self):
        """
        Check for strings longer than the initialised column width
        This will always return bytes
        """
        if python_sys.version_info >= (3, 0, 0):
            bytevalues = [v.encode() for v in self.values]
        else:
            bytevalues = self.values
        for bv in bytevalues:
            if len(bv) > self.size:
                raise ode.ValidationException(
                    None, None,
                    "Maximum string (byte) length in column %s is %d" %
                    (self.name, self.size))
        return [bytevalues]

    def dtypes(self):
        """
        Overriding to correct for size.
        (Testing suggests this may not be necessary, the size appears to be
        correctly set at initialisation)
        """
        return [(self.name, "S", self.size)]

    def descriptor(self, pos):
        # During initialization, size might be zero
        # to prevent exceptions we temporarily assume size 1
        if pos is None:
            return tables.StringCol(pos=pos, itemsize=1)
        if self.size < 1:
            raise ode.ApiUsageException(
                None, None, "String size must be > 0 (Column: %s)"
                % self.name)
        return tables.StringCol(pos=pos, itemsize=self.size)

    def fromrows(self, rows, field_only=False):
        AbstractColumn.fromrows(self, rows, field_only=field_only)
        for i, val in enumerate(self.values):
            if isbytes(val):
                self.values[i] = bytes_to_native_str(val)

class AbstractArrayColumn(AbstractColumn):
    """
    Additional base logic for array columns
    """

    def __init__(self):
        AbstractColumn.__init__(self)

    def settable(self, tbl):
        AbstractColumn.settable(self, tbl)

        # Pytables 2.1 has the array size in Column.dtype.shape
        # shape = getattr(tbl.cols, self.name).dtype.shape
        # self.size = shape[0]

        # Pytables 2.2 and later replaced this with Column.shape
        # shape = getattr(tbl.cols, self.name).shape
        # assert(len(shape) == 2)
        # self.size = shape[1]

        # TODO: Clean this up

        # Taken from http://www.pytables.org/trac-bck/changeset/4176
        column = getattr(tbl.cols, self.name)
        self.size = column.descr._v_dtypes[column.name].shape[0]

    def arrays(self):
        """
        Arrays of size 1 have to be converted to scalars, otherwise the
        column-to-row conversion in HdfStorage.append() will fail.
        This is messy, but I can't think of a better way.
        """
        for v in self.values:
            if len(v) != self.size:
                raise ode.ValidationException(
                    None, None, "Column %s requires arrays of length %d" %
                    (self.name, self.size))

        if self.size == 1:
            return [[v[0] for v in self.values]]
        return [self.values]

    def dtypes(self):
        """
        Overriding to correct for size.
        """
        return [(self.name, self._types[0], self.size)]

class FloatArrayColumnI(AbstractArrayColumn, ode.grid.FloatArrayColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.FloatArrayColumn.__init__(self, name, *args)
        AbstractArrayColumn.__init__(self)

    def descriptor(self, pos):
        # During initialization, size might be zero
        if pos is None:
            return tables.Float32Col(pos=pos)
        if self.size < 1:
            raise ode.ApiUsageException(
                None, None, "Array length must be > 0 (Column: %s)"
                % self.name)
        return tables.Float32Col(pos=pos, shape=self.size)

class DoubleArrayColumnI(AbstractArrayColumn, ode.grid.DoubleArrayColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.DoubleArrayColumn.__init__(self, name, *args)
        AbstractArrayColumn.__init__(self)

    def descriptor(self, pos):
        # During initialization, size might be zero
        if pos is None:
            return tables.Float64Col(pos=pos)
        if self.size < 1:
            raise ode.ApiUsageException(
                None, None, "Array length must be > 0 (Column: %s)"
                % self.name)
        return tables.Float64Col(pos=pos, shape=self.size)

class LongArrayColumnI(AbstractArrayColumn, ode.grid.LongArrayColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.LongArrayColumn.__init__(self, name, *args)
        AbstractArrayColumn.__init__(self)

    def descriptor(self, pos):
        # During initialization, size might be zero
        if pos is None:
            return tables.Int64Col(pos=pos)
        if self.size < 1:
            raise ode.ApiUsageException(
                None, None, "Array length must be > 0 (Column: %s)"
                % self.name)
        return tables.Int64Col(pos=pos, shape=self.size)

class MaskColumnI(AbstractColumn, ode.grid.MaskColumn):

    def __init__(self, name="Unknown", *args):
        ode.grid.MaskColumn.__init__(self, name, *args)
        AbstractColumn.__init__(self)

    def __noneorsame(self, a, b):
        if a is None:
            if b is None:
                return
        # a not none
        if b is not None:
            if len(a) == len(b):
                return
        raise ode.ValidationException(None, None, "Columns don't match")

    def __sanitycheck(self):
        self.__noneorsame(self.imageId, self.theZ)
        self.__noneorsame(self.imageId, self.theT)
        self.__noneorsame(self.imageId, self.x)
        self.__noneorsame(self.imageId, self.y)
        self.__noneorsame(self.imageId, self.w)
        self.__noneorsame(self.imageId, self.h)
        self.__noneorsame(self.imageId, self.bytes)

    def descriptor(self, pos):
        class MaskDescription(tables.IsDescription):
            _v_pos = pos
            i = tables.Int64Col(pos=0)
            z = tables.Int32Col(pos=1)
            t = tables.Int32Col(pos=2)
            x = tables.Float64Col(pos=3)
            y = tables.Float64Col(pos=4)
            w = tables.Float64Col(pos=5)
            h = tables.Float64Col(pos=6)
        return MaskDescription()

    def arrays(self):
        self.__sanitycheck()
        a = [
            self.imageId,
            self.theZ,
            self.theT,
            self.x,
            self.y,
            self.w,
            self.h,
            ]
        return a

    def getsize(self):
        self.__sanitycheck()
        if self.imageId is None:
            return None
        else:
            return len(self.imageId)

    def setsize(self, size):
        if size is None:
            self.imageId = None
            self.theZ = None
            self.theT = None
            self.x = None
            self.y = None
            self.w = None
            self.h = None
        else:
            dts = self.dtypes()
            self.imageId = numpy.zeroes(size, dtype=dts[0])
            self.theZ = numpy.zeroes(size, dtype=dts[1])
            self.theT = numpy.zeroes(size, dtype=dts[2])
            self.x = numpy.zeroes(size, dtype=dts[3])
            self.y = numpy.zeroes(size, dtype=dts[4])
            self.w = numpy.zeroes(size, dtype=dts[5])
            self.h = numpy.zeroes(size, dtype=dts[6])

    def readCoordinates(self, tbl, rowNumbers):
        self.__sanitycheck()
        # calls fromrows
        AbstractColumn.readCoordinates(self, tbl, rowNumbers)
        masks = self._getmasks(tbl)
        if rowNumbers is None or len(rowNumbers) == 0:
            rowNumbers = list(range(masks.nrows))
        self.getbytes(masks, rowNumbers)

    def read(self, tbl, start, stop):
        self.__sanitycheck()
        # calls fromrows
        AbstractColumn.read(self, tbl, start, stop)
        masks = self._getmasks(tbl)
        rowNumbers = list(range(start, stop))
        self.getbytes(masks, rowNumbers)

    def getbytes(self, masks, rowNumbers):
        self.bytes = []
        for idx in rowNumbers:
            self.bytes.append(masks[idx].tolist())

    def fromrows(self, rows, field_only=False):

        if not field_only:
            rows = rows[self.name]

        # WORKAROUND:
        self.imageId = rows["i"].tolist()
        self.theZ = rows["z"].tolist()  #
        self.theT = rows["t"].tolist()  #
        self.x = rows["x"]
        self.y = rows["y"]
        self.w = rows["w"]
        self.h = rows["h"]

    def append(self, tbl):
        self.__sanitycheck()
        masks = self._getmasks(tbl)
        for x in self.bytes:
            if isinstance(x, list):
                # This occurs primarily in testing.
                masks.append(numpy.array(x, dtype=tables.UInt8Atom()))
            else:
                masks.append(numpy.fromstring(x, count=len(x),
                                              dtype=tables.UInt8Atom()))

    def _getmasks(self, tbl):
        n = tbl._v_name
        f = tbl._v_file
        p = tbl._v_parent
        # http://doc.zeroc.com/display/Ice/Basic+Types
        # Ice::Byte can be -128 to 127 OR 0 to 255, but using UInt8 for the
        # moment
        try:
            masks = getattr(p, "%s_masks" % n)
        except tables.NoSuchNodeError:
            if has_pytables3:
                masks = f.create_vlarray(p, "%s_masks" % n, tables.UInt8Atom())
            else:
                masks = f.createVLArray(p, "%s_masks" % n, tables.UInt8Atom())
        return masks

# Helpers
# ========================================================================

# Conversion classes are for ode.model <--> ode.model only (no python)
class ObjectFactory(Ice.ObjectFactory):

    def __init__(self, cls, f):
        self.id = cls.ice_staticId()
        self.f = f

    def create(self, string):
        return self.f()

    def destroy(self):
        pass

    def register(self, ic):
        ic.addObjectFactory(self, self.id)

# Object factories
# =========================================================================

ObjectFactories = {
    FileColumnI: ObjectFactory(FileColumnI, lambda: FileColumnI()),
    ImageColumnI: ObjectFactory(ImageColumnI, lambda: ImageColumnI()),
    RoiColumnI: ObjectFactory(RoiColumnI, lambda: RoiColumnI()),
    WellColumnI: ObjectFactory(WellColumnI, lambda: WellColumnI()),
    PlateColumnI: ObjectFactory(PlateColumnI, lambda: PlateColumnI()),
    DatasetColumnI: ObjectFactory(DatasetColumnI, lambda: DatasetColumnI()),
    BoolColumnI: ObjectFactory(BoolColumnI, lambda: BoolColumnI()),
    DoubleColumnI: ObjectFactory(DoubleColumnI, lambda: DoubleColumnI()),
    LongColumnI: ObjectFactory(LongColumnI, lambda: LongColumnI()),
    StringColumnI: ObjectFactory(StringColumnI, lambda: StringColumnI()),
    FloatArrayColumnI: ObjectFactory(
        FloatArrayColumnI, lambda: FloatArrayColumnI()),
    DoubleArrayColumnI: ObjectFactory(
        DoubleArrayColumnI, lambda: DoubleArrayColumnI()),
    LongArrayColumnI: ObjectFactory(
        LongArrayColumnI, lambda: LongArrayColumnI()),
    MaskColumnI: ObjectFactory(MaskColumnI, lambda: MaskColumnI())
    }