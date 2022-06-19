package ode.util;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import ode.ServerError;
import ode.api.RawPixelsStorePrx;
import ode.model.Pixels;

/**
 * Access strategy which can be implemented by diverse resources
 *
 */
public class RPSTileData implements TileData
{

    final protected RawPixelsStorePrx rps;

    final protected RPSTileLoop loop;

    public RPSTileData(RPSTileLoop loop, RawPixelsStorePrx rps) {
        this.loop = loop;
        this.rps = rps;
    }

    public byte[] getTile(int z, int c, int t, int x, int y, int w, int h) {
        try {
            return rps.getTile(z, c, t, x, y, w, h);
        } catch (ServerError se) {
            throw new RuntimeException(se);
        }
    }

    public void setTile(byte[] buffer, int z, int c, int t, int x, int y, int w, int h) {
        try {
            rps.setTile(buffer, z, c, t, x, y, w, h);
        } catch (ServerError se) {
            throw new RuntimeException(se);
        }
    }

    public void close() {
        try {
            Pixels pixels = rps.save();
            loop.setPixels(pixels);
            rps.close();
        } catch (ServerError se) {
            throw new RuntimeException(se);
        }
    }

}