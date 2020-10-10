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

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.discord.WebhookSender;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

public class CommonDiscordChatListener<TUser, TString, TPlayer> implements DiscordChatListener<TString, TPlayer> {

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
    private CurrentServerService currentServerService;

    @Inject
    private EmojiService emojiService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private StaffChatService staffChatService;

    @Override
    public void onChatEvent(ChatEvent<TString, TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return;
        if (staffChatService.contains(userService.getUUID((TUser) event.getPlayer()))) return;

        String message = event.getRawMessage();
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                message = message.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }
        if(!permissionService.hasPermission(event.getPlayer(),
            registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION))) {
            message = message.replaceAll("@", "");
        }

        String server = currentServerService.getName(userService.getUserName((TUser) event.getPlayer())).orElse("null");
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
            registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL),
            event.getPlayer()
        );
    }

    @Override
    public void onStaffChatEvent(StaffChatEvent<TString, TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return;

        String message = event.getRawMessage();
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                message = message.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }

        if (event.getIsConsole()) {
            webHookSender.sendConsoleWebhookMessage(
                registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
                message,
                registry.getOrDefault(CatalystKeys.DISCORD_STAFF_CHANNEL)
            );
            return;
        }


        String server = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
            ? serverService.getPrefixForPlayer(userService.getUserName((TUser) event.getPlayer()))
            : currentServerService.getName(userService.getUserName((TUser) event.getPlayer())).orElse("null");

        String name = registry.getOrDefault(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT)
            .replace("%server%", server)
            .replace("%player%", userService.getUserName((TUser) event.getPlayer()))
            .replace("%prefix%", luckPermsService.getPrefix(event.getPlayer()))
            .replace("%suffix%", luckPermsService.getSuffix(event.getPlayer()));
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            name,
            message,
            registry.getOrDefault(CatalystKeys.DISCORD_STAFF_CHANNEL),
            event.getPlayer()
        );
    }

    @Override
    public void onPlayerJoinEvent(JoinEvent<TPlayer> event) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return;

        String joinMessage = registry.getOrDefault(CatalystKeys.JOIN_FORMAT);
        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : emojiService.getEmojis().keySet()) {
                joinMessage = joinMessage.replace(emojiService.getEmojis().get(key).toString(), key);
            }
        }
        String server = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
            ? serverService.getPrefixForPlayer(userService.getUserName((TUser) event.getPlayer()))
            : currentServerService.getName(userService.getUserName((TUser) event.getPlayer())).orElse("null");

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

    @Override
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
            : currentServerService.getName(userService.getUserName((TUser) event.getPlayer())).orElse("null");
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
