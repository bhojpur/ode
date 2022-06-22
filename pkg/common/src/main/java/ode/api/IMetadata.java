package ode.api;

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
import java.util.Set;

import ode.annotations.NotNull;
import ode.annotations.Validate;
import ode.model.acquisition.Instrument;
import ode.model.IObject;
import ode.model.annotations.Annotation;
import ode.model.core.LogicalChannel;
import ode.parameters.Parameters;

/** 
 * Provides method to interact with acquisition metadata and 
 * annotations.
 */
public interface IMetadata 
	extends ServiceInterface
{

	/** The name space indicating that the tag is used a tag set. */
	public static final String NS_INSIGHT_TAG_SET = 
		"bhojpur.net/ode/insight/tagset";
	
	 /**
     * The name space indicating that the <code>Long</code> annotation is
     * a rating annotation i.e. an integer in the interval <code>[0, 5]</code>.
     */
    public static final String NS_INSIGHT_RATING = 
    	"bhojpur.net/ode/insight/rating";
    
    /**
     * The name space indicating that the <code>Boolean</code> annotation 
     * indicated if an archived image is imported with the image. 
     */
    public static final String NS_IMPORTER_ARCHIVED = 
    	"bhojpur.net/ode/importer/archived";

    /** 
     * The name space used to indicate that the <code>FileAnnotation</code> 
     * is an <code>MPEG</code> file.
     */
    public static final String MOVIE_MPEG_NS = 
    	"bhojpur.net/ode/movie/mpeg";

    /** 
     * The name space used to indicate that the <code>FileAnnotation</code> 
     * is an <code>QT</code> file.
     */
    public static final String MOVIE_QT_NS = 
    	"bhojpur.net/ode/movie/qt";
    
	/** 
     * The name space used to indicate that the <code>FileAnnotation</code> 
     * is an <code>WMV</code> file.
     */
    public static final String MOVIE_WMV_NS = 
    	"bhojpur.net/ode/movie/wmv";
    
	/**
	 * Loads the <code>logical channels</code> and the acquisition metadata 
	 * related to them.
	 * 
	 * @param ids The collection of logical channel's ids. 
	 * 		      Mustn't be <code>null</code>.
	 * @return The collection of loaded logical channels.
	 */
	public Set<LogicalChannel> loadChannelAcquisitionData(@NotNull 
			@Validate(Long.class) Set<Long> ids);
	
    /**
     * Loads all the annotations of given types, 
     * that have been attached to the specified <code>rootNodes</code> 
     * for the specified <code>annotatorIds</code>.
     * If no types specified, all annotations will be loaded.
     * This method looks for the annotations that have been attached to each of
     * the specified objects. It then maps each <code>rootNodeId</code> onto
     * the set of annotations that were found for that node. If no
     * annotations were found for that node, then the entry will be
     * <code>null</code>. Otherwise it will be a <code>Set</code>
     * containing {@link Annotation} objects.
     * 
     * @param nodeType The type of the nodes the annotations are linked to. 
     *                 Mustn't be <code>null</code>.
     * @param rootNodeIds  Ids of the objects of type <code>rootNodeType</code>.
     * 				   Mustn't be <code>null</code>.
     * @param annotationType The types of annotation to retrieve. 
     * 						 If <code>null</code> all annotations will be
     *                       loaded. String of the type
     *                       <code>ode.model.annotations.*</code>.
     * @param annotatorIds Ids of the users for whom annotations should be 
     *                     retrieved. 
     *                     If <code>null</code>, all annotations returned.
     * @param options
     * @return A map whose key is rootNodeId and value the <code>Set</code> of
     *         all annotations for that node or <code>null</code>.
     */
    public <T extends IObject, A extends Annotation> 
    Map<Long, Set<A>> loadAnnotations(
            @NotNull Class<T> nodeType, @NotNull @Validate(Long.class)
            Set<Long> rootNodeIds, @NotNull @Validate(String.class) 
            Set<String> annotationType,
            @Validate(Long.class) Set<Long> annotatorIds, Parameters options);
    
    /**
     * Loads all the annotations of a given type.
     * It is possible to filter the annotations by including or excluding name
     * spaces set on the annotations.
     * 
     * @param type The type of annotations to load.
     * @param include Include the annotations with the specified name spaces.
     * @param exclude   Exclude the annotations with the specified name spaces.
     * @param options The POJO options.
     * @return A collection of found annotations.
     */
    public <A extends Annotation> Set<A> loadSpecifiedAnnotations(
    		@NotNull Class type, 
    		@Validate(String.class) Set<String> include, 
    		@Validate(String.class) Set<String> exclude, Parameters options);
    
    /**
     * Loads the Tag Set if the id is specified otherwise loads all the Tag
     * Set.
     * 
     * @param tagIds	The id of the tag to load or <code>-1</code>.
     * @param options	The POJO options.
     * @return Map whose key is a <code>Tag/Tag Set</code> and the value
     * 		   either a Map or a list of related <code>DataObject</code>.
     */
    public Map<Long, Set<IObject>> loadTagContent(
    		@NotNull @Validate(Long.class) Set<Long> tagIds, Parameters options);
     
    /**
     * Loads all the tag Sets. Returns a collection of 
     * <code>AnnotationAnnotatioLink</code> objects and, if the 
     * <code>orphan</code> parameters is <code>true</code>, the 
     * <code>TagAnnotation</code> object.
     * Note that the difference between a Tag Set and a Tag is made
     * using the name space {@link #NS_INSIGHT_TAG_SET}.
     * 
     * @param options The POJO options.
     * @return See above.
     */
    public Set<IObject> loadTagSets(Parameters options);
    
    /**
     * Returns a map whose key is a tag's id and the value the number of
     * Projects, Datasets, and Images linked to that tag.
     * 
     * @param tagIds The collection of ids.
     * @param options The POJO options.
     * @return See above.
     */
    public Map<Long, Long> getTaggedObjectsCount(@NotNull @Validate(Long.class) 
    		Set<Long> tagIds, Parameters options);
    
    /**
     * Counts the number of annotation of a given type.
     * 
     * @param type      The type of annotations to load.
     * @param include   The collection of name space, one of the constants 
     * 					defined by this class.
     * @param exclude   The collection of name space, one of the constants 
     * 					defined by this class.
     * @param options	The POJO options.
     * @return See above.
     */
    public Long countSpecifiedAnnotations(@NotNull Class type, 
    		@Validate(String.class) Set<String> include, 
    		@Validate(String.class) Set<String> exclude, Parameters options);
    
    /**
     * Loads the specified annotations.
     * 
     * @param annotationIds The collection of annotation's ids.
     * @return See above.
     */
    public <A extends Annotation> Set<A> loadAnnotation(
    		@NotNull @Validate(Long.class) Set<Long> annotationIds);
    
    /**
     * Loads the instrument and its components i.e. detectors, objectives, etc.
     * 
     * @param id The id of the instrument to load.
     * @return See above
     */
    public Instrument loadInstrument(long id);
    
    /**
     * Counts the number of annotation of a given type used by the specified
     * user but not owned by the user.
     * 
     * @param annotationType     The type of annotations to load.
     * @param userID   The identifier of the user.
     * @return See above.
     */
    public Long countAnnotationsUsedNotOwned(@NotNull Class annotationType, 
    		long userID);
    
    /**
     * Loads the annotations of a given type used by the specified
     * user but not owned by the user.
     * 
     * @param annotationType     The type of annotations to load.
     * @param userID   The identifier of the user.
     * @return See above.
     */
    public Set<IObject> loadAnnotationsUsedNotOwned(@NotNull Class annotationType,
    		long userID);
    
    /**
     * Loads the annotations of a given type linked to the specified objects.
     * It is possible to filter the annotations by including or excluding name
     * spaces set on the annotations.
     * 
     * This method looks for the annotations that have been attached to each of
     * the specified objects. It then maps each <code>rootNodeId</code> onto
     * the set of annotations that were found for that node. If no
     * annotations were found for that node, the map will not contain an entry
     * for that node. Otherwise it will be a <code>Set</code>
     * containing {@link Annotation} objects.
     * The <code>rootNodeType</code> supported are:
     * Project, Dataset, Image, Pixels, Screen, Plate, PlateAcquisition, Well,
     * Fileset.
     * 
     * @param type      The type of annotations to load.
     * @param include Include the annotations with the specified name spaces.
     * @param exclude   Exclude the annotations with the specified name spaces.
     * @param rootNodeType The type of objects the annotations are linked to.
     * @param rootNodeIds The identifiers of the objects.
     * @param options   The POJO options.
     * @return A collection of found annotations.
     */
    public <A extends Annotation> Map<Long, Set<A>> loadSpecifiedAnnotationsLinkedTo(
    		@NotNull Class type,
    		@Validate(String.class) Set<String> include,
    		@Validate(String.class) Set<String> exclude,
    		@NotNull Class rootNodeType,
    		@NotNull @Validate(Long.class) Set<Long> rootNodeIds,
    		Parameters options);

    /**
     * Find the original file IDs for the import logs corresponding to the given Image or Fileset IDs.
     * @param rootNodeType the root node type, may be {@link ode.model.core.Image} or {@link ode.model.fs.Fileset}
     * @param ids the IDs of the entities for which the import log original file IDs are required
     * @return the original file IDs of the import logs
     */
    public Map<Long, Set<IObject>> loadLogFiles(@NotNull Class<? extends IObject> rootNodeType,
            @Validate(Long.class) Set<Long> ids);
}