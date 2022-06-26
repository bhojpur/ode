package net.bhojpur.gradle.api.utils

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
import org.apache.commons.io.FilenameUtils
import org.gradle.api.Transformer
import org.gradle.api.internal.file.copy.RegExpNameMapper
import net.bhojpur.gradle.api.types.Prefix

@CompileStatic
class ApiNamer implements Serializable {

    public static final String DEFAULT_SOURCE_NAME = "(.*?)I[.]combined"

    public static final String DEFAULT_RESULT_NAME = "\$1I"

    private final String sourceRegEx
    private final String replaceWith
    private final Transformer<String, String> rename

    ApiNamer() {
        this(DEFAULT_SOURCE_NAME, DEFAULT_RESULT_NAME)
    }

    ApiNamer(String sourceRegEx, String replaceWith) {
        this.sourceRegEx = sourceRegEx ?: DEFAULT_SOURCE_NAME
        this.replaceWith = replaceWith ?: DEFAULT_RESULT_NAME
    }

    ApiNamer(Transformer<String, String> namer) {
        this.rename = namer
    }

    Transformer<String, String> getRenamer(Prefix prefix) {
        if (rename) {
            return rename
        }
        return new RegExpNameMapper(sourceRegEx, selectExtension(prefix, replaceWith))
    }

    private String selectExtension(Prefix prefix, String replace) {
        final int index = FilenameUtils.indexOfExtension(replace)
        if (index == -1) {
            return "${replace}.${prefix.extension}"
        } else {
            return replace
        }
    }

}