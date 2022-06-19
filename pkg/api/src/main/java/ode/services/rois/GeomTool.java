package ode.services.roi;

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

import static ode.rtypes.rdouble;
import static ode.rtypes.rint;
import static ode.rtypes.rlong;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import ode.conditions.ApiUsageException;
import ode.io.nio.PixelBuffer;
import ode.model.IObject;
import ode.model.core.Pixels;
import ode.services.util.Executor;
import ode.tools.hibernate.SessionFactory;
import ode.util.Filterable;
import ode.util.SqlAction;
import ode.api.RoiStats;
import ode.api.ShapePoints;
import ode.api.ShapeStats;
import ode.model.AffineTransformI;
import ode.model.Ellipse;
import ode.model.Line;
import ode.model.Point;
import ode.model.Rectangle;
import ode.model.Shape;
import ode.model.SmartEllipseI;
import ode.model.SmartLineI;
import ode.model.SmartPointI;
import ode.model.SmartRectI;
import ode.model.SmartShape;
import ode.util.IceMapper;
import ode.util.ObjectFactoryRegistry.ObjectFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Strategy for handling the conversion between {@link Shape shapes} and
 * database-specific geometries.
 */
public class GeomTool {

    protected Logger log = LoggerFactory.getLogger(GeomTool.class);

    protected final AtomicBoolean hasShapes = new AtomicBoolean(true);

    protected final SqlAction sql;

    protected final SessionFactory factory;

    protected final PixelData data;

    protected final Executor ex;

    protected final String uuid;

    public GeomTool(PixelData data, SqlAction sql,
            SessionFactory factory) {
        this(data, sql, factory, null, null);
    }

    public GeomTool(PixelData data, SqlAction sql,
            SessionFactory factory, Executor ex, String uuid) {
        this.data = data;
        this.sql = sql;
        this.factory = factory;
        this.ex = ex;
        this.uuid = uuid;
    }

    /**
     * Loads just the shape and no other relationships. This
     * 
     * @param shapeId
     * @param session
     * @return See above.
     */
    private Shape justShapeById(long shapeId, Session session) {
        Query q = session.createQuery("select s from Shape s where s.id = :id");
        q.setParameter("id", shapeId);
        ode.model.roi.Shape shape = (ode.model.roi.Shape) q.uniqueResult();
        return (Shape) new ShapeMapper().map(shape);
    }

    //
    // Factory methods
    //

    public List<Shape> random(int count) {
        if (count < 1 || count > 100000) {
            throw new RuntimeException("Count out of bounds: " + count);
        }

        Map<String, ObjectFactory> map = new RoiTypes.RoiTypesObjectFactoryRegistry().createFactories(null);
        List<String> types = new ArrayList<String>(map.keySet());
        List<Shape> shapes = new ArrayList<Shape>();
        Random r = new Random();

        try {
            while (shapes.size() < count) {
                int which = r.nextInt(map.size());
                String typeName = types.get(which);
                ObjectFactory of = map.get(typeName);
                SmartShape s = (SmartShape) of.create("");
                Method m = s.getClass().getMethod("randomize", Random.class);
                m.invoke(s, r);
                shapes.add((Shape) s);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failure on creating shape "
                    + shapes.size(), e);
        }

        return shapes;
    }

    public Line ln(double x1, double y1, double x2, double y2) {
        SmartLineI rect = new SmartLineI();
        rect.setX1(rdouble(x1));
        rect.setY1(rdouble(y1));
        rect.setX2(rdouble(x2));
        rect.setY2(rdouble(y2));
        return rect;
    }

    public Rectangle rect(double x, double y, double w, double h) {
        SmartRectI rect = new SmartRectI();
        rect.setX(rdouble(x));
        rect.setY(rdouble(y));
        rect.setWidth(rdouble(w));
        rect.setHeight(rdouble(h));
        return rect;
    }

    public Point pt(double x, double y) {
        SmartPointI pt = new SmartPointI();
        pt.setX(rdouble(x));
        pt.setY(rdouble(y));
        return pt;
    }

    public Ellipse ellipse(double x, double y, double radiusx, double radiusy) {
        SmartEllipseI ellipse = new SmartEllipseI();
        ellipse.setX(rdouble(x));
        ellipse.setY(rdouble(y));
        ellipse.setRadiusX(rdouble(radiusx));
        ellipse.setRadiusY(rdouble(radiusy));
        return ellipse;
    }

    public Ellipse ellipse(double x, double y, double radiusx, double radiusy, int t,
            int z) {
        Ellipse ellipse = ellipse(x, y, radiusx, radiusy);
        ellipse.setTheT(rint(t));
        ellipse.setTheZ(rint(z));
        return ellipse;
    }

