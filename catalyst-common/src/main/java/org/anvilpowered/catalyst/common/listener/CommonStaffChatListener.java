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

package org.anvilpowered.catalyst.common.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.StaffChatListener;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.slf4j.Logger;

public class CommonStaffChatListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource>
    implements StaffChatListener<TString, TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Logger logger;

    @Inject
    private EmojiService emojiService;

    @Override
    public void onStaffChatEvent(StaffChatEvent<TString, TPlayer> event) {
        String message = event.getRawMessage();
        TUser player = (TUser) event.getPlayer();
        String userName = userService.getUserName(player);

        if (event.getIsConsole()) {
            String finalMessage = message;
            userService.getOnlinePlayers().forEach(p -> {
                if (permissionService.hasPermission(p, registry.getOrDefault(CatalystKeys.STAFFCHAT_PERMISSION))) {
                    textService.send(pluginMessages.getStaffChatMessageFormatted("Console",
                        textService.deserialize(finalMessage)), (TCommandSource) p);
                }
            });
            logger.info("[STAFF] Console: " + message);
            return;
        }

        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)
            && permissionService.hasPermission(player,
            registry.getOrDefault(CatalystKeys.EMOJI_PERMISSION))) {
            message = emojiService.toEmoji(message, "&d");
        }

        String finalMessage = message;
        userService.getOnlinePlayers().forEach(p -> {
            if (permissionService.hasPermission(p, registry.getOrDefault(CatalystKeys.STAFFCHAT_PERMISSION))) {
                textService.send(
                    pluginMessages.getStaffChatMessageFormatted(userName, textService.deserialize(finalMessage)),
                    (TCommandSource) p
                );
            }
        });
        logger.info("[STAFF] " + userName + " : " + finalMessage);
    }
}
