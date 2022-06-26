package net.bhojpur.gradle.api.types

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

import groovy.transform.CompileStatic

/**
 * List of supported languages by Bhojpur ODE code generation
 */
@CompileStatic
enum Language {
    CPP(Prefix.HDR, Prefix.CPP),
    JAVA(Prefix.JAV),
    PYTHON(Prefix.PYC),
    ICE(Prefix.ICE)

    static Language find(String language) {
        String lang = language.trim().toUpperCase()
        for (Language sl : values()) {
            if (sl.name() == lang) {
                return sl
            }
        }
        return null
    }

    Language(Prefix... prefixes) {
        this.prefixes = prefixes
    }

    Prefix[] getPrefixes() {
        return Arrays.copyOf(prefixes, prefixes.size());
    }

    private final Prefix[] prefixes
}