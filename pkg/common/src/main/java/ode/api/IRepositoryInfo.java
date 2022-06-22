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

import ode.conditions.InternalException;

/**
 * Provides methods for obtaining information for server repository disk space
 * allocation. Could be used generically to obtain usage information for any 
 * mount point, however, this interface is prepared for the API to provide 
 * methods to obtain usage info for the server filesystem containing the image
 * uploads. For the Bhojpur ODE server base this is /ODE. For this implementation 
 * it could be anything e.g. /Data1. 
 * <p>
 * Methods that fail or cannot execute on the server will throw an
 * InternalException. This would not be normal and would indicate some server or
 * disk failure.
 * </p>
 */
public interface IRepositoryInfo extends ServiceInterface {

	/**
	 * This method returns the total space in bytes for this file system
	 * including nested subdirectories.  The Java 6 J2SE provides this 
	 * functionality now using similar methods in the class java.io.File. A 
	 * refactoring of related classes should be performed when the later SDK 
	 * is adopted.
	 * 
	 * @return Total space used on this file system.
	 * @throws ResourceError If there is a problem retrieving disk space used.
	 */
	public long getUsedSpaceInKilobytes();

	/**
	 * This method returns the free or available space on this file system
	 * including nested subdirectories. The Java 6 J2SE provides this 
	 * functionality now using similar methods in the class java.io.File. A 
	 * refactoring of related classes should be performed when the later SDK 
	 * is adopted.
	 * 
	 * @return Free space on this file system in KB.
	 * @throws ResourceError If there is a problem retrieving disk space free.
	 */
	public long getFreeSpaceInKilobytes();

	/**
	 * This method returns a double of the used space divided by the free space.
	 * This method will be called by a client to watch the repository 
	 * filesystem so that it doesn't exceed 95% full.
	 * 
	 * @return Fraction of used/free.
	 * @throws ResourceError If there is a problem calculating the usage
	 * fraction.
	 */
	public double getUsageFraction();
	
    /**
     * Checks that image data repository has not exceeded 95% disk space use
     * level.
     * @throws ResourceError If the repository usage has exceeded 95%.
     * @throws InternalException If there is a critical failure while sanity
     * checking the repository.
     */
	public void sanityCheckRepository();
    
    /**
     * Removes all files from the server that do not have an OriginalFile
     * complement in the database, all the Pixels that do not have a complement
     * in the database and all the Thumbnail's that do not have a complement in
     * the database.
     * @throws ResourceError If deletion fails.
     */
    public void removeUnusedFiles();
}