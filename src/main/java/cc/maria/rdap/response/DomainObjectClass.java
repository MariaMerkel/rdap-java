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

public class DomainObjectClass extends ObjectClass {
    private String ldhName;
    private String unicodeName;
    private DomainVariant[] variants;
    private NameserverObjectClass[] nameservers;
    private IPNetworkObjectClass network;

    public String getLdhName() {
        return ldhName;
    }

    public String getUnicodeName() {
        return unicodeName;
    }

    public DomainVariant[] getVariants() {
        return variants;
    }

    public IPNetworkObjectClass getNetwork() {
        return network;
    }

    public NameserverObjectClass[] getNameservers() {
        return nameservers;
    }
}
