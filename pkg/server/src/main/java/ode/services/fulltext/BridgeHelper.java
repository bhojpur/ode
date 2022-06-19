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

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ode.conditions.ApiUsageException;
import ode.io.nio.OriginalFilesService;
import ode.model.IEnum;
import ode.model.IObject;
import ode.model.core.OriginalFile;
import ode.services.messages.ReindexMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Base class for building custom {@link FieldBridge} implementations.
 */
@Deprecated
public abstract class BridgeHelper implements FieldBridge,
        ApplicationEventPublisherAware {

    /**
     * Name of the {@link Field} which contains the union of all fields. This is
     * also the default search field, so users need not append the value to
     * search the full index. A field name need only be added to a search to
     * eliminate other fields.
     */
    //TODO add to constants
    public final static String COMBINED = "combined_fields";

    /**
     * Simpler wrapper to handle superclass proxy objects (e.g. Annotation)
     * which do * not behave properly with instanceof checks.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProxiedObject(T proxy) {
        if (proxy instanceof HibernateProxy) {
            return (T) ((HibernateProxy) proxy).getHibernateLazyInitializer()
                    .getImplementation();
        }
        return proxy;
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ApplicationEventPublisher publisher;

    public final Logger logger() {
        return log;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Method to be implemented by all {@link FieldBridge bridges}. The "value"
     * argument is an active Hibernate object, and so the full graph can be
     * walked.
     */
    public abstract void set(final String name, final Object value,
            final Document document, final LuceneOptions opts);

    /**
     * Helper method which takes the parameters from the
     * {@link #set(String, Object, Document, LuceneOptions)}
     * method (possibly modified) as well as the parsed {@link String} value
     * which should be added to the index, and adds two fields. One with the
     * given field name and another to the {@link #COMBINED} field which is the
     * default search provided to users. In addition to storing the value as is,
     * another {@link Field} will be added for both the named and
     * {@link #COMBINED} cases using a {@link StringReader} to allow Lucene to
     * tokenize the {@link String}.
     * 
     * @param d
     *            Document as passed to the set method. Do not modify.
     * @param field
     *            Field name which probably <em/>should</em> be modified. If
     *            this value is null, then the "value" will only be added to the
     *            {@link #COMBINED} field.
     * @param value
     *            Value which has been parsed out for this field. If null, then
     *            this is a no-op.
     * @param opts
     *            LuceneOptions, passed in from the runtime. If overriding on
     *            the interface values is required, see {@link SimpleLuceneOptions}
     */
    protected void addIfNotNull(Document d, String field, String value, LuceneOptions opts) {
        if (value != null) {
            add(d, field, value, opts);
        }
    }

    /**
     * Helper method which takes the parameters from the
     * {@link #set(String, Object, Document, LuceneOptions)}
     * method (possibly modified) as well as the {@link IEnum} value
     * which should be added to the index, and adds two fields. One with the
     * given field name and another to the {@link #COMBINED} field which is the
     * default search provided to users. In addition to storing the value as is,
     * another {@link Field} will be added for both the named and
     * {@link #COMBINED} cases using a {@link StringReader} to allow Lucene to
     * tokenize the {@link String}.
     *
     * @param d
     *            Document as passed to the set method. Do not modify.
     * @param field
     *            Field name which probably <em/>should</em> be modified. If
     *            this value is null, then the "value" will only be added to the
     *            {@link #COMBINED} field.
     * @param value
     *            {@link IEnum} whose {@link IEnum#getValue()} method will be called.
     *            If null, then this is a no-op.
     * @param opts
     *            LuceneOptions, passed in from the runtime. If overriding on
     *            the interface values is required, see {@link SimpleLuceneOptions}
     */
    protected void addEnumIfNotNull(Document d, String field, IEnum value, LuceneOptions opts) {
        if (value != null) {
            add(d, field, value.getValue(), opts);
        }
    }

    /**
     * Helper method which takes the parameters from the
     * {@link #set(String, Object, Document, LuceneOptions)}
     * method (possibly modified) as well as the parsed {@link String} value
     * which should be added to the index, and adds two fields. One with the
     * given field name and another to the {@link #COMBINED} field which is the
     * default search provided to users. In addition to storing the value as is,
     * another {@link Field} will be added for both the named and
     * {@link #COMBINED} cases using a {@link StringReader} to allow Lucene to
     * tokenize the {@link String}.
     *
     * @param d
     *            Document as passed to the set method. Do not modify.
     * @param field
     *            Field name which probably <em/>should</em> be modified. If
     *            this value is null, then the "value" will only be added to the
     *            {@link #COMBINED} field.
     * @param value
     *            Value which has been parsed out for this field. Should
     *            <em/>not</em> be null. If you need to store a null value in
     *            the index, use a null token like "null".
     * @param opts
     *            LuceneOptions, passed in from the runtime. If overriding on
     *            the interface values is required, see {@link SimpleLuceneOptions}
     */
    protected void add(Document d, String field, String value, LuceneOptions opts) {

        if (value == null) {
            throw new RuntimeException(
                    "Value for indexing cannot be null. Use a null token instead.");
        }

        Float boost = opts.getBoost();
        Store store = opts.getStore();
        Index index = opts.getIndex();

        // If the field == null, then we ignore it, to allow easy addition
        // of Fields as COMBINED
        if (field != null) {
            final Field named_field = new Field(field, value, store, index);
            if (boost != null) {
                named_field.setBoost(boost);
            }
            d.add(named_field);
            final Field named_parsed_field = new Field(field, new StringReader(
                    value));
            d.add(named_parsed_field);
        }

        // Never storing in combined fields, since it's duplicated
        final Field combined_field = new Field(COMBINED, value, Store.NO, index);
        if (boost != null) {
            combined_field.setBoost(boost);
        }
        d.add(combined_field);
        final Field combined_parsed_field = new Field(COMBINED,
                new StringReader(value));
        d.add(combined_parsed_field);
    }

    /**
     * Second helper method used when parsing files. The {@link OriginalFile}
     * will be passed to {@link #parse(OriginalFile, OriginalFilesService, Map)}
     * to generate {@link Reader} instances, which will be read until they
     * signal an end, however it is not the responsibility of this instance to
     * close the Readers since this happens asynchronously.
     * 
     * The contents of the file will be parsed both to {@link #COMBINED} and
     * "file.contents".
     * 
     * @param d
     *            {@link Document} as passed to set. Do not modify.
     * @param name String to be used as the name of the field. If null, then
     *         the contents will only be added to the {@link #COMBINED}
     *         {@link Field}.
     * @param file
     *            Non-null, possibly unloaded {@link OriginalFile} which is used
     *            to look up the file on disk.
     * @param files
     *            {@link OriginalFilesService} which knows how to find where this
     *            {@link OriginalFile} is stored on disk.
     * @param parsers
     *            {@link Map} of {@link FileParser} instances to be used based
     *            on the {@link OriginalFile#getMimetype() Format} of
     *            the {@link OriginalFile}
     * @param opts
     *            The search option.
     */
    protected void addContents(final Document d, final String name,
            final OriginalFile file, final OriginalFilesService files,
            final Map<String, FileParser> parsers, final LuceneOptions opts) {

        if (file == null) {
            throw new RuntimeException(
                    "File cannot be null. Either do not attempt to add "
                            + "anything for this field, or use a null token like "
                            + "\"null\" instead.");
        }

        Field f;
        Float boost = opts.getBoost();
        if (name != null) {
            for (Reader parsed : parse(file, files, parsers)) {
                f = new Field(name, parsed);
                if (boost != null) {
                    f.setBoost(boost);
                }
                d.add(f);
            }
        }

        for (Reader parsed : parse(file, files, parsers)) {
            f = new Field(COMBINED, parsed);
            if (boost != null) {
                f.setBoost(boost);
            }
            d.add(f);
        }

    }

    /**
     * Publishes a {@link ReindexMessage} which will get processed
     * asynchronously.
     */
    protected <T extends IObject> void reindex(T object) {
        reindexAll(Collections.singletonList(object));
    }

    /**
     * Publishes a {@link ReindexMessage} which will get processed
     * asynchronously.
     */
    protected <T extends IObject> void reindexAll(List<T> list) {
        if (publisher == null) {
            throw new ApiUsageException(
                    "Bridge is not configured for sending messages.");
        }
        for (T object : list) {
            if (object == null || object.getId() == null) {
                throw new ApiUsageException("Object cannot be null");
            }
        }
        final ReindexMessage<T> rm = new ReindexMessage<T>(this, list);
        publisher.publishEvent(rm);
    }

    /**
     * Attempts to parse the given {@link OriginalFile}. If any of the
     * necessary components is null, then it will return an empty, but not null
     * {@link Iterable}. Also looks for the catch all parser under "*"
     * 
     * @param file
     *            Can be null.
     * @return will not be null.
     */
    protected Iterable<Reader> parse(final OriginalFile file,
            final OriginalFilesService files,
            final Map<String, FileParser> parsers) {
        if (files != null && parsers != null) {
            if (file != null && file.getMimetype() != null) {
                String path = files.getFilesPath(file.getId());
                String format = file.getMimetype();
                FileParser parser = parsers.get(format);
                if (parser != null) {
                    return parser.parse(new File(path));
                } else {
                    parser = parsers.get("*");
                    if (parser != null) {
                        return parser.parse(new File(path));
                    }
                }
            }
        }
        return FileParser.EMPTY;
    }
}
