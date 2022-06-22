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

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ode.services.server.repo.PublicRepositoryI;
import ode.RLong;
import ode.RObject;
import ode.RType;
import ode.ServerError;
import ode.api.LongPair;
import ode.cmd.Delete2;
import ode.cmd.DiskUsage2;
import ode.cmd.DiskUsage2Response;
import ode.cmd.ManageImageBinaries;
import ode.cmd.ManageImageBinariesResponse;
import ode.cmd.graphs.ChildOption;
import ode.gateway.util.Requests;
import ode.model.Annotation;
import ode.model.Channel;
import ode.model.Dataset;
import ode.model.DatasetI;
import ode.model.DatasetImageLink;
import ode.model.DatasetImageLinkI;
import ode.model.FileAnnotationI;
import ode.model.Folder;
import ode.model.IObject;
import ode.model.Image;
import ode.model.ImageI;
import ode.model.Instrument;
import ode.model.Objective;
import ode.model.OriginalFile;
import ode.model.Pixels;
import ode.model.Project;
import ode.model.ProjectDatasetLink;
import ode.model.ProjectDatasetLinkI;
import ode.model.ProjectI;
import ode.sys.EventContext;
import ode.sys.ParametersI;

import org.apache.commons.beanutils.BeanUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Integration tests for the {@link ode.cmd.DiskUsage2} request.
 */
@Test(groups = { "integration" })
public class DiskUsageTest extends AbstractServerTest {

    private EventContext ec;
    private Long imageId;
    private Long pixelsId;
    private Long fileSize;
    private Long thumbnailSize;

    /**
     * Submit a disk usage request for the given objects and return the server's response.
     * @param objects the target objects
     * @return the objects' disk usage
     * @throws Exception if thrown during request execution
     */
    private DiskUsage2Response runDiskUsage(Map<java.lang.String, ? extends Collection<Long>> objects) throws Exception {
        final DiskUsage2 request = Requests.diskUsage().target(objects).build();
        return (DiskUsage2Response) doChange(request);
    }

    /**
     * Create a new file annotation.
     * @param size the size of the file annotation
     * @return the ID of the new file annotation
     * @throws Exception unexpected
     */
    private long createFileAnnotation(long size) throws Exception {
        final OriginalFile file = mmFactory.createOriginalFile();
        file.setSize(ode.rtypes.rlong(size));

        FileAnnotationI annotation = new FileAnnotationI();
        annotation.setFile(file);
        annotation = (FileAnnotationI) iUpdate.saveAndReturnObject(annotation);
        return annotation.getId().getValue();
    }

    /**
     * Retrieve a model object from the database. Assumed to exist.
     * @param targetClass the object's class
     * @param targetId the object's ID
     * @return the object
     * @throws ServerError if the retrieval failed
     */
    private <X extends IObject> X queryForObject(Class<X> targetClass, long targetId) throws ServerError {
        final String query = "FROM " + targetClass.getSimpleName() + " WHERE id = " + targetId;
        final List<List<RType>> results = iQuery.projection(query, null);
        return targetClass.cast(((RObject) results.get(0).get(0)).getValue());
    }

    /**
     * Run a HQL query that accepts a single ID and returns a single ID.
     * @param query the query to run
     * @param id the ID for the query's {@code :id} field
     * @return the ID returned by the query
     * @throws ServerError if the query failed
     */
    private long queryForId(String query, long id) throws ServerError {
        final List<List<RType>> results = iQuery.projection(query, new ParametersI().addId(id));
        return ((RLong) results.get(0).get(0)).getValue();
    }

    /**
     * Add an annotation to the given object.
     * @param targetClass the object's class
     * @param targetId the object's ID
     * @param annotationId the ID of the annotation to add to the object
     * @throws Exception unexpected
     */
    private void addAnnotation(Class<? extends IObject> targetClass, long targetId, long annotationId) throws Exception {
        final String className = targetClass.getSimpleName();
        final IObject parent = queryForObject(targetClass, targetId);
        final Annotation child = queryForObject(Annotation.class, annotationId);

        final String linkClassName = "ode.model." + className + "AnnotationLinkI";
        final Class<? extends IObject> linkClass = Class.forName(linkClassName).asSubclass(IObject.class);
        final IObject link = linkClass.newInstance();

        BeanUtils.setProperty(link, "parent", parent);
        BeanUtils.setProperty(link, "child", child);
        iUpdate.saveObject(link);
    }

