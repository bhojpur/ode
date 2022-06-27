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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.TreeMultimap;

import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;

/**
 * Utility class for printing a list of scientific domains supported by
 * ODE-Formats, and all of the supported formats in each domain.
 */
public class PrintDomains {

  public static void main(String[] args) {
    // get a list of all available readers
    IFormatReader[] readers = new ImageReader().getReaders();

    final TreeMultimap<String, String> domains = TreeMultimap.create();

    for (IFormatReader reader : readers) {
      try {
        String[] readerDomains = reader.getPossibleDomains("");
        for (String domain : readerDomains) {
          domains.put(domain, reader.getFormat());
        }
      }
      catch (FormatException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    for (final Map.Entry<String, Collection<String>> domain :
             domains.asMap().entrySet()) {
      System.out.println(domain.getKey() + ":");
      for (final String readerFormat : domain.getValue()) {
        System.out.println("  " + readerFormat);
      }
    }
  }

}
