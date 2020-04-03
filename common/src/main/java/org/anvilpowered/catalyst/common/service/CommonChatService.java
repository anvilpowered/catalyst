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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.core.model.coremember.CoreMember;
import org.anvilpowered.anvil.api.data.key.Key;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.config.Channel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public class CommonChatService<
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    implements ChatService<TString, TPlayer, TCommandSource> {

    @Inject
    Registry registry;

    @Inject
    MemberManager<TString> memberManager;

    @Inject
    TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private LuckpermsService<TPlayer> luckpermsService;

    @Inject
    private CurrentServerService currentServerService;

    @Inject
    private PermissionService<TCommandSource> permissionService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    Map<UUID, String> channelMap = new HashMap<>();

    Map<UUID, List<UUID>> ignoreMap = new HashMap<>();

    @Override
    public void switchChannel(UUID userUUID, String channelId) {
        channelMap.put(userUUID, channelId);
    }

    @Override
    public String getChannelIdForUser(UUID userUUID) {
        String channelId = channelMap.get(userUUID);
        if (channelId == null) {
            return registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL);
        }
        return channelId;
    }

    @Override
    public Optional<Channel> getChannelFromId(String channelId) {
        return registry.get(CatalystKeys.CHAT_CHANNELS).flatMap(channels ->
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
    public int getChannelUserCount(String channelId) {
        return (int) userService.getOnlinePlayers()
            .stream()
            .filter(p -> getChannelIdForUser(userService.getUUID(p))
                .equals(channelId)).count();
    }

    @Override
    public TString getUsersInChannel(String channelId) {
        List<String> channelUsersList = userService.getOnlinePlayers()
            .stream()
            .filter(p -> getChannelIdForUser(userService.getUUID(p)).equals(channelId))
            .map(p -> userService.getUserName(p))
            .collect(Collectors.toList());

        return textService.builder()
            .green().append("------------------- ")
            .gold().append(channelId)
            .green().append(" --------------------\n")
            .append(String.join(", ", channelUsersList))
            .build();
    }

    @Override
    public CompletableFuture<Void> sendMessageToChannel(String channelId, TString message, UUID senderUUID, Predicate<? super TPlayer> checkOverridePerm) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> {
            if (checkOverridePerm.test(p) || getChannelIdForUser(userService.getUUID(p)).equals(channelId)) {
                if (!senderUUID.equals(userService.getUUID(p))) {
                    if (!isIgnored(userService.getUUID(p), senderUUID)) {
                        textService.send(message, p);
                    }
                } else {
                    textService.send(message, p);
                }
            }
        }));
    }

    @Override
    public CompletableFuture<Void> sendGlobalMessage(TString message) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> textService.send(message, p)));
    }

    @Override
    public CompletableFuture<Optional<TString>> formatMessage(
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
                return Optional.of(textService.fail("Couldn't find a user matching that name!"));
            }

            CoreMember<?> optionalCoreMember = optionalMember.get();
            if (optionalMember.get().isMuted()) {
                return Optional.empty();
            }

            String finalName = optionalMember.get().getUserName();
            if (optionalCoreMember.getNickName() != null) {
                finalName = optionalMember.get().getNickName() + "&r";
            } else {
                finalName = nameColor + finalName + "&r";
            }
            return Optional.of(textService
                .builder()
                .append(textService.deserialize(replacePlaceholders(message, prefix, finalName, hasChatColorPermission, suffix, serverName, channelPrefix, CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE)))
                .onHoverShowText(textService.deserialize(replacePlaceholders(message, prefix, finalName, hasChatColorPermission, suffix, serverName, channelPrefix, CatalystKeys.PROXY_CHAT_FORMAT_HOVER)))
                .onClickSuggestCommand(replacePlaceholders(message, prefix, userName, hasChatColorPermission, suffix, finalName, channelPrefix, CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND))
                .build());
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
            .replace("%message%", hasChatColorPermission ? rawMessage : textService.toPlain(rawMessage));
    }

    @Override
    public List<TString> getPlayerList() {
        List<String> playerList = userService.getOnlinePlayers().stream()
            .map(userService::getUserName).collect(Collectors.toList());
        List<TString> tempList = new ArrayList<>();
        StringBuilder builder = null;
        for (String s : playerList) {
            if (builder == null) {
                builder = new StringBuilder(s);
            } else if (builder.length() + s.length() < 50) {
                builder.append(" ").append(s);
            } else {
                tempList.add(textService.of(builder.toString()));
                builder = new StringBuilder(s);
            }
        }
        return tempList;
    }

    @Override
    public void sendList(TCommandSource commandSource) {
        textService.paginationBuilder()
            .header(textService.builder().green().append("Online Players").build())
            .padding(textService.of("-"))
            .contents(getPlayerList())
            .build()
            .sendTo(commandSource);
    }

    @Override
    public TString createTempChannel(String name, UUID creator) {
        Channel channel = new Channel();
        channel.aliases = Arrays.asList(name);
        channel.id = name;
        channel.prefix = name;
        return textService.success("Created the channel " + name + " successfully");
    }

    @Override
    public TString ignore(UUID playerUUID, UUID targetPlayerUUID) {
        List<UUID> uuidList = new ArrayList<>();
        if (ignoreMap.get(playerUUID) == null) {
            uuidList.add(targetPlayerUUID);
        } else {
            uuidList = ignoreMap.get(playerUUID);
            if (uuidList.contains(targetPlayerUUID)) {
                return unIgnore(playerUUID, targetPlayerUUID);
            }
        }
        ignoreMap.put(playerUUID, uuidList);
        return textService.success("You are now ignoring " + userService.getUserName(targetPlayerUUID).get());
    }

    @Override
    public TString unIgnore(UUID playerUUID, UUID targetPlayerUUID) {
        List<UUID> uuidList = ignoreMap.get(playerUUID);
        if (isIgnored(playerUUID, targetPlayerUUID)) {
            uuidList.remove(targetPlayerUUID);
            ignoreMap.replace(playerUUID, uuidList);
        }
        return textService.success("You are no longer ignoring " + userService.getUserName(targetPlayerUUID).get());
    }

    @Override
    public boolean isIgnored(UUID playerUUID, UUID targetPlayerUUID) {
        List<UUID> uuidList = ignoreMap.get(playerUUID);
        if (uuidList == null) {
            return false;
        }
        return uuidList.contains(targetPlayerUUID);
    }

    @Override
    public String checkPlayerName(String message) {
        for (TPlayer player : userService.getOnlinePlayers()) {
            if (message.contains(userService.getUserName(player))) {
                String userName = userService.getUserName(player);
                message = message.replaceAll(
                    userName.toUpperCase(),
                    "&b@" + userName + "&r")
                    .replaceAll(
                        userName.toLowerCase(),
                        "&b@" + userName + "&r"
                    );
            }
        }
        return message;
    }

    @Override
    public void sendChatMessage(TPlayer player, String message) {
        String prefix = luckpermsService.getPrefix(player);
        String chatColor = luckpermsService.getChatColor(player);
        String nameColor = luckpermsService.getNameColor(player);
        String suffix = luckpermsService.getSuffix(player);
        String userName = userService.getUserName(player);
        UUID userUUID = userService.getUUID(userName).orElseThrow(() ->
            new IllegalStateException("Unable to find a UUID for " + userName));
        String server = currentServerService.getName(userName).orElseThrow(() ->
            new IllegalStateException(userName + " is not in a valid server!"));
        String channelId = getChannelIdForUser(userService.getUUID(player));
        Optional<Channel> channel = getChannelFromId(channelId);
        String channelPrefix = getChannelPrefix(channelId).orElseThrow(() ->
            new IllegalStateException("Please specify a prefix for " + channelId));

        boolean hasColorPermission = permissionService.hasPermission(
            player,
            registry.getOrDefault(CatalystKeys.CHAT_COLOR)
        );

        formatMessage(
            prefix,
            nameColor,
            userName,
            chatColor + message,
            hasColorPermission,
            suffix,
            server,
            channelId,
            channelPrefix
        ).thenAcceptAsync(optionalMessage -> {
            if (optionalMessage.isPresent()) {
                sendMessageToChannel(channelId, optionalMessage.get(), userUUID, p ->
                    permissionService.hasPermission(p, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS))
                );
            } else {
                textService.send(pluginMessages.getMuted(), player);
            }
        });
    }
}
