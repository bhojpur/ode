package ode.util.utests;

import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.util.ShallowCopy;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ShallowCopyTest {

    @Test
    public void testNoNulls() throws Exception {
        Pixels pix = new Pixels();
        pix.setId(1L);
        pix.setSizeC(1);
        pix.setImage(new Image());

        Pixels test = new ShallowCopy().copy(pix);
        Assert.assertNotNull(test.getId());
        Assert.assertNotNull(test.getDetails());
        Assert.assertNotNull(test.getSizeC());
        Assert.assertNotNull(test.getImage());
    }

}