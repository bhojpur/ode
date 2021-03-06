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

import OlMap from 'ol/Map';
import {containsExtent} from 'ol/extent';
import Viewer from '../Viewer';
// import PROJECTION from '../globals';
// this is undefined when used below. TODO: Look at import order?
// TEMP workaround is to devine it again here:
const PROJECTION = {
    /** normal **/
    "NORMAL" : 'normal',
    /** intmax **/
    "INTMAX" : 'intmax'
};

/**
 * Generates an array of resolutions according to a step size
 * If one likes to think of it in terms of 100% (resolution: 1)
 * then the step size will determine how many levels there will be.
 * To cater for slightly different needs (i.e. tiled vs untiled sources)
 * a zoom_in step size and zoom_out step size can be given to result
 * in corresponding level numbers for above and below resolution 1.
 *
 * @static
 * @function
 * @param {number} zoom_in the increment for zoom_in
 * @param {number} zoom_out the increment for zoom_out
 */
export const generateDefaultResolutions = function(zoom_in, zoom_out) {
    // checks and clamps so as to not get an unreasonable number of
    // levels
    if (typeof zoom_in !== 'number' || zoom_in < 0.01 || zoom_in >= 1)
        zoom_in = 0.2;
    if (typeof zoom_out !== 'number' || zoom_out < 0.1 || zoom_out >= 5)
        zoom_out = 1;

    var resolutions = [];
    var zoom_out_levels = [];
    for (var z=1+zoom_out; z<5; z+=zoom_out)
        zoom_out_levels.push(z);
    resolutions = resolutions.concat(zoom_out_levels.reverse());
    resolutions.push(1);
    var zoom_in_levels = [];
    for (var z=1-zoom_in; z>0.01; z-=zoom_in)
        zoom_in_levels.push(z);
    resolutions = resolutions.concat(zoom_in_levels);

    return resolutions;
}

/**
 * This convenience method prepares the resolutions for pyramid and non-tiled
 * sources likewise. It makes use of {@link DEFAULT_RESOLUTIONS} to achieve a
 * certain number of zoom levels
 *
 * @static
 * @function
 * @param {?Array} resolutions the resolutions array or null
 */
export const prepareResolutions = function(resolutions) {
    if (!isArray(resolutions) ||
        resolutions.length === 0 ||
        (resolutions.length === 1 && resolutions[0] === 1))
            return generateDefaultResolutions(0.025, 0.10);

    // for tiled sources we find the 1:1, then go backwards in the array
    // filling up with levels for zoom out
    var newRes = [1];
    var oneToOneIndex = resolutions.indexOf(1.0);
    if (oneToOneIndex === -1) resolutions.push(1.0);
    // make sure we are sorted and in reverse
    resolutions.sort((a,b) => b - a);
    oneToOneIndex = resolutions.indexOf(1.0);
    var p = oneToOneIndex > 0 ? oneToOneIndex : resolutions.length-1;
    for (var i=p; i>0;i--) {
        var resAtI = resolutions[i];
        var resBefI = resolutions[i-1];
        var delta = Math.abs(resBefI - resAtI);
        // we divide up into 8 levels in between, i.e. 12.5% of the original delta
        var partialDelta = delta * 0.125;
        for (var j=1;j<=8;j++) newRes.push(resAtI + j * partialDelta);
    }
    // append zoom in factors (if present, unlikely with tiled)
    if (oneToOneIndex < resolutions.length-1)
        for (var x=oneToOneIndex+1;x<resolutions.length;x++)
            newRes.push(resolutions[x]);

    // now we fill up zoom in and out positions
    // for a total number of resolutions
    newRes.sort((a,b) => b - a);
    var totalNumberOfLevels = newRes.length === 1 ? 20 : newRes.length + 10;
    var remainingPositions = totalNumberOfLevels - newRes.length;
    //we alternate starting at which end has fewer zoom level
    var insertFront = true;
    if (newRes.length - oneToOneIndex < oneToOneIndex) insertFront = false;
    while (remainingPositions > 0) {
        if (insertFront) {
            insertFront=false;
            newRes.splice(0, 0, newRes[0] * 1.20);
        } else {
            insertFront=true;
            var newVal = newRes[newRes.length-1] / 1.10;
            newRes.push(newVal);
        }
        remainingPositions--;
    }

    return newRes;
};


/**
 * Deals with multiple features under the same coordinate
 *
 * @static
 * @param {Array.<ol.Feature>} features the features found under the coordinate
 */
