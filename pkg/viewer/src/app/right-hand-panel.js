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
import {inject, customElement, BindingEngine} from 'aurelia-framework';
import Context from './context';
import {PROJECTION, TABS} from '../utils/constants';
import 'bootstrap';

/**
 * @classdesc
 *
 * the right hand panel
 */
@customElement('right-hand-panel')
@inject(Context, BindingEngine)
export class RightHandPanel {
    /**
     * expose TABS constants to template (no other way in aurelia)
     */
    TABS = TABS;

    /**
     * the selected image config
     * @memberof RightHandPanel
     * @type {ImageConfig}
     */
    image_config = null;

    /**
     * observer listening for image config changes
     * @memberof RightHandPanel
     * @type {Object}
     */
    observer = null;

    /**
     * @constructor
     * @param {Context} context the application context
     * @param {BindingEngine} bindingEngine the BindingEngine (injected)
     */
    constructor(context, bindingEngine) {
        this.context = context;
        this.bindingEngine = bindingEngine;
        // set initial image config
        // NB: This will likely be null initially
        this.image_config = this.context.getSelectedImageConfig();
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is bound within aurelia
     * in other words an 'init' hook that happens before 'attached'
     *
     * @memberof RightHandPanel
     */
    bind() {
        // listen for image config changes
        this.observer =
            this.bindingEngine.propertyObserver(
                this.context, 'selected_config').subscribe(
                    (newValue, oldValue) => {
                        this.image_config = this.context.getSelectedImageConfig();
                        // If multi-view mode (image data is already loaded),
                        // may need to load ROIs for selected image.
                        this.makeInitialRoisRequestIfRoisTabIsActive();
                });
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is unbound within aurelia
     * in other words a 'destruction' hook that happens after 'detached'
     *
     * @memberof RightHandPanel
     */
    unbind() {
        // get rid of observer
        if (this.observer) {
            this.observer.dispose();
            this.observer = null;
        }
    }

    /**
     * Overridden aurelia lifecycle method:
     * fired when PAL (dom abstraction) is ready for use
     *
     * @memberof RightHandPanel
     */
    attached() {
        $("#panel-tabs").find("a").click((e) => {
            e.preventDefault();
            this.context.selected_tab = e.currentTarget.hash.substring(1);
            this.makeInitialRoisRequestIfRoisTabIsActive();
            $(e.currentTarget).tab('show');
        });

        // If ROI ID is set, show ROIs tab:
        if (this.image_config && this.image_config.image_info &&
                (this.image_config.image_info.initial_roi_id || this.image_config.image_info.initial_shape_id)) {
            // This will trigger image_info to load ROIs once image data is loaded
            this.context.selected_tab = TABS.ROIS;
        }
    }

    /**
     * Requests rois if tab is active and rois have not yet been requested
     * and image_info is ready.
     *
     * @memberof RightHandPanel
     */
    makeInitialRoisRequestIfRoisTabIsActive() {
        if (this.context.isRoisTabActive()) {
            // If image_info is not yet ready (still loading) then ROIs will be
            // requested when image_info is loaded. We don't want to load ROIs
            // before image_info since we won't know image_info.roi_count
            // (don't know if we need to paginate ROIs)
            if (this.image_config === null ||
                !this.image_config.image_info.ready ||
                this.image_config.regions_info === null) return;

            this.image_config.regions_info.requestData();
        };
    }

    /**
     * Overridden aurelia lifecycle method:
     * fired when PAL (dom abstraction) is unmounted
     *
     * @memberof RightHandPanel
     */
    detached() {
        $("#panel-tabs").find("a").unbind("click");
    }
}