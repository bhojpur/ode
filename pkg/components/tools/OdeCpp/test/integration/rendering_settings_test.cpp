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

#include <ode/fixture.h>
#include <ode/util/tiles.h>

using namespace std;
using namespace ode::api;
using namespace ode::model;
using namespace ode::rtypes;
using namespace ode::sys;
using namespace ode::util;

class MyIteration : virtual public TileLoopIteration {
private:
    // Preventing copy-construction and assigning by value.
    MyIteration& operator=(const MyIteration& rv);
    MyIteration(MyIteration&);
public:
    MyIteration() : TileLoopIteration(){}
    ~MyIteration(){}
    void run(const TileDataPtr& data, int z, int c, int t, int x, int y, int tileWidth, int tileHeight, int /*tileCount*/) const {
        Ice::ByteSeq buf(tileWidth*tileHeight*8);
        data->setTile(buf, z, c, t, x, y, tileWidth, tileHeight);
    }
};


class RndFixture {
    Fixture f;
    ServiceFactoryPrx sf;
    string _name;
public:
    RndFixture() {
    }
    RndFixture(string name) {
        _name = name;
    }
    void init() {
        if (!sf) {
            ode::client_ptr client;
            if (_name.empty()) {
                f.login();
                client = f.client;
            } else if (_name == "root") {
                client = f.root;
            } else {
                f.login(_name);
                client = f.client;
            }
            sf = client->getSession();
        }
    }

    IUpdatePrx update() {
        init();
        return sf->getUpdateService();
    }
    IRenderingSettingsPrx rndService() {
        init();
        return sf->getRenderingSettingsService();
    }

    /**
     * Create a single image with binary.
     *
     * After recent changes on the server to check for existing
     * binary data for pixels, many resetDefaults methods tested
     * below began returning null since {@link ode.LockTimeout}
     * exceptions were being thrown server-side. By using
     * ode.client.forEachTile, we can set the necessary data easily.
     *
     * @see ticket:5755
     */
    ImagePtr createBinaryImage() {
        ImagePtr image = new ImageI();
        image->setName(rstring("createBinaryImage"));
        image->addPixels(f.pixels());
        image = ImagePtr::dynamicCast(update()->saveAndReturnObject(image));
        return createBinaryImage(image);
    }


    /**
     * Create the binary data for the given image.
     */
    ImagePtr createBinaryImage(ImagePtr _image) {

        PixelsPtr pixels = _image->getPixels(0);
        RPSTileLoopPtr loop = new RPSTileLoop(f.client->getSession(), pixels);
        loop->forEachTile(256, 256, new MyIteration());
        // This block will change the updateEvent on the pixels
        // therefore we're going to reload the pixels.

        _image->setPixels(0, loop->getPixels());
        return _image;

    }

};

TEST(RenderingSettingsTest, testResetDefaultsInImage )
{

    RndFixture f;
    ImagePtr img = f.createBinaryImage();
    ASSERT_TRUE( img->getId() );
    f.rndService()->resetDefaultsInImage(img->getId()->getValue());
}
