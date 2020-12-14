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
import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.coremember.CoreMemberManager;
import org.anvilpowered.anvil.api.model.coremember.CoreMember;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class NickNameCommand<
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

    public int execute(CommandContext<TCommandSource> context, Class<?> playerClass) {
        String nick = context.getArgument("nickname", String.class);
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            textService.send(textService.of("Player only command!"), context.getSource());
            return 1;
        }

        if (nick.contains("&")) {
            if (permissionService.hasPermission(context.getSource(),
                registry.getOrDefault(CatalystKeys.NICKNAME_COLOR_PERMISSION))) {
                if ((!permissionService.hasPermission(context.getSource(),
                    registry.getOrDefault(CatalystKeys.NICKNAME_MAGIC_PERMISSION)) && nick.contains("&k"))) {
                    textService.send(pluginMessages.getNoNickMagicPermission(), context.getSource());
                    nick = nick.replaceAll("&k", "");
                }
            } else {
                nick = textService.serializePlain(textService.of(nick));
                textService.send(pluginMessages.getNoNickColorPermission(), context.getSource());
            }
        }
        memberManager.setNickName(userService.getUserName((TPlayer) context.getSource()), nick)
            .thenAcceptAsync(m -> textService.send(m, context.getSource()));
        return 1;
    }

    public int executeOther(CommandContext<TCommandSource> context) {
        memberManager.setNickNameForUser(context.getArgument("target", String.class),
            context.getArgument("targetnick", String.class)).thenAcceptAsync(m ->
            textService.send(m, context.getSource()));
        return 1;
    }
}

