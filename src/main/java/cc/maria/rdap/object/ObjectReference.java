/*
 * Copyright (c) 2023 Maria Merkel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */

package cc.maria.rdap.object;

import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.bootstrap.*;
import cc.maria.rdap.exception.UnknownObjectTypeException;
import cc.maria.rdap.exception.UnknownServiceException;
import inet.ipaddr.IPAddressString;
import jakarta.ws.rs.client.WebTarget;

public class ObjectReference {
    private final String handle;
    private final WebTarget service;
    private final ObjectType type;

    public ObjectReference (String handle) {
        this (handle, null, null);
    }

    public ObjectReference (String handle, WebTarget service) {
        this (handle, service, null);
    }

    public ObjectReference (String handle, ObjectType type) {
        this (handle, null, type);
    }

    public ObjectReference (String handle, WebTarget service, ObjectType type) {
        this.handle = handle;
        this.service = service;
        this.type = type;
    }

    public String getHandle () {
        String tempHandle = handle;

        try {
            if (getType() == ObjectType.ASN) {
                tempHandle = tempHandle.replace("ASN", "");
                tempHandle = tempHandle.replace("AS", "");
            }
        } catch (UnknownObjectTypeException ignored) {}

        return tempHandle;
    }

    public ObjectType getType () throws UnknownObjectTypeException {
        if (type != null) return type;

        if (handle.matches ("AS\\d+")) return ObjectType.ASN;
        if (handle.matches ("ASN\\d+")) return ObjectType.ASN;
        if (handle.matches("\\d+")) return ObjectType.ASN;
        if (handle.matches("(\\d{1,3}\\.){3}\\d{1,3}(/\\d{1,2})?")) return ObjectType.IPv4;
        if (new IPAddressString(handle).isIPv6()) return ObjectType.IPv6;

        throw new UnknownObjectTypeException();
    }

    public WebTarget getService () throws UnknownServiceException {
        if (service != null) return service;

        // Method called without HTTP client, so we are unable to bootstrap automatically
        throw new UnknownServiceException();
    }

    public WebTarget getService (RDAPClient client) throws UnknownServiceException, UnknownObjectTypeException {
        if (service != null) return service;

        if (client.getServiceURL() != null) return client.getClient().target(client.getServiceURL());

        String serviceURL;
        switch (getType()) {
            case ASN:
                serviceURL = ASNBootstrapRegistry.getInstance(client.getClient()).getServiceURLForASN(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.getClient().target(serviceURL);

            case DOMAIN:
                serviceURL = DomainBootstrapRegistry.getInstance(client.getClient()).getServiceURLForFQDN(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.getClient().target(serviceURL);

            case ENTITY:
                serviceURL = EntityBootstrapRegistry.getInstance(client.getClient()).getServiceURLForHandle(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.getClient().target(serviceURL);

            case IPv4:
                serviceURL = IPv4BootstrapRegistry.getInstance(client.getClient()).getServiceURLForIP(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.getClient().target(serviceURL);

            case IPv6:
                serviceURL = IPv6BootstrapRegistry.getInstance(client.getClient()).getServiceURLForIP(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.getClient().target(serviceURL);
        }

        throw new UnknownServiceException();
    }

    public WebTarget getObjectURL (WebTarget service) throws UnknownObjectTypeException {
        switch (getType()) {
            case ASN:
                return service.path("autnum/" + getHandle());

            case DOMAIN:
                return service.path("domain/" + getHandle());

            case ENTITY:
                return service.path("entity/" + getHandle());

            case IPv4:
            case IPv6:
                return service.path("ip/" + getHandle());
        }

        // This will never happen
        throw new UnknownObjectTypeException();
    }

    public WebTarget getObjectURL (RDAPClient client) throws UnknownObjectTypeException, UnknownServiceException {
        return getObjectURL(getService(client));
    }

    public WebTarget getObjectURL () throws UnknownServiceException, UnknownObjectTypeException {
        return getObjectURL(getService());
    }
}
