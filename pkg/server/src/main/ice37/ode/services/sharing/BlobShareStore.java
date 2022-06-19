package ode.services.sharing;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ode.api.IShare;
import ode.conditions.OptimisticLockException;
import ode.model.IObject;
import ode.model.acquisition.Detector;
import ode.model.acquisition.DetectorSettings;
import ode.model.acquisition.Dichroic;
import ode.model.acquisition.Filter;
import ode.model.acquisition.FilterSet;
import ode.model.acquisition.Instrument;
import ode.model.acquisition.Laser;
import ode.model.acquisition.LightPath;
import ode.model.acquisition.LightSettings;
import ode.model.acquisition.LightSource;
import ode.model.acquisition.Microscope;
import ode.model.acquisition.Objective;
import ode.model.acquisition.ObjectiveSettings;
import ode.model.acquisition.TransmittanceRange;
import ode.model.core.Channel;
import ode.model.core.Image;
import ode.model.core.LogicalChannel;
import ode.model.core.Pixels;
import ode.model.core.PlaneInfo;
import ode.model.display.ChannelBinding;
import ode.model.display.CodomainMapContext;
import ode.model.display.QuantumDef;
import ode.model.display.RenderingDef;
import ode.model.display.Thumbnail;
import ode.model.meta.Experimenter;
import ode.model.meta.Share;
import ode.model.meta.ShareMember;
import ode.model.stats.StatsInfo;
import ode.services.sharing.data.Obj;
import ode.services.sharing.data.ShareData;
import ode.services.sharing.data.ShareItem;
import ode.system.OdeContext;
import ode.tools.hibernate.QueryBuilder;
import ode.tools.hibernate.SessionFactory;
import ode.util.SqlAction;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Implements {@link ShareStore} and provides functionality to work with binary
 * Ice data from the share. Also provides methods for verification if metadata
 * graph elements are safe to load (part of the security system's ACL vote).
 */
public class BlobShareStore extends ShareStore implements
        ApplicationContextAware {

    /**
     * Used <em>indirectly</em> to obtain sessions for querying and updating the
     * store during normal operation. Due to this classes late initialization,
     * all sessions should be obtained from {@link #session()}.
     */
    protected SessionFactory __dont_use_me_factory;

    protected OdeContext ctx;

    protected SqlAction sqlAction;

    protected Map<Long, Long> pixToImageCache = new HashMap<Long, Long>();

    protected Map<Long, List<Long>> obToImageCache = new HashMap<Long, List<Long>>();

    /**
     * Because there is a cyclic dependency (SF -&gt; ACLVoter -&gt; BlobStore -&gt; SF), we
     * have to lazy-load the session factory via the context.
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.ctx = (OdeContext) applicationContext;
    }

    public void setSqlAction(SqlAction sqlAction) {
        this.sqlAction = sqlAction;
    }

    // Initialization/Destruction

    @Override
    public void doInit() {
        // Currently nothing.
    }

    // Overrides
    // =========================================================================

    @Override
    public Long totalShares() {
        return (Long) session().createQuery("select count(id) from Share")
                .uniqueResult();
    }

    @Override
    public Long totalSharedItems() {
        return (Long) session().createQuery("select sum(itemCount) from Share")
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doSet(Share share, ShareData data, List<ShareItem> items) {
        long oldOptLock = data.optlock;
        long newOptLock = oldOptLock + 1;
        Session session = session();

        List<Share> list = (List<Share>) session.createQuery(
                "select s from Share s where s.id = " + data.id
                        + " and s.version =" + data.optlock).list();

        if (list.size() == 0) {
            throw new OptimisticLockException("Share " + data.id
                    + " has been updated by someone else.");
        }

        data.optlock = newOptLock;
        share.setData(parse(data));
        share.setActive(data.enabled);
        share.setItemCount((long) items.size());
        share.setVersion((int) newOptLock);
        session.merge(share);
        synchronizeMembers(session, data);
    }

    @Override
    public ShareData get(final long id) {
        Session session = session();
        Share s = (Share) session.get(Share.class, id);
        if (s == null) {
            return null;
        }
        byte[] data = s.getData();
        return parse(id, data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ShareData> getShares(long userId, boolean own,
            boolean activeOnly) {
        Session session = session();
        QueryBuilder qb = new QueryBuilder();
        qb.select("share.id");
        qb.from("ShareMember", "sm");
        qb.join("sm.parent", "share", false, false);
        qb.where();
        qb.and("sm.child.id = :userId");
        qb.param("userId", userId);
        if (own) {
            qb.and("share.owner.id = sm.child.id");
        } else {
            qb.and("share.owner.id != sm.child.id");
        }
        if (activeOnly) {
            qb.and("share.active is true");
        }
        Query query = qb.query(session);
        List<Long> shareIds = (List<Long>) query.list();

        if (shareIds.size() == 0) {
            return new ArrayList<ShareData>(); // EARLY EXIT!
        }

        List<ShareData> rv = new ArrayList<ShareData>();
        try {
            Map<Long, byte[]> data = data(shareIds);
            for (Long id : data.keySet()) {
                byte[] bs = data.get(id);
                ShareData d = parse(id, bs);
                rv.add(d);
            }
            return rv;
        } catch (EmptyResultDataAccessException empty) {
            return null;
        }
    }

    boolean imagesContainsPixels(Session s, List<Long> images, Pixels pix,
            Map<Long, Long> cache) {
        Long pixID = pix.getId();
        return imagesContainsPixels(s, images, pixID, cache);
    }

    boolean imagesContainsPixels(Session s, List<Long> images, long pixID,
            Map<Long, Long> cache) {
        Long imgID;
        if (cache.containsKey(pixID)) {
            imgID = cache.get(pixID);
        } else {
            imgID = (Long) s
                    .createQuery("select image.id from Pixels where id = ?")
                    .setParameter(0, pixID).uniqueResult();
            cache.put(pixID, imgID);
        }
        return images.contains(imgID);
    }

    @SuppressWarnings("unchecked")
    boolean imagesContainsInstrument(Session s, List<Long> images,
            Instrument instr, Map<Long, List<Long>> cache) {
        if (instr == null) {
            return false;
        }
        Long instrID = instr.getId();
        List<Long> imgIDs;
        if (cache.containsKey(instrID)) {
            imgIDs = cache.get(instrID);
        } else {
            imgIDs = (List<Long>) s
                    .createQuery("select id from Image where instrument.id = ?")
                    .setParameter(0, instrID).list();
            cache.put(instrID, imgIDs);
        }
        return CollectionUtils.containsAny(images, imgIDs);
    }

    boolean imagesContainsObjectiveSettings(Session s, List<Long> images,
            ObjectiveSettings os, Map<Long, List<Long>> cache) {
        Long osID = os.getId();
        return imagesContainsObjectiveSettings(s, images, osID, cache);
    }

    @SuppressWarnings("unchecked")
    boolean imagesContainsObjectiveSettings(Session s, List<Long> images,
            long osID, Map<Long, List<Long>> cache) {
        List<Long> imgIDs;
        if (cache.containsKey(osID)) {
            imgIDs = cache.get(osID);
        } else {
            imgIDs = (List<Long>) s
                    .createQuery(
                            "select id from Image where objectiveSettings.id = ?")
                    .setParameter(0, osID).list();
            cache.put(osID, imgIDs);
        }
        return CollectionUtils.containsAny(images, imgIDs);
    }

    @Override
    public <T extends IObject> boolean doContains(long sessionId, Class<T> kls,
            long objId) {
        ShareData data = get(sessionId);
        if (data == null) {
            return false;
        }
        return doContains(data, kls, objId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends IObject> boolean doContains(ShareData data,
            Class<T> kls, long objId) {
        List<Long> ids = data.objectMap.get(kls.getName());
        if (ids != null && ids.contains(objId)) {
            return true;
        }

        // Implementing logic similar to the query
        // in DeleteBean in order to allow all objects which link
        // back to an Image to also be loaded.
        /*
         * + "left outer join fetch i.pixels as p " +
         * "left outer join fetch p.channels as c " +
         * "left outer join fetch c.logicalChannel as lc " +
         * "left outer join fetch lc.channels as c2 " +
         * "left outer join fetch c.statsInfo as sinfo " +
         * "left outer join fetch p.planeInfo as pinfo " +
         * "left outer join fetch p.thumbnails as thumb " +
         * "left outer join fetch p.pixelsFileMaps as map " +
         * "left outer join fetch map.parent as ofile " +
         * "left outer join fetch p.settings as setting " // rdef +
         * "left outer join fetch r.waveRendering " +
         * "left outer join fetch r.quantization "
         */

        List<Long> images = data.objectMap.get(Image.class.getName());
        Session s = session();
        if (Pixels.class.isAssignableFrom(kls)) {
            return imagesContainsPixels(s, images, objId, pixToImageCache);
        } else if (RenderingDef.class.isAssignableFrom(kls)) {
            RenderingDef obj = (RenderingDef) s.get(RenderingDef.class, objId);
            return imagesContainsPixels(s, images, obj.getPixels(),
                    pixToImageCache);
        } else if (ChannelBinding.class.isAssignableFrom(kls)) {
            ChannelBinding obj = (ChannelBinding) s.get(ChannelBinding.class,
                    objId);
            return imagesContainsPixels(s, images, obj.getRenderingDef()
                    .getPixels(), pixToImageCache);
        } else if (CodomainMapContext.class.isAssignableFrom(kls)) {
            final CodomainMapContext obj = (CodomainMapContext) s.get(CodomainMapContext.class, objId);
            return imagesContainsPixels(s, images, obj.getChannelBinding().getRenderingDef()
                    .getPixels(), pixToImageCache);
        } else if (Thumbnail.class.isAssignableFrom(kls)) {
            Thumbnail obj = (Thumbnail) s.get(Thumbnail.class, objId);
            return imagesContainsPixels(s, images, obj.getPixels(),
                    pixToImageCache);
        } else if (Channel.class.isAssignableFrom(kls)) {
            Channel obj = (Channel) s.get(Channel.class, objId);
            return imagesContainsPixels(s, images, obj.getPixels(),
                    pixToImageCache);
        } else if (LogicalChannel.class.isAssignableFrom(kls)) {
            LogicalChannel obj = (LogicalChannel) s.get(LogicalChannel.class,
                    objId);
            Iterator<Channel> it = obj.iterateChannels();
            while (it.hasNext()) {
                Channel ch = it.next();
                if (images.contains(ch.getPixels().getImage().getId())) {
                    return true;
                }
            }
        } else if (PlaneInfo.class.isAssignableFrom(kls)) {
            PlaneInfo obj = (PlaneInfo) s.get(PlaneInfo.class, objId);
            return imagesContainsPixels(s, images, obj.getPixels(),
                    pixToImageCache);
        } else if (StatsInfo.class.isAssignableFrom(kls)
                || QuantumDef.class.isAssignableFrom(kls)
                || LightPath.class.isAssignableFrom(kls)
                || Microscope.class.isAssignableFrom(kls)
                || TransmittanceRange.class.isAssignableFrom(kls)) {
            // Objects we just don't care about so let the
            // user load them if they really want to.
            return true;
        }

        if (ObjectiveSettings.class.isAssignableFrom(kls)) {
            ObjectiveSettings obj = (ObjectiveSettings) s.get(
                    ObjectiveSettings.class, objId);
            return imagesContainsObjectiveSettings(s, images, obj,
                    obToImageCache);
        } else if (Objective.class.isAssignableFrom(kls)) {
            Objective obj = (Objective) s.get(Objective.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (Detector.class.isAssignableFrom(kls)) {
            Detector obj = (Detector) s.get(Detector.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (Dichroic.class.isAssignableFrom(kls)) {
            Dichroic obj = (Dichroic) s.get(Dichroic.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (FilterSet.class.isAssignableFrom(kls)) {
            FilterSet obj = (FilterSet) s.get(FilterSet.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (Filter.class.isAssignableFrom(kls)) {
            Filter obj = (Filter) s.get(Filter.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (LightSource.class.isAssignableFrom(kls)) {
            LightSource obj = (LightSource) s.get(LightSource.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (Laser.class.isAssignableFrom(kls)) {
            Laser obj = (Laser) s.get(Laser.class, objId);
            return imagesContainsInstrument(s, images, obj.getInstrument(),
                    obToImageCache);
        } else if (LightSettings.class.isAssignableFrom(kls)) {
            LightSettings obj = (LightSettings) s.get(LightSettings.class,
                    objId);
            return imagesContainsInstrument(s, images, obj.getLightSource()
                    .getInstrument(), obToImageCache);
        } else if (DetectorSettings.class.isAssignableFrom(kls)) {
            DetectorSettings obj = (DetectorSettings) s.get(
                    DetectorSettings.class, objId);
            if (imagesContainsInstrument(s, images, obj.getDetector()
                    .getInstrument(), obToImageCache)) {
                return true;
            } else {
                List<LogicalChannel> lcs = (List<LogicalChannel>) s
                        .createQuery(
                                "select l from LogicalChannel l "
                                        + "where l.detectorSettings.id = "
                                        + obj.getId()).list();
                for (LogicalChannel lc : lcs) {
                    if (doContains(data, LogicalChannel.class, lc.getId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void doClose() {
        // no-op
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Long> keys() {
        Session session = session();
        List<Long> list = (List<Long>) session.createQuery(
                "select id from Share").list();
        return new HashSet<Long>(list);
    }

    // Helpers
    // =========================================================================

    /**
     * Returns a list of data from all shares.
     *
     * @return map of share ID to byte data.
     */
    @SuppressWarnings("unchecked")
    private Map<Long, byte[]> data(List<Long> ids) {
        return sqlAction.getShareData(ids);
    }

    private Session session() {
        return initialize().getSession();
    }

    /**
     * Loads the {@link SessionFactory}. This is the only method which should
     * access the {@link #__dont_use_me_factory} instance variable, since it
     * guarantees loading. Any direct access may well throw a
     * {@link NullPointerException}
     */
    private synchronized SessionFactory initialize() {
        if (__dont_use_me_factory != null) {
            return __dont_use_me_factory; // GOOD!
        }

        if (ctx == null) {
            throw new IllegalStateException("Have no context to load factory");
        }

        __dont_use_me_factory = (SessionFactory) ctx
                .getBean("odeSessionFactory");

        if (__dont_use_me_factory == null) {
            throw new IllegalStateException("Cannot find factory");
        }

        // Finally calling init here, since before it's not possible
        init();
        return __dont_use_me_factory;
    }

    @SuppressWarnings("unchecked")
    private void synchronizeMembers(Session session, ShareData data) {
        Query q = session.createQuery("select sm from ShareMember sm "
                + "where sm.parent = ?");
        q.setLong(0, data.id);
        List<ShareMember> members = (List<ShareMember>) q.list();
        Map<Long, ShareMember> lookup = new HashMap<Long, ShareMember>();
        for (ShareMember sm : members) {
            lookup.put(sm.getChild().getId(), sm);
        }

        Set<Long> intendedUserIds = new HashSet<Long>(data.members);
        intendedUserIds.add(data.owner);

        Set<Long> currentUserIds = lookup.keySet();

        Set<Long> added = new HashSet<Long>(intendedUserIds);
        added.removeAll(currentUserIds);
        for (Long toAdd : added) {
            ShareMember sm = new ShareMember();
            sm.link(new Share(data.id, false), new Experimenter(toAdd, false));
            session.merge(sm);
        }

        Set<Long> removed = new HashSet<Long>(currentUserIds);
        removed.removeAll(intendedUserIds);
        for (Long toRemove : removed) {
            session.delete(lookup.get(toRemove));
        }

    }

    public static void main(String[] args) throws Exception {

        final BlobShareStore store = new BlobShareStore();

        final ShareData template = new ShareData();
        template.enabled = true;
        template.guests = Arrays.asList("a", "b", "c");
        template.id = 100;
        template.members = Arrays.asList(1L, 2L, 3L);
        template.objectList = Arrays.asList(new Obj("type", 200L));
        template.objectMap = new HashMap<String, List<Long>>();
        template.objectMap.put("type", Arrays.asList(100L));
        template.optlock = 12345;
        template.owner = 67890;

        File file = new File(args[0]);
        if (file.exists()) {
            int size = (int) file.length();
            byte[] buf = new byte[size];
            FileInputStream fis = new FileInputStream(file);
            fis.read(buf);
            ShareData data = store.parse(1, buf);
            if (data == null) {
                System.out.println("No share found");
                System.exit(100);
            }
            System.out.println("enabled:" + data.enabled);
            System.out.println("guests:" + data.guests);
            System.out.println("id:" + data.id);
            System.out.println("members:" + data.members);
            System.out.println("objectList:" + data.objectList);
            System.out.println("objectMap:" + data.objectMap);
            System.out.println("optlock:" + data.optlock);
            System.out.println("owner:" + data.owner);
        } else {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte[] buf = store.parse(template);
                fos.write(buf);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.exit(0);

    }
}