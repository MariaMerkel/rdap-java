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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class RFC8056Tests {
    @Test
    public void testEPPStatusCodeMapping() throws JsonProcessingException {
        String json = "{\"objectClassName\":\"domain\",\"handle\":\"187144380_DOMAIN_CC-VRSN\",\"ldhName\":\"MARIA.CC\",\"links\":[{\"value\":\"https:\\/\\/tld-rdap.verisign.com\\/cc\\/v1\\/domain\\/MARIA.CC\",\"rel\":\"self\",\"href\":\"https:\\/\\/tld-rdap.verisign.com\\/cc\\/v1\\/domain\\/MARIA.CC\",\"type\":\"application\\/rdap+json\"},{\"value\":\"https:\\/\\/rdap.staclar.com\\/domain\\/MARIA.CC\",\"rel\":\"related\",\"href\":\"https:\\/\\/rdap.staclar.com\\/domain\\/MARIA.CC\",\"type\":\"application\\/rdap+json\"}],\"status\":[\"client delete prohibited\",\"client renew prohibited\",\"client transfer prohibited\",\"client update prohibited\",\"server delete prohibited\",\"server transfer prohibited\",\"server update prohibited\"],\"entities\":[{\"objectClassName\":\"entity\",\"handle\":\"3884\",\"roles\":[\"registrar\"],\"publicIds\":[{\"type\":\"IANA Registrar ID\",\"identifier\":\"3884\"}],\"vcardArray\":[\"vcard\",[[\"version\",{},\"text\",\"4.0\"],[\"fn\",{},\"text\",\"Staclar, Inc.\"]]],\"entities\":[{\"objectClassName\":\"entity\",\"roles\":[\"abuse\"],\"vcardArray\":[\"vcard\",[[\"version\",{},\"text\",\"4.0\"],[\"fn\",{},\"text\",\"\"],[\"tel\",{\"type\":\"voice\"},\"uri\",\"tel:+1-302-291-1140\"],[\"email\",{},\"text\",\"abuse@staclar.com\"]]]}]}],\"events\":[{\"eventAction\":\"registration\",\"eventDate\":\"2023-01-18T09:02:34Z\"},{\"eventAction\":\"expiration\",\"eventDate\":\"2026-01-18T09:02:34Z\"},{\"eventAction\":\"last changed\",\"eventDate\":\"2023-03-28T16:58:06Z\"},{\"eventAction\":\"last update of RDAP database\",\"eventDate\":\"2024-05-05T07:16:05Z\"}],\"secureDNS\":{\"delegationSigned\":false},\"nameservers\":[{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS1-34.AZURE-DNS.COM\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS2-34.AZURE-DNS.NET\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS3-34.AZURE-DNS.ORG\"},{\"objectClassName\":\"nameserver\",\"ldhName\":\"NS4-34.AZURE-DNS.INFO\"}],\"rdapConformance\":[\"rdap_level_0\",\"icann_rdap_technical_implementation_guide_0\",\"icann_rdap_response_profile_0\"],\"notices\":[{\"title\":\"Terms of Use\",\"description\":[\"Service subject to Terms of Use.\"],\"links\":[{\"href\":\"https:\\/\\/www.verisign.com\\/domain-names\\/registration-data-access-protocol\\/terms-service\\/index.xhtml\",\"type\":\"text\\/html\"}]},{\"title\":\"Status Codes\",\"description\":[\"For more information on domain status codes, please visit https:\\/\\/icann.org\\/epp\"],\"links\":[{\"href\":\"https:\\/\\/icann.org\\/epp\",\"type\":\"text\\/html\"}]},{\"title\":\"RDDS Inaccuracy Complaint Form\",\"description\":[\"URL of the ICANN RDDS Inaccuracy Complaint Form: https:\\/\\/icann.org\\/wicf\"],\"links\":[{\"href\":\"https:\\/\\/icann.org\\/wicf\",\"type\":\"text\\/html\"}]}]}";
        DomainObjectClass domainObjectClass = new ObjectMapper().readValue(json, DomainObjectClass.class);

        assertArrayEquals(domainObjectClass.getEPPStatus(), new String[]{"clientDeleteProhibited", "clientRenewProhibited", "clientTransferProhibited", "clientUpdateProhibited", "serverDeleteProhibited", "serverTransferProhibited", "serverUpdateProhibited"});
    }
}
