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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;

import ode.grid.ImportProcessPrx;
import ode.model.Fileset;

/**
 * Helper class for holding the individual {@link ManagedImportProcessI}
 * instances created by {@link ManagedRepositoryI}. Responsible for providing
 * lookups as well as keep-alive and reap methods which will be called by
 * background threads.
 */
public class ProcessContainer {

    private final static Logger log = LoggerFactory.getLogger(ProcessContainer.class);

    public interface Process {
        ImportProcessPrx getProxy();
        Fileset getFileset();
        long getGroup();
        void ping();
        void shutdown();
    }

    private final static class Data {

        private static final MapMaker mapMaker = new MapMaker();

        private final Map<Process, Object> processes = mapMaker.makeMap();

        void add(Process process) {
            processes.put(process, process);
        }

        void remove(Process process) {
            processes.remove(process);
        }

        void fill(List<Process> toFill) {
            for (Process process : processes.keySet()) {
                toFill.add(process);
            }
        }

    }

    private final LoadingCache<Long, Data> perGroup = CacheBuilder.newBuilder().build(
            new CacheLoader<Long, Data>() {
                @Override
                public Data load(Long group) {
                    return new Data();
                }
            });

    private Data getOrCreate(Long group) {
        try {
            return perGroup.get(group);
        } catch (ExecutionException e) {
            /* cannot occur unless loading thread is interrupted */
            return null;
        }
    }

    /**
     * Called once the {@link ManagedImportProcessI} service is created.
     * @param process The process to handle.
     */
    public void addProcess(Process process) {
        Data data = getOrCreate(process.getGroup());
        data.add(process);
    }

    /**
     * Removes the process.
     * @param process The process to remove.
     */
    public void removeProcess(Process process) {
        final Data data = getOrCreate(process.getGroup());
        if (data != null) {
            data.remove(process);
        }
    }

    public List<Process> listProcesses(Collection<Long> groups) {
        if (groups == null) {
            groups = perGroup.asMap().keySet();
        }

        final List<Process> rv
            = new ArrayList<Process>();

        for (Long group : groups) {
            final Data d = getOrCreate(group);
            if (d != null) {
                d.fill(rv);
            }
        }
        return rv;
    }

    /**
     * Called periodically by quartz to keep the sessions alive.
     */
    public int pingAll() {
        // Use listImports since it's the fastest copy method we have.
        List<Process> processes = listProcesses(null);
        int errors = 0;
        for (Process process: processes) {
            try {
                process.ping();
            } catch (Throwable t) {
                errors++;
                log.info(String.format("Removing process [%s] due to error: %s",
                        process, t));
                log.debug(t.toString()); // slf4j migration: toString()
            }
        }
        return errors;
    }

    /**
     * Called on shutdown of the server.
     */
    public int shutdownAll() {
        // Use listImports since it's the fastest copy method we have.
        final List<Process> processes = listProcesses(null);
        int errors = 0;
        for (Process process: processes) {
            try {
                process.shutdown();
            } catch (Throwable t) {
                errors++;
                log.error("Ignoring error on process shutdown", t);
            }
        }
        return errors;
    }

}