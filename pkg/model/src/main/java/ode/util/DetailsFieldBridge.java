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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.springframework.context.ApplicationContext;

/**
 * Delegating {@link FieldBridge} which passes the "fieldBridge" bean from the
 * "ode.model" Spring {@link ApplicationContext} all arguments.
 */
public class DetailsFieldBridge implements FieldBridge {

    private static FieldBridge bridge;

    private final static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void lock() {
        lock.writeLock().lock();
    }

    public static boolean tryLock() {
        return lock.writeLock().tryLock();
    }

    public static void unlock() {
        lock.writeLock().unlock();
    }

    public static void setFieldBridge(FieldBridge bridge) {
        DetailsFieldBridge.bridge = bridge;
    }

    public void set(String name, Object value, Document document, LuceneOptions opts) {
        bridge.set(name, value, document, opts);

    }

}