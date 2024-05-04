/*
 * Copyright (c) 2024 Maria Merkel
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

package cc.maria.rdap.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import inet.ipaddr.IPAddress;

@JsonIgnoreProperties (ignoreUnknown = true)
public class IPNetworkObjectClass extends ObjectClass {
    private String startAddress;
    private String endAddress;
    private IPAddress.IPVersion ipVersion;
    private String name;
    private String type;
    private String country;
    private String parentHandle;

    public String getType() {
        return type;
    }

    public IPAddress.IPVersion getIpVersion() {
        return ipVersion;
    }

    public String getCountry() {
        return country;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getName() {
        return name;
    }

    public String getParentHandle() {
        return parentHandle;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public enum ipVersion {
        v4, v6
    }
}
