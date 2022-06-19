package ode.services.server.impl;

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

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import ode.api.IQuery;
import ode.api.IUpdate;
import ode.model.IObject;
import ode.model.core.OriginalFile;
import ode.parameters.Filter;
import ode.services.server.util.ServerExecutor;
import ode.services.server.util.ServerOnly;
import ode.services.server.util.ServiceFactoryAware;
import ode.services.roi.GeomTool;
import ode.services.throttling.Adapter;
import ode.services.util.Executor.SimpleWork;
import ode.system.ServiceFactory;
import ode.tools.hibernate.QueryBuilder;
import ode.util.SqlAction;
import ode.ServerError;
import ode.api.AMD_IRoi_findByImage;
import ode.api.AMD_IRoi_findByPlane;
import ode.api.AMD_IRoi_findByRoi;
import ode.api.AMD_IRoi_getMeasuredRois;
import ode.api.AMD_IRoi_getMeasuredRoisMap;
import ode.api.AMD_IRoi_getPoints;
import ode.api.AMD_IRoi_getRoiMeasurements;
import ode.api.AMD_IRoi_getRoiStats;
import ode.api.AMD_IRoi_getShapeStats;
import ode.api.AMD_IRoi_getShapeStatsList;
import ode.api.AMD_IRoi_getShapeStatsRestricted;
import ode.api.AMD_IRoi_getTable;
import ode.api.AMD_IRoi_uploadMask;
import ode.api.RoiOptions;
import ode.api.RoiResult;
import ode.api._IRoiOperations;
import ode.constants.namespaces.NSMEASUREMENT;
import ode.model.OriginalFileI;
import ode.model.Roi;
import ode.model.Shape;
import ode.util.IceMapper;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import Ice.Current;

/**
 * implementation of the IRoi service interface.
 */
