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

#ifndef ODE_UTIL_TILES_H
#define ODE_UTIL_TILES_H

#include <ode/IceNoWarnPush.h>
#include <Ice/Ice.h>
#include <IceUtil/Handle.h>
#include <ode/API.h>
#include <ode/api/RawPixelsStore.h>
#include <ode/model/Pixels.h>
#include <ode/IceNoWarnPop.h>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
    namespace util {
        class TileData;
        class TileLoopIteration;
        class TileLoop;
        class RPSTileLoop;
    }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::Object* upCast(::ode::util::TileData*);
  ODE_CLIENT ::Ice::Object* upCast(::ode::util::TileLoopIteration*);
  ODE_CLIENT ::Ice::Object* upCast(::ode::util::TileLoop*);
  ODE_CLIENT ::Ice::Object* upCast(::ode::util::RPSTileLoop*);
}

namespace ode {
    namespace util {

        /**
         * Interface which must be returned from TileLoop.createData
         */
        typedef IceUtil::Handle<TileData> TileDataPtr;

        class ODE_CLIENT TileData : virtual public IceUtil::Shared {
        private:
            // Preventing copy-construction and assigning by value.
            TileData& operator=(const TileData& rv);
            TileData(TileData&);
        public:
            TileData();
            virtual ~TileData() = 0;
            virtual Ice::ByteSeq getTile(int z, int c, int t, int x, int y, int w, int h) = 0;
            virtual void setTile(const Ice::ByteSeq& buffer, int z, int c, int t, int x, int y, int w, int h) = 0;
            virtual void close() = 0;
        };

        /**
         * Interface to be passed to forEachTile.
         */
        typedef IceUtil::Handle<TileLoopIteration> TileLoopIterationPtr;

        class ODE_CLIENT TileLoopIteration : virtual public IceUtil::Shared {
        private:
            // Preventing copy-construction and assigning by value.
            TileLoopIteration& operator=(const TileLoopIteration& rv);
            TileLoopIteration(TileLoopIteration&);
        public:
            TileLoopIteration();
            virtual ~TileLoopIteration() = 0;
            virtual void run(const TileDataPtr& data, int z, int c, int t, int x, int y,
                             int tileWidth, int tileHeight, int tileCount) const = 0;
        };

        /**
         * Interface to be passed to forEachTile.
         */
        typedef IceUtil::Handle<TileLoop> TileLoopPtr;

        class ODE_CLIENT TileLoop : virtual public IceUtil::Shared {
        private:
            // Preventing copy-construction and assigning by value.
            TileLoop& operator=(const TileLoop& rv);
            TileLoop(TileLoop&);
        public:
            TileLoop();
            virtual ~TileLoop() = 0;
            virtual TileDataPtr createData() = 0;
            virtual int forEachTile(int sizeX, int sizeY, int sizeZ, int sizeT, int sizeC,
                                   int tileHeight, int tileWidth, const TileLoopIterationPtr& iteration);
        };

        // Forward defs
        typedef IceUtil::Handle<RPSTileLoop> RPSTileLoopPtr;

        class ODE_CLIENT RPSTileData : virtual public TileData {
        protected:
            RPSTileLoopPtr loop;
            ode::api::RawPixelsStorePrx rps;
        public:
            RPSTileData(const RPSTileLoopPtr& loop, const ode::api::RawPixelsStorePrx& rps);
            virtual ~RPSTileData();
            virtual Ice::ByteSeq getTile(int z, int c, int t, int x, int y, int w, int h);
            virtual void setTile(const Ice::ByteSeq& buffer, int z, int c, int t, int x, int y, int w, int h);
            virtual void close();
        };

        class ODE_CLIENT RPSTileLoop : virtual public TileLoop {
        protected:
            ode::api::ServiceFactoryPrx session;
            ode::model::PixelsPtr pixels;
        public:
            using TileLoop::forEachTile;
            RPSTileLoop(const ode::api::ServiceFactoryPrx& session, const ode::model::PixelsPtr& pixels);
            virtual ~RPSTileLoop();
            virtual ode::api::ServiceFactoryPrx getSession();
            virtual ode::model::PixelsPtr getPixels();
            virtual void setPixels(const ode::model::PixelsPtr& pixels);
            virtual int forEachTile(int tileHeight, int tileWidth, const TileLoopIterationPtr& iteration);
            virtual TileDataPtr createData();
        };


    }

}

#endif // ODE_UTIL_TILES_H
