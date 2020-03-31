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
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;

public class CommonMessageCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TString> privateMessageService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PermissionService<TSubject> permissionService;

    public void execute(TCommandSource source, TSubject subject, String[] args, boolean isConsole) {
        String name;
        if (args.length < 1) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.messageCommandUsage(), source);
            return;
        }

        if (userService.getPlayer(args[0]).isPresent()) {
            name = args[0];
            args[0] = "";
            String message = String.join(" ", args);

            if (isConsole) {
                privateMessageService.sendMessage("Me", name, message);
                privateMessageService.sendMessage("Console", "Me", message);
            }
            if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.MESSAGE))) {
                privateMessageService.sendMessage(
                    userService.getUserName((TPlayer) source),
                    name,
                    message
                );
                privateMessageService.replyMap().put(
                    userService.getUUID(userService.getPlayer(name).get()),
                    userService.getUUID((TPlayer) source)
                );
            }
        }
    }
}
