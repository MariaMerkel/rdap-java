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

package cc.maria.rdap.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Arrays;

@JsonIgnoreProperties (ignoreUnknown = true)
public class ObjectClass {
    private String objectClassName;
    private String handle;
    private EntityObjectClass[] entities;
    private String[] status;
    private NoticeOrRemark[] remarks;
    private Link[] links;
    private String port43;
    private Event[] events;
    private NoticeOrRemark[] notices;
    private String[] rdapConformance;

    public Link[] getLinks() {
        return links;
    }

    public EntityObjectClass[] getEntities() {
        return entities;
    }

    public Event[] getEvents() {
        return events;
    }

    public NoticeOrRemark[] getRemarks() {
        return remarks;
    }

    public String getHandle() {
        return handle;
    }

    public String getObjectClassName() {
        return objectClassName;
    }

    public String getPort43() {
        return port43;
    }

    public String[] getStatus() {
        return status;
    }

    public String[] getRdapConformance() {
        return rdapConformance;
    }

    public NoticeOrRemark[] getNotices() {
        return notices;
    }

    public EntityObjectClass getEntityByRole (String role) {
        for (EntityObjectClass e : entities) {
            if (Arrays.asList(e.getRoles()).contains(role)) {
                return e;
            }
        }

        return null;
    }

    public EntityObjectClass getRegistrantContact () {
        return getEntityByRole("registrant");
    }

    public EntityObjectClass getTechnicalContact () {
        return getEntityByRole("technical");
    }

    public EntityObjectClass getAdministrativeContact () {
        return getEntityByRole("administrative");
    }

    public EntityObjectClass getAbuseContact () {
        return getEntityByRole("abuse");
    }

    public EntityObjectClass getBillingContact () {
        return getEntityByRole("billing");
    }

    public EntityObjectClass getRegistrarContact () {
        return getEntityByRole("registrar");
    }

    public EntityObjectClass getResellerContact () {
        return getEntityByRole("reseller");
    }

    public EntityObjectClass getSponsorContact () {
        return getEntityByRole("sponsor");
    }

    public EntityObjectClass getProxyContact () {
        return getEntityByRole("proxy");
    }

    public EntityObjectClass getNotificationContact () {
        return getEntityByRole("notifications");
    }

    public EntityObjectClass getNOCContact () {
        return getEntityByRole("noc");
    }

    public String[] getEPPStatus () {
        ArrayList<String> eppStatus = new ArrayList<>();
        for (String s : status) {
            switch (s) {
                case "add period":
                    eppStatus.add("addPeriod");
                    break;

                case "auto renew period":
                    eppStatus.add("autoRenewPeriod");
                    break;

                case "client delete prohibited":
                    eppStatus.add("clientDeleteProhibited");
                    break;

                case "client hold":
                    eppStatus.add("clientHold");
                    break;

                case "client renew prohibited":
                    eppStatus.add("clientRenewProhibited");
                    break;

                case "client transfer prohibited":
                    eppStatus.add("clientTransferProhibited");
                    break;

                case "client update prohibited":
                    eppStatus.add("clientUpdateProhibited");
                    break;

                case "inactive":
                    eppStatus.add("inactive");
                    break;

                case "associated":
                    eppStatus.add("linked");
                    break;

                case "active":
                    eppStatus.add("ok");
                    break;

                case "pending create":
                    eppStatus.add("pendingCreate");
                    break;

                case "pending delete":
                    eppStatus.add("pendingDelete");
                    break;

                case "pending renew":
                    eppStatus.add("pendingRenew");
                    break;

                case "pending restore":
                    eppStatus.add("pendingRestore");
                    break;

                case "pending transfer":
                    eppStatus.add("pendingTransfer");
                    break;

                case "pending update":
                    eppStatus.add("pendingUpdate");
                    break;

                case "redemption period":
                    eppStatus.add("redemptionPeriod");
                    break;

                case "renew period":
                    eppStatus.add("renewPeriod");
                    break;

                case "server delete prohibited":
                    eppStatus.add("serverDeleteProhibited");
                    break;

                case "server renew prohibited":
                    eppStatus.add("serverRenewProhibited");
                    break;

                case "server transfer prohibited":
                    eppStatus.add("serverTransferProhibited");
                    break;

                case "server update prohibited":
                    eppStatus.add("serverUpdateProhibited");
                    break;

                case "server hold":
                    eppStatus.add("serverHold");
                    break;

                case "transfer period":
                    eppStatus.add("transferPeriod");
                    break;
            }
        }

        return eppStatus.toArray(new String[0]);
    }
}
