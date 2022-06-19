package ode.formats.importer;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Collections2;

import ode.util.TempFileManager;

/**
 * Tests that filesets aren't skipped if they reference a common file. Also
 * makes sure that the order of these files doesn't matter either.
 */
public class ImportCandidatesTest {

    private File testFolder;
    private final List<String> testFiles = new ArrayList<String>();
    private final Set<String> expectedFilesets = new HashSet<String>();

    @BeforeClass
    /**
     * Creates the nhdr test file sets
     */
    public void createFiles() throws Exception {
        testFolder = TempFileManager.create_path("ImportCandidatesTest", "",
                true);

        // Create a test.nhdr / test.raw pair, but also an additional
        // test_extra.nhdr referencing the same test.raw file
        // Expected result: Two filesets test.nhdr and test_extra.nhdr
        // should be imported.
        final String content = "NRRD0001\n" + "type: float\n"
                + "sizes: 7 38 39 40\n" + "data file: ./test.raw\n"
                + "encoding: raw";

        File f = new File(testFolder, "test.raw");
        echo("", f);
        testFiles.add(f.getAbsolutePath());

        f = new File(testFolder, "test.nhdr");
        echo(content, f);
        testFiles.add(f.getAbsolutePath());
        expectedFilesets.add(f.getAbsolutePath());

        f = new File(testFolder, "test_extra.nhdr");
        echo(content, f);
        testFiles.add(f.getAbsolutePath());
        expectedFilesets.add(f.getAbsolutePath());
    }

    @AfterClass
    /**
     * Deletes the nhdr test file sets
     */
    public void deleteFiles() throws Exception {
        FileUtils.deleteQuietly(testFolder);
    }

    @Test
    /**
     * Uses the nhdr filesets (two nhdr files referencing the same raw file).
     * Tests all possible permutations of the file paths passed to
     * ImportCandidates.
     */
    public void testOrder() {
        for (List<String> imp : Collections2.orderedPermutations(testFiles)) {
            String[] f = new String[imp.size()];
            f = imp.toArray(f);

            List<ImportContainer> cons = createImportCandidates(f);

            Assert.assertEquals(expectedFilesets.size(), cons.size(), info(f, cons));

            for (ImportContainer con : cons) {
                Assert.assertTrue(expectedFilesets
                        .contains(con.getFile().getAbsolutePath()), info(f, cons));
            }
        }
    }

    @Test
    /**
     * Uses the nhdr filesets (two nhdr files referencing the same raw file).
     * Passes the whole folder to ImportCandidates.
     */
    public void testFolder() {
        // test the folder itself
        String[] f = new String[] { testFolder.getAbsolutePath() };

        List<ImportContainer> cons = createImportCandidates(f);

        Assert.assertEquals(expectedFilesets.size(),
                cons.size(), info(f, cons));

        for (ImportContainer con : cons) {
            Assert.assertTrue(
                    expectedFilesets.contains(con.getFile().getAbsolutePath()), info(f, cons));
        }
    }

    @Test
    /**
     * Tests a single fake/pattern fileset
     */
    public void testSingleFileset() throws Exception {
        File fakeFolder = TempFileManager.create_path("ImportCandidatesTest_1",
                "", true);

        File f = new File(fakeFolder, "test1.pattern");
        echo("test1_T<1-3>.fake", f);
        echo("", new File(fakeFolder, "test1_T1.fake"));
        echo("", new File(fakeFolder, "test1_T2.fake"));
        echo("", new File(fakeFolder, "test1_T3.fake"));

        String[] files = new String[] { fakeFolder.getAbsolutePath() };

        List<ImportContainer> cons = createImportCandidates(files);

        Assert.assertEquals(1, cons.size(), info(files, cons));

        Assert.assertTrue(f.getAbsolutePath()
                .equals(cons.iterator().next().getFile().getAbsolutePath()), info(files, cons));

        FileUtils.deleteQuietly(fakeFolder);
    }

    @Test
    /**
     * Tests two distinct fake/pattern filesets
     */
    public void testMultifileFilesets() throws Exception {
        File fakeFolder = TempFileManager.create_path("ImportCandidatesTest_2",
                "", true);

        File f1 = new File(fakeFolder, "test1.pattern");
        echo("test1_T<1-3>.fake", f1);
        echo("", new File(fakeFolder, "test1_T1.fake"));
        echo("", new File(fakeFolder, "test1_T2.fake"));
        echo("", new File(fakeFolder, "test1_T3.fake"));

        File f2 = new File(fakeFolder, "test2.pattern");
        echo("test1_T<4-6>.fake", f2);
        echo("", new File(fakeFolder, "test1_T4.fake"));
        echo("", new File(fakeFolder, "test1_T5.fake"));
        echo("", new File(fakeFolder, "test1_T6.fake"));

        String[] files = new String[] { fakeFolder.getAbsolutePath() };

        List<ImportContainer> cons = createImportCandidates(files);

        Assert.assertEquals(2, cons.size(), info(files, cons));

        for (ImportContainer con : cons) {
            Assert.assertTrue(
                    f1.getAbsolutePath().equals(con.getFile().getAbsolutePath())
                            || f2.getAbsolutePath()
                                    .equals(con.getFile().getAbsolutePath()), info(files, cons));
        }

        FileUtils.deleteQuietly(fakeFolder);
    }

