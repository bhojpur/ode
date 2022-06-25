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

import OlPolygon from 'ol/geom/Polygon';
import SimpleGeometry from 'ol/geom/SimpleGeometry';
import {isArray} from '../utils/Misc';
import {getLength} from '../utils/Regions';
import {applyTransform,
    applyInverseTransform,
    convertMatrixToAffineTransform,
    convertAffineTransformIntoMatrix} from '../utils/Transform';

/**
 * @classdesc
 * Polygon is an extension of the built-in ol.geom.Polygon to be able
 * to use affine transformations
 *
 * @extends {ol.geom.Polygon}
 */
class Polygon extends OlPolygon {

    /**
     * @constructor
     *
     * @param {Array.<Array>} coords the coordinates for the polygon
     * @param {Object=} transform an AffineTransform object according to Bhojpur ODE marshal
     */
    constructor(coords, transform) {
        // preliminary checks: are all mandatory parameters numeric
        if (!isArray(coords) || coords.length === 0)
            console.error("Polygon needs a non-empty array of coordinates!");

        super(coords);

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
        this.flatCoordinates = applyTransform(this.transform_, this.initial_coords_);
    }

    /**
     * Returns the coordinates as a flat array (excl. any potential transform)
     * @return {Array.<number>} the coordinates as a flat array
     */
    getPolygonCoordinates() {
        return (
            this.transform_ ? this.initial_coords_ : this.getFlatCoordinates()
        );
    }

    /**
     * Gets the transformation associated with the polygon
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
    };

    /**
     * Returns the coordinates after (potentially) inverting a transformation
     * @return {Array} the coordinate array
     */
    getInvertedCoordinates() {
        if (this.transform_ === null) return this.getCoordinates();

        var coords = this.getCoordinates();
        var invCoords = new Array(coords[0].length);
        for (var i=0;i<coords[0].length;i++)
            invCoords[i] = applyInverseTransform(
                    this.transform_, coords[0][i]);

        return [invCoords];
    }

    /**
     * Makes a complete copy of the geometry.
     * @return {Polygon} Clone.
     */
    clone() {
        return new Polygon(
                this.getInvertedCoordinates(), this.getTransform());
    };

    /**
     * Returns the length of the polygon
     *
     * @return {number} the length of the polygon
     */
    getLength() {
        return getLength(this);
    }

    /**
     * For displaying coords, this returns a list of [name, value] pairs
     * @return {List} 2D list of 'name', vaule pairs.
     */
    getDisplayCoords() {
        let coords = this.getInvertedCoordinates();
        // Expect only 1 set of coords
        if (coords.length !== 1) return [];
        coords = coords[0];
        return [['Points', coords.map(c => `${ c[0].toFixed(1) },${ -c[1].toFixed(1) }`).join(' ')]];
    }
}

export default Polygon;