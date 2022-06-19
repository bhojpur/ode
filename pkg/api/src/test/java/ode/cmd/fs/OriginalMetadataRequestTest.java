package ode.cmd.fs;

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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ode.io.nio.PixelsService;
import ode.services.server.test.AbstractServantTest;
import ode.services.util.Executor;
import ode.system.ServiceFactory;
import ode.RString;
import ode.RType;
import ode.cmd.ERR;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.OriginalMetadataResponse;
import ode.cmd.Request;
import ode.cmd.Response;
import ode.cmd.Status;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

@Test(groups = "integration")
public class OriginalMetadataRequestTest extends AbstractServantTest {

    private static final ImmutableMap<String, String> expectedGlobalMetadata;
    private static final ImmutableMap<String, String> expectedSeriesMetadata;

    static {
        Builder<String, String> builder;
        builder = ImmutableMap.builder();
        builder.put("a=b", "c");
        builder.put("(a=b)", "c");
        builder.put("a", "(b=c)");
        builder.put("{a=b", "c}");
        builder.put("{(a=b)", "c}");
        builder.put("{a", "(b=c)}");
        builder.put("(p=q", "r");
        builder.put("p=q", "r)");
        builder.put("((p=q)", "r");
        builder.put("p", "(q=r))");
        builder.put("p q", "r s");
        expectedGlobalMetadata = builder.build();
        builder = ImmutableMap.builder();
        builder.put("ein må lære seg å krype før ein lærer å gå", "learn to walk before you can run");
        builder.put("money doesn't grow on trees", "pengar växer inte på träd");
        builder.put("аб", "вг");
        expectedSeriesMetadata = builder.build();
    }

	@Override
	@BeforeClass
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected OriginalMetadataResponse assertRequest(
			final OriginalMetadataRequestI req, Map<String, String> ctx) {

		final Status status = new Status();

		@SuppressWarnings("unchecked")
		List<Object> rv = (List<Object>) user.ex.execute(ctx, user
				.getPrincipal(), new Executor.SimpleWork(this, "testRequest") {
			@Transactional(readOnly = false)
			public List<Object> doWork(Session session, ServiceFactory sf) {

				// from HandleI.steps()
				List<Object> rv = new ArrayList<Object>();

				Helper helper = new Helper((Request) req, status,
						getSqlAction(), session, sf);
				req.init(helper);

				int j = 0;
				while (j < status.steps) {
					try {
						status.currentStep = j;
						rv.add(req.step(j));
					} catch (Cancel c) {
						throw c;
					} catch (Throwable t) {
						throw helper.cancel(new ERR(), t, "bad-step", "step",
								"" + j);
					}
					j++;
				}

				return rv;
			}
		});

		// Post-process
		for (int step = 0; step < status.steps; step++) {
			Object obj = rv.get(step);
			req.buildResponse(step, obj);
		}

		Response rsp = req.getResponse();
		if (rsp instanceof ERR) {
			fail(rsp.toString());
		}

		return (OriginalMetadataResponse) rsp;
	}

	@Test
	public void testFileset() throws Exception {
		OriginalMetadataRequestI req = new OriginalMetadataRequestI(
				(PixelsService) user.ctx.getBean("/ODE/Pixels"));
		req.imageId = makeImage(); // FAILING
		OriginalMetadataResponse rsp = assertRequest(req, null);
	}

    /**
     * Test that pre-FS original_metadata.txt files are parsed as expected,
     * including selection of which "=" to split at, and non-ASCII characters.
     * @throws FileNotFoundException if the test INI-style file is not accessible
     */
    @Test
    public void testMetadataParsing() throws FileNotFoundException {
        final OriginalMetadataRequestI request = new OriginalMetadataRequestI(null);
        request.init(new Helper(request, new Status(), null, null, null));
        request.parseOriginalMetadataTxt(ResourceUtils.getFile("classpath:original_metadata.txt"));
        request.buildResponse(0, null);
        final OriginalMetadataResponse response = (OriginalMetadataResponse) request.getResponse();
        final Map<String, String> actualGlobalMetadata = new HashMap<String, String>();
        for (final Entry<String, RType> keyValue : response.globalMetadata.entrySet()) {
            actualGlobalMetadata.put(keyValue.getKey(), ((RString) keyValue.getValue()).getValue());
        }
        Assert.assertTrue(CollectionUtils.isEqualCollection(expectedGlobalMetadata.entrySet(), actualGlobalMetadata.entrySet()));
        final Map<String, String> actualSeriesMetadata = new HashMap<String, String>();
        for (final Entry<String, RType> keyValue : response.seriesMetadata.entrySet()) {
            actualSeriesMetadata.put(keyValue.getKey(), ((RString) keyValue.getValue()).getValue());
        }
        Assert.assertTrue(CollectionUtils.isEqualCollection(expectedSeriesMetadata.entrySet(), actualSeriesMetadata.entrySet()));
    }
}