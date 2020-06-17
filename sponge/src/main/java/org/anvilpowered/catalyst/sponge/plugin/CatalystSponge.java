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

package org.anvilpowered.catalyst.sponge.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.catalyst.common.plugin.Catalyst;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;
import org.anvilpowered.catalyst.sponge.command.CatalystSpongeCommandNode;
import org.anvilpowered.catalyst.sponge.listener.SpongeListener;
import org.anvilpowered.catalyst.sponge.module.SpongeModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
    id = CatalystPluginInfo.id,
    name = CatalystPluginInfo.name,
    version = CatalystPluginInfo.version,
    description = CatalystPluginInfo.description,
    authors = {"STG_Allen"},
    dependencies = @Dependency(id = "anvil")
)
public class CatalystSponge extends Catalyst<PluginContainer> {

    @Inject
    public CatalystSponge(Injector rootInjector) {
        super(rootInjector, new SpongeModule(), CatalystSpongeCommandNode.class);
    }

    @Override
    protected void whenReady(Environment environment) {
        super.whenReady(environment);
    }

    @Override
    protected void applyToBuilder(Environment.Builder builder) {
        builder.addEarlyServices(SpongeListener.class, t ->
            Sponge.getEventManager().registerListeners(this, t));
    }
}
