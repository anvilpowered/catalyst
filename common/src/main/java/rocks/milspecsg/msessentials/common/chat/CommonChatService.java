/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.common.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import rocks.milspecsg.msessentials.api.chat.ChatService;
import rocks.milspecsg.msessentials.api.data.config.Channel;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msrepository.api.data.key.Key;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.StringResult;
import rocks.milspecsg.msrepository.api.util.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Singleton
public class CommonChatService<
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    implements ChatService<TString> {

    @Inject
    Registry registry;

    @Inject
    MemberManager<TString> memberManager;

    @Inject
    StringResult<TString, TCommandSource> stringResult;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    Map<UUID, String> channelMap = new HashMap<>();
    List<String> playerListMap = new ArrayList<>();

    @Override
    public void switchChannel(UUID userUUID, String channelId) {
        channelMap.put(userUUID, channelId);
    }

    @Override
    public String getChannelIdForUser(UUID userUUID) {
        String channelId = channelMap.get(userUUID);
        if (channelId == null) {
            return registry.getOrDefault(MSEssentialsKeys.CHAT_DEFAULT_CHANNEL);
        }
        return channelId;
    }

    @Override
    public Optional<Channel> getChannelFromId(String channelId) {
        return registry.get(MSEssentialsKeys.CHAT_CHANNELS).flatMap(channels ->
            channels.stream()
                .filter(c -> c.id.equals(channelId))
                .findAny()
        );
    }

    @Override
    public Optional<String> getChannelPrefix(String channelId) {
        return getChannelFromId(channelId).map(c -> c.prefix);
    }

    @Override
    public List<String> getUsersInChannel(String channelId) {
        List<String> userList = new ArrayList<>();
        return userList;
    }

    @Override
    public CompletableFuture<Void> sendMessageToChannel(String channelId, TString message) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> {
            if (getChannelIdForUser(userService.getUUID(p)).equals(channelId)) {
                stringResult.send(message, p);
            }
        }));
    }

    @Override
    public CompletableFuture<Void> sendGlobalMessage(TString message) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> stringResult.send(message, p)));
    }

    @Override
    public CompletableFuture<TString> formatMessage(
        String prefix,
        String nameColor,
        String userName,
        String message,
        boolean hasChatColorPermission,
        String suffix,
        String serverName,
        String channelId,
        String channelPrefix
    ) {
        return memberManager.getPrimaryComponent().getOneForUser(userName).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return stringResult.fail("Couldn't find a user matching that name!");
            }

            String finalName = optionalMember.get().getUserName();
            if (optionalMember.get().getNickName() != null) {
                finalName = optionalMember.get().getNickName();
            } else {
                finalName = nameColor + finalName;
            }
            return stringResult
                .builder()
                .append(stringResult.deserialize(replacePlaceholders(message, prefix, finalName, hasChatColorPermission, suffix, serverName, channelPrefix, MSEssentialsKeys.PROXY_CHAT_FORMAT_MESSAGE)))
                .onHoverShowText(stringResult.deserialize(replacePlaceholders(message, prefix, finalName, hasChatColorPermission, suffix, serverName, channelPrefix, MSEssentialsKeys.PROXY_CHAT_FORMAT_HOVER)))
                .onClickSuggestCommand(replacePlaceholders(message, prefix, userName, hasChatColorPermission, suffix, finalName, channelPrefix, MSEssentialsKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND))
                .build();
        });
    }

    private String replacePlaceholders(
        String rawMessage,
        String prefix,
        String userName,
        boolean hasChatColorPermission,
        String suffix,
        String serverName,
        String channelPrefix,
        Key<String> key
    ) {
        return registry.get(key)
            .orElseThrow(() -> new IllegalStateException("Missing chat formatting!"))
            .replace("%prefix%", prefix)
            .replace("%username%", userName)
            .replace("%suffix%", suffix)
            .replace("%server%", serverName)
            .replace("%channel%", channelPrefix)
            .replace("%message%", hasChatColorPermission ? rawMessage : stringResult.removeColor(rawMessage));
    }

    @Override
    public String getPlayerList() {
        return userService.getOnlinePlayers().stream().map(userService::getUserName).collect(Collectors.joining(", \n"));
    }

    @Override
    public TString list() {
        return stringResult.builder()
            .green().append("------------------- Online Players --------------------\n")
            .gray().append(getPlayerList())
            .build();
    }
}
