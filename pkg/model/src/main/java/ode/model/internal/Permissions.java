package ode.model.internal;

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;

import ode.conditions.ApiUsageException;

/**
 * class responsible for storing all Right/Role-based information for entities
 * as well as various flags for the containing {@link Details} instance. It is
 * strongly encouraged to <em>not</em> base any code on the implementation of
 * the rights, roles, and flag but rather to rely on the public methods.
 * <p>
 * In the future, further roles, rights, and flags may be added to this class.
 * This will change the representation in the database, but the simple
 * grant/revoke/isSet logic will remain the same.
 * </p>
 */
public class Permissions implements Serializable {

    private static final long serialVersionUID = 708953452345658023L;

    /**
     * enumeration of currently active roles. The {@link #USER} role is active
     * when the contents of {@link Details#getOwner()} equals the current user
     * as determined from the Security system (Server-side only). Similarly, the
     * {@link #GROUP} role is active when the contents of
     * {@link Details#getGroup()} matches the current group. {@link #WORLD} is
     * used for any non-USER, non-GROUP user.
     * 
     * For more advanced, ACL, methods taking
     * {@link ode.model.meta.Experimenter} references can be implemented.
     */
    public enum Role {
        USER(8), GROUP(4), WORLD(0);

        private final int shift;

        Role(int shift) {
            this.shift = shift;
        }

        int shift() {
            return this.shift;
        }
    }

    /**
     * enumeration of granted rights. The {@link #READ} right allows for a user
     * with the given role to retrieve an entity. This means that all fields of
     * that entity can be retrieved. Care is taken by the server, that once an
     * entity was readable and another entity was attached to it, that further
     * READ access will not throw an exception. In turn,
     * care should be taken by users to not overly soon grant {@link #READ}
     * permissions lest they no longer be revokable. As of 4.4, this also permits
     * certain view-based linkages of objects (e.g. RenderingDef, Thumbnail).
     *
     * The {@link #ANNOTATE} right allows a user with the given role to link
     * annotations and other non-core data to an entity.
     *
     * The {@link #WRITE} right allows for a user with the given role to alter
     * the fields of an entity, including changing the contents of its
     * collection, assigning it to another collection, or deleting it.
     * This does not include changing the fields of those linked
     * entities, only whether or not they are members of the given collection.
     *
     * Note: if WRITE is granted, ANNOTATE will also be granted.
     */
    public enum Right {

        ANNOTATE(1), WRITE(2), READ(4);

        private final int mask;

        Right(int mask) {
            this.mask = mask;
        }

        int mask() {
            return this.mask;
        }
    }

    /**
     * Currently unused.
     */
    public enum Flag {
        UNUSED(1 << 19);

        /*
         * Implementation note: -------------------- Flags work with reverse
         * logic such that the default permissions can remain -1L (all 1s), a
         * flag is "set" when its bit is set to 0. This holds for everything
         * over 64.
         */
        private final int bit;

        Flag(int bit) {
            this.bit = bit;
        }

        int bit() {
            return this.bit;
        }
    }

    private static final ThreadLocal<BooleanArrayCache> CACHE = new ThreadLocal<BooleanArrayCache>() {
        @Override
        protected BooleanArrayCache initialValue() {
            return new BooleanArrayCache();
        }
    };

    // ~ Constructors
    // =========================================================================
    /**
     * simple constructor. Will turn on all {@link Right rights} for all
     * {@link Role roles}
     */
    public Permissions() {
    }

    /**
     * copy constructor. Will create a new {@link Permissions} with the same
     * {@link Right rights} as the argument.
     */
    public Permissions(Permissions p) {
        if (p == null) {
            throw new IllegalArgumentException(
                    "Permissions argument cannot be null.");
        }
        if (p == DUMMY) {
            throw new IllegalArgumentException("No valid permissions available! DUMMY permissions are not intended for copying. " +
                    "Make sure that you have not passed ode.group=-1 for a save without context");
        }
        this.revokeAll(p);
        this.restrictions = p.restrictions;
        copyRestrictions(p.extendedRestrictions);
    }

    // ~ Fields
    // =========================================================================

    /**
     * represents the lower 64-bits of permissions data.
     */
    private long perm1 = -1; // all bits turned on.

