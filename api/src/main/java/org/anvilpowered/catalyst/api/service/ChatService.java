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

import org.anvilpowered.catalyst.api.data.config.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface ChatService<TString, TPlayer, TCommandSource> {

    void switchChannel(UUID userUUID, String channelId);

    String getChannelIdForUser(UUID userUUID);

    Optional<Channel> getChannelFromId(String channelId);

    Optional<String> getChannelPrefix(String channelId);

    int getChannelUserCount(String channelId);

    TString getUsersInChannel(String channelId);

    CompletableFuture<Void> sendMessageToChannel(String channelId, TString message, UUID senderUUID, Predicate<? super TPlayer> checkOverridePerm);

    CompletableFuture<Void> sendGlobalMessage (TString message);

    CompletableFuture<Optional<TString>> formatMessage(String prefix, String nameColor, String userName, String message, boolean hasChatColorPermission, String suffix, String serverName, String channelId, String channelPrefix);

    List<TString> getPlayerList();

    void sendList(TCommandSource commandSource);

    TString createTempChannel(String prefix, UUID creator);

    TString ignore(UUID playerUUID, UUID targetPlayerUUID);

    TString unIgnore(UUID playerUUID, UUID targetPlayerUUID);

    boolean isIgnored(UUID playerUUID, UUID targetPlayerUUID);

    String checkPlayerName(String message);

    void sendChatMessage(TPlayer player, String message);
}
