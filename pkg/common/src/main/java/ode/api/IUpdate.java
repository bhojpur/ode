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

import java.util.Collection;
import java.util.List;

import ode.annotations.Validate;
import ode.conditions.ValidationException;
import ode.model.IObject;

/**
 * Provides methods for directly updating object graphs. IUpdate is the lowest
 * level (level-1) interface which may make changes (INSERT, UPDATE, DELETE) to
 * the database. All other methods of changing the database may leave it in an
 * inconsistent state.
 * 
 * <p>
 * All the save* methods act recursively on the entire object graph, replacing
 * placeholders and details where necessary, and then "merging" the final graph.
 * This means that the objects that are passed into IUpdate.save* methods are
 * copied over to new instances which are then returned. The original objects
 * <b>should be discarded</b>.
 * </p>
 * 
 * <p>{@link #saveAndReturnIds(IObject[])} behaves slightly differently in that
 * it does <em>not</em> handle object modifications. The graph of objects
 * passed in can consist <em>ONLY</em> if either newly created objects without
 * ids or of unloaded objects with ids. <em>Note:</em> The ids of the saved values
 * may not be in order. This is caused by persistence-by-transitivity. Hibernate
 * may detect an item later in the array if they are interconnected and therefore
 * choose to save it first.
 * </p>
 * 
 * <p>
 * All methods throw {@link ode.conditions.ValidationException} if the input
 * objects do not pass validation, and
 * {@link ode.conditions.OptimisticLockException} if the version of a given has
 * already been incremented.
 * 
 * @see ode.util.Validation
 * @see ode.model.internal.Details
 */
public interface IUpdate extends ServiceInterface {

    /** Logic differs from other methods. See class description
     * @see ode.api.IUpdate */
    List<Long> saveAndReturnIds(IObject[] objects);
    
    /** @see ode.api.IUpdate */
    void saveCollection(@Validate(IObject.class)
    Collection<IObject> graph);

    /** @see ode.api.IUpdate */
    void saveObject(IObject graph);

    /** @see ode.api.IUpdate */
    void saveArray(IObject[] graph);

    /** @see ode.api.IUpdate */
    <T extends IObject> T saveAndReturnObject(T graph);

    /** @see ode.api.IUpdate */
    IObject[] saveAndReturnArray(IObject[] graph);

    /**
     * Deletes a single entity. Unlike the other IUpdate methods, deleteObject
     * does not propagate to related entities (e.g. foreign key relationships)
     * and so calls to deleteObject must be properly ordered.
     *
     * For example, if you would like to delete a FileAnnotation along with
     * the linked OriginalFile, it is necessary to first call
     * deleteObject(OriginalFile) and then deleteObject(FileAnnotation).
     *
     * Instead, you may look to use the more advanced method provided in
     * {@link ode.api.IDelete} which provide support for deleting entire
     * graphs of objects in the correct order.
     *
     * @param row
     *            a persistent {@link IObject} to be deleted.
     * @throws ValidationException
     *             if the row is locked, has foreign key constraints, or is
     *             otherwise marked un-deletable.
     */
    void deleteObject(IObject row) throws ValidationException;

    /**
     * Initiates full-text indexing for the given object. This may have to wait
     * for the current {@code ode.services.fulltext.FullTextThread} to finish.
     * Can only be executed by an admin. Other users must wait for the
     * background {@link Thread} to complete.
     * 
     * @param row
     *            a persistent {@link IObject} to be deleted
     * @throws ValidationException
     *             if the object does not exist or is nul
     */
    void indexObject(IObject row) throws ValidationException;
}