export const featuresAtCoords = function(features) {
    if (!isArray(features) || features.length ===0) return null;

    // determine priority of whih feature ought to be returned
    var filteredIntersectingFeatures = [];
    for (var i in features)
        if (filteredIntersectingFeatures.length > 0) {
            // this should ensure that if a feature is contained by another
            // it will be always ranked first
            var firstSuchGeometry= filteredIntersectingFeatures[0].getGeometry();
            if (containsExtent( // we have a feature that is contained by our first feature
                        firstSuchGeometry.getExtent(),
                        features[i].getGeometry().getExtent()))
                filteredIntersectingFeatures[0] = features[i]; // replace it
        } else filteredIntersectingFeatures.push(features[i]);

    return filteredIntersectingFeatures.length > 0 ?
        filteredIntersectingFeatures[0] : null;
};


/**
 * Returns all classes associated with the html element
 *
 * @static
 * @param {Object} element an html element
 * @return {Array.<string>|null} an array of classes or null
 */
export const getClass = function(element) {
    if (typeof(element) !== 'object' ||
        typeof(element['className']) !== 'string') return null;

    var classNames = element['className'];

    return classNames.match(/\S+/g) || null;
}

/**
 * Sets a class for an element
 *
 * @static
 * @param {Object} element an html element
 * @param {string} className a class
 */
export const setClass = function(element, className) {
    if (typeof(element) !== 'object' ||
        typeof(element['className']) !== 'string' ||
        typeof(className) !== 'string') return;

    element['className'] = className;
}

/**
 * Checks whethere a given html element has a given class associated with it
 *
 * @static
 * @param {Object} element an html element
 * @param {string} className a class
 * @return {boolean} true if the element has the class on it, false otherwise
 */
export const containsClass = function(element, className) {
    if (typeof(className) !== 'string' || className.length === 0) return false;

    var allClasses = getClass(element);
    if (allClasses === null) return false;

    return allClasses.indexOf(className) >= 0;
}

/**
 * Checks if something is an array
 *
 * @static
 * @param {string} something a potential array
 * @return {boolean} true if something is an array, otherwise false
 */
export const isArray = function(something) {
   if (typeof something !== 'object' || something === null) return false;

   if (something instanceof Array ||
       Object.prototype.toString.call(null, something) === '[object Array]')
           return true;

   return false;
}

/**
 * Finds cookie (if exists) matching the given name
 *
 * @static
 * @param {string} name the name of the cookie
 * @return {string} the cookie's value
 */
export const getCookie = function(name) {
    if (typeof(name) != 'string') return "";

    var all = document.cookie.split(';');
    for(var i=0, ii = all.length;i<ii; i++) {
        var cookie = all[i];
        while (cookie.charAt(0)==' ') cookie = cookie.substring(1);
        if (cookie.indexOf(name + '=') == 0)
            return cookie.substring(name.length+1,cookie.length);
    }
    return "";
};

/**
 * Takes a string with channel information in the form:
 * -1|111:343$808080,2|0:255$FF0000 and parses it into an object that contains
 * the respective channel information/properties
 * Likewise it will take map information in json format
 * and add the inverted flag to the channels
 *
 * @static
 * @function
 * @param {string} channel_info a string containing encoded channel info
 * @param {string} maps_info a string in json format containing inverted flag
 * @return {Array|null} an array of channel objects or null
 */
