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
import org.apache.commons.net.util.SubnetUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * RFC 9224-compliant wrapper around the IANA RDAP Bootstrap Service Registry for the IPv4 address space
 */
public class IPv4BootstrapRegistry {
    private HashMap<String, String> subnetsToServiceMap = new HashMap<>();

    private static HashMap<Client, IPv4BootstrapRegistry> instances = new HashMap<>();

    /**
     * Get current instance of the wrapper
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return IPv4BootstrapRegistry instance
     */
    public static IPv4BootstrapRegistry getInstance (Client client) {
        if (!instances.containsKey(client)) refresh(client);

        return instances.get(client);
    }

    /**
     * Throw away the current instance and get a new one with updated bootstrap data
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return IPv4BootstrapRegistry instance
     */
    public static IPv4BootstrapRegistry refresh (Client client) {
        try {
            instances.put(client, new IPv4BootstrapRegistry(client));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instances.get(client);
    }

    private IPv4BootstrapRegistry(Client client) throws IOException {
        this (client, "https://data.iana.org/rdap/ipv4.json");
    }

    private IPv4BootstrapRegistry(Client client, String url) {
        JSONObject json = new JSONObject(client.target(url).request().get().readEntity(String.class));
        JSONArray services = json.getJSONArray("services");

        for (Object a : services) {
            JSONArray array = (JSONArray) a;

            for (Object s : array.getJSONArray(0)) {
                subnetsToServiceMap.put((String) s, ((JSONArray) array.get(1)).getString(0));
            }
        }
    }

    /**
     * Get the RDAP service URL for a given IPv4 address
     *
     * @param ip IP address to look up
     * @return RDAP service URL
     */
    public String getServiceURLForIP (String ip) {
        // If a subnet is provided, we simply strip the prefix length. Provided no subnet to be looked up overlaps multiple allocations to RIRs, this should be fine
        if (ip.contains("/")) ip = ip.split("/")[0];

        for (String subnet : subnetsToServiceMap.keySet()) {
            if (new SubnetUtils(subnet).getInfo().isInRange(ip)) return subnetsToServiceMap.get(subnet);
        }

        return null;
    }
}
