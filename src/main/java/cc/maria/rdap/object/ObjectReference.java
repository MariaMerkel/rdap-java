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

import cc.maria.rdap.bootstrap.ASNBootstrapRegistry;
import cc.maria.rdap.bootstrap.DomainBootstrapRegistry;
import cc.maria.rdap.bootstrap.EntityBootstrapRegistry;
import cc.maria.rdap.bootstrap.IPv4BootstrapRegistry;
import cc.maria.rdap.exception.UnknownObjectTypeException;
import cc.maria.rdap.exception.UnknownServiceException;
import jakarta.ws.rs.client.Client;
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
                tempHandle = tempHandle.replace("AS", "");
                tempHandle = tempHandle.replace("ASN", "");
            }
        } catch (UnknownObjectTypeException ignored) {}

        return tempHandle;
    }

    public ObjectType getType () throws UnknownObjectTypeException {
        if (type != null) return type;

        if (handle.startsWith ("AS")) return ObjectType.ASN;
        if (handle.matches("\\d+")) return ObjectType.ASN;

        throw new UnknownObjectTypeException();
    }

    public WebTarget getService () throws UnknownServiceException {
        if (service != null) return service;

        // Method called without HTTP client, so we are unable to bootstrap automatically
        throw new UnknownServiceException();
    }

    public WebTarget getService (Client client) throws UnknownServiceException, UnknownObjectTypeException {
        if (service != null) return service;

        String serviceURL;
        switch (getType()) {
            case ASN:
                serviceURL = ASNBootstrapRegistry.getInstance(client).getServiceURLForASN(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.target(serviceURL);

            case DOMAIN:
                serviceURL = DomainBootstrapRegistry.getInstance(client).getServiceURL(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.target(serviceURL);

            case ENTITY:
                serviceURL = EntityBootstrapRegistry.getInstance(client).getServiceURLForHandle(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.target(serviceURL);

            case IPv4:
                serviceURL = IPv4BootstrapRegistry.getInstance(client).getServiceURLForIP(getHandle());
                if (serviceURL == null) throw new UnknownServiceException();
                return client.target(serviceURL);
        }

        throw new UnknownServiceException();
    }
}
