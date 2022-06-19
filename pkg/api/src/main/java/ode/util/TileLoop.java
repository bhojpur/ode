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

public abstract class TileLoop {

    /**
     * Subclasses must provide a fresh instance of {@link TileData}.
     * The instance will be closed after the run of
     * {@link #forEachTile(int, int, int, int, int, int, int, TileLoopIteration)}.
     * @return the new instance
     */
    public abstract TileData createData();

    /**
     * Iterates over every tile in a given pixel based on the
     * over arching dimensions and a requested maximum tile width and height.
     * @param sizeX the size of the plane's X dimension
     * @param sizeY the size of the plane's Y dimension
     * @param sizeZ the size of the plane's Z dimension
     * @param sizeC the size of the plane's C dimension
     * @param sizeT the size of the plane's T dimension
     * @param iteration Invoker to call for each tile.
     * @param tileWidth <b>Maximum</b> width of the tile requested. The tile
     * request itself will be smaller than the original tile width requested if
     * {@code x + tileWidth > sizeX}.
     * @param tileHeight <b>Maximum</b> height of the tile requested. The tile
     * request itself will be smaller if {@code y + tileHeight > sizeY}.
     * @return The total number of tiles iterated over.
     */
    public int forEachTile(int sizeX, int sizeY,
                           int sizeZ, int sizeC, int sizeT,
                           int tileWidth, int tileHeight,
                           TileLoopIteration iteration) {

        final TileData data = createData();

        try
        {
            int x, y, w, h;
            int tileCount = 0;
            for (int t = 0; t < sizeT; t++)
            {
                for (int c = 0; c < sizeC; c++)
                {
                    for (int z = 0; z < sizeZ; z++)
                    {
                        for (int tileOffsetY = 0;
                            tileOffsetY < (sizeY + tileHeight - 1) / tileHeight;
                            tileOffsetY++)
                        {
                            for (int tileOffsetX = 0;
                                tileOffsetX < (sizeX + tileWidth - 1) / tileWidth;
                                tileOffsetX++)
                            {
                                x = tileOffsetX * tileWidth;
                                y = tileOffsetY * tileHeight;
                                w = tileWidth;
                                if (w + x > sizeX)
                                {
                                    w = sizeX - x;
                                }
                                h = tileHeight;
                                if (h + y > sizeY)
                                {
                                    h = sizeY - y;
                                }
                                iteration.run(data, z, c, t, x, y, w, h, tileCount);
                                tileCount++;
                            }
                        }
                    }
                }
            }

            return tileCount;

        } finally {

            data.close();

        }
    }
}