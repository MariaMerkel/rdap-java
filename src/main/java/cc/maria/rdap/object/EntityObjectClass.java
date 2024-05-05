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

package cc.maria.rdap.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ezvcard.Ezvcard;
import ezvcard.VCard;

@JsonIgnoreProperties (ignoreUnknown = true)
public class EntityObjectClass extends ObjectClass {
    private String[] roles;
    private PublicId[] publicIds;
    private Event asEventActor;
    private IPNetworkObjectClass networks;
    private AutnumObjectClass autnums;

    @JsonProperty ("vcardArray")
    private JsonNode vcardArray;

    public AutnumObjectClass getAutnums() {
        return autnums;
    }

    public Event getAsEventActor() {
        return asEventActor;
    }

    public IPNetworkObjectClass getNetworks() {
        return networks;
    }

    public PublicId[] getPublicIds() {
        return publicIds;
    }

    public String[] getRoles() {
        return roles;
    }

    public VCard getvCard() {
        return Ezvcard.parseJson(vcardArray.toString()).first();
    }
}