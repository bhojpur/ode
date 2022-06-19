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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ode.conditions.ApiUsageException;
import ode.model.annotations.Annotation;
import ode.model.annotations.BooleanAnnotation;
import ode.model.annotations.CommentAnnotation;
import ode.model.annotations.DoubleAnnotation;
import ode.model.annotations.FileAnnotation;
import ode.model.annotations.LongAnnotation;
import ode.model.annotations.TagAnnotation;
import ode.model.annotations.TermAnnotation;
import ode.model.annotations.TextAnnotation;
import ode.model.annotations.TimestampAnnotation;
import ode.model.annotations.XmlAnnotation;
import ode.model.core.OriginalFile;
import ode.model.internal.Details;
import ode.system.ServiceFactory;
import ode.tools.hibernate.QueryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * Query for {@link ode.api.Search} which uses an example {@link Annotation}
 * instance as the basis for comparison. Instances of the specified
 * {@link SearchValues#onlyTypes type} are found with a matching annotation.
 * 
 * Currently only the class of the annotation and its main attribute --
 * {@link TextAnnotation#textValue}, {@link FileAnnotation#file}, etc. -- are
 * considered. Use the other methods on {@link ode.api.Search} like
 * {@link ode.api.Search#onlyOwnedBy(Details)} to refine your search.
 * 
 * Ignores {@link ode.api.Search#onlyAnnotatedWith(Class...)}
 */
public class AnnotatedWith extends SearchAction {

    private static final Logger log = LoggerFactory.getLogger(AnnotatedWith.class);

    private static final long serialVersionUID = 1L;

    private final Annotation[] annotation;

    private final String[] path;

    private final Class[] annCls;

    private final Class[] type;

    private final Object[] value;

    private final boolean[] fetch;

    /**
     * copy of fetchAnnotations list, so that items which are already fetched
     * via {@link #fetch} are not fetched again.
     */
    private final List<Class> fetchAnnotationsCopy = new ArrayList<Class>();

    private final boolean useNamespace;

    private final boolean useLike;

    private final Class cls;

    public AnnotatedWith(SearchValues values, Annotation[] annotation,
            boolean useNamespace, boolean useLike) {
        super(values);
        this.annotation = annotation;
        this.useNamespace = useNamespace;
        this.useLike = useLike;

        // Note: It should be possible to set cls = IAnnotated.class, but
        // there must be a way to then remove the conditionals for
        // IGlobals below.
        if (values.onlyTypes == null || values.onlyTypes.size() != 1) {
            throw new ApiUsageException(
                    "Searches by annotated with are currently limited "
                            + "to a single type\n"
                            + "Plese use Search.onlyType()");
        } else {
            cls = values.onlyTypes.get(0);
        }

        if (annotation == null || annotation.length == 0) {
            throw new ApiUsageException("Must specify at least one annotation.");
        }
        this.path = new String[annotation.length];
        this.annCls = new Class[annotation.length];
        this.type = new Class[annotation.length];
        this.value = new Object[annotation.length];
        this.fetch = new boolean[annotation.length];
        this.fetchAnnotationsCopy.addAll(values.fetchAnnotations);
        for (int i = 0; i < annotation.length; i++) {
            if (annotation[i] instanceof TextAnnotation) {
                // FIXME This should be unneccessary.
                if (annotation[i] instanceof CommentAnnotation) {
                    annCls[i] = CommentAnnotation.class;
                } else if (annotation[i] instanceof TagAnnotation) {
                    annCls[i] = TagAnnotation.class;
                } else if (annotation[i] instanceof TermAnnotation) {
                    annCls[i] = TermAnnotation.class;
                } else if (annotation[i] instanceof XmlAnnotation) {
                    annCls[i] = XmlAnnotation.class;
                } else {
                    annCls[i] = TextAnnotation.class;
                }
                type[i] = String.class;
                path[i] = "textValue";
                value[i] = ((TextAnnotation) annotation[i]).getTextValue();
            } else if (annotation[i] instanceof BooleanAnnotation) {
                annCls[i] = BooleanAnnotation.class;
                type[i] = Boolean.class;
                path[i] = "boolValue";
                value[i] = ((BooleanAnnotation) annotation[i]).getBoolValue();
            } else if (annotation[i] instanceof TimestampAnnotation) {
                annCls[i] = TimestampAnnotation.class;
                type[i] = Timestamp.class;
                path[i] = "timeValue";
                value[i] = ((TimestampAnnotation) annotation[i]).getTimeValue();
            } else if (annotation[i] instanceof FileAnnotation) {
                annCls[i] = FileAnnotation.class;
                type[i] = OriginalFile.class;
                path[i] = "file";
                value[i] = ((FileAnnotation) annotation[i]).getFile();
            } else if (annotation[i] instanceof DoubleAnnotation) {
                annCls[i] = DoubleAnnotation.class;
                type[i] = Double.class;
                path[i] = "doubleValue";
                value[i] = ((DoubleAnnotation) annotation[i]).getDoubleValue();
            } else if (annotation[i] instanceof LongAnnotation) {
                annCls[i] = LongAnnotation.class;
                type[i] = Long.class;
                path[i] = "longValue";
                value[i] = ((LongAnnotation) annotation[i]).getLongValue();
            } else {
                throw new ApiUsageException("Unsupported annotation type:"
                        + annotation);
            }

            // If we have an example of the given annotation, then we can
            // fetch it directly and don't need to use the fetchAnnotationsCopy
            // collection.
            for (Class ac : values.fetchAnnotations) {
                if (annCls[i].isAssignableFrom(ac)) {
                    fetch[i] = true;
                    fetchAnnotationsCopy.remove(ac);
                }
            }
            // On the other hand, if the value is null, then we might as well
            // treat this as a fetch request and search only on Ann.class =.
            // See the guards around the Joins section and the the call to
            // notNullOrLikeOrEqual below.
            if (value[i] == null) {
                fetchAnnotationsCopy.add(annCls[i]);
            }
        }

    }

    @Transactional(readOnly = true)
    public Object doWork(Session session, ServiceFactory sf) {

        String[] link = new String[annotation.length];
        String[] ann = new String[annotation.length];

        QueryBuilder qb = new QueryBuilder();
        qb.select("this");
        qb.from(cls.getName(), "this");

        // Joins
        for (int i = 0; i < ann.length; i++) {
            if (value[i] != null) {
                link[i] = qb.unique_alias("link");
                ann[i] = link[i] + "_child";
                qb.join("this.annotationLinks", link[i], false, fetch[i]);
                qb.join(link[i] + ".child", ann[i], false, fetch[i]);
            }
        }

        // fetch annotations
        for (int i = 0; i < fetchAnnotationsCopy.size(); i++) {
            qb.join("this.annotationLinks", "fetchannlink" + i, false, true);
            qb.join("fetchannlink" + i + ".child", "fetchannchild" + i, false,
                    true);
        }
        qb.where();
        for (int i = 0; i < fetchAnnotationsCopy.size(); i++) {
            qb.and("fetchannchild" + i + ".class = "
                    + fetchAnnotationsCopy.get(i).getSimpleName());
        }

        ids(qb, "this.");
        ownerOrGroup(cls, qb, "this.");
        createdOrModified(cls, qb, "this.");

        for (int j = 0; j < annotation.length; j++) {
            // Main criteria
            if (useNamespace) {
                notNullOrLikeOrEqual(qb, ann + ".ns", type[j], annotation[j]
                        .getNs(), useLike, values.caseSensitive);
            }

            // If the value of the annotation is null, we assume that we are not
            // actually searching for null annotations (whose nullability is
            // actually a by-product of polymorphism), instead we assume the
            // null acts like a wildcard, in which case this search has been
            // added to the fetchAnnotationsCopy collection above.
            if (value[j] != null) {
                notNullOrLikeOrEqual(qb, ann[j] + "." + path[j], type[j],
                        value[j], useLike, values.caseSensitive);
            }

            annotatedBetween(qb, ann[j] + ".");
            annotatedBy(qb, ann[j] + ".");
        }

        // orderBy
        for (String orderBy : values.orderBy) {
            String orderByPath = orderByPath(orderBy);
            boolean ascending = orderByAscending(orderBy);
            qb.order("this." + orderByPath, ascending);
        }

        log.debug(qb.toString());
        final Query query = qb.query(session);
        if (timeout != null) {
            query.setTimeout(timeout);
        }
        return query.list();
    }
}
