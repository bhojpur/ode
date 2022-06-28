package loci.formats.tools;

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

import java.io.File;
import java.io.IOException;

import loci.common.DataTools;
import loci.formats.Memoizer;

/**
 * Generate cache file(s) for a specified file or directory.
 */
public class GenerateCache {

  /**
   * Use the given Memoizer to initialize the given file
   * and attempt to generate a memo file.
   * Prints a message if the memo file could not be saved.
   */
  private static void generateMemo(Memoizer reader, String path) {
    boolean success = false;
    try {
      success = reader.generateMemo(path);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    if (!success) {
      System.out.println("Memo file not saved for " + path);
    }
  }

  /**
   * Recursively scan the given directory and generate a memo file
   * for each found file.
   * Delegates to #generateMemo(Memoizer, String) to perform the actual
   * memo file generation.
   */
  private static void processDirectory(Memoizer reader, File dir) {
    String[] list = dir.list();
    for (String f : list) {
      File file = new File(dir, f);
      if (file.isDirectory()) {
        processDirectory(reader, file);
      }
      else {
        generateMemo(reader, file.getAbsolutePath());
      }
    }
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage:");
      System.out.println(
        "odecachegen [-list] fileOrDir cacheFileDir");
      System.out.println();
      System.out.println("If '-list' is specified, then 'fileOrDir' is a text file with one file per line.");
      return;
    }

    CommandLineTools.runUpgradeCheck(args);

    boolean fileList = args.length >= 3 && args[0].equals("-list");
    String input = args[args.length - 2];
    String outputDir = args[args.length - 1];

    Memoizer reader = new Memoizer(0, new File(outputDir));
    File inputFile = new File(input);

    if (!inputFile.isDirectory()) {
      if (fileList) {
        String[] files = null;
        try {
          files = DataTools.readFile(inputFile.getAbsolutePath()).split("\n");
        }
        catch (IOException e) {
          System.out.println("Could not read file list from " + inputFile);
          e.printStackTrace();
        }
        if (files != null) {
          for (String f : files) {
            generateMemo(reader, f);
          }
        }
      }
      else {
        generateMemo(reader, inputFile.getAbsolutePath());
      }
    }
    else {
      processDirectory(reader, inputFile);
    }
  }

}
