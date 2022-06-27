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

import ij.IJ;
import ij.gui.YesNoCancelDialog;
import ij.io.DirectoryChooser;
import ij.plugin.PlugIn;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import loci.formats.FilePattern;
import loci.formats.ImageReader;
import loci.plugins.LociImporter;

/**
 * Processes all image files in the chosen directory,
 * grouping files with similar names.
 */
public class Mass_Importer implements PlugIn {

  public void run(String arg) {
    // prompt user for directory to process
    DirectoryChooser dc = new DirectoryChooser("ODE-Formats Mass Importer");
    String dirPath = dc.getDirectory();

    // create a list of files we have already processed
    HashSet<String> done = new HashSet<String>();

    // list of files to actually open with ODE-Formats Importer
    ArrayList<String> filesToOpen = new ArrayList<String>();

    // process all files in the chosen directory
    File dir = new File(dirPath);
    File[] files = dir.listFiles();
    IJ.showStatus("Scanning directory");
    
    // image reader object, for testing whether a file is in a supported format
    try (ImageReader tester = new ImageReader()) {
      for (int i=0; i<files.length; i++) {
        String id = files[i].getAbsolutePath();
        IJ.showProgress((double) i / files.length);
  
        // skip files that have already been processed
        if (done.contains(id)) continue;
  
        // skip unsupported files
        if (!tester.isThisType(id, false)) continue;
  
        // use FilePattern to group files with similar names
        String name = files[i].getName();
        FilePattern fp = new FilePattern(name, dirPath);
  
        // get a list of all files part of this group, and mark them as done
        String[] used = fp.getFiles();
        for (int j=0; j<used.length; j++) done.add(used[j]);
  
        filesToOpen.add(id);
      }
    } catch (IOException e) {
      IJ.error("Sorry, an error while closing ImageReader: " + e.getMessage());
    }
    IJ.showProgress(1.0);
    IJ.showStatus("");

    // confirm that user wants to proceed in opening the file groups
    int numToOpen = filesToOpen.size();
    if (numToOpen == 0) {
      IJ.showMessage("No file groups found.");
      return;
    }
    String groups = numToOpen == 1 ?
      "1 file group" : (numToOpen + " file groups");
    YesNoCancelDialog confirm = new YesNoCancelDialog(IJ.getInstance(),
      "ODE-Formats Mass Importer", "Found " + groups + " in directory '" +
      dirPath + "'; proceed?");
    if (!confirm.yesPressed()) return;

    // launch the ODE-Formats Importer plugin to open each group of files
    for (int i=0; i<numToOpen; i++) {
      String id = (String) filesToOpen.get(i);
      String params =
        "location=[Local machine] " +
        "windowless=true " +
        "groupFiles=true " +
        "id=[" + id + "] ";
      new LociImporter().run(params);
    }
    IJ.showStatus("");
  }

}
