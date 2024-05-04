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

import java.time.LocalDateTime;

public class Event {
    private String eventAction;
    private String eventActor;
    private LocalDateTime eventDate;

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public String getEventAction() {
        return eventAction;
    }

    public String getEventActor() {
        return eventActor;
    }
}
