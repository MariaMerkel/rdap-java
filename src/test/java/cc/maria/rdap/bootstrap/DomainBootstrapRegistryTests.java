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

import org.junit.Test;

import static org.junit.Assert.*;

public class DomainBootstrapRegistryTests {
    /**
     * Tests retrieval of a label-service mapping that appears as a single-label entry on the bootstrap registry
     */
    @Test
    public void testValidLabelFromSingleEntry () {
        assertEquals(DomainBootstrapRegistry.getInstance().getServiceURL("com"), "https://rdap.verisign.com/com/v1/");
    }

    /**
     * Tests retrieval of a label-service mapping that appears as a multi-label entry on the bootstrap registry
     */
    @Test
    public void testValidLabelFromMultiEntry () {
        assertEquals(DomainBootstrapRegistry.getInstance().getServiceURL("dev"), "https://www.registry.google/rdap/");
    }

    /**
     * Tests retrieval of a label-service mapping that does not appear on the bootstrap registry
     */
    @Test
    public void testInvalidLabel () {
        assertNull(DomainBootstrapRegistry.getInstance().getServiceURL("invalid"));
    }

    /**
     * Test resolution of service for a SLD
     */
    @Test
    public void testValidSLD () {
        assertEquals(DomainBootstrapRegistry.getInstance().getServiceURLForFQDN("example.com"), "https://rdap.verisign.com/com/v1/");
    }

    /**
     * Test resolution of service for a SLD under a TLD that does not appear on the bootstrap registry
     */
    @Test
    public void testInvalidSLD () {
        assertNull(DomainBootstrapRegistry.getInstance().getServiceURLForFQDN("example.invalid"));
    }

    /**
     * Test resolution of service for a FQDN
     */
    @Test
    public void testValidFQDN () {
        assertEquals(DomainBootstrapRegistry.getInstance().getServiceURLForFQDN("example.sub.example.com"), "https://rdap.verisign.com/com/v1/");
    }

    /**
     * Test resolution of service for a FQDN under a TLD that does not appear on the bootstrap registry
     */
    @Test
    public void testInvalidFQDN () {
        assertNull(DomainBootstrapRegistry.getInstance().getServiceURLForFQDN("example.sub.example.invalid"));
    }
}
