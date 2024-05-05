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

package cc.maria.rdap.usecase;

import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.exception.RDAPException;
import cc.maria.rdap.object.DomainObjectClass;
import cc.maria.rdap.object.ObjectReference;
import cc.maria.rdap.object.ObjectType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryTests {
    @Test
    public void testFullQuery () throws RDAPException, JsonProcessingException {
        DomainObjectClass domainObjectClass = (DomainObjectClass) new RDAPClient().query(new ObjectReference("maria.cc", ObjectType.DOMAIN));

        assertEquals("187144380_DOMAIN_CC-VRSN", domainObjectClass.getHandle());
    }
}
