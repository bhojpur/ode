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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.IOException;
import java.lang.Iterable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import loci.formats.ClassList;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * Unit tests for {@link loci.formats.ClassList}.
 */
public class ClassListTest {

  private ClassList<Iterable> c;
  String configFile;

  public String writeConfigFile(String content) throws IOException {
    File file = File.createTempFile("iterables", ".tmp");
    file.deleteOnExit();
    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
    bw.write(content);
    bw.close();
    return file.getAbsolutePath();
  }

  @Test
  public void testDefaultConstructor() {
    c = new ClassList<Iterable>(Iterable.class);
    assertEquals(c.getClasses().length, 0);
  }

  @Test
  public void testNullConstructor() throws IOException {
    c = new ClassList<Iterable>(null, Iterable.class);
    assertEquals(c.getClasses().length, 0);
  }

  @Test
  public void testConfigFileConstructor1() throws IOException {
    configFile = writeConfigFile(
      "java.util.ArrayList\njava.util.AbstractList");
    c = new ClassList<Iterable>(configFile, Iterable.class, null);
    assertEquals(c.getClasses().length, 2);
    assertEquals(c.getClasses()[0], ArrayList.class);
    assertEquals(c.getClasses()[1], AbstractList.class);
  }

  @Test
  public void testConfigFileConstructor2() throws IOException {
    c = new ClassList<Iterable>("iterables.txt", Iterable.class, ClassListTest.class);
    assertEquals(c.getClasses().length, 2);
    assertEquals(c.getClasses()[0], AbstractList.class);
    assertEquals(c.getClasses()[1], ArrayList.class);
  }

  @Test
  public void testInvalidFileConstructor1() throws IOException {
    c = new ClassList<Iterable>("invalid", Iterable.class, null);
    assertEquals(c.getClasses().length, 0);
  }

  @Test
  public void testInvalidFileConstructor2() throws IOException {
    c = new ClassList<Iterable>("invalid", Iterable.class, ClassListTest.class);
    assertEquals(c.getClasses().length, 0);
  }

  @Test
  public void testParseFile() throws IOException {
    c = new ClassList<Iterable>(null, Iterable.class);
    c.parseFile("iterables.txt",ClassListTest.class);
    assertEquals(c.getClasses().length, 2);
  }

  @Test
  public void testAddClass() {
    c = new ClassList<Iterable>(Iterable.class);
    c.addClass(AbstractList.class);
    assertEquals(c.getClasses().length, 1);
    assertEquals(c.getClasses()[0], AbstractList.class);
    c.addClass(ArrayList.class);
    assertEquals(c.getClasses().length, 2);
    assertEquals(c.getClasses()[1], ArrayList.class);
    c.addClass(ArrayList.class);
    assertEquals(c.getClasses().length, 3);
    assertEquals(c.getClasses()[2], ArrayList.class);
  }

  @Test
  public void testRemoveClass() throws IOException {
    c = new ClassList<Iterable>("iterables.txt", Iterable.class, ClassListTest.class);
    c.removeClass(AbstractList.class);
    assertEquals(c.getClasses().length, 1);
    assertEquals(c.getClasses()[0], ArrayList.class);
    c.removeClass(ArrayList.class);
    assertEquals(c.getClasses().length, 0);
    c.removeClass(ArrayList.class);
    assertEquals(c.getClasses().length, 0);
  }

  @Test
  public void testAppend() throws IOException {
    configFile = writeConfigFile("java.util.ArrayList");
    c = new ClassList<Iterable>(configFile, Iterable.class, null);
    assertEquals(c.getClasses().length, 1);
    assertEquals(c.getClasses()[0], ArrayList.class);

    String configFile2 = writeConfigFile("java.util.AbstractList");
    ClassList<Iterable> c2 = new ClassList<Iterable>(configFile2, Iterable.class, null);
    c.append(c2);
    assertEquals(c.getClasses().length, 2);
    assertEquals(c.getClasses()[0], ArrayList.class);
    assertEquals(c.getClasses()[1], AbstractList.class);
  }

