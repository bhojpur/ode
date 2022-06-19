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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * {@link Analyzer} implementation based largely on {@link SimpleAnalyzer}, but
 * with extensions for handling scientific and OS-type strings.
 */
@Deprecated
public final class FullTextAnalyzer extends Analyzer {

    private final static Logger log = LoggerFactory.getLogger(FullTextAnalyzer.class);

    static {
        log.info("Initialized FullTextAnalyzer");
    }

    /**
     * Based on {@link LowerCaseTokenizer}, with the same optimization.
     * However, in order to do alphanumeric tokenizing, rather than just
     * alphabetic, it was necessary to combine that implementation with
     * {@link LetterTokenizer} and extend {@link CharTokenizer} directly.
     * 
     */
    static class LowercaseAlphaNumericTokenizer extends CharTokenizer {

        public LowercaseAlphaNumericTokenizer(Reader input) {
            super(input);
        }

        /**
         * Returns true if "c" is {@link Character#isLetter(char)} or
         * {@link Character#isDigit(char)}.
         */
        @Override
        protected boolean isTokenChar(char c) {
            return Character.isLetter(c) || Character.isDigit(c);
        }

        /**
         * Lower cases via {@link Character#toLowerCase(char)}
         */
        @Override
        protected char normalize(char c) {
            return Character.toLowerCase(c);
        }
    }

    /**
     * Returns a {@link ode.services.fulltext.FullTextAnalyzer.LowercaseAlphaNumericTokenizer}
     */
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new LowercaseAlphaNumericTokenizer(reader);
    }

}
