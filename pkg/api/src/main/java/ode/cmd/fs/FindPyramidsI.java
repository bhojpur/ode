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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.api.IQuery;
import ode.io.bioformats.BfPixelBuffer;
import ode.io.bioformats.BfPixelsWrapper;
import ode.io.bioformats.BfPyramidPixelBuffer;
import ode.io.nio.PixelBuffer;
import ode.io.nio.PixelsService;
import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.parameters.Parameters;
import ode.services.util.ReadOnlyStatus;
import ode.cmd.HandleI.Cancel;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.FindPyramids;
import ode.cmd.FindPyramidsResponse;
import ode.cmd.Response;

/**
 * Retrieves pyramid files
 */
public class FindPyramidsI extends FindPyramids implements IRequest, ReadOnlyStatus.IsAware {

    private static final long serialVersionUID = -1L;

    private final FindPyramidsResponse rsp = new FindPyramidsResponse();

    private static Logger log = LoggerFactory.getLogger(FindPyramidsI.class);

    /** The collection of pyramid image ID.*/
    private List<Long> imageIds = new ArrayList<Long>();

    private final PixelsService pixelsService;

    private Helper helper;

    private IQuery service;

    private String query;

    private File pixeldsDir;

    public FindPyramidsI(PixelsService pixelsService)
    {
        this.pixelsService = pixelsService;
        pixeldsDir = new File(pixelsService.getPixelsDirectory());
    }

    @Override
    public Map<String, String> getCallContext() {
        Map<String, String> all = new HashMap<String, String>();
        all.put("ode.group", "-1");
        return all;
    }

    @Override
    public void init(Helper helper) throws Cancel {
        this.helper = helper;
        service = helper.getServiceFactory().getQueryService();
        helper.setSteps(1);
    }

    @Override
    public Object step(int step) throws Cancel {
        helper.assertStep(step);
        switch (step) {
            case 0:
                findPyramids(); break;
            default:
                throw helper.cancel(new ERR(), null, "unknown-step", "step" , ""+step);
        }
        return null;
    }

    @Override
    public void finish() throws Cancel {
     // no-op
    }

    @Override
    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (step == 0) {
            helper.setResponseIfNull(rsp);
        }
    }

    @Override
    public Response getResponse() {
        return helper.getResponse();
    }

    /**
     * Finds the pyramids and prepares the response
     */
    private void findPyramids() {
        StringBuilder sb = new StringBuilder();
        sb.append("select p from Pixels as p ");
        sb.append("left outer join fetch p.image as i ");
        sb.append("left outer join fetch i.details.creationEvent ");
        sb.append("where p.id = :id");
        query = sb.toString();
        walkDirectory(pixeldsDir);
        rsp.pyramidFiles = imageIds;
    }

    /**
     * Walks the specified directory.
     *
     * @param dir The directory to walk.
     */
    private void walkDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (imageIds.size() >= limit) {
                break;
            }
            if (file.isDirectory()) {
                walkDirectory(file);
            } else {
                String name = file.getName();
                if (name.endsWith("_pyramid")) {
                    boolean toFind = true;
                    if (checkEmptyFile) {
                        if (file.length() == 0) {
                            File[] list = pixeldsDir.listFiles();
                            for (File lockfile : list) {
                                String n = lockfile.getName();
                                if (n.startsWith("." + name) &&
                                (n.endsWith(".tmp") || n.endsWith(".pyr_lock"))) {
                                    toFind = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (toFind) {
                        String[] values = name.split("_");
                        long id = getImage(Long.parseLong(values[0]));
                        if (id >= 0) {
                            imageIds.add(id);
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds the image matching the criteria.
     * @param pixelsId The pixels set ID.
     * @return The image Id or -1.
     */
    private long getImage(long pixelsId) {
        Pixels pixels = service.findByQuery(query, new Parameters().addId(pixelsId));
        if (pixels == null) {
            return -1;
        }
        Image image = pixels.getImage();
        if (checkEmptyFile) {
            return image.getId().longValue();
        }
        long creation = image.getDetails().getCreationEvent().getTime().getTime();
        long time = -1;
        if (importedAfter != null) {
            time = importedAfter.getValue();
        }
        if (time < creation) {
            if (littleEndian == null || isLittleEndian(pixels) == littleEndian.getValue()) {
                return image.getId().longValue();
            }
        }
        return -1;
    }

    /**
     * Returns whether or not the pixels set is little endian.
     * @param pixels The pixels set to handle
     * @return See above.
     */
    private boolean isLittleEndian(Pixels pixels) {
        try {
            //TODO: review after work on OdePyramidWriter
            PixelBuffer pf = pixelsService._getPixelBuffer(pixels, false);
            if (pf instanceof BfPixelsWrapper) {
                return ((BfPixelsWrapper) pf).isLittleEndian();
            }
            if (pf instanceof BfPyramidPixelBuffer) {
                return ((BfPyramidPixelBuffer) pf).isLittleEndian();
            }
            if (pf instanceof BfPixelBuffer) {
                return ((BfPixelBuffer) pf).isLittleEndian();
            }
        } catch (Exception e) {
            log.debug("Error instantiating pixel buffer", e);
        }
        return false;
    }

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}