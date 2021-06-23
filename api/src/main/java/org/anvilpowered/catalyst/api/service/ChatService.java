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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.anvilpowered.catalyst.api.event.ChatEvent;

public interface ChatService<TString, TPlayer, TCommandSource> {

  CompletableFuture<Void> sendMessageToChannel(String channelId,
                                               TString message,
                                               UUID userUUID);

  CompletableFuture<Void> sendGlobalMessage(TPlayer player, TString message);

  CompletableFuture<Optional<TString>> formatMessage(String prefix,
                                                     String nameColor,
                                                     String userName,
                                                     UUID userUUID,
                                                     String message,
                                                     boolean hasChatColorPermission,
                                                     String suffix,
                                                     String serverName,
                                                     String channelId);

  List<TString> getPlayerList();

  void sendList(TCommandSource commandSource);

  TString ignore(UUID playerUUID, UUID targetPlayerUUID);

  TString unIgnore(UUID playerUUID, UUID targetPlayerUUID);

  boolean isIgnored(UUID playerUUID, UUID targetPlayerUUID);

  String checkPlayerName(TPlayer sender, String message);

  void sendChatMessage(ChatEvent<TString, TPlayer> chatEvent);

  void toggleChatForUser(TPlayer player);

  boolean isDisabledForUser(TPlayer player);
}
