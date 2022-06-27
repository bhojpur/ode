/*
 * Top-level reader and writer APIs
 */

package loci.formats.utests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

import loci.formats.in.MetadataOptions;
import loci.formats.in.DefaultMetadataOptions;
import loci.formats.in.MetadataLevel;


/**
 * Unit tests for {@link loci.formats.in.DefaultMetadataOptions}.
 */
public class DefaultMetadataOptionsTest {

  private MetadataOptions opt;

  @BeforeMethod
  public void setUp() {
    opt = new DefaultMetadataOptions();
  }

  @Test
  public void testMetadataLevel() {
    assertEquals(opt.getMetadataLevel(), MetadataLevel.ALL);
    for (MetadataLevel level: MetadataLevel.values()) {
      opt.setMetadataLevel(level);
      assertEquals(opt.getMetadataLevel(), level);
      assertEquals(
          (new DefaultMetadataOptions(level)).getMetadataLevel(), level
      );
    }
  }

  @Test
  public void testIsValidate() {
    assertFalse(opt.isValidate());
    opt.setValidate(true);
    assertTrue(opt.isValidate());
    opt.setValidate(false);
    assertFalse(opt.isValidate());
  }

}
