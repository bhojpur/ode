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

import ode.annotations.Validate;
import ode.model.IObject;
import ode.model.core.Channel;
import ode.model.core.Pixels;
import ode.model.core.PlaneInfo;
import ode.model.display.RenderingDef;
import ode.model.enums.PixelsType;
import ode.model.stats.StatsInfo;

/**
 * Metadata gateway for the {@link odeis.providers.re.RenderingEngine} and
 * clients. This service provides all DB access that the rendering engine 
 * needs as well as Pixels services to a client. It also allows the rendering 
 * engine to also be run external to the server (e.g. client-side).
 */
public interface IPixels extends ServiceInterface 
{
    /**
     * Retrieves the pixels metadata with the following objects pre-linked:
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
     * @param pixelsId
     *            Pixels id.
     * @return Pixels object which matches <i>id</i>.
     */
    public Pixels retrievePixDescription(long pixelsId);

    /**
     * Retrieves the rendering settings for a given pixels set and the currently
     * logged in user. If the current user has no {@link RenderingDef}, and the
     * user is an administrator, then a {@link RenderingDef} may be returned
     * for the owner of the {@link Pixels}. This matches the behavior of the
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
     * @param pixelsId
     *            Pixels id.
     * @return Rendering definition.
     */
    public RenderingDef retrieveRndSettings(long pixelsId);
    
    /**
     * Retrieves the rendering settings for a given pixels set and the passed
     * user with the following objects pre-linked:
     * <ul>
     * <li>renderingDef.quantization</li>
     * <li>renderingDef.model</li>
     * <li>renderingDef.waveRendering</li>
     * <li>renderingDef.waveRendering.color</li>
     * <li>renderingDef.waveRendering.family</li>
     * <li>renderingDef.spatialDomainEnhancement</li>
     * </ul>
     * 
     * @param pixelsId
     *            Pixels id.
     * @param userID 
     * 			  The id of the user.
     * @return Rendering definition.
     */
    public RenderingDef retrieveRndSettingsFor(long pixelsId, long userID);
    
    /**
     * Retrieves all the rendering settings for a given pixels set and the 
     * passed user with the following objects pre-linked:
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
     */
    public List<IObject> retrieveAllRndSettings(long pixId, long userId);
    
    /**
     * Loads a specific set of rendering settings with the following objects 
     * pre-linked:
     * <ul>
     * <li>renderingDef.quantization</li>
     * <li>renderingDef.model</li>
     * <li>renderingDef.waveRendering</li>
     * <li>renderingDef.waveRendering.color</li>
     * <li>renderingDef.waveRendering.family</li>
     * <li>renderingDef.spatialDomainEnhancement</li>
     * </ul>
     * 
     * @param renderingDefId
     *            Rendering definition id.
     * @throws ValidationException If no <code>RenderingDef</code> matches the
     * ID <code>renderingDefId</code>.
     * @return Rendering definition.
     */
    public RenderingDef loadRndSettings(long renderingDefId);
    
    /**
     * Copies the metadata, and <b>only</b> the metadata linked to a Pixels
     * object into a new Pixels object of equal or differing size across one
     * or many of its three physical dimensions or temporal dimension.
     * It is beyond the scope of this method to handle updates or changes to
     * the raw pixel data available through {@link RawPixelsStore} or to add
     * and link {@link ode.model.core.PlaneInfo} and/or other Pixels set
     * specific metadata.
     * It is also assumed that the caller wishes the pixels dimensions and
     * {@link PixelsType} to remain the same;
     * changing these is outside the scope of this method. <b>NOTE:</b> As
     * {@link ode.model.core.Channel} objects are only able to apply to a
     * single set of Pixels any annotations or linkage to these objects will
     * be lost.
     * 
     * @param pixelsId The source Pixels set id.
     * @param sizeX The new size across the X-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeY The new size across the Y-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeZ The new size across the Z-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeT The new number of timepoints. <code>null</code> if the
     * copy should maintain the same number.
     * @param channelList The channels that should be copied into the new Pixels
     * set.
     * @param methodology An optional string signifying the methodology that
     * will be used to produce this new Pixels set.
     * @param copyStats Whether or not to copy the {@link StatsInfo} for each
     * channel.
     * @return Id of the new Pixels object on success or <code>null</code> on
     * failure.
     * @throws ValidationException If the X, Y, Z, T or channelList dimensions 
     * are out of bounds or the Pixels object corresponding to 
     * <code>pixelsId</code> is unlocatable. 
     */
    public Long copyAndResizePixels(long pixelsId, Integer sizeX, Integer sizeY,
                                    Integer sizeZ, Integer sizeT, 
                                    @Validate(Integer.class)
                                    List<Integer> channelList,
                                    String methodology, boolean copyStats);
    
