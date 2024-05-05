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

@JsonIgnoreProperties (ignoreUnknown = true)
public class DSData {
    private int keyTag;
    private int algorithm;
    private String digest;
    private int digestType;
    private Event[] events;
    private Link[] links;

    public int getAlgorithm() {
        return algorithm;
    }

    public Event[] getEvents() {
        return events;
    }

    public Link[] getLinks() {
        return links;
    }

    public int getDigestType() {
        return digestType;
    }

    public int getKeyTag() {
        return keyTag;
    }

    public String getDigest() {
        return digest;
    }
}