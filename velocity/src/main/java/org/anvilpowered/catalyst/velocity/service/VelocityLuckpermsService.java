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

package org.anvilpowered.catalyst.velocity.service;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;

import java.util.concurrent.TimeUnit;

public class VelocityLuckpermsService {


    @Inject
    private LuckpermsService<Player> luckpermsService;

    @Inject
    private EventService eventService;

    private Registry registry;

    @Inject
    public VelocityLuckpermsService(Registry registry) {
        this.registry = registry;
        this.registry.whenLoaded(this::startLuckpermsScheduler);
    }

    private void startLuckpermsScheduler() {
        eventService.schedule(
            luckpermsService.syncPlayerCache(),
            TimeUnit.SECONDS,
            10
        );
    }
}
