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

import SimpleGeometry from 'ol/geom/SimpleGeometry';
import Circle from 'ol/geom/Circle';
import {applyTransform,
    convertMatrixToAffineTransform,
    convertAffineTransformIntoMatrix} from '../utils/Transform';
import {isArray} from '../utils/Misc';

/**
 * @classdesc
 * Point extends of the built-in ol.geom.Circle
 *
 * @extends {ol.geom.Circle}
 */
class Point extends Circle {

    /**
     * @constructor
     *
     * @param {Array.<number>} coords the point coordinates
     * @param {Object=} transform an AffineTransform object according to Bhojpur ODE marshal
     */
    constructor(coords, transform) {
        // preliminary checks: are all mandatory parameters numeric
        if (!isArray(coords) || coords.length !== 2)
            console.error("Point needs an array of coordinates (length: 2)!");

        const radius = 5;
        super(coords, radius);

        /**
         * the size of the point (radius of circle that is)
         * @type {Array.<number>}
         * @private
         */
        this.radius_ = radius;

        /**
         * the initial coordinates as a flat array
         * @type {Array.<number>}
         * @private
         */
        this.initial_coords_ = null;

        /**
         * the transformation matrix of length 6
         * @type {Array.<number>|null}
         * @private
         */
        this.transform_ = convertAffineTransformIntoMatrix(transform);

        this.initial_coords_ = this.getFlatCoordinates();

        // apply potential transform
        this.flatCoordinates = applyTransform(
                this.transform_, this.initial_coords_);
    }

    /**
     * Returns the coordinates as a flat array (excl. any potential transform)
     * @return {Array.<number>} the coordinates as a flat array
     */
    getPointCoordinates() {
        var ret =
            this.transform_ ? this.initial_coords_ : this.getFlatCoordinates();
        return ret.slice(0, 2);
    }

    /**
     * Gets the transformation associated with the point
     * @return {Object|null} the AffineTransform object (Bhojpur ODE marshal) or null
     */
    getTransform() {
        return convertMatrixToAffineTransform(this.transform_);
    }

    /**
     * First translate then store the newly translated coords
     *
     * @private
     */
    translate(deltaX, deltaY) {
        // delegate
        if (this.transform_) {
            this.transform_[4] += deltaX;
            this.transform_[5] -= deltaY;
            this.flatCoordinates = applyTransform(
                    this.transform_, this.initial_coords_);
            this.changed();
        } else SimpleGeometry.prototype.translate.call(this, deltaX, deltaY);
    }

    /**
     * Makes a complete copy of the geometry.
     * @return {Point} Clone.
     */
    clone() {
        return new Point(
            this.getPointCoordinates(), this.getTransform());
    }

    /**
     * For displaying coords, this returns a list of [name, value] pairs
     * @return {List} 2D list of 'name', vaule pairs.
     */
    getDisplayCoords() {
        var point = this.getPointCoordinates();
        return [['X', point[0].toFixed(1)],
                ['Y', -point[1].toFixed(1)]];
    }
}

export default Point;