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

import java.io.Reader;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A full-text search analyzer usable by the Bhojpur ODE model. Wraps an underlying analyzer.
 * The wrapped analyzer class is configured according to server settings or otherwise.
 */
public final class ConfiguredAnalyzer extends Analyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguredAnalyzer.class);

    private static AtomicReference<Class<? extends Analyzer>> configuredAnalyzerClass = new AtomicReference<>();

    private final Analyzer wrappedAnalyzer;

    /**
     * Construct an analyzer wrapping the same class as previously passed to {@link #ConfiguredAnalyzer(Class)}.
     */
    public ConfiguredAnalyzer() {
        final Class<? extends Analyzer> analyzerClass = configuredAnalyzerClass.get();
        if (analyzerClass == null) {
            throw new IllegalStateException("must configure with analyzer class before using default constructor");
        }
        wrappedAnalyzer = instantiateWrappedAnalyzer(analyzerClass);
    }

    /**
     * Construct an analyzer wrapping the given class.
     * @param analyzerClass a non-abstract class of Lucene analyzer, never {@code null}
     */
    public ConfiguredAnalyzer(Class<? extends Analyzer> analyzerClass) {
        if (analyzerClass == null) {
            throw new IllegalArgumentException("must give analyzer class or use default constructor");
        }
        wrappedAnalyzer = instantiateWrappedAnalyzer(analyzerClass);
        /* set only after verifying it can be instantiated */
        if (analyzerClass != configuredAnalyzerClass.getAndSet(analyzerClass)) {
            LOGGER.debug("set analyzer class to {}", analyzerClass);
        }
    }

    /**
     * Provide an instance of the analyzer to be wrapped.
     * @param analyzerClass a class of Lucene analyzer
     * @return an instance of that class
     */
    private Analyzer instantiateWrappedAnalyzer(Class<? extends Analyzer> analyzerClass) {
        try {
            return analyzerClass.newInstance();
        } catch (ReflectiveOperationException roe) {
            throw new IllegalArgumentException("cannot instantiate analyzer class " + analyzerClass);
        }
    }

    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        return wrappedAnalyzer.tokenStream(fieldName, reader);
    }

    @Override
    public void close() {
        wrappedAnalyzer.close();
    }
}