    /**
     * Copies the metadata, and <b>only</b> the metadata linked to a Image
     * object into a new Image object of equal or differing size across one
     * or many of its three physical dimensions or temporal dimension.
     * It is beyond the scope of this method to handle updates or changes to 
     * the raw pixel data available through {@link RawPixelsStore} or to add 
     * and link {@link PlaneInfo} and/or other Pixels set specific metadata. 
     * It is also assumed that the caller wishes the pixels dimensions and
     * {@link PixelsType} to remain the same;
     * changing these is outside the scope of this method. <b>NOTE:</b> As 
     * {@link Channel} objects are only able to apply to a single set of
     * Pixels any annotations or linkage to these objects will be lost.
     * 
     * @param imageId The source Image id.
     * @param sizeX The new size across the X-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeY The new size across the Y-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeZ The new size across the Z-axis. <code>null</code> if the
     * copy should maintain the same size.
     * @param sizeT The new number of timepoints. <code>null</code> if the
     * copy should maintain the same number.
     * @param channelList The channels that should be copied into the new Pixels
     * set.
     * @param name The name of the new Image.
     * @param copyStats Whether or not to copy the {@link StatsInfo} for each
     * channel.
     * @return Id of the new Pixels object on success or <code>null</code> on
     * failure.
     * @throws ValidationException If the X, Y, Z, T or channelList dimensions 
     * are out of bounds or the Pixels object corresponding to 
     * <code>pixelsId</code> is unlocatable. 
     */
    public Long copyAndResizeImage(long imageId, Integer sizeX, Integer sizeY,
                                   Integer sizeZ, Integer sizeT, 
                                   @Validate(Integer.class)
                                   List<Integer> channelList,
                                   String name, boolean copyStats);
    
    /**
     * Creates the metadata, and <b>only</b> the metadata linked to an Image
     * object. It is beyond the scope of this method to handle updates or 
     * changes to the raw pixel data available through {@link RawPixelsStore} 
     * or to add and link {@link PlaneInfo} or {@link StatsInfo} objects
     * and/or other Pixels set specific metadata. It is also up to the caller
     * to update the pixels dimensions.
     * 
     * @param sizeX The new size across the X-axis.
     * @param sizeY The new size across the Y-axis.
     * @param sizeZ The new size across the Z-axis.
     * @param sizeT The new number of timepoints.
     * @param channelList The channels (emission wavelength in nanometers) that 
     * should be added to the new Pixels set.
     * @param name The name of the new Image.
     * @param description The description of the new Image.
     * @return Id of the new Image object on success or <code>null</code> on
     * failure.
     * @throws ValidationException If the channel list is <code>null</code> or 
     * of size == 0.
     */
    public Long createImage(int sizeX, int sizeY,int sizeZ, int sizeT,
    		                @Validate(Integer.class) List<Integer> channelList,
    		                PixelsType pixelType, String name,
    		                String description);
    
    /**
     * Sets the channel global (all 2D optical sections corresponding to a 
     * particular channel) minimum and maximum for a Pixels set.
     * @param pixelsId The source Pixels set id.
     * @param channelIndex The channel index within the Pixels set.
     * @param min The channel global minimum.
     * @param max The channel global maximum.
     */
    public void setChannelGlobalMinMax(long pixelsId, int channelIndex,
                                       double min, double max);

    /**
     * Saves the specified rendering settings.
     * 
     * @param rndSettings
     *            Rendering settings.
     */
    public void saveRndSettings(RenderingDef rndSettings);

    /**
     * Bit depth for a given pixel type.
     * 
     * @param type
     *            Pixels type.
     * @return Bit depth in bits.
     */
    public int getBitDepth(PixelsType type);

    /**
     * Retrieves a particular enumeration for a given enumeration class.
     * 
     * @param klass
     *            Enumeration class.
     * @param value
     *            Enumeration string value.
     * @return Enumeration object.
     */
    public <T extends IObject> T getEnumeration(Class<T> klass, String value);

    /**
     * Retrieves the exhaustive list of enumerations for a given enumeration
     * class.
     * 
     * @param klass
     *            Enumeration class.
     * @return List of all enumeration objects for the <i>klass</i>.
     */
    public <T extends IObject> List<T> getAllEnumerations(Class<T> klass);
    	
}