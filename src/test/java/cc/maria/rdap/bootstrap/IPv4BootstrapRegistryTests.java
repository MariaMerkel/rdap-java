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

public class IPv4BootstrapRegistryTests {
    private static Client testClient = ClientBuilder.newClient();

    /**
     * Test resolution of service for a valid IP address contained in a single-service entry
     */
    @Test
    public void testValidIPSingleEntry () {
        assertEquals(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("2.56.11.1"), "https://rdap.db.ripe.net/");
    }

    /**
     * Test resolution of service for a valid IP address contained in a multi-service entry
     */
    @Test
    public void testValidIPMultiEntry () {
        assertEquals(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("3.1.1.1"), "https://rdap.arin.net/registry/");
    }

    /**
     * Test resolution of service for a valid subnet
     */
    @Test
    public void testValidSubnet () {
        assertEquals(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("2.56.11.0/24"), "https://rdap.db.ripe.net/");
    }

    /**
     * Test resolution of service for an IP address outside an assigned range
     */
    @Test
    public void testInvalidIP () {
        assertNull(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("255.1.1.1"));
    }
}
