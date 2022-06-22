package ode.conditions.utests;

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