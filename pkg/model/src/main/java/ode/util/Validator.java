package ode.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.model.IObject;
import ode.model.acquisition.ImagingEnvironment;
import ode.model.core.Channel;
import ode.model.core.Pixels;

/**
 * tests of model objects for validity.
 */
public abstract class Validator {

    protected static Logger log = LoggerFactory.getLogger(Validator.class);

    public static Validation validate(IObject obj) {
        return Validation.VALID();
    }

    public static Validation validate(Channel channel) {
        Validation v = Validation.VALID();

        /**
         * this needs to span multiple types. we need to be an attached graph so
         * we can check those relationships. Can be assume that? Non-Anemic
         * model? Do null tests count?
         */
        // Pixels pixels = channel.getPixels (need inverse)
        String piType =
        	channel.getLogicalChannel().
        	        getPhotometricInterpretation().getValue(); // TODO
        // null
        // Safe?
        if (piType.equals("RGB") || piType.equals("ARGB")
                || piType.equals("CMYK") || piType.equals("HSV")) {
            if (channel.getRed() == null || channel.getGreen() == null || channel.getBlue() == null || channel.getAlpha() == null) {
                v.invalidate("Channel colors  cannot be null if PiType == " +
                		"{RGB|ARGB|CMYK|HSV}");
            }
        }

        return v;
    }

    public static Validation validate(ImagingEnvironment imageEnvironment) {
        Validation v = Validation.VALID();

        Double co2 = imageEnvironment.getCo2percent();
        if (null != co2 && (co2.floatValue() < 0 || co2.floatValue() > 1)) {
            v.invalidate("ImageEnvironment.co2percent must be between 0 and 1");
        }

        Double humidity = imageEnvironment.getHumidity();
        if (null != humidity
                && (humidity.floatValue() < 0 || humidity.floatValue() > 1)) {
            v.invalidate("ImageEnvironment.humidity must be between 0 and 1");
        }

        return v;
    }

    public static Validation valid(Pixels pixels) {
        Validation v = Validation.VALID();

        /** careful; collections! */
        int planeInfoSize = pixels.sizeOfPlaneInfo();
        int sizeC = pixels.getSizeC().intValue(); // TODO and sizeX null
        // (excep? or invalidate?)
        int sizeT = pixels.getSizeT().intValue();
        int sizeZ = pixels.getSizeZ().intValue();
        if (!(planeInfoSize == 0 || planeInfoSize == sizeC * sizeT * sizeZ)) {
            v
                    .invalidate("Size of planeInfo in Pixels should be 0 or sizeC*sizeT*sizeZ");
        }

        return v;
    }

}