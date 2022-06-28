package loci.formats.utests;

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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.nio.file.Files;

import loci.formats.Memoizer;
import loci.formats.in.FakeReader;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class MemoizerTest {

  private static final String TEST_FILE =
    "test&pixelType=int8&sizeX=20&sizeY=20&sizeC=1&sizeZ=1&sizeT=1.fake";
  private static final String TMP_PREFIX = MemoizerTest.class.getName() + ".";

  private File idDir;
  private String id;
  private FakeReader reader;

  private static File createTempDir() throws Exception {
    return Files.createTempDirectory(TMP_PREFIX).toFile();
  }

  private static void recursiveDeleteOnExit(File rootDir) {
    rootDir.deleteOnExit();
    File[] children = rootDir.listFiles();
    if (null != children) {
      for (File child: children) {
        if (child.isDirectory()) {
          recursiveDeleteOnExit(child);
        } else {
          child.deleteOnExit();
        }
      }
    }
  }

  private static void checkMemo(Memoizer memoizer, String id)
      throws Exception {
    memoizer.setId(id);
    assertFalse(memoizer.isLoadedFromMemo());
    assertTrue(memoizer.isSavedToMemo());
    memoizer.close();
    memoizer.setId(id);
    assertTrue(memoizer.isLoadedFromMemo());
    assertFalse(memoizer.isSavedToMemo());
    memoizer.close();
  }

  private static void checkNoMemo(Memoizer memoizer, String id)
      throws Exception {
    memoizer.setId(id);
    assertFalse(memoizer.isLoadedFromMemo());
    assertFalse(memoizer.isSavedToMemo());
    memoizer.close();
  }

  private void checkMemoFile(File memoFile) {
    checkMemoFile(memoFile, idDir);
  }

  private void checkMemoFile(File memoFile, File memoDir) {
    File expMemoFile = new File(memoDir, "." + TEST_FILE + ".bfmemo");
    assertEquals(memoFile.getAbsolutePath(), expMemoFile.getAbsolutePath());
  }

  @BeforeMethod
  public void setUp() throws Exception {
    idDir = createTempDir();
    File tempFile = new File(idDir, TEST_FILE);
    tempFile.createNewFile();
    id = tempFile.getAbsolutePath();
    reader = new FakeReader(); // No setId !
  }

  @AfterMethod
  public void tearDown() throws Exception {
    reader.close();
    recursiveDeleteOnExit(idDir);
  }

  public void testDefaultConstructor() throws Exception {
    Memoizer memoizer = new Memoizer();
    checkMemoFile(memoizer.getMemoFile(id));
  }

  @Test
  public void testConstructorTimeElapsed() throws Exception {
    Memoizer memoizer = new Memoizer(0);
    checkMemoFile(memoizer.getMemoFile(id));
    checkMemo(memoizer, id);
  }

  @Test
  public void testConstructorReader() throws Exception {
    Memoizer memoizer = new Memoizer(reader);
    checkMemoFile(memoizer.getMemoFile(id));
  }

  @Test
  public void testConstructorReaderTimeElapsed() throws Exception {
    Memoizer memoizer = new Memoizer(reader, 0);
    checkMemoFile(memoizer.getMemoFile(id));
    checkMemo(memoizer, id);
  }

  @Test
  public void testConstructorTimeElapsedDirectory() throws Exception {
    File directory = createTempDir();
    directory.delete();
    Memoizer memoizer = new Memoizer(0, directory);
    // Check non-existing memo directory returns null
    assertNull(memoizer.getMemoFile(id));
    directory.mkdirs();
    String memoDir = idDir.getAbsolutePath();
    memoDir = memoDir.substring(memoDir.indexOf(File.separator) + 1);
    checkMemoFile(memoizer.getMemoFile(id), new File(directory, memoDir));
    checkMemo(memoizer, id);
    recursiveDeleteOnExit(directory);
  }

  @Test
  public void testConstructorTimeElapsedNull() throws Exception {
    Memoizer memoizer = new Memoizer(0, null);
    // Check null memo directory returns null
    assertNull(memoizer.getMemoFile(id));
    checkNoMemo(memoizer, id);
  }

  @Test
  public void testConstructorReaderTimeElapsedDirectory() throws Exception {
    File directory = createTempDir();
    directory.delete();
    Memoizer memoizer = new Memoizer(reader, 0, directory);
    // Check non-existing memo directory returns null
    assertNull(memoizer.getMemoFile(id));
    directory.mkdirs();
    String memoDir = idDir.getAbsolutePath();
    memoDir = memoDir.substring(memoDir.indexOf(File.separator) + 1);
    checkMemoFile(memoizer.getMemoFile(id), new File(directory, memoDir));
    checkMemo(memoizer, id);
    recursiveDeleteOnExit(directory);
  }

  @Test
  public void testConstructorReaderTimeElapsedNull() throws Exception {
    Memoizer memoizer = new Memoizer(reader, 0, null);
    // Check null memo directory returns null
    assertNull(memoizer.getMemoFile(id));
    checkNoMemo(memoizer, id);
  }

  @Test
  public void testGetMemoFilePermissionsDirectory() throws Exception {
    File directory = createTempDir();
    Memoizer memoizer = new Memoizer(reader, 0, directory);
    if (directory.setWritable(false)) {
      assertNull(memoizer.getMemoFile(id));
    }
  }

  @Test
  public void testGetMemoFilePermissionsInPlaceDirectory() throws Exception {
    Memoizer memoizer = new Memoizer(reader, 0, idDir);
    if (idDir.setWritable(false)) {
      assertNull(memoizer.getMemoFile(id));
    }
  }

  @Test
  public void testGetMemoFilePermissionsInPlace() throws Exception {
    Memoizer memoizer = new Memoizer(reader);
    if (idDir.setWritable(false)) {
      assertNull(memoizer.getMemoFile(id));
    }
  }

  @Test
  public void testRelocate() throws Exception {
    // Create an in-place memo file
    Memoizer memoizer = new Memoizer(reader, 0);
    memoizer.setId(id);
    memoizer.close();
    assertFalse(memoizer.isLoadedFromMemo());
    assertTrue(memoizer.isSavedToMemo());

    // Rename the directory (including the file and the memo file)
    File newidDir = new File(idDir.getAbsolutePath() + ".new");
    idDir.renameTo(newidDir);
    File newtempFile = new File(newidDir, TEST_FILE);
    String newid = newtempFile.getAbsolutePath();

    // Try to reopen the file with the Memoizer
    memoizer.setId(newid);
    memoizer.close();
    assertTrue(memoizer.isLoadedFromMemo());
    assertFalse(memoizer.isSavedToMemo());
    recursiveDeleteOnExit(newidDir);
  }

  @Test
  public void testWrappedReader() throws Exception {
    Memoizer memoizer = new Memoizer(reader, 0);
    File memoFile = memoizer.getMemoFile(id);
    assertFalse(memoFile.exists());
    reader.setId(id);
    assertFalse(memoFile.exists());
    reader.close();
    checkMemo(memoizer, id);
  }

}
