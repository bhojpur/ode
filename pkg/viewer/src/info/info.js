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
import OpenWith from '../utils/openwith';

import {inject, customElement, bindable, BindingEngine} from 'aurelia-framework';
import {INITIAL_TYPES, WEBCLIENT, VIEWER, WEBGATEWAY} from '../utils/constants';

@customElement('info')
@inject(Context, BindingEngine)
export class Info {

    /**
     * a reference to the image info  (bound in template)
     * @memberof Info
     * @type {ImageInfo}
     */
    @bindable image_info = null;

    /**
     * the open with links
     * @memberof Info
     * @type {Array.<>}
     */
    open_with_links = [];

    /**
     * the associated dataset info
     * @memberof Info
     * @type {Object}
     */
    dataset_info = null;

    /**
     * the list of observers
     * @memberof Info
     * @type {Array.<Object>}
     */
    observers = [];

    /**
     * Values to display
     * @memberof Info
     */
    columns = null;

    /**
     * @constructor
     * @param {Context} context the application context (injected)
     * @param {BindingEngine} bindingEngine the BindingEngine (injected)
     */
    constructor(context, bindingEngine) {
        this.context = context;
        this.bindingEngine = bindingEngine;
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is bound within aurelia
     * in other words an 'init' hook that happens before 'attached'
     *
     * @memberof Info
     */
    bind() {
        let changeImageConfig = () => {
            if (this.image_info === null) return;

            if (this.image_info.ready) {
                this.onImageConfigChange();
                return;
            }
            // we are not yet ready, wait for ready via observer
            if (this.observers.length > 1 &&
                typeof this.observers[1] === 'object' &&
                this.observers[1] !== null) this.observers.pop().dispose();
            ['ready', 'parent_type'].map(
                (p) => {
                    this.observers.push(
                        this.bindingEngine.propertyObserver(
                            this.image_info, p).subscribe(
                                (newValue, oldValue) => this.onImageConfigChange()));
                });
        };
        // listen for image info changes
        this.observers.push(
            this.bindingEngine.propertyObserver(
                this, 'image_info').subscribe(
                    (newValue, oldValue) => changeImageConfig()));
        // initial image config
        changeImageConfig();
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is unbound within aurelia
     * in other words a 'destruction' hook that happens after 'detached'
     *
     * @memberof Info
     */
    unbind() {
        // get rid of observers
        this.observers.map((o) => {
            if (o) o.dispose();
        });
        this.observers = [];
        this.columns = [];
    }

    /**
     * Handles changes of the seleccted ImageConfig
     *
     * @memberof Header
     */
    onImageConfigChange() {
        if (this.image_info === null) return;

        let pixels_size = "";
        let pixels_size_label = "Pixels Size (XYZ)";
        if (typeof this.image_info.image_pixels_size === 'object' &&
            this.image_info.image_pixels_size !== null) {
            let symbol = '\xB5m'; // microns by default
            let unit = 'MICROMETER';
            let count = 0;
            let set = false;
            if (typeof this.image_info.image_pixels_size.unit_x == 'number') {
                pixels_size += this.image_info.image_pixels_size.unit_x.toFixed(2);
                symbol = this.image_info.image_pixels_size.symbol_x;
                set = true;
            } else {
               pixels_size += "-";
               count++;
            }
            pixels_size += " x ";
            if (typeof this.image_info.image_pixels_size.unit_y == 'number') {
                pixels_size += this.image_info.image_pixels_size.unit_y.toFixed(2);
                if (!set) {
                    symbol = this.image_info.image_pixels_size.symbol_y;
                    set = true;
                }
            } else {
                pixels_size += "-";
                count++;
            }
            pixels_size += " x ";
            if (typeof this.image_info.image_pixels_size.unit_z == 'number') {
                pixels_size += this.image_info.image_pixels_size.unit_z.toFixed(2);
                if (!set) {
                    symbol = this.image_info.image_pixels_size.symbol_z;
                    set = true;
                }
            } else {
                pixels_size += "-";
                count++;
            }
            if (count === 3) {
                pixels_size = "Not available"
            } else {
                if (set) {
                     pixels_size_label += " ("+symbol+")";
                }
            }
        }
        pixels_size_label += ":";
        let size_xy = this.image_info.dimensions.max_x+" x "+this.image_info.dimensions.max_y
        this.columns = [
            {"label": "Owner:",
             "value": this.image_info.author,
            }
        ];
        if (this.image_info.acquisition_date) {
            this.columns.push(
                {"label": "Acquisition Date:",
                 "value": this.image_info.acquisition_date});
        };
        this.columns = this.columns.concat([
            {"label": "Import Date:",
             "value": this.image_info.import_date
            },
            {"label": "Dimension (XY):",
             "value": size_xy
            },
            {"label": "Pixels Type:",
             "value": this.image_info.image_pixels_type
            },
            {"label": pixels_size_label,
             "value": pixels_size
            },
            {"label": "Z-sections:",
             "value": this.image_info.dimensions.max_z
            },
            {"label": "Timepoints:",
             "value": this.image_info.dimensions.max_t
            }
        ]);
        if (typeof this.image_info.parent_id === 'number') {
            let isWell =
                this.context.initial_type === INITIAL_TYPES.WELL ||
                this.image_info.parent_type === INITIAL_TYPES.WELL;
            this.parent_info = {
                title: isWell ? "Well" : "Dataset",
                url: this.context.server +
                        this.context.getPrefixedURI(WEBCLIENT) +
                        '/?show=' + (isWell ? 'well' : 'dataset') +
                        '-' + this.image_info.parent_id,
                name: isWell ?
                        this.image_info.parent_id :
                        this.image_info.dataset_name === 'Multiple' ?
                            this.image_info.parent_id :
                            this.image_info.dataset_name
            }
        } else this.parent_info = null;

        this.updateOpenWithLinks();
    }

    /**
     * Updates open with links
     *
     * @memberof Header
     */
     updateOpenWithLinks() {
         let image_id = this.image_info.image_id;
         let image_name = this.image_info.image_name;
         let viewer_url = this.image_info.context.getPrefixedURI(VIEWER);
         let webgateway_url = this.image_info.context.getPrefixedURI(WEBGATEWAY);

         this.open_with_links.splice(
             0, this.open_with_links.length,
             ...OpenWith.getOpenWithLinkParams(image_id, image_name, viewer_url, webgateway_url));
     }
}