    //
    // Conversion methods
    //

    private static final String ORIGIN = "'(0,0)'";

    public String dbPath(Shape shape) {

        if (shape == null) {
            // doing anything we can to prevent null returns values
            log.warn("Shape is null");
            return ORIGIN;
        }

        SmartShape ss = assertSmart(shape);
        List<Point> points = ss.asPoints();
        // to prevent the objects from being repeatedly
        // checked, they must be set to something. Here we are using
        // the top-left point as a default (like SmartText)
        if (points == null) {
            return ORIGIN;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("'(");
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            SmartShape.Util.appendDbPoint(sb, points.get(i));
        }
        sb.append(")'");
        return sb.toString();
    }

    //
    // Database access methods
    //

    public ShapePoints getPoints(long shapeId, Session session) {
        Shape shape = justShapeById(shapeId, session);
        SmartShape smart = assertSmart(shape);
        final List<Integer> xs = new ArrayList<Integer>(); // This is not good
        final List<Integer> ys = new ArrayList<Integer>(); // nor is this.
        smart.areaPoints(new SmartShape.PointCallback() {
            public void handle(int x, int y) {
                xs.add(x);
                ys.add(y);
            }
        });
        ShapePoints sp = new ShapePoints();
        sp.x = new int[xs.size()];
        sp.y = new int[ys.size()];
        for (int i = 0; i < sp.x.length; i++) {
            sp.x[i] = xs.get(i);
            sp.y[i] = ys.get(i);
        }
        return sp;
    }

    public RoiStats getStats(List<Long> shapeIds) {

        if (shapeIds == null) {
            return null; // EARLY EXIT
        }

        final Session session = factory.getSession();
        final RoiStats rs = new RoiStats();
        rs.perShape = new ShapeStats[shapeIds.size()];

        for (int i = 0; i < shapeIds.size(); i++) {

            final long shapeId = shapeIds.get(i);

            final ode.model.roi.Shape shape = (ode.model.roi.Shape) session
                    .createQuery(
                            "select s from Shape s "
                                    + "join fetch s.roi r join fetch r.image i "
                                    + "join fetch i.pixels p join fetch p.channels c "
                                    + "join fetch c.logicalChannel lc "
                                    + "where s.id = :id").setParameter("id",
                            shapeId).uniqueResult();
            final SmartShape smartShape = (SmartShape) new ShapeMapper()
                    .map(shape);

            final ode.model.roi.Roi roi = shape.getRoi();
            final ode.model.core.Image img = roi.getImage();
            final ode.model.core.Pixels pix = img.getPrimaryPixels();

            final long roiId = roi.getId();
            final long imgId = img.getId();
            final long pixId = pix.getId();

            final int maxZ = pix.getSizeZ();
            final int maxT = pix.getSizeT();

            // We only take the values for the first Shape. If this call is
            // being made with different shapes, then the user will know as
            // much.
            if (rs.combined == null) {
                rs.roiId = roiId;
                rs.imageId = imgId;
                rs.pixelsId = pixId;

                int ch = pix.sizeOfChannels();
                rs.combined = makeStats(ch);
                rs.combined.shapeId = -1;
                rs.combined.channelIds = new long[ch];
                for (int w = 0; w < ch; w++) {
                    rs.combined.channelIds[w] = pix.getChannel(w)
                            .getLogicalChannel().getId();
                }
            }
            final ShapeStats stats = makeStats(pix, shape);
            stats.shapeId = shape.getId();

            final int ch = stats.channelIds.length;
            final double[] sumOfSquares = new double[ch];

            final Integer theZ = shape.getTheZ(); // May be null
            final Integer theT = shape.getTheT(); // May be null

            final int startZ = (theZ == null) ? 0 : theZ.intValue();
            final int startT = (theT == null) ? 0 : theT.intValue();

            final int endZ = (theZ == null) ? (maxZ - 1) : theZ.intValue();
            final int endT = (theT == null) ? (maxT - 1) : theT.intValue();

            final PixelBuffer buf = data.getBuffer(pixId);
            try {
                SmartShape.PointCallback cb = new SmartShape.PointCallback() {

                    public void handle(int x, int y) {

                        for (int w = 0; w < ch; w++) {

                            for (int z = startZ; z <= endZ; z++) {
                                for (int t = startT; t <= endT; t++) {

                                    // WHAT TO DO ABOUT THE CHANNELS IN AGGREGATION?
                                    stats.pointsCount[w]++;
                                    double value = data.get(buf, x, y, z, w, t);
                                    stats.min[w] = Math.min(value, stats.min[w]);
                                    stats.max[w] = Math.max(value, stats.max[w]);
                                    stats.sum[w] += value;
                                    sumOfSquares[w] += value * value;

                                }
                            }

                        }
                    }
                };

                smartShape.areaPoints(cb);
            } finally {
                try {
                    buf.close();
                } catch (IOException e) {
                    log.error("Error closing " + buf, e);
                }
            }

            for (int w = 0; w < ch; w++) {

                stats.mean[w] = stats.sum[w] / stats.pointsCount[w];
                if (stats.pointsCount[w] > 1) {
                    double sigmaSquare = (sumOfSquares[w] - stats.sum[w]
                            * stats.sum[w] / stats.pointsCount[w])
                            / (stats.pointsCount[w] - 1);
                    if (sigmaSquare > 0) {
                        stats.stdDev[w] = Math.sqrt(sigmaSquare);
                    }
                }
            }

            rs.perShape[i] = stats;
        }

        return rs;

    }

