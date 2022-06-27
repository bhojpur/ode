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

import java.util.ArrayList;
import java.util.List;

import loci.formats.FormatTools;
import loci.formats.ImageWriter;
import loci.formats.MetadataTools;
import loci.formats.ode.ODEXMLMetadata;
import loci.formats.services.ODEXMLServiceImpl;
import loci.formats.services.ODEXMLService;
import loci.common.services.ServiceFactory;
import loci.formats.meta.IMetadata;

import ode.xml.model.MapPair;

public class writeMapAnnotationsExample {

    public static void main(String[] args) throws Exception{

        if (args.length < 1) {
            System.out.println("Please specify an output file name.");
            System.exit(1);
        }
        String id = args[0];

        // create blank 512x512 image
        System.out.println("Creating random image...");
        int w = 512, h = 512, c = 1;
        int pixelType = FormatTools.UINT16;
        byte[] img = new byte[w * h * c * FormatTools.getBytesPerPixel(pixelType)];

        // fill with random data
        for (int i=0; i<img.length; i++) img[i] = (byte) (256 * Math.random());

        // Create MapPair Object and add to List
        List<MapPair> mapList = new ArrayList<MapPair>();
        mapList.add(new MapPair("Example Key","Example Value"));
        mapList.add(new MapPair("ODE-Formats Version", FormatTools.VERSION));

        // create metadata object with minimum required metadata fields
        System.out.println("Populating metadata...");
        //add (minimum+Map)Annotations to the metadata object
        ServiceFactory factory = new ServiceFactory();
        ODEXMLService service = factory.getInstance(ODEXMLService.class);
        IMetadata metadata = service.createODEXMLMetadata();
        metadata.createRoot();
        MetadataTools.populateMetadata(metadata, 0, null, false, "XYZCT",
                FormatTools.getPixelTypeString(pixelType), w, h, 1, c, 1, c);
        
        int mapAnnotationIndex = 0;
        int annotationRefIndex = 0;
        String mapAnnotationID = MetadataTools.createLSID("MapAnnotation", 0, mapAnnotationIndex);
        
        metadata.setMapAnnotationID(mapAnnotationID, mapAnnotationIndex);
        metadata.setMapAnnotationValue(mapList, mapAnnotationIndex);
        metadata.setMapAnnotationAnnotator("Example Map Annotation", mapAnnotationIndex);
        metadata.setMapAnnotationDescription("Example Description", mapAnnotationIndex);
        metadata.setMapAnnotationNamespace("Example NameSpace", mapAnnotationIndex);
        metadata.setImageAnnotationRef(mapAnnotationID,0, annotationRefIndex);

        mapAnnotationIndex = 1;
        annotationRefIndex = 1;
        mapAnnotationID = MetadataTools.createLSID("MapAnnotation", 0, mapAnnotationIndex);
        metadata.setMapAnnotationID(mapAnnotationID, mapAnnotationIndex);
        metadata.setMapAnnotationValue(mapList, mapAnnotationIndex);
        metadata.setMapAnnotationAnnotator("Example Map Annotation 1", mapAnnotationIndex);
        metadata.setMapAnnotationDescription("Example Description 1", mapAnnotationIndex);
        metadata.setMapAnnotationNamespace("Example NameSpace 1", mapAnnotationIndex);
        metadata.setImageAnnotationRef(mapAnnotationID,0, annotationRefIndex);
        
        //Initialize writer and save file
        ImageWriter writer = new ImageWriter();
        writer.setMetadataRetrieve(metadata);
        writer.setId(id);
        writer.saveBytes(0, img);
        writer.close();

        System.out.println("Done.");

    }

}
