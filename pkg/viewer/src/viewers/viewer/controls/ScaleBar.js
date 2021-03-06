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

import ScaleLine from 'ol/control/ScaleLine';
import OlMap from 'ol/Map';
import {listen, unlistenByKey} from 'ol/events';
import {UNITS_LENGTH} from '../globals';

/**
 * For a given pixel unit, e.g. microns, we want the scalebar to be one of
 * these lengths.
 */
const SNAP_TO_LENGTHS = [1, 2, 5, 10, 20, 50,
                         100, 200, 500, 1000, 2000, 5000, 10000];

/**
 * @classdesc
 * Extends the built ScaleLine
 */
class ScaleBar extends ScaleLine {

    /**
     * @constructor
     * @param {Object} opt_options optional options
     */
    constructor(opt_options) {
        if (typeof(opt_options) !== 'object') opt_options = {};
        super(opt_options);

        /**
         * minimum scale bar width in pixels
         * @type {number}
         * @private
         */
        this.bar_min_width_ = 100;

        /**
         * the scalebar 'drag' listener
         * @type {number}
         * @private
         */
        this.drag_listener_ = null;

        // give element a tooltip
        this.element.title = "Click and drag to move scalebar";
        // append ol-control
        this.element.className += " ol-control";

        // register 'drag' listener
        listen(
            this.element, "mousedown",
            function(start) {
                if (!(this.map_ instanceof OlMap)) return;
                if (this.drag_listener_ !== null) {
                    unlistenByKey(this.drag_listener_);
                    this.drag_listener_ = null;
                }
                var offsetX = -start.offsetX;
                var offsetY = -start.offsetY;
                this.drag_listener_ =
                    listen(this.map_, "pointermove",
                        function(move) {
                            var e = move.originalEvent;
                            if (!((typeof e.buttons === 'undefined' &&
                                e.which === 1) || e.buttons === 1)) {
                                    unlistenByKey(this.drag_listener_);
                                    this.drag_listener_ = null;
                                    return;
                                }
                            this.element.style.bottom = "auto";
                            this.element.style.left =
                                (move.pixel[0] + offsetX) + "px";
                            this.element.style.top =
                                (move.pixel[1] + offsetY) + "px";
                        }, this);
        }, this);
    }

    /**
     * Overridden to deal with mere pixel to unit issues instead of geographic
     * projections
     * @private
     */
    updateElement_() {
        var viewState = this.viewState_;

        if (!viewState) {
            if (this.renderedVisible_) {
            this.element.style.display = 'none';
            this.renderedVisible_ = false;
            }
            return;
        }

        var micronsPerPixel = viewState.projection.getMetersPerUnit();
        var resolution = viewState.resolution;

        // first find the Units and Length for min-length scalebar
        var scaleBarLengthInUnits = micronsPerPixel * this.bar_min_width_ * resolution;
        var symbol = '\u00B5m';
        for (var u=0;u<UNITS_LENGTH.length;u++) {
            var unit = UNITS_LENGTH[u];
            if (scaleBarLengthInUnits < unit.threshold) {
                scaleBarLengthInUnits *= unit.multiplier;
                symbol = unit.symbol;
                break;
            }
        }

        // Find a length value BIGGER than our min target length
        var value = 0;
        let pixels = 0;
        for (let snap=0; snap<SNAP_TO_LENGTHS.length; snap++) {
            value = SNAP_TO_LENGTHS[snap];
            if (value >= scaleBarLengthInUnits) {
                pixels = this.bar_min_width_ * value / scaleBarLengthInUnits;
                break;
            }
        }

        var html = value + ' ' + symbol;
        this.innerElement_.innerHTML = html;
        this.innerElement_.style.width = pixels + 'px';

        if (!this.renderedVisible_) {
            this.element.style.display = '';
            this.renderedVisible_ = true;
        }
    }
}

export default ScaleBar;