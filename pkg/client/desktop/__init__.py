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

import os

try:
    from ._version import version as __version__
except ImportError:
    __version__ = "not-installed"

# Allows us to use pydata/sparse arrays as layer data
os.environ.setdefault('SPARSE_AUTO_DENSIFY', '1')
del os

# Add everything that needs to be accessible from the Bhojpur ODE namespace here.
_proto_all_ = [
    '__version__',
    'components',
    'experimental',
    'layers',
    'qt',
    'types',
    'viewer',
    'utils',
]

_submod_attrs = {
    '_event_loop': ['gui_qt', 'run'],
    'plugins.io': ['save_layers'],
    'utils': ['sys_info'],
    'utils.notifications': ['notification_manager'],
    'view_layers': [
        'view_image',
        'view_labels',
        'view_path',
        'view_points',
        'view_shapes',
        'view_surface',
        'view_tracks',
        'view_vectors',
    ],
    'viewer': ['Viewer', 'current_viewer'],
}

# All imports in __init__ are hidden inside of `__getattr__` to prevent
# importing the full chain of packages required when calling `import viewer`.
#
# This has the biggest implications for running `desktop` on the command line
# (or running `python -m desktop`) since `desktop.__init__` gets imported
# on the way to `desktop.__main__`. Importing everything here has the
# potential to take a second or more, so we definitely don't want to import it
# just to access the CLI (which may not actually need any of the imports)

from ._lazy import install_lazy

__getattr__, __dir__, __all__ = install_lazy(
    __name__, _proto_all_, _submod_attrs
)
del install_lazy