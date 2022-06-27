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

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class represents an LSID as used by the ODE-XML data model.
 */
public class LSID
{
	/** Concrete Java class which qualifies the type of object. */
	private Class klass;
	
	/** Indexes within the ODE-XML data model. */
	private int[] indexes;
	
	/** The LSID as a string. */
	private String asString;
	
	/** Our hash code. */
	private int hashCode;
	
	/** 
	 * The collator that we use to alphabetically sort by class name
	 * within a given level of the ODE-XML hierarchy.
	 */
	private final RuleBasedCollator stringComparator = 
		(RuleBasedCollator) Collator.getInstance(Locale.ENGLISH);
	
	/**
	 * Default constructor.
	 * @param klass Concrete Java class which qualifies the type of object
	 * this LSID represents.
	 * @param indexes Indexes for this LSID within the ODE-XML data model.
	 */
	public LSID(Class klass, int... indexes)
	{
		this.klass = klass;
		this.indexes = indexes;
        asString = klass.getName();
        for (int index : indexes)
        {
            asString = asString + ":" + index;
        }
        hashCode = asString.hashCode();
	}
	
	/**
	 * Constructor for non-standard LSIDs.
	 * @param asString The LSID as a string.
	 */
	public LSID(String asString)
	{
		this.asString = asString;
		hashCode = asString.hashCode();
	}
	
	/**
	 * Constructor for standard LSIDs that should be parsed.
	 * @param asString The LSID as a string.
	 * @param parse Whether or not to parse the LSID.
	 */
	public LSID(String asString, boolean parse)
	{
		this.asString = asString;
		hashCode = asString.hashCode();
		if (parse)
		{
			klass = parseJavaClass();
			indexes = parseIndexes();
		}
	}
	
	/**
	 * Returns the Java class which qualifies the type of object this
	 * LSID represents.
	 * @return See above.
	 */
	public Class getJavaClass()
	{
		return klass;
	}
	
	/**
	 * Attempts to parse and return the concrete Java class for the LSID from
	 * the LSID's string representation.
	 * @return See above. <code>null</code> if the concrete class cannot be
	 * parsed. 
	 */
	public Class parseJavaClass()
	{
		int colonIndex = asString.indexOf(":");
		if (colonIndex > -1)
		{
			try
			{
				return Class.forName(asString.substring(0, colonIndex));
			}
			catch (ClassNotFoundException e)
			{
				// No-op. We return null below.
			}
		}
		return null;
	}
	
	/**
	 * Attempts to parse and return the indexes for the LSID parsed from the
	 * LSID's string representation.
	 * @return See above.
	 */
	public int[] parseIndexes()
	{
		List<Integer> indexList = new ArrayList<Integer>();
		int colonIndex = asString.indexOf(":");
		while (colonIndex > - 1)
		{
			int nextIndex = asString.indexOf(":", colonIndex + 1);
			if (nextIndex > -1)
			{
				String s = asString.substring(colonIndex + 1, nextIndex);
				indexList.add(Integer.parseInt(s));
				colonIndex = nextIndex;
			}
			else
			{
				String s = asString.substring(colonIndex + 1);
				indexList.add(Integer.parseInt(s));
				break;
			}
		}
		int[] toReturn = new int[indexList.size()];
		for (int i = 0; i < indexList.size(); i++)
		{
			toReturn[i] = indexList.get(i);
		}
		return toReturn;
	}
	
	/**
	 * Returns the indexes for this LSID within the ODE-XML data model.
	 * @return See above.
	 */
	public int[] getIndexes()
	{
		return indexes;
	}
	
	@Override
	public String toString()
	{
		return asString;
	}
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof LSID)
		{
			LSID comparator = (LSID) obj;
			Class comparatorClass = comparator.getJavaClass();
			if (comparatorClass == null || klass == null)
			{
				return stringComparator.compare(asString, obj.toString()) == 0;
			}
			if (comparatorClass.equals(klass))
			{
				int[] comparatorIndexes = comparator.getIndexes();
				if (indexes.length != comparatorIndexes.length)
				{
					// Handle cases where a given LSID class may have
					// multiple paths with different index counts.
					return false;
				}
				for (int i = 0; i < indexes.length; i++)
				{
					if (indexes[i] != comparatorIndexes[i])
					{
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}
}