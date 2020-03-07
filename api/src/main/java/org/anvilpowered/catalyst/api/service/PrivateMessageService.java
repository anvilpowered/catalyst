/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.api.service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PrivateMessageService<TString> {

    Set<UUID> socialSpySet();

    Map<UUID, UUID> replyMap();

    void setSender(String sender);

    String getSender();

    void setRecipient(String recipient);

    String getRecipient();

    void setRawMessage(String rawMessage);

    String getRawMessage();

    TString formatMessage(String sender, String recipient, String rawMessage);

    CompletableFuture<Void> sendMessage(String sender, String recipient, String rawMessage);

    CompletableFuture<Void> socialSpy(String sender, String recipient, String rawMessage);

    TString formatSocialSpyMessage(String sender, String recipient, String rawMessage);
}
