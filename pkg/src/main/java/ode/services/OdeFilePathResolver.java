package ode.services;

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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ode.io.nio.AbstractFileSystemService;
import ode.io.nio.FilePathResolver;
import ode.model.core.Pixels;
import ode.util.SqlAction;

/**
 * Bhojpur ODE server based resolver for file paths.
 */
public class OdeFilePathResolver implements FilePathResolver
{
    /** The logger for this particular class */
    private static Logger log = LoggerFactory.getLogger(FilePathResolver.class);

    /** SQL action instance for this class. */
    protected final SqlAction sql;

    /** The server's Bhojpur ODE data directory. */
    protected final String odeDataDir;

    /**
     * Constructor.
     * @param sql SQL action instance for this class.
     * @param odeDataDir The server's Bhojpur ODE data directory.
     */
    public OdeFilePathResolver(SqlAction sql, String odeDataDir)
    {
        this.sql = sql;
        this.odeDataDir = odeDataDir;
    }

    /* (non-Javadoc)
     * @see ode.io.nio.FilePathResolver#getOriginalFilePath(ode.io.nio.AbstractFileSystemService, ode.model.core.Pixels)
     */
    public String getOriginalFilePath(AbstractFileSystemService service,
            Pixels pixels)
    {
        List<String> namePathRepo =
            sql.getPixelsNamePathRepo(pixels.getId());
        String name = namePathRepo.get(0);
        String path = namePathRepo.get(1);
        String repo = namePathRepo.get(2);
        if (name != null && path != null) // && repo == null)
            // FIXME: In order to enable multi-server FS proper
            // redirecting will need to happen at the pixel buffer
            // layer as is currently happening in RawPixelsStoreI etc.
            // In other words, far before we reach this code, we should
            // already have been re-directed to the proper repo.
        {
            File f = new File(repo == null ? odeDataDir : sql.findRepoRootPath(repo));
            f = new File(f, namePathRepo.get(1));
            f = new File(f, namePathRepo.get(0));
            String originalFilePath = f.getAbsolutePath();
            log.info("Metadata only file, resulting path: " +
                    originalFilePath);
            return originalFilePath;
        }
        return null;
    }
}