package loci.common;

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import loci.common.services.DependencyException;
import loci.common.services.S3ClientService;
import loci.common.services.S3ClientServiceException;
import loci.common.services.S3ClientStat;

import loci.common.services.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides random access to S3 buckets using the IRandomAccess interface.
 * Instances of S3Handle are read-only.
 *
 * @see IRandomAccess
 * @see StreamHandle
 * @see java.net.URLConnection
 *
 */
public class S3Handle extends StreamHandle {

  /**
   * An S3 IOException that was not thrown immediately
   */
  class DelayedObjectNotFound extends IOException {
    DelayedObjectNotFound(S3Handle s3) {
      super(String.format("Object not found: [%s] %s", s3, s3.objectNotFound), s3.objectNotFound);
    }
  }

  /** Default protocol for fetching s3:// */
  public final static String DEFAULT_S3_PROTOCOL = "https";

  private static final Logger LOGGER = LoggerFactory.getLogger(S3Handle.class);

  protected final static Pattern SCHEME_PARSER = Pattern.compile("s3(\\+\\p{Alnum}+)?(://.*)?");

  /** S3 configuration */
  private final Settings settings;

  /** Parsed URI used to configure this handle */
  private final URI uri;

  /** access key, if provided */
  private final String accessKey;

  /** secret key, if provided */
  private final String secretKey;

  /** name of the bucket */
  private final String bucket;

  /** endpoint to which requests will be sent */
  private final String server;

  /** port at the given server */
  private final int port;

  /** remaining path, or key, for this accessed resource */
  private final String path;

  /** S3 client */
  private S3ClientService s3Client;

  /** Remote file stat */
  private S3ClientStat stat;

  /** Is this a directory (currently only buckets are considered directories */
  private boolean isBucket;

  /**
   * Exception if thrown during construction
   */
  private Throwable objectNotFound;

  /** If seeking more than this distance reset and reopen at offset */
  protected static final int S3_MAX_FORWARD_SEEK = 1048576;

  /**
   * Return true if this is a URL with an s3 scheme
   * @param url URL
   * @return true if this class can handle url
   */
  public static boolean canHandleScheme(String url) {
    return SCHEME_PARSER.matcher(url).matches();
  }

  /**
   * Open an S3 file
   *
   * @param url the full URL to the S3 resource
   * @throws IOException if there is an error during opening
   */
  public S3Handle(String url) throws IOException {
    this(url, true, null);
  }

  /**
   * Open an S3 file
   *
   * @param uristr the full URL to the S3 resource
   * @param initialize If true open the stream, otherwise just parse connection
   *        string
   * @param s custom settings object
   * @throws IOException if there is an error during opening
   */
  public S3Handle(String uristr, boolean initialize, Settings s) throws
      IOException {
    if (s == null) {
      this.settings = new StreamHandle.Settings();
    }
    else {
      this.settings = s;
    }

    try {
      this.uri = new URI(uristr);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Invalid URI " + uristr, e);
    }

    // access[:secret]
    String auth = this.uri.getUserInfo();
    String accessKey = null;
    String secretKey = null;
    if (auth != null) {
      String[] authparts = auth.split(":", 2);
      accessKey = authparts[0];
      if (authparts.length > 1) {
        secretKey = authparts[1];
      }
    }
    this.accessKey = accessKey;
    this.secretKey = secretKey;

    String protocol;
    String scheme = this.uri.getScheme();
    if (scheme.equals("s3")) {
      protocol = DEFAULT_S3_PROTOCOL;
    }
    else if (scheme.startsWith("s3+")) {
      protocol = scheme.substring(3);
    }
    else {
      protocol = scheme;
    }
    this.server = protocol + "://" + this.uri.getHost();

    if (this.uri.getPort() == -1) {
      this.port = 0;
    }
    else {
      this.port = this.uri.getPort();
    }

    // First path component is the bucket
    // TODO: Parsing this seems way more complicated than it should be
    String fullpath = this.uri.getPath();
    if (fullpath == null || fullpath.length() == 0) {
      fullpath = "/";
    }
    // Leading / means first element is always ""
    String[] pathparts = fullpath.split("/", 3);
    if (pathparts[1].length() > 0) {
      this.bucket = pathparts[1];
    }
    else {
      this.bucket = null;
    }
    if (pathparts.length > 2 && pathparts[2].length() > 0) {
      this.path = pathparts[2];
    }
    else {
      this.path = null;
    }

    this.isBucket = false;
    this.stat = null;

    if (initialize) {
      // Throw if there is an IOException, otherwise save the exception and only throw if a method
      // that requires a valid object is called
      this.connect();
      try {
        this.initialize();
      }
      catch (S3ClientServiceException e) {
        this.objectNotFound = e;
        LOGGER.debug("Object not found: [{}] {}", this, e);
      }
      LOGGER.trace("isBucket:{} stat:{}", isBucket, stat);
    }
  }

