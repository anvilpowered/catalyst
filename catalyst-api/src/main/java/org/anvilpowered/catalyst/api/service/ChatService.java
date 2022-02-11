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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.jetbrains.annotations.Nullable;

public interface ChatService<TPlayer, TCommandSource> {

  CompletableFuture<Void> sendMessageToChannel(String channelId,
                                               Component message,
                                               UUID userUUID);

  CompletableFuture<Void> sendGlobalMessage(TPlayer player, Component message);

  CompletableFuture<@Nullable Component> formatMessage(String prefix,
                                                       String nameColor,
                                                       String userName,
                                                       UUID userUUID,
                                                       String message,
                                                       boolean hasChatColorPermission,
                                                       String suffix,
                                                       String serverName,
                                                       String channelId);

  List<Component> getPlayerList();

  void sendList(TCommandSource commandSource);

  Component ignore(UUID playerUUID, UUID targetPlayerUUID);

  Component unIgnore(UUID playerUUID, UUID targetPlayerUUID);

  boolean isIgnored(UUID playerUUID, UUID targetPlayerUUID);

  String checkPlayerName(TPlayer sender, String message);

  void sendChatMessage(ChatEvent<TPlayer> chatEvent);

  void toggleChatForUser(TPlayer player);

  boolean isDisabledForUser(TPlayer player);
}
