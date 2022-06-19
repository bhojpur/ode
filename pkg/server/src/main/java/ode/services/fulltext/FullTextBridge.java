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

import ode.io.nio.OriginalFilesService;
import ode.model.IAnnotated;
import ode.model.ILink;
import ode.model.IObject;
import ode.model.annotations.Annotation;
import ode.model.annotations.DoubleAnnotation;
import ode.model.annotations.FileAnnotation;
import ode.model.annotations.LongAnnotation;
import ode.model.annotations.MapAnnotation;
import ode.model.annotations.TagAnnotation;
import ode.model.annotations.TermAnnotation;
import ode.model.annotations.TextAnnotation;
import ode.model.containers.Folder;
import ode.model.core.Channel;
import ode.model.core.Image;
import ode.model.core.LogicalChannel;
import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.fs.Fileset;
import ode.model.fs.FilesetEntry;
import ode.model.internal.Details;
import ode.model.internal.NamedValue;
import ode.model.internal.Permissions;
import ode.model.meta.Event;
import ode.model.meta.Experimenter;
import ode.model.meta.ExperimenterGroup;
import ode.model.roi.Roi;
import ode.util.DetailsFieldBridge;
import ode.util.Utils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.builtin.DateBridge;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Primary definition of what will be indexed via Hibernate Search. This class
 * is delegated to by the {@link DetailsFieldBridge}, and further delegates to
 * classes as defined under "SearchBridges".
 */
@Deprecated
public class FullTextBridge extends BridgeHelper {
// TODO insert/update OR delete regular type OR annotated type OR originalfile

    final protected OriginalFilesService files;
    final protected Map<String, FileParser> parsers;
    final protected Class<FieldBridge>[] classes;

    /**
     * Since this constructor provides the instance with no way of parsing
     * {@link OriginalFile} binaries, all files will be assumed to have blank
     * content. Further, no custom bridges are provided and so only the default
     * indexing will take place.
     */
    public FullTextBridge() {
        this(null, null);
    }

    /**
     * Constructor which provides an empty set of custom
     * {@link FieldBridge bridges}.
     */
    @SuppressWarnings("unchecked")
    public FullTextBridge(OriginalFilesService files,
            Map<String, FileParser> parsers) {
        this(files, parsers, new Class[] {});
    }

    /**
     * Main constructor.
     *
     * @param files
     *            {@link OriginalFilesService} for getting access to binary files.
     * @param parsers
     *            List of {@link FileParser} instances which are currently
     *            configured.
     * @param bridgeClasses
     *            set of {@link FieldBridge bridge classes} which will be
     *            instantiated via a no-arg constructor.
     */
    @SuppressWarnings("unchecked")
    public FullTextBridge(OriginalFilesService files,
            Map<String, FileParser> parsers, Class<FieldBridge>[] bridgeClasses) {
        this.files = files;
        this.parsers = parsers;
        this.classes = bridgeClasses == null ? new Class[] {} : bridgeClasses;
    }

    /**
     * Default implementation of the
     * {@link #set(String, Object, Document, LuceneOptions)}
     * method which calls
     * {@link #set_file(String, IObject, Document, LuceneOptions)}
     * {@link #set_annotations(String, IObject, Document, LuceneOptions)},
     * {@link #set_acquisition(String, IObject, Document, LuceneOptions)},
     * {@link #set_details(String, IObject, Document, LuceneOptions)},
     * {@link #set_fileset(String, IObject, Document, LuceneOptions)},
     * {@link #set_folders(String, IObject, Document, LuceneOptions)},
     * and finally
     * {@link #set_custom(String, IObject, Document, LuceneOptions)}.
     * as well as all {@link Annotation annotations}.
     */
    @Override
    public void set(String name, Object value, Document document, LuceneOptions opts) {

        IObject object = (IObject) value;

        // Store class in COMBINED
        String cls = Utils.trueClass(object.getClass()).getName();
        add(document, null, cls, opts);

        set_file(name, object, document, opts);
        set_annotations(name, object, document, opts);
        set_acquisition(name, object, document, opts);
        set_details(name, object, document, opts);
        set_fileset(name, object, document, opts);
        set_folders(name, object, document, opts);
        set_custom(name, object, document, opts);

    }

