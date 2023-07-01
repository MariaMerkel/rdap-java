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

import cc.maria.rdap.http.RDAPRequestFilter;
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

    public void sendTestRequest () {
        getWebTarget("example.com").path("domain/example.com").request().get();
    }

    /**
     * Get the service URL for a given object.
     * If the service URL is not set, the service URL will be looked up from the bootstrap registry.
     * NOTE: RDAP servers can further redirect the client to another service URL. This method returns the first responsible service URL.
     *
     * @param object The object to be looked up
     * @return The service URL responsible for the object
     */
    public String getServiceURL (String object) {
        if (serviceURL != null) return serviceURL;
        return DomainBootstrapRegistry.getInstance().getServiceURLForFQDN(object);
    }

    public WebTarget getWebTarget (String object) {
        return client.target(getServiceURL(object));
    }
}
