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

import static org.junit.Assert.*;

public class EntityBootstrapRegistryTests {
    private static Client testClient = ClientBuilder.newClient();

    /**
     * Tests retrieval of a tag-service mapping that appears as a single-service entry on the bootstrap registry
     */
    @Test
    public void testValidTagFromSingleEntry () {
        assertEquals(EntityBootstrapRegistry.getInstance(testClient).getServiceURL("RIPE"), "https://rdap.db.ripe.net/");
    }

    /**
     * Tests retrieval of a tag-service mapping that appears as a multi-service entry on the bootstrap registry
     */
    @Test
    public void testValidTagFromMultiEntry () {
        assertEquals(EntityBootstrapRegistry.getInstance(testClient).getServiceURL("ARIN"), "https://rdap.arin.net/registry/");
    }

    /**
     * Tests retrieval of a tag-service mapping that does not appear on the bootstrap registry
     */
    @Test
    public void testInvalidTag () {
        assertNull(EntityBootstrapRegistry.getInstance(testClient).getServiceURL("INVALID"));
    }

    /**
     * Test resolution of service for a handle
     */
    @Test
    public void testValidHandle () {
        assertEquals(EntityBootstrapRegistry.getInstance(testClient).getServiceURLForHandle("EXAMPLE-RIPE"), "https://rdap.db.ripe.net/");
    }

    /**
     * Test resolution of service for a handle with a tag that does not appear on the bootstrap registry
     */
    @Test
    public void testInvalidHandle () {
        assertNull(EntityBootstrapRegistry.getInstance(testClient).getServiceURLForHandle("EXAMPLE-INVALID"));
    }

    /**
     * Test resolution of service for a handle that does not include a tag
     */
    @Test
    public void testHandleWithoutTag () {
        assertNull(EntityBootstrapRegistry.getInstance(testClient).getServiceURLForHandle("EXAMPLE"));
    }
}