    // These are duplicated in Constants.ice
    public static final int LINKRESTRICTION = 0;
    public static final int EDITRESTRICTION = 1;
    public static final int DELETERESTRICTION = 2;
    public static final int ANNOTATERESTRICTION = 3;
    public static final int CHGRPRESTRICTION = 4;
    public static final int CHOWNRESTRICTION = 5;

    /**
     * Calculated restrictions which are based on both the store
     * representation({@link #perm1}) and the current calling context.
     */
    private boolean[] restrictions;

    /**
     * Further calculated restrictions which can be defined individually by
     * any service. Individual service methods should specify in their
     * documentation which strings must be checked by clients.
     */
    private String[] extendedRestrictions;

    // ~ Getters
    // =========================================================================

    /** tests that a given {@link Role} has the given {@link Right}. */
    public boolean isGranted(Role role, Right right) {
        return (perm1 & right.mask() << role.shift()) == right.mask() << role
                .shift();
    }

    /** tests that a given {@link Flag} is set. */
    public boolean isSet(Flag flag) {
        return (perm1 & flag.bit()) != flag.bit();
    }

    /**
     * returns the order of the bit representing the given {@link Flag}. This
     * is dependent on the internal representation of {@link Permissions} and
     * should only be used when necessary.
     */
    public static int bit(Flag flag) {
        return flag.bit();
    }

    /**
     * returns the order of the bit representing the given {@link Role} and
     * {@link Right}. This is dependent on the internal representation of
     * {@link Permissions} and should only be used when necessary.
     */
    public static int bit(Role role, Right right) {
        return right.mask() << role.shift();
    }

    public static Permissions parseString(String rwrwrw) {

        Permissions p = new Permissions(EMPTY);
        String regex = "([Rr_-][AaWw_-]){3}";

        if (rwrwrw == null || !rwrwrw.matches(regex)) {
            throw new ApiUsageException("Permissions are of the form: " + regex);
        }

        char c;

        c = rwrwrw.charAt(0);
        if (c == 'r' || c == 'R') {
            p.grant(Role.USER, Right.READ);
        }
        c = rwrwrw.charAt(1);
        if (c == 'a' || c == 'A') {
            p.grant(Role.USER, Right.ANNOTATE);
        } else if (c == 'w' || c == 'W') {
            p.grant(Role.USER, Right.ANNOTATE);
            p.grant(Role.USER, Right.WRITE);
        }
        c = rwrwrw.charAt(2);
        if (c == 'r' || c == 'R') {
            p.grant(Role.GROUP, Right.READ);
        }
        c = rwrwrw.charAt(3);
        if (c == 'a' || c == 'A') {
            p.grant(Role.GROUP, Right.ANNOTATE);
        } else if (c == 'w' || c == 'W') {
            p.grant(Role.GROUP, Right.ANNOTATE);
            p.grant(Role.GROUP, Right.WRITE);
        }
        c = rwrwrw.charAt(4);
        if (c == 'r' || c == 'R') {
            p.grant(Role.WORLD, Right.READ);
        }
        c = rwrwrw.charAt(5);
        if (c == 'a' || c == 'A') {
            p.grant(Role.WORLD, Right.ANNOTATE);
        } else if (c == 'w' || c == 'W') {
            p.grant(Role.WORLD, Right.ANNOTATE);
            p.grant(Role.WORLD, Right.WRITE);
        }

        return p;
    }

    public static boolean isDisallow(final boolean[] restrictions, final int restriction) {
        if (restrictions != null && restrictions.length > restriction) {
            return restrictions[restriction];
        }
        return false;
    }

    @Transient
    public boolean isDisallowAnnotate() {
        return isDisallow(restrictions, ANNOTATERESTRICTION);
    }

    @Transient
    public boolean isDisallowChgrp() {
        return isDisallow(restrictions, CHGRPRESTRICTION);
    }

    @Transient
    public boolean isDisallowChown() {
        return isDisallow(restrictions, CHOWNRESTRICTION);
    }

    @Transient
    public boolean isDisallowDelete() {
        return isDisallow(restrictions, DELETERESTRICTION);
    }

    @Transient
    public boolean isDisallowEdit() {
        return isDisallow(restrictions, EDITRESTRICTION);
    }

