package net.bhojpur.gradle.dsl.extensions

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
import org.apache.velocity.runtime.RuntimeConstants

@CompileStatic
class VelocityConfig {

    final Map<String, String> data = new Hashtable<String, String>()

    void setProperty(String key, String value) {
        data.put(key, value)
    }

    void setProperties(Map<String, String> propertyMap) {
        propertyMap.each { entry ->
            data.put(entry.key, entry.value)
        }
    }

    void setResourceLoader(String resourceLoader) {
        data.put(
                RuntimeConstants.RESOURCE_LOADER,
                resourceLoader
        )
    }

    void setFileResourceLoaderPath(String fileResourceLoaderPath) {
        data.put(
                RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
                fileResourceLoaderPath
        )
    }

    void setFileResourceLoaderCache(boolean fileResourceLoaderCache) {
        data.put(
                RuntimeConstants.FILE_RESOURCE_LOADER_CACHE,
                fileResourceLoaderCache as String
        )
    }

    void setLoggerClassName(String loggerClassName) {
        data.put(
                RuntimeConstants.RUNTIME_LOG_NAME,
                loggerClassName
        )
    }

    void setMaxNumberLoops(int max) {
        data.put(
                RuntimeConstants.MAX_NUMBER_LOOPS,
                max as String
        )
    }

    void setSkipInvalidIterator(boolean skipInvalid) {
        data.put(
                RuntimeConstants.SKIP_INVALID_ITERATOR,
                skipInvalid as String
        )
    }

    void setCheckEmptyObjects(boolean emptyCheck) {
        data.put(RuntimeConstants.CHECK_EMPTY_OBJECTS, emptyCheck as String)
    }

    void setErrorMsgStart(String msgStart) {
        data.put(
                RuntimeConstants.ERRORMSG_START,
                msgStart
        )
    }

    void setErrorMsgEnd(String msgEnd) {
        data.put(
                RuntimeConstants.ERRORMSG_END,
                msgEnd
        )
    }

    void setParseDirectiveMaxdepth(int max) {
        data.put(
                RuntimeConstants.DEFINE_DIRECTIVE_MAXDEPTH,
                max as String
        )
    }

    void setProvideScopeControl(boolean scope) {
        data.put(
                RuntimeConstants.PROVIDE_SCOPE_CONTROL,
                scope as String
        )
    }

    VelocityConfig() {


        // Set default for velocity config
        setCheckEmptyObjects(false)
    }

    Properties getProperties()
    {
        Properties p = new Properties()
        p.putAll(data)
        return p
    }
}