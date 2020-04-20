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

package org.anvilpowered.catalyst.velocity.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.catalyst.api.service.ServerInfoService;
import org.anvilpowered.catalyst.common.plugin.Catalyst;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;
import org.anvilpowered.catalyst.velocity.command.CatalystCommandManager;
import org.anvilpowered.catalyst.velocity.listener.VelocityListener;
import org.anvilpowered.catalyst.velocity.module.VelocityModule;
import org.anvilpowered.catalyst.velocity.service.VelocityJDAService;
import org.anvilpowered.catalyst.velocity.service.VelocityLuckpermsService;
import org.anvilpowered.catalyst.velocity.tab.GlobalTab;
import org.slf4j.Logger;


@Plugin(
    id = CatalystPluginInfo.id,
    name = CatalystPluginInfo.name,
    version = CatalystPluginInfo.version,
    authors = {"STG_Allen", "Cableguy20"},
    description = CatalystPluginInfo.description,
    url = CatalystPluginInfo.url,
    dependencies = {
        @Dependency(id = "anvil"),
        @Dependency(id = "luckperms"),
        @Dependency(id = "votlin")
    }
)
public class CatalystVelocity extends Catalyst<PluginContainer> {

    @Inject
    Logger logger;
    boolean alreadyLoaded = false;
    @Inject
    private ProxyServer proxyServer;

    @Inject
    public CatalystVelocity(Injector injector) {
        super(injector,
            new VelocityModule(),
            GlobalTab.class,
            CatalystCommandManager.class,
            VelocityJDAService.class,
            VelocityLuckpermsService.class,
            ServerInfoService.class
        );
    }

    @Override
    protected void whenLoaded(Environment environment) {
        super.whenLoaded(environment);
        logger.info("Injecting listeners");
        if (!alreadyLoaded) {
            alreadyLoaded = true;
            proxyServer.getEventManager().register(
                this,
                environment.getInjector().getInstance(VelocityListener.class)
            );
        }
    }
}