  /**
   * Connect to the server
   * @throws IOException if there was an error connecting to the server
   */
  protected void connect() throws IOException {
    final String appName = "Bio-Formats";
    // TODO: Replace "dev" with a version
    final String appVersion = "dev";
    try {
      ServiceFactory factory = new ServiceFactory();
      s3Client = factory.getInstance(S3ClientService.class);
      s3Client.initialize(server, port, accessKey, secretKey, appName, appVersion);
    }
    catch (S3ClientServiceException e) {
      throw new IOException(String.format(
              "Failed to connect: %s", this), e);
    }
    catch (DependencyException e) {
      throw new IOException(String.format(
          "S3 requires additional dependencies: %s", this), e);
    }
    LOGGER.trace("connected: server:{} port:{}", server, port);
  }

  /**
   * Check bucket or object exists
   * @throws IOException if unable to get the object
   * @throws S3ClientServiceException if unable to get the object
   */
  protected void initialize() throws
      IOException,
      S3ClientServiceException {
    if (path == null) {
      isBucket = s3Client.bucketExists(bucket);
    }
    else {
      isBucket = false;
      stat = s3Client.statObject(bucket, path);
      resetStream();
    }
  }

  public String getServer() {
    return server;
  }

  public int getPort() {
    return port;
  }

  public String getBucket() {
    return bucket;
  }

  public String getPath() {
    return path;
  }

  /**
   * Download an S3 object to a file system cache if it doesn't already exist
   *
   * @param url the full URL to the S3 resource
   * @param s custom settings object
   * @return File path to the cached object
   * @throws IOException if there is an error during reading or writing
   * @throws HandleException if no destination for the cache is provided
   */
  public static String cacheObject(String url, Settings s) throws
      IOException,
      HandleException {
    String cacheroot = s.getRemoteCacheRootDir();
    if (cacheroot == null) {
      throw new HandleException("Remote cache root dir is not set");
    }
    S3Handle s3 = new S3Handle(url, true, s);
    // TODO: Need to ensure this path is safe. Is there a Java method to check?
    String cacheobj = s3.getCacheKey();
    // Hopefully creates a cross-platform path
    Path cachepath = Paths.get(cacheroot, cacheobj);

    if (Files.exists(cachepath)) {
      LOGGER.debug("Found existing cache for {} at {}", s3, cachepath);
    }
    else {
      LOGGER.debug("Caching {} to {}", s3, cachepath);
      s3.downloadObject(cachepath);
      LOGGER.debug("Downloaded {}", cachepath);
    }
    return cachepath.toString();
  }

  public String getCacheKey(){
    String cachekey =
      getServer().replace("://", "/") + "/" +
      getPort() + "/" +
      getBucket() + "/" +
      getPath();
    return cachekey;
  }

  protected void downloadObject(Path destination) throws HandleException, IOException {
    LOGGER.trace("destination:{}", destination);
    if (this.stat == null || this.objectNotFound != null) {
      throw new IOException("Object not found " + this, this.objectNotFound);
    }
    if (path == null) {
      throw new HandleException("Download path=null not allowed");
    }
    Files.createDirectories(destination.getParent());
    try {
      s3Client.getObject(bucket, path, destination.toString());
    }
    catch (S3ClientServiceException e) {
        throw new HandleException("Download failed " + toString(), e);
      }
  }

  /**
   * Is this an accessible bucket?
   * TODO: If this bucket doesn't exist do we return false or thrown an exception?
   *
   * @return True if a bucket
   */
  public boolean isBucket() {
    //if (this.objectNotFound != null) {
    //  throw new DelayedObjectNotFound(this);
    //}
    return isBucket;
  }

  /* @see IRandomAccess#length() */
  @Override
  public long length() throws IOException {
    if (this.stat == null || this.objectNotFound != null) {
      throw new DelayedObjectNotFound(this);
    }
    return length;
  }

  /**
   * @see StreamHandle#seek(long)
   */
  @Override
  public void seek(long pos) throws IOException {
    LOGGER.trace("{}", pos);
    if (this.stat == null || this.objectNotFound != null) {
      throw new DelayedObjectNotFound(this);
    }
    long diff = pos - fp;

    if (diff < 0 || diff > S3_MAX_FORWARD_SEEK) {
      resetStream(pos);
    }
    else {
      super.seek(pos);
    }
  }

  /**
   * @see StreamHandle#resetStream()
   */
  @Override
  protected void resetStream() throws IOException {
    resetStream(0);
  }

  /**
   * Does this represent an accessible location?
   * @return true if this location is accessible
   * @throws IOException if unable to determine whether this location is accessible
   */
  @Override
  public boolean exists() throws IOException {
    return (objectNotFound == null) && (isBucket || stat != null);
  }

  /**
   * Reset the stream to an offset position
   * @param offset Offset into object
   * @throws IOException if there is an error during reading or writing
   */
  protected void resetStream(long offset) throws IOException {
    LOGGER.trace("Resetting {}", offset);
    if (this.stat == null || this.objectNotFound != null) {
      throw new DelayedObjectNotFound(this);
    }
    try {
      length = stat.length();
      stream = new DataInputStream(new BufferedInputStream(
              s3Client.getObject(bucket, path, offset)));
      fp = offset;
      mark = offset;
      }
      catch (S3ClientServiceException e) {
        throw new IOException(String.format(
              "failed to load s3: %s\n\t%s", uri, this), e);
    }
  }

  public String toString() {
    boolean found = (objectNotFound == null) && (isBucket || stat != null);
    return String.format("server:%s port:%d bucket:%s path:%s found:%s",
                          server, port, bucket, path, found);
  }
}