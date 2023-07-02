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
import jakarta.ws.rs.client.ClientBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ASNBootstrapRegistryTests {
    private static Client testClient = ClientBuilder.newClient();

    /**
     * Test resolution of service for a valid ASN contained in a single-service entry
     */
    @Test
    public void testValidASNSingleEntry () {
        assertEquals(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN(34854), "https://rdap.db.ripe.net/");
    }

    /**
     * Test resolution of service for a valid ASN contained in a multi-service entry
     */
    @Test
    public void testValidASNMultiEntry () {
        assertEquals(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN(13335), "https://rdap.arin.net/registry/");
    }

    /**
     * Test resolution of service for a valid ASN specified as a String
     */
    @Test
    public void testValidASNString () {
        assertEquals(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN("34854"), "https://rdap.db.ripe.net/");
    }

    /**
     * Test resolution of service for an ASN outside an assigned range
     */
    @Test
    public void testInvalidASN () {
        assertNull(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN(64496));
    }
}
