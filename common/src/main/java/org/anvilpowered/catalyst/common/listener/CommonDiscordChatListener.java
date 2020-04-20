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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.discord.WebhookSender;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.ServerInfoService;

public class CommonDiscordChatListener<TUser, TString, TPlayer> implements DiscordChatListener<TString, TPlayer> {

    @Inject
    private Registry registry;

    @Inject
    private LuckpermsService<TPlayer> luckpermsService;

    @Inject
    private WebhookSender<TPlayer> webhookSender;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private ServerInfoService serverService;

    @Inject
    private CurrentServerService currentServerService;

    @Override
    public void onChatEvent(ChatEvent<TString, TPlayer> event) {
        String message = event.getRawMessage();
        String name;
        if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
            name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT)
                .replace("%server%", serverService.getPrefixForPlayer(pluginMessages.removeColor(userService.getUserName((TUser) event.getSender()))))
                .replace("%player%", userService.getUserName((TUser) event.getSender()))
                .replace("%prefix%", luckpermsService.getPrefix(event.getSender()))
                .replace("%suffix%", luckpermsService.getSuffix(event.getSender()));
        } else {
            name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT)
                .replace("%server%", currentServerService.getName(userService.getUUID((TUser) event.getSender())).get())
                .replace("%player%", userService.getUserName((TUser) event.getSender()))
                .replace("%prefix%", luckpermsService.getPrefix(event.getSender()))
                .replace("%suffix%", luckpermsService.getSuffix(event.getSender()));
        }
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            webhookSender.sendWebhookMessage(
                registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
                name,
                message,
                registry.getOrDefault(CatalystKeys.MAIN_CHANNEL),
                event.getSender()
            );
        }
    }

    @Override
    public void onStaffChatEvent(StaffChatEvent<TString, TPlayer> event) {
        String message = event.getRawMessage();
        String name;
        if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
            name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT)
                .replace("%server%", serverService.getPrefixForPlayer(pluginMessages.removeColor(userService.getUserName((TUser) event.getSender()))))
                .replace("%player%", userService.getUserName((TUser) event.getSender()))
                .replace("%prefix%", luckpermsService.getPrefix(event.getSender()))
                .replace("%suffix%", luckpermsService.getSuffix(event.getSender()));
        } else {
            name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT)
                .replace("%server%", currentServerService.getName(userService.getUUID((TUser) event.getSender())).get())
                .replace("%player%", userService.getUserName((TUser) event.getSender()))
                .replace("%prefix%", luckpermsService.getPrefix(event.getSender()))
                .replace("%suffix%", luckpermsService.getSuffix(event.getSender()));
        }
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            webhookSender.sendWebhookMessage(
                registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
                name,
                message,
                registry.getOrDefault(CatalystKeys.STAFF_CHANNEL),
                event.getSender()
            );
        }
    }

    @Override
    public void onPlayerJoinEvent(JoinEvent<TPlayer> event) {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            webhookSender.sendWebhookMessage(
                registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
                registry.getOrDefault(CatalystKeys.BOT_NAME),
                registry.getOrDefault(CatalystKeys.JOIN_FORMAT).replace(
                    "%player%",
                    userService.getUserName((TUser) event.getPlayer())
                ),
                registry.getOrDefault(CatalystKeys.MAIN_CHANNEL),
                event.getPlayer()
            );
        }
    }

    @Override
    public void onPlayerLeaveEvent(LeaveEvent<TPlayer> event) {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            webhookSender.sendWebhookMessage(
                registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
                registry.getOrDefault(CatalystKeys.BOT_NAME),
                registry.getOrDefault(CatalystKeys.LEAVE_FORMAT).replace(
                    "%player%",
                    userService.getUserName((TUser) event.getPlayer())
                ),
                registry.getOrDefault(CatalystKeys.MAIN_CHANNEL),
                event.getPlayer()
            );
        }
    }
}
