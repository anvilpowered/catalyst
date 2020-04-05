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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class CommonNickNameCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> {

    @Inject
    private MemberManager<TString> memberManager;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    public void execute(TCommandSource source, TSubject subject, String[] args) {
        String nick;
        if (!permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.NICKNAME))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.nickNameCommandUsage(), source);
            return;
        }

        if (args[0].equals("other") && permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.NICKNAME_OTHER))) {
            nick = args[2];
            memberManager.setNickNameForUser(args[1], nick).thenAcceptAsync(m -> textService.send(m, source));
            return;
        } else {
            nick = args[0];
        }

        if (nick.contains("&")) {
            if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.NICKNAME_COLOR))) {
                if (nick.contains("&k")
                    && permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.NICKNAME_MAGIC))) {
                    memberManager.setNickName(userService.getUserName((TPlayer) source), nick).thenAcceptAsync(m -> textService.send(m, source));
                } else {
                    textService.send(pluginMessages.getNoNickMagicPermission(), source);
                    return;
                }
                memberManager.setNickName(userService.getUserName((TPlayer) source), nick).thenAcceptAsync(m -> textService.send(m, source));
            } else {
                textService.send(pluginMessages.getNoNickColorPermission(), source);
            }
        } else {
            memberManager.setNickName(userService.getUserName((TPlayer) source), nick).thenAcceptAsync(m -> textService.send(m, source));
        }
    }
}
