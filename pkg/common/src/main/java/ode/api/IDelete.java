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

import java.util.List;
import java.util.Set;

import ode.annotations.NotNull;
import ode.annotations.Validate;
import ode.conditions.ApiUsageException;
import ode.conditions.SecurityViolation;
import ode.conditions.ValidationException;
import ode.model.IObject;
import ode.model.annotations.ImageAnnotationLink;
import ode.model.containers.Dataset;
import ode.model.containers.DatasetImageLink;
import ode.model.core.Image;
import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.core.PlaneInfo;
import ode.model.display.RenderingDef;
import ode.model.display.Thumbnail;

/**
 * Provides simplifed methods for deleting instances from the database. Various
 * policies may exist for delete and so it is important to consult the
 * implementation documentation to know what types will be deleted by force
 * (using admin privileges) or alternatively throw an exception due to
 * constraint violations.
 */
public interface IDelete extends ServiceInterface {

    /**
     * Returns all entities which would prevent the given {@link Image} id from
     * being deleted. The force boolean determines whether or not the user's
     * collections should be removed in order to facilitate the delete.
     * 
     * Currently this only includes {@link Dataset datasets}. The force boolean
     * determines if {@link Dataset} instances from the same user will be
     * considered as constraints. Regardless of force, datasets from other users
     * are considered constraints.
     * 
     * @param id
     *            of the {@link Image} to be deleted.
     * @param force
     *            return value will be included
     * @return unloaded entity list
     */
    public List<IObject> checkImageDelete(long id, boolean force);

    /**
     * Returns all entities which would be deleted by a call to
     * {@link #deleteImage(long, boolean)}.
     * 
     * @param id
     * @param force
     * @return unloaded entity list
     */
    public List<IObject> previewImageDelete(long id, boolean force);

    /**
     * Deletes an {@link Image} and all related (subordinate) metadata as
     * defined below. This method calls {@link #checkImageDelete(long, boolean)}
     * and throws a {@code ConstraintViolationException} with the results of
     * that call are not empty; then it forcibly deletes all objects returned by
     * {@link #previewImageDelete(long, boolean)}
     * 
     * <p>
     * The deleted metadata includes all of the following types which belong to
     * the current user:
     * <ul>
     * <li>{@link Pixels}</li>
     * <li>{@link PlaneInfo}</li>
     * <li>{@link RenderingDef}</li>
     * <li>{@link OriginalFile}</li>
     * <li>{@link ImageAnnotationLink}</li>
     * </ul>
     * If any of these types do not belong to the current user, the
     * {@link Image} data graph will be considered corrupted and a
     * {@link ValidationException} will be thrown.
     * </p>
     * <p>
     * For the types:
     * <ul>
     * <li>{@link Thumbnail}</li>
     * </ul>
     * a forced deletion will take place even if the user information does not
     * match the current user.
     * </p>
     * <p>
     * If the {@link Image} is not owned by the current user, then
     * {@link SecurityViolation} is thrown, unless the user is root or the group
     * leader.
     * </p>
     * An image will not be deleted if it is contained in a
     * {@link Dataset} owned by another user. If the {@link Image} is contained
     * in other {@link Dataset datasets} belonging to the same user, then the
     * force parameter decides what will happen. A force value of true implies
     * that the {@link Image} will be removed as well as the related
     * {@link DatasetImageLink links}.
     * 
     * The {@link Pixels#getRelatedTo()} field will be set to null for all
     * {@link Pixels} pointing to a {@link Pixels} instance which is about to
     * be deleted.
     * 
     * @param id
     *            id of the {@link Image} to be deleted
     * @param force
     *            {@link Image} will be removed even if contained in other
     *            {@link Dataset datasets}. If false, a
     *            {@code ConstraintViolationException} will be raised.
     * @throws ValidationException
     *             throws an exception if there is any unexpected object which
     *             prevents this object from being deleted, i.e. not in
     *             {@link #checkImageDelete(long, boolean)} (check first)
     * @throws ApiUsageException
     *             if the object has constraints. Use
     *             {@link #checkImageDelete(long, boolean)} first in order to
     *             verify that there are no constraints.
     * @throws SecurityViolation
     *             If the {@link Image} does not belong to the current user.
     */
    public void deleteImage(long id, boolean force) throws SecurityViolation,
            ValidationException, ApiUsageException;

    /**
     * Deletes several {@link Image} instances within a single transaction via
     * the {@link #deleteImage(long, boolean)} method.
     * 
     * @param ids
     *            As {@link #deleteImage(long, boolean)}
     * @param force
     *            As {@link #deleteImage(long, boolean)}
     * @throws ValidationException
     *             As {@link #deleteImage(long, boolean)}
     * @throws ApiUsageException
     *             As {@link #deleteImage(long, boolean)}
     * @throws SecurityViolation
     *             As {@link #deleteImage(long, boolean)}
     */
    public void deleteImages(@NotNull
    @Validate(Long.class)
    Set<Long> ids, boolean force) throws SecurityViolation,
            ValidationException, ApiUsageException;

    /**
     * Deletes the user-visible {@link Image} instances of the given
     * {@link Dataset} within a single transaction via the
     * {@link #deleteImage(long, boolean)}. In addition, before {@link Image}
     * deletion is attempted, the {@link DatasetImageLink links} to the given
     * {@link Dataset} are first removed, otherwise this method would always
     * require a "force" argument of true.
     * 
     * @param datasetId
     *            As {@link #deleteImage(long, boolean)}
     * @param force
     *            As {@link #deleteImage(long, boolean)}
     * @throws ValidationException
     *             As {@link #deleteImage(long, boolean)}
     * @throws ApiUsageException
     *             As {@link #deleteImage(long, boolean)}
     * @throws SecurityViolation
     *             As {@link #deleteImage(long, boolean)}
     */
    public void deleteImagesByDataset(long datasetId, boolean force)
            throws SecurityViolation, ValidationException, ApiUsageException;

    /**
     * Deletes all rendering settings for the given Pixel id. This removes
     * all the same objects that deleteImage() would delete below the given
     * Pixels
     */
    public void deleteSettings(long pixelId);

    /**
     * Deletes all the images contained in a plate as if deleted by:
     * <code>
     *   deleteImage(id, true)
     * </code>
     * so that all Dataset and Annotation links are broken, with WellSamples removed
     * first. Then all Wells in the Plate and the Plate itself are removed.
     *
     * WellAnnotationLinks, and PlateAnnotationLinks are
     * deleted as necessary.
     *
     * @param plateId
     */
    public void deletePlate(long plateId);
}