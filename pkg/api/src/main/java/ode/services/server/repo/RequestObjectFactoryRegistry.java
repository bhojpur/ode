package ode.services.server.repo;

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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import loci.formats.IFormatReader;
import ode.io.nio.PixelsService;
import ode.io.nio.ReaderSecurityCheck;
import ode.io.nio.TileSizes;
import ode.services.server.fire.Registry;
import ode.services.server.fire.Ring;
import ode.system.OdeContext;
import ode.formats.importer.ImportConfig;
import ode.formats.importer.ODEWrapper;

import ode.util.Resources;

/**
 * Requests which are handled by the repository servants.
 */
public class RequestObjectFactoryRegistry extends
        ode.util.ObjectFactoryRegistry implements ApplicationContextAware {

    private final Registry reg;

    private final TileSizes sizes;

    private final RepositoryDao dao;

    private final Ring ring;

    private final PixelsService pixels;

    private final Resources resources;

    private final ReaderSecurityCheck readerSecurityCheck;

    private/* final */OdeContext ctx;

    @Deprecated
    public RequestObjectFactoryRegistry(Registry reg, TileSizes sizes,
            RepositoryDao repositoryDao, Ring ring,
            PixelsService pixels) {
        this(reg, sizes, repositoryDao, ring, pixels, null);
    }

    @Deprecated
    public RequestObjectFactoryRegistry(Registry reg, TileSizes sizes,
            RepositoryDao repositoryDao, Ring ring,
            PixelsService pixels, Resources resources) {
        this(reg, sizes, repositoryDao, ring, pixels, resources,
                new ReaderSecurityCheck() {
                    @Override
                    public void assertUsedFilesReadable(IFormatReader reader) {
                    }
        });
    }

    public RequestObjectFactoryRegistry(Registry reg, TileSizes sizes,
            RepositoryDao repositoryDao, Ring ring,
            PixelsService pixels, Resources resources, ReaderSecurityCheck readerSecurityCheck) {
        this.reg = reg;
        this.sizes = sizes;
        this.dao = repositoryDao;
        this.ring = ring;
        this.pixels = pixels;
        this.resources = resources;
        this.readerSecurityCheck = readerSecurityCheck;
    }

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    public Map<String, ObjectFactory> createFactories(Ice.Communicator ic) {
        Map<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();
        factories.put(ManagedImportRequestI.ice_staticId(), new ObjectFactory(
                ManagedImportRequestI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                ManagedImportRequestI mir = new ManagedImportRequestI(reg, sizes, dao,
                        new ODEWrapper(
                                new ImportConfig(),
                                pixels.getMemoizerWait(),
                                pixels.getMemoizerDirectory(),
                                readerSecurityCheck),
                        ring.uuid);
                mir.setResources(resources);
                return mir;
            }

        });
        factories.put(RawAccessRequestI.ice_staticId(), new ObjectFactory(
                RawAccessRequestI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                return new RawAccessRequestI(reg);
            }

        });
        return factories;
    }

}