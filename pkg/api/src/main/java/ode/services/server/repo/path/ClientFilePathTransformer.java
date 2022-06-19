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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Transform client-local {@link File} to repository {@link FsFile} path. Wholly thread-safe.
 */
public class ClientFilePathTransformer {
    /* a function to make file-path components safe across platforms */
    private final Function<String, String> pathSanitizer;
    
    /**
     * Construct a new client-side file path transformer.
     * @param pathSanitizer a file-path component string transformer
     * whose behavior corresponds to that passed to {@link ServerFilePathTransformer#setPathSanitizer} server-side.
     */
    public ClientFilePathTransformer(Function<String, String> pathSanitizer) {
        this.pathSanitizer = pathSanitizer;
    }
    
    /**
     * Given a client-local {@link File}, and a path component depth,
     * returns the corresponding repository path. Must be executed client-side.
     * @param clientFile a client-local {@link File}
     * @param depth the path component depth (including filename)
     * @return the corresponding repository path, intended to be safe cross-platform
     * @throws IOException if the absolute path of the {@link File} could not be found
     */
    public FsFile getFsFileFromClientFile(File clientFile, int depth) throws IOException {
        if (depth < 1) 
            throw new IllegalArgumentException("path depth must be strictly positive");
        return new FsFile(new FsFile(clientFile), depth).transform(this.pathSanitizer);
    }
    
    /**
     * Test if the given path component depth suffices for disambiguating the given set of
     * {@link File}s. Must be executed client-side.
     * @param files a set of {@link File}s
     * @param depth a path component depth
     * @return if the depth allows the {@link File}s to each be named distinctly
     * @throws IOException if the absolute path of any of the {@link File}s could not be found
     */
    private boolean isDepthSufficient(Collection<File> files, int depth) throws IOException {
        final Set<FsFile> fsPaths = new HashSet<FsFile>();
        for (final File file : files)
            if (!fsPaths.add(getFsFileFromClientFile(file, depth)))
                return false;
        return true;
    }
    
    /**
     * Get the smallest path component depth that allows the given set of {@link File}s
     * to be disambiguated. Must be executed client-side.
     * @param files a set of {@link File}s
     * @return the minimum depth for disambiguating the {@link File}s, no less than 1
     * @throws IOException if the absolute path of any of the {@link File}s could not be found
     */
    public int getMinimumDepth(Collection<File> files) throws IOException {
        if (files.size() < 2)
            /* must be a strictly positive integer */
            return 1;
        if (!isDepthSufficient(files, Integer.MAX_VALUE))
            throw new IllegalArgumentException("file set is not unique, so no depth can fix it");
        int depth;
        for (depth = 1; !isDepthSufficient(files, depth); depth++);
        return depth;
    }
    
    /**
     * Get the files that are too similarly named.
     * @param files a set of files
     * @return the files grouped by those to which they are too similar,
     * or <code>null</code> if all the files are named sufficiently distinctly
     * @throws IOException if the absolute path of any of the {@link File}s could not be found
     */
    public Set<Collection<File>> getTooSimilarFiles(Set<File> files) throws IOException {
        final SetMultimap<String, File> filesByFsFile = HashMultimap.create();
        for (final File file : files) {
            final String path = getFsFileFromClientFile(file, Integer.MAX_VALUE).toString().toLowerCase();
            filesByFsFile.put(path, file);
        }
        final Set<Collection<File>> tooSimilarFiles = new HashSet<Collection<File>>();
        for (final Collection<File> similarFiles : filesByFsFile.asMap().values()) {
            if (similarFiles.size() > 1) {
                tooSimilarFiles.add(similarFiles);
            }
        }
        return tooSimilarFiles.isEmpty() ? null : tooSimilarFiles;
    }
}