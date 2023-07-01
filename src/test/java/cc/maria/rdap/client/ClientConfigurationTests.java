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

package cc.maria.rdap.client;

import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.bootstrap.DomainBootstrapRegistry;
import jakarta.ws.rs.client.ClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientConfigurationTests {
    @Test
    public void testDefaultClient () {
        RDAPClient client = new RDAPClient();
        assertNotNull(client);
        Assert.assertEquals(client.getServiceURL("example.com"), DomainBootstrapRegistry.getInstance(ClientBuilder.newClient()).getServiceURLForFQDN("example.com"));
    }

    @Test
    public void testSetServiceURL () {
        RDAPClient client = new RDAPClient("https://rdap.invalid/");
        assertNotNull(client);
        assertEquals(client.getServiceURL("example.com"), "https://rdap.invalid/");
    }

    @Test
    public void testServiceURLNormalization () {
        RDAPClient client = new RDAPClient("https://rdap.invalid");
        assertNotNull(client);
        assertEquals(client.getServiceURL("example.com"), "https://rdap.invalid/");
    }
}