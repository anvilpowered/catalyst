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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffListService;

import java.util.UUID;

public class CommonJoinListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource,
    TSubject,
    TEvent>
    implements JoinListener<TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TString> privateMessageService;

    @Inject
    private LuckpermsService<TPlayer> luckpermsService;

    @Inject
    private StaffListService<TString> staffListService;

    @Inject
    private BroadcastService<TString> broadcastService;

    @Inject
    private LoggerService<TString> loggerService;

    @Inject
    private JoinEvent<TPlayer> joinEvent;

    @Inject
    private EventService<TEvent> eventService;

    @Inject
    private DiscordChatListener<TString, TPlayer> discordChatListener;

    @Inject
    private AdvancedServerInfoService serverService;

    @Inject
    private CurrentServerService currentServerService;

    @Override
    public void onPlayerJoin(TPlayer player, UUID playerUUID, String virtualHost) {
        if (permissionService.hasPermission((TSubject) player,
            registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN_PERMISSION))) {
            privateMessageService.socialSpySet().add(playerUUID);
        }
        luckpermsService.addPlayerToCache(player);
        String userName = userService.getUserName((TUser) player);
        String server = currentServerService.getName(userName).orElse("null");

        if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
            serverService.insertPlayer(userName, serverService.getPrefix(virtualHost));
            server = serverService.getPrefixForPlayer(userName);
        }

        staffListService.getStaffNames(
            userName,
            permissionService.hasPermission(
                (TSubject) player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN_PERMISSION)
            ),
            permissionService.hasPermission(
                (TSubject) player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF_PERMISSION)
            ),
            permissionService.hasPermission(
                (TSubject) player,
                registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER_PERMISSION)
            )
        );
        broadcastService.broadcast(
            textService.deserialize(
                registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
                    .replace("%player%", userName)
                    .replace("%server%", server)
            )
        );
        loggerService.info(
            textService.deserialize(
                registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
                    .replace("%player%", userName)
                    .replace("%server%", server)
            )
        );
        joinEvent.setPlayer(player);
        eventService.fire((TEvent) joinEvent);
        discordChatListener.onPlayerJoinEvent(joinEvent);
    }
}
