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

import DragBox from 'ol/interaction/DragBox';
import {listen,
    unlistenByKey} from 'ol/events';
import {platformModifierKeyOnly} from 'ol/events/condition';
import Regions from '../source/Regions';

/**
 * @classdesc
 * Extends the built in dragbox interaction to make it more custom in terms of keys
 * and add the appropriate boxstart and boxend handlers straight away
 *
 * @extends {ol.interaction.DragBox}
 * @fires ol.interaction.DragBox.Event
 */
class BoxSelect extends DragBox {

    /**
     * @constructor
     * @param {source.Regions} regions_reference a reference to get to all (selected) rois
     */
    constructor(regions_reference) {
        if (!(regions_reference instanceof Regions))
            console.error("Select needs Regions instance!");
        super();

        /**
         * @private
         * @type {ol.events.ConditionType}
         */
        this.condition_ = platformModifierKeyOnly;

        // we do need the regions reference to get the (selected) rois
        if (!(regions_reference instanceof Regions)) return;

        /**
         * a reference to the Regions instance
         * @private
         * @type {source.Regions}
         */
        this.regions_ = regions_reference;

        /**
         * the box start listener
         * @private
         * @type {function}
         */
        this.boxStartFunction_ =
            function() {
                if (this.regions_.select_) this.regions_.select_.clearSelection();
            };
        this.boxStartListener_ = null;

        /**
         * the box end listener
         * @private
         * @type {function}
         */
        this.boxEndFunction_ = function() {
            if (this.regions_.select_ === null) return;

            var ids = [];
            var extent = this.getGeometry().getExtent();
            this.regions_.forEachFeatureInExtent(
                extent, function(feature) {
                    if (feature.getGeometry().intersectsExtent(extent))
                        ids.push(feature.getId());
                });
            if (ids.length > 0) this.regions_.setProperty(ids, "selected", true);
        };
        this.boxEndListener_ = null;

        this.registerListeners = function() {
            this.boxStartListener_ =
                listen(this, 'boxstart', this.boxStartFunction_, this);
            this.boxEndListener_ =
                listen(this, 'boxend', this.boxEndFunction_, this);
        };

        this.registerListeners();
    };

    /**
     * Register the start/end listeners
     */
    resetListeners() {
        this.unregisterListeners();
        this.registerListeners();
    }

    /**
     * Unregister the start/end listeners
     */
    unregisterListeners() {
        if (this.boxStartListener_) unlistenByKey(this.boxStartListener_);
        if (this.boxEndListener_) unlistenByKey(this.boxEndListener_);
    }

    /**
     * a sort of destructor
     */
    disposeInternal() {
        this.unregisterListeners();
        this.regions_ = null;
    }
}

export default BoxSelect;