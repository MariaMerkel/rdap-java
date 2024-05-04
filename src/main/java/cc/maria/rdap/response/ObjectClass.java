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
}
