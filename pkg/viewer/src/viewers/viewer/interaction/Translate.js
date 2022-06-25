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

import OlTranslate from 'ol/interaction/Translate';
import {TranslateEvent} from 'ol/interaction/Translate';
import Collection from 'ol/Collection';
import {listen} from 'ol/events';
import Regions from '../source/Regions';
import Label from '../geom/Label';
import Mask from '../geom/Mask';
import {sendEventNotification} from '../utils/Misc';
import {REGIONS_STATE} from '../globals';

/**
 * @classdesc
 * Extension that handles shape dragging, notifications etc.
 *
 * @extends {ol.interaction.Translate}
 */

// Since we can't import e.g. ol.interaction.TranslateEventType.TRANSLATESTART
// We define equivalent constants here:
const TRANSLATESTART = 'translatestart';
const TRANSLATEEND = 'translateend';
const TRANSLATING = 'translating';

class Translate extends OlTranslate {

    /**
     * @constructor
     * 
     * @param {source.Regions} regions_reference an Regions instance.
     */
    constructor(regions_reference) {
        // we do need the regions reference to do translations
        if (!(regions_reference instanceof Regions))
            console.error("Translate needs Regions instance!");

        super({});

        /**
         * @type {source.Regions}
         * @private
         */
        this.regions_ = regions_reference;

        /**
         * @type {Array.<ol.Feature>}
         * @private
         */
        this.translatedFeatures_ = [];

        /**
         * @type {number}
         * @private
         */
        this.hist_id_ = -1;

        /**
         * @type {Collection.<ol.Feature>}
         * @private
         */
        this.features_ = this.regions_.select_.getFeatures();

        // a listener to react on translate start
        listen(
            this,
            TRANSLATESTART,
            function(event) {
                this.translatedFeatures_ = [];
                var features = event.features.array_;
                for (var x=0;x<features.length;x++) {
                    var f = features[x];
                    if (f.getGeometry() instanceof Mask ||
                        (typeof f['permissions'] === 'object' &&
                        f['permissions'] !== null &&
                        typeof f['permissions']['canEdit'] === 'boolean' &&
                        !f['permissions']['canEdit'])) continue;
                    var clonedF = f.clone();
                    clonedF.setId(f.getId());
                    this.translatedFeatures_.push(clonedF);
                }
            }, this);

        // a listener to note whether there was an actual translate ...
        listen(
            this,
            TRANSLATING,
            function(event) {
                // If dragging a single shape, update the ShapeEditPopup
                if (event.features.getArray().length == 1) {
                    let geometry = event.features.getArray()[0].getGeometry();
                    this.regions_.viewer_.viewer_.getOverlays().forEach(o => {
                        if (o.updatePopupCoordinates) {
                            o.updatePopupCoordinates(geometry);
                        }
                    });
                }
                // start the history for the translate
                // if we haven't done so already
                if (this.hist_id_ >= 0 ||
                    this.translatedFeatures_.length === 0) return;
                this.hist_id_ =
                    this.regions_.addHistory(this.translatedFeatures_, true);
            }, this);

        // a listener to react on translate end
        listen(
            this,
            TRANSLATEEND,
            Translate.prototype.handleTranslateEnd,
            this);
    };

    /**
     * We want to react to the end of translation for some features
     * @param {ol.interaction.Translate.Event} event a translate event.
     */
    handleTranslateEnd(event) {
        // reset original feature array
        this.translatedFeatures_ = [];

        // snapshot present features
        var featuresTranslated = event.features.array_;
        var filtered = [];
        var ids = [];
        for (var x=0;x<featuresTranslated.length;x++) {
            var f = featuresTranslated[x];
            if (f.getGeometry() instanceof Mask ||
                (typeof f['permissions'] === 'object' &&
                f['permissions'] !== null &&
                typeof f['permissions']['canEdit'] === 'boolean' &&
                !f['permissions']['canEdit'])) continue;
            if (f.getGeometry() instanceof Label)
                f.getGeometry().modifyOriginalCoordinates(
                    event.target.getMap().getView().getRotation(),
                    event.target.getMap().getView().getResolution()
                );
            filtered.push(f);
            ids.push(f.getId());
        }

        if (this.hist_id_ < 0) return;

        // complete history entry
        this.regions_.addHistory(filtered, false, this.hist_id_);
        sendEventNotification(
            this.regions_.viewer_,
            "REGIONS_HISTORY_ENTRY", {"hist_id": this.hist_id_, "shape_ids": ids});
        this.hist_id_ = -1; // reset

        // set modified flag
        if (ids.length > 0) this.regions_.setProperty(
            ids, "state", REGIONS_STATE.MODIFIED);
    }

    /**
     * @param {ol.MapBrowserPointerEvent} mapBrowserEvent Event.
     * @return {boolean} Start drag sequence?
     * @this {ol.interaction.Translate}
     * @private
     */
    handleDownEvent_(mapBrowserEvent) {
        this.lastFeature_ = this.featuresAtCoords_(mapBrowserEvent.pixel);
        if (!this.lastCoordinate_ && this.lastFeature_) {
            this.lastCoordinate_ = mapBrowserEvent.coordinate;
            Translate.handleMoveEvent_.call(
                this, mapBrowserEvent);
            this.dispatchEvent(
                new TranslateEvent(
                    'translatestart',
                    this.features_, mapBrowserEvent.coordinate));
            return true;
        }
        return false;
    };

    /**
     * @param {ol.MapBrowserEvent} mapBrowserEvent Event.
     * @this {ol.interaction.Translate}
     * @private
     */
    handleMoveEvent_(mapBrowserEvent) {
        // overridden on purpose to do nothing
    };

    /**
     * @param {ol.MapBrowserPointerEvent} mapBrowserEvent Event.
     * @return {boolean} Stop drag sequence?
     * @this {ol.interaction.Translate}
     * @private
     */
    handleUpEvent_(mapBrowserEvent) {
        if (this.lastCoordinate_) {
            this.lastCoordinate_ = null;
            Translate.handleMoveEvent_.call(
                this, mapBrowserEvent);
            this.dispatchEvent(
                new TranslateEvent(
                    'translateend',
                    this.features_, mapBrowserEvent.coordinate));
            return true;
        }
        return false;
    };

    /**
     * Tests to see if the given coordinates intersects any of our features
     * as well as a permissions check
     * @param {ol.Coordinate} coord coordinate to test for intersection.
     * @return {ol.Feature} Returns the feature found at the specified pixel
     * coordinates.
     * @private
     */
    featuresAtCoords_(coord) {
        var hit = this.regions_.select_.featuresAtCoords_(coord, 5, true);
        if (hit === null || !(this.features_ instanceof Collection) ||
            hit.getGeometry() instanceof Mask ||
                (typeof hit['permissions'] === 'object' &&
                hit['permissions'] !== null &&
                typeof hit['permissions']['canEdit'] === 'boolean' &&
                !hit['permissions']['canEdit']) ||
                (typeof this.regions_['is_modified'] === 'boolean' &&
                this.regions_['is_modified']))
                    return null;
        return hit;
    };

    /**
     * a sort of destructor
     */
    disposeInternal() {
        this.features_  = null;
        this.regions_ = null;
    }
}

export default Translate;