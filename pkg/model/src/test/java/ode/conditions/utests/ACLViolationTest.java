package ode.conditions.utests;

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

import org.testng.annotations.Test;
import ode.conditions.acl.ACLDeleteViolation;
import ode.conditions.acl.ACLLoadViolation;
import ode.conditions.acl.ACLViolation;
import ode.conditions.acl.CollectedACLViolations;
import ode.model.containers.Project;
import ode.model.core.Image;

public class ACLViolationTest {

    @Test(expectedExceptions = CollectedACLViolations.class)
    public void testCollectionACLViolationToStringTest() throws Exception {
        CollectedACLViolations coll = new CollectedACLViolations("test");
        ACLViolation[] array = {
                new ACLLoadViolation(Image.class, 1L, "can't load img"),
                new ACLDeleteViolation(Project.class, 2L, "can't delete prj") };
        for (int i = 0; i < array.length; i++) {
            coll.addViolation(array[i]);
        }
        coll.setStackTrace(new CollectedACLViolations(null).getStackTrace());
        throw coll;
    }

}