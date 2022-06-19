package ode.services.server.test.utests;

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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;

import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.ServerFilePathTransformer;
import ode.services.server.repo.path.FsFile;
import ode.services.server.repo.path.MakePathComponentSafe;
import ode.util.TempFileManager;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"fs"})
public class ServerFilePathTransformerTest extends FilePathTransformerTestBase {
    private static final TempFileManager tempFileManager =
            new TempFileManager("test-" + ServerFilePathTransformerTest.class.getSimpleName());
    
    private ServerFilePathTransformer fpts;
    private ClientFilePathTransformer fptc;
    private File tempDir;
    
    /**
     * Set up a new test directory as the managed repository,
     * and initialize file path transformers for server and client.
     * @throws IOException unexpected
     */
    @BeforeClass
    public void setup() throws IOException {
        this.tempDir = tempFileManager.createPath("unit-test",  null,  true);
        final Function<String, String> transformer = new MakePathComponentSafe(this.conservativeRules);
        this.fpts = new ServerFilePathTransformer();
        this.fpts.setPathSanitizer(transformer);
        this.fpts.setBaseDirFile(this.tempDir);
        this.fptc = new ClientFilePathTransformer(transformer);
    }
    
    /**
     * Test that legal repository paths convert to local server files then back to the original path.
     * @throws IOException unexpected
     */
    @Test
    public void testServerPathConversion() throws IOException {
        final FsFile repositoryPath = new FsFile("wibble/wobble/|]{~`±§/óß€Åæ");
        final File serverPath = fpts.getServerFileFromFsFile(repositoryPath);
        Assert.assertEquals(fpts.getFsFileFromServerFile(serverPath), repositoryPath,
                "conversion from legal repository paths to server-local paths and back must return the original");
    }
    
    /**
     * Test that the given components, when sanitized, become the given repository path,
     * and that a path of that name can be used for data storage on the server's local filesystem.
     * @param fsPath the expected repository path for the path components
     * @param components path components to be sanitized into a repository path
     * @throws IOException unexpected
     */
    private void testClientPath(String fsPath, String... components) throws IOException {
        final FsFile rootFile = fptc.getFsFileFromClientFile(componentsToFile(), Integer.MAX_VALUE);
        final FsFile fsFile = fptc.getFsFileFromClientFile(componentsToFile(components), Integer.MAX_VALUE);
        Assert.assertEquals(fsFile.getPathFrom(rootFile).toString(), fsPath,
                "client-side file path components do not assemble to form the expected repository path");
        Assert.assertTrue(fpts.isLegalFsFile(fsFile),
                "sanitized client-side file paths should be sanitary server-side");
        final File serverFile = fpts.getServerFileFromFsFile(fsFile);
        final FleetingDirectory serverDir = new FleetingDirectory(serverFile.getParentFile());
        final long testContentsOut = System.nanoTime();
        final long testContentsIn;
        final DataOutputStream out = new DataOutputStream(new FileOutputStream(serverFile));
        out.writeLong(testContentsOut);
        out.close();
        final DataInputStream in = new DataInputStream(new FileInputStream(serverFile));
        testContentsIn = in.readLong();
        in.close();
        serverFile.delete();
        serverDir.deleteCreated();
        Assert.assertEquals(testContentsIn, testContentsOut,
                "should be able to read and write data with safe file-paths");
    }
    
    /**
     * Test that a difficult client path is properly sanitized then usable for data storage.
     * @throws IOException unexpected
     */
    @Test
    public void testClientPathSafety() throws IOException {
        testClientPath("C;/Foo1._/_nUl.txt/coM5_/_$bar/_.[]._", "C:", "Foo1.", "nUl.txt", "coM5", "$bar", ".<>.");
    }
    
    /**
     * Test the ability to check if a given repository path is legal.
     */
    @Test
    public void testLegalityCheck() {
        Assert.assertTrue(fpts.isLegalFsFile(new FsFile("a/b/c")));
        Assert.assertFalse(fpts.isLegalFsFile(new FsFile("a/*/c")));
        Assert.assertFalse(fpts.isLegalFsFile(new FsFile("a/b/lpt1")));
    }
    
    /**
     * Reverse the actions of {@link #setup()}.
     * @throws IOException unable to delete temporary directory
     */
    @AfterClass
    public void tearDown() throws IOException {
        tempFileManager.removePath(this.tempDir);
        this.fpts = null;
        this.fptc = null;
    }
}