package ode.cmd;

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

import ode.io.nio.PixelsService;
import ode.io.nio.ThumbnailService;
import ode.security.ACLVoter;
import ode.security.SecuritySystem;
import ode.security.auth.PasswordProvider;
import ode.security.auth.PasswordUtil;
import ode.services.mail.MailUtil;
import ode.system.OdeContext;
import ode.system.Roles;
import ode.tools.hibernate.ExtendedMetadata;
import ode.cmd.admin.ResetPasswordRequestI;
import ode.cmd.basic.DoAllI;
import ode.cmd.basic.ListRequestsI;
import ode.cmd.basic.TimingI;
import ode.cmd.fs.FindPyramidsI;
import ode.cmd.fs.ManageImageBinariesI;
import ode.cmd.fs.OriginalMetadataRequestI;
import ode.cmd.fs.UsedFilesRequestI;
import ode.cmd.graphs.Chgrp2I;
import ode.cmd.graphs.ChildOptionI;
import ode.cmd.graphs.Chmod2I;
import ode.cmd.graphs.Chown2I;
import ode.cmd.graphs.Delete2I;
import ode.cmd.graphs.DiskUsage2I;
import ode.cmd.graphs.DiskUsageI;
import ode.cmd.graphs.DuplicateI;
import ode.cmd.graphs.FindChildrenI;
import ode.cmd.graphs.FindParentsI;
import ode.cmd.graphs.GraphRequestFactory;
import ode.cmd.graphs.LegalGraphTargetsI;
import ode.cmd.graphs.SkipHeadI;
import ode.cmd.mail.SendEmailRequestI;

/**
 * SPI type picked up from the Spring configuration and given a chance to
 * register all its {@link Ice.ObjectFactory} instances with the
 * {@link Ice.Communicator}.
 */
public class RequestObjectFactoryRegistry extends
        ode.util.ObjectFactoryRegistry implements ApplicationContextAware {

    private final ExtendedMetadata em;

    private final ACLVoter voter;

    private final Roles roles;

    private final PixelsService pixelsService;

    private final ThumbnailService thumbnailService;

    private final MailUtil mailUtil;

    private final PasswordUtil passwordUtil;
    
    private final SecuritySystem sec;
    
    private final PasswordProvider passwordProvider;
    
    private final GraphRequestFactory graphRequestFactory;

    private/* final */OdeContext ctx;

    public RequestObjectFactoryRegistry(ExtendedMetadata em,
            ACLVoter voter,
            Roles roles,
            PixelsService pixelsService,
            ThumbnailService thumbnailService,
            MailUtil mailUtil,
            PasswordUtil passwordUtil,
            SecuritySystem sec,
            PasswordProvider passwordProvider,
            GraphRequestFactory graphRequestFactory) {

        this.em = em;
        this.voter = voter;
        this.roles = roles;
        this.pixelsService = pixelsService;
        this.thumbnailService = thumbnailService;
        this.mailUtil = mailUtil;
        this.passwordUtil = passwordUtil;
        this.sec = sec;
        this.passwordProvider = passwordProvider;
        this.graphRequestFactory = graphRequestFactory;
    }

    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException {
        this.ctx = (OdeContext) ctx;
    }

    public Map<String, ObjectFactory> createFactories(final Ice.Communicator ic) {
        Map<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();
        factories.put(TimingI.ice_staticId(), new ObjectFactory(
                TimingI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                return new TimingI();
            }

        });
        factories.put(DoAllI.ice_staticId(), new ObjectFactory(
                DoAllI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                return new DoAllI(ctx);
            }

        });
        factories.put(ListRequestsI.ice_staticId(), new ObjectFactory(
                ListRequestsI.ice_staticId()) {
            @Override
            public Ice.Object create(String name) {
                return new ListRequestsI(ctx);
            }

        });
        factories.put(Chgrp2I.ice_staticId(),
                new ObjectFactory(Chgrp2I.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(Chgrp2I.class);
                    }

                });
        factories.put(Chmod2I.ice_staticId(),
                new ObjectFactory(Chmod2I.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(Chmod2I.class);
                    }

                });
        factories.put(Chown2I.ice_staticId(),
                new ObjectFactory(Chown2I.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(Chown2I.class);
                    }

                });
        factories.put(Delete2I.ice_staticId(),
                new ObjectFactory(Delete2I.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(Delete2I.class);
                    }

                });
        factories.put(SkipHeadI.ice_staticId(),
                new ObjectFactory(SkipHeadI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(SkipHeadI.class);
                    }

                });
        factories.put(LegalGraphTargetsI.ice_staticId(),
                new ObjectFactory(LegalGraphTargetsI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new LegalGraphTargetsI(graphRequestFactory);
                    }
                });
        factories.put(OriginalMetadataRequestI.ice_staticId(),
                new ObjectFactory(OriginalMetadataRequestI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new OriginalMetadataRequestI(pixelsService);
                    }
                });
        factories.put(UsedFilesRequestI.ice_staticId(),
                new ObjectFactory(UsedFilesRequestI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new UsedFilesRequestI(pixelsService);
                    }
                });
        factories.put(ManageImageBinariesI.ice_staticId(),
                new ObjectFactory(ManageImageBinariesI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new ManageImageBinariesI(pixelsService, voter);
                    }
                });
        factories.put(FindPyramidsI.ice_staticId(),
                new ObjectFactory(FindPyramidsI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new FindPyramidsI(pixelsService);
                    }
                });
        factories.put(DiskUsageI.ice_staticId(),
                new ObjectFactory(DiskUsageI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return new DiskUsageI(pixelsService, thumbnailService, graphRequestFactory.getGraphPathBean());
                    }
                });
        factories.put(DiskUsage2I.ice_staticId(),
                new ObjectFactory(DiskUsage2I.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        final DiskUsage2I request = graphRequestFactory.getRequest(DiskUsage2I.class);
                        request.setPixelsService(pixelsService);
                        request.setThumbnailService(thumbnailService);
                        return request;
                    }
                });
        factories.put(DuplicateI.ice_staticId(),
                new ObjectFactory(DuplicateI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(DuplicateI.class);
                    }
                });
        factories.put(SendEmailRequestI.ice_staticId(),
                new ObjectFactory(SendEmailRequestI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                    	return new SendEmailRequestI(mailUtil);
                    }
                });
        factories.put(ResetPasswordRequestI.ice_staticId(),
                new ObjectFactory(ResetPasswordRequestI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                    	return new ResetPasswordRequestI(mailUtil, passwordUtil, sec, passwordProvider);
                    }
                });
        factories.put(FindParentsI.ice_staticId(),
                new ObjectFactory(FindParentsI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(FindParentsI.class);
                    }
                });
        factories.put(FindChildrenI.ice_staticId(),
                new ObjectFactory(FindChildrenI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.getRequest(FindChildrenI.class);
                    }
                });
        /* request parameters */
        factories.put(ChildOptionI.ice_staticId(),
                new ObjectFactory(ChildOptionI.ice_staticId()) {
                    @Override
                    public Ice.Object create(String name) {
                        return graphRequestFactory.createChildOption();
                    }
                });
        return factories;
    }

}