    /**
     * Uses {@link BridgeHelper#parse(OriginalFile, OriginalFilesService, Map)}
     * to get a {@link Reader} for the given
     * file which is then passed to
     * {@link #addContents(Document, String, OriginalFile, OriginalFilesService, Map, LuceneOptions)}
     * using the field name "file.contents".
     *
     * @param name
     * @param object
     * @param document
     * @param opts
     */
    public void set_file(final String name, final IObject object,
            final Document document, final LuceneOptions opts) {

        if (object instanceof OriginalFile) {
            OriginalFile file = (OriginalFile) object;
            addContents(document, "file.contents", file, files, parsers, opts);
        }

    }

    /**
     * Walks the various {@link Annotation} instances attached to the object
     * argument and adds various levels to the index.
     *
     * @param name
     * @param object
     * @param document
     * @param opts
     */
    public void set_annotations(final String name, final IObject object,
            final Document document, final LuceneOptions opts) {

        if (object instanceof ILink) {
            ILink link = (ILink) object;
            if (link.getChild() instanceof Annotation) {
                reindex(link.getParent());
            }
        }
        if (object instanceof IAnnotated) {
            IAnnotated annotated = (IAnnotated) object;
            List<Annotation> list = annotated.linkedAnnotationList();
            for (Annotation annotation : list) {
                if (annotation instanceof HibernateProxy) {
                    annotation = (Annotation) ((HibernateProxy) annotation).getHibernateLazyInitializer().getImplementation();
                }
                final String at = annotation.getClass().getSimpleName();
                add(document, "annotation.type", at, opts);
                if (annotation.getName() != null) {
                    add(document, "annotation.name", annotation.getName(), opts);
                }
                if (annotation.getNs() != null) {
                    add(document, "annotation.ns", annotation.getNs(), opts);
                }
                if (annotation instanceof TermAnnotation) {
                    TermAnnotation term = (TermAnnotation) annotation;
                    String termValue = term.getTermValue();
                    termValue = termValue == null ? "" : termValue;
                    add(document, "term", termValue, opts);
                } else if (annotation instanceof DoubleAnnotation) {
                    final Double value = ((DoubleAnnotation) annotation).getDoubleValue();
                    if (value != null) {
                        add(document, "annotation", value.toString(), opts);
                    }
                } else if (annotation instanceof LongAnnotation) {
                    final Long value = ((LongAnnotation) annotation).getLongValue();
                    if (value != null) {
                        add(document, "annotation", value.toString(), opts);
                    }
                } else if (annotation instanceof TextAnnotation) {
                    TextAnnotation text = (TextAnnotation) annotation;
                    String textValue = text.getTextValue();
                    textValue = textValue == null ? "" : textValue;
                    add(document, "annotation", textValue, opts);
                    if (annotation instanceof TagAnnotation) {
                        add(document, "tag", textValue, opts);
                        List<Annotation> list2 = annotation
                                .linkedAnnotationList();
                        for (Annotation annotation2 : list2) {
                            if (annotation2 instanceof TextAnnotation) {
                                TextAnnotation text2 = (TextAnnotation) annotation2;
                                String textValue2 = text2.getTextValue();
                                textValue2 = textValue2 == null ? ""
                                        : textValue2;
                                add(document, "annotation", textValue2, opts);
                            }
                        }
                    }
                } else if (annotation instanceof FileAnnotation) {
                    FileAnnotation fileAnnotation = (FileAnnotation) annotation;
                    handleFileAnnotation(document, opts, fileAnnotation);
                } else if (annotation instanceof MapAnnotation) {
                    MapAnnotation mapAnnotation = (MapAnnotation) annotation;
                    handleMapAnnotation(document, opts, mapAnnotation);
                }
            }
        }

        // Have to be careful here, since Annotations are also IAnnotated.
        // Don't use if/else
        if (object instanceof FileAnnotation) {
            FileAnnotation fileAnnotation = (FileAnnotation) object;
            handleFileAnnotation(document, opts, fileAnnotation);
        } else if (object instanceof MapAnnotation) {
            MapAnnotation mapAnnotation = (MapAnnotation) object;
            handleMapAnnotation(document, opts, mapAnnotation);
        }
    }

