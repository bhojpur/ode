package ode.services.fulltext;

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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * Implementation of {@link LuceneOptions} which takes an existing instance and
 * possible overrides for each of the values.
 */
@Deprecated
public class SimpleLuceneOptions implements LuceneOptions {

    private final LuceneOptions delegate;

    private final Float boost;

    private final Field.Index index;

    private final Field.Store store;

    public SimpleLuceneOptions(LuceneOptions opts, Float boost) {
        this(opts, boost, null, null);
    }

    public SimpleLuceneOptions(LuceneOptions opts, Field.Store store) {
        this(opts, null, null, store);
    }

    public SimpleLuceneOptions(LuceneOptions opts, Index index, Store store) {
        this(opts, null, index, store);
    }

    public SimpleLuceneOptions(LuceneOptions opts, Float boost,
            Field.Index index, Field.Store store) {
        this.delegate = opts;
        this.boost = boost;
        this.store = store;
        this.index = index;
    }

    @Override
    public Float getBoost() {
        if (boost != null) {
            return boost;
        }
        return delegate.getBoost();
    }

    @Override
    public String indexNullAs() {
        return null;
    }

    @Override
    public Index getIndex() {
        if (index != null) {
            return index;
        }
        return delegate.getIndex();
    }

    @Override
    public Store getStore() {
        if (store != null) {
            return store;
        }
        return delegate.getStore();
    }

    @Override
    public TermVector getTermVector() {
        return delegate.getTermVector();
    }


    @Override
    public void addFieldToDocument(String fieldName, String indexedString, Document document) {

    }

    @Override
    public void addNumericFieldToDocument(String fieldName, Object numericValue, Document document) {

    }

    @Override
    public boolean isCompressed() {
        return false;
    }


}