  @Test
  public void testPrepend() throws IOException {
    configFile = writeConfigFile("java.util.ArrayList");
    c = new ClassList<Iterable>(configFile, Iterable.class, null);
    assertEquals(c.getClasses().length, 1);
    assertEquals(c.getClasses()[0], ArrayList.class);

    String configFile2 = writeConfigFile("java.util.AbstractList");
    ClassList<Iterable> c2 = new ClassList<Iterable>(configFile2, Iterable.class, null);
    c.prepend(c2);
    assertEquals(c.getClasses().length, 2);
    assertEquals(c.getClasses()[0], AbstractList.class);
    assertEquals(c.getClasses()[1], ArrayList.class);
  }

  @DataProvider(name = "escaped lines")
  public Object[][] createEscapedLines() throws ClassNotFoundException {
    return new Object[][] {
      {""}, {"  # comment"}, {"# comment"},
    };
  }

  @Test(dataProvider = "escaped lines")
  public void testParseEscapedLine(String line) throws IOException  {
    c = new ClassList<Iterable>(null, Iterable.class);
    c.parseLine(line);
    assertEquals(c.getClasses().length, 0);
    assertTrue(c.getOptions().isEmpty());
  }

  @DataProvider(name = "classes")
  public Object[][] createClassesNoOptions() throws ClassNotFoundException {
    return new Object[][] {
      {"java.util.ArrayList", ArrayList.class},
      {"java.util.ArrayList  ", ArrayList.class},
      {"java.util.ArrayList  # comment", ArrayList.class},
    };
  }

  @Test(dataProvider = "classes")
  public void testParseClasses(String line, Object output) throws IOException   {
    c = new ClassList<Iterable>(null, Iterable.class);
    c.parseLine(line);
    assertEquals(c.getClasses()[0], output);
    assertTrue(c.getOptions().isEmpty());
  }

  @DataProvider(name = "classes with options")
  public Object[][] createClassesWithOptions() throws ClassNotFoundException {
    return new Object[][] {
      {"java.util.ArrayList[a=b]", ArrayList.class},
      {"java.util.ArrayList[a=b]  ", ArrayList.class},
      {"java.util.ArrayList[a=b]  # comment", ArrayList.class},
    };
  }

  @Test(dataProvider = "classes with options")
  public void testParseClassesWithOptions(String line, Object output) throws IOException  {
    c = new ClassList<Iterable>(null, Iterable.class);
    c.parseLine(line);
    assertEquals(c.getClasses()[0], output);
    assertEquals(c.getOptions().size(), 1);
    assertEquals(c.getOptions().get("java.util.ArrayList.a"), "b");
  }

  @Test
  public void testSetOption() throws IOException  {
    c = new ClassList<Iterable>(null, Iterable.class);
    assertEquals(Collections.emptySet(), c.getOptions().keySet());
    c.addOption("a", "b");
    assertEquals(c.getOptions().keySet(), Collections.singleton("a"));
    assertEquals(c.getOptions().get("a"), "b");
  }

  @DataProvider(name = "option string")
  public Object[][] createOptionStrings() {
    return new Object[][] {
      // Invalid key-value pairs
      {"a", new HashMap<String, String>()},
      {"a,b", new HashMap<String, String>()},
      {"=", new HashMap<String, String>()},
      {"", new HashMap<String, String>()},
      {"a=", new HashMap<String, String>()},
      {"=b", new HashMap<String, String>()},
      // Valid key-value pairs
      {"a=b", new HashMap<String, String>() {{
        put("a","b");}}
      },
      {"a=b,c=d", new HashMap<String, String>() {{
        put("a","b");
        put("c","d");}}
      },
      {"a=b,a=b", new HashMap<String, String>() {{
        put("a","b");}}
      },
      {"a=b,c", new HashMap<String, String>() {{
        put("a","b");}}
      },
      {"a=b,=,c=d", new HashMap<String, String>() {{
        put("a","b");
        put("c","d");}}
      },
    };
  }

  @Test(dataProvider = "option string")
  public void testParseOptions(String options, HashMap map) throws IOException
  {
    c = new ClassList<Iterable>(null, Iterable.class);
    assertEquals(map, c.parseOptions(options));
  }

  @Test
  public void testAllowedKeys() throws IOException
  {
    c = new ClassList<Iterable>(null, Iterable.class);
    assertTrue(c.isAllowedKey("type"));
    assertTrue(c.isAllowedKey("package.name.type"));
    assertTrue(c.isAllowedKey("type"));
    assertFalse(c.isAllowedKey("type.subtype"));
    assertFalse(c.isAllowedKey(""));
    assertFalse(c.isAllowedKey("."));
  }
}
