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

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

/**
 * Simple bean which calls "map.put(key, object)" on
 * {@link InitializingBean#afterPropertiesSet()}. Must adhere to the
 * {@link Map} interface for nulls, etc.
 */
public class MapPut implements InitializingBean {

    private Map<String, Object> map;

    private String key;

    private Object object;

    public void afterPropertiesSet() throws Exception {
        map.put(key, object);
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}