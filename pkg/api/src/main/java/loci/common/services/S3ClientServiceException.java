package loci.common.services;

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
 * Exception thrown when internal error specific to the S3 client is raised
 */
public class S3ClientServiceException extends ServiceException
{
  /**
   * Constructor.
   * @param message Error message.
   */
  public S3ClientServiceException(String message)
  {
    super(message);
  }

  /**
   * Constructor.
   * @param message Error message. 
   * @param cause Upstream exception.
   */
  public S3ClientServiceException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Constructor.
   * @param cause Upstream exception.
   */
  public S3ClientServiceException(Throwable cause)
  {
    super(cause);
  }
}