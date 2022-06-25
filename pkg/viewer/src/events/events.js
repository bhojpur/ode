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

/** whenever the image settings have been refreshed */
export const IMAGE_SETTINGS_REFRESH = "IMAGE_SETTINGS_REFRESH";
/** whenever the image canvas data was retrieved */
export const IMAGE_CANVAS_DATA = "IMAGE_CANVAS_DATA";
/** whenever pixel intensity querying should be turned on/off */
export const IMAGE_INTENSITY_QUERYING = "IMAGE_INTENSITY_QUERYING";
/** whenever the image viewer's controls need to be shown/hidden */
export const IMAGE_VIEWER_CONTROLS_VISIBILITY = "IMAGE_VIEWER_CONTROLS_VISIBILITY";
/** whenever the image viewer needs to be resized */
export const IMAGE_VIEWER_RESIZE = "IMAGE_VIEWER_RESIZE";
/** whenever an image viewer interaction (zoom/drag) ocurrs */
export const IMAGE_VIEWER_INTERACTION = "IMAGE_VIEWER_INTERACTION";
/** whenever the image rendering settings change: channel, model, projection*/
export const IMAGE_SETTINGS_CHANGE = "IMAGE_SETTINGS_CHANGE";
/** whenever the projection settings need to be synced*/
export const VIEWER_PROJECTIONS_SYNC = "VIEWER_PROJECTIONS_SYNC";
/** whenever an image dimension (c,t,z) changes */
export const IMAGE_DIMENSION_CHANGE = "IMAGE_DIMENSION_CHANGE";
/** whenever shape Comment change comes from the Image viewer */
export const IMAGE_COMMENT_CHANGE = "IMAGE_COMMENT_CHANGE";
/** whenever the dimension play should be started/stopped */
export const IMAGE_DIMENSION_PLAY = "IMAGE_DIMENSION_PLAY";
/** whenever the viewport canvas data should be captured */
export const IMAGE_VIEWPORT_CAPTURE = "IMAGE_VIEWPORT_CAPTURE";
/** whenever we want a link to current viewport */
export const IMAGE_VIEWPORT_LINK = "IMAGE_VIEWPORT_LINK";
/** to set rois/shape properties such as visibility and selection */
export const REGIONS_SET_PROPERTY = "REGIONS_SET_PROPERTY";
/** whenever a region property change is received, e.g. selection, modification */
export const REGIONS_PROPERTY_CHANGED = "REGIONS_PROPERTY_CHANGED";
/** whenever a new shape has been drawn */
export const REGIONS_DRAW_SHAPE = "REGIONS_DRAW_SHAPE";
/** whenever a new shape has been generated/drawn */
export const REGIONS_SHAPE_GENERATED = "REGIONS_SHAPE_GENERATED";
/** whenever the modes ought to be changed */
export const REGIONS_CHANGE_MODES = "REGIONS_CHANGE_MODES";
/** whenever comments ought to be shown/hidden */
export const REGIONS_SHOW_COMMENTS = "REGIONS_SHOW_COMMENTS";
/** whenever shape popup should be enabled/disabled */
export const ENABLE_SHAPE_POPUP = "ENABLE_SHAPE_POPUP";
/** whenever shapes ought to be generated */
export const REGIONS_GENERATE_SHAPES = "REGIONS_GENERATE_SHAPES";
/** whenever shapes are stored */
export const REGIONS_STORE_SHAPES = "REGIONS_STORE_SHAPES";
/** after modified/new/deleted shapes were stored */
export const REGIONS_STORED_SHAPES = "REGIONS_STORED_SHAPES";
/** whenever the styling of shapes is to be changed */
export const REGIONS_MODIFY_SHAPES = "REGIONS_MODIFY_SHAPES";
/** Retrieves the viewer image settings */
export const VIEWER_IMAGE_SETTINGS = "VIEWER_IMAGE_SETTINGS";
/** Sets the viewer's sync group */
export const VIEWER_SET_SYNC_GROUP = "VIEWER_SET_SYNC_GROUP";
/** whenever the ol3 viewer has made a history entry */
export const REGIONS_HISTORY_ENTRY = "REGIONS_HISTORY_ENTRY";
/** whenever we want to affect an ol3 viewer history undo/redo */
export const REGIONS_HISTORY_ACTION = "REGIONS_HISTORY_ACTION";
/** whenever we want to copy selected shape definitions */
export const REGIONS_COPY_SHAPES = "REGIONS_COPY_SHAPES";
/** whenever the histogram range was updated */
export const HISTOGRAM_RANGE_UPDATE = "HISTOGRAM_RANGE_UPDATE";
/** whenever thumbnails are supposed to be updated */
export const THUMBNAILS_UPDATE = "THUMBNAILS_UPDATE";
/** whenever the presently active image settings should be saved */
export const SAVE_ACTIVE_IMAGE_SETTINGS = "SAVE_ACTIVE_IMAGE_SETTINGS";

/**
 * Facilitates recurring event subscription
 * by providing subscribe and unsubscribe methods so that any class
 * that wishes use the eventbus just needs to inherit from it and
 * override/add to the sub_list array.
 *
 */
export class EventSubscriber {
    /**
     * our internal subscription handles (for unsubscribe)
     * @memberof EventSubscriber
     * @type {Array.<Object>}
     */
    subscriptions = [];
    /**
     * the list of events that we loop over to un/subscribe
     * consisting of an array with 2 entries:
     * the first one an EVENTS constant (see above) to denote the type
     * the second is a function/handler to be called, e.g.
     * [EVENTS.IMAGE_VIEWER_RESIZE, (params) => handleMe()]
     *
     * @memberof EventSubscriber
     * @type {Array.<string,function>}
     */
    sub_list = [];

    /**
     * @constructor
     * @param {EventAggregator} eventbus the event aggregator
     */
    constructor(eventbus) {
        this.eventbus = eventbus;
    }

    /**
     * Iterate over sub_list and subscribe to the given events
     * @memberof EventSubscriber
     */
    subscribe() {
        this.unsubscribe();
        this.sub_list.map(
            (value) => this.subscriptions.push(
                this.eventbus.subscribe(value[0], value[1])
            ));
    }

    /**
     * Iterate over sub_list and unsubscribe from all subscriptions
     * @memberof EventSubscriber
     */
    unsubscribe() {
        if (this.subscriptions.length === 0) return;
        while (this.subscriptions.length > 0)
            this.subscriptions.pop().dispose();
    }
}