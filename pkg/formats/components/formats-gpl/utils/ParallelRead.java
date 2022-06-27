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
import loci.common.services.ServiceFactory;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Reads all files in given directory in parallel,
 * using a separate thread for each.
 */
public class ParallelRead implements Runnable {
  private String id;

  public ParallelRead(String id) {
    this.id = id;
  }

  public void run() {
    try {
      ImageReader r = new ImageReader();
      ServiceFactory factory = new ServiceFactory();
      ODEXMLService service = factory.getInstance(ODEXMLService.class);
      IMetadata meta = service.createODEXMLMetadata();
      r.setMetadataStore(meta);
      r.setId(id);
      System.out.println(Thread.currentThread().getName() +
        ": id=" + id +
        ", sizeX=" + r.getSizeX() +
        ", sizeY=" + r.getSizeY() +
        ", sizeZ=" + r.getSizeZ() +
        ", sizeT=" + r.getSizeT() +
        ", sizeC=" + r.getSizeC() +
        ", imageName=" + meta.getImageName(0));
      r.close();
    }
    catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String dir = args[0];
    File[] list = new File(dir).listFiles();
    for (int i=0; i<list.length; i++) {
      ParallelRead pr = new ParallelRead(list[i].getAbsolutePath());
      new Thread(pr).start();
    }
  }
}
