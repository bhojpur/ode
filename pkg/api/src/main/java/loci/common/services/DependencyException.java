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
 * Exception thrown when there is an object instantiation error or error
 * processing dependencies.
 */
public class DependencyException extends Exception
{
  /** Serial for this version. */
  private static final long serialVersionUID = -7836244849086491562L;
  
  /** The class that was used in a failed instantiation. */
  private Class<? extends Service> failureClass;

  /**
   * Default constructor.
   * @param message Error message.
   */
  public DependencyException(String message)
  {
    super(message);
  }

  /**
   * Default constructor.
   * @param message Error message. 
   * @param klass Failed instantiation class.
   */
  public DependencyException(String message, Class<? extends Service> klass)
  {
    super(message);
    this.failureClass = klass;
  }

  /**
   * Default constructor.
   * @param message Error message. 
   * @param klass Failed instantiation class.
   * @param cause Upstream exception.
   */
  public DependencyException(String message, Class<? extends Service> klass,
      Throwable cause)
  {
    super(message, cause);
    this.failureClass = klass;
  }

  /**
   * Default constructor.
   * @param cause Upstream exception.
   */
  public DependencyException(Throwable cause)
  {
    super(cause);
  }

  /**
   * Returns the class that was used during a failed instantiation.
   * @return See above.
   */
  public Class<? extends Service> getFailureClass()
  {
    return failureClass;
  }

  @Override
  public String toString()
  {
    if (failureClass == null)
    {
      return getMessage();
    }
    return getMessage() + " for " + failureClass;
  }
}