    /**
     * Test that two maps have the same keys with the same non-null values.
     * @param actual the map of actual values
     * @param expected the map of expected values
     */
    private static <K, V> void assertMapsEqual(Map<K, V> actual, Map<K, V> expected) {
        Assert.assertEquals(actual.size(), expected.size());
        for (final Map.Entry<K, V> actualEntry : actual.entrySet()) {
            final K actualKey = actualEntry.getKey();
            final V actualValue = actualEntry.getValue();
            Assert.assertNotNull(actualValue);
            Assert.assertEquals(actualValue, expected.get(actualKey));
        }
    }

    /**
     * Import a test image and note information related to it.
     * @throws Throwable unexpected
     */
    @BeforeClass
    public void setup() throws Throwable {
        ec = iAdmin.getEventContext();

        String name = "testDV&pixelType=int16&sizeX=20&sizeY=20&sizeZ=5&sizeT=6&sizeC=1.fake";
        final File imageFile = new File(System.getProperty("java.io.tmpdir"), name);
        imageFile.deleteOnExit();
        try (final Writer out = new FileWriter(imageFile)) {
            for (int index = 0; index < 100; index ++) {
                out.append("fake" + index);
            }
        }
        fileSize = imageFile.length();

        final Pixels pixels = importFile(imageFile, "fake").get(0);
        final Image image = pixels.getImage();
        pixelsId = pixels.getId().getValue();
        imageId = image.getId().getValue();

        final Instrument instrument = mmFactory.createInstrument();
        final Objective objective = mmFactory.createObjective();
        image.setInstrument(instrument);
        objective.setInstrument(instrument);
        iUpdate.saveArray(ImmutableList.of(image, objective));

        final ManageImageBinaries mibRequest = new ManageImageBinaries();
        mibRequest.imageId = imageId;
        final ManageImageBinariesResponse mibResponse = (ManageImageBinariesResponse) doChange(mibRequest);
        thumbnailSize = mibResponse.thumbnailSize;

        Assert.assertNotNull(ec);
        Assert.assertNotNull(imageId);
        Assert.assertNotNull(pixelsId);
        Assert.assertNotNull(fileSize);
        Assert.assertNotNull(thumbnailSize);
    }

    /**
     * Delete the test image.
     * @throws Exception unexpected
     */
    @AfterClass
    public void teardown() throws Exception {
        if (imageId != null) {
            final Delete2 request = Requests.delete().target("Image").id(imageId).build();
            doChange(request);
        }
    }

