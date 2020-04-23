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

public class CommonChannelCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> {

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    private String channelId;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void execute(TCommandSource source, TSubject subject, String[] args) {
        if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION) + channelId)) {
            if (args.length == 0) {
                chatService.switchChannel(userService.getUUID((TPlayer) source), channelId);
                textService.send(textService.of("Switched channel to " + channelId), source);
            }
        } else {
            textService.send(pluginMessages.getNoPermission(), source);
        }
    }
}
