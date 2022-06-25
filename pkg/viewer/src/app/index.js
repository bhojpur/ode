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

// images
require('../../node_modules/bootstrap/fonts/glyphicons-halflings-regular.woff');
require('../../node_modules/jquery-ui/themes/base/images/ui-icons_777777_256x240.png');
require('../../node_modules/jquery-ui/themes/base/images/ui-icons_555555_256x240.png');
require('../../node_modules/jquery-ui/themes/base/images/ui-icons_ffffff_256x240.png');
require('../../node_modules/jquery-ui/themes/base/images/ui-icons_444444_256x240.png');

// js
import {inject} from 'aurelia-framework';
import Context from './context';
import Misc from '../utils/misc';
import OpenWith from '../utils/openwith';
import Ui from '../utils/ui';
import {PLUGIN_PREFIX, SYNC_LOCK, WEBGATEWAY} from '../utils/constants';
import {IMAGE_VIEWER_RESIZE, IMAGE_VIEWER_CONTROLS_VISIBILITY,
        REGIONS_STORE_SHAPES, REGIONS_STORED_SHAPES} from '../events/events';

/**
 * @classdesc
 *
 * The index view/page so to speak
 */
@inject(Context)
export class Index  {
    /**
     * the handle on the the resize function (from setTimeout)
     * @memberof Index
     * @type {number}
     */
    resizeHandle = null;

    /**
     * a prefix for the full screen api methods
     * @memberof Index
     * @type {string}
     */
    full_screen_api_prefix = null;

    /**
     * @memberof Index
     * @type {boolean}
     */
    restoreMDI = false;

    /**
     * array of possible sync locks
     * @memberof Index
     * @type {Array.<Object>}
     */
    sync_locks = [
        SYNC_LOCK.ZT,
        SYNC_LOCK.VIEW,
        SYNC_LOCK.CHANNELS
    ];

    /**
     * @constructor
     * @param {Context} context the application context
     */
    constructor(context) {
        this.context = context;
    }

    /**
     * Overridden aurelia lifecycle method:
     * fired when PAL (dom abstraction) is ready for use
     *
     * @memberof Index
     */
    attached() {
        // listen to resizing of the browser window
        let publishResize =
            () => this.context.publish(
                IMAGE_VIEWER_RESIZE,
                {config_id: -1, window_resize: true});
        window.onresize = () => {
            if (typeof this.resizeHandle !== null) {
                clearTimeout(this.resizeHandle);
                this.resizeHandle = null;
            }
            this.resizeHandle = setTimeout(publishResize, 50);
        };
        window.onbeforeunload = () => {
            if (Misc.useJsonp(this.context.server)) return null;
            let hasBeenModified = false;
            for (var [k,v] of this.context.image_configs) {
                if (v && v.regions_info && v.regions_info.hasBeenModified() &&
                    v.regions_info.image_info.can_annotate) {
                    hasBeenModified = true;
                    break;
                }
            }
            if (hasBeenModified)
                return "You have new/deleted/modified ROI(s).\n" +
                        "If you leave you'll lose your changes.";
            return null;
        };
        // register resize and collapse handlers
        Ui.registerSidePanelHandlers(
            this.context.eventbus,
            this.context.getPrefixedURI(PLUGIN_PREFIX, true),
        );

        // register the fullscreenchange handler
        this.full_screen_api_prefix = Ui.getFullScreenApiPrefix();
        if (this.full_screen_api_prefix &&
            typeof document['on' +
                this.full_screen_api_prefix + 'fullscreenchange'] !== 'function')
                document['on' +
                    this.full_screen_api_prefix + 'fullscreenchange'] =
                        () => this.onFullScreenChange();

        // fetch open with scripts
        OpenWith.fetchOpenWithScripts(
            this.context.server + this.context.getPrefixedURI(WEBGATEWAY));
    }

