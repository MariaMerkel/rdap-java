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

package cc.maria.rdap;

import cc.maria.rdap.bootstrap.DomainBootstrapRegistry;

import cc.maria.rdap.exception.InvalidObjectTypeException;
import cc.maria.rdap.exception.RDAPException;
import cc.maria.rdap.http.RDAPRequestFilter;
import cc.maria.rdap.object.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

public class RDAPClient {
    private String serviceURL = null;

    private final Client client;

    public RDAPClient () {
        this (ClientBuilder.newClient());
    }

    public RDAPClient (String serviceURL) {
        this ();
        setServiceURL(serviceURL);
    }

    public RDAPClient (Client client) {
        client.register(RDAPRequestFilter.class);
        this.client = client;
    }

    public RDAPClient (String serviceURL, Client client) {
        this (client);
        setServiceURL(serviceURL);
    }

    private void setServiceURL (String serviceURL) {
        if (!serviceURL.endsWith("/")) serviceURL += "/";
        this.serviceURL = serviceURL;
    }

    /**
     * Get the service URL for a given object.
     * If the service URL is not set, the service URL will be looked up from the bootstrap registry.
     * NOTE: RDAP servers can further redirect the client to another service URL. This method returns the first responsible service URL.
     *
     * TODO: Fix
     *
     * @param object The object to be looked up
     * @return The service URL responsible for the object
     */
    public String getServiceURL (String object) {
        if (serviceURL != null) return serviceURL;
        return DomainBootstrapRegistry.getInstance(client).getServiceURLForFQDN(object);
    }

    /**
     * Query the responsible RDAP service for a specified object
     *
     * @param objectReference The object to query for
     * @return The object class returned by the server
     *
     * @throws RDAPException Error in the RDAP protocol
     * @throws JsonProcessingException Error in JSON parsing
     */
    public ObjectClass query (ObjectReference objectReference) throws RDAPException, JsonProcessingException {
        String json = objectReference.getObjectURL(this).request().get().readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        switch (objectReference.getType()) {
            case ASN:
                return mapper.readValue(json, AutnumObjectClass.class);

            case DOMAIN:
                return mapper.readValue(json, DomainObjectClass.class);

            case ENTITY:
                return mapper.readValue(json, EntityObjectClass.class);

            case IPv4:
            case IPv6:
                return mapper.readValue(json, IPNetworkObjectClass.class);
        }

        // This will never happen as objectReference.getType will throw an exception if no type is known
        return null;
    }

    public AutnumObjectClass queryAutnum (ObjectReference objectReference) throws RDAPException, JsonProcessingException {
        if (objectReference.getType() != ObjectType.ASN) throw new InvalidObjectTypeException();

        return (AutnumObjectClass) query(objectReference);
    }

    public DomainObjectClass queryDomain (ObjectReference objectReference) throws RDAPException, JsonProcessingException {
        if (objectReference.getType() != ObjectType.DOMAIN) throw new InvalidObjectTypeException();

        return (DomainObjectClass) query(objectReference);
    }

    public EntityObjectClass queryEntity (ObjectReference objectReference) throws RDAPException, JsonProcessingException {
        if (objectReference.getType() != ObjectType.ENTITY) throw new InvalidObjectTypeException();

        return (EntityObjectClass) query(objectReference);
    }

    public IPNetworkObjectClass queryIPNetwork (ObjectReference objectReference) throws RDAPException, JsonProcessingException {
        if (objectReference.getType() != ObjectType.IPv4 && objectReference.getType() != ObjectType.IPv6) throw new InvalidObjectTypeException();

        return (IPNetworkObjectClass) query(objectReference);
    }

    /**
     * Get custom service URL set during initialization
     *
     * @return Service URL override
     */
    public String getServiceURL() {
        return serviceURL;
    }

    public Client getClient() {
        return client;
    }
}
