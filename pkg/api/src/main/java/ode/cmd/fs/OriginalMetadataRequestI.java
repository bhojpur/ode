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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;

import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import ode.api.IQuery;
import ode.io.nio.PixelsService;
import ode.model.annotations.FileAnnotation;
import ode.model.core.Image;
import ode.model.core.Pixels;
import ode.model.fs.Fileset;
import ode.parameters.Parameters;
import ode.services.util.ReadOnlyStatus;
import ode.RType;
import ode.cmd.ERR;
import ode.cmd.Helper;
import ode.cmd.IRequest;
import ode.cmd.OriginalMetadataRequest;
import ode.cmd.OriginalMetadataResponse;
import ode.cmd.Response;
import ode.cmd.HandleI.Cancel;
import ode.constants.annotation.file.ORIGINALMETADATA;
import ode.constants.namespaces.NSCOMPANIONFILE;
import ode.util.IceMapper;

import com.google.common.collect.ImmutableMap;

/**
 * Original metadata loader, handling both pre-FS and post-FS data.
 */
public class OriginalMetadataRequestI extends OriginalMetadataRequest implements
		IRequest, ReadOnlyStatus.IsAware {

	private static final long serialVersionUID = -1L;

	private final OriginalMetadataResponse rsp = new OriginalMetadataResponse();

	private final PixelsService pixelsService;

	private Helper helper;

    public OriginalMetadataRequestI(PixelsService pixelsService) {
        this.pixelsService = pixelsService;
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
		this.helper.setSteps(1);
	}

	public Object step(int step) {
		helper.assertStep(step);
		loadFileset();
		if (rsp.filesetId == null) {
			loadFileAnnotation();
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
	// LOADING & PARSING
	//

	/**
	 * Searches for a {@link Fileset} attached to this {@link Image}, and if present,
	 * uses Bio-Formats to parse the metadata into the {@link OriginalMetadataResponse}
	 * instance. If no {@link Fileset} is present, then there <em>may</em> be a
	 * {@link FileAnnotation} present which has a static version of the metadata.
	 */
	protected void loadFileset() {
		rsp.filesetId = firstIdOrNull("select i.fileset.id from Image i where i.id = :id");
		if (rsp.filesetId != null) {
			final Image image = helper.getServiceFactory().getQueryService().get(Image.class, imageId);
			final Pixels pixels = image.getPrimaryPixels();

			try {
				final IFormatReader reader = pixelsService.getBfReader(pixels);
				final Hashtable<String, Object> global = reader.getGlobalMetadata();
				final Hashtable<String, Object> series = reader.getSeriesMetadata();
				rsp.globalMetadata = wrap(global);
				rsp.seriesMetadata = wrap(series);
			} catch (Throwable t) {
				helper.cancel(new ERR(), t, "bf-reader-failure", "pixels", ""+pixels.getId());
			}
		}
	}

    /**
     * Only called if {@link #loadFileset()} finds no {@link Fileset}. If any {@link FileAnnotation}
     * instances with the appropriate namespace and name are found, the first one is taken and
     * parsed into the {@link OriginalMetadataResponse}.
     */
    protected void loadFileAnnotation() {
        rsp.fileAnnotationId = firstIdOrNull(
                "select a.id from Image i join i.annotationLinks l join l.child a " +
                "where i.id = :id and a.file.name = '" + ORIGINALMETADATA.value + "' " +
                "and a.ns = '" + NSCOMPANIONFILE.value + "'");
        if (rsp.fileAnnotationId != null) {
            final IQuery iQuery = helper.getServiceFactory().getQueryService();
            final FileAnnotation fileAnnotation = iQuery.get(FileAnnotation.class, rsp.fileAnnotationId.getValue());
            final String filePath = pixelsService.getFilesPath(fileAnnotation.getFile().getId());
            parseOriginalMetadataTxt(new File(filePath));
        }
    }

	//
	// HELPERS
	//

	/**
	 * Use {@link IQuery#projection(String, Parameters)} to load the first
	 * long which matches the given query. This means that the first return
	 * value in the select statement should likely be the id of an object.
	 */
	protected ode.RLong firstIdOrNull(String query) {
		List<Object[]> ids = helper.getServiceFactory().getQueryService()
				.projection(query, new Parameters().addId(imageId).page(0, 1));
		if (ids != null && ids.size() > 0) {
		    Object[] id = ids.get(0);
		    if (id != null && id.length > 0) {
		        return ode.rtypes.rlong((Long) id[0]);
		    }
		}
		return null;
	}

	/**
	 * Use {@link IceMapper} to convert from {@link Object} instances in
	 * the given {@link Hashtable} to {@link RType} instances. This may
	 * throw an exception on unknown types.
	 */
	protected Map<String, RType> wrap(Hashtable<String, Object> table) {
		final Map<String, RType> rv = new HashMap<String, RType>();
		if (table == null || table.size() == 0) {
			return rv;
		}

		final IceMapper mapper = new IceMapper();
		for (Entry<String, Object> entry : table.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			try {
				if (val instanceof Short) {
					// Likely could be handled in toRType
					rv.put(key, mapper.toRType(((Short) val).intValue()));
				} else {
					rv.put(key, mapper.toRType(val));
				}
			} catch (Exception e) {
				String msg = String.format("Could not convert to rtype: " +
					"key=%s, value=%s, type=%s ", key, val,
					(val == null ? "null" : val.getClass()));
				if (helper == null) {
					// from command-line
					System.err.println(msg);
				} else {
					helper.warn(msg);
				}
			}
		}
		return rv;
	}

    /**
     * Split the given string at the rightmost '=' character among those that are the least enclosed by some kind of bracket.
     * @param keyValue the key = value string
     * @return the extracted key and value, or <code>null</code> if there is no '=' character
     */
    private static Map.Entry<String, String> splitOnEquals(String keyValue) {
        Integer equalsIndex = null;
        Integer equalsSmallestDepth = null;
        int currentIndex = 0;
        int currentDepth = 0;
        while (currentIndex < keyValue.length()) {
            switch (keyValue.charAt(currentIndex)) {
            case '(':
            case '[':
            case '{':
                currentDepth++;
                break;
            case ')':
            case ']':
            case '}':
                currentDepth--;
                break;
            case '=':
                if (equalsSmallestDepth == null || currentDepth <= equalsSmallestDepth) {
                    equalsIndex = currentIndex;
                    equalsSmallestDepth = currentDepth;
                }
                break;
            }
            currentIndex++;
        }
        if (equalsIndex == null) {
            return null;
        } else {
            return new AbstractMap.SimpleImmutableEntry<>(
                    keyValue.substring(0, equalsIndex).trim(),
                    keyValue.substring(equalsIndex + 1).trim());
        }
    }

    /**
     * Read the given INI-style file and populate the maps with the properties from the corresponding sections.
     * @param file the file to read
     */
    protected void parseOriginalMetadataTxt(File file) {
        final Pattern section = Pattern.compile("\\s*\\[\\s*(.+?)\\s*\\]\\s*");
        rsp.globalMetadata = new TreeMap<String, RType>();
        rsp.seriesMetadata = new TreeMap<String, RType>();
        final ImmutableMap<String, Map<String, RType>> sections =
                ImmutableMap.of("GlobalMetadata", rsp.globalMetadata, "SeriesMetadata", rsp.seriesMetadata);
        Map<String, RType> currentSection = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while (true) {
                String line;
                line = in.readLine();
                if (line == null) {
                    break;
                }
                Matcher matcher;
                if ((matcher = section.matcher(line)).matches()) {
                    currentSection = sections.get(matcher.group(1));
                } else if (currentSection != null) {
                    final Entry<String, String> keyValue = splitOnEquals(line);
                    if (keyValue != null) {
                        currentSection.put(keyValue.getKey(), ode.rtypes.rstring(keyValue.getValue()));
                    }
                }
            }
        } catch (IOException e) {
            if (helper != null) {
                helper.cancel(new ERR(), e, "reader-failure", "original-metadata", file.getPath());
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { }
            }
        }
    }

	public static void main(String[] args) throws Exception {
		OriginalMetadataRequestI omr = new OriginalMetadataRequestI(null);
		ImageReader reader = new ImageReader();
		for (String file : args) {
			reader.setId(file);
			final Hashtable<String, Object> bfglobal = reader.getGlobalMetadata();
			final Hashtable<String, Object> bfseries = reader.getSeriesMetadata();
			Map<String, RType> global = omr.wrap(bfglobal);
			Map<String, RType> series = omr.wrap(bfseries);
			printMap("[GlobalMetadata]", global);
			printMap("[SeriesMetadata]", series);
		}
		reader.close();
	}

	private static void printMap(String title, Map<String, RType> map) {
		System.out.println(title);
		for (Map.Entry<String, RType> entry : map.entrySet()) {
			System.out.print(entry.getKey());
			System.out.print("=");
			System.out.print(entry.getValue());
			System.out.print("\n");
		}
	}

    @Override
    public boolean isReadOnly(ReadOnlyStatus readOnly) {
        return true;
    }
}