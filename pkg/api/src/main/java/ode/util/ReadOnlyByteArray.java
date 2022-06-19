package ode.util;

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

/** 
 * A read-only slice of a given array.
 * Given a <code>base</code> array and an interval <code>[offset, offset+length]
 * </code> contained within <code>[0, base.length]</code>, an instance of this
 * class provides read access to the elements from <code>base[offset]</code> to 
 * <code>base[offset+length-1]</code>.  However, you access the elements by
 * specifying relative indeces to the {@link #get(int) get} method &#151; so
 * <code>get(0)</code> returns <code>base[offset]</code>, <code>get(1)</code>
 * returns <code>base[offset+1]</code>, and so on.  One thing to bear in mind
 * is that changes to the original <code>base</code> array will be reflected
 * to the corresponding <code>ReadOnlyByteArray</code> object as this class
 * simply keeps a reference to the original <code>base</code> array, without
 * making an internal copy.  This can be useful in those situations when you
 * want to emulate a memory pointer. 
 */
public class ReadOnlyByteArray 
{

	/** The original array. */
	protected final byte[]	base;
	
	/** Marks the start of the slice. */
	protected final int		offset;
	
	/** The length of the slice. */
	public final int		length;
	
	
	/**
	 * Makes sure that <code>i</code> is in <code>[0, length)</code>.
	 * Throws an exception if this constraint is not met.
	 * 
	 * @param i		The index to verify.
	 */
	protected void checkIndex(int i)
	{
		if (i < 0 || length <= i)
			throw new ArrayIndexOutOfBoundsException(
				"Index not in [0, "+length+"): "+i+".");
	}
	
	/**
	 * Creates a read-only slice of <code>base</code>.
	 * The <code>offset</code> argument marks the start of the slice, at
	 * <code>base[offset]</code>.  The <code>length</code> argument defines
	 * the length of the slice, the last element being 
	 * <code>base[offset+length-1]</code>.  Obviously enough, these two
	 * arguments must define an interval  
	 * <code>[offset, offset+length]</code> in <code>[0, base.length]</code>.
	 * 
	 * @param base	The original array.
	 * @param offset The start of the slice.
	 * @param length	The length of the slice.
	 */
	public ReadOnlyByteArray(byte[] base, int offset, int length)
	{
		if (base == null) throw new NullPointerException("No base.");
		if (offset < 0 || length < 0 || base.length < offset+length)
			throw new IllegalArgumentException(
				"[offset="+offset+", offset+length="+(offset+length)+
				"] not in [0, base.length="+base.length+"].");
		this.base = base;
		this.offset = offset;
		this.length = length;
	}
	
	/**
	 * Reads the element at the <code>index</code> position within this
	 * slice.
	 * 
 	 * @param index	The index. Must be in the <code>[0, {@link #length})
     *              </code> interval.
	 * @return	The element at the <code>index</code> position in this slice.
	 */
	public byte get(int index)
	{
		checkIndex(index);
		return base[offset+index];
	}

}