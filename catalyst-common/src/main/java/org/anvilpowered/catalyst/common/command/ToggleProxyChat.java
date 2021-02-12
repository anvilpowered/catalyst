/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.ChatService;

public class ToggleProxyChat<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private TextService<TString, TCommandSource> textService;

    public int execute(CommandContext<TCommandSource> context, Class<?> playerClass) {
        if (!permissionService.hasPermission(context.getSource(),
            registry.getOrDefault(CatalystKeys.TOGGLE_CHAT_PERMISSION))) {
            textService.send(pluginMessages.getNoPermission(), context.getSource());
            return 0;
        }
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            textService.send(textService.of("Player only command!"), context.getSource());
            return 0;
        }
        TPlayer player = (TPlayer) context.getSource();

        if (chatService.isDisabledForUser(player)) {
            chatService.toggleChatForUser(player);
            sendToggle(context.getSource(), true);
            return 1;
        }
        chatService.toggleChatForUser(player);
        sendToggle(context.getSource(), false);
        return 1;
    }

    private void sendToggle(TCommandSource source, boolean enabled) {
        String setting = enabled ? "enabled" : "disabled";
        textService.builder()
            .appendPrefix()
            .green().append("Proxy-Wide chat has been ")
            .gold().append(setting)
            .sendTo(source);
        textService.builder()
            .yellow().append("Note: This only effects YOU")
            .sendTo(source);
    }
}
