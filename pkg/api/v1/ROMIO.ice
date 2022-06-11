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

#ifndef ODE_IO_ICE
#define ODE_IO_ICE

#include <ode/ServerErrors.ice>
#include <Ice/BuiltinSequences.ice>
#include <ode/Collections.ice>

module ode
{

/**

Primitives for working with binary data.

@see ode.api.RenderingEngine
@see ode.api.RawPixelsStore

 **/
module romio
{

    sequence<Ice::ByteSeq> RGBBands;
    const int RedBand = 0;
    const int GreenBand = 1;
    const int BlueBand = 2;

    class RGBBuffer
    {
      RGBBands bands;
      int sizeX1;
      int sizeX2;
    };

    class RegionDef
    {
      int x;
      int y;
      int width;
      int height;
    };

    const int XY = 0;
    const int ZY = 1;
    const int XZ = 2;

    class PlaneDef
    {
      int slice;
      int x;
      int y;
      int z;
      int t;
      RegionDef region;
      int stride;
    };

    /**
     * Extends PlaneDef by an option to toggle server side Mask rendering. By
     * default all masks attached to the image fulfilling rendering criteria,
     * will be rendered. This criteria is currently masks with a width and
     * height equal to those of the image.
     **/
    class PlaneDefWithMasks extends PlaneDef
    {
      /** Optional (currently unimplemented) list of Masks to render. */
      ode::api::LongList shapeIds;
    };

    class CodomainMapContext
    {
    };

    /**
     * The reverse intensity.
     **/
    class ReverseIntensityMapContext extends CodomainMapContext
    {
    };

};

};

#endif  // ODE_IO_ICE