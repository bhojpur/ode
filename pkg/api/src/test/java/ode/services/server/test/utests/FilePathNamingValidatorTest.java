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

import java.util.HashSet;
import java.util.Set;

import ode.services.server.repo.path.FilePathNamingValidator;
import ode.FilePathNamingException;

import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"fs"})
public class FilePathNamingValidatorTest extends FilePathTransformerTestBase {
    /**
     * Test that file path naming validation accepts legal names.
     * @throws FilePathNamingException unexpected
     */
    @Test
    public void testValidFilePath() throws FilePathNamingException {
        final FilePathNamingValidator validator = new FilePathNamingValidator(this.conservativeRules);
        validator.validateFilePathNaming("C;/Foo1._/_nUl.txt/coM5_/_$bar/_.[]._/óß€Åæ");
    }

    /**
     * Test that file path naming validation flags the correct proscriptions.
     * @throws FilePathNamingException unexpected
     */
    @Test
    public void testInvalidFilePath() throws FilePathNamingException {
        final String illegalFilePath = "C:/Foo1./nUl.txt/coM5/$bar/.<>.";
        final Set<Integer> illegalCodePoints = new HashSet<Integer>();
        final Set<String> illegalPrefixes = new HashSet<String>();
        final Set<String> illegalSuffixes = new HashSet<String>();
        final Set<String> illegalNames = new HashSet<String>();

        illegalCodePoints.add(0x3A);  // :
        illegalCodePoints.add(0x3C);  // [
        illegalCodePoints.add(0x3E);  // ]

        illegalPrefixes.add("NUL.");
        illegalPrefixes.add("$");
        illegalPrefixes.add(".");
        illegalSuffixes.add(".");
        illegalNames.add("COM5");

        final FilePathNamingValidator validator = new FilePathNamingValidator(this.conservativeRules);
        try {
            validator.validateFilePathNaming(illegalFilePath);
            Assert.fail("illegal file path passed name validation");
        } catch (FilePathNamingException fpne) {
            Assert.assertEquals(fpne.illegalFilePath, illegalFilePath);
            Assert.assertTrue(CollectionUtils.isEqualCollection(fpne.illegalCodePoints, illegalCodePoints));
            Assert.assertTrue(CollectionUtils.isEqualCollection(fpne.illegalPrefixes, illegalPrefixes));
            Assert.assertTrue(CollectionUtils.isEqualCollection(fpne.illegalSuffixes, illegalSuffixes));
            Assert.assertTrue(CollectionUtils.isEqualCollection(fpne.illegalNames, illegalNames));
        }
    }
}