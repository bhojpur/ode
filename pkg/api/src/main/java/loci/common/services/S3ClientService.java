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

import java.io.IOException;
import java.io.InputStream;

/**
 * An S3 client
 */
public interface S3ClientService extends Service {

  /**
   * Initialise the S3 client
   * @param server servername
   * @param port port
   * @param accessKey access key
   * @param secretKey secret key
   * @param appName user agent application name
   * @param appVersion user agent application version
   * @throws S3ClientServiceException if an S3 error occurred
   */
  void initialize(String server, int port, String accessKey, String secretKey,
                  String appName, String appVersion)
      throws S3ClientServiceException;

  /**
   * Check whether a bucket exists
   * @param bucket Bucket name
   * @return true if bucket exists
   * @throws S3ClientServiceException if an S3 error occurred
   * @throws IOException if an S3 error occurred
   */
  boolean bucketExists(String bucket) throws S3ClientServiceException, IOException;

  /**
   * Stat the object
   * @param bucket Bucket name
   * @param object Object path
   * @return S3ClientStat object
   * @throws S3ClientServiceException if an S3 error occurred
   * @throws IOException if an S3 error occurred
   */
  S3ClientStat statObject(String bucket, String object) throws S3ClientServiceException, IOException;

  /**
   * Read an object
   * @param bucket Bucket name
   * @param object Object path
   * @param offset Start reading at this offset
   * @return InputStream to the object
   * @throws S3ClientServiceException if an S3 error occurred
   * @throws IOException if an S3 error occurred
   */
  InputStream getObject(String bucket, String object, long offset) throws S3ClientServiceException, IOException;

  /**
   * Download an object
   * @param bucket Bucket name
   * @param object Object path
   * @param filename Destination file
   * @throws S3ClientServiceException if an S3 error occurred
   * @throws IOException if an S3 error occurred
   */
  void getObject(String bucket, String object, String filename) throws S3ClientServiceException, IOException;

}