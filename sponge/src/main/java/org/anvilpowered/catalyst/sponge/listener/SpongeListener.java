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

package org.anvilpowered.catalyst.sponge.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.plugin.Plugin;
import org.anvilpowered.catalyst.sponge.service.SpongeTeleportationService;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class SpongeListener {

    @Inject
    private SpongeTeleportationService service;

    @Inject
    private Plugin<?> plugin;

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        Task.builder()
            .execute(() -> {
                if (service.teleportationMap.containsKey(player.getName())) {
                    service.teleport(player.getName(), service.teleportationMap.get(player.getName()), true);
                }
            })
            .delay(10, TimeUnit.SECONDS)
            .submit(plugin);
    }

    @Listener
    public void onPlayerLeave(ClientConnectionEvent.Disconnect event) {
        Player player = event.getTargetEntity();
        service.teleportationMap.remove(player.getName());
    }
}
