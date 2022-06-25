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

// import Feature from 'ol/Feature';
import Pointer from 'ol/interaction/Pointer';
import Overlay from 'ol/Overlay.js';
import {featuresAtCoords} from '../utils/Misc';

/**
 * @classdesc
 * Implements a leaner version of the standard open layers select without an extra
 * layer
 *
 * @extends {ol.interaction.Interaction}
 */
class Hover extends Pointer {

    /**
     * @constructor
     * 
     * @param {source.Regions} regions_reference a reference to Regions
     */
    constructor(regions_reference) {

        super({});

        var els = document.querySelectorAll(
            '.hover-popup');
        this.tooltip = document.createElement('div');
        this.tooltip.className = 'hover-popup';

        this.regions_ = regions_reference;
        this.map = regions_reference.viewer_.viewer_;

        this.overlay = new Overlay({
            element: this.tooltip,
            insertFirst: false,   // show over other controls
            autoPan: false,
          });

        this.map.addOverlay(this.overlay);
    };

    /**
     * Handle mouse moving on the image. If it moves over a feature and the
     * feature has some text to display, we show a popup.
     *
     * @param {Object} mapBrowserEvent
     */
    handleMoveEvent(mapBrowserEvent) {
        const map = mapBrowserEvent.map;
        let hits = [];
        // First check for features under mouse pointer (0 tolerance)
        map.forEachFeatureAtPixel(mapBrowserEvent.pixel,
            (feature) => hits.push(feature),
            {hitTolerance: 0}
        );
        // If nothing found, check wider
        if (hits.length == 0) {
            map.forEachFeatureAtPixel(mapBrowserEvent.pixel,
                (feature) => hits.push(feature),
                {hitTolerance: 5}
            );
        }
        let hit = featuresAtCoords(hits);
        this.overlay.setPosition(undefined);
        // If the event has come via the ShapeEditPopup or the shape is
        // selected then we ignore it.
        let isOverShapeEditPopup = mapBrowserEvent.originalEvent.isOverShapeEditPopup;
        if (hit && !hit['selected'] && !isOverShapeEditPopup) {
            let coords = mapBrowserEvent.coordinate;
            let textStyle = hit['oldText'];
            if (textStyle && textStyle.getText() && textStyle.getText().length > 0) {
                let text = textStyle.getText();
                let width = Math.min(Math.max(100, text.length * 8), 250);
                this.tooltip.innerHTML = `<div style='width: ${width}px'>
                                            ${ textStyle.getText() }
                                        </div>`;
                this.overlay.setPosition([coords[0], coords[1] + 20]);
            }
            // hover effect
            let shapeId = hit.getId();
            // if change in hover
            if (this.regions_.getHoverId() != shapeId) {
                this.regions_.setHoverId(shapeId);
            }
        } else {
            if (this.regions_.getHoverId()) {
                this.regions_.setHoverId(null);
            }
        }
    }

    /**
     * a sort of destructor - remove the overlay from the map
     */
    disposeInternal() {
        this.overlay.setPosition(undefined);
        this.map.removeOverlay(this.overlay);
        this.map = null;
    }
}

export default Hover;