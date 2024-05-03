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

package cc.maria.rdap.object;

import cc.maria.rdap.bootstrap.ASNBootstrapRegistry;
import cc.maria.rdap.bootstrap.IPv4BootstrapRegistry;
import cc.maria.rdap.exception.RDAPException;
import cc.maria.rdap.exception.UnknownObjectTypeException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectReferenceTests {
    private static Client testClient = ClientBuilder.newClient();

    @Test
    public void testASNHandleCorrection () {
        assertEquals("1234", new ObjectReference("AS1234", ObjectType.ASN).getHandle());
        assertEquals("1234", new ObjectReference("ASN1234", ObjectType.ASN).getHandle());
    }

    @Test
    public void testSetType () throws RDAPException {
        assertEquals(ObjectType.IPv4, new ObjectReference("AS1234", ObjectType.IPv4).getType());
    }

    @Test
    public void testASNTypeDetection () throws RDAPException {
        assertEquals(ObjectType.ASN, new ObjectReference("1234").getType());
        assertEquals(ObjectType.ASN, new ObjectReference("AS1234").getType());
        assertEquals(ObjectType.ASN, new ObjectReference("ASN1234").getType());
    }

    @Test
    public void testIPv4TypeDetection () throws RDAPException {
        assertEquals(ObjectType.IPv4, new ObjectReference("1.1.1.1").getType());
        assertEquals(ObjectType.IPv4, new ObjectReference("1.1.1.0/24").getType());
        assertThrows(UnknownObjectTypeException.class, () -> new ObjectReference("1234.1.1.1").getType());
        assertThrows(UnknownObjectTypeException.class, () -> new ObjectReference("1.1.1.1.1").getType());
        assertThrows(UnknownObjectTypeException.class, () -> new ObjectReference("1.1.1.0/123").getType());
    }

    @Test
    public void testIPv6TypeDetection () throws RDAPException {
        assertEquals(ObjectType.IPv6, new ObjectReference("2001:0db8:85a3:0000:0000:8a2e:0370:7334").getType());
        assertEquals(ObjectType.IPv6, new ObjectReference("2001:0db8:85a3::8a2e:0370:7334").getType());
        assertEquals(ObjectType.IPv6, new ObjectReference("2001:0db8:85a3::/48").getType());
        assertNotEquals(ObjectType.IPv6, new ObjectReference("AS1234").getType());
        assertNotEquals(ObjectType.IPv6, new ObjectReference("1.1.1.1").getType());
        assertNotEquals(ObjectType.IPv6, new ObjectReference("1.1.1.0/24").getType());
    }

    @Test
    public void testSetService () throws RDAPException {
        assertEquals("https://example.com/", new ObjectReference("AS1234", testClient.target("https://example.com/")).getService().getUri().toString());
    }

    @Test
    public void testSetServiceWithClient () throws RDAPException {
        assertEquals("https://example.com/", new ObjectReference("AS1234", testClient.target("https://example.com/")).getService(testClient).getUri().toString());
    }

    @Test
    public void testASNServiceDiscovery () throws RDAPException {
        assertEquals(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN(207908), new ObjectReference("AS207908").getService(testClient).getUri().toString());
    }

    @Test
    public void testIPv4ServiceDiscovery () throws RDAPException {
        assertEquals(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("45.151.215.1"), new ObjectReference("45.151.215.1").getService(testClient).getUri().toString());
    }

    @Test
    public void testASNURLDiscovery () throws RDAPException {
        assertEquals("https://example.com/autnum/1234", new ObjectReference("AS1234", testClient.target("https://example.com/")).getObjectURL().getUri().toString());
    }

    @Test
    public void testDomainURLDiscovery () throws RDAPException {
        assertEquals("https://example.com/domain/example.com", new ObjectReference("example.com", testClient.target("https://example.com/"), ObjectType.DOMAIN).getObjectURL().getUri().toString());
    }

    @Test
    public void testEntityDiscovery () throws RDAPException {
        assertEquals("https://example.com/entity/EXAMPLE-EXAMPLE", new ObjectReference("EXAMPLE-EXAMPLE", testClient.target("https://example.com/"), ObjectType.ENTITY).getObjectURL().getUri().toString());
    }

    @Test
    public void testIPv4URLDiscovery () throws RDAPException {
        assertEquals("https://example.com/ip/1.1.1.1", new ObjectReference("1.1.1.1", testClient.target("https://example.com/")).getObjectURL().getUri().toString());
    }

    @Test
    public void testIPv4SubnetURLDiscovery () throws RDAPException {
        assertEquals("https://example.com/ip/1.1.1.0/24", new ObjectReference("1.1.1.0/24", testClient.target("https://example.com/")).getObjectURL().getUri().toString());
    }

    @Test
    public void testIPv6URLDiscovery () throws RDAPException {
        assertEquals("https://example.com/ip/2001:0db8:85a3::8a2e:0370:7334", new ObjectReference("2001:0db8:85a3::8a2e:0370:7334", testClient.target("https://example.com/")).getObjectURL().getUri().toString());
    }

    @Test
    public void testIPv6SubnetURLDiscovery () throws RDAPException {
        assertEquals("https://example.com/ip/2001:0db8:85a3::/48", new ObjectReference("2001:0db8:85a3::/48", testClient.target("https://example.com/")).getObjectURL().getUri().toString());
    }
}
