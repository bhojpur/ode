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

import ode.ClientError;
import ode.ServerError;

import ode.api.RawPixelsStorePrx;
import ode.api.ServiceFactoryPrx;
import ode.model.Pixels;

/**
 * Helper subclass of {@link TileLoop} which handles
 * unloaded {@link Pixels} instances and manages the
 * creation of the {@link RPSTileData}.
 */
public class RPSTileLoop extends TileLoop {

    protected final ServiceFactoryPrx session;

    /**
     * Instance will be replaced on {@link RawPixelsStorePrx#save()}
     */
    protected volatile Pixels pixels;

    public RPSTileLoop(ServiceFactoryPrx session, Pixels pixels) {
        this.session = session;
        this.pixels = pixels;

        if (pixels == null || pixels.getId() == null) {
            throw new ClientError("pixels instance must be managed!");
        }

    }

    protected ServiceFactoryPrx getSession() {
        return session;
    }

    /**
     * After saving the binary data, the update event of the
     * {@link Pixels} instance will be updated and therefore
     * need to be reloaded. As a convenience the returned
     * value is accessible here.
     */
    public Pixels getPixels() {
        return this.pixels;
    }

    /**
     * Called by the {@link TileData} implementation to update
     * the {@link #pixels} instance for re-use by the client.
     */
    public void setPixels(Pixels pixels) {
        this.pixels = pixels;
    }

    public TileData createData() {
        try {
            RawPixelsStorePrx rps = getSession().createRawPixelsStore();
            rps.setPixelsId(getPixels().getId().getValue(), false); // 'false' is ignored here.
            return new RPSTileData(this, rps);
        } catch (ServerError se) {
            throw new RuntimeException(se);
        }
    }

    /**
     * Iterates over every tile in a given pixel based on the
     * over arching dimensions and a requested maximum tile width and height.
     * @param iteration Invoker to call for each tile.
     * @param tileWidth <b>Maximum</b> width of the tile requested. The tile
     * request itself will be smaller than the original tile width requested if
     * {@code x + tileWidth > sizeX}
     * @param tileHeight <b>Maximum</b> height of the tile requested. The tile
     * request itself will be smaller if {@code y + tileHeight > sizeY}.
     * @return The total number of tiles iterated over.
     */
    public int forEachTile(int tileWidth, int tileHeight,
                           TileLoopIteration iteration) throws ClientError, ServerError {

        if (!pixels.isLoaded()) {
            try {
                pixels = getSession().getPixelsService().retrievePixDescription(pixels.getId().getValue());
            } catch (Exception e) {
                throw new ClientError("Failed to load " + pixels.getId().getValue() + "\n" + e);
            }
        }

        final int sizeX = pixels.getSizeX().getValue();
        final int sizeY = pixels.getSizeY().getValue();
        final int sizeZ = pixels.getSizeZ().getValue();
        final int sizeC = pixels.getSizeC().getValue();
        final int sizeT = pixels.getSizeT().getValue();

        return forEachTile(sizeX, sizeY, sizeZ, sizeC, sizeT, tileWidth, tileHeight, iteration);

    }
}