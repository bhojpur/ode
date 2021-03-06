package ode.services.scripts;

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

import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.getPath;
import static org.apache.commons.io.FilenameUtils.normalizeNoEndSeparator;
import static org.apache.commons.io.FilenameUtils.separatorsToUnix;

import java.io.File;

import ode.model.enums.ChecksumAlgorithm;

import org.apache.commons.io.FilenameUtils;
import ode.util.checksum.ChecksumProviderFactory;
import ode.util.checksum.ChecksumProviderFactoryImpl;
import ode.util.checksum.ChecksumType;

/**
 * File type wrapper for paths which are intended for being stored in the
 * database as a part of this repository.
 */
public class RepoFile {

    private final static ChecksumProviderFactory cpf = new ChecksumProviderFactoryImpl();

    /**
     * Somewhat complicated method to turn any path into a unique like path
     * rooted at "/".
     *
     * @param path Non-null;
     * @return See above.
     */
    public static String norm(String path) {
        String s2u = separatorsToUnix(path);
        String tmp = normalizeNoEndSeparator(getPath(s2u));
        if (tmp == null) {
            tmp = "";
        }
        else if (tmp.equals("")) {
            // pass
        }
        else {
            tmp = "/" + tmp;
        }
        return String.format("%s/%s.%s", tmp, getBaseName(s2u), getExtension(s2u));
    }

    final private FsFile fs;
    final private String rel;
    final private String root;
    final private String absPath;

    /**
     * Assumes that path is relative under root, and constructs a new {@link File}
     * and calls {@link #RepoFile(File, File)}
     */
    public RepoFile(File root, String path) {
        this(root, new File(root, norm(path)));
    }

    /**
     * Both root and file are absolute paths to files. This constructor calculates
     * the relative part of the second argument based on the first.
     */
    public RepoFile(File root, File file) {
        this.fs = new FsFile(file);
        this.root = FilenameUtils.normalize(root.getAbsolutePath());
        this.rel = fs.path.substring((int) this.root.length());
        this.absPath = new File(root, rel).getAbsolutePath();
    }

    public boolean matches(File file) {
        return FilenameUtils.equalsNormalizedOnSystem(absPath, file
                .getAbsolutePath());
    }

    public File file() {
        return new File(fs.path);
    }

    public String basename() {
        return FilenameUtils.getName(rel);
    }

    public String dirname() {
        return FilenameUtils.getFullPath(rel);
    }

    public String fullname() {
        return rel;
    }

    public ChecksumAlgorithm hasher() {
        return fs.hasher();
    }

    public String hash() {
        return fs.hash();
    }

    public long length() {
        return fs.length();
    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.rel;
    }

    /**
     * File type wrapper for actual OS files.
     */
    public static class FsFile {

        final public String path;
        final public String name;
        final private ChecksumAlgorithm hasher = new ChecksumAlgorithm(ChecksumAlgorithm.VALUE_SHA1_160);

        FsFile(String path) {
            this.path = FilenameUtils.normalize(path);
            this.name = FilenameUtils.getName(this.path);
        }

        FsFile(File file) {
            this(file.getAbsolutePath());
        }

        public long length() {
            return new File(path).length();
        }

        public ChecksumAlgorithm hasher() {
            return this.hasher;
        }

        public String hash() {
            return cpf.getProvider(ChecksumType.SHA1).putFile(path).checksumAsString();
        }

        @Override
        public String toString() {
            return super.toString() + ":" + this.path;
        }

    }
}
