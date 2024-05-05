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

import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.bootstrap.ASNBootstrapRegistry;
import cc.maria.rdap.bootstrap.IPv4BootstrapRegistry;
import cc.maria.rdap.exception.RDAPException;
import cc.maria.rdap.exception.UnknownObjectTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        assertEquals("https://example.com/", new ObjectReference("AS1234", testClient.target("https://example.com/")).getService(new RDAPClient(testClient)).getUri().toString());
    }

    @Test
    public void testASNServiceDiscovery () throws RDAPException {
        assertEquals(ASNBootstrapRegistry.getInstance(testClient).getServiceURLForASN(207908), new ObjectReference("AS207908").getService(new RDAPClient(testClient)).getUri().toString());
    }

    @Test
    public void testIPv4ServiceDiscovery () throws RDAPException {
        assertEquals(IPv4BootstrapRegistry.getInstance(testClient).getServiceURLForIP("45.151.215.1"), new ObjectReference("45.151.215.1").getService(new RDAPClient(testClient)).getUri().toString());
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

    @Test
    public void testGetEntityByRole () throws JsonProcessingException {
        String json = "{\"objectClassName\":\"domain\",\"handle\":\"187144380_DOMAIN_CC-VRSN\",\"ldhName\":\"MARIA.CC\",\"links\":[{\"value\":\"https:\\/\\/tld-rdap.verisign.com\\/cc\\/v1\\/domain\\/MARIA.CC\",\"rel\":\"self\",\"href\":\"https:\\/\\/tld-rdap.verisign.com\\/cc\\/v1\\/domain\\/MARIA.CC\",\"type\":\"application\\/rdap+json\"},{\"value\":\"https:\\/\\/rdap.staclar.com\\/domain\\/MARIA.CC\",\"rel\":\"related\",\"href\":\"https:\\/\\/rdap.staclar.com\\/domain\\/MARIA.CC\",\"type\":\"application\\/rdap+json\"}],\"status\":[\"client delete prohibited\",\"client renew prohibited\",\"client transfer prohibited\",\"client update prohibited\",\"server delete prohibited\",\"server transfer prohibited\",\"server update prohibited\"],\"entities\":[{\"objectClassName\":\"entity\",\"handle\":\"3884\",\"roles\":[\"registrar\"],\"publicIds\":[{\"type\":\"IANA Registrar ID\",\"identifier\":\"3884\"}],\"vcardArray\":[\"vcard\",[[\"version\",{},\"text\",\"4.0\"],[\"fn\",{},\"text\",\"Staclar, Inc.\"]]],\"entities\":[{\"objectClassName\":\"entity\",\"roles\":[\"abuse\"],\"vcardArray\":[\"vcard\",[[\"version\",{},\"text\",\"4.0\"],[\"fn\",{},\"text\",\"\"],[\"tel\",{\"type\":\"voice\"},\"uri\",\"tel:+1-302-291-1140\"],[\"email\",{},\"text\",\"abuse@staclar.com\"]]]}]}],\"events\":[{\"eventAction\":\"registration\",\"eventDate\":\"2023-01-18T09:02:34Z\"},{\"eventAction\":\"expiration\",\"eventDate\":\"2026-01-18T09:02:34Z\"},{\"eventAction\":\"last changed\",\"eventDate\":\"2023-03-28T16:58:06Z\"},{\"eventAction\":\"last update of RDAP database\",\"eventDate\":\"2024-05-05T07:16:05Z\"}],\"secureDNS\":{\"delegationSigned\":false},\"nameservers\":[{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS1-34.AZURE-DNS.COM\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS2-34.AZURE-DNS.NET\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS3-34.AZURE-DNS.ORG\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS4-34.AZURE-DNS.INFO\"}],\"rdapConformance\":[\"rdap_level_0\",\"icann_rdap_technical_implementation_guide_0\",\"icann_rdap_response_profile_0\"],\"notices\":[{\"title\":\"Terms of Use\",\"description\":[\"Service subject to Terms of Use.\"],\"links\":[{\"href\":\"https:\\/\\/www.verisign.com\\/domain-names\\/registration-data-access-protocol\\/terms-service\\/index.xhtml\",\"type\":\"text\\/html\"}]},{\"title\":\"Status Codes\",\"description\":[\"For more information on domain status codes, please visit https:\\/\\/icann.org\\/epp\"],\"links\":[{\"href\":\"https:\\/\\/icann.org\\/epp\",\"type\":\"text\\/html\"}]},{\"title\":\"RDDS Inaccuracy Complaint Form\",\"description\":[\"URL of the ICANN RDDS Inaccuracy Complaint Form: https:\\/\\/icann.org\\/wicf\"],\"links\":[{\"href\":\"https:\\/\\/icann.org\\/wicf\",\"type\":\"text\\/html\"}]}]}";
        DomainObjectClass domainObjectClass = new ObjectMapper().readValue(json, DomainObjectClass.class);

        assertEquals("3884", domainObjectClass.getEntityByRole("registrar").getHandle());
    }
}
