package ode.testing;

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

import ode.api.IAdmin;
import ode.api.IAnalysis;
import ode.api.IConfig;
import ode.api.IContainer;
import ode.api.IPixels;
import ode.api.IQuery;
import ode.api.ITypes;
import ode.api.IUpdate;
import ode.system.ServiceFactory;
import odeis.providers.re.RenderingEngine;

import org.jmock.Mock;

/**
 * <a href="http://jmock.org">JMock'ed</a> ServiceFactory whose public fields
 * can be set like:
 * 
 * <code>
 *   mockServiceFactory.mockUpdate = mock(IUpdate.class);
 * </code>
 * 
 * and later used like:
 * 
 * <code>
 *   mockServiceFactory.mockUpdate.expects( once() ).method( "saveObject" );
 * </code>
 * 
 * when the test-case subclasses {@link org.jmock.MockObjectTestCase}.
 * 
 * @see org.jmock.MockObjectTestCase
 */
public class MockServiceFactory extends ServiceFactory {

    @Override
    protected String getDefaultContext() {
        return null;
    }

    public Mock mockAdmin = new Mock(IAdmin.class);

    @Override
    public IAdmin getAdminService() {
        return (IAdmin) mockAdmin.proxy();
    }

    public Mock mockAnalysis = new Mock(IAnalysis.class);

    @Override
    public IAnalysis getAnalysisService() {
        return (IAnalysis) mockAnalysis.proxy();
    }

    public Mock mockPixels = new Mock(IPixels.class);

    @Override
    public IPixels getPixelsService() {
        return (IPixels) mockPixels.proxy();
    }

    public Mock mockConfig = new Mock(IConfig.class);

    @Override
    public IConfig getConfigService() {
        return (IConfig) mockConfig.proxy();
    }
    
    public Mock mockContainer = new Mock(IContainer.class);

    @Override
    public IContainer getContainerService() {
        return (IContainer) mockContainer.proxy();
    }

    public Mock mockQuery = new Mock(IQuery.class);

    @Override
    public IQuery getQueryService() {
        return (IQuery) mockQuery.proxy();
    }

    public Mock mockRendering = new Mock(RenderingEngine.class);

    public RenderingEngine getRenderingService() {
        return (RenderingEngine) mockRendering.proxy();
    }

    public Mock mockUpdate = new Mock(IUpdate.class);

    @Override
    public IUpdate getUpdateService() {
        return (IUpdate) mockUpdate.proxy();
    }

    public Mock mockTypes = new Mock(ITypes.class);

    @Override
    public ITypes getTypesService() {
        return (ITypes) mockTypes.proxy();
    }

}