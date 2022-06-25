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

import {createCanvasContext2D} from 'ol/dom';
import OlImageTile from 'ol/ImageTile';
import TileState from 'ol/TileState';
import {getUid} from 'ol/util';
import {isArray} from '../utils/Misc';

/**
 * @classdesc
 * ImageTile is a custom extention of ol.ImageTile in order to allow peforming
 * byte operations on the image data after the tile has been loaded
 *
 * @constructor
 * @extends {ol.ImageTile}
 *
 * @param {ol.TileCoord} tileCoord Tile coordinate.
 * @param {ol.TileState} state State.
 * @param {string} src Image source URI.
 * @param {?string} crossOrigin Cross origin.
 * @param {ol.TileLoadFunctionType} tileLoadFunction Tile load function.
 * @param {olx.TileOptions=} opt_options Tile options.
 */
class ImageTile extends OlImageTile {

    constructor(tileCoord, state, src, crossOrigin,
                tileLoadFunction, opt_options) {

        super(tileCoord, state, src, crossOrigin,
                     tileLoadFunction, opt_options);

        /**
         * @type {object}
         * @private
         */
        this.imageByContext_ = {};
    };

    /**
     * A convenience method to draw the tile into the context to then be called
     * within the scope of the post tile load hook for example...
     *
     * @function
     * @private
     * @param {Object} tile the tile as an image object
     * @param {Array.<number>} tileSize the tile size as an array [width, height]
     * @param {boolean} createContextOnlyForResize renders on canvas only if resize is needed
     * @param {string} key the uid (for createContextOnlyForResize is true)
     * @return {HTMLCanvasElement|HTMLImageElement|HTMLVideoElement} the image drawn on the context.
     */
    getRenderedTileAsContext(tile, tileSize, createContextOnlyForResize, key) {
        if ((typeof(tile) !== 'object') ||
            (typeof(tile['width']) !== 'number') ||
            (typeof(tile['height']) !== 'number')) return tile;

        if (typeof createContextOnlyForResize !== 'boolean')
            createContextOnlyForResize = false;

        var w = tile.width;
        var h = tile.height;
        if (isArray(tileSize) && tileSize.length > 1) {
            w = tileSize[0];
            h = tileSize[1];
        }

        // no need to use a canvas if we don't resize
        if (createContextOnlyForResize && w === tile.width && h === tile.height)
            return tile;

        var context = createCanvasContext2D(w,h);
        context.drawImage(tile, 0,0);

        // we'd like to store the resized tile so that we don't have to do it again
        if (createContextOnlyForResize && key)
            this.imageByContext_[key] = context.canvas;

        return context.canvas;
    }

    /**
     * Get the HTML image element for this tile (may be a Canvas, Image, or Video).
     * @function
     * @param {Object} opt_context Object.
     * @return {HTMLCanvasElement|HTMLImageElement|HTMLVideoElement} Image.
     */
    getImage(opt_context) {
        var image = super.getImage();
        // we are not loaded yet => good byes
        if (this.state !== TileState.LOADED) return image;

        // do we have the image already (resized or post tile function applied)
        var key = getUid(image);
        if (key in this.imageByContext_)
            return this.imageByContext_[key];

        // do we have a post tile function
        var postTileFunction = this.source.getPostTileLoadFunction();
        var tileSize = this.source.tileGrid.tileSize_;
        if (typeof(postTileFunction) !== 'function') // no => return
            return this.getRenderedTileAsContext(image, tileSize, true, key);

        // post tile function
        image = this.getRenderedTileAsContext(image);
        try {
            var context =  postTileFunction.call(this, image);
            if (context === null) context = image;

            this.imageByContext_[key] = context;

            return context;
        } catch(planb) {
            return image;
        }
    }
}

export default ImageTile;