    public ShapeStats [] getStatsRestricted(
            List<Long> shapeIds, 
            int zForUnattached, int tForUnattached,
            int[] channels) {
        if (shapeIds == null || shapeIds.isEmpty())
            throw new ApiUsageException("Provide a non empty list of shape ids.");
        if (channels == null || channels.length == 0)
            throw new ApiUsageException("Provide a non empty array of channels.");

       // fetch shapes from db and perform some basic checks
        final Session session = factory.getSession();
        List results =
           session.createQuery(
               "select distinct s from Shape s " +
               "left join fetch s.transform t left join fetch s.roi r " +
               "join fetch r.image i join fetch i.pixels p " +
               "where s.id in (:ids)").
           setParameterList("ids", shapeIds).list();
       if (results.size() != shapeIds.size()) {
           throw new ApiUsageException("Given shape id(s) invalid");
       }

       final List<ShapeStats> shapeStats =
               new ArrayList<ShapeStats>(shapeIds.size());
       final Multimap<String, ode.model.roi.Shape> zt_lookup = HashMultimap.create();
       ode.model.core.Image image = null;
       ode.model.core.Pixels pixels = null;

       for (final Object r : results) {
           final ode.model.roi.Shape shape = (ode.model.roi.Shape) r;
           final ode.model.roi.Roi roi = shape.getRoi();
           final ode.model.core.Image img = roi.getImage();

           // check if all shapes come from the same image
           if (image == null) {
               image = img;
               pixels = image.getPrimaryPixels();
           } else if (!image.getId().equals(img.getId()))
               throw new ApiUsageException("All shapes have to be from the same image");
           // check if z/t unattached fallback values are not out of bounds
           if (zForUnattached < 0 || zForUnattached >= pixels.getSizeZ() ||
               tForUnattached < 0 || tForUnattached >= pixels.getSizeT())
               throw new ApiUsageException(
                   "Fallback value(s) for unattached z/t shapes are out of bounds");
           // if we have unattached z/t we use the unattached z,t fallback
           final int theZ = shape.getTheZ() != null ? shape.getTheZ() : zForUnattached;
           final int theT = shape.getTheT() != null ? shape.getTheT() : tForUnattached;
           zt_lookup.put(theZ + "/" + theT, shape);
       }

       // any point iteration in a tiled image is a lost cause
       if (data.requiresPixelsPyramid(pixels)) 
           throw new ApiUsageException("This method cannot handle tiled images yet.");

       // check if given channels are valid
       Set<Integer> validChannels = new HashSet<Integer>();
       if (channels != null && channels.length > 0) {
           for (int ch : channels) {
               if (ch < 0 || ch >= pixels.getSizeC())
                   throw new ApiUsageException("Given channel(s) out of bounds.");
               validChannels.add(ch);
           }
       }

       // common info for all shapes
       final long pixelId = pixels.getId();
       final int sizeX = pixels.getSizeX();
       final int sizeY = pixels.getSizeY();

       // loop over shapes (grouped by z/t planes)
       for (final String key : zt_lookup.keySet()) {
           final String[] keyTokens = key.split("/");
           final int z = Integer.parseInt(keyTokens[0]);
           final int t = Integer.parseInt(keyTokens[1]);

           for (ode.model.roi.Shape shape : zt_lookup.get(key)) {
               final SmartShape smartShape = (SmartShape) new ShapeMapper().map(shape);
               final int size_stats = validChannels.size();
               final ShapeStats stats = makeStats(size_stats);
               stats.shapeId = shape.getId();
               final double[] sumOfSquares = new double[size_stats];

               try (final PixelBuffer buf = data.getBuffer(pixelId)) {
            	   int i = 0;
                   for (int c : validChannels) {
                       final int w = i;
                       stats.channelIds[w] = c;
                       final ode.util.PixelData pd = data.getPlane(buf, z, c, t);
                       smartShape.areaPoints(
                           new SmartShape.PointCallback() {
                               public void handle(int x, int y) {
                                   // we won't use pixels outside of the image 
                                   if (x < 0 || y < 0 || x >= sizeX || y >= sizeY) return;
                                   stats.pointsCount[w]++;
                                   double value = pd.getPixelValue(sizeX * y + x);
                                   stats.min[w] = Math.min(value, stats.min[w]);
                                   stats.max[w] = Math.max(value, stats.max[w]);
                                   stats.sum[w] += value;
                                   sumOfSquares[w] += value * value;
                               }
                           });
                       pd.dispose();
                       i++;
                   }
               } catch (IOException io) {
                   log.error("Error closing buffer", io);
               }

               for (int w = 0; w < size_stats; w++) {
                   if (stats.pointsCount[w] > 0) {
                       stats.mean[w] = stats.sum[w] / stats.pointsCount[w];
                       if (stats.pointsCount[w] > 1) {
                           double sigmaSquare =
                               (sumOfSquares[w] - stats.sum[w] * stats.mean[w]) /
                               (stats.pointsCount[w] - 1);
                           if (sigmaSquare > 0) stats.stdDev[w] = Math.sqrt(sigmaSquare);
                       }
                   }
               }
               shapeStats.add(stats);
           }
       }
       
       return shapeStats.toArray(new ShapeStats[] {});
    }

