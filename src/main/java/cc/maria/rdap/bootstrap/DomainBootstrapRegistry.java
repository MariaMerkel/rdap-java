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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * RFC 9224-compliant wrapper around the IANA RDAP Bootstrap Service Registry for the Domain Name Space
 */
public class DomainBootstrapRegistry {
    private HashMap<String, String> labelToServiceMap = new HashMap<>();

    private static DomainBootstrapRegistry instance;

    /**
     * Get current instance of the wrapper
     *
     * @return DomainBootstrapRegistry instance
     */
    public static DomainBootstrapRegistry getInstance () {
        if (instance == null) refresh();

        return instance;
    }

    /**
     * Throw away the current instance and get a new one with updated bootstrap data
     *
     * @return DomainBootstrapRegistry instance
     */
    public static DomainBootstrapRegistry refresh () {
        try {
            instance = new DomainBootstrapRegistry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instance;
    }

    private DomainBootstrapRegistry () throws IOException {
        this (new URL("https://data.iana.org/rdap/dns.json"));
    }

    private DomainBootstrapRegistry (URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            JSONObject json = new JSONObject(new String(inputStream.readAllBytes()));
            JSONArray services = json.getJSONArray("services");

            for (Object a : services) {
                JSONArray array = (JSONArray) a;

                for (Object s : array.getJSONArray(0)) {
                    labelToServiceMap.put((String) s, ((JSONArray) array.get(1)).getString(0));
                }
            }
        }
    }

    /**
     * Get the RDAP service URL for a given label
     *
     * @param label label to look up
     * @return RDAP service URL
     */
    public String getServiceURL (String label) {
        return labelToServiceMap.get(label);
    }

    /**
     * Get the RDAP service URL for a given FQDN
     *
     * @param fqdn FQDN to look up
     * @return RDAP service URL
     */
    public String getServiceURLForFQDN (String fqdn) {
        if (labelToServiceMap.containsKey(fqdn)) return labelToServiceMap.get(fqdn);
        if (!fqdn.contains(".")) return null;
        return getServiceURLForFQDN(fqdn.substring(fqdn.indexOf('.') + 1));
    }
}
