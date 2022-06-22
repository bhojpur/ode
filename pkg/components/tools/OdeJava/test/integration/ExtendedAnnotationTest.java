package integration;

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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ode.api.IMetadataPrx;
import ode.model.CommentAnnotationI;
import ode.model.Detector;
import ode.model.DetectorAnnotationLink;
import ode.model.DetectorAnnotationLinkI;
import ode.model.CommentAnnotation;
import ode.model.IObject;
import ode.model.Instrument;
import ode.sys.Parameters;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Testing of the range of {@link AnnotationRef} locations
 * added to the model.
 */
public class ExtendedAnnotationTest extends AbstractServerTest {

    /**
     * Test annotations on detector in instrument.
     * Retrieve via simplest methods
     * @throws Exception
     */
    @Test
    public void testAnnotationOnDetector() throws Exception {
        String uuid = UUID.randomUUID().toString();

        Instrument instrument = (Instrument) iUpdate
                .saveAndReturnObject(mmFactory.createInstrument(uuid));
        Assert.assertNotNull(instrument);

        // creation
        Detector detector = mmFactory.createDetector();
        detector.setInstrument((Instrument) instrument.proxy());
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);

        // updating
        detector.setManufacturer(ode.rtypes.rstring("ODE-Sample Inc"));
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("commentOnDetector"));
        DetectorAnnotationLink dal = new DetectorAnnotationLinkI();
        dal.setParent((Detector)detector.proxy());
        dal.setChild(annotation);
        dal = (DetectorAnnotationLink) iUpdate.saveAndReturnObject(dal);
        Assert.assertNotNull(dal);

        // retrieval
        String sql = "select d from Detector as d left outer join fetch d.annotationLinks as link left outer join fetch link.child as child where d.id = " + detector.getId().getValue();
        detector = (Detector) iQuery.findByQuery(sql, null);
        Assert.assertNotNull(detector);
        Assert.assertTrue(detector.isAnnotated());
        List<DetectorAnnotationLink> listOfLinks = detector.copyAnnotationLinks();
        Assert.assertNotNull(listOfLinks);
        Assert.assertEquals(1, listOfLinks.size());
        Assert.assertNotNull(listOfLinks.get(0));
        DetectorAnnotationLink newDal = listOfLinks.get(0);
        Assert.assertNotNull(newDal);
        Assert.assertNotNull(newDal.getChild());

        annotation = (CommentAnnotation) newDal.getChild();

        // comparison
        Assert.assertEquals("ODE-Sample Inc", detector.getManufacturer().getValue());
        Assert.assertEquals("commentOnDetector", annotation.getTextValue().getValue());
    }

    /**
     * Test annotations on detector in instrument.
     * Retrieve via two methods
     * @throws Exception
     */
    @Test
    public void testAnnotationOnDetectorFull() throws Exception {
        String uuid = UUID.randomUUID().toString();

        Instrument instrument = (Instrument) iUpdate
                .saveAndReturnObject(mmFactory.createInstrument(uuid));
        Assert.assertNotNull(instrument);

        // creation
        Detector detector = mmFactory.createDetector();
        detector.setInstrument((Instrument) instrument.proxy());
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);

        // updating
        detector.setManufacturer(ode.rtypes.rstring("ODE-Sample Inc"));
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);

        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("commentOnDetector"));
        DetectorAnnotationLink dal = new DetectorAnnotationLinkI();
        dal.setParent((Detector)detector.proxy());
        dal.setChild(annotation);
        dal = (DetectorAnnotationLink) iUpdate.saveAndReturnObject(dal);
        Assert.assertNotNull(dal);

        // retrieval
        String sql = "select d from Detector as d left outer join fetch d.annotationLinks as link left outer join fetch link.child as child where d.id = " + detector.getId().getValue();
        detector = (Detector) iQuery.findByQuery(sql, null);
        Assert.assertNotNull(detector);
        Assert.assertTrue(detector.isAnnotated());
        List<DetectorAnnotationLink> listOfLinks = detector.copyAnnotationLinks();
        Assert.assertNotNull(listOfLinks);
        Assert.assertEquals(1, listOfLinks.size());
        Assert.assertNotNull(listOfLinks.get(0));
        DetectorAnnotationLink newDal = listOfLinks.get(0);
        Assert.assertNotNull(newDal);
        Assert.assertNotNull(newDal.getChild());

        annotation = (CommentAnnotation) newDal.getChild();
        Assert.assertEquals("commentOnDetector", annotation.getTextValue().getValue());

        Assert.assertEquals(1, detector.sizeOfAnnotationLinks());
        Assert.assertNotNull(detector.linkedAnnotationList());
        Assert.assertNotNull(detector.linkedAnnotationList().get(0));
        Assert.assertNotNull(detector.linkedAnnotationList().get(0).getId());

        sql = "select a from Annotation as a where a.id = " + detector.linkedAnnotationList().get(0).getId().getValue();
        annotation = (CommentAnnotation) iQuery.findByQuery(sql, null);
        Assert.assertNotNull(annotation);
        Assert.assertNotNull(annotation.getTextValue());

        // comparison
        Assert.assertEquals("ODE-Sample Inc", detector.getManufacturer().getValue());
        Assert.assertEquals("commentOnDetector", annotation.getTextValue().getValue());
    }

    /**
     * Test annotations on detector in instrument.
     * Retrieve via iMetadata.
     * @throws Exception
     */
    @Test
    public void testAnnotationOnDetectorViaMetadata() throws Exception {
        String uuid = UUID.randomUUID().toString();

        Instrument instrument = (Instrument) iUpdate
                .saveAndReturnObject(mmFactory.createInstrument(uuid));
        Assert.assertNotNull(instrument);

        // creation
        Detector detector = mmFactory.createDetector();
        detector.setInstrument((Instrument) instrument.proxy());
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);
        
        // updating
        detector.setManufacturer(ode.rtypes.rstring("ODE-Sample Inc"));
        detector = (Detector) iUpdate.saveAndReturnObject(detector);
        Assert.assertNotNull(detector);
        
        // Create annotation
        CommentAnnotation annotation = new CommentAnnotationI();
        annotation.setTextValue(ode.rtypes.rstring("commentOnDetectorViaiMetadata"));
        DetectorAnnotationLink dal = new DetectorAnnotationLinkI();
        dal.setParent((Detector)detector.proxy());
        dal.setChild(annotation);
        dal = (DetectorAnnotationLink) iUpdate.saveAndReturnObject(dal);
        Assert.assertNotNull(dal);
        
        // retrieval
        String sql = "select d from Detector as d left outer join fetch d.annotationLinks where d.id = " + detector.getId().getValue();
        detector = (Detector) iQuery.findByQuery(sql, null);
        Assert.assertTrue(detector.isAnnotated());
        Assert.assertEquals(1, detector.sizeOfAnnotationLinks());
        
        // load the annotations via iMetadata
        List<Long> ids = new ArrayList<Long>();
        Parameters param = new Parameters();
        List<Long> nodes = new ArrayList<Long>();
        nodes.add(detector.getId().getValue());
        IMetadataPrx iMetadata = factory.getMetadataService();
        String COMMENT_ANNOTATION = "ode.model.annotations.CommentAnnotation";
        Map<Long, List<IObject>> result = iMetadata.loadAnnotations(
                Detector.class.getName(), nodes, Arrays.asList(COMMENT_ANNOTATION),
                ids, param);
        Assert.assertNotNull(result);
        List<IObject> l = result.get(detector.getId().getValue());
        Assert.assertNotNull(l);
        Assert.assertEquals(1,l.size());


        // comparison
        Assert.assertEquals("ODE-Sample Inc", detector.getManufacturer().getValue());

        Iterator<IObject> i = l.iterator();
        IObject o;
        while (i.hasNext()) {
            o = i.next();
            if (o instanceof CommentAnnotation) {
                CommentAnnotation theComAnn = (CommentAnnotation) o;
                Assert.assertNotNull(theComAnn);
                Assert.assertEquals("commentOnDetectorViaiMetadata", theComAnn.getTextValue().getValue());
            }
        }
    }
}


/*       Filter f = mmFactory.createFilter(500, 560);
       f.setInstrument((Instrument) instrument.proxy());
       f = (Filter) iUpdate.saveAndReturnObject(f);
       Assert.assertNotNull(f);

       Dichroic di = mmFactory.createDichroic();
       di.setInstrument((Instrument) instrument.proxy());
       di = (Dichroic) iUpdate.saveAndReturnObject(di);
       Assert.assertNotNull(di);

       Objective o = mmFactory.createObjective();
       o.setInstrument((Instrument) instrument.proxy());
       o = (Objective) iUpdate.saveAndReturnObject(o);
       Assert.assertNotNull(o);

       Laser l = mmFactory.createLaser();
       l.setInstrument((Instrument) instrument.proxy());
       l = (Laser) iUpdate.saveAndReturnObject(l);
       Assert.assertNotNull(l);
*/

