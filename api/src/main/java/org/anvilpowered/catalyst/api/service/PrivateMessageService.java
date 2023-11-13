/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.api.service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;

public interface PrivateMessageService {

  Set<UUID> socialSpySet();

  Map<UUID, UUID> replyMap();

  String getSource();

  void setSource(String sender);

  String getRecipient();

  void setRecipient(String recipient);

  String getRawMessage();

  void setRawMessage(String rawMessage);

  Component formatMessage(String sender, String recipient, String rawMessage);

  CompletableFuture<Void> sendMessage(String sender, String recipient, String rawMessage);

  CompletableFuture<Void> sendMessageFromConsole(String recipient, String rawMessage,
                                                 Class<?> console);

  CompletableFuture<Void> socialSpy(String sender, String recipient, String rawMessage);

  Component formatSocialSpyMessage(String sender, String recipient, String rawMessage);
}
