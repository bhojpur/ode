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

// js
import Context from '../app/context';
import Misc from '../utils/misc';
import Ui from '../utils/ui';
import {TABS, ROI_TABS, VIEWER} from '../utils/constants';
import {REGIONS_STORE_SHAPES, REGIONS_SHOW_COMMENTS} from '../events/events';
import {inject, customElement, bindable} from 'aurelia-framework';

/**
 * Represents the regions section in the right hand panel
 */
@customElement('regions')
@inject(Context)
export default class Regions {
    /**
     * a bound reference to regions_info
     * and its associated change handler
     * @memberof Regions
     * @type {RegionsInfo}
     */
    @bindable regions_info = null;

    /**
     * a list of keys we want to listen for
     * @memberof Regions
     * @type {Object}
     */
    key_actions = [
        { key: 'S', func: this.saveShapes},                          // ctrl - s
        { key: 'Y', func: this.redoHistory},                         // ctrl - y
        { key: 'Z', func: this.undoHistory}                          // ctrl - z
    ];

    /**
     * Make this available to the template
     */
    ROI_TABS = ROI_TABS;

    /**
     * Currently selected tab within the ROI panel. One of the ROI_TABS options.
     * @type {String}
     */
    selected_roi_tab = ROI_TABS.ROI_TABLE;

    show_roi_link = false;

    /**
     * @constructor
     * @param {Context} context the application context (injected)
     */
    constructor(context) {
        this.context = context;
    }

    /**
     * Overridden aurelia lifecycle method:
     * fired when PAL (dom abstraction) is ready for use
     *
     * @memberof Regions
     */
    attached() {
        Ui.registerKeyHandlers(this.context, this.key_actions, TABS.ROIS, this);

        $("#regions-tabs").find("a").click((e) => {
            e.preventDefault();
            this.selected_roi_tab = e.currentTarget.hash.substring(1);
        });
    }

    /**
     * Overridden aurelia lifecycle method:
     * called when the view and its elemetns are detached from the PAL
     * (dom abstraction)
     *
     * @memberof Regions
     */
    detached() {
        this.key_actions.map(
            (action) => this.context.removeKeyListener(action.key, TABS.ROIS));
    }

    /**
     * Saves all modified, deleted and new shapes
     *
     * @memberof Regions
     */
    saveShapes() {
        if (Misc.useJsonp(this.context.server)) {
            alert("Saving the regions will not work cross-domain!");
            return;
        }
        if (!this.regions_info.ready) return;

        this.context.publish(
            REGIONS_STORE_SHAPES,
            {config_id : this.regions_info.image_info.config_id});
    }

    /**
     * Show/Hide Text Labels
     *
     * @param {Object} event the mouse event object
     * @memberof Regions
     */
    showComments(event) {
        event.stopPropagation();
        event.preventDefault();

        if (!this.regions_info.ready) return false;

        this.regions_info.show_comments = event.target.checked;
        this.context.publish(
            REGIONS_SHOW_COMMENTS,
            {config_id : this.regions_info.image_info.config_id,
             value: this.regions_info.show_comments});
        return false;
    }

    /**
     * Undoes the last region modification
     *
     * @memberof Regions
     */
    undoHistory() {
        if (!this.regions_info.ready) return;

        this.regions_info.history.undoHistory();
    }

    /**
     * Redoes a previous region modification
     *
     * @memberof Regions
     */
    redoHistory() {
        if (!this.regions_info.ready) return;

        this.regions_info.history.redoHistory();
    }

    /**
     * Handles Click on ROI link button
     *
     * @memberof Regions
     */
    toggleShowRoiLink() {
        this.show_roi_link = !this.show_roi_link;
        if (this.show_roi_link) {
            setTimeout(() => {document.getElementById('roi_link_input').select()}, 0);
        }
    }

    /**
     * Used by template to provide link to currently selected ROIs
     *
     * @param {Array} shapes from regions_info.selected_shapes 'roi:shape' IDs
     * @memberof Regions
     */
    getRoisLink(shapes) {
        if (!shapes || shapes.length === 0) {
            return "No shapes selected";
        }
        // Only take first ROI:shape
        let shapeId = shapes[0].split(':')[1];
        // If unsaved, will have negative IDs. Return message instead
        if (shapeId < 0) {
            return "Can't link to unsaved ROIs";
        }
        return window.location.origin + this.context.getPrefixedURI(VIEWER) + '?shape=' + shapeId;
    }
}