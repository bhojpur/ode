/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_CORE_IRENDERINGSETTINGS_ICE
#define ODE_CORE_IRENDERINGSETTINGS_ICE

#include <ode/ModelF.ice>
#include <ode/ServicesF.ice>
#include <ode/System.ice>
#include <ode/Collections.ice>

module ode {

    module core {

        /**
         * Provides method to apply rendering settings to a collection of
         * images.
         * All methods will receive the id of the pixels set to copy the
         * rendering settings from.
         **/
        ["ami", "amd"] interface IRenderingSettings extends ServiceInterface
            {
                /**
                 * Checks if the specified sets of pixels are compatible.
                 * Returns <code>true</code> if the pixels set is valid,
                 * <code>false</code> otherwise.
                 *
                 * @param pFrom The pixels set to copy the settings from.
                 * @param pTo The pixels set to copy the settings to.
                 * @return See above.
                 **/
                idempotent bool sanityCheckPixels(ode::model::Pixels pFrom, ode::model::Pixels pTo) throws ServerError;

                /**
                 * Returns the default rendering settings for a given pixels
                 * for the current user.
                 *
                 * @param pixelsId The Id of the Pixels
                 * @return See above.
                 * @throws ValidationException if the image qualified by
                 * <code>imageId</code> is unlocatable.
                 **/
                idempotent ode::model::RenderingDef getRenderingSettings(long pixelsId) throws ServerError;

                /**
                 * Creates a new rendering definition object along with its
                 * sub-objects.
                 *
                 * @param pixels The Pixels set to link to the rendering
                 *               definition.
                 * @return A new, blank rendering definition and sub-objects.
                 * <b>NOTE:</b> the linked Pixels has been unloaded.
                 **/
                ode::model::RenderingDef createNewRenderingDef(ode::model::Pixels pixels) throws ServerError;

                /**
                 * Resets the given rendering settings to those that are
                 * specified by the rendering engine intelligent <i>pretty
                 * good image (PG)</i> logic for the pixels set linked to that
                 * set of rendering settings. <b>NOTE:</b> This method should
                 * only be used to reset a rendering definition that has been
                 * retrieved via {@code getRenderingSettings} as it
                 * relies on certain objects being loaded. The rendering
                 * settings are saved upon completion.
                 *
                 * @param def A RenderingDef to reset. It is expected that
                 *            def.pixels will be <i>unloaded</i> and that the
                 *            actual linked Pixels set will be provided in the
                 *            <code>pixels</code> argument.
                 * @param pixels The Pixels set for <code>def</code>.
                 **/
                void resetDefaults(ode::model::RenderingDef def, ode::model::Pixels pixels) throws ServerError;

                /**
                 * Resets the given rendering settings to those that are
                 * specified by the rendering engine intelligent <i>pretty
                 * good image (PG)</i> logic for the pixels set linked to that
                 * set of rendering settings. <b>NOTE:</b> This method should
                 * only be used to reset a rendering definition that has been
                 * retrieved via {@code getRenderingSettings(long)} as it
                 * relies on certain objects being loaded. The rendering
                 * settings are not saved.
                 *
                 * @param def A RenderingDef to reset. It is expected that
                 *            def.pixels will be <i>unloaded</i> and that the
                 *            actual linked Pixels set will be provided in the
                 *            <code>pixels</code> argument.
                 * @param pixels The Pixels set for <code>def</code>.
                 * @return <code>def</code> with the rendering settings reset.
                 **/
                ode::model::RenderingDef resetDefaultsNoSave(ode::model::RenderingDef def, ode::model::Pixels pixels) throws ServerError;

                /**
                 * Resets an image's default rendering settings back to those
                 * that are specified by the rendering engine intelligent
                 * <i>pretty good image (PG)</i> logic.
                 *
                 * @param imageId The Id of the Image.
                 * @throws ValidationException if the image qualified by
                 * <code>imageId</code> is unlocatable.
                 **/
                void resetDefaultsInImage(long imageId) throws ServerError;

                /**
                 * Resets a Pixels' default rendering settings back to those
                 * that are specified by the rendering engine intelligent
                 * <i>pretty good image (PG)</i> logic.
                 *
                 * @param pixelsId The Id of the Pixels.
                 * @throws ValidationException if the Pixels qualified by
                 * <code>pixelsId</code> is unlocatable.
                 **/
                void resetDefaultsForPixels(long pixelsId) throws ServerError;

                /**
                 * Resets a dataset's rendering settings back to those that
                 * are specified by the rendering engine intelligent <i>pretty
                 * good image (PG)</i> logic.
                 *
                 * @param datasetId The Id of the Dataset.
                 * @return A {@link java.util.Set} of image IDs that have had
                 *         their rendering settings reset.
                 * @throws ValidationException if the image qualified by
                 * <code>datasetId</code> is not locatable.
                 **/
                ode::sys::LongList resetDefaultsInDataset(long datasetId) throws ServerError;

                /**
                 * Resets a rendering settings back to one or many containers
                 * that are specified by the rendering engine intelligent
                 * <i>pretty good image (PG)</i> logic. Supported container
                 * types are:
                 * <ul>
                 *   <li>{@link ode.model.Project}</li>
                 *   <li>{@link ode.model.Dataset}</li>
                 *   <li>{@link ode.model.Image}</li>
                 *   <li>{@link ode.model.Plate}</li>
                 *   <li>{@link ode.model.Pixels}</li>
                 * </ul>
                 * @param type The type of nodes to handle.
                 * @param nodeIds Ids of the node type.
                 * @return A {@link java.util.Set} of image IDs that have had
                 *         their rendering settings reset.
                 * @throws ValidationException if an illegal <code>type</code>
                 *         is used.
                 **/
                ode::sys::LongList resetDefaultsInSet(string type, ode::sys::LongList nodeIds) throws ServerError;

                /**
                 * Resets the rendering settings of a given group of
                 * containers based on the owner's (essentially a copy).
                 * Supported container types are:
                 * <ul>
                 *   <li>{@link ode.model.Project}</li>
                 *   <li>{@link ode.model.Dataset}</li>
                 *   <li>{@link ode.model.Image}</li>
                 *   <li>{@link ode.model.Plate}</li>
                 *   <li>{@link ode.model.Pixels}</li>
                 * </ul>
                 * @param type The type of nodes to handle.
                 * @param nodeIds Ids of the node type.
                 * @return A {@link java.util.Set} of image IDs that have
                 *         had their rendering settings reset.
                 * @throws ValidationException if an illegal <code>type</code>
                 *         is used.
                 */
                ode::sys::LongList resetDefaultsByOwnerInSet(string type, ode::sys::LongList nodeIds) throws ServerError;

                /**
                 * Resets a the channel windows for one or many containers
                 * back to their global minimum and global maximum for the
                 * channel. Supported container types are:
                 * <ul>
                 *   <li>{@link ode.model.Project}</li>
                 *   <li>{@link ode.model.Dataset}</li>
                 *   <li>{@link ode.model.Image}</li>
                 *   <li>{@link ode.model.Plate}</li>
                 *   <li>{@link ode.model.Pixels}</li>
                 * </ul>
                 * @param type The type of nodes to handle.
                 * @param nodeIds Ids of the node type.
                 * @return A {@link java.util.Set} of image IDs that have
                 *         had their rendering settings reset.
                 * @throws ValidationException if an illegal <code>type</code>
                 *         is used.
                 */
                ode::sys::LongList resetMinMaxInSet(string type, ode::sys::LongList nodeIds) throws ServerError;

                /**
                 * Applies rendering settings to one or many containers. If a
                 * container such as Dataset is to be copied to, all images
                 * within that Dataset will have the rendering settings
                 * applied. Supported container types are:
                 * <ul>
                 *   <li>{@link ode.model.Project}</li>
                 *   <li>{@link ode.model.Dataset}</li>
                 *   <li>{@link ode.model.Image}</li>
                 *   <li>{@link ode.model.Plate}</li>
                 *   <li>{@link ode.model.Screen}</li>
                 *   <li>{@link ode.model.Pixels}</li>
                 * </ul>
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param toType The type of nodes to handle.
                 * @param nodeIds Ids of the node type.
                 * @return A map with two boolean keys. The value of the key
                 *         <code>TRUE</code> is a collection of image IDs, the
                 *         settings were successfully applied to.
                 *         The value of the key <code>FALSE</code> is a collection
                 *         of image IDs, the settings could not be applied to.
                 * @throws ValidationException if an illegal <code>type</code>
                 *         is used.
                 */
                BooleanIdListMap applySettingsToSet(long from, string toType, ode::sys::LongList nodeIds) throws ServerError;

                /**
                 * Applies rendering settings to all images in all Datasets of
                 * a given Project.
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param to The Id of the project container to apply settings
                 *           to.
                 * @return A map with two boolean keys. The value of the
                 *         <code>TRUE</code> is a collection of images ID, the
                 *         settings were successfully applied to.
                 *         The value of the <code>FALSE</code> is a collection
                 *         of images ID, the settings could not be applied to.
                 * @throws ValidationException if the rendering settings
                 *         <code>from</code> is not locatable or the project
                 *         <code>to</code> is not locatable.
                 */
                BooleanIdListMap applySettingsToProject(long from, long to) throws ServerError;

                /**
                 * Applies rendering settings to all images in a given Dataset.
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param to The Id of the dataset container to apply settings
                 *           to.
                 * @return A map with two boolean keys. The value of the
                 *         <code>TRUE</code> is a collection of images ID, the
                 *         settings were successfully applied to.
                 *         The value of the <code>FALSE</code> is a collection
                 *         of images ID, the settings could not be applied to.
                 * @throws ValidationException if the rendering settings
                 *         <code>from</code> is not locatable or the dataset
                 *         <code>to</code> is not locatable.
                 */
                BooleanIdListMap applySettingsToDataset(long from, long to) throws ServerError;

                /**
                 * Applies rendering settings to a given Image.
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param to The Id of the image container to apply settings
                 *           to.
                 * @return <code>true</code> if the settings were successfully
                 *         applied, <code>false</code> otherwise.
                 * @throws ValidationException if the rendering settings
                 *         <code>from</code> is not locatable or the image
                 *         <code>to</code> is not locatable.
                 */
                BooleanIdListMap applySettingsToImages(long from, ode::sys::LongList to) throws ServerError;

                /**
                 * Applies rendering settings to a given Image.
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param to The Id of the image container to apply settings
                 *           to.
                 * @return <code>true</code> if the settings were successfully
                 *         applied, <code>false</code> otherwise.
                 * @throws ValidationException if the rendering settings
                 *         <code>from</code> is not locatable or the image
                 *         <code>to</code> is not locatable.
                 */
                bool applySettingsToImage(long from, long to) throws ServerError;

                /**
                 * Applies rendering settings to a given Pixels.
                 *
                 * @param from The Id of the pixels set to copy the rendering
                 *             settings from.
                 * @param to The Id of the pixels container to apply settings
                 *           to.
                 * @return See above.
                 * @throws ValidationException if the rendering settings
                 *         <code>from</code> is not locatable or the
                 *         pixels<code>to</code> is not locatable.
                 */
                bool applySettingsToPixels(long from, long to) throws ServerError;

                /**
                 * Resets an image's default rendering settings back to
                 * channel global minimum and maximum.
                 *
                 * @param imageId The Id of the Image.
                 * @throws ValidationException if the image qualified by
                 *         <code>imageId</code> is not locatable.
                 */
                void setOriginalSettingsInImage(long imageId) throws ServerError;

                /**
                 * Resets an Pixels' default rendering settings back to
                 * channel global minimum and maximum.
                 *
                 * @param pixelsId The Id of the Pixels set.
                 * @throws ValidationException if the image qualified by
                 *         <code>pixelsId</code> is not locatable.
                 */
                void setOriginalSettingsForPixels(long pixelsId) throws ServerError;

                /**
                 * Resets a dataset's rendering settings back to channel global
                 * minimum and maximum.
                 *
                 * @param datasetId The id of the dataset to handle.
                 * @return A {@link java.util.Set} of image IDs that have
                 *         had their rendering settings reset.
                 * @throws ValidationException if the image qualified by
                 *         <code>datasetId</code> is not locatable.
                 */
                ode::sys::LongList setOriginalSettingsInDataset(long datasetId) throws ServerError;

                /**
                 * Resets a rendering settings back to channel global minimum
                 * and maximum for the specified containers. Supported
                 * container types are:
                 * <ul>
                 *   <li>{@link ode.model.Project}</li>
                 *   <li>{@link ode.model.Dataset}</li>
                 *   <li>{@link ode.model.Image}</li>
                 *   <li>{@link ode.model.Plate}</li>
                 *   <li>{@link ode.model.Pixels}</li>
                 * </ul>
                 *
                 * @param type The type of nodes to handle.
                 * @param nodeIds Ids of the node type.
                 * @return A {@link java.util.Set} of image IDs that have
                 *         had their rendering settings reset.
                 * @throws ValidationException if an illegal <code>type</code>
                 *         is used.
                 */
                ode::sys::LongList setOriginalSettingsInSet(string type, ode::sys::LongList nodeIds) throws ServerError;
            };
    };
};

#endif