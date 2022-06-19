package ode.tools;

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

import java.util.ArrayList;
import java.util.List;

import ode.api.IRepositoryInfo;
import ode.util.SqlAction;

/**
 * Class implementation of various mechanized tasks, database queries, file I/O,
 * etc. This class is used by the public services provided by IRepositoryInfo
 */
public class RepositoryTask {

	final private SqlAction sql;

	public RepositoryTask(SqlAction sql) {
	    this.sql = sql;
	}
	
	/**
	 * This public method is used to return a list of file ids that require
	 * deletion from the disk repository.
	 * 
	 * @return List<Long> representing the ids for files that were deleted
	 */
	public List<Long> getFileIds() {
	    return sql.getDeletedIds("ode.model.core.OriginalFile");
	}
	
	/**
	 * This public method is used to return a list of pixel ids that require
	 * deletion from the disk repository.
	 * 
	 * @return List<Long> representing the ids for pixels that were deleted
	 */
	public List<Long> getPixelIds() {
	    return sql.getDeletedIds("ode.model.core.Pixels");
	}
	
	/**
	 * This public method is used to return a list of thumbnail ids that require
	 * deletion from the disk repository.
	 * 
	 * @return List<Long> representing the ids for thumbnails that were deleted
	 */
	public List<Long> getThumbnailIds() {
	    return sql.getDeletedIds("ode.model.display.Thumbnail");
	}
}