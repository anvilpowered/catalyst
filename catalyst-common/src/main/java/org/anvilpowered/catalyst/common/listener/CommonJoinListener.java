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
import org.anvilpowered.anvil.api.misc.Named;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.slf4j.Logger;

import java.util.UUID;

public class CommonJoinListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource>
    implements JoinListener<TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TString> privateMessageService;

    @Inject
    private StaffListService<TString> staffListService;

    @Inject
    private BroadcastService<TString> broadcastService;

    @Inject
    private Logger logger;

    @Inject
    private AdvancedServerInfoService serverService;

    @Inject
    private LocationService locationService;

    @Inject
    private EmojiService emojiService;


    @Override
    public void onPlayerJoin(JoinEvent<TPlayer> event) {
        TPlayer player = event.getPlayer();
        UUID playerUUID = event.getPlayerUUID();
        String virtualHost = event.getHostString();

        if (permissionService.hasPermission(player,
            registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN_PERMISSION))) {
            privateMessageService.socialSpySet().add(playerUUID);
        }
        String userName = userService.getUserName((TUser) player);
        String server = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
            ? locationService.getServer(userName).map(Named::getName).orElse("null")
            : locationService.getServer(playerUUID).map(Named::getName).orElse("null");

        if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
            serverService.insertPlayer(userName, serverService.getPrefix(virtualHost));
            server = serverService.getPrefixForPlayer(userName);
        }

        staffListService.getStaffNames(
            userName,
            permissionService.hasPermission(
                player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN_PERMISSION)
            ),
            permissionService.hasPermission(
                player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF_PERMISSION)
            ),
            permissionService.hasPermission(
                player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER_PERMISSION)
            )
        );

        String joinMessage = registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)
            ? emojiService.toEmoji(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE), "&f")
            : registry.getOrDefault(CatalystKeys.JOIN_MESSAGE);

        broadcastService.broadcast(
            textService.deserialize(
                joinMessage
                    .replace("%player%", userName)
                    .replace("%server%", server)
            )
        );
        logger.info(
            textService.serializePlain(
                textService.of(
                    registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
                        .replace("%player%", userName)
                        .replace("%server%", server)
                )
            )
        );
    }
}
