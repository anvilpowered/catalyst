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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.slf4j.Logger;

import java.util.Optional;

public class IgnoreCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private Logger logger;

    public int execute(CommandContext<TCommandSource> context, Class<?> playerClass) {
        Optional<TPlayer> targetPlayer = userService.getPlayer(context.getArgument("target", String.class));
        
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            logger.error("Player only command!");
            return 0;
        }

        if (!targetPlayer.isPresent()) {
            textService.send(pluginMessages.offlineOrInvalidPlayer(), context.getSource());
            return 0;
        }

        if (permissionService.hasPermission(targetPlayer.get(), registry.getOrDefault(CatalystKeys.IGNORE_EXEMPT_PERMISSION))) {
            textService.send(pluginMessages.ignoreExempt(), context.getSource());
            return 0;
        }

        textService.send(
            chatService.ignore(
                userService.getUUID((TPlayer) context.getSource()),
                userService.getUUID(targetPlayer.get())),
            context.getSource());
        return 1;
    }
}