    @Transient
    public boolean isDisallowLink() {
        return isDisallow(restrictions, LINKRESTRICTION);
    }

    public void addExtendedRestrictions(Set<String> extendedRestrictions) {
        if (extendedRestrictions == null || extendedRestrictions.isEmpty()) {
            return;
        }

        if (this.extendedRestrictions == null) {
            this.extendedRestrictions = extendedRestrictions.toArray(
                    new String[extendedRestrictions.size()]);
        } else {
            // Should be a much less likely case since these will
            // likely not have been loaded/set yet.
            Set<String> copy = new HashSet<String>();
            for (String er : this.extendedRestrictions) {
                copy.add(er);
            }
            copy.addAll(extendedRestrictions);
            this.extendedRestrictions = copy.toArray(
                    new String[copy.size()]);
        }
    }

    @Transient
    public boolean[] getRestrictions() {
        return restrictions;
    }

    /**
     * Produce a copy of restrictions for use elsewhere.
     */
    @Deprecated
    public boolean[] copyRestrictions() {
        if (restrictions == null) {
            return null;
        }
        boolean[] copy = new boolean[restrictions.length];
        System.arraycopy(restrictions, 0, copy, 0, restrictions.length);
        return copy;
    }

    /**
     * Produce a copy of restrictions for use elsewhere.
     */
    public String[] copyExtendedRestrictions() {
        if (extendedRestrictions == null) {
            return null;
        }
        String[] copy = new String[extendedRestrictions.length];
        System.arraycopy(extendedRestrictions, 0,
                copy, 0, extendedRestrictions.length);
        return copy;
    }

    /**
     * Safely copy the source array.
     */
    public void copyRestrictions(String[] extendedRestrictions) {
        if (ArrayUtils.isEmpty(extendedRestrictions)) {
            this.extendedRestrictions = null;
        } else {
            final int sz = extendedRestrictions.length;
            this.extendedRestrictions = new String[sz];
            System.arraycopy(extendedRestrictions, 0, this.extendedRestrictions, 0, sz);
        }
    }

    /**
     * Safely copy the source array. If it is null or contains no "true" values,
     * then the restrictions field will remain null.
     */
    @Deprecated
    public void copyRestrictions(final boolean[] source, String[] extendedRestrictions) {
        copyRestrictions(extendedRestrictions);
        if (noTrues(source)) {
            this.restrictions = null;
        } else {
            if (restrictions == null || source.length != restrictions.length) {
                restrictions = new boolean[source.length];
            }
            System.arraycopy(source, 0, restrictions, 0, source.length);
        }
    }

    /**
     * Copy restrictions based on the integer returned by BasicACLVoter.
     */
    public void copyRestrictions(int allow, Set<String> extendedRestrictions) {

        if (extendedRestrictions == null || extendedRestrictions.isEmpty()) {
            this.extendedRestrictions = null;
        } else {
            this.extendedRestrictions = extendedRestrictions.toArray(
                    new String[extendedRestrictions.size()]);
        }

        if (allow == 63) { // Would be all false.
            this.restrictions = null;
            return;
        }

        this.restrictions = new boolean[6]; // All false
        this.restrictions[LINKRESTRICTION] |= (0 == (allow & (1 << LINKRESTRICTION)));
        this.restrictions[EDITRESTRICTION] |= (0 == (allow & (1 << EDITRESTRICTION)));
        this.restrictions[DELETERESTRICTION] |= (0 == (allow & (1 << DELETERESTRICTION)));
        this.restrictions[ANNOTATERESTRICTION] |= (0 == (allow & (1 << ANNOTATERESTRICTION)));
        this.restrictions[CHGRPRESTRICTION] |= (0 == (allow & (1 << CHGRPRESTRICTION)));
        this.restrictions[CHOWNRESTRICTION] |= (0 == (allow & (1 << CHOWNRESTRICTION)));
        restrictions = CACHE.get().getArrayFor(restrictions);
    }

    private static boolean noTrues(boolean[] source) {
        if (source == null) {
            return true;
        }
        for (int i = 0; i < source.length; i++) {
            if (source[i]) {
                return false;
            }
        }
        return true;
    }

