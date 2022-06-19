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

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import ode.FilePathNamingException;

public class FilePathNamingValidator {
    protected final FilePathRestrictions rules;

    /**
     * @param rules the rules to apply in validating file naming
     */
    public FilePathNamingValidator(FilePathRestrictions... rules) {
        this.rules = FilePathRestrictions.combineFilePathRestrictions(rules);
    }

    /**
     * Validate the given FS file.
     * @param fsFile the FS file
     * @throws FilePathNamingException if the FS file is invalidly named
     */
    public void validateFilePathNaming(FsFile fsFile) throws FilePathNamingException {
        final SortedSet<Integer> illegalCodePoints = new TreeSet<Integer>();
        final SortedSet<String> illegalPrefixes = new TreeSet<String>();
        final SortedSet<String> illegalSuffixes = new TreeSet<String>();
        final SortedSet<String> illegalNames = new TreeSet<String>();

        for (final String string : fsFile.getComponents()) {
            final String ucString = string.toUpperCase();
            for (final String unsafeName : rules.unsafeNames)
                if (ucString.equals(unsafeName)) {
                    illegalNames.add(unsafeName);
                }
            for (final String unsafePrefix : rules.unsafePrefixes)
                if (ucString.startsWith(unsafePrefix)) {
                    illegalPrefixes.add(unsafePrefix);
                }
            for (final String unsafeSuffix : rules.unsafeSuffixes)
                if (ucString.endsWith(unsafeSuffix)) {
                    illegalSuffixes.add(unsafeSuffix);
                }
            final int codePointCount = string.codePointCount(0, string.length());
            for (int codePointIndex = 0; codePointIndex < codePointCount; codePointIndex++) {
                final int codePoint = string.codePointAt(string.offsetByCodePoints(0, codePointIndex));
                if (rules.transformationMatrix.containsKey(codePoint)) {
                    illegalCodePoints.add(codePoint);
                }
            }
        }

        if (!(illegalCodePoints.isEmpty() && illegalPrefixes.isEmpty() && illegalSuffixes.isEmpty() && illegalNames.isEmpty())) {
            throw new FilePathNamingException(null, null, "illegal file path", fsFile.toString(),
                    new ArrayList<Integer>(illegalCodePoints), new ArrayList<String>(illegalPrefixes),
                    new ArrayList<String>(illegalSuffixes), new ArrayList<String>(illegalNames));
        }
    }

    /**
     * Validate the given file path.
     * @param filePath the file path
     * @throws FilePathNamingException if the file path is invalidly named
     */
    public void validateFilePathNaming(String filePath) throws FilePathNamingException {
        validateFilePathNaming(new FsFile(filePath));
    }
}