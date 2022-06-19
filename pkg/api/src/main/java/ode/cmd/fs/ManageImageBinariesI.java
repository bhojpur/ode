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

import static ode.rtypes.rlong;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ode.api.IQuery;
import ode.conditions.RootException;
import ode.io.nio.PixelsService;
import ode.model.IObject;
import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.model.display.Thumbnail;
import ode.model.fs.Fileset;
import ode.parameters.Parameters;
import ode.security.ACLVoter;
import ode.cmd.ERR;
import ode.cmd.HandleI.Cancel;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.ManageImageBinaries;
import ode.cmd.ManageImageBinariesResponse;
import ode.cmd.Response;

/**
 * Workflow for converting attached original images into Bhojpur ODE filesets. Input
 * is an Image ID and which of the various workflow steps ("deletePixels", etc)
 * will be performed. In any case, the image will be loaded and various metrics
 * of the used space stored in the {@link ManageImageBinariesResponse} instance.
 */
public class ManageImageBinariesI extends ManageImageBinaries implements
        IRequest {

    static class PixelFiles {
        final File pixels;
        final File backup;
        final File pyramid;
        PixelFiles(String path) {
            pixels = new File(path);
            backup = new File(path + "_bak");
            pyramid = new File(path + "_pyramid");
        }
        public void update(ManageImageBinariesResponse rsp) {
            if (pixels.exists()) {
                rsp.pixelsPresent = true;
                rsp.pixelSize = pixels.length();
            } else {
                rsp.pixelsPresent = false;
                if (backup.exists()) {
                    rsp.pixelSize = backup.length();
                }
            }
            rsp.pyramidPresent = false;
            if (pyramid.exists()) {
                rsp.pyramidPresent = true;
                rsp.pyramidSize = pyramid.length();
            }
        }
    }

    private static final long serialVersionUID = -1L;

    private final ManageImageBinariesResponse rsp = new ManageImageBinariesResponse();

    private final PixelsService pixelsService;

    private final ACLVoter voter;

    private Helper helper;

    private PixelFiles files;

    private List<File> thumbnailFiles = new ArrayList<File>();

    public ManageImageBinariesI(PixelsService pixelsService,
            ACLVoter voter) {
        this.pixelsService = pixelsService;
        this.voter = voter;
    }

    //
    // CMD API
    //

    public Map<String, String> getCallContext() {
        Map<String, String> all = new HashMap<String, String>();
        all.put("ode.group", "-1");
        return all;
    }

    public void init(Helper helper) {
        this.helper = helper;
        this.helper.setSteps(7);
    }

    public Object step(int step) {
        helper.assertStep(step);
        switch (step) {
        case 0: findImage(); break;
        case 1: findAttached(); break;
        case 2: findBinary(); break;
        case 3: findFileset(); break;
        case 4: togglePixels(); break;
        case 5: deletePyramid(); break;
        case 6: deleteThumbnails(); break;
        default:
            throw helper.cancel(new ERR(), null, "unknown-step", "step" , ""+step);
        }
        return null;
    }

    @Override
    public void finish() throws Cancel {
        // no-op
    }

    public void buildResponse(int step, Object object) {
        helper.assertResponse(step);
        if (helper.isLast(step)) {
            helper.setResponseIfNull(rsp);
        }
    }

    public Response getResponse() {
        return helper.getResponse();
    }

    //
    // STEP METHODS
    //

    /**
     * Simply load the image for this instance, calling cancel if it cannot
     * loadable.
     */
    private void findImage() {
        try {
            IQuery query = helper.getServiceFactory().getQueryService();
            // Load as a test.
            query.get(Image.class, imageId);
        } catch (RootException re) {
            throw helper.cancel(new ERR(), re, "no-image", "image-id", ""
                    + imageId);
        }
    }

    private void findAttached() {
        IQuery query = helper.getServiceFactory().getQueryService();
        List<IObject> rv = query.findAllByQuery(
            "select o from Image i join i.pixels p " +
            "join p.pixelsFileMaps m join m.parent o " +
            "where i.id = :id", new Parameters().addId(imageId));
        rsp.archivedFiles = new ArrayList<Long>();
        for (IObject obj : rv) {
            long id = obj.getId();
            rsp.archivedFiles.add(id);
            File f = new File(pixelsService.getFilesPath(id));
            if (f.exists()) {
                rsp.archivedSize += f.length();
            }
        }
    }

    /**
     * Use {@link PixelsService} to find pre-FS binary files under
     * "/ODE/Files", "/ODE/Pixels", and "/ODE/Thumbnails",
     *  and store their size in the response.
     */
    private void findBinary() {
        IQuery query = helper.getServiceFactory().getQueryService();
        Pixels pixels = query.get(Image.class, imageId).getPrimaryPixels();
        List<Thumbnail> thumbs = query.findAllByQuery(
                "select tb from Thumbnail tb where " +
                "tb.pixels.id = :id", new Parameters().addId(pixels.getId()));

        String path = pixelsService.getPixelsPath(pixels.getId());
        files = new PixelFiles(path);
        files.update(rsp);
        for (Thumbnail tb: thumbs) {
            path = pixelsService.getThumbnailPath(tb.getId());
            File thumbnailFile = new File(path);
            thumbnailFiles.add(thumbnailFile);
            rsp.thumbnailSize += thumbnailFile.length();
        }
    }

    /**
     * Load the {@link Fileset} for the imageId if it exists.
     */
    private void findFileset() {
        try {
            IQuery query = helper.getServiceFactory().getQueryService();
            Fileset fs = query.findByQuery(
                    "select fs from Image i join i.fileset fs "
                            + "where i.id = :id",
                    new Parameters().addId(imageId));
            if (fs != null) {
                rsp.filesetId = rlong(fs.getId());
            }
        } catch (RootException re) {
            throw helper.cancel(new ERR(), re, "fileset-load-err", "image-id",
                    "" + imageId);
        }

    }

    private void togglePixels() {
        if (togglePixels) {
            requireFileset("pixels");
            processFile("pixels-move", files.pixels, files.backup);
            files.update(rsp);
        }
    }

    private void deletePyramid() {
        if (deletePyramid) {
            requireFileset("pyramid");
            processFile("pyramid", files.pyramid, null);
            files.update(rsp);
        }
    }

    private void deleteThumbnails() {
        if (deleteThumbnails) {
            Iterator<File> i = thumbnailFiles.iterator();
            while (i.hasNext()) {
                processFile("thumbnail", i.next(), null);
            }
        }
    }

    private void requireFileset(String which) {
        if (rsp.filesetId == null) {
            throw helper.cancel(new ERR(), null, which + "-requires-fileset");
        }
    }

    private void processFile(String which, File file, File dest) {

        if (!file.exists()) {
            return; // Nothing to do
        }

        IQuery query = helper.getServiceFactory().getQueryService();
        Image image = query.get(Image.class, imageId);
        if (!voter.allowDelete(image, image.getDetails())) {
            throw helper.cancel(new ERR(), null, which + "-delete-disallowed");
        }
        if (dest != null) {
            if (!file.renameTo(dest)) {
                throw helper.cancel(new ERR(), null, which + "-delete-false");
            }
        } else {
            if (!file.delete()) {
                // TODO: should we schedule for deleteOnExit here?
                throw helper.cancel(new ERR(), null, which + "-delete-false");
            }
        }
    }
}