    // ~ Setters (return this)
    // =========================================================================

    /**
     * turns on the {@link Right rights} for the given {@link Role role}. Null
     * or empty rights are simply ignored. For example, <code>
     *   sodePermissions().grant(USER,READ,WRITE,USE);
     * </code> will guarantee
     * that the current user has all rights on this entity.
     */
    public Permissions grant(Role role, Right... rights) {
        if (rights != null && rights.length > 0) {
            for (Right right : rights) {
                perm1 = perm1 | singleBitOn(role, right);
            }
        }
        return this;
    }

    /**
     * turns off the {@link Right rights} for the given {@link Role role}. Null
     * or empty rights are simply ignored. For example, <code>
     *   new Permissions().revoke(WORLD,WRITE,USE);
     * </code> will return a
     * Permissions instance which cannot be altered or linked to by members of
     * WORLD.
     */
    public Permissions revoke(Role role, Right... rights) {
        if (rights != null && rights.length > 0) {
            for (Right right : rights) {
                perm1 = perm1 & singleBitOut(role, right);
            }
        }
        return this;
    }

    /**
     * takes a permissions instance and ORs it with the current instance. This
     * means that any privileges which have been granted to the argument will
     * also be granted to the current instance. For example, <code>
     *   Permissions mask = new Permissions().grant(WORLD,READ);
     *   someEntity.getDetails().getPermissions().grantAllk(mask);
     * </code> will allow READ access (and possibly more) to
     * <code>someEntity</code> for members of WORLD.
     */
    public Permissions grantAll(Permissions mask) {
        if (mask == null) {
            return this;
        }
        long maskPerm1 = mask.getPerm1();
        this.perm1 = this.perm1 | maskPerm1;
        return this;
    }

    /**
     * takes a permissions instance and ANDs it with the current instance. This
     * means that any privileges which have been revoked from the argument will
     * also be revoked from the current instance. For example, <code>
     *   Permissions mask = new Permissions().revoke(WORLD,READ,WRITE,USE);
     *   someEntity.getDetails().getPermissions().applyMask(mask);
     * </code> will disallow all access to <code>someEntity</code> for members
     * of WORLD.
     * 
     * This also implies that applyMask can be used to make copies of
     * Permissions. For example, <code>
     *   new Permissions().applyMask( somePermissions );
     * </code> will produce a copy of
     * <code>somePermissions</code>.
     * 
     * Note: the logic here is different from Unix UMASKS.
     */
    public Permissions revokeAll(Permissions mask) {
        if (mask == null) {
            return this;
        }
        long maskPerm1 = mask.getPerm1();
        this.perm1 = this.perm1 & maskPerm1;
        return this;
    }

    /** turn a given {@link Flag} on. A null {@link Flag} will be ignored. */
    public Permissions set(Flag flag) {
        if (flag == null) {
            return this;
        }
        this.perm1 &= -1L ^ flag.bit();
        return this;
    }

    /** turn a given {@link Flag} off. A null {@link Flag} will be ignored. */
    public Permissions unSet(Flag flag) {
        if (flag == null) {
            return this;
        }
        this.perm1 |= 0L ^ flag.bit();
        return this;
    }

    @Deprecated
    public static void setDisallow(boolean[] restrictions,
            int restriction, boolean disallow) {

        // The array is already long enough, just set the value
        if (restrictions != null && restrictions.length >= restriction) {
            restrictions[restriction] = disallow;
        } else {
            // if !disallow, then we won't need to set anything,
            // since the default value will be false.
            if (disallow) {
                boolean[] copy = new boolean[restriction+1];
                if (restrictions != null) {
                    System.arraycopy(restrictions, 0, copy, 0, restrictions.length);
                }
                copy[restriction] = disallow; // i.e. true
                restrictions = copy;
            }
        }
    }

