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
import org.anvilpowered.anvil.api.util.KickService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

import java.util.Optional;

public class KickCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private KickService kickService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    public int execute(CommandContext<TCommandSource> context) {
        Optional<TPlayer> player = userService.getPlayer(context.getArgument("target", String.class));
        if (player.isPresent()) {
            if (permissionService.hasPermission(
                player.get(),
                registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION))) {
                textService.send(pluginMessages.getKickExempt(), context.getSource());
                return 0;
            }
            kickService.kick(
                context.getArgument("target", String.class),
                context.getArgument("reason", String.class)
            );
        } else {
            textService.send(pluginMessages.offlineOrInvalidPlayer(), context.getSource());
        }
        return 1;
    }

    public int withoutReason(CommandContext<TCommandSource> context) {
        String reason = "You have been kicked!";
        Optional<TPlayer> player = userService.getPlayer(context.getArgument("target", String.class));
        if (player.isPresent()) {
            if (permissionService.hasPermission(
                player.get(),
                registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION))) {
                textService.send(pluginMessages.getKickExempt(), context.getSource());
                return 0;
            }
            kickService.kick(context.getArgument("target", String.class), reason);
        } else {
            textService.send(pluginMessages.offlineOrInvalidPlayer(), context.getSource());
        }
        return 1;
    }
}