    /**
     * Closes viewer in MDI mode checking whether there is a need
     * to store
     *
     * @memberof Index
     */
    closeViewerInMDI(id) {
        if (!this.context.useMDI) return;

        let conf = this.context.getImageConfig(id);
        if (conf === null || conf.regions_info === null ||
            !conf.regions_info.hasBeenModified() ||
            Misc.useJsonp(this.context.server) ||
            !conf.regions_info.image_info.can_annotate) {
                this.context.removeImageConfig(id);
                return;
            };

            let saveHandler = () => {
                let tmpSub =
                    this.context.eventbus.subscribe(
                        REGIONS_STORED_SHAPES,
                        (params={}) => {
                            tmpSub.dispose();
                            this.context.removeImageConfig(id);
                    });
                setTimeout(()=>
                    this.context.publish(
                        REGIONS_STORE_SHAPES,
                        {config_id : conf.id,
                         omit_client_update: true}), 20);
            };
            Ui.showConfirmationDialog(
                'Save ROIs?',
                'You have new/deleted/modified ROI(s).<br>' +
                'Do you want to save your changes?',
                saveHandler, () => this.context.removeImageConfig(id));
    }

    /**
     * Adds/Removes the given image config to the sync group
     * @param {number} id the image config id
     * @param {string|null} group the sync group or null
     * @memberof Index
     */
    toggleSyncGroup(id, group=null) {
        let conf = this.context.getImageConfig(id);
        if (conf === null) return;

        conf.toggleSyncGroup(group);
        this.context
    }

    /**
     * Visually highlights image configs contained in group
     * @param {Array.<number>} group the group containing the image configs
     * @param {boolean} flag if true we highlight, otherwise not
     * @memberof Index
     */
    highlightSyncGroup(group = [], flag) {
        for (let g in group)
            $('#' + group[g]).css("border-color", flag ? "yellow" : "");
    }

    /**
     * Turns on/off syncing locks
     *
     * @param {number} id the image config id
     * @param {string} lock the sync lock, e.g. z, t, c or v
     * @param {boolean} flag if true the lock is applied, otherwise not
     * @memberof Index
     */
    toggleSyncLock(id, lock = 'c', flag) {
        let conf = this.context.getImageConfig(id);
        if (conf === null || conf.sync_group === null) return;
        let syncGroup = this.context.sync_groups.get(conf.sync_group);
        if (syncGroup) {
            syncGroup.sync_locks[lock] = flag;
        }
    }

    /**
     * Shows/Hides viewer controls
     *
     * @param {number} id the image config id
     * @memberof Index
     */
    toggleViewerControlsInMDI(id) {
        let conf = this.context.getImageConfig(id);
        if (conf === null) return;

        this.context.publish(
            IMAGE_VIEWER_CONTROLS_VISIBILITY,
            {config_id : conf.id, flag: !conf.show_controls});
    }

    /**
     * Handles fullscreen changes
     *
     * @memberof Index
     */
    onFullScreenChange() {
        let isInFullScreen =
            document[this.full_screen_api_prefix + 'isFullScreen'] ||
            document[this.full_screen_api_prefix + 'FullScreen'] ||
            document[this.full_screen_api_prefix + 'FullscreenElement'];

        if (isInFullScreen) {
            this.restoreMDI = this.context.useMDI;
            if (this.restoreMDI) this.context.useMDI = false;
        } else {
            if (this.restoreMDI) this.context.useMDI = true;
        }
        $(".viewer-context-menu").hide();
    }

    /**
     * Handle dropping of a thumbnail on to the centre panel.
     * Opens the image in Multi-Display mode
     *
     * @param {Object} event Drop event
     */
    handleDrop(event) {
        var image_id = parseInt(event.dataTransfer.getData("id"), 10);
        this.context.useMDI = true;
        // similar behaviour to double-clicking
        this.context.onClicks(image_id, true);
    }

    /**
     * Simply preventDefault() to allow drop here
     *
     * @param {Object} event Dragover event
     */
    handleDragover(event) {
        event.preventDefault();
    }

    /**
     * Overridden aurelia lifecycle method:
     * called when the view and its elemetns are detached from the PAL
     * (dom abstraction)
     *
     * @memberof Index
     */
    detached() {
        window.onresize = null;
        window.onbeforeunload = null;
        if (this.full_screen_api_prefix !== null &&
            document['on' +
                this.full_screen_api_prefix + 'fullscreenchange']) {
                document['on'
                    + this.full_screen_api_prefix + 'fullscreenchange'] = null;
        }
    }
}