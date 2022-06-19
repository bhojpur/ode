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

/**
* A single iteration of a tile for each loop.
*/
public interface TileLoopIteration
{
    /**
    * Invoke a single loop iteration.
    * @param data the tile access strategy
    * @param z Z section counter of the loop.
    * @param c Channel counter of the loop.
    * @param t Timepoint counter of the loop.
    * @param x X offset within the plane specified by the section, channel and
    * timepoint counters.
    * @param y Y offset within the plane specified by the section, channel and
    * timepoint counters.
    * @param tileWidth Width of the tile requested. The tile request
    * itself may be smaller than the original tile width requested if
    * {@code x + tileWidth > sizeX}.
    * @param tileHeight Height of the tile requested. The tile request
    * itself may be smaller if {@code y + tileHeight > sizeY}.
    * @param tileCount Counter of the tile since the beginning of the loop.
    */
    void run(TileData data, int z, int c, int t, int x, int y,
             int tileWidth, int tileHeight, int tileCount);

}