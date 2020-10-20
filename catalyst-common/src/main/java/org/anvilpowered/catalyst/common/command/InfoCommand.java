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
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class InfoCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private MemberManager<TString> memberManager;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    public int execute(CommandContext<TCommandSource> context) {
        if (!permissionService.hasPermission(context.getSource(),
            registry.getOrDefault(CatalystKeys.INFO_PERMISSION))) {
            textService.send(pluginMessages.getNoPermission(), context.getSource());
            return 0;
        }

        boolean isActive = userService.get(context.getArgument("target", String.class)).isPresent();
        boolean[] permissions = new boolean[3];
        permissions[0] = permissionService.hasPermission(context.getSource(),
            registry.getOrDefault(CatalystKeys.INFO_IP_PERMISSION));
        permissions[1] = permissionService.hasPermission(context.getSource(),
            registry.getOrDefault(CatalystKeys.INFO_BANNED_PERMISSION));
        permissions[2] = permissionService.hasPermission(context.getSource(),
            registry.getOrDefault(CatalystKeys.INFO_CHANNEL_PERMISSION));
        memberManager.info(
            context.getArgument("target", String.class),
            isActive,
            permissions)
            .thenAcceptAsync(m -> textService.send(m, context.getSource()));
        return 1;
    }
}
