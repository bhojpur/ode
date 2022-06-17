package ode.security.policy;

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ode.conditions.SecurityViolation;
import ode.model.IObject;
import ode.model.core.Image;
import ode.model.core.OriginalFile;
import ode.model.fs.Fileset;
import ode.model.fs.FilesetEntry;
import ode.model.internal.NamedValue;
import ode.model.meta.ExperimenterGroup;
import ode.model.screen.Plate;
import ode.model.screen.PlateAcquisition;
import ode.model.screen.Well;
import ode.model.screen.WellSample;
import ode.security.ACLVoter;

import org.hibernate.AssertionFailure;
import org.hibernate.Hibernate;

/**
 *  Policy which should be checked anytime access to original binary files in
 *  Bhojpur ODE is being attempted. This check is <em>in addition</em> to the
 *  standard permission permission and is intended to allow customizing who
 *  has access to widely shared data.
 */
public class BinaryAccessPolicy extends BasePolicy {

    /**
     * This string can also be found in the Constants.ice file in the
     * server package.
     */
    public final static String NAME = "RESTRICT-BINARY-ACCESS";

    private final ACLVoter voter;

    private final Set<String> global;

    public BinaryAccessPolicy(Set<Class<IObject>> types, ACLVoter voter) {
        this(types, voter, null);
    }

    public BinaryAccessPolicy(Set<Class<IObject>> types, ACLVoter voter,
            String[] config) {
        super(types);
        this.voter = voter;
        if (config == null) {
            this.global = Collections.emptySet();
        } else {
            this.global = new HashSet<String>(Arrays.asList(config));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRestricted(IObject obj) {
        final Set<String> group= groupRestrictions(obj);

        if (notAorB("+write", "-write", group)) {
            // effectively "None"
            return true;
        } else if (notAorB("+read", "-read", group)) {
            if (!voter.allowUpdate(obj, obj.getDetails())) {
                return true;
            }
        }

        final boolean noImage = notAorB("+image", "-image", group);
        final boolean noPlate = notAorB("+plate", "-plate", group);

        // Possible performance impact!
        if (obj instanceof OriginalFile) {
            OriginalFile ofile = (OriginalFile) obj;

            // Quick short-cut for necessary files
            boolean isTxt = ofile.getName().endsWith(".txt");
            if (isTxt) {
                return false;
            }

            Iterator<FilesetEntry> it = ofile.iterateFilesetEntries();
            while (it.hasNext()) {
                FilesetEntry fe = it.next();
                 if (fe != null && fe.getFileset() != null) {
                    Fileset f = fe.getFileset();
                    if (has(f, Fileset.IMAGES)) {
                        if (noImage) {
                            return true;
                        } else if (noPlate) {
                            Iterator<Image> it2 = f.iterateImages();
                            while (it2.hasNext()) {
                                Image img = it2.next();
                                if (img != null) {
                                    if (has(img, Image.WELLSAMPLES)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (obj instanceof Image) {
            if (noImage) {
                return true;
            }
            // If an Image has a WellSample, then *also* perform the plate check
            // Note: checking noPlate first since it doesn't need to hit the DB.
            if (noPlate) {
                Image img = (Image) obj;
                if (has(img, Image.WELLSAMPLES)) {
                    return true;
                }
            }
        } else if (obj instanceof Plate ||
            obj instanceof PlateAcquisition ||
            obj instanceof Well ||
            obj instanceof WellSample) {

            if (noImage || noPlate) {
                return true;
            }
        }

        return false;
    }

    protected Set<String> groupRestrictions(IObject obj) {
        ExperimenterGroup grp = obj.getDetails().getGroup();
        if (grp != null && grp.getConfig() != null && grp.getConfig().size() > 0) {
            Set<String> rv = null;
            for (NamedValue nv : grp.getConfig()) {
                if ("ode.policy.binary_access".equals(nv.getName())) {
                    if (rv == null) {
                        rv = new HashSet<String>();
                    }
                    String setting = nv.getValue();
                    rv.add(setting);
                }
            }
            if (rv != null) {
                return rv;
            }
        }
        return Collections.emptySet();
    }

    /**
     * Returns true if the minus argument is present in the configuration
     * collections <em>or</em> if the plus argument is not present.
     */
    private final boolean notAorB(String plus, String minus, Collection<String> group) {
        if (global.contains(minus) || group.contains(minus)) {
            return true;
        } else if (global.contains(plus) || group.contains(plus)) {
            return false;
        }
        return true;
    }

    /**
     * Test if the size of the given collection is loadable and more than 0.
     *
     * If an {@link AssertionFailure} is thrown,  we assume that someone else
     * is trying to load the {@link IObject} at the same time. Since the
     * flag will be set on an earlier {@link IObject}, we assume that
     * an actual download won't be attempted. If it is, then the policy will
     * properly load this {@link IObject} and throw a SecurityViolation.
     */
    private boolean has(IObject obj, String field) {
        try {
            Collection<?> c = (Collection<?>) obj.retrieve(field);
            Hibernate.initialize(c);
            if (c != null && !c.isEmpty()) {
                return true;
            }
        } catch (AssertionFailure ae) {
            // pass
        }
        return false;
    }

    @Override
    public void checkRestriction(IObject obj) {
        if (isRestricted(obj)) {
            throw new SecurityViolation(String.format(
                    "Download is restricted for %s",
                    obj));
        }
    }
}