    /**
     * Test that the usage is associated with the correct user and group.
     * @throws Exception unexpected
     */
    @Test
    public void testOwnership() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        final ImmutableList<Map<LongPair, ?>> responseElements = ImmutableList.of(
                        response.bytesUsedByReferer, response.fileCountByReferer, response.totalBytesUsed, response.totalFileCount);
        for (final Map<LongPair, ?> responseElement : responseElements) {
            Assert.assertEquals(responseElement.size(), 1);
            for (final LongPair key : responseElement.keySet()) {
                Assert.assertEquals(key.first, ec.userId);
                Assert.assertEquals(key.second, ec.groupId);
            }
        }
    }


    /**
     * Test that the file size of the actual image file is correctly computed.
     * @throws Exception unexpected
     */
    @Test
    public void testFileSize() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
        for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
            Assert.assertEquals(byReferer.get("FilesetEntry"), fileSize);
        }
    }

    /**
     * Test that the file size of the actual image file is correctly computed even when containers must be opened.
     * @throws Exception unexpected
     */
    @Test
    public void testFileSizeInContainersPD() throws Exception {
        final Project project = new ProjectI();
        project.setName(ode.rtypes.rstring("test project"));
        final Dataset dataset = new DatasetI();
        dataset.setName(ode.rtypes.rstring("test dataset"));

        final long projectId = iUpdate.saveAndReturnObject(project).getId().getValue();
        final long datasetId = iUpdate.saveAndReturnObject(dataset).getId().getValue();

        final ProjectDatasetLink pdl = new ProjectDatasetLinkI();
        pdl.setParent(new ProjectI(projectId, false));
        pdl.setChild(new DatasetI(datasetId, false));
        iUpdate.saveObject(pdl);

        final DatasetImageLink dil = new DatasetImageLinkI();
        dil.setParent(new DatasetI(datasetId, false));
        dil.setChild(new ImageI(imageId, false));
        iUpdate.saveObject(dil);

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Project", Collections.singleton(projectId)));
            Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
            for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
                Assert.assertEquals(byReferer.get("FilesetEntry"), fileSize);
            }
        } finally {
            final ChildOption option = Requests.option().excludeType("Image").build();
            final Delete2 request = Requests.delete().target("Project").id(projectId).option(option).build();
            doChange(request);
        }
    }

    /**
     * Test that the file size of the actual image file is correctly computed even when containers must be opened.
     * Requires double recursion along Folder → Folder.
     * @throws Exception unexpected
     */
    @Test
    public void testFileSizeInContainersFF() throws Exception {
        Folder parentFolder = (Folder) saveAndReturnFolder(mmFactory.simpleFolder()).proxy();
        Folder middleFolder = mmFactory.simpleFolder();
        middleFolder.setParentFolder(parentFolder);
        middleFolder = (Folder) saveAndReturnFolder(middleFolder).proxy();
        Folder childFolder = mmFactory.simpleFolder();
        childFolder.setParentFolder(middleFolder);
        childFolder.linkImage(new ImageI(imageId, false));
        childFolder = (Folder) saveAndReturnFolder(childFolder).proxy();

        final long parentFolderId = parentFolder.getId().getValue();

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Folder", Collections.singleton(parentFolderId)));
            Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
            for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
                Assert.assertEquals(byReferer.get("FilesetEntry"), fileSize);
            }
        } finally {
            final ChildOption option = Requests.option().excludeType("Image").build();
            final Delete2 request = Requests.delete().target("Folder").id(parentFolderId).option(option).build();
            doChange(request);
        }
    }

    /**
     * Test that the import log size for the image is correctly computed.
     * @throws Exception unexpected
     */
    @Test
    public void testImportLogSize() throws Exception {
        final String query = "SELECT o.size FROM " +
                "Image i, Fileset f, FilesetJobLink fjl, UploadJob j, JobOriginalFileLink jol, OriginalFile o " +
                "WHERE i.id = :id AND f = i.fileset AND fjl.parent = f AND fjl.child = j AND jol.parent = j AND jol.child = o " +
                "AND o.mimetype = '" + PublicRepositoryI.IMPORT_LOG_MIMETYPE + "'";

        final long importLogSize = queryForId(query, imageId);

        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
        for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
            Assert.assertEquals((long) byReferer.get("Job"), importLogSize);
        }
    }

    /**
     * Test that the size of the thumbnail is correctly computed.
     * @throws Exception unexpected
     */
    @Test
    public void testThumbnailSize() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
        for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
            Assert.assertEquals(byReferer.get("Thumbnail"), thumbnailSize);
        }
    }

    /**
     * Test that the size of the file annotations is correctly computed.
     * @throws Exception unexpected
     */
    @Test
    public void testFileAnnotationSize() throws Exception {
        final Random rng = new Random(123456);  // fixed seed for deterministic testing
        final int annotationCount = 5;
        long totalAnnotationSize = 0;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            final long size = rng.nextInt(Integer.MAX_VALUE) + 1L;  // positive
            totalAnnotationSize += size;
            annotationIds.add(createFileAnnotation(size));
        }

        final long channelId = queryForId("SELECT id FROM Channel WHERE pixels.id = :id", pixelsId);
        final long instrumentId = queryForId("SELECT instrument.id FROM Image WHERE id = :id", imageId);
        final long objectiveId = queryForId("SELECT id FROM Objective WHERE instrument.id = :id", instrumentId);

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Channel.class, channelId, annotationIds.get(1));
        addAnnotation(Instrument.class, instrumentId, annotationIds.get(2));
        addAnnotation(Objective.class, objectiveId, annotationIds.get(3));
        addAnnotation(Annotation.class, annotationIds.get(3), annotationIds.get(4));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
            for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Long) totalAnnotationSize);
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the size of the file annotations is correctly computed even if some are attached to multiple objects.
     * @throws Exception unexpected
     */
    @Test
    public void testDuplicatedFileAnnotationSize() throws Exception {
        final Random rng = new Random(123456);  // fixed seed for deterministic testing
        final int annotationCount = 3;
        long totalAnnotationSize = 0;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            final long size = rng.nextInt(Integer.MAX_VALUE) + 1L;  // positive
            totalAnnotationSize += size;
            annotationIds.add(createFileAnnotation(size));
        }

        final long channelId = queryForId("SELECT id FROM Channel WHERE pixels.id = :id", pixelsId);
        final long instrumentId = queryForId("SELECT instrument.id FROM Image WHERE id = :id", imageId);
        final long objectiveId = queryForId("SELECT id FROM Objective WHERE instrument.id = :id", instrumentId);

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Channel.class, channelId, annotationIds.get(1));
        addAnnotation(Instrument.class, instrumentId, annotationIds.get(2));
        addAnnotation(Objective.class, objectiveId, annotationIds.get(0));
        addAnnotation(Annotation.class, annotationIds.get(0), annotationIds.get(1));
        addAnnotation(Annotation.class, annotationIds.get(1), annotationIds.get(2));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
            for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Long) totalAnnotationSize);
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the size of the file annotations is correctly computed even if the annotations are attached in a cycle.
     * @throws Exception unexpected
     */
    @Test
    public void testCyclicFileAnnotationSize() throws Exception {
        final Random rng = new Random(123456);  // fixed seed for deterministic testing
        final int annotationCount = 3;
        long totalAnnotationSize = 0;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            final long size = rng.nextInt(Integer.MAX_VALUE) + 1L;  // positive
            totalAnnotationSize += size;
            annotationIds.add(createFileAnnotation(size));
        }

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Annotation.class, annotationIds.get(0), annotationIds.get(1));
        addAnnotation(Annotation.class, annotationIds.get(1), annotationIds.get(2));
        addAnnotation(Annotation.class, annotationIds.get(2), annotationIds.get(0));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.bytesUsedByReferer.size(), 1);
            for (final Map<String, Long> byReferer : response.bytesUsedByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Long) totalAnnotationSize);
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the file counts are as expected.
     * @throws Exception unexpected
     */
    @Test
    public void testCounts() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        Assert.assertEquals(response.fileCountByReferer.size(), 1);
        for (final Map<String, Integer> byReferer : response.fileCountByReferer.values()) {
            Assert.assertEquals(byReferer.size(), 3);
            Assert.assertEquals(byReferer.get("FilesetEntry"), Integer.valueOf(1));  // original image file
            Assert.assertEquals(byReferer.get("Job"), Integer.valueOf(1));  // import log
            Assert.assertEquals(byReferer.get("Thumbnail"), Integer.valueOf(1));
        }
    }

    /**
     * Test that the number of file annotations is correctly computed.
     * @throws Exception unexpected
     */
    @Test
    public void testCountsWithFileAnnotations() throws Exception {
        final int annotationCount = 5;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            annotationIds.add(createFileAnnotation(1));
        }

        final long channelId = queryForId("SELECT id FROM Channel WHERE pixels.id = :id", pixelsId);
        final long instrumentId = queryForId("SELECT instrument.id FROM Image WHERE id = :id", imageId);
        final long objectiveId = queryForId("SELECT id FROM Objective WHERE instrument.id = :id", instrumentId);

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Channel.class, channelId, annotationIds.get(1));
        addAnnotation(Instrument.class, instrumentId, annotationIds.get(2));
        addAnnotation(Objective.class, objectiveId, annotationIds.get(3));
        addAnnotation(Annotation.class, annotationIds.get(3), annotationIds.get(4));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.fileCountByReferer.size(), 1);
            for (final Map<String, Integer> byReferer : response.fileCountByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Integer) annotationIds.size());
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the number of file annotations is correctly computed even if some are attached to multiple objects.
     * @throws Exception unexpected
     */
    @Test
    public void testCountWithDuplicatedFileAnnotations() throws Exception {
        final int annotationCount = 3;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            annotationIds.add(createFileAnnotation(1));
        }

        final long channelId = queryForId("SELECT id FROM Channel WHERE pixels.id = :id", pixelsId);
        final long instrumentId = queryForId("SELECT instrument.id FROM Image WHERE id = :id", imageId);
        final long objectiveId = queryForId("SELECT id FROM Objective WHERE instrument.id = :id", instrumentId);

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Channel.class, channelId, annotationIds.get(1));
        addAnnotation(Instrument.class, instrumentId, annotationIds.get(2));
        addAnnotation(Objective.class, objectiveId, annotationIds.get(0));
        addAnnotation(Annotation.class, annotationIds.get(0), annotationIds.get(1));
        addAnnotation(Annotation.class, annotationIds.get(1), annotationIds.get(2));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.fileCountByReferer.size(), 1);
            for (final Map<String, Integer> byReferer : response.fileCountByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Integer) annotationIds.size());
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the number of file annotations is correctly computed even if some are attached to multiple objects.
     * @throws Exception unexpected
     */
    @Test
    public void testCountWithCyclicFileAnnotations() throws Exception {
        final int annotationCount = 3;
        final List<Long> annotationIds = new ArrayList<Long>(annotationCount);
        for (int i = 0; i < annotationCount; i++) {
            annotationIds.add(createFileAnnotation(1));
        }

        addAnnotation(Image.class, imageId, annotationIds.get(0));
        addAnnotation(Annotation.class, annotationIds.get(0), annotationIds.get(1));
        addAnnotation(Annotation.class, annotationIds.get(1), annotationIds.get(2));
        addAnnotation(Annotation.class, annotationIds.get(2), annotationIds.get(0));

        try {
            final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
            Assert.assertEquals(response.fileCountByReferer.size(), 1);
            for (final Map<String, Integer> byReferer : response.fileCountByReferer.values()) {
                Assert.assertEquals(byReferer.get("FileAnnotation"), (Integer) annotationIds.size());
            }
        } finally {
            final Delete2 request = Requests.delete().target("FileAnnotation").id(annotationIds).build();
            doChange(request);
        }
    }

    /**
     * Test that the total bytes used is the sum of the by-referer breakdown.
     * Applies only when there is no duplication between different referers.
     * @throws Exception unexpected
     */
    @Test
    public void testSizeTotals() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        final Map<LongPair, Long> expected = new HashMap<LongPair, Long>();
        for (final Map.Entry<LongPair, Map<String, Long>> byReferer : response.bytesUsedByReferer.entrySet()) {
            long total = 0;
            for (final Long size : byReferer.getValue().values()) {
                total += size;
            }
            final Long currentTotal = expected.get(byReferer.getKey());
            if (currentTotal != null) {
                total += currentTotal;
            }
            expected.put(byReferer.getKey(), total);
        }
        assertMapsEqual(response.totalBytesUsed, expected);
    }

    /**
     * Test that the total files used is the sum of the by-referer breakdown.
     * Applies only when there is no duplication between different referers.
     * @throws Exception unexpected
     */
    @Test
    public void testCountTotals() throws Exception {
        final DiskUsage2Response response = runDiskUsage(ImmutableMap.of("Image", Collections.singleton(imageId)));
        final Map<LongPair, Integer> expected = new HashMap<LongPair, Integer>();
        for (final Map.Entry<LongPair, Map<String, Integer>> byReferer : response.fileCountByReferer.entrySet()) {
            int total = 0;
            for (final Integer size : byReferer.getValue().values()) {
                total += size;
            }
            final Integer currentTotal = expected.get(byReferer.getKey());
            if (currentTotal != null) {
                total += currentTotal;
            }
            expected.put(byReferer.getKey(), total);
        }
        assertMapsEqual(response.totalFileCount, expected);
    }

    /**
     * Test that a bad class name causes an error response.
     * @throws Exception unexpected
     */
    @Test
    public void testBadClassName() throws Exception {
        final DiskUsage2 request = Requests.diskUsage().target("NoClass").id(1L).build();
        doChange(client, factory, request, false);
    }
}
