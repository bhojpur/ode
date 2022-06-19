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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import loci.formats.IFormatReader;

import ode.api.IQuery;
import ode.conditions.SecurityViolation;
import ode.io.nio.ReaderSecurityCheck;
import ode.parameters.Parameters;
import ode.services.server.repo.path.FsFile;
import ode.services.server.repo.path.ServerFilePathTransformer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide Bhojpur ODE permissions checking for Bio-Formats readers applied to managed repository filesets.
 */
public class ManagedReaderSecurityCheck implements ReaderSecurityCheck  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedReaderSecurityCheck.class);

    private static final int BATCH_SIZE = 256;

    private final IQuery iQuery;
    private final Set<String> repoUuids;
    private final Map<String, ServerFilePathTransformer> repoServerPaths = new ConcurrentHashMap<>();

    /**
     * Construct a new thread-safe used-file security checker.
     * @param iQuery a query service for checking repository roots and image file accessibility
     * @param managedRepoUuids the UUIDs of the managed repositories; will not be taken as final
     */
    public ManagedReaderSecurityCheck(IQuery iQuery, Set<String> managedRepoUuids) {
       this.iQuery = iQuery;
       this.repoUuids = managedRepoUuids;
    }

    /**
     * Find the fileset ID for the given file in the managed repository.
     * @param repository the UUID of a file's repository
     * @param serverPath the file's path within the repository
     * @return the ID of the file's fileset, never {@code null}
     */
    private long getFilesetId(String repository, String serverPath) {
        final int lastSeparator = serverPath.lastIndexOf(FsFile.separatorChar);
        final String serverPathHead, serverPathTail;
        if (lastSeparator == -1) {
            serverPathHead = String.valueOf(FsFile.separatorChar);
            serverPathTail = serverPath;
        } else {
            serverPathHead = serverPath.substring(0, lastSeparator + 1);
            serverPathTail = serverPath.substring(serverPathHead.length());
        }
        final String hql =
                "SELECT e.fileset.id FROM FilesetEntry AS e LEFT JOIN e.originalFile AS o " +
                "WHERE o.repo = :repo AND o.path = :path AND o.name = :name";
        final Parameters parameters = new Parameters()
                .addString("repo", repository)
                .addString("path", serverPathHead)
                .addString("name", serverPathTail);
        final List<Object[]> results = iQuery.projection(hql, parameters);
        if (results.size() != 1) {
            final String error = "file " + serverPath + " does not have a unique readable fileset";
            LOGGER.warn(error);
            throw new SecurityViolation(error);
        }
        return (Long) results.get(0)[0];
    }

    @Override
    public void assertUsedFilesReadable(IFormatReader reader) throws SecurityViolation {
        /* Check that we know all the managed repository roots.*/
        final Set<String> missingRepos = Sets.difference(repoUuids, repoServerPaths.keySet());
        if (!missingRepos.isEmpty()) {
            final String hql = "SELECT hash, path || name FROM OriginalFile " +
                    "WHERE hash IN (:repos) AND repo IS NULL and mimetype = 'Repository'";
            final Parameters parameters = new Parameters().addSet("repos", missingRepos);
            for (final Object[] result : iQuery.projection(hql, parameters)) {
                final String repo = (String) result[0];
                final String path = (String) result[1];
                final ServerFilePathTransformer serverPaths = new ServerFilePathTransformer();
                serverPaths.setBaseDirFile(new File(path));
                repoServerPaths.put(repo, serverPaths);
                LOGGER.info("noted managed repository {}", repo);
            }
        }
        LOGGER.debug("checking used files for " + reader.getCurrentFile());
        final String[] usedFiles = reader.getUsedFiles();
        final SetMultimap<String, String> repoPaths = HashMultimap.create();
        final List<String> badPaths = new ArrayList<>();
        /* Check that all this batch of used files are in a managed repository. */
        for (final String usedFile : usedFiles) {
            boolean isWithinRepo = false;
            for (final Map.Entry<String, ServerFilePathTransformer> repoServerPath : repoServerPaths.entrySet()) {
                final String repo = repoServerPath.getKey();
                final ServerFilePathTransformer serverPaths = repoServerPath.getValue();
                final FsFile serverPath;
                try {
                    serverPath = serverPaths.getFsFileFromServerFile(new File(usedFile).getAbsoluteFile());
                } catch (IllegalArgumentException iae) {
                    continue;
                }
                repoPaths.put(repo, serverPath.toString());
                isWithinRepo = true;
                break;
            }
            if (!isWithinRepo) {
                badPaths.add(usedFile);
            }
        }
        if (!badPaths.isEmpty()) {
            final StringBuilder errorBuilder = new StringBuilder();
            errorBuilder.append("reader for " + reader.getCurrentFile() + " accesses data outside managed repository:");
            for (final String badPath : badPaths) {
                errorBuilder.append("\n\t");
                errorBuilder.append(badPath);
            }
            final String error = errorBuilder.toString();
            LOGGER.warn(error);
            throw new SecurityViolation(error);
        }
        /* Check that all the used files' Bhojpur ODE permissions have them readable by the current user. */
        final Map.Entry<String, String> repoPathExample = repoPaths.entries().iterator().next();
        final long filesetId = getFilesetId(repoPathExample.getKey(), repoPathExample.getValue());
        final String hql =
                "SELECT COUNT(DISTINCT o) FROM FilesetEntry AS e LEFT JOIN e.originalFile AS o " +
                "WHERE e.fileset.id = :fileset AND o.repo = :repo AND o.path || o.name IN (:paths)";
        for (final Map.Entry<String, Collection<String>> pathsOneRepo : repoPaths.asMap().entrySet()) {
            final String repo = pathsOneRepo.getKey();
            for (final List<String> paths : Iterables.partition(pathsOneRepo.getValue(), BATCH_SIZE)) {
                final Parameters parameters = new Parameters()
                        .addLong("fileset", filesetId)
                        .addString("repo", repo)
                        .addList("paths", new ArrayList<>(paths));
                final Object[] result = iQuery.projection(hql, parameters).get(0);
                if (paths.size() != (Long) result[0]) {
                    final String error = "reader for " + reader.getCurrentFile() + " uses file from repository " + repo +
                            " that is not readable from database";
                    LOGGER.warn(error);
                    throw new SecurityViolation(error);
                }
            }
        }
    }
}