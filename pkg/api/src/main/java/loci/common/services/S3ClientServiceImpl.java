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

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.ObjectStat;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * An S3 client
 */
public class S3ClientServiceImpl extends AbstractService implements S3ClientService {

  /**
   * Minio client
   */
  private MinioClient s3Client;

  /**
   * Default constructor.
   */
  public S3ClientServiceImpl() {
    checkClassDependency(io.minio.MinioClient.class);
  }

  // -- S3ClientService methods

  @Override
  public void initialize(String server, int port, String accessKey, String secretKey,
                         String appName, String appVersion)
      throws S3ClientServiceException {
    try {
      s3Client = new MinioClient(server, port, accessKey, secretKey);
      s3Client.setAppInfo(appName, appVersion);
    } catch (MinioException e) {
      throw new S3ClientServiceException(e);
    }
  }

  @Override
  public boolean bucketExists(String bucket) throws S3ClientServiceException, IOException {
    try {
      return s3Client.bucketExists(bucket);
    }
    catch (
        MinioException |
        InvalidKeyException |
        NoSuchAlgorithmException |
        XmlPullParserException e) {
      throw new S3ClientServiceException(e);
    }
  }

  @Override
  public S3ClientStat statObject(String bucket, String object) throws S3ClientServiceException, IOException {
    try {
      ObjectStat mcstat = s3Client.statObject(bucket, object);
      return new S3ClientStat(mcstat.length());
    }
    catch (
        MinioException |
        InvalidKeyException |
        NoSuchAlgorithmException |
        XmlPullParserException e) {
      throw new S3ClientServiceException(e);
    }
  }

  @Override
  public InputStream getObject(String bucket, String object, long offset) throws S3ClientServiceException, IOException {
    try {
      return s3Client.getObject(bucket, object, offset);
    }
    catch (
        InvalidKeyException |
        MinioException |
        NoSuchAlgorithmException |
        XmlPullParserException e) {
      throw new S3ClientServiceException(e);
    }
  }

  @Override
  public void getObject(String bucket, String object, String filename) throws S3ClientServiceException, IOException {
    try {
      s3Client.getObject(bucket, object, filename);
    }
    catch (
        InvalidKeyException |
        MinioException |
        NoSuchAlgorithmException |
        XmlPullParserException e) {
      throw new S3ClientServiceException(e);
    }
  }

}