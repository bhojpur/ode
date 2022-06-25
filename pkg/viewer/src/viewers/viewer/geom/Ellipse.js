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

import Polygon from 'ol/geom/Polygon';
import SimpleGeometry from 'ol/geom/SimpleGeometry';
import {applyTransform,
    convertMatrixToAffineTransform,
    convertAffineTransformIntoMatrix} from '../utils/Transform';
import {isArray} from '../utils/Misc';
import {getLength} from '../utils/Regions';

/**
 * @classdesc
 * Ellipse is an extension of the built-in ol.geom.Polygon that will allow
 * you to create ellipses within the open layers framework.
 *
 * At present the approach taken is aiming at best integration into openlayers
 * as well as cross browser support. For HTMLCanvas there is a drawEllipse
 * method which, toDate, is only supported by Chrome.
 *
 * That said, there are various methods out there how people accomplish the task
 * of drawing en ellipse on an HTMLCanvas. They range from scaled circles, over
 * sets of bezier curves to what is, undoubtedly, the most accurate method
 * mathematically, namely to trace the outline for a given step size according to
 * the following formulae for x,y:
 * <pre>x = a * cos(theta)</pre> and
 * <pre>y = b * sin(theta)</pre>
 * see: {@link https://en.wikipedia.org/wiki/Ellipse}
 *
 * The latter technique is used here since it's accurate enough and produces
 * a polygon of connected points which openlayers likes.
 *
 * @extends {ol.geom.Polygon}
 */
class Ellipse extends Polygon {

    /**
     * @constructor
     *
     * @param {number} cx the center x coordinate of the ellipse
     * @param {number} cy the center y coordinate of the ellipse
     * @param {number} rx the radius x distance of the ellipse
     * @param {number} ry the radius y distance of the ellipse
     * @param {Object=} transform an AffineTransform object according to Bhojpur ODE marshal
     */
    constructor(cx, cy, rx, ry, transform) {
        // preliminary checks: are all mandatory parameters numeric
        if (typeof cx !== 'number' || typeof cy !== 'number' ||
                typeof rx !== 'number' || typeof ry !== 'number')
            console.error("at least one ellipse param is not numeric!");

        let step = 0.1;
        super([getEllipseCoords(cx, cy, rx, ry, transform, step)]);

        /**
         * center x coordinate
         * @type {number}
         * @private
         */
        this.cx_ = cx;

        /**
         * center y coordinate
         * @type {number}
         * @private
         */
        this.cy_ = cy;

        /**
         * radius x distance
         * @type {number}
         * @private
         */
        this.rx_ = rx;

        /**
         * radius y distance
         * @type {number}
         * @private
         */
        this.ry_ = ry;

        /**
         * the transformation matrix of length 6
         * @type {Array.<number>|null}
         * @private
         */
        this.transform_ = convertAffineTransformIntoMatrix(transform);

        /**
         * the step size for plotting
         * @type {number}
         * @private
         */
        this.step_ = step;
    }

    /**
     * Traces out the ellipse and returns the coords
     * @return {Array.<number>} the coordinate array for the outline
     */
    getPolygonCoords() {
        return getEllipseCoords(this.cx_,  this.cy_,  this.rx_,  this.ry_,
            this.transform_, this.step_);
    }

    /**
     * Gets the transformation associated with the ellipse
     * @return {Object|null} the AffineTransform object (Bhojpur ODE marshal) or null
     */
    getTransform() {
        return convertMatrixToAffineTransform(this.transform_);
    }

    /**
     * Gets the center of the ellipse in array form [cx,cy]
     * @return {Array.<number>} the center of the ellipse as an array
     */
    getCenter() {
        return [this.cx_,this.cy_] ;
    }

    /**
     * Sets the center of the ellipse using a coordinate array [cx, cy]
     *
     * @param {Array.<number>} value the center of the ellipse as an array
     */
    setCenter(value) {
        if (!isArray(value) ||
            typeof value[0] !== 'number' || typeof value[1] !== 'number')
                console.error(
                    "the center needs to be given as a numeric array [cx,cy]");
        this.cx_ = value[0];
        this.cy_ = value[1];
    }

    /**
     * Gets the radius (distance x, distance y) of the ellipse in array form [rx,ry]
     * @return {Array.<number>} the radius of the ellipse as an array
     */
    getRadius() {
        return [this.rx_, this.ry_];
    }

    /**
     * Sets the radius (distance x, distance y) of the ellipse in array form [rx,ry]
     *
     * @param {Array.<number>} value the radius of the ellipse as an array
     */
    setRadius(value) {
        if (!isArray(value) ||
            typeof value[0] !== 'number' || typeof value[1] !== 'number')
                console.error("the radius needs to be given as a numeric array [rx,ry]");
        this.rx_ = value[0];
        this.ry_ = value[1];
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
                this.setCoordinates([this.getPolygonCoords()]);
        } else {
            SimpleGeometry.prototype.translate.call(this, deltaX, deltaY);
            this.setCenter([this.cx_ + deltaX, this.cy_ + deltaY]);
        }
    }

    /**
     * First scale then store the newly scaled coords
     *
     * @private
     */
    scale(factor) {
        // delegate
        if (this.transform_) {
            this.transform_[0] *= factor;
            this.transform_[1] *= factor;
            this.transform_[2] *= factor;
            this.transform_[3] *= factor;
            this.setCoordinates([this.getPolygonCoords()]);
        } else {
            SimpleGeometry.prototype.scale.call(this, factor);
            var radius = this.getRadius();
            this.setRadius([radius[0] * factor, radius[1] * factor])
        }
    }

    /**
     * Returns the length of the ellipse
     *
     * @return {number} the length of the ellipse
     */
    getLength() {
        return getLength(this);
    }

    /**
     * Returns the Area as Pi * rx * ry instead of relying on Polygon superclass
     * method which is less accurate.
     *
     * @return {number} the area of the ellipse
     */
    getArea() {
        return Math.PI * this.rx_ * this.ry_;
    }

    /**
     * Make a complete copy of the geometry.
     * @return {Ellipse} Clone.
     */
    clone() {
        return new Ellipse(
            this.cx_, this.cy_, this.rx_, this.ry_, this.getTransform());
    }

    /**
     * For displaying coords, this returns a list of [name, value] pairs
     * @return {List} 2D list of 'name', vaule pairs.
     */
    getDisplayCoords() {
        return [['X', this.cx_.toFixed(1)],
                ['Y', -this.cy_.toFixed(1)],
                ['RadiusX', this.rx_.toFixed(1)],
                ['RadiusY', this.ry_.toFixed(1)]];
    }
}

/**
 * Traces out the ellipse and returns the coords
 * @return {Array.<number>} the coordinate array for the outline
 */
function getEllipseCoords(cx, cy, rx, ry, transform, step) {
    // trace ellipse now and store coordinates
    var coords = [];
    for (var i = 0 * Math.PI, ii=2*Math.PI; i < ii; i += step) {
        var x = cx + rx * Math.cos(i);
        var y = cy + ry * Math.sin(i);
        coords.push(applyTransform(transform, [x, y]));
    }
    if (coords.length > 0) coords.push(coords[0]); // close polygon

    return coords;
}

export default Ellipse;