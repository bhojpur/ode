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

#ifndef ODE_CORE_RAWPIXELSSTORE_ICE
#define ODE_CORE_RAWPIXELSSTORE_ICE

#include <ode/ModelF.ice>
#include <ode/Collections.ice>
#include <ode/ROMIO.ice>
#include <ode/core/PyramidService.ice>

module ode {

    module core {

        /**
         * Binary data provider. Initialized with the ID of a
         * {@link ode.model.Pixels} instance, this service can provide
         * various slices, stacks, regions of the 5-dimensional (X-Y planes with
         * multiple Z-sections and Channels over Time). The byte array returned
         * by the getter methods and passed to the setter methods can and will
         * be interpreted according to results of {@code getByteWidth},
         * {@code isFloat}, and {@code isSigned}.
         *
         * <p>
         * <b>Read-only caveat</b>:
         *
         * Mutating methods (set*) are only available during the first access.
         * Once the Pixels data has been successfully saved (via the save or
         * close methods on this interface), then the data should be treated
         * read-only. If Pixels data writing fails and the service is
         * inadvertently closed, delete the Pixels object, and create a new
         * one. Any partially written data will be removed.
         */
        ["ami", "amd"] interface RawPixelsStore extends PyramidService
            {

                /**
                 * Initializes the stateful service for a given Pixels set.
                 *
                 * @param pixelsId Pixels set identifier.
                 * @param bypassOriginalFile Whether or not to bypass checking
                 *        for an original file to back the pixel buffer used
                 *        by this service. If requests are predominantly
                 *        <code>write-only</code> or involve the population of
                 *        a brand new pixel buffer using <code>true</code>
                 *        here is a safe optimization otherwise
                 *        <code>false</code> is expected.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                void setPixelsId(long pixelsId, bool bypassOriginalFile) throws ServerError;

                /**
                 * Returns the current Pixels set identifier.
                 * @return See above.
                 */
                idempotent long getPixelsId() throws ServerError;

                /**
                 * Returns the current Pixels path.
                 * @return See above.
                 */
                idempotent string getPixelsPath() throws ServerError;

                /**
                 * Prepares the stateful service with a cache of loaded Pixels
                 * objects.
                 * This method is designed to combat query overhead, where
                 * many sets of Pixels are to be read from or written to, by
                 * loading all the Pixels sets at once. Multiple calls will
                 * result in the existing cache being overwritten.
                 *
                 * @param pixelsIds Pixels IDs to cache.
                 */
                idempotent void prepare(ode::sys::LongList pixelsIds) throws ServerError;

                /**
                 * Retrieves the in memory size of a 2D image plane in this
                 * pixel store.
                 * @return 2D image plane size in bytes
                 *            <code>sizeX*sizeY*byteWidth</code>.
                 */
                idempotent long getPlaneSize() throws ServerError;

                /**
                 * Retrieves the in memory size of a row or scanline of pixels
                 * in this pixel store.
                 * @return row or scanline size in bytes
                 *         <code>sizeX*byteWidth</code>
                 */
                idempotent int getRowSize() throws ServerError;

                /**
                 * Retrieves the in memory size of the entire number of
                 * optical sections for a <b>single</b> wavelength or channel
                 * at a particular timepoint in this pixel store.
                 *
                 * @return stack size in bytes
                 *         <code>sizeX*sizeY*byteWidth</code>.
                 */
                idempotent long getStackSize() throws ServerError;

                /**
                 * Retrieves the in memory size of the entire number of
                 * optical sections for <b>all</b> wavelengths or channels at
                 * a particular timepoint in this pixel store.
                 * @return timepoint size in bytes
                 *         <code>sizeX*sizeY*sizeZ*sizeC*byteWidth</code>.
                 */
                idempotent long getTimepointSize() throws ServerError;

                /**
                 * Retrieves the in memory size of the entire pixel store.
                 * @return total size of the pixel size in bytes
                 *         <code>sizeX*sizeY*sizeZ*sizeC*sizeT*byteWidth</code>.
                 */
                idempotent long getTotalSize() throws ServerError;

                /**
                 * Retrieves the offset for a particular row or scanline in
                 * this pixel store.
                 * @param y offset across the Y-axis of the pixel buffer.
                 * @param z offset across the Z-axis of the pixel buffer.
                 * @param c offset across the C-axis of the pixel buffer.
                 * @param t offset across the T-axis of the pixel buffer.
                 * @return offset of the row or scanline.
                 */
                idempotent long getRowOffset(int y, int z, int c, int t) throws ServerError;

                /**
                 * Retrieves the offset for a particular 2D image plane in this pixel
                 * store.
                 * @param z offset across the Z-axis of the pixel buffer.
                 * @param c offset across the C-axis of the pixel buffer.
                 * @param t offset across the T-axis of the pixel buffer.
                 * @return offset of the 2D image plane.
                 */
                idempotent long getPlaneOffset(int z, int c, int t) throws ServerError;

                /**
                 * Retrieves the offset for the entire number of optical
                 * sections for a <b>single</b> wavelength or channel at a
                 * particular timepoint in this pixel store.
                 *
                 * @param c offset across the C-axis of the pixel buffer.
                 * @param t offset across the T-axis of the pixel buffer.
                 * @return offset of the stack.
                 */
                idempotent long getStackOffset(int c, int t) throws ServerError;

                /**
                 * Retrieves the in memory size of the entire number of
                 * optical sections for <b>all</b> wavelengths or channels at
                 * a particular timepoint in this pixel store.
                 *
                 * @param t offset across the T-axis of the pixel buffer.
                 * @return offset of the timepoint.
                 */
                idempotent long getTimepointOffset(int t) throws ServerError;

                /**
                 * Retrieves a tile from this pixel buffer.
                 * @param z offset across the Z-axis of the pixel buffer.
                 * @param c offset across the C-axis of the pixel buffer.
                 * @param t offset across the T-axis of the pixel buffer.
                 * @param x Top left corner of the tile, X offset.
                 * @param y Top left corner of the tile, Y offset.
                 * @param w Width of the tile.
                 * @param h Height of the tile.
                 * @return buffer containing the data.
                 */
                idempotent Ice::ByteSeq getTile(int z, int c, int t, int x, int y, int w, int h) throws ServerError;

                /**
                 * Retrieves a n-dimensional block from this pixel store.
                 * @param offset offset for each dimension within pixel store.
                 * @param size of each dimension (dependent on dimension).
                 * @param step needed of each dimension (dependent on
                 *             dimension).
                 * @return buffer containing the data.
                 */
                idempotent Ice::ByteSeq getHypercube(ode::sys::IntList offset, ode::sys::IntList size, ode::sys::IntList step) throws ServerError;

                /**
                 * Retrieves a region from this pixel store.
                 * @param size byte width of the region to retrieve.
                 * @param offset offset within the pixel store.
                 * @return buffer containing the data.
                 */
                idempotent Ice::ByteSeq getRegion(int size, long offset) throws ServerError;

                /**
                 * Retrieves a particular row or scanline from this pixel store.
                 * @param y offset across the Y-axis of the pixel store.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @return buffer containing the data which comprises this row
                 *                or scanline.
                 */
                idempotent Ice::ByteSeq getRow(int y, int z, int c, int t) throws ServerError;

                /**
                 * Retrieves a particular column from this pixel store.
                 * @param x offset across the X-axis of the pixel store.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @return buffer containing the data which comprises this column.
                 */
                idempotent Ice::ByteSeq getCol(int x, int z, int c, int t) throws ServerError;

                /**
                 * Retrieves a particular 2D image plane from this pixel store.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @return buffer containing the data which comprises this 2D image plane.
                 */
                idempotent Ice::ByteSeq getPlane(int z, int c, int t) throws ServerError;

                /**
                 * Retrieves a region from a given plane from this pixel store.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @param size the number of pixels to retrieve.
                 * @param offset the offset at which to retrieve <code>size</code> pixels.
                 * @return buffer containing the data which comprises the region of the
                 * given 2D image plane. It is guaranteed that this buffer will have been
                 * byte swapped.
                 */
                idempotent Ice::ByteSeq getPlaneRegion(int z, int c, int t, int size, int offset) throws ServerError;

                /**
                 * Retrieves the the entire number of optical sections for a <b>single</b>
                 * wavelength or channel at a particular timepoint in this pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @return buffer containing the data which comprises this stack.
                 */
                idempotent Ice::ByteSeq getStack(int c, int t) throws ServerError;

                /**
                 * Retrieves the entire number of optical sections for <b>all</b>
                 * wavelengths or channels at a particular timepoint in this pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 * @return buffer containing the data which comprises this stack.
                 */
                idempotent Ice::ByteSeq getTimepoint(int t) throws ServerError;

                /**
                 * Sets a tile in this pixel buffer.
                 * @param buf A byte array of the data.
                 * @param z offset across the Z-axis of the pixel buffer.
                 * @param c offset across the C-axis of the pixel buffer.
                 * @param t offset across the T-axis of the pixel buffer.
                 * @param x Top left corner of the tile, X offset.
                 * @param y Top left corner of the tile, Y offset.
                 * @param w Width of the tile.
                 * @param h Height of the tile.
                 * @throws IOException if there is a problem writing to the pixel buffer.
                 * @throws BufferOverflowException if an attempt is made to write off the
                 * end of the file.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setTile(Ice::ByteSeq buf, int z, int c, int t, int x, int y, int w, int h) throws ServerError;

                /**
                 * Sets a region in this pixel buffer.
                 * @param size byte width of the region to set.
                 * @param offset offset within the pixel buffer.
                 * @param buf a byte array of the data.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setRegion(int size, long offset, Ice::ByteSeq buf) throws ServerError;

                /**
                 * Sets a particular row or scanline in this pixel store.
                 * @param buf a byte array of the data comprising this row or scanline.
                 * @param y offset across the Y-axis of the pixel store.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setRow(Ice::ByteSeq buf, int y, int z, int c, int t) throws ServerError;

                /**
                 * Sets a particular 2D image plane in this pixel store.
                 * @param buf a byte array of the data comprising this 2D image plane.
                 * @param z offset across the Z-axis of the pixel store.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setPlane(Ice::ByteSeq buf, int z, int c, int t) throws ServerError;

                /**
                 * Sets the entire number of optical sections for a <b>single</b>
                 * wavelength or channel at a particular timepoint in this pixel store.
                 * @param buf a byte array of the data comprising this stack.
                 * @param c offset across the C-axis of the pixel store.
                 * @param t offset across the T-axis of the pixel store.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setStack(Ice::ByteSeq buf, int z, int c, int t) throws ServerError;

                /**
                 * Sets the entire number of optical sections for <b>all</b>
                 * wavelengths or channels at a particular timepoint in this pixel store.
                 * @param buf a byte array of the data comprising this timepoint.
                 * @param t offset across the T-axis of the pixel buffer.
                 *
                 * See <b>Read-only caveat</b> under {@link RawPixelsStore}
                 */
                idempotent void setTimepoint(Ice::ByteSeq buf, int t) throws ServerError;

                /**
                 * Retrieves the histogram data for the specified plane and channels. This method can currently only handle non-pyramid images.
                 * @param channels the channels to generate the histogram data for
                 * @param binCount the number of the histogram bins (optional, default: 256)
                 * @param plane the plane (optional, default: whole region of first z/t plane)
                 * @param globalRange use the global minimum/maximum to determine the histogram range, otherwise use plane minimum/maximum value
                 * @return See above.
                 */
                idempotent IntegerIntegerArrayMap getHistogram(IntegerArray channels, int binCount, bool globalRange, ode::romio::PlaneDef plane) throws ServerError;
                
                /**
                 * Find the minimum and maximum pixel values for the specified channels by iterating over a full plane.
                 * In case of multi-z/t images only the 'middle' plane with index maxZ/2, respectively maxT/2 is taken into account.
                 * Note: This method can currently only handle non-pyramid images, otherwise an empty map will be returned.
                 * @param channels the channels
                 * @return See above.
                 */
                idempotent IntegerDoubleArrayMap findMinMax(IntegerArray channels) throws ServerError;
                
                /**
                 * Returns the byte width for the pixel store.
                 * @return See above.
                 */
                idempotent int getByteWidth() throws ServerError;

                /**
                 * Returns whether or not the pixel store has signed pixels.
                 * @return See above.
                 */
                idempotent bool isSigned() throws ServerError;

                /**
                 * Returns whether or not the pixel buffer has floating point pixels.
                 * @return See above.
                 */
                idempotent bool isFloat() throws ServerError;

                /**
                 * Calculates a SHA-1 message digest for the entire pixel store.
                 * @return byte array containing the message digest.
                 */
                idempotent Ice::ByteSeq calculateMessageDigest() throws ServerError;

                /**
                 * Save the current state of the pixels, updating the SHA1. This should
                 * only be called AFTER all data is successfully set. Future invocations
                 * of set methods may be disallowed. This read-only status will allow
                 * background processing (generation of thumbnails, compression, etc.)
                 * to begin. More information under {@link RawPixelsStore}.
                 *
                 * A null instance will be returned if no save was performed.
                 *
                 */
                idempotent ode::model::Pixels save() throws ServerError;

            };

    };
};

#endif