    @Test(timeOut=3000)
    /**
     * Tests two fake/pattern filesets where the files of one fileset are fully
     * included in the other fileset.
     */
    public void testContainingFilesets() throws Exception {
        File fakeFolder = TempFileManager.create_path("ImportCandidatesTest_3",
                "", true);

        File f1 = new File(fakeFolder, "test1.pattern");
        echo("test1_T<2-4>.fake", f1);
        echo("", new File(fakeFolder, "test1_T1.fake"));
        echo("", new File(fakeFolder, "test1_T2.fake"));
        echo("", new File(fakeFolder, "test1_T3.fake"));
        echo("", new File(fakeFolder, "test1_T4.fake"));
        echo("", new File(fakeFolder, "test1_T5.fake"));
        echo("", new File(fakeFolder, "test1_T6.fake"));

        File f2 = new File(fakeFolder, "test2.pattern");
        echo("test1_T<1-6>.fake", f2);

        String[] files = new String[] { fakeFolder.getAbsolutePath() };

        List<ImportContainer> cons = createImportCandidates(files);

        Assert.assertEquals(2, cons.size(), info(files, cons));

        for (ImportContainer con : cons) {
            Assert.assertTrue(
                    f1.getAbsolutePath().equals(con.getFile().getAbsolutePath())
                            || f2.getAbsolutePath()
                                    .equals(con.getFile().getAbsolutePath()), info(files, cons));
        }

        FileUtils.deleteQuietly(fakeFolder);
    }

    @Test
    /**
     * Tests fake/pattern filesets where both filesets share some files
     */
    public void testOverlappingFilesets() throws Exception {
        File fakeFolder = TempFileManager.create_path("ImportCandidatesTest_4",
                "", true);

        File f1 = new File(fakeFolder, "test1.pattern");
        echo("test1_T<1-4>.fake", f1);
        echo("", new File(fakeFolder, "test1_T1.fake"));
        echo("", new File(fakeFolder, "test1_T2.fake"));
        echo("", new File(fakeFolder, "test1_T3.fake"));
        echo("", new File(fakeFolder, "test1_T4.fake"));
        echo("", new File(fakeFolder, "test1_T5.fake"));
        echo("", new File(fakeFolder, "test1_T6.fake"));

        File f2 = new File(fakeFolder, "test2.pattern");
        echo("test1_T<3-6>.fake", f2);

        String[] files = new String[] { fakeFolder.getAbsolutePath() };

        List<ImportContainer> cons = createImportCandidates(files);

        Assert.assertEquals(2, cons.size(), info(files, cons));

        for (ImportContainer con : cons) {
            Assert.assertTrue(
                    f1.getAbsolutePath().equals(con.getFile().getAbsolutePath())
                            || f2.getAbsolutePath()
                                    .equals(con.getFile().getAbsolutePath()), info(files, cons));
        }

        FileUtils.deleteQuietly(fakeFolder);
    }

    /**
     * Creates a ImportCandidates from the provided files and returns its
     * ImportContainers.
     * 
     * @param files
     *            The file to import
     * @return The ImportContainers
     */
    private List<ImportContainer> createImportCandidates(String[] files) {
        ImportConfig config = new ImportConfig();
        ODEWrapper w = new ODEWrapper(config);
        IObserver o = new IObserver() {
            public void update(IObservable importLibrary, ImportEvent event) {
                // nothing to do
            }
        };
        return (new ImportCandidates(w, files, o)).getContainers();
    }

    /**
     * Just generates a String with detailed information about the current test
     * situation
     * 
     * @param files
     *            The files array being tested
     * @param cons
     *            The ImportContainer returned from ImportCandidates
     * @return An informative String
     */
    private String info(String[] files, List<ImportContainer> cons) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nInput:\n");
        for (String f : files)
            sb.append(f + "\n");
        sb.append("\nExpected filesets:\n");
        for (String s : expectedFilesets)
            sb.append(s + "\n");
        sb.append("\nActual filesets:\n");
        for (ImportContainer c : cons)
            sb.append(c.getFile().getAbsolutePath() + "\n");
        return sb.toString();
    }

    /**
     * Simply writes the content into the file
     * 
     * @param content
     *            The content
     * @param file
     *            The file
     * @throws IOException
     *             If the file couldn't be wrote
     */
    private static void echo(String content, File file) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(content);
        }
    }
}