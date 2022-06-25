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

import {noView} from 'aurelia-framework';
import Misc from './misc';

/**
 * Open With Helper Class
 */
@noView
export default class OpenWith {

    /**
     * 'Initializes' OpenWith (global variable and function definitions)
     *
     * @static
     */
    static initOpenWith() {
        window.ODE = window.ODE || {};  // In case this ns exists already

        window.ODE.setOpenWithEnabledHandler = function(label, fn) {
            // look for label in OPEN_WITH
            OpenWith.OPEN_WITH.forEach(function(ow){
                if (ow.label === label) {
                    ow.isEnabled = function() {
                        // wrap fn with try/catch, since error here will break jsTree menu
                        var args = Array.from(arguments);
                        var enabled = false;
                        try {
                            enabled = fn.apply(this, args);
                        } catch (e) {
                            // Give user a clue as to what went wrong
                            console.log("Open with " + label + ": " + e);
                        }
                        return enabled;
                    }
                }
            });
        };

        window.ODE.setOpenWithUrlProvider = function(id, fn) {
            // look for label in OPEN_WITH
            OpenWith.OPEN_WITH.forEach(function(ow){
                if (ow.id === id) {
                    ow.getUrl = fn;
                }
            });
        };
    }

    /**
     * Fetches the open with scripts and stores them in OpenWith.OPEN_WITH
     *
     * @static
     * @param {string} server_prefix the server prefix
     */
    static fetchOpenWithScripts(server_prefix) {
        OpenWith.OPEN_WITH = [];
        $.ajax(
            {url : server_prefix + "/open_with/",
            success : (response) => {
                if (typeof response !== 'object' || response === null ||
                    !Misc.isArray(response.open_with_options)) return;

                OpenWith.OPEN_WITH = response.open_with_options;
                // Try to load scripts if specified:
                OpenWith.OPEN_WITH.forEach(ow => {
                    if (ow.script_url) {
                        $.getScript(ow.script_url);
                    }
                });
            }
        });
    }

    /**
     * Returns list of open with link parameters
     *
     * @static
     * @param {number} image_id the image id for the open_with_links
     * @param {string} image_name the image_name for the open_with_links
     * @param {string} viewer_url the prefixed Bhojpur ODE viewer url
     * @param {string} webgateway_url the prefixed webgateway url
     * @return {Array.<Object>} a list of link parameters or empty list
     */
    static getOpenWithLinkParams(image_id, image_name, viewer_url, webgateway_url) {

        return OpenWith.OPEN_WITH.map(v => {
            var selectedObjs = [{id: image_id,
                                 type: 'image',
                                 name: image_name}];
            var enabled = false;
            if (typeof v.isEnabled === "function") {
                enabled = v.isEnabled(selectedObjs);
            } else if (typeof v.supported_objects === "object" && v.supported_objects.length > 0) {
                enabled = v.supported_objects.reduce(function(prev, supported){
                    // enabled if plugin supports 'images' or 'image'
                    return prev || supported === 'images' || supported === 'image';
                }, false);
            }
            if (!enabled) return;

            // Ignore open_with -> viewer or webgateway viewer
            if (v.url.indexOf(viewer_url) === 0 ||
                v.url.indexOf(webgateway_url) == 0) return;

            var label = v.label || v.id;

            // Get the link via url provider...
            var the_url;
            try {
                the_url = v.getUrl(selectedObjs, v.url);
            }
            catch(err){}
            var url = the_url || v.url + '?image=' + image_id;

            return ({text: label, url: url});
        }).filter(l => l);
    }
}