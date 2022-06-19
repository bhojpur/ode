package ode.services.search;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ode.conditions.ApiUsageException;
import ode.conditions.InternalException;
import ode.model.annotations.CommentAnnotation;
import ode.model.annotations.TextAnnotation;
import ode.system.ServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyTermEnum;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.reader.ReaderProvider;
import org.hibernate.search.store.DirectoryProvider;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Search to find similar terms to some given terms.
 */
public class SimilarTerms extends SearchAction {

    private static final Logger log = LoggerFactory.getLogger(SimilarTerms.class);

    private static final long serialVersionUID = 1L;

    private final String[] terms;

    public SimilarTerms(SearchValues values, String...terms) {
        super(values);
        this.terms = terms;
    }

    @Transactional(readOnly = true)
    public Object doWork(Session s, ServiceFactory sf) {

        if (values.onlyTypes == null || values.onlyTypes.size() != 1) {
            throw new ApiUsageException(
                    "Searches by similar terms are currently limited to a single type.\n"
                            + "Plese use Search.onlyType()");
        }
        final Class<?> cls = values.onlyTypes.get(0);

        final FullTextSession session = Search.getFullTextSession(s);
        final SearchFactory factory = session.getSearchFactory();
        final DirectoryProvider[] directory = factory.getDirectoryProviders(cls);
        final ReaderProvider provider = factory.getReaderProvider();

        Assert.notEmpty(directory, "Must have a directory provider");
        Assert.isTrue(directory.length == 1, "Can only handle one directory");
        
        final IndexReader reader = provider.openReader(directory[0]);
        
        FuzzyTermEnum fuzzy = null;
        List<TextAnnotation> rv = new ArrayList<TextAnnotation>();
        try {
            fuzzy = new FuzzyTermEnum(reader, new Term("combined_fields", terms[0]));
            while (fuzzy.next()) {
                CommentAnnotation text = new CommentAnnotation();
                text.setNs(terms[0]);
                text.setTextValue(fuzzy.term().text());
                rv.add(text);
            }
            return rv;
        } catch (IOException e) {
            throw new InternalException("Error reading from index: "+e.getMessage());
        } finally {
            if (fuzzy != null) {
                fuzzy.endEnum();
            }
        }
        
    }
}
