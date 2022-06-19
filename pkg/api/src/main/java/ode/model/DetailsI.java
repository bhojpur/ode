package ode.model;

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

import java.util.Map;

import Ice.Object;
import ode.system.EventContext;

public class DetailsI extends Details implements ode.model.ModelBased {

    public static final Ice.ObjectFactory makeFactory(final ode.client client) {

        return new Ice.ObjectFactory() {

            public Object create(String arg0) {
                return new DetailsI(client);
            }

            public void destroy() {
                // no-op
            }

        };
    };

    public final static Ice.ObjectFactory Factory = makeFactory(null);

    protected final ode.client client;

    protected final ode.api.ServiceFactoryPrx session;

    public DetailsI() {
        this(null);
    }

    public DetailsI(ode.client client) {
        this.client = client;
        if (client != null) {
            this.session = client.getSession();
        } else {
            this.session = null;
        }
    }

    public ode.client getClient() {
        return this.client;
    }

    public ode.api.ServiceFactoryPrx getSession() {
        return this.session;
    }

    public ode.sys.EventContext getEventContext() {
        return this.event;
    }

    public Map<String, String> getCallContext() {
        return this.call;
    }

    public ode.model.Experimenter getOwner(Ice.Current current) {
        return this.owner;
    }

    public void setOwner(ode.model.Experimenter owner, Ice.Current current) {
        this.owner = owner;

    }

    public ode.model.ExperimenterGroup getGroup(Ice.Current current) {
        return this.group;
    }

    public void setGroup(ode.model.ExperimenterGroup group,
            Ice.Current current) {
        this.group = group;

    }

    public ode.model.Event getCreationEvent(Ice.Current current) {
        return this.creationEvent;
    }

    public void setCreationEvent(ode.model.Event creationEvent,
            Ice.Current current) {
        this.creationEvent = creationEvent;

    }

    public ode.model.Event getUpdateEvent(Ice.Current current) {
        return this.updateEvent;
    }

    public void setUpdateEvent(ode.model.Event updateEvent,
            Ice.Current current) {
        this.updateEvent = updateEvent;

    }

    public ode.model.Permissions getPermissions(Ice.Current current) {
        return this.permissions;
    }

    public void setPermissions(ode.model.Permissions permissions,
            Ice.Current current) {
        this.permissions = permissions;

    }

    public ode.model.ExternalInfo getExternalInfo(Ice.Current current) {
        return this.externalInfo;
    }

    public void setExternalInfo(ode.model.ExternalInfo externalInfo,
            Ice.Current current) {
        this.externalInfo = externalInfo;

    }

    private void ellideEventUuid(Event evt) {
        if (evt != null && evt.session != null) {
            evt.session.uuid = ode.rtypes.rstring("********");
        }

    }

    @SuppressWarnings("unchecked")
    public void copyObject(ode.util.Filterable model,
            ode.util.ModelMapper _mapper) {
        ode.util.IceMapper mapper = (ode.util.IceMapper) _mapper;
        if (model instanceof ode.model.internal.Details) {
            ode.model.internal.Details source = (ode.model.internal.Details) model;
            this.setOwner((ode.model.ExperimenterI) mapper.findTarget(source
                    .getOwner()));
            this.setGroup((ode.model.ExperimenterGroupI) mapper
                    .findTarget(source.getGroup()));
            this.setCreationEvent((ode.model.EventI) mapper.findTarget(source
                    .getCreationEvent()));
            this.setUpdateEvent((ode.model.EventI) mapper.findTarget(source
                    .getUpdateEvent()));

            ellideEventUuid(creationEvent);
            ellideEventUuid(updateEvent);

            this.setExternalInfo((ode.model.ExternalInfoI) mapper
                    .findTarget(source.getExternalInfo()));

            // Since ode.system.EventContext is later in the build
            // graph than ode.model.internal.Details, it's only
            // possible to load it as a java.lang.Object.
            // Note: call context will frequently be null.
            this.event = ode.util.IceMapper.convert(
                    (EventContext) source.contextAt(0));
            if (this.event != null) {
                this.event.sessionUuid = "*********";
            }
            this.call = (Map<String, String>) source.contextAt(1);

            ode.model.internal.Permissions sourceP = source.getPermissions();
            if (sourceP != null) {
                this.setPermissions(new PermissionsI(sourceP));
            }
        } else {
            throw new IllegalArgumentException("Details cannot copy from "
                    + (model == null ? "null" : model.getClass().getName()));
        }
    }

    public ode.util.Filterable fillObject(ode.util.ReverseModelMapper _mapper) {
        ode.util.IceMapper mapper = (ode.util.IceMapper) _mapper;
        ode.model.internal.Details target = ode.model.internal.Details.create(null);
        mapper.store(this, target);
        target.setOwner(
                (ode.model.meta.Experimenter) mapper
                        .reverse((ode.model.ModelBased) this.getOwner()));
        target.setGroup(
                (ode.model.meta.ExperimenterGroup) mapper
                        .reverse((ode.model.ModelBased) this.getGroup()));
        target.setCreationEvent(
                        (ode.model.meta.Event) mapper
                                .reverse((ode.model.ModelBased) this
                                        .getCreationEvent()));
        target.setUpdateEvent(
                (ode.model.meta.Event) mapper
                        .reverse((ode.model.ModelBased) this.getUpdateEvent()));
        target.setExternalInfo(
                        (ode.model.meta.ExternalInfo) mapper
                                .reverse((ode.model.ModelBased) this
                                        .getExternalInfo()));

        Permissions sourceP = this.getPermissions();
        ode.model.internal.Permissions targetP = null;
        if (sourceP != null) {
                targetP = ode.util.Utils.toPermissions(sourceP.getPerm1());
                targetP = new ode.model.internal.Permissions(targetP);
        }
        target.putAt(ode.model.internal.Details.PERMISSIONS, targetP);

        return target;
    }

    public void unload(Ice.Current c) {
        this.setOwner(null);
        this.setGroup(null);
        this.setCreationEvent(null);
        this.setUpdateEvent(null);
        this.setPermissions(null);
        this.setExternalInfo(null);
    }

}