export const parseChannelParameters = function(channel_info, maps_info) {
    if (typeof channel_info !== 'string' || channel_info.length === 0)
        return null;

    var ret = [];

    // first remove any whitespace there may be
    channel_info = channel_info.replace(/\s/g, "");

    // split up into channels
    var chans = channel_info.split(',');
    if (chans.length === 0) return null;

    // iterate over channel tokens
    for (var k in chans) {
        // extract channel number
        var c = chans[k];
        var pos = c.indexOf('|');
        if (pos === -1) continue;

        var chanNum = parseInt(c.substring(0, pos));
        if (isNaN(chanNum)) continue;

        var tmp = {
            'index' : chanNum < 0 ? (-chanNum)-1 : chanNum-1,
            'active' : chanNum < 0 ? false : true
        };

        // extract range token
        c = c.substring(pos+1); // shave off number info
        pos = c.indexOf("$");
        if (pos === -1) continue;
        var rTok = c.substring(0, pos).split(':');
        if (rTok.length !== 2) continue; // we need start and end
        var rStart = parseInt(rTok[0]);
        var rEnd = parseInt(rTok[1]);
        if (isNaN(rStart) || isNaN(rEnd)) continue;
        tmp['start'] = rStart;
        tmp['end'] = rEnd;
        // extract last bit: color tokens
        c = c.substring(pos+1); // shave off range info
        tmp['color'] = c;

        // add to return
        ret.push(tmp);
    }

    // integrate maps info for inverted flag
    if (typeof maps_info !== 'string' || maps_info.length === 0)
        return ret;
    try {
        maps_info = maps_info.replace(/&quot;/g, '"');
        var maps = JSON.parse(maps_info);
        if (!isArray(maps)) return ret;
        var len = ret.length;
        for (var i=0;i<len && i<maps.length;i++) {
            var m = maps[i];
            if (typeof m !== 'object' || m === null) continue;
            ret[i]['inverted'] =
                typeof m['inverted'] === 'object' && m['inverted'] &&
                typeof m['inverted']['enabled'] === 'boolean' &&
                m['inverted']['enabled'];
            if (typeof m['family'] === 'string' && m['family'] !== "" &&
                typeof m['coefficient'] === 'number' &&
                !isNaN(m['coefficient'])) {
                    ret[i]['family'] = m['family'];
                    ret[i]['coefficient'] = m['coefficient'];
            }
        }
    } catch(malformedJson) {}

    return ret;
}

/**
 * Takes a string with projection information of the form 'normal or intmax|0:2'
 * and returns an object containing the projection type as well as additional
 * projection information such as start/end
 *
 * @static
 * @function
 * @param {string} projection_info a string containing the projection info
 * @return {Object} an object containing the parsed projection info
 */
export const parseProjectionParameter = function(projection_info) {
    var ret = {
        projection: PROJECTION['NORMAL']
    };
    if (typeof projection_info !== 'string' || projection_info.length === 0)
        return ret;

    var pipePos = projection_info.indexOf('|');
    if (pipePos !== -1) {
        ret.projection = projection_info.substring(0, pipePos);
        var tokens = projection_info.substring(pipePos+1).split(":");
        if (tokens.length === 2) {
            ret.start = parseInt(tokens[0]);
            ret.end = parseInt(tokens[1]);
        }
    } else ret.projection = projection_info;

    // last sanity check before returning
    if (ret.projection !== PROJECTION['NORMAL'] &&
        ret.projection !== PROJECTION['INTMAX'])
            ret.projection = PROJECTION['NORMAL'];

    return ret;
}

/**
 * Sends out an event notification
 *
 * @static
 * @param {Viewer} viewer an instance of the Viewer
 * @param {string} type the event type
 * @param {Object=} content the event content as an object (optional)
 * @param {number=} delay delay for sending (optional)
 */
export const sendEventNotification = function(viewer, type, content, delay) {
    if (!(viewer instanceof Viewer) ||
        !(viewer.viewer_ instanceof OlMap) ||
        viewer.prevent_event_notification_ ||
        typeof type !== 'string' ||
        type.length === 0) return;

    var config_id = getTargetId(viewer.viewer_.getTargetElement());
    var eventbus = viewer.eventbus_;
    if (config_id && eventbus) { // publish
        if (typeof content !== 'object' || content === null) content = {};
        content['config_id'] = config_id;
        content['sync_group'] = viewer.sync_group_;
        var triggerEvent = function() {
            eventbus.publish(type, content);
        };
        if (typeof delay === 'number' && delay > 0)
            setTimeout(triggerEvent, delay);
        else triggerEvent();
    }
}

/**
 * Extracts the id which is part of the viewer's target element id
 * e.g. xxxxx_344455. In a standalone ol3 setup there won't be a number
 * but just an id
 *
 * @static
 * @param {Element|string} target the viewer's target element
 * @return {number|string|null} the target element's id as a number/string
 *                              (latter: for ol3 alone) or null
 *                              (no element id or parse error)
 */
export const getTargetId = function(target) {
    try {
        var elemId =
            typeof target === 'string' ? target :
                typeof target === 'object' &&
                    target !== null && typeof target.id === 'string' ?
                        target.id : null;
        if (elemId === null) return null;

        var pos = elemId.lastIndexOf("_");
        if (pos === -1) return elemId;

        var id = parseInt(elemId.substring(pos+1));
        if (isNaN(id)) return null;

        return id;
    } catch(no_care) {
        return null;
    }
}