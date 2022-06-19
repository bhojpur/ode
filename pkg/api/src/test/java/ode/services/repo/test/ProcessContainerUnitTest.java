package ode.services.repo.test;

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

import java.util.Arrays;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ode.services.server.repo.ProcessContainer;

@Test(groups = { "repo" })
public class ProcessContainerUnitTest extends MockObjectTestCase {

    ProcessContainer pc;

    Mock m;

    ProcessContainer.Process p;

    List<ProcessContainer.Process> procs;

    @BeforeMethod
    public void setUp() {
        procs = null;
        pc = new ProcessContainer();
        m = mock(ProcessContainer.Process.class);
        m.expects(atLeastOnce()).method("getGroup").will(returnValue(1L));
        p = (ProcessContainer.Process) m.proxy();
    }

    public void testListByGroup() {
        pc.addProcess(p);
        procs = pc.listProcesses(Arrays.asList(p.getGroup()));
        assertEquals(1, procs.size());
    }

    public void testListAll() {
        pc.addProcess(p);
        procs = pc.listProcesses(null);
        assertEquals(1, procs.size());
    }

    public void testRemove() {
        pc.addProcess(p);
        procs = pc.listProcesses(null);
        assertEquals(1, procs.size());
        pc.removeProcess(p);
        procs = pc.listProcesses(null);
        assertEquals(0, procs.size());
    }

    public void testPingOk() {
        m.expects(once()).method("ping");
        pc.addProcess(p);
        assertEquals(0, pc.pingAll());
    }

    public void testPingThrows() {
        m.expects(once()).method("ping")
            .will(throwException(new RuntimeException()));
        pc.addProcess(p);
        assertEquals(1, pc.pingAll());
    }

    public void testShutdownOk() {
        m.expects(once()).method("shutdown");
        pc.addProcess(p);
        assertEquals(0, pc.shutdownAll());
    }

    public void testShutdownThrows() {
        m.expects(once()).method("shutdown")
            .will(throwException(new RuntimeException()));
        pc.addProcess(p);
        assertEquals(1, pc.shutdownAll());
    }

}