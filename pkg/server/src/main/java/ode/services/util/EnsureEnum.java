package ode.services.util;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ode.model.IEnum;
import ode.model.IGlobal;
import ode.system.Login;
import ode.system.Principal;
import ode.system.Roles;
import ode.system.ServiceFactory;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.ImmutableMap;

/**
 * Utility bean for ensuring that enumeration values do exist in the database.
 */
public class EnsureEnum {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnsureEnum.class);

    private final Executor executor;
    private final Principal principal;
    private final Map<String, String> callContext;
    private final boolean isReadOnlyDb;

    /**
     * Construct a new enumeration ensurer. Expected to be instantiated via Spring.
     * @param executor the internal task executor
     * @param uuid a UUID suitable for constructing a privileged principal
     * @param roles information about the system roles
     * @param readOnly the read-only status
     */
    public EnsureEnum(Executor executor, String uuid, Roles roles, ReadOnlyStatus readOnly) {
        this.executor = executor;
        this.principal = new Principal(uuid, roles.getSystemGroupName(), "Internal");
        this.callContext = ImmutableMap.of(Login.ODE_GROUP, Long.toString(roles.getUserGroupId()));
        isReadOnlyDb = readOnly.isReadOnlyDb();
    }

    /**
     * Ensure that the given enumeration exists.
     * @param session the Hibernate session for accessing the current enumerations
     * @param enumClass the model class of the enumeration
     * @param enumValue the name of the enumeration (case-sensitive)
     * @return the ID of the enumeration, or {@code null} if it did not exist and could not be created
     */
    private static <E extends IEnum & IGlobal> Long ensure(Session session, Class<E> enumClass, String enumValue) {
        IEnum instance = (IEnum) session.createCriteria(enumClass).add(Restrictions.eq("value", enumValue)).uniqueResult();
        if (instance != null) {
            return instance.getId();
        }
        final String prettyEnum = enumClass.getSimpleName() + '.' + enumValue;
        try {
            instance = enumClass.getConstructor(String.class).newInstance(enumValue);
        } catch (IllegalArgumentException | ReflectiveOperationException | SecurityException e) {
            LOGGER.error("failed to create enumeration value " + prettyEnum, e);
            return null;
        }
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            LOGGER.warn("no enumeration value {} but database is read-only", prettyEnum);
            return null;
        } else {
            LOGGER.info("adding to database new enumeration value " + prettyEnum);
            return (Long) session.save(instance);
        }
    }

    /**
     * Ensure that the given enumerations exist.
     * @param enumClass the model class of the enumeration
     * @param enumValues the names of the enumerations (case-sensitive)
     * @return the IDs of the enumerations, with {@code null} for any that did not exist and could not be created
     */
    @SuppressWarnings("unchecked")
    public <E extends IEnum & IGlobal> List<Long> ensure(final Class<E> enumClass, final Collection<String> enumValues) {
        if (enumValues.isEmpty()) {
            return Collections.emptyList();
        }
        return isReadOnlyDb ? (List<Long>) executor.execute(callContext, principal, new FetchEnums<E>() {
            @Override
            public String description() {
                return "check enum values (ro)";
            }

            @Override
            @Transactional(readOnly = true)
            public List<Long> doWork(Session session, ServiceFactory sf) {
                return innerWork(session, enumClass, enumValues);
            }
        })
        : (List<Long>) executor.execute(callContext, principal, new FetchEnums<E>() {
            @Override
            public String description() {
                return "ensure enum values (rw)";
            }

            @Override
            @Transactional(readOnly = false)
            public List<Long> doWork(Session session, ServiceFactory sf) {
                return innerWork(session, enumClass, enumValues);
            }
        });
    }

    /**
     * Base class for workers that ensure that enumeration values exist.
     * Perhaps could be refactored away in Java 8.
     */
    private static abstract class FetchEnums<E extends IEnum & IGlobal> implements Executor.LoggedWork<List<Long>> {

        /**
         * Ensure that the given enumerations exist.
         * @param session the Hibernate session for accessing the current enumerations
         * @param enumClass the model class of the enumeration
         * @param enumValues the names of the enumerations (case-sensitive)
         * @return the IDs of the enumerations, with {@code null} for any that did not exist and could not be created
         */
        protected List<Long> innerWork(Session session, Class<E> enumClass, Collection<String> enumValues) {
            final List<Long> enumIds = new ArrayList<Long>(enumValues.size());
            for (final String enumValue : enumValues) {
                enumIds.add(ensure(session, enumClass, enumValue));
            }
            return enumIds;
        }
    }
}