    public void setDisallow(final int restriction, boolean disallow) {
        if (disallow) {
            /* must set the bit */
            if (restrictions == null || restrictions.length <= restriction) {
                /* the bit is clear implicitly */
                restrictions = Arrays.copyOf(restrictions, restriction + 1);
                restrictions[restriction] = true;
                restrictions = CACHE.get().getArrayFor(restrictions);
            } else if (!restrictions[restriction]) {
                /* the bit is clear explicitly */
                restrictions = CACHE.get().transform(new BooleanArrayCache.Transformer() {
                    @Override
                    public boolean[] transform(boolean[] restrictions) {
                        restrictions[restriction] = true;
                        return restrictions;
                    }
                }, restrictions);
            }
        } else {
            /* must clear the bit */
            if (restrictions != null && restrictions.length > restriction && restrictions[restriction]) {
                /* the bit is set */
                restrictions = CACHE.get().transform(new BooleanArrayCache.Transformer() {
                    @Override
                    public boolean[] transform(boolean[] restrictions) {
                        restrictions[restriction] = false;
                        return restrictions;
                    }
                }, restrictions);
            }
        }
    }

    public Permissions setDisallowAnnotate(boolean disallowAnnotate) {
        setDisallow(ANNOTATERESTRICTION, disallowAnnotate);
        return this;
    }

    public Permissions setDisallowChgrp(boolean disallowChgrp) {
        setDisallow(CHGRPRESTRICTION, disallowChgrp);
        return this;
    }

    public Permissions setDisallowChown(boolean disallowChown) {
        setDisallow(CHOWNRESTRICTION, disallowChown);
        return this;
    }

    public Permissions setDisallowDelete(boolean disallowDelete) {
        setDisallow(DELETERESTRICTION, disallowDelete);
        return this;
    }

    public Permissions setDisallowEdit(boolean disallowEdit) {
        setDisallow(EDITRESTRICTION, disallowEdit);
        return this;
    }

    public Permissions setDisallowLink(boolean disallowLink) {
        setDisallow(LINKRESTRICTION, disallowLink);
        return this;
    }

    // ~ Overrides
    // =========================================================================

