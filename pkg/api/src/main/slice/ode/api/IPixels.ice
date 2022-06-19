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

#ifndef ODE_API_ICONFIG_ICE
#define ODE_API_ICONFIG_ICE

#include <ode/Collections.ice>
#include <ode/ServicesF.ice>

module ode {

    module api {
        /**
         * Metadata gateway for the {@link ode.api.RenderingEngine} and
         * clients. This service provides all DB access that the rendering
         * engine needs as well as Pixels services to a client. It also allows
         * the rendering  engine to also be run external to the server (e.g.
         * client-side).
         *
         **/
        ["ami", "amd"] interface IPixels extends ServiceInterface
            {
                /**
                 * Retrieves the pixels metadata. The following objects are
                 * pre-linked:
                 * <ul>
                 * <li>pixels.pixelsType</li>
                 * <li>pixels.pixelsDimensions</li>
                 * <li>pixels.channels</li>
                 * <li>pixels.channnels.statsInfo</li>
                 * <li>pixels.channnels.colorComponent</li>
                 * <li>pixels.channnels.logicalChannel</li>
                 * <li>pixels.channnels.logicalChannel.photometricInterpretation</li>
                 * </ul>
                 *
                 * @param pixId Pixels id.
                 * @return Pixels object which matches <code>id</code>.
                 */
                idempotent ode::model::Pixels retrievePixDescription(long pixId) throws ServerError;

                /**
                 * Retrieves the rendering settings for a given pixels set and
                 * the currently logged in user. If the current user has no
                 * {@link ode.model.RenderingDef}, and the user is an
                 * administrator, then a {@link ode.model.RenderingDef} may
                 * be returned for the owner of the
                 *{@link ode.model.Pixels}. This matches the behavior of the
                 * Rendering service.
                 *
                 * The following objects will be pre-linked:
                 * <ul>
                 * <li>renderingDef.quantization</li>
                 * <li>renderingDef.model</li>
                 * <li>renderingDef.waveRendering</li>
                 * <li>renderingDef.waveRendering.color</li>
                 * <li>renderingDef.waveRendering.family</li>
                 * <li>renderingDef.spatialDomainEnhancement</li>
                 * </ul>
                 *
                 * @param pixId Pixels id.
                 * @return Rendering definition.
                 */
                idempotent ode::model::RenderingDef retrieveRndSettings(long pixId) throws ServerError;

                /**
                 * Retrieves the rendering settings for a given pixels set and
                 * the passed user. The following objects are pre-linked:
                 * <ul>
                 * <li>renderingDef.quantization</li>
                 * <li>renderingDef.model</li>
                 * <li>renderingDef.waveRendering</li>
                 * <li>renderingDef.waveRendering.color</li>
                 * <li>renderingDef.waveRendering.family</li>
                 * <li>renderingDef.spatialDomainEnhancement</li>
                 * </ul>
                 *
                 * @param pixId Pixels id.
                 * @param userId  The id of the user.
                 * @return Rendering definition.
                 **/
                idempotent ode::model::RenderingDef retrieveRndSettingsFor(long pixId, long userId) throws ServerError;

                /**
                 * Retrieves all the rendering settings for a given pixels set
                 * and the passed user. The following objects are pre-linked:
                 * <ul>
                 * <li>renderingDef.quantization</li>
                 * <li>renderingDef.model</li>
                 * <li>renderingDef.waveRendering</li>
                 * <li>renderingDef.waveRendering.color</li>
                 * <li>renderingDef.waveRendering.family</li>
                 * <li>renderingDef.spatialDomainEnhancement</li>
                 * </ul>
                 *
                 * @param pixId    Pixels id.
                 * @param userId   The id of the user.
                 * @return Rendering definition.
                 **/
                idempotent IObjectList retrieveAllRndSettings(long pixId, long userId) throws ServerError;

                /**
                 * Loads a specific set of rendering settings. The
                 * following objects are pre-linked:
                 * <ul>
                 * <li>renderingDef.quantization</li>
                 * <li>renderingDef.model</li>
                 * <li>renderingDef.waveRendering</li>
                 * <li>renderingDef.waveRendering.color</li>
                 * <li>renderingDef.waveRendering.family</li>
                 * <li>renderingDef.spatialDomainEnhancement</li>
                 * </ul>
                 *
                 * @param renderingSettingsId Rendering definition id.
                 * @throws ValidationException If no <code>RenderingDef</code>
                 * matches the ID <code>renderingDefId</code>.
                 * @return Rendering definition.
                 **/
                idempotent ode::model::RenderingDef loadRndSettings(long renderingSettingsId) throws ServerError;

                /**
                 * Saves the specified rendering settings.
                 *
                 * @param rndSettings Rendering settings.
                 **/
                void saveRndSettings(ode::model::RenderingDef rndSettings) throws ServerError;

                /**
                 * Bit depth for a given pixel type.
                 *
                 * @param type Pixels type.
                 * @return Bit depth in bits.
                 **/
                idempotent int getBitDepth(ode::model::PixelsType type) throws ServerError;

                /**
                 * Retrieves a particular enumeration for a given enumeration
                 * class.
                 *
                 * @param enumClass Enumeration class.
                 * @param value Enumeration string value.
                 * @return Enumeration object.
                 **/
                 ["deprecate:Use ITypes#getEnumeration(string, string) instead."]
                idempotent ode::model::IObject getEnumeration(string enumClass, string value) throws ServerError;

                /**
                 * Retrieves the exhaustive list of enumerations for a given
                 * enumeration class.
                 *
                 * @param enumClass Enumeration class.
                 * @return List of all enumeration objects for the
                 *         <i>enumClass</i>.
                 **/
                 ["deprecate:Use ITypes#allEnumerations(string) instead."]
                idempotent IObjectList getAllEnumerations(string enumClass) throws ServerError;

                /**
                 * Copies the metadata, and <b>only</b> the metadata linked to
                 * a Pixels object into a new Pixels object of equal or
                 * differing size across one or many of its three physical
                 * dimensions or temporal dimension.
                 * It is beyond the scope of this method to handle updates or
                 * changes to the raw pixel data available through
                 * {@link ode.api.RawPixelsStore} or to add
                 * and link {@link ode.model.PlaneInfo} and/or other Pixels
                 * set specific metadata.
                 * It is also assumed that the caller wishes the pixels
                 * dimensions and {@link ode.model.PixelsType} to remain the
                 * same; changing these is outside the scope of this method.
                 * <b>NOTE:</b> As {@link ode.model.Channel} objects are
                 * only able to apply to a single set of Pixels any
                 * annotations or linkage to these objects will be lost.
                 *
                 * @param pixelsId The source Pixels set id.
                 * @param sizeX The new size across the X-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeY The new size across the Y-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeZ The new size across the Z-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeT The new number of timepoints.
                 *              <code>null</code> if the copy should maintain
                 *              the same number.
                 * @param channelList The channels that should be copied into
                 *                    the new Pixels set.
                 * @param methodology An optional string signifying the
                 *                    methodology that will be used to produce
                 *                    this new Pixels set.
                 * @param copyStats Whether or not to copy the
                 *                  {@link ode.model.StatsInfo} for each
                 *                  channel.
                 * @return Id of the new Pixels object on success or
                 *         <code>null</code> on failure.
                 * @throws ValidationException If the X, Y, Z, T or
                 *         channelList dimensions are out of bounds or the
                 *         Pixels object corresponding to
                 *         <code>pixelsId</code> is unlocatable.
                 **/
                ode::RLong copyAndResizePixels(long pixelsId,
                                                 ode::RInt sizeX,
                                                 ode::RInt sizeY,
                                                 ode::RInt sizeZ,
                                                 ode::RInt sizeT,
                                                 ode::sys::IntList channelList,
                                                 string methodology,
                                                 bool copyStats) throws ServerError;

                /**
                 * Copies the metadata, and <b>only</b> the metadata linked to
                 * a Image object into a new Image object of equal or
                 * differing size across one or many of its three physical
                 * dimensions or temporal dimension.
                 * It is beyond the scope of this method to handle updates or
                 * changes to  the raw pixel data available through
                 * {@link ode.api.RawPixelsStore} or to add
                 * and link {@link ode.model.PlaneInfo} and/or other Pixels
                 * set specific metadata.
                 * It is also assumed that the caller wishes the pixels
                 * dimensions and {@link ode.model.PixelsType} to remain the
                 * same; changing these is outside the scope of this method.
                 * <b>NOTE:</b> As {@link ode.model.Channel} objects are
                 * only able to apply to a single set of Pixels any
                 * annotations or linkage to these objects will be lost.
                 *
                 * @param imageId The source Image id.
                 * @param sizeX The new size across the X-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeY The new size across the Y-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeZ The new size across the Z-axis.
                 *              <code>null</code> if the copy should maintain
                 *              the same size.
                 * @param sizeT The new number of timepoints.
                 *              <code>null</code> if the copy should maintain
                 *              the same number.
                 * @param channelList The channels that should be copied into
                 *                    the new Pixels set.
                 * @param methodology The name of the new Image.
                 * @param copyStats Whether or not to copy the
                 *                  {@link ode.model.StatsInfo} for each
                 *                  channel.
                 * @return Id of the new Pixels object on success or
                 *         <code>null</code> on failure.
                 * @throws ValidationException If the X, Y, Z, T or
                 *         channelList dimensions are out of bounds or the
                 *         Pixels object corresponding to
                 *         <code>pixelsId</code> is unlocatable.
                 */
                ode::RLong copyAndResizeImage(long imageId,
                                                ode::RInt sizeX,
                                                ode::RInt sizeY,
                                                ode::RInt sizeZ,
                                                ode::RInt sizeT,
                                                ode::sys::IntList channelList,
                                                string methodology,
                                                bool copyStats) throws ServerError;

                /**
                 * Creates the metadata, and <b>only</b> the metadata linked
                 * to an Image object. It is beyond the scope of this method
                 * to handle updates or changes to the raw pixel data
                 * available through {@link ode.api.RawPixelsStore} or to
                 * add and link {@link ode.model.PlaneInfo} or
                 * {@link ode.model.StatsInfo} objects and/or other Pixels
                 * set specific metadata. It is also up to the caller to
                 * update the pixels dimensions.
                 *
                 * @param sizeX The new size across the X-axis.
                 * @param sizeY The new size across the Y-axis.
                 * @param sizeZ The new size across the Z-axis.
                 * @param sizeT The new number of timepoints.
                 * @param pixelsType The pixelsType
                 * @param name The name of the new Image.
                 * @param description The description of the new Image.
                 * @return Id of the new Image object on success or
                 *         <code>null</code> on failure.
                 * @throws ValidationException If the channel list is
                 *         <code>null</code> or of size == 0.
                 */
                ode::RLong createImage(int sizeX, int sizeY, int sizeZ, int sizeT,
                                         ode::sys::IntList channelList,
                                         ode::model::PixelsType pixelsType,
                                         string name, string description) throws ServerError;

                /**
                 * Sets the channel global (all 2D optical sections
                 * corresponding to a particular channel) minimum and maximum
                 * for a Pixels set.
                 *
                 * @param pixelsId The source Pixels set id.
                 * @param channelIndex The channel index within the Pixels set.
                 * @param min The channel global minimum.
                 * @param max The channel global maximum.
                 */
                void setChannelGlobalMinMax(long pixelsId, int channelIndex, double min, double max) throws ServerError;
            };
    };
};

#endif