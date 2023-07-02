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
 * RFC 9224-compliant wrapper around the IANA RDAP Bootstrap Service Registry for ASN allocations
 */
public class ASNBootstrapRegistry {
    private HashMap<String, String> asnRangeToServiceMap = new HashMap<>();

    private static HashMap<Client, ASNBootstrapRegistry> instances = new HashMap<>();

    /**
     * Get current instance of the wrapper
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return ASNBootstrapRegistry instance
     */
    public static ASNBootstrapRegistry getInstance (Client client) {
        if (!instances.containsKey(client)) refresh(client);

        return instances.get(client);
    }

    /**
     * Throw away the current instance and get a new one with updated bootstrap data
     *
     * @param client HTTP client for fetching bootstrap data
     *
     * @return ASNBootstrapRegistry instance
     */
    public static ASNBootstrapRegistry refresh (Client client) {
        try {
            instances.put(client, new ASNBootstrapRegistry(client));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instances.get(client);
    }

    private ASNBootstrapRegistry(Client client) throws IOException {
        this (client, "https://data.iana.org/rdap/asn.json");
    }

    private ASNBootstrapRegistry(Client client, String url) {
        JSONObject json = new JSONObject(client.target(url).request().get().readEntity(String.class));
        JSONArray services = json.getJSONArray("services");

        for (Object a : services) {
            JSONArray array = (JSONArray) a;

            for (Object s : array.getJSONArray(0)) {
                asnRangeToServiceMap.put((String) s, ((JSONArray) array.get(1)).getString(0));
            }
        }
    }

    /**
     * Get the RDAP service URL for a given ASN
     *
     * @param asn ASN to look up
     * @return RDAP service URL
     */
    public String getServiceURLForASN (String asn) {
        return getServiceURLForASN(Integer.parseInt(asn));
    }

    /**
     * Get the RDAP service URL for a given ASN
     *
     * @param asn ASN to look up
     * @return RDAP service URL
     */
    public String getServiceURLForASN (Integer asn) {
        for (String range : asnRangeToServiceMap.keySet()) {
            // KPN and Roche have to be special and their ASNs are allocated to RIPE as single ASNs rather than blocks
            if (!range.contains("-")) {
                if (Integer.parseInt(range) == asn) asnRangeToServiceMap.get(range);
            } else {
                int start = Integer.parseInt(range.split("-")[0]);
                int end = Integer.parseInt(range.split("-")[1]);

                if (asn >= start && asn <= end) {
                    return asnRangeToServiceMap.get(range);
                }
            }
        }

        return null;
    }
}
