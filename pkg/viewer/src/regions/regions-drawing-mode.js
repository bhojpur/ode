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
import {Utils} from '../utils/regions';
import {Converters} from '../utils/converters';
import {REGIONS_DRAWING_MODE} from '../utils/constants';
import {REGIONS_GENERATE_SHAPES} from '../events/events';
import {inject, customElement, bindable} from 'aurelia-framework';

/**
 * Represents the regions drawing mode section in the regions settings/tab
 */
@customElement('regions-drawing-mode')
@inject(Context)
export default class RegionsDrawingMode {
    /**
     * a reference to the image config
     * @memberof RegionsDrawingMode
     * @type {RegionsInfo}
     */
    @bindable regions_info = null;

    /**
     * @constructor
     * @param {Context} context the application context (injected)
     */
    constructor(context) {
        this.context = context;
    }

    /**
     * Hide/Shows regions drawing mode section
     *
     * @memberof RegionsDrawingMode
     */
    toggleRegionsDrawingMode() {
        if ($('.regions-drawing-mode').is(':visible')) {
            $('.regions-drawing-mode').hide();
            $('.regions-drawing-mode-toggler').removeClass('collapse-up');
            $('.regions-drawing-mode-toggler').addClass('expand-down');
        } else {
            $('.regions-drawing-mode').show();
            $('.regions-drawing-mode-toggler').removeClass('expand-down');
            $('.regions-drawing-mode-toggler').addClass('collapse-up');
        }
    }

    /**
     * Clears information
     * @memberof RegionsDrawingMode
     * @param {Element} target the target element (input)
     */
    onDimensionInputFocus(target) {
        if (target.value.indexOf("Enter as") !== -1) {
            target.value = '';
            target.style = '';
        }
    }

    /**
     * Handles z/t propagation changes, typed in by the user
     * @memberof RegionsDrawingMode
     * @param {string} dim the dimension: 't' or 'z'
     * @param {string} value the input value
     */
    onDimensionInputChange(dim, value) {
        if (dim !== 't' && dim !== 'z') return;

        this.regions_info.drawing_dims[dim] =
            Utils.parseDimensionInput(
                value, this.regions_info.image_info.dimensions['max_' + dim]);
    }

    /**
     * Handler for dimension attachment changes
     * @memberof RegionsDrawingMode
     * @param {number} option the chosen attachment option
     */
    onAttachmentOptionChange(option) {
        this.regions_info.drawing_mode = option;
        $('.regions-attachment-choice').html(
            $(".regions-attachment-option-" + option).text());
        if (option !== REGIONS_DRAWING_MODE.CUSTOM_Z_AND_T) {
            let inputs = $('.regions-attachment-options [type="input"]');
            inputs.val('Enter as 4-9 or 3,9,11...');
            inputs.css({
                "filter": "alpha(opacity=65)",
                "opacity": ".65",
                "-webkit-box-shadow": "none",
                "box-shadow": "none"});
        }
        return true;
    }

    /**
     * Propagate selected shapes using chosen drawing modes
     *
     * @memberof RegionsDrawingMode
     */
    propagateSelectedShapes() {
        let hist_id = this.regions_info.history.getHistoryId();
         this.regions_info.selected_shapes.map(
             (id) => {
                 let shape =
                     Object.assign({}, this.regions_info.getShape(id));
                 // collect dimensions for propagation
                 let theDims =
                     Utils.getDimensionsForPropagation(
                         this.regions_info, shape.TheZ, shape.TheT);
                 if (theDims.length > 0)
                     this.context.publish(
                         REGIONS_GENERATE_SHAPES,
                         {
                             config_id: this.regions_info.image_info.config_id,
                             shapes: [shape],
                             number : theDims.length,
                             roi_id: Converters.extractRoiAndShapeId(id).roi_id,
                             hist_id: hist_id,
                             theDims : theDims
                         });
             });
    }
}