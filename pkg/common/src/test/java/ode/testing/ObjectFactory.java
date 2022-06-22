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

import ode.model.core.Channel;
import ode.model.core.Image;
import ode.model.core.LogicalChannel;
import ode.model.core.OriginalFile;
import ode.model.core.Pixels;
import ode.model.core.PlaneInfo;
import ode.model.display.ChannelBinding;
import ode.model.display.PlaneSlicingContext;
import ode.model.display.QuantumDef;
import ode.model.display.RenderingDef;
import ode.model.display.Thumbnail;
import ode.model.enums.AcquisitionMode;
import ode.model.enums.DimensionOrder;
import ode.model.enums.Family;
import ode.model.enums.PhotometricInterpretation;
import ode.model.enums.PixelsType;
import ode.model.enums.RenderingModel;
import ode.model.enums.UnitsLength;
import ode.model.enums.UnitsTime;
import ode.model.stats.StatsInfo;
import ode.model.units.Length;
import ode.model.units.Time;

/**
 * these method serve as a both client and test data store. An object that has
 * no id is "new"; an object with an id is detached and can represent something
 * serialized from IQuery.
 *
 * NOTE: this is a bit dangerous, causing model builds to fail sometimes. where
 * else could it live?
 */
public class ObjectFactory {

    public static OriginalFile createFile() {
        OriginalFile ofile = new OriginalFile();
        ofile.setName("testing");
        ofile.setPath("/dev/null");
        ofile.setHash("abc");
        ofile.setSize(1L);
        ofile.setMimetype("text/plain");

        return ofile;
    }

    public static Thumbnail createThumbnails(Pixels p) {
        Thumbnail t = new Thumbnail();
        t.setMimeType("txt");
        t.setSizeX(1);
        t.setSizeY(1);
        p.addThumbnail(t);
        return t;
    }

    public static Pixels createPixelGraph(Pixels example) {
        return createPixelGraphWithChannels(example, 1);
    }

    public static Channel createChannel(Channel example) {
        Channel ch = new Channel();
        LogicalChannel lc = new LogicalChannel();
        StatsInfo si = new StatsInfo();
        PhotometricInterpretation pi = new PhotometricInterpretation("RGB");
        lc.setPhotometricInterpretation(pi);
        si.setGlobalMax(0.0);
        si.setGlobalMin(0.0);
        ch.setStatsInfo(si);
        ch.setLogicalChannel(lc);
        return ch;
    }

    public static Pixels createPixelGraphWithChannels(Pixels example, int channelCount) {

        Pixels p = new Pixels();
        PhotometricInterpretation pi = new PhotometricInterpretation();
        AcquisitionMode mode = new AcquisitionMode();
        PixelsType pt = new PixelsType();
        DimensionOrder dO = new DimensionOrder();
        Image i = new Image();
        Channel[] c = new Channel[channelCount];
        LogicalChannel[] lc = new LogicalChannel[channelCount];
        StatsInfo[] si = new StatsInfo[channelCount];
        PlaneInfo[] pl = new PlaneInfo[channelCount];
        for (int w = 0; w < channelCount; w++) {
            c[w] = new Channel();
            lc[w] = new LogicalChannel();
            si[w] = new StatsInfo();
            pl[w] = new PlaneInfo();
        }

        if (example != null) {
            p.setId(example.getId());
            p.setVersion(example.getVersion());

            // everything else unloaded.
            pt.setId(example.getPixelsType().getId());
            pt.unload();
            dO.setId(example.getDimensionOrder().getId());
            dO.unload();
            i.setId(example.getImage().getId());
            i.unload();
            for (int w = 0; w < channelCount; w++) {
                c[w].setId(example.getChannel(w).getId());
                c[w].unload();
            }
            // Not needed but useful
            p.addPlaneInfo(example.iteratePlaneInfo().next());
            (p.iteratePlaneInfo().next()).unload();
        }

        else {

            mode.setValue("Wide-field");
            pi.setValue("RGB");

            pt.setValue("int8");
            pt.setBitSize(8);

            dO.setValue("XYZTC");

            for (int w = 0; w < channelCount; w++) {
                c[w].setPixels(p);
                lc[w].setPhotometricInterpretation(pi);

                // Not required but useful
                si[w].setGlobalMax(new Double(0.0));
                si[w].setGlobalMin(new Double(0.0));
                c[w].setLogicalChannel(lc[w]);
                c[w].setStatsInfo(si[w]);
                pl[w].setTheC(new Integer(w));
                pl[w].setTheZ(new Integer(0));
                pl[w].setTheT(new Integer(0));

                Time deltaT = new Time(0.0, UnitsTime.SECOND);
                pl[w].setDeltaT(deltaT);
                p.addPlaneInfo(pl[w]);

            }

            i.setName("test");
            i.addPixels(p);

        }

        Length mm1 = new Length(1.0, UnitsLength.MILLIMETER);
        p.setSizeX(new Integer(1));
        p.setSizeY(new Integer(1));
        p.setSizeZ(new Integer(1));
        p.setSizeC(new Integer(1));
        p.setSizeT(new Integer(1));
        p.setPhysicalSizeX(mm1);
        p.setPhysicalSizeY(mm1);
        p.setPhysicalSizeZ(mm1);
        p.setSha1("09bc7b2dcc9a510f4ab3a40c47f7a4cb77954356"); // "pixels"
        p.setPixelsType(pt);
        p.setDimensionOrder(dO);
        p.setImage(i);

        for (int w = 0; w < channelCount; w++) {
            p.addChannel(c[w]);
        }

        return p;
    }

    public static ChannelBinding createChannelBinding() {
        // Prereqs for binding

        Family family = new Family();
        family.setValue("linear");

        ChannelBinding binding = new ChannelBinding();
        binding.setActive(Boolean.FALSE);
        binding.setCoefficient(new Double(1));
        binding.setAlpha(new Integer(1));
        binding.setBlue(new Integer(1));
        binding.setGreen(new Integer(1));
        binding.setRed(new Integer(1));
        binding.setFamily(family);
        binding.setInputEnd(new Double(1.0));
        binding.setInputStart(new Double(1.0));
        binding.setNoiseReduction(Boolean.FALSE);

        return binding;
    }

    public static RenderingDef createRenderingDef() {
        // Prereqs for RenderingDef
        RenderingModel model = new RenderingModel();
        model.setValue("rgb");

        QuantumDef qdef = new QuantumDef();
        qdef.setBitResolution(new Integer(1));
        qdef.setCdEnd(new Integer(1));
        qdef.setCdStart(new Integer(1));

        RenderingDef def = new RenderingDef();
        def.setDefaultT(new Integer(1));
        def.setDefaultZ(new Integer(1));
        def.setModel(model);
        def.setPixels(ObjectFactory.createPixelGraph(null));
        def.setQuantization(qdef);

        return def;
    }

    public static PlaneSlicingContext createPlaneSlicingContext() {
        PlaneSlicingContext enhancement = new PlaneSlicingContext();
        enhancement.setConstant(Boolean.FALSE);
        enhancement.setLowerLimit(new Integer(1));
        enhancement.setPlanePrevious(new Integer(1));
        enhancement.setPlaneSelected(new Integer(1));
        enhancement.setUpperLimit(new Integer(1));

        return enhancement;

    }
}