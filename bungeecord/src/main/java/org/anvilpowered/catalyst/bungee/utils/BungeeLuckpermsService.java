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

package org.anvilpowered.catalyst.bungee.utils;

import com.google.inject.Inject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;

import java.util.concurrent.TimeUnit;

public class BungeeLuckpermsService {

    @Inject
    private LuckpermsService<ProxiedPlayer> luckpermsService;

    private Registry registry;

    @Inject
    public BungeeLuckpermsService(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::syncPlayerData);
    }

    public void syncPlayerData() {
        CatalystBungee.plugin.getLogger().info("Starting Luckperms Sync Service");
        ProxyServer.getInstance().getScheduler().schedule(CatalystBungee.plugin,
            luckpermsService.syncPlayerCache(), 15, TimeUnit.SECONDS);
    }
}
