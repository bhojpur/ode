package ode.formats.importer;

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

import ode.model.Pixels;

/**
 * Calculates the various dimensions of an image from a {@link Pixels} instance.
 */

public class ImportSize {

    public final String fileName;
    public final Pixels pixels;
    public final String dimOrder;
    public final int sizeX, sizeY, sizeZ, sizeC, sizeT, imageCount;
    public final int zSize, wSize, tSize;

    public ImportSize(String fileName, Pixels pixels, String dimOrder) {
        this.fileName = fileName;
        this.pixels = pixels;
        this.dimOrder = dimOrder;

        sizeZ = pixels.getSizeZ().getValue();
        sizeC = pixels.getSizeC().getValue();
        sizeT = pixels.getSizeT().getValue();
        sizeX = pixels.getSizeX().getValue();
        sizeY = pixels.getSizeY().getValue();
        imageCount = sizeZ * sizeC * sizeT;

        final int order = getSequenceNumber(dimOrder);

        int smallOffset = 1;
        switch (order) {
        // ZTW sequence
        case 0:
            zSize = smallOffset;
            tSize = zSize * sizeZ;
            wSize = tSize * sizeT;
            break;
        // WZT sequence
        case 1:
            wSize = smallOffset;
            zSize = wSize * sizeC;
            tSize = zSize * sizeZ;
            break;
        // ZWT sequence
        case 2:
            zSize = smallOffset;
            wSize = zSize * sizeZ;
            tSize = wSize * sizeC;
            break;
        // TWZ sequence
        case 3:
            tSize = smallOffset;
            wSize = tSize * sizeT;
            zSize = wSize * sizeC;
            break;
        // WTZ sequence
        case 4:
            wSize = smallOffset;
            tSize = wSize * sizeC;
            zSize = tSize * sizeT;
            break;
        // TZW
        case 5:
            tSize = smallOffset;
            zSize = tSize * sizeT;
            wSize = zSize * sizeZ;
            break;
        default:
            throw new RuntimeException("Bad order");
        }
    }

    private int getSequenceNumber(String dimOrder) {
        if (ode.model.enums.DimensionOrderXYZTC.value.equals(dimOrder))
            return 0;
        if (ode.model.enums.DimensionOrderXYCZT.value.equals(dimOrder))
            return 1;
        if (ode.model.enums.DimensionOrderXYZCT.value.equals(dimOrder))
            return 2;
        if (ode.model.enums.DimensionOrderXYTCZ.value.equals(dimOrder))
            return 3;
        if (ode.model.enums.DimensionOrderXYCTZ.value.equals(dimOrder))
            return 4;
        if (ode.model.enums.DimensionOrderXYTZC.value.equals(dimOrder))
            return 5;
        throw new RuntimeException(dimOrder + " not represented in "
                + "getSequenceNumber");
    }

}