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
import org.anvilpowered.catalyst.api.service.ChatService;

public class CommonListCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

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

    public void execute(TCommandSource source, String[] args) {
        if (!permissionService.hasPermission(source,
            registry.getOrDefault(CatalystKeys.LIST_PERMISSION))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }
        if (userService.getOnlinePlayers().size() == 0) {
            textService.send(textService.of("There are no players online!"), source);
            return;
        }
        chatService.sendList(source);
    }
}
