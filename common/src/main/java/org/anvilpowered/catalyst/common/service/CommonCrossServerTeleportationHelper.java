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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.redis.RedisService;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.catalyst.api.service.CrossServerTeleportationHelper;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class CommonCrossServerTeleportationHelper<TCommandSource> implements CrossServerTeleportationHelper {

    @Inject
    private CurrentServerService currentServerService;

    @Inject
    private RedisService redisService;

    @Inject
    private ExecuteCommandService<TCommandSource> executeCommandService;

    public Map<String, String> teleportationAcceptMap = new HashMap<>();

    @Override
    public void teleport(String sender, String recipient) {
        if (currentServerService.getName(sender).isPresent() && currentServerService.getName(recipient).isPresent()) {
            if (isOnSameServer(sender, recipient)) {
                sendTeleportMessage(currentServerService.getName(sender).get(), sender, recipient);
            } else {
                sendTeleportMessage(currentServerService.getName(recipient).get(), sender, recipient);
                executeCommandService.executeAsConsole("send " + sender + " " + currentServerService.getName(recipient).get());
            }
        }
    }

    @Override
    public boolean isOnSameServer(String sender, String recipient) {
        if (currentServerService.getName(sender).isPresent() && currentServerService.getName(recipient).isPresent()) {
            return currentServerService.getName(sender).get().equalsIgnoreCase(currentServerService.getName(recipient).get());
        }
        return false;
    }

    @Override
    public void sendTeleportMessage(String serverName, String sender, String recipient) {
        CompletableFuture.runAsync(() -> {
            redisService.getJedisPool().getResource().publish(serverName, "Teleport " + sender + " " + recipient);
        });
    }

    @Override
    public void insertIntoTeleportationMap(String sender, String recipient) {
        teleportationAcceptMap.put(sender, recipient);
    }

    @Override
    public String getSender(String recipient) {
        AtomicReference<String> s = new AtomicReference<>("null");
        teleportationAcceptMap.forEach((k, v) -> {
            if (v.equalsIgnoreCase(recipient)) {
                s.set(v);
            }
        });
        return s.get();
    }

    @Override
    public String getRecipient(String sender) {
        return teleportationAcceptMap.get(sender);
    }

    @Override
    public boolean isPresentInMap(String sender) {
        return teleportationAcceptMap.containsKey(sender);
    }
}
