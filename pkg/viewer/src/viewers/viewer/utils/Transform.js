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

import {isArray} from '../utils/Misc';

/**
 * Converts the AffineTransform Object into a flat transformation matrix
 *
 * @static
 * @param {Object} transform a transformation object with properties A00-A12
 * @return {Array.<number>} a transformation matrix as an array of length 6
 *                          or null if an error occured
 */
export const convertAffineTransformIntoMatrix = function(transform) {
    if (typeof transform !== 'object' ||
        transform === null || typeof transform['@type'] !== 'string' ||
        transform['@type'].indexOf("#AffineTransform") === -1) return null;

    try {
        return [
            transform['A00'], transform['A10'], transform['A01'],
            transform['A11'], transform['A02'], transform['A12']
        ];
    } catch(err) {
        console.error("failed to convert tranform into matrix");
    }
    return null;
}

/**
 * Gets the transformation as an AffineTransform object (Bhojpur ODE marshal 2016-06)
 * given a transformation matrix of length 6
 *
 * @static
 * @param {Array.<number>} transform the supposed transformation matrix
 * @return {Object|null} an AffineTransform object according to Bhojpur ODE marshal
 */
export const convertMatrixToAffineTransform = function(transform) {
    // check geometry for a flat transformation matrix of length 6
    if (!isArray(transform) || transform.length !== 6)
        return null;

    return {
        '@type': "http://www.bhojpur.net/Schemas/ODE/2016-06#AffineTransform",
        'A00' : transform[0], 'A10' : transform[1],
        'A01' : transform[2], 'A11' : transform[3],
        'A02' : transform[4], 'A12' : transform[5]
    };
}

/**
 * Applies a transformation to the given coordinates
 *
 * @static
 * @param {Array.<number>} transform a flat transformation matrix (length: 3 * 2)
 * @param {Array.<number>} coords a flat array of coordinates (n * (x|y) tuples)
 * @return {Array.<number>} an array of transformed coords
 */
export const applyTransform = function(transform, coords) {
    // preliminary checks
    if (!isArray(transform) || transform.length !== 6 ||
        !isArray(coords) || coords.length === 0 ||
        coords.length % 2 !== 0) return coords;

    var len = coords.length;
    var transCoords = new Array(len);
    for (var i=0;i<len;i+=2) {
        transCoords[i] = //x
            transform[0] * coords[i] +
                transform[2] * (-coords[i+1]) + transform[4];
        transCoords[i+1] = //y
            -(transform[1] * coords[i] +
                transform[3] * (-coords[i+1]) + transform[5]);
    }
    return transCoords;
}

/**
 * Performs an inverse transform (if exists) on some transformed coordinates
 * given a their original transformation matrix
 *
 * @static
 * @param {Array.<number>} transform a flat transformation matrix (length: 3 * 2)
 * @param {Array.<number>} coords a flat array of coordinates (n * (x|y) tuples)
 * @return {Array.<number>} an array of transformed coords equal in dimensinonality to the input
 */
export const applyInverseTransform = function(transform, coords) {
    // preliminary checks:
    if (!isArray(transform) || transform.length !== 6 ||
        !isArray(coords) || coords.length === 0 ||
        coords.length % 2 !== 0) return coords;

    // find matrix inverse hecking for determinant being 0 (trace)
    var det = transform[0] * transform[3] - transform[1] * transform[2];
    if (det === 0) return coords;
    var inverse = new Array(transform.length);
    var a = transform[0];
    var b = transform[1];
    var c = transform[2];
    var d = transform[3];
    var e = transform[4];
    var f = transform[5];
    inverse[0] = d / det;
    inverse[1] = -b / det;
    inverse[2] = -c / det;
    inverse[3] = a / det;
    inverse[4] = (c * f - d * e) / det;
    inverse[5] = -(a * f - b * e) / det;

    // multiply coordinates with inverted matrix
    var len = coords.length;
    var transCoords = new Array(len);
    for (var i=0;i<len;i+=2) {
        transCoords[i] = //x
            inverse[0] * coords[i] + inverse[2] * (-coords[i+1]) + inverse[4];
        transCoords[i+1] = //y
            -(inverse[1] * coords[i] + inverse[3] * (-coords[i+1]) + inverse[5]);
    }

    return transCoords;
}

/**
 * Takes the 3x2 transformation matrix and extracts the rotation and scaling
 *
 * @static
 * @param {Array.<number>} transform a flat transformation matrix (length: 3 * 2)
 * @return {Object|null} an object properties for scale_x, scale_y,
 *                       rot_deg and rot_rad or null (if invalid matrix)
 */
export const getRotationAndScaling = function(transform) {
    // preliminary checks:
    if (!isArray(transform) ||
        transform.length !== 6) return null;

    // extract scale factors for x/y
    var scale_x =
        Math.sqrt(
            transform[0] * transform[0] + transform[1] * transform[1]);
    var scale_y =
        Math.sqrt(
            transform[2] * transform[2] + transform[3] * transform[3]);
    // recover angle in radians
    var rot_rad = Math.atan2(transform[2],transform[3]);
    // see if scales are negative
    if ((rot_rad >= 0 && rot_rad <= Math.PI && transform[0] < 0) ||
        (rot_rad > Math.PI && rot_rad < Math.PI*2) && transform[0] > 0)
            scale_x *= -1;
    if ((rot_rad >= 0 && rot_rad <= Math.PI && transform[3] < 0) ||
        (rot_rad > Math.PI && rot_rad < Math.PI*2) && transform[3] > 0)
            scale_y *= -1;

    return {
        'scale_x': scale_x,
        'scale_y': scale_y,
        'rot_rad': rot_rad,
        'rot_deg': rot_rad * (180 / Math.PI)
    };
}