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

import loci.common.services.ServiceFactory;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.ODEXMLService;

/**
 * Edits the given file's image name (but does not save back to disk).
 */
public class EditImageName {

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Usage: java EditImageName file");
      return;
    }
    ImageReader reader = new ImageReader();
    // record metadata to ODE-XML format
    ServiceFactory factory = new ServiceFactory();
    ODEXMLService service = factory.getInstance(ODEXMLService.class);
    IMetadata odexmlMeta = service.createODEXMLMetadata();
    reader.setMetadataStore(odexmlMeta);
    String id = args[0];
    System.out.print("Reading metadata ");
    reader.setId(id);
    System.out.println(" [done]");

    // get image name
    String name = odexmlMeta.getImageName(0);
    System.out.println("Initial Image name = " + name);
    // change image name (reverse it)
    char[] arr = name.toCharArray();
    for (int i=0; i<arr.length/2; i++) {
      int i2 = arr.length - i - 1;
      char c = arr[i];
      char c2 = arr[i2];
      arr[i] = c2;
      arr[i2] = c;
    }
    name = new String(arr);
    // save altered name back to ODE-XML structure
    odexmlMeta.setImageName(name, 0);
    System.out.println("Updated Image name = " + name);
    // output full ODE-XML block
    System.out.println("Full ODE-XML dump:");
    String xml = service.getODEXML(odexmlMeta);
    System.out.println(xml);
  }

}
