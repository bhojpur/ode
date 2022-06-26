package net.bhojpur.gradle.dsl

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

import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.util.PatternSet

abstract class DslBase {

    private static final Logger Log = Logging.getLogger(DslBase)

    static File findDatabaseType(FileCollection collection, String type) {
        if (!type) {
            throw new GradleException("Database type (psql, sql, oracle, etc.) not specified")
        }

        File file = new File("$type-types.$FileTypes.EXTENSION_DB_TYPE")
        File databaseType = findInCollection(collection, file, FileTypes.PATTERN_DB_TYPE)
        if (!databaseType) {
            Set<File> files = getFiles(collection, "*resources*")
            if (files.isEmpty()) {
                databaseType = file
                Log.info("using reference file $databaseType")
            } else {
                throw new GradleException("Can't find $file in collection of database types")
            }
        } else {
            Log.info("Found database types file $databaseType")
        }
        return databaseType
    }

    static File findTemplate(FileCollection collection, File file) {
        if (!file) {
            throw new GradleException("No template (.vm) specified")
        }

        if (file.isAbsolute() && file.isFile()) {
            return file
        }
        Log.info("Searching for template with file name: $file")
        File template = findInCollection(collection, file, FileTypes.PATTERN_TEMPLATE)
        if (!template) {
            throw new GradleException("Can't find $file in collection of templates")
        }
        Log.info("Found template file $template")
        return template
    }

    static File findInCollection(FileCollection collection, File file, String include) {
        Set<File> files = getFiles(collection, include)
        Log.info("Looking for file with name $file.name from the following")
        files.find { File f ->
            Log.info("$f")
            f.name == file.name
        }
    }

    static Set<File> getFiles(FileCollection collection, String include) {
        if (collection.isEmpty()) {
            throw new GradleException("Collection is empty")
        }
        FileTree src = collection.asFileTree
        src.matching(new PatternSet().include(include)).files
    }

}