    /**
     * Walks the acquisition related metadata including channel names. This includes:
     *
     * - channel.name
     * - channel.fluor
     * - channel.mode
     * - channel.photometricInterpretation
     *
     * @param name
     * @param object
     * @param document
     * @param opts
     */
    public void set_acquisition(final String name, final IObject object,
                                final Document document, final LuceneOptions opts) {
        if (object instanceof Image) {
            final Image image = (Image) object;
            if (image.sizeOfPixels() == 0) {
                return;
            }
            final Pixels pixels = image.getPrimaryPixels();
            if (pixels == null) {
                return;
            }
            final Iterator<Channel> channelIterator = pixels.iterateChannels();
            while (channelIterator.hasNext()) {
                final Channel channel = channelIterator.next();
                if (channel == null) {
                    continue;
                }
                final LogicalChannel logical = channel.getLogicalChannel();
                if (logical == null) {
                    continue;
                }
                addIfNotNull(document, "channel.name", logical.getName(), opts);
                addIfNotNull(document, "channel.fluor", logical.getFluor(), opts);
                addEnumIfNotNull(document, "channel.mode", logical.getMode(), opts);
                addEnumIfNotNull(document, "channel.photometricInterpretation",
                        logical.getPhotometricInterpretation(), opts);
                // Note: length items omitted due to difficulty of handling units
            }
        }
    }

    /**
     * Parses all ownership and time-based details to the index for the given
     * object.
     *
     * @param name
     * @param object
     * @param document
     * @param opts
     */
    public void set_details(final String name, final IObject object,
            final Document document, final LuceneOptions opts) {

        final LuceneOptions stored = new SimpleLuceneOptions(opts, Store.YES);
        final LuceneOptions storedNotAnalyzed = new SimpleLuceneOptions(opts, Index.NOT_ANALYZED, Store.YES);

        Details details = object.getDetails();
        if (details != null) {
            Experimenter e = details.getOwner();
            if (e != null && e.isLoaded()) {
                String odename = e.getOdeName();
                String firstName = e.getFirstName();
                String lastName = e.getLastName();
                add(document, "details.owner.odeName", odename, stored);
                add(document, "details.owner.firstName", firstName, opts);
                add(document, "details.owner.lastName", lastName, opts);
            }

            ExperimenterGroup g = details.getGroup();
            if (g != null && g.isLoaded()) {
                String groupName = g.getName();
                add(document, "details.group.name", groupName, stored);
            }

            Event creationEvent = details.getCreationEvent();
            if (creationEvent != null) {
                add(document, "details.creationEvent.id", creationEvent.getId()
                        .toString(), storedNotAnalyzed);
                if (creationEvent.isLoaded()) {
                    String creation = DateBridge.DATE_SECOND
                            .objectToString(creationEvent.getTime());
                    add(document, "details.creationEvent.time", creation,
                            storedNotAnalyzed);
                }
            }

            Event updateEvent = details.getUpdateEvent();
            if (updateEvent != null) {
                add(document, "details.updateEvent.id", updateEvent.getId()
                        .toString(), storedNotAnalyzed);
                if (updateEvent.isLoaded()) {
                    String update = DateBridge.DATE_SECOND
                            .objectToString(updateEvent.getTime());
                    add(document, "details.updateEvent.time", update,
                            storedNotAnalyzed);
                }
            }

            Permissions perms = details.getPermissions();
            if (perms != null) {
                add(document, "details.permissions", perms.toString(), stored);
            }
        }

    }

    /**
     * Walks the various {@link Folder} instances attached to the object
     * argument so that it may be found via its immediate parent folder.
     */
    public void set_folders(final String name, final IObject object,
            final Document document, final LuceneOptions opts) {
        if (object instanceof Image) {
            final Image image = (Image) object;
            final Iterator<Roi> roiIterator = image.iterateRois();
            while (roiIterator.hasNext()) {
                final Roi roi = roiIterator.next();
                final Iterator<Folder> folderIterator = roi.linkedFolderIterator();
                while (folderIterator.hasNext()) {
                    final Folder folder = folderIterator.next();
                    add(document, "roi.folder.name", folder.getName(), opts);
                }
            }
        }
    }

    /**
     * Walks the {@link Fileset} instances attached to an Image. Fields that are added include:
     *
     * - fileset.entry.clientPath
     * - fileset.entry.name
     */
    public void set_fileset(final String name, final IObject object,
                            final Document document, final LuceneOptions opts) {
        if (object instanceof Image) {
            final Image image = (Image) object;
            final Fileset fileset = image.getFileset();
            if (fileset == null) {
                return;
            }
            final Iterator<FilesetEntry> entryIterator = fileset.iterateUsedFiles();
            while (entryIterator.hasNext()) {
                final FilesetEntry entry = entryIterator.next();
                if (entry == null) {
                    continue;
                }
                add(document, "fileset.entry.clientPath", entry.getClientPath(), opts);
                add(document, "fileset.entry.name", entry.getOriginalFile().getName(), opts);
                add(document, "fileset.templatePrefix", fileset.getTemplatePrefix(), opts);
            }
        }
    }

    /**
     * Loops over each {@link #classes field bridge class} and calls its
     * {@link FieldBridge#set(String, Object, Document, LuceneOptions)}
     * method. Any exceptions are logged but do not cancel execution.
     *
     * @param name
     * @param object
     * @param document
     * @param opts
     */
    public void set_custom(final String name, final IObject object,
            final Document document, final LuceneOptions opts) {

        for (Class<FieldBridge> bridgeClass : classes) {
            if (bridgeClass != null) {
                FieldBridge bridge = null;
                try {
                    bridge = bridgeClass.newInstance();
                    if (bridge instanceof BridgeHelper) {
                        BridgeHelper helper = (BridgeHelper) bridge;
                        helper.setApplicationEventPublisher(publisher);
                    }
                    bridge.set(name, object, document, opts);
                } catch (Exception e) {
                    final String msg = String
                            .format(
                                    "Error calling set on custom bridge type:%s; instance:%s",
                                    bridgeClass, bridge);
                    logger().error(msg, e);
                }
            }
        }

    }

    /**
     * Creates {@link Field} instances for {@link FileAnnotation} objects.
     *
     * @param document
     * @param opts
     * @param fileAnnotation
     */
    private void handleFileAnnotation(final Document document,
            final LuceneOptions opts, FileAnnotation fileAnnotation) {
        OriginalFile file = fileAnnotation.getFile();
        if (file != null) {
            // None of these values can be null
            add(document, "file.name", file.getName(), opts);
            add(document, "file.path", file.getPath(), opts);
            if (file.getHasher() != null) {
                add(document, "file.hasher", file.getHasher().getValue(), opts);
            }
            if (file.getHash() != null) {
                add(document, "file.hash", file.getHash(), opts);
            }
            if (file.getMimetype() != null) {
                add(document, "file.format", file.getMimetype(), opts);
                // duplicating for backwards compatibility
                add(document, "file.mimetype", file.getMimetype(), opts);
            }
            addContents(document, "file.contents", file, files, parsers, opts);
        }
    }

    /**
     * Creates {@link Field} instances for {@link MapAnnotation} named-value
     * pair.
     *
     * @param document
     * @param opts
     * @param mapAnnotation
     */
    private void handleMapAnnotation(final Document document,
            final LuceneOptions opts, MapAnnotation mapAnnotation) {
        List<NamedValue> nvs = mapAnnotation.getMapValue();
        if (nvs != null && nvs.size() > 0) {
            for (NamedValue nv : nvs) {
                if (nv != null) {
                    add(document, nv.getName(), nv.getValue(), opts);
                    add(document, "has_key", nv.getName(), opts);
                    add(document, "annotation", nv.getValue(), opts);
                    add(document, "annotation", nv.getName(), opts);
                }
            }
        }
    }
}
