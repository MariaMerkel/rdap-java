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

package cc.maria.rdap.bootstrap;

import jakarta.ws.rs.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * RFC 8521-compliant wrapper around the IANA RDAP Bootstrap Service Registry for Entities
 */
public class EntityBootstrapRegistry {
    private HashMap<String, String> tagToServiceMap = new HashMap<>();

    private static HashMap<Client, EntityBootstrapRegistry> instances = new HashMap<>();

    /**
     * Get current instance of the wrapper
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return DomainBootstrapRegistry instance
     */
    public static EntityBootstrapRegistry getInstance (Client client) {
        if (!instances.containsKey(client)) refresh(client);

        return instances.get(client);
    }

    /**
     * Throw away the current instance and get a new one with updated bootstrap data
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return DomainBootstrapRegistry instance
     */
    public static EntityBootstrapRegistry refresh (Client client) {
        try {
            instances.put(client, new EntityBootstrapRegistry(client));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instances.get(client);
    }

    private EntityBootstrapRegistry(Client client) throws IOException {
        this (client, "https://data.iana.org/rdap/object-tags.json");
    }

    private EntityBootstrapRegistry(Client client, String url) {
        JSONObject json = new JSONObject(client.target(url).request().get().readEntity(String.class));
        JSONArray services = json.getJSONArray("services");

        for (Object a : services) {
            JSONArray array = (JSONArray) a;

            for (Object s : array.getJSONArray(1)) {
                tagToServiceMap.put((String) s, ((JSONArray) array.get(2)).getString(0));
            }
        }
    }

    /**
     * Get the RDAP service URL for a given tag
     *
     * @param tag tag to look up
     * @return RDAP service URL
     */
    public String getServiceURL (String tag) {
        return tagToServiceMap.get(tag);
    }

    /**
     * Get the RDAP service URL for a given handle
     *
     * @param handle Handle to look up
     * @return RDAP service URL
     */
    public String getServiceURLForHandle (String handle) {
        String tag = handle.substring(handle.lastIndexOf('-') + 1);
        if (!tagToServiceMap.containsKey(tag)) return null;
        return tagToServiceMap.get(tag);
    }
}