public class RoiI extends AbstractAmdServant implements _IRoiOperations,
        ServiceFactoryAware, ServerOnly {

    protected ServiceFactoryI factory;

    protected final GeomTool geomTool;
    
    protected final SqlAction sql;

    public RoiI(ServerExecutor be, GeomTool geomTool, SqlAction sql) {
    	super(null, be);
        this.geomTool = geomTool;
	this.sql = sql;
    }

    public void setServiceFactory(ServiceFactoryI sf) {
        this.factory = sf;
    }

    // ~ Service methods
    // =========================================================================

    public void findByImage_async(AMD_IRoi_findByImage __cb,
            final long imageId, final RoiOptions opts, Current __current)
            throws ServerError {

        final IceMapper mapper = new RoiResultMapper(opts);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "findByImage", imageId, opts) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                final Filter f = filter(opts);
                final QueryBuilder qb = new QueryBuilder();
                qb.select("distinct r").from("Roi", "r");
                qb.join("r.image", "i", false, false);
                qb.join("r.shapes", "shapes", false, true); // fetch
                qb.join("r.folderLinks", "folderLinks", true, true); // fetch
                qb.join("folderLinks.parent", "folder", true, true); // fetch
                qb.where();
                qb.and("i.id = :id");
                qb.filter("r", f);
                qb.filterNow();
                qb.order("r.id", true); // ascending
                qb.param("id", imageId);
                return qb.queryWithoutFilter(session).list();
            }
        }));

    }

    public void findByRoi_async(AMD_IRoi_findByRoi __cb, final long roiId,
            final RoiOptions opts, Current __current) throws ServerError {

        final IceMapper mapper = new RoiResultMapper(opts);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "findByRoi", roiId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                RoiQueryBuilder qb = new RoiQueryBuilder(Arrays.asList(roiId), opts);
                return qb.query(session).list();
            }
        }));
    }

    public void findByPlane_async(AMD_IRoi_findByPlane __cb,
            final long imageId, final int z, final int t, final RoiOptions opts,
            Current __current) throws ServerError {

        final IceMapper mapper = new RoiResultMapper(opts);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "findByPlane", imageId, z, t) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {

                final Filter f = filter(opts);
                final QueryBuilder qb = new QueryBuilder();
                qb.select("distinct r").from("Roi", "r");
                qb.join("r.shapes", "s", false, true); // fetch
                qb.join("r.folderLinks", "folderLinks", true, true); // fetch
                qb.join("folderLinks.parent", "folder", true, true); // fetch
                qb.join("r.image", "i", false, false);
                qb.where();
                qb.and("i.id = :id");
                qb.and(" ( s.theZ is null or s.theZ = :z ) ");
                qb.and(" ( s.theT is null or s.theT = :t ) ");
                qb.filter("r", f);
                qb.filterNow();
                qb.order("r.id", true); // ascending
                qb.param("id", imageId);
                qb.param("z", z);
                qb.param("t", t);
                return qb.queryWithoutFilter(session).list();

            }
        }));
    }

    public void getPoints_async(AMD_IRoi_getPoints __cb, final long shapeId,
            Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getPoints", shapeId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                return geomTool.getPoints(shapeId, session);
            }
        }));
    }

    public void getShapeStats_async(AMD_IRoi_getShapeStats __cb,
            final long shapeId, Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getShapeStats", shapeId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                return geomTool.getStats(Arrays.asList(shapeId)).perShape[0];
            }
        }));

    }

    public void getShapeStatsList_async(AMD_IRoi_getShapeStatsList __cb,
            final List<Long> shapeIdList, Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getShapeStatsList", shapeIdList) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                return Arrays.asList(geomTool.getStats(shapeIdList).perShape);
            }
        }));
    }

    public void getRoiStats_async(AMD_IRoi_getRoiStats __cb, final long roiId,
            Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getRoiStats", roiId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                List<Long> shapesInRoi = sql.getShapeIds(roiId);
                return geomTool.getStats(shapesInRoi);
            }
        }));
    }

    public void getShapeStatsRestricted_async(AMD_IRoi_getShapeStatsRestricted __cb,
        final List<Long> shapeIdList, final int zForUnattached, final int tForUnattached,
        final int[] channels, Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getShapeStatsRestricted", 
                shapeIdList, zForUnattached, tForUnattached, channels) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                return
                    geomTool.getStatsRestricted(
                        shapeIdList, zForUnattached, tForUnattached, channels);
            }
        }));
    }

    // Measurement results.
    // =========================================================================

    public void getRoiMeasurements_async(AMD_IRoi_getRoiMeasurements __cb,
            final long imageId, final RoiOptions opts, Current __current)
            throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.FILTERABLE_COLLECTION);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getRoiMeasurements", imageId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                QueryBuilder qb = new QueryBuilder();
                qb.select("distinct fa");
                qb.from("Image", "i");
                qb.append(", Roi roi ");
                qb.join("roi.annotationLinks", "rlinks", false, false);
                qb.join("rlinks.child", "rfa", false, false);
                qb.join("i.wellSamples", "ws", false, false);
                qb.join("ws.well", "well", false, false);
                qb.join("well.plate", "plate", false, false);
                qb.join("plate.annotationLinks", "links", false, false);
                qb.join("links.child", "fa", false, false);
                qb.where();
                qb.and("fa.ns = '" + NSMEASUREMENT.value + "'");
                qb.and("rfa.id = fa.id");
                qb.and("i.id = :id");
                qb.and("i.id = roi.image");
                qb.param("id", imageId);
                qb.filter("fa", filter(opts));
                return qb.query(session).list();
            }
        }));
    }

    protected List<ode.model.roi.Roi> loadMeasuredRois(Session session,
            long imageId, long annotationId) {
        Query q = session
                .createQuery("select distinct r from Roi r join r.image i "
                        + "join fetch r.shapes join i.wellSamples ws join ws.well well "
                        + "join well.plate plate join plate.annotationLinks links "
                        + "join links.child a where a.id = :aid and i.id = :iid "
                        + "order by r.id");
        q.setParameter("iid", imageId);
        q.setParameter("aid", annotationId);
        return q.list();
    }

    public void getMeasuredRoisMap_async(AMD_IRoi_getMeasuredRoisMap __cb,
            final long imageId, final List<Long> annotationIds,
            RoiOptions opts, Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(new RoiResultMapReturnMapper(
                opts));

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getMeasuredRoisMap", imageId, annotationIds) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                if (annotationIds == null) {
                    return null;
                }
                Map<Long, List<ode.model.roi.Roi>> rv = new HashMap<Long, List<ode.model.roi.Roi>>();
                for (Long annotationId : annotationIds) {
                    rv.put(annotationId, loadMeasuredRois(session, imageId,
                            annotationId));
                }
                return rv;
            }
        }));
    }

    public void getMeasuredRois_async(AMD_IRoi_getMeasuredRois __cb,
            final long imageId, final long annotationId, final RoiOptions opts,
            Current __current) throws ServerError {

        final IceMapper mapper = new RoiResultMapper(opts);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getMeasuredRois", imageId, annotationId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {
                return loadMeasuredRois(session, imageId, annotationId);
            }
        }));
    }

    public void getTable_async(AMD_IRoi_getTable __cb, final long annotationId,
            final Current __current) throws ServerError {

        final IceMapper mapper = new IceMapper(IceMapper.UNMAPPED);

        runnableCall(__current, new Adapter(__cb, __current, mapper, factory
                .getExecutor(), factory.principal, new SimpleWork(this,
                "getTable", annotationId) {

            @Transactional(readOnly = true)
            public Object doWork(Session session, ServiceFactory sf) {

                QueryBuilder qb = new QueryBuilder();
                qb.select("f");
                qb.from("FileAnnotation", "fa");
                qb.join("fa.file", "f", false, false);
                qb.where();
                qb.and("fa.id = :id");
                qb.param("id", annotationId);
                OriginalFile file = (OriginalFile) qb.query(session)
                        .uniqueResult();

                if (file == null) {
                    throw new ode.conditions.ApiUsageException("No such file annotation: " + annotationId);
                }

                return file.getId();

            }
        }) {
            /* transforms the file annotation ID to a handle to an open table */
            @Override
            protected Object postProcess(Object rv) throws ServerError {
                return factory.sharedResources(__current).openTable(new OriginalFileI((Long) rv, false));
            }
        });
    }

    class MaskClass
    {
    	Set<Point> points;
    	int colour;
    	Point min, max;
    	int width;
		int height;
	 
    	MaskClass(int value)
    	{
    		points = new HashSet<Point>();
    		colour = value;
    	}
    	
    	public Color getColour()
    	{
    		return new Color(colour);
    	}
    	
    	
    	public byte[] asBytes() throws IOException
    	{
    	   		
    		byte[] data = new byte[(int) Math.ceil((double)width*(double)height/8.0)];
    		int offset = 0;
    		for(int y = min.y ; y < max.y + 1 ; y++)
    		{
    			for(int x = min.x ; x < max.x + 1 ; x++)
    			{
    				if(points.contains(new Point(x,y)))
    					setBit(data, offset, 1);
    				else
    					setBit(data, offset, 0);
    				offset++;
    			}
    		}
    		return data;
    	}
    	
    	public void add(Point p)
    	{
    		if(points.size()==0)
    		{
    			min = new Point(p);
    			max = new Point(p);
    		}
    		else
    		{
    			min.x = Math.min(p.x, min.x);
    			min.y = Math.min(p.y, min.y);
    			max.x = Math.max(p.x, max.x);
    			max.y = Math.max(p.y, max.y);
    		}
   			width = max.x-min.x+1;
			height = max.y-min.y+1;
    		points.add(p);
    	}
    	
    	public ode.model.roi.Mask asMaskI(int z, int t) throws IOException
    	{
    		ode.model.roi.Mask mask = new ode.model.roi.Mask();
    		mask.setX((double)min.x);
    		mask.setY((double)min.y);
    		mask.setWidth((double)width);
    		mask.setHeight((double)height);
    		mask.setLocked(true);
    		mask.setTheT(t);
    		mask.setTheZ(z);
    		byte[] theseBytes;
    		theseBytes = this.asBytes();
    		mask.setBytes(theseBytes);
    		return mask;
    	}
    	
    	/** 
    	 * Set the bit value in a byte array at position bit to be the value
    	 * value.
    	 * @param data See above.
    	 * @param bit See above.
    	 * @param val See above.
    	 */
    	private void setBit(byte[] data, int bit, int val) 
    	{
    		int bytePosition = bit/8;
    		int bitPosition = 7-bit%8;
    		data[bytePosition] = (byte) ((byte)(data[bytePosition]&
    									(~(byte)(0x1<<bitPosition)))|
    									(byte)(val<<bitPosition));
    	}

    	/** 
    	 * Set the bit value in a byte array at position bit to be the value
    	 * value.
    	 * @param data See above.
    	 * @param bit See above.
    	 * @param val See above.
    	 */
    	private byte getBit(byte[] data, int bit) 
    	{
    		int bytePosition = bit/8;
    		int bitPosition = 7-bit%8;
    		return (byte) ((byte)(data[bytePosition] & (0x1<<bitPosition))!=0 ? (byte)1 : (byte)0);
    	}

    }
    
    @SuppressWarnings("unchecked")
    private <T extends IObject> T safeReverse(Object o, IceMapper mapper) {
        try {
            return (T) mapper.reverse(o);
        } catch (Exception e) {
            throw new RuntimeException("Failed to safely reverse: " + o);
        }
    }
    
	public void uploadMask_async(final AMD_IRoi_uploadMask __cb,
			final long imageId, final int z, final int t, final byte[] bytes,
			final Current __current) throws ServerError
	{

		final IceMapper mapper = new IceMapper(IceMapper.VOID);

		runnableCall(__current, new Adapter(__cb, __current, mapper, factory
				.getExecutor(), factory.principal, new SimpleWork(this,
				"uploadMask", bytes)
		{

			@Transactional(readOnly = false)
			public Object doWork(Session session, ServiceFactory sf)
			{
				IUpdate update = sf.getUpdateService();
				
				ode.model.core.Image image;
				ode.model.roi.Roi roi;
				ByteArrayInputStream s = new ByteArrayInputStream(bytes);
				IQuery query = sf.getQueryService();
				IObject o =  query.findByQuery("from Image as i left outer join " +
						"fetch i.pixels as p where i.id = "+imageId, null);
				
				try
				{
					image = (ode.model.core.Image) o;
					BufferedImage inputImage = ImageIO.read(s);
					Map<Integer, MaskClass> map = new HashMap<Integer, MaskClass>();
					MaskClass mask;
					int value;
					for (int x = 0; x < inputImage.getWidth(); x++)
						for (int y = 0; y < inputImage.getHeight(); y++)
						{
							value = inputImage.getRGB(x, y);
							if(value==Color.black.getRGB())
								continue;
							if (!map.containsKey(value))
							{
								mask = new MaskClass(value);
								map.put(value, mask);
							}
							else
								mask = map.get(value);
							mask.add(new Point(x, y));
						}
					Iterator<Integer> maskIterator = map.keySet().iterator();
					while (maskIterator.hasNext())
					{
						int colour = maskIterator.next();
						mask = map.get(colour);
						roi = new ode.model.roi.Roi();
						roi.setImage(image);
						ode.model.roi.Mask  toSaveMask = mask.asMaskI(z, t);
						roi.addShape(toSaveMask);
						ode.model.roi.Roi newROI  = update.saveAndReturnObject(roi);
					}
					return null;
				} catch (Exception e)
				{
					__cb.ice_exception(e);
				}
				return null;
			}
		}));
	}
    
    // Helpers
    // =========================================================================

    private static Filter filter(RoiOptions opts) {
        Filter f = new Filter();
        if (opts != null) {
            if (opts.userId != null) {
                f.owner(opts.userId.getValue());
            }
            if (opts.groupId != null) {
                f.group(opts.groupId.getValue());
            }
            Integer offset = null;
            Integer limit = null;
            if (opts.offset != null) {
                offset = opts.offset.getValue();
            }
            if (opts.limit != null) {
                limit = opts.limit.getValue();
            }
            if (offset != null || limit != null) {
                f.page(offset, limit);
            }
        }
        return f;
    }

    private static class RoiQueryBuilder extends QueryBuilder {

        final RoiOptions opts;
        RoiQueryBuilder(List<Long> roiIds, RoiOptions opts) {
            this.opts = opts;
            this.paramList("ids", roiIds);
            this.select("distinct r");
            this.from("Roi", "r");
            this.join("r.shapes", "s", false, true);
            this.where();
        }

        @Override
        public Query query(Session session) {
            this.and("r.id in (:ids)");
            Filter f = RoiI.filter(opts);
            this.filter("r", f);
            this.filterNow();
            this.append("order by r.id");
            return super.queryWithoutFilter(session);
        }

    }

    public static class RoiResultMapper extends IceMapper {
        public RoiResultMapper(RoiOptions opts) {
            super(new RoiResultReturnMapper(opts));
        }
    }

    public static class RoiResultReturnMapper implements
            IceMapper.ReturnMapping {

        private final RoiOptions opts;

        public RoiResultReturnMapper(RoiOptions opts) {
            this.opts = opts;
        }

        @SuppressWarnings("unchecked")
        public Object mapReturnValue(IceMapper mapper, Object value)
                throws Ice.UserException {
            RoiResult result = new RoiResult();
            result.opts = opts;

            if (value == null) {
                result.rois = Collections.emptyList();
                result.byZ = Collections.emptyMap();
                result.byT = Collections.emptyMap();
                return result; // EARLY EXIT
            }

            List<Roi> rois = (List<Roi>) IceMapper.FILTERABLE_COLLECTION
                    .mapReturnValue(mapper, value);
            result.rois = rois;
            MultiMap byZ = new MultiValueMap();
            MultiMap byT = new MultiValueMap();
            for (Roi roi : rois) {
                ode.model.RoiI roii = (ode.model.RoiI) roi;
                Iterator<Shape> it = roii.iterateShapes();
                while (it.hasNext()) {
                    Shape shape = it.next();
                    if (shape == null) {
                        continue;
                    }
                    if (shape.getTheT() != null) {
                        byT.put(shape.getTheT().getValue(), shape);
                    } else {
                        byT.put(-1, shape);
                    }
                    if (shape.getTheZ() != null) {
                        byZ.put(shape.getTheZ().getValue(), shape);
                    } else {
                        byZ.put(-1, shape);
                    }
                }
                result.byZ = byZ;
                result.byT = byT;
            }
            return result;
        }
    }

    public static class RoiResultMapReturnMapper implements
            IceMapper.ReturnMapping {

        private final RoiOptions opts;

        public RoiResultMapReturnMapper(RoiOptions opts) {
            this.opts = opts;
        }

        @SuppressWarnings("unchecked")
        public Object mapReturnValue(IceMapper mapper, Object value)
                throws Ice.UserException {

            Map<Long, RoiResult> rv = new HashMap<Long, RoiResult>();
            Map<Long, List<ode.model.roi.Roi>> iv = (Map<Long, List<ode.model.roi.Roi>>) value;

            RoiResultMapper m = new RoiResultMapper(opts);

            for (Map.Entry<Long, List<ode.model.roi.Roi>> entry : iv.entrySet()) {
                rv.put(entry.getKey(), (RoiResult) m.mapReturnValue(entry
                        .getValue()));
            }

            return rv;
        }

    }
    
    private class NamespaceKeywords
    {
    	public String[] namespaces;
    	public String[][] keywords;
    }
    
}