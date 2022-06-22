package odeis.providers.re.data;

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

import java.io.Serializable;

/** 
 * Identifies a rectangular region.
 */
public class RegionDef 
	implements Serializable
{

	 /** The generated serial number */
	private static final long serialVersionUID = 2681169086599580818L;

	/** The x-coordinate of the top-left corner. */
	private int x;
	
	/** The y-coordinate of the top-left corner. */
	private int y;
	
	/** The width of the region. */
	private int width;
	
	/** The height of the region. */
	private int height;

	/** Creates a default instance. */
	public RegionDef()
	{
		this(0, 0, 0, 0);
	}
	
	/**
	 * Creates a rectangular region.
	 * 
	 * @param x The x-coordinate of the top-left corner.
	 * @param y The y-coordinate of the top-left corner.
	 * @param width The width of the region.
	 * @param height The height of the region.
	 */
	public RegionDef(int x, int y, int width, int height)
	{
		setHeight(height);
		setWidth(width);
		setX(x);
		setY(y);
	}
	
	/**
	 * Returns the x-coordinate of the top-left corner.
	 * 
	 * @return See above.
	 */
	public int getX() { return x; }
	
	/**
	 * Returns the y-coordinate of the top-left corner.
	 * 
	 * @return See above.
	 */
	public int getY() { return y; }
	
	/**
	 * Returns the width of the region.
	 * 
	 * @return See above.
	 */
	public int getWidth() { return width; }
	
	/**
	 * Returns the height of the region.
	 * 
	 * @return See above.
	 */
	public int getHeight() { return height; }
	
	/**
	 * Sets the x-coordinate of the top-left corner.
	 * 
	 * @param x The value to set.
	 */
	public void setX(int x)
	{
		if (x < 0) x = 0;
		this.x = x;
	}
	
	/**
	 * Sets the y-coordinate of the top-left corner.
	 * 
	 * @param y The value to set.
	 */
	public void setY(int y)
	{
		if (y < 0) y = 0;
		this.y = y;
	}
	
	/**
	 * Sets the width of the region.
	 * 
	 * @param width The value to set.
	 */
	public void setWidth(int width)
	{
		if (width < 0) width = 0;
		this.width = width;
	}
	
	/**
	 * Sets the height of the region.
	 * 
	 * @param height The value to set.
	 */
	public void setHeight(int height)
	{
		if (height < 0) height = 0;
		this.height = height;
	}
	
	/**
	 * Overridden to return the region as a string.
	 * @see Object#toString()
	 */
	public String toString()
	{
		return "x="+x+" y="+y+" width="+width+" height="+height;
	}
	
}