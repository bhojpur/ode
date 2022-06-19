package ode.services.server.repo.path;

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
import java.util.function.Function;

/**
 * Transform between repository {@link FsFile} path and server-local {@link File}.
 */
public class ServerFilePathTransformer {
    /* where the repository should store its files */
    private File baseDirFile;
    /* the parent path components that should be omitted from repository paths */
    private FsFile baseDirFsFile;
    /* a function to make file-path components safe across platforms */
    private Function<String, String> pathSanitizer;
    
    /**
     * Given a repository path, returns the corresponding server-local {@link File}.
     * Must be executed server-side.
     * @param fsFile a repository path
     * @return the corresponding server-local {@link File}
     */
    public File getServerFileFromFsFile(FsFile fsFile) {
        return fsFile.toFile(this.baseDirFile);
    }
    
    /**
     * Given a server-local {@link File}, returns the corresponding repository path.
     * Must be executed server-side.
     * @param serverFile a server-local {@link File} within the repository
     * @return the corresponding repository path
     */
    public FsFile getFsFileFromServerFile(File serverFile) {
        final FsFile fullFsFile = new FsFile(serverFile);
        final FsFile childFsFile = fullFsFile.getPathFrom(this.baseDirFsFile);
        if (childFsFile == null)
            throw new IllegalArgumentException("server files must be within the repository");
        return childFsFile;
    }
    
    /**
     * Test if the given {@link FsFile} has been properly sanitized by the client.
     * @param fsFile a repository path
     * @return if the path is sanitary
     */
    public boolean isLegalFsFile(FsFile fsFile) {
        return fsFile.transform(this.pathSanitizer).equals(fsFile);
    }
    
    /**
     * Get the string transformer that is used to make file-path components safe across platforms.
     * @return the file-path component string transformer
     */
    public Function<String, String> getPathSanitizer() {
        return this.pathSanitizer;
    }
    
    /**
     * Set the string transformer that is used to make file-path components safe across platforms.
     * This is not required to be an injective function; two different components may transform to the same.
     * @param pathSanitizer the file-path component string transformer
     */
    public void setPathSanitizer(Function<String, String> pathSanitizer) {
        this.pathSanitizer = pathSanitizer;
    }
    
    /**
     * Set the repository root directory, to which {@link FsFile} instances are considered to be relative.
     * @throws IOException if the absolute path of the root directory could not be found
     * @throws IllegalArgumentException if the root directory does not exist
     * @param baseDirFile the repository root directory
     */
    public void setBaseDirFile(File baseDirFile) {
        if (!baseDirFile.isDirectory())
            throw new IllegalArgumentException(baseDirFile.getPath() + " must specify an existing FS repository directory");
        this.baseDirFile = baseDirFile.getAbsoluteFile();
        this.baseDirFsFile = new FsFile(this.baseDirFile);
    }
}