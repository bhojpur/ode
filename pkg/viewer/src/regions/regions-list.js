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
import {inject,
    customElement,
    computedFrom,
    bindable,
    BindingEngine} from 'aurelia-framework';
import {
    REGIONS_SET_PROPERTY, EventSubscriber,
    IMAGE_DIMENSION_CHANGE,
    IMAGE_SETTINGS_CHANGE,
    IMAGE_DIMENSION_PLAY
} from '../events/events';

/**
 * Represents the regions list/table in the regions settings/tab
 * @extend {EventSubscriber}
 */
@customElement('regions-list')
@inject(Context, BindingEngine)
export default class RegionsList extends EventSubscriber {
    /**
     * a bound reference to regions_info
     * and its associated change handler
     * @memberof RegionsEdit
     * @type {RegionsInfo}
     */
    @bindable regions_info = null;
    regions_infoChanged(newVal, oldVal) {
        this.waitForRegionsInfoReady();
    }

    sortBy = "id";
    sortAscending = true;

    /**
     * the column showing (only one - mutually exclusive for now)
     * @memberof RegionsList
     * @type {number}
     */
     active_column = 'comments';

     /**
      * selected row (id) for multi-selection with shift
      * @memberof RegionsList
      * @type {string}
      */
      selected_row = '';

    /**
     * the list of property observers
     * @memberof RegionsList
     * @type {Array.<Object>}
     */
    observers = [];

    /**
     * the regions info ready observers
     * @memberof RegionsList
     * @type {Object}
     */
    regions_ready_observer = null;

    /**
     * Set this while range slider is sliding to show in UI
     * @memberof RegionsList
     * @type {number}
     */
    temp_sliding_page_number = undefined;

    /**
     * events we subscribe to
     * @memberof RegionsList
     * @type {Array.<string,function>}
     */
    sub_list = [
        [IMAGE_DIMENSION_CHANGE,
            (params={}) => this.changeDimension(params)],
        [IMAGE_SETTINGS_CHANGE,
            (params={}) => this.changeImageSettings(params)],
        [IMAGE_DIMENSION_PLAY,
            (params={}) => this.playImageDimension(params)],
    ];

    /**
     * @constructor
     * @param {Context} context the application context (injected)
     * @param {BindingEngine} bindingEngine the BindingEngine (injected)
     */
    constructor(context, bindingEngine) {
        super(context.eventbus);
        this.context = context;
        this.bindingEngine = bindingEngine;
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is bound within aurelia
     * in other words an 'init' hook that happens before 'attached'
     *
     * @memberof RegionsList
     */
    bind() {
        this.waitForRegionsInfoReady();
    }

    sort(value) {
        this.sortAscending = this.sortBy === value ? !this.sortAscending : true;
        this.sortBy = value;
    }

    sortCss(sortBy, sortAscending, attrName) {
        if (attrName !== sortBy) return 'sortable';
        return sortAscending ? 'sortable asc' : 'sortable desc';
    }

    /**
     * Gets page count for current plane.
     * Allows view to show page count and automatically binds change in
     * roi_count_on_current_plane
     */
    get pageCount() {
        return Math.ceil(this.regions_info.roi_count_on_current_plane/
                            this.regions_info.roi_page_size)
    }

    /**
     * Makes sure that all regions info data is there
     *
     * @memberof RegionsList
     */
    waitForRegionsInfoReady() {
        if (this.regions_info === null) return;

        let onceReady = () => {
            if (this.regions_info === null) return;
            // register observer
            this.registerObservers();
            // event subscriptions
            this.subscribe();
        };

        // tear down old observers
        this.unregisterObservers();
        if (this.regions_info.ready) {
            onceReady();
            return;
        }

        // we are not yet ready, wait for ready via observer
        if (this.regions_ready_observer === null)
            this.regions_ready_observer =
                this.bindingEngine.propertyObserver(
                    this.regions_info, 'ready').subscribe(
                        (newValue, oldValue) => onceReady());
    }

    /**
     * Registers property observers
     *
     * @memberof RegionsList
     */
    registerObservers() {
        this.observers.push(
            this.bindingEngine.collectionObserver(
                this.regions_info.selected_shapes).subscribe(
                    (newValue, oldValue) => this.actUponSelectionChange()));
        this.observers.push(
            this.bindingEngine.propertyObserver(
                this.regions_info, 'visibility_toggles').subscribe(
                    (newValue, oldValue) =>
                        $('#shapes_visibility_toggler').prop(
                            'checked', newValue === 0)));
    }

    /**
     * Unregisters the the observers (property and regions info ready)
     *
     * @param {boolean} property_only true if only property observers are cleaned up
     * @memberof RegionsList
     */
    unregisterObservers(property_only = false) {
        this.observers.map((o) => {if (o) o.dispose();});
        this.observers = [];
        if (property_only) return;
        if (this.regions_ready_observer) {
            this.regions_ready_observer.dispose();
            this.regions_ready_observer = null;
        }
    }

    /**
     * Scrolls to selected row
     *
     * @memberof RegionsList
     */
     actUponSelectionChange() {
         if (this.regions_info.selected_shapes.length === 0) return;

         let nrOfSelShapes = this.regions_info.selected_shapes.length;
         let idOflastEntry = this.regions_info.selected_shapes[nrOfSelShapes-1];
         let lastSelShape = this.regions_info.getLastSelectedShape();
         if (lastSelShape === null) return;
         let lastCanEdit =
            this.regions_info.checkShapeForPermission(lastSelShape, "canEdit");

         // exception: mixed permissions - don't scroll for canEdit=false
         if (idOflastEntry !== lastSelShape.shape_id && !lastCanEdit) return;

         Ui.scrollContainer(
             'roi-' + lastSelShape.shape_id, '.regions-table');
     }

    /**
     * Hide/Shows tegions table
     *
     * @memberof RegionsList
     */
    toggleRegionsTable() {
        if ($('.regions-list').is(':visible')) {
            $('.regions-list').hide();
            $('.regions-list-toggler').removeClass('collapse-up');
            $('.regions-list-toggler').addClass('expand-down');
        } else {
            $('.regions-list').show();
            $('.regions-list-toggler').removeClass('expand-down');
            $('.regions-list-toggler').addClass('collapse-up');
        }
    }

    /**
     * Set the pagination number of the ROI table and reload
     *
     * @param {number} zeroBasedPageNumber New page number
     */
    setPage(zeroBasedPageNumber=0) {
        this.temp_sliding_page_number = undefined;
        if (this.regions_info.is_pending) return;

        if (this.regions_info.hasBeenModified()) {
            Ui.showModalMessage(
                "You have unsaved ROI changes. Please Save or Undo " +
                "before going to next page.",
                "OK");
            return false;
        } else {
            return this.regions_info.setPageAndReload(zeroBasedPageNumber);
        }
    }

    /**
     * Listen for Z/T changes and re-load ROIs for the new plane if needed
     *
     * @param {Object} params Event params
     */
    changeDimension(params) {
        if (params.dim === "t" || params.dim == "z") {
            let image_info = this.regions_info.image_info;
            let image_config = this.context.getImageConfig(image_info.config_id);
            let last_plane = image_info.dimensions['max_' + params.dim] - 1;
            let dim_value = image_info.dimensions[params.dim];
            // If movie is playing and we're not on the last plane - ignore
            if (image_config.is_movie_playing && dim_value < last_plane &&
                    this.regions_info.isRoiLoadingPaginatedByPlane()) {
                this.regions_info.resetRegionsInfo();
                return;
            }
            this.requestRoisForNewPlane();
        }
    }

    /**
     * Listen for Z projection changes and re-load ROIs if needed
     * @param {Object} params Event params
     */
    changeImageSettings(params) {
        if (params.projection) {
            this.requestRoisForNewPlane();
        }
    }

    /**
     * Listen for movie 'stop' re-load ROIs if needed
     * @param {Object} params Event params
     */
    playImageDimension(params) {
        if (params.stop) {
            this.requestRoisForNewPlane();
        }
    }

    /**
     * IF we're filtering ROIs by current plane, reload ROIs after
     * resetting page to zero
     */
    requestRoisForNewPlane() {
        if (!this.regions_info.isRoiLoadingPaginatedByPlane()) {
            return;
        }
        // reset page and reload
        this.regions_info.roi_page_number = 0;
        this.regions_info.requestData(true);
    }

    /**
     * Handle change event for input range slider to choose page
     *
     * @param {object} event change event from pagination range input
     */
    handlePageRange(event) {
        let page = parseInt(event.target.value, 10);
        let changed = this.setPage(page);
        if (!changed) {
            // reset slider handle
            event.target.value = this.regions_info.roi_page_number;
        }
    }

    /**
     * Handle change event for input range slider to choose page
     *
     * @param {object} event change event from pagination range input
     */
    handlePageInput(event) {
        let page = parseInt(event.target.value, 10);
        if (isNaN(page)) {
            event.target.value = this.regions_info.roi_page_number + 1;
            return;
        }
        // UI input is 1-based, but we set 0-based
        let changed = this.setPage(page - 1);
        if (!changed) {
            // reset slider handle
            event.target.value = this.regions_info.roi_page_number + 1;
        }
    }

    /**
     * Handle slide event for input range slider to choose page
     *
     * @param {object} event change event from pagination range input
     */
    handlePageRangeSlide(event) {
        let page = parseInt(event.target.value, 10);
        this.temp_sliding_page_number = page;
    }

    /**
     * Select shapes handler
     *
     * @param {number} id the shape id
     * @param {boolean} selected the selected state
     * @param {Event} event the browser's event object
     * @param {boolean} is_rois_row is a roi containing shapes
     * @memberof RegionsList
     */
    selectShape(id, selected, event, is_rois_row=false) {
        if (event.target.tagName.toUpperCase() === 'INPUT' ||
            (is_rois_row &&
                (event.target.className.indexOf("roi_id") !== -1 ||
                event.target.parentNode.className.indexOf("roi_id") !== -1)))
                    return true;

        // see if cmd/ctrl or shift was used (for multiselection)
        let cmdKey = Misc.isApple() ? 'metaKey' : 'ctrlKey';
        let ctrl = typeof event[cmdKey] === 'boolean' && event[cmdKey];
        let shift = event.shiftKey;

        // initialize selection params
        id += '';
        let ids = [id];
        let multipleSelection = ctrl;
        let deselect = multipleSelection && selected;

        // rois rows
        let roi_shapes = [];
        let all_shapes_selected = false;
        if (is_rois_row) {
            let roi = this.regions_info.data.get(parseInt(id));
            if (typeof roi === 'object' && roi.shapes instanceof Map) {
                let selectableShapes = 0;
                let selectedShapes = 0;
                roi.shapes.forEach((s) => {
                    if (!(s.is_new && s.deleted)) {
                        selectableShapes++;
                        if (s.selected) selectedShapes++;
                        roi_shapes.push(s.shape_id);
                    }
                });
                all_shapes_selected = (selectedShapes >= selectableShapes);
            }
            if (multipleSelection && all_shapes_selected) deselect = true;
            else deselect = false;
            ids = roi_shapes;
        }

        if (!is_rois_row && this.selected_row.indexOf(':') !== -1 &&
            this.regions_info.selected_shapes.indexOf(
                this.selected_row) === -1) this.selected_row = '';
        if (shift && this.selected_row !== '' &&
            this.selected_row !== id) {
            deselect = false;
            // gather ids by means of row search
            let start = false;
            ids = [];
            let addRoiShapes = (el) => {
                let subId = el.id.substring("roi-".length);
                if (subId.indexOf(":") === -1) {
                    let roi = this.regions_info.data.get(parseInt(subId));
                    if (typeof roi === 'object' && roi.shapes instanceof Map) {
                        roi.shapes.forEach((s) => {
                            if (!(s.is_new && s.deleted)) ids.push(s.shape_id);
                        });
                    }
                } else ids.push(subId);
            }
            $('.regions-table-row').each((idx, el) => {
                if (el && typeof el.id === 'string' &&
                    el.id.indexOf('roi-') === 0) {
                    if ((el.id === 'roi-' + id) ||
                        (el.id === 'roi-' + this.selected_row)) {
                        addRoiShapes(el);
                        if (!start) start = true
                        else return false;
                    } else if (start) addRoiShapes(el);
                }
            });
        } else {
            let selLen = this.regions_info.selected_shapes.length;
            if (!deselect) this.selected_row = id;
            else if (deselect && this.selected_row === id && selLen > 0) {
                    this.selected_row =
                        this.regions_info.selected_shapes[selLen-1];
                    if (this.selected_row === id && selLen > 1)
                        this.selected_row =
                            this.regions_info.selected_shapes[selLen-2];
            }
        }

        this.context.publish(
           REGIONS_SET_PROPERTY, {
               config_id: this.regions_info.image_info.config_id,
               property: 'selected',
               shapes : ids, clear: !multipleSelection,
               value : !deselect, center : !multipleSelection});
    }

    /**
     * Select shapes for roi
     *
     * @param {number} roi_id the roi id
     * @param {Event} event the browser's event object
     * @memberof RegionsList
     */
    selectShapes(roi_id, event) {
        event.stopPropagation();
        if (event.target.className.indexOf("roi_id") !== -1 ||
            event.target.parentNode.className.indexOf("roi_id") !== -1)
                return true;

        let roi = this.regions_info.data.get(roi_id);
        if (typeof roi === 'undefined') return;

        let ids = [];
        roi.shapes.forEach((s) => ids.push(s.shape_id));
        this.context.publish(
           REGIONS_SET_PROPERTY, {
               config_id: this.regions_info.image_info.config_id,
               property: 'selected',
               shapes : ids, clear: true, value : true, center : true});
    }

    /**
     * ROI visibility toggler
     * Toggles the visibility of ALL shapes in the ROI
     *
     * @param {number} roi_id the roi id
     * @param {Object} event the mouse event object
     * @memberof RegionsList
     */
    toggleRoiVisibility(roi_id, event) {
        event.stopPropagation();
        event.preventDefault();
        let visible = event.target.checked;
        let roi = this.regions_info.data.get(roi_id);
        let shape_ids = [];
        roi.shapes.forEach((s) => {
            if (s.visible !== visible) {
                shape_ids.push(s.shape_id);
            }
        });
        if (shape_ids.length == 0) return;
        this.context.publish(
            REGIONS_SET_PROPERTY, {
                config_id: this.regions_info.image_info.config_id,
                property : "visible",
                shapes : shape_ids,
                value : visible});
        return false;
    }

    /**
     * shape visibility toggler
     *
     * @param {number} id the shape id
     * @param {Object} event the mouse event object
     * @memberof RegionsList
     */
    toggleShapeVisibility(id, event) {
        event.stopPropagation();
        event.preventDefault();
        this.context.publish(
           REGIONS_SET_PROPERTY, {
               config_id: this.regions_info.image_info.config_id,
               property : "visible",
               shapes : [id], value : event.target.checked});
        return false;
    }

    /**
     * Show/Hide shapes within roi
     *
     * @param {number} roi_id the roi id
     * @param {Event} event the browser's event object
     * @memberof RegionsList
     */
    expandOrCollapseRoi(roi_id, event) {
        event.stopPropagation();

        let roi = this.regions_info.data.get(roi_id);
        if (typeof roi === 'undefined') return;
        roi.show = !roi.show;
    }

    /**
     * Show/Hide all shapes
     *
     * @param {Object} event the mouse event object
     * @memberof RegionsList
     */
    toggleAllShapesVisibility(event) {
        // IMPORTANT (and enforced through a template show.bind as well):
        // we cannot have the initial state altered, the method of toggle diffs
        // won't work any more.
        event.stopPropagation();
        let show = event.target.checked;
        if (this.regions_info.number_of_shapes === 0) return;
        let ids = [];
        this.regions_info.data.forEach(
            (roi) =>
                roi.shapes.forEach(
                    (shape) => {
                        if (shape.visible !== show &&
                            !(shape.deleted &&
                            typeof shape.is_new === 'boolean' && shape.is_new))
                                ids.push(shape.shape_id);
                    })
        );
        this.context.publish(
           REGIONS_SET_PROPERTY, {
               config_id: this.regions_info.image_info.config_id,
               property : "visible",
               shapes : ids, value : show});
    }

    /**
     * Selects column (mutally exclusive for now)
     *
     * @param {string} which the column name
     * @memberof RegionsList
     */
    showColumn(which) {
        if (typeof which !== 'string' || which.length === 0 ||
            which === this.active_column) return;
        this.active_column = which;
    }

    /**
     * Evaluates whether permission(s) are missing for a given shape
     * (Helper method for template only!)
     *
     * @private
     * @param {Object} shape a shape
     * @return {boolean} if true user has full permissions, otherwise not
     * @memberof RegionsList
     */
    hasPermissions(shape) {
        return !(shape && shape.permissions) ||
           (shape.permissions &&
            shape.permissions.canAnnotate &&
            shape.permissions.canEdit &&
            shape.permissions.canDelete);
    }

    /**
     * Overridden aurelia lifecycle method:
     * called whenever the view is unbound within aurelia
     * in other words a 'destruction' hook that happens after 'detached'
     *
     * @memberof RegionsList
     */
    unbind() {
        this.unsubscribe();
        this.unregisterObservers();
    }
}