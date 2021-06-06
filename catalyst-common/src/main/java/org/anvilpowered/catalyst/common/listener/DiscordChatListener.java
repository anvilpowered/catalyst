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

package org.anvilpowered.catalyst.common.listener;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import org.anvilpowered.anvil.api.misc.Named;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.discord.WebhookSender;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.registry.ChatChannel;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;

import java.util.Optional;

public class DiscordChatListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource> {

    @Inject
    private Registry registry;

    @Inject
    private LuckpermsService luckPermsService;

    @Inject
    private WebhookSender webHookSender;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private AdvancedServerInfoService serverService;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private LocationService locationService;

    @Inject
    private EmojiService emojiService;

    @Inject
    private PermissionService permissionService;

    @Subscribe
    public void onChatEvent(ChatEvent<TString, TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            return;
        }

        Optional<ChatChannel> optionalChannel =
            chatService.getChannelFromId(chatService.getChannelIdForUser(userService.getUUID((TUser) event.getPlayer())));
        String dChannelId = registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL);

        if (optionalChannel.isPresent()) {
            if (optionalChannel.get().discordChannel != null) {
                dChannelId = optionalChannel.get().discordChannel;
            }
        }

        String message = event.getRawMessage();
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                message = message.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }
        if (!permissionService.hasPermission(event.getPlayer(),
            registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION))) {
            message = message.replaceAll("@", "");
        }

        String server =
            locationService.getServer(userService.getUserName((TUser) event.getPlayer())).map(Named::getName).orElse("null");
        if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
            server = serverService.getPrefixForPlayer(userService.getUserName((TUser) event.getPlayer()));
        }

        String name = registry.getOrDefault(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT)
            .replace("%server%", server)
            .replace("%player%", userService.getUserName((TUser) event.getPlayer()))
            .replace("%prefix%", luckPermsService.getPrefix(event.getPlayer()))
            .replace("%suffix%", luckPermsService.getSuffix(event.getPlayer()));
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            name,
            message,
            dChannelId,
            event.getPlayer()
        );
    }

    @Subscribe
    public void onPlayerJoinEvent(JoinEvent<TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            return;
        }

        String joinMessage = registry.getOrDefault(CatalystKeys.DISCORD_JOIN_FORMAT);
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                joinMessage = joinMessage.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }
        String server = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
            ? serverService.getPrefixForPlayer(userService.getUserName((TUser) event.getPlayer()))
            : locationService.getServer(userService.getUserName((TUser) event.getPlayer())).map(Named::getName).orElse("null");

        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            registry.getOrDefault(CatalystKeys.BOT_NAME),
            joinMessage.replace(
                "%player%", userService.getUserName((TUser) event.getPlayer())
            ).replace(
                "%server%", server
            ),
            registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL),
            event.getPlayer()
        );
    }

    @Subscribe
    public void onPlayerLeaveEvent(LeaveEvent<TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return;
        String leaveMessage = registry.getOrDefault(CatalystKeys.DISCORD_LEAVE_FORMAT);
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                leaveMessage = leaveMessage.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }
        String server = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
            ? serverService.getPrefixForPlayer(userService.getUserName((TUser) event.getPlayer()))
            : locationService.getServer(userService.getUserName((TUser) event.getPlayer())).map(Named::getName).orElse("null");
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            registry.getOrDefault(CatalystKeys.BOT_NAME),
            leaveMessage.replace(
                "%player%",
                userService.getUserName((TUser) event.getPlayer())
            ).replace(
                "%server%",
                server
            ),
            registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL),
            event.getPlayer()
        );
    }
}