    /**
     * produces a String representation of the {@link Permissions} similar to
     * those on a Unix filesystem. Unset bits are represented by a dash, while
     * other bits are represented by a symbolic value in the correct bit
     * position. For example, a Permissions with all {@link Right rights}
     * granted to all but WORLD {@link Role roles} would look like: rwrw--
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(isGranted(Role.USER, Right.READ) ? "r" : "-");
        sb.append(annotateOrWorld(Role.USER));
        sb.append(isGranted(Role.GROUP, Right.READ) ? "r" : "-");
        sb.append(annotateOrWorld(Role.GROUP));
        sb.append(isGranted(Role.WORLD, Right.READ) ? "r" : "-");
        sb.append(annotateOrWorld(Role.WORLD));
        return sb.toString();
    }

    private String annotateOrWorld(Role role) {
        if (isGranted(role, Right.WRITE)) {
            return "w";
        } else if (isGranted(role, Right.ANNOTATE)) {
            return "a";
        } else {
            return "-";
        }
    }

    /**
     * returns true if two {@link Permissions} instances have all the same
     * {@link Right} / {@link Role} pairs granted.
     */
    public boolean sameRights(Permissions p) {
        if (p == this) {
            return true;
        }

        for (Role ro : Role.values()) {
            for (Right rt : Right.values()) {
                if (isGranted(ro, rt) != p.isGranted(ro, rt)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * two {@link Permissions} instances are <code>identical</code> if they have
     * the same bit representation.
     */
    // @Override
    public boolean identical(Permissions p) {
        // if (!(obj instanceof Permissions)) return false;
        //    	
        // Permissions p = (Permissions) obj;

        if (p == this) {
            return true;
        }

        if (p.perm1 == this.perm1) {
            return true;
        }

        return false;

    }

    // /** hashCode based on the bit representation of this {@link Permissions}
    // * instance.
    // */
    // @Override
    // public int hashCode() {
    // int result = 11;
    // result = 17 * result + (int)(perm1^(perm1>>>32));
    // return result;
    // }

    // ~ Property accessors : used primarily by Hibernate
    // =========================================================================

    @Column(name = "permissions", nullable = false, updatable = false)
    protected long getPerm1() {
        return this.perm1;
    }

    protected void setPerm1(long value) {
        this.perm1 = value;
    }

    // ~ Helpers
    // =========================================================================

    /** returns a long with only a single 0 defined by role/right */
    final protected static long singleBitOut(Role role, Right right) {
        return -1L ^ right.mask() << role.shift();
    }

    /** returns a long with only a single 1 defined by role/right */
    final protected static long singleBitOn(Role role, Right right) {
        return 0L | right.mask() << role.shift();
    }

    /**
     * an immutable wrapper around {@link Permission} instances so that commonly
     * used permissions can be made available as public final static constants.
     */
    private static class ImmutablePermissions extends Permissions implements
            Serializable {

        private static final long serialVersionUID = -4407900270934589522L;

        /**
         * Factory method to create an immutable Permissions object.
         */
        public static Permissions immutable(Permissions p) {
            return new ImmutablePermissions(p);
        }

        /**
         * the delegate {@link Permissions} which this immutable wrapper bases
         * all of its logic on. Not final for reasons of serialization.
         */
        private Permissions delegate;

        /**
         * the sole constructor for an {@link ImmutablePermissions}. Note: this
         * does not behave like {@link Permissions#Permissions(Permissions)} --
         * the copy constructor. Rather stores the {@link Permissions} instance
         * for delegation
         * 
         * @param p
         *            Non-null {@link Permissions} instance.
         */
        ImmutablePermissions(Permissions p) {
            if (p == null) {
                throw new IllegalArgumentException(
                        "Permissions may not be null");
            }

            this.delegate = new Permissions(p);
        }

        // ~ SETTERS
        // =====================================================================
        /**
         * throws {@link UnsupportedOperationException}
         */
        @Override
        public Permissions grant(Role role, Right... rights) {
            throw new UnsupportedOperationException();
        }

        /**
         * throws {@link UnsupportedOperationException}
         */
        @Override
        public Permissions revoke(Role role, Right... rights) {
            throw new UnsupportedOperationException();
        }

        /**
         * throws {@link UnsupportedOperationException}
         */
        @Override
        public Permissions grantAll(Permissions mask) {
            throw new UnsupportedOperationException();
        }

        /**
         * throws {@link UnsupportedOperationException}
         */
        @Override
        public Permissions revokeAll(Permissions mask) {
            throw new UnsupportedOperationException();
        }

        /**
         * delegates to {@link #set(ode.model.internal.Permissions.Flag)}
         */
        @Override
        public Permissions set(Flag flag) {
            return delegate.set(flag);
        }

        /**
         * delegates to {@link #unSet(ode.model.internal.Permissions.Flag)}
         */
        @Override
        public Permissions unSet(Flag flag) {
            return delegate.unSet(flag);
        }

        // ~ GETTERS
        // =========================================================================

        /**
         * delegates to {@link #delegate}
         */
        @Override
        public boolean isGranted(Role role, Right right) {
            return delegate.isGranted(role, right);
        }

        /**
         * delegates to {@link #delegate}
         */
        @Override
        protected long getPerm1() {
            return delegate.getPerm1();
        }

        /**
         * delegates to {@link #delegate}
         */
        @Override
        protected void setPerm1(long value) {
            delegate.setPerm1(value);
        }

        /**
         * delegates to {@link #isSet(ode.model.internal.Permissions.Flag)}
         */
        @Override
        public boolean isSet(Flag flag) {
            return delegate.isSet(flag);
        }

        // ~ Other
        // =====================================================================

        /**
         * delegates to {@link #identical(Permissions)}
         */
        @Override
        public boolean identical(Permissions p) {
            return delegate.identical(p);
        }

        /**
         * delegates to {@link #sameRights(Permissions)}
         */
        @Override
        public boolean sameRights(Permissions p) {
            return delegate.sameRights(p);
        }

        /**
         * delegates to {@link #toString()}
         */
        @Override
        public String toString() {
            return delegate.toString();
        }

        // ~ Serialization
        // =====================================================================

        private void readObject(ObjectInputStream s) throws IOException,
                ClassNotFoundException {
            Permissions p = (Permissions) s.readObject();
            if (p == null) {
                throw new IllegalArgumentException(
                        "Permissions may not be null");
            }

            this.delegate = new Permissions(p);
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            s.writeObject(delegate);
        }

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // NOTE: when rights or roles change, the definition of EMPTY needs to
    // be kept in sync.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * an immutable {@link Permissions} instance with all {@link Right rights}
     * turned off.
     */
    public final static Permissions EMPTY;

    static {
            final Permissions permissions = new Permissions();
            for (final Role role : Role.values()) {
                permissions.revoke(role, Right.values());
            }
            EMPTY = new ImmutablePermissions(permissions);
    }

    /**
     * Marker object which can be set on objects to show that the Permissions
     * instance given contains no value.
     */
    public final static Permissions DUMMY = new ImmutablePermissions(EMPTY);

    // ~ Systematic
    // =========================================================================
    /*
     * All possible (sensible) permission combinations are:
     * 
     * R_____ user immutable RW____ user private RWR___ group readable RWRW__
     * group private RWRWR_ group writeable RWRWRW world writeable RWR_R_ user
     * writeable R_R_R_ world immutable R_R___ group immutable
     */

    /**
     * R______ : user and only the user can only read
     */
    public final static Permissions USER_IMMUTABLE = new ImmutablePermissions(
            new Permissions(EMPTY).grant(Role.USER, Right.READ));

    /**
     * RW____ : user and only user can read and write
     */
    public final static Permissions USER_PRIVATE = new ImmutablePermissions(
            new Permissions(EMPTY).grant(Role.USER, Right.READ, Right.ANNOTATE, Right.WRITE));

    /**
     * RWR___ : user can read and write, group can read
     */
    public final static Permissions GROUP_READABLE = new ImmutablePermissions(
            new Permissions(USER_PRIVATE).grant(Role.GROUP, Right.READ));

    /**
     * RWRW__ : user and group can read and write
     */
    public final static Permissions GROUP_PRIVATE = new ImmutablePermissions(
            new Permissions(GROUP_READABLE).grant(Role.GROUP, Right.ANNOTATE, Right.WRITE));

    /**
     * RWRWR_ : user and group can read and write, world can read
     */

    public final static Permissions GROUP_WRITEABLE = new ImmutablePermissions(
            new Permissions(GROUP_PRIVATE).grant(Role.WORLD, Right.READ));

    /**
     * RWRWRW : everyone can read and write
     */
    public final static Permissions WORLD_WRITEABLE = new ImmutablePermissions(
            new Permissions(GROUP_WRITEABLE).grant(Role.WORLD, Right.ANNOTATE, Right.WRITE));

    /**
     * RWR_R_ : all can read, user can write
     */
    public final static Permissions USER_WRITEABLE = new ImmutablePermissions(
            new Permissions(GROUP_READABLE).grant(Role.WORLD, Right.READ));

    /**
     * R_R_R_ : all can only read
     */
    public final static Permissions WORLD_IMMUTABLE = new ImmutablePermissions(
            new Permissions(USER_WRITEABLE).revoke(Role.USER, Right.ANNOTATE, Right.WRITE));

    /**
     * R_R___ : user and group can only read
     */
    public final static Permissions GROUP_IMMUTABLE = new ImmutablePermissions(
            new Permissions(WORLD_IMMUTABLE).revoke(Role.WORLD, Right.READ));

    // ~ Non-systematic (easy to remember)
    // =========================================================================

    /**
     * an immutable {@link Permissions} instance with all {@link Right#WRITE}
     * rights turned off. Identical to {@link #WORLD_IMMUTABLE}
     */
    public final static Permissions READ_ONLY = WORLD_IMMUTABLE;

    /**
     * an immutable {@link Permissions} instance with permissions only for the
     * object owner.. Identical to {@link #USER_PRIVATE}.
     */
    public final static Permissions PRIVATE = USER_PRIVATE;

    /**
     * an immutable {@link Permissions} instance with permissions for group
     * members to read other members' data. Identical to
     * {@link #GROUP_READABLE}.
     */
    public final static Permissions COLLAB_READONLY = GROUP_READABLE;

    /**
     * an immutable {@link Permissions} instance with read and write permissions
     * for group members. Identical to {@link #GROUP_PRIVATE}.
     */
    public final static Permissions COLLAB_READLINK = GROUP_PRIVATE;

    /**
     * an immutable {@link Permissions} instance with all {@link Right Rights}
     * granted. Identical to {@link #WORLD_WRITEABLE}
     */
    public final static Permissions PUBLIC = WORLD_WRITEABLE;

}