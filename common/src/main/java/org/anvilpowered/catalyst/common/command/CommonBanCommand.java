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


public class CommonBanCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> {

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private MemberManager<TString> memberManager;

    @Inject
    private Registry registry;


    public void execute(TCommandSource source, TSubject subject, String[] args) {
        if (!permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.BAN))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.banCommandUsage(), source);
            return;
        }
        String userName = args[0];
        userService.getPlayer(userName).ifPresent(p -> {
            if (permissionService.hasPermission((TSubject) p, registry.getOrDefault(CatalystKeys.BAN_EXEMPT))) {
                textService.send(pluginMessages.getBanExempt(), source);
                return;
            }
            if (args.length == 1) {
                memberManager.ban(userName).thenAcceptAsync(m -> textService.send(m, source));
            } else {
                String reason = String.join(" ", args).replace(userName + " ", " ");
                memberManager.ban(userName, reason).thenAcceptAsync(m -> textService.send(m, source));
            }
        });
    }
}
