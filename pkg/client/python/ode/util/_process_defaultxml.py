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
Convert the nodes and server-instances in default.xml to a multi-node
configuration

The configuration string should be in the form node1:s1,s2,... node2:s3 ...

Examples

Everything on a single node (default, the same as passing no config):
master:ODE-0,Indexer-0,DropBox,MonitorServer,FileServer,Storm,PixelData-0,Processor-0,Tables-0,TestDropBox

Processor on a separate node:
master:ODE-0,Indexer-0,DropBox,MonitorServer,FileServer,Storm,PixelData-0,Tables-0,TestDropBox slave:Processor-0

Two Processor and two PixelData on two separate nodes:
master:ODE-0,Indexer-0,DropBox,MonitorServer,FileServer,Storm,Tables-0,TestDropBox slave-1:Processor-0,PixelData-0 slave-2:Processor-1,PixelData-1
"""

import re
import sys

def _getnodes(nodedescs):
    nodes = {}
    for nd in nodedescs:
        s = ''
        node, descs = nd.split(':')
        descs = descs.split(',')
        for d in descs:
            try:
                t, i = d.split('-')
            except ValueError:
                t = d
                i = None
            s += '      <server-instance template="%sTemplate"' % t
            if i is not None:
                s += ' index="%s"' % i
            if t in ('ODE',):
                s += ' config="default"'
            if t in ('PixelData', 'Processor', 'Tables'):
                s += ' dir=""'
            s += '/>\n'
        nodes[node] = s
    return nodes


def _process_xml(xml, nodedescs):
    pattern = r'\<node name="master"\>\s*\<server-instance[^\>]*\>(.*?\</node\>)'
    m = re.search(pattern, xml, re.DOTALL)
    assert m

    master = '\n    </node>\n'
    slaves = ''
    nodes = _getnodes(nodedescs.split())
    for nodename in sorted(nodes.keys()):
        servers = nodes[nodename]
        if nodename == 'master':
            master = '%s%s' % (servers, master)
        else:
            slaves += '    <node name="%s">\n%s    </node>\n' % (nodename, servers)

    if nodes:
        xmlout = xml[:m.start(1)] + master + slaves + xml[m.end(1):]
    else:
        xmlout = xml
    return xmlout