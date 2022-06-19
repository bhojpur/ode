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

import java.util.function.Function;

public class MakePathComponentSafe implements Function<String, String> {
    protected final FilePathRestrictions rules;

    /**
     * @param rules the rules to apply in making path components safe
     */
    public MakePathComponentSafe(FilePathRestrictions... rules) {
        this.rules = FilePathRestrictions.combineFilePathRestrictions(rules);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(String string) {
        final String ucString = string.toUpperCase();
        for (final String unsafeName : rules.unsafeNames)
            if (ucString.equals(unsafeName))
                string = string + rules.safeCharacter;
        for (final String unsafePrefix : rules.unsafePrefixes)
            if (ucString.startsWith(unsafePrefix))
                string = rules.safeCharacter + string;
        for (final String unsafeSuffix : rules.unsafeSuffixes)
            if (ucString.endsWith(unsafeSuffix))
                string = string + rules.safeCharacter;
        final int codePointCount = string.codePointCount(0, string.length());
        final int[] codePointArray = new int[codePointCount];
        for (int codePointIndex = 0; codePointIndex < codePointCount; codePointIndex++) {
            final int originalCodePoint = string.codePointAt(string.offsetByCodePoints(0, codePointIndex));
            final Integer substituteCodePoint = rules.transformationMap.get(originalCodePoint);
            if (substituteCodePoint == null)
                codePointArray[codePointIndex] = originalCodePoint;
            else
                codePointArray[codePointIndex] = substituteCodePoint;
        }
        return new String(codePointArray, 0, codePointCount);
    }
}