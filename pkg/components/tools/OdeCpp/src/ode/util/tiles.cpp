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

/*
 * Bhojpur ODE Tile Utilities
 */

#include <ode/util/tiles.h>
#include <ode/ClientErrors.h>
#include <ode/IceNoWarnPush.h>
#include <ode/api/IPixels.h>
#include <ode/IceNoWarnPop.h>

namespace ode {
    namespace util {

        //
        // TileLoopIteration
        //

        TileLoopIteration::TileLoopIteration() {
        }

        TileLoopIteration::~TileLoopIteration() {
        }

        //
        // TileData
        //

        TileData::TileData() {
        }

        TileData::~TileData() {
        }

        //
        // TileLoop
        //

        TileLoop::TileLoop() {
        }

        TileLoop::~TileLoop() {
        }

        int TileLoop::forEachTile(int sizeX, int sizeY, int sizeZ, int sizeT, int sizeC,
                                  int tileHeight, int tileWidth, const TileLoopIterationPtr& iteration) {

            TileDataPtr data = createData();
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
                                iteration->run(data, z, c, t, x, y, w, h, tileCount);
                                tileCount++;
                            }
                        }
                    }
                }

            }
            return tileCount;
        }

        //
        // RPSTileData
        //

        RPSTileData::RPSTileData(const RPSTileLoopPtr& loop,
                                 const ode::api::RawPixelsStorePrx& rps) : TileData(), loop(loop), rps(rps) {
        }

        RPSTileData::~RPSTileData() {
        }

        Ice::ByteSeq RPSTileData::getTile(int z, int c, int t, int x, int y, int w, int h) {
            return rps->getTile(z, c, t, x, y, w, h);
        }

        void RPSTileData::setTile(const Ice::ByteSeq& buffer, int z, int c, int t, int x, int y, int w, int h) {
            rps->setTile(buffer, z, c, t, x, y, w, h);
        }

        void RPSTileData::close() {
            ode::model::PixelsPtr pixels = rps->save();
            loop->setPixels(pixels);
            rps->close(); // TODO: this should be a wrapper which calls close
        }

        //
        // RPSTileLoop
        //

        RPSTileLoop::RPSTileLoop(const ode::api::ServiceFactoryPrx& session,
                                 const ode::model::PixelsPtr& pixels) : TileLoop(), session(session), pixels(pixels) {

            if (!this->pixels || !this->pixels->getId()) {
                throw ode::ClientError(__FILE__, __LINE__, "pixels instance must be managed!");
            }

        }

        RPSTileLoop::~RPSTileLoop() {
        }

        ode::model::PixelsPtr RPSTileLoop::getPixels() {
            return this->pixels;
        }

        void RPSTileLoop::setPixels(const ode::model::PixelsPtr& pixels) {
            this->pixels = pixels;
        }

        TileDataPtr RPSTileLoop::createData() {
            ode::api::RawPixelsStorePrx rps = getSession()->createRawPixelsStore();
            rps->setPixelsId(getPixels()->getId()->getValue(), false); // 'false' is ignored here.
            return new RPSTileData(this, rps);
        }

        ode::api::ServiceFactoryPrx RPSTileLoop::getSession() {
            return session;
        }

        int RPSTileLoop::forEachTile(int tileHeight, int tileWidth, const TileLoopIterationPtr& iteration) {

            if (!pixels->isLoaded()) {
                pixels = getSession()->getPixelsService()->retrievePixDescription(pixels->getId()->getValue());
            }

            int sizeX = pixels->getSizeX()->getValue();
            int sizeY = pixels->getSizeY()->getValue();
            int sizeZ = pixels->getSizeZ()->getValue();
            int sizeC = pixels->getSizeC()->getValue();
            int sizeT = pixels->getSizeT()->getValue();

            return TileLoop::forEachTile(sizeX, sizeY, sizeZ, sizeT, sizeC, tileWidth, tileHeight, iteration);
        }

    }
}

