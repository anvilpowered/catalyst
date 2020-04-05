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
import org.anvilpowered.anvil.api.util.KickService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class CommonKickCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private KickService kickService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    public void execute(TCommandSource source, TSubject subject, String[] args) {
        String reason = "You have been kicked!";

        if (!permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.KICK))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.kickCommandUsage(), source);
            return;
        }

        if (args.length > 1) {
            reason = args[1];
        }

        if (userService.get(args[0]).isPresent()) {
            if (permissionService.hasPermission((TSubject) userService.getPlayer(args[0]), registry.getOrDefault(CatalystKeys.KICK_EXEMPT))) {
                textService.send(pluginMessages.getKickExempt(), source);
                return;
            }
            kickService.kick(args[0], reason);
        } else {
            textService.send(pluginMessages.offlineOrInvalidPlayer(), source);
        }
    }
}
