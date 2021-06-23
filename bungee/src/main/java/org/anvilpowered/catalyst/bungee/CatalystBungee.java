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

package org.anvilpowered.catalyst.bungee;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.catalyst.api.CatalystImpl;
import org.anvilpowered.catalyst.bungee.listener.BungeeListener;
import org.anvilpowered.catalyst.bungee.module.BungeeModule;
import org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher;

public class CatalystBungee extends Plugin {

    private final Inner inner;

    public CatalystBungee() {
        AbstractModule module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Plugin.class).toInstance(CatalystBungee.this);
                bind(CatalystBungee.class).toInstance(CatalystBungee.this);
            }
        };
        Injector injector = Guice.createInjector(module);
        inner = new Inner(injector);
    }

    public final class Inner extends CatalystImpl {
        public Inner(Injector rootInjector) {
            super(rootInjector, new BungeeModule());
        }

        @Override
        protected void applyToBuilder(Environment.Builder builder) {
            super.applyToBuilder(builder);
            builder.addEarlyServices(BungeeCommandDispatcher.class);
            builder.addEarlyServices(BungeeListener.class, t ->
                getProxy().getPluginManager().registerListener(CatalystBungee.this, t)
            );
        }
    }
}