    /**
     * Maps from multiple possible user-provided names of shapes (e.g.
     * "::ode::model::Text", "Text", "TextI", "ode.model.TextI",
     * "ode.model.roi.Text", ...) to the definitive database discriminator.
     * 
     * @param string The string to check.
     * @return See above.
     */
    public Object discriminator(String string) {
        if (string == null || string.length() == 0) {
            throw new ApiUsageException("Empty string");
        }
        String[] s = string.split("[.:]");
        string = s[s.length - 1];
        string = string.toLowerCase();
        if (string.endsWith("i")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    //
    // helpers
    //

    private ShapeStats makeStats(int ch) {
        ShapeStats stats = new ShapeStats();
        stats.channelIds = new long[ch];
        stats.min = new double[ch];
        stats.max = new double[ch];
        stats.sum = new double[ch];
        stats.mean = new double[ch];
        stats.stdDev = new double[ch];
        stats.pointsCount = new long[ch];
        Arrays.fill(stats.min, 0, ch, Double.MAX_VALUE);
        return stats;
    }

    private ShapeStats makeStats(Pixels pix, ode.model.roi.Shape shape) {

        // If the shape does not explicitly list any channels, then we will
        // need to take all the available channels from the pixels.

        Integer theC = shape.getTheC();
        int sizeC = pix.sizeOfChannels();
        if (theC != null) {
            sizeC = 1;
        }

        ShapeStats stats = makeStats(sizeC);

        for (int w = 0; w < sizeC; w++) {
            if (theC != null) {
                stats.channelIds[w] = pix.getChannel(theC).getLogicalChannel()
                        .getId();
            } else {
                stats.channelIds[w] = pix.getChannel(w).getLogicalChannel()
                        .getId();
            }

        }

        return stats;
    }

    private SmartShape assertSmart(Shape shape) {
        if (!SmartShape.class.isAssignableFrom(shape.getClass())) {
            throw new RuntimeException(
                    "Internally only SmartShapes should be used! not "
                            + shape.getClass());
        }

        SmartShape ss = (SmartShape) shape;
        return ss;
    }

    private static class ShapeMapper extends IceMapper {

        boolean called = false;

        /**
         * Overrides {@link IceMapper#filter(String, Filterable)} in order to
         * only allow descending one level deep.
         */
        @Override
        public Filterable filter(String fieldId, Filterable source) {

            if (!called) {
                called = true;
                return super.filter(fieldId, source);
            }

            Object o = findTarget(source);
            if (o instanceof ode.model.IObject) {
                IObject iobj = (IObject) source;
                ode.model.IObject robj = (ode.model.IObject) o;
                robj.setId(rlong(iobj.getId()));
                // this seems to me the least unintrusive way to remedy
                // the copying of the affine tranform
                // the other being the .combined files
                // which as is just cast for the transform 
                // (effectively losing info)
                if (robj instanceof AffineTransformI)
                    ((AffineTransformI) robj).copyObject(source, this);
                else robj.unload();
            }

            return source;
        }

    }

}