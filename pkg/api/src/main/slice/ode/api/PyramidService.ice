/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_PYRAMIDSERVICE_ICE
#define ODE_API_PYRAMIDSERVICE_ICE

#include <ode/ServicesF.ice>

module ode {

    module api {

        class ResolutionDescription {
            int sizeX;
            int sizeY;
        };

        /**
         * Description of the geometry of a single resolution level.
         * Initially this contains simply the sizeX/sizeY so that the
         * client can calculate percentages. Eventually, this may also
         * include columns, rows, etc.
         */
        sequence<ResolutionDescription> ResolutionDescriptions;

        ["ami", "amd"]
        interface PyramidService extends StatefulServiceInterface {

                /**
                 * Whether or not this raw pixels store requires a backing
                 * pixels pyramid to provide sub-resolutions of the data.
                 * @return <code>true</code> if the pixels store requires a
                 * pixels pyramid and <code>false</code> otherwise.
                 */
                idempotent bool requiresPixelsPyramid() throws ServerError;

                /**
                 * Retrieves the number of resolution levels that the backing
                 * pixels pyramid contains.
                 * @return The number of resolution levels. This value does not
                 * necessarily indicate either the presence or absence of a
                 * pixels pyramid.
                 */
                idempotent int getResolutionLevels() throws ServerError;

                /**
                 * Retrieves a more complete definition of the resolution
                 * level in question. The size of this array will be of
                 * length {@code getResolutionLevels}.
                 */
                idempotent ResolutionDescriptions getResolutionDescriptions() throws ServerError;

                /**
                 * Retrieves the active resolution level.
                 * @return The active resolution level.
                 */
                idempotent int getResolutionLevel() throws ServerError;

                /**
                 * Sets the active resolution level.
                 * @param resolutionLevel The resolution level to be used by
                 * the pixel buffer.
                 */
                idempotent void setResolutionLevel(int resolutionLevel) throws ServerError;

                /**
                 * Retrieves the tile size for the pixel store.
                 * @return An array of <code>length = 2</code> where the first
                 * value of the array is the tile width and the second value is
                 * the tile height.
                 */
                idempotent Ice::IntSeq getTileSize() throws ServerError;

            };

    };
};

#endif