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

package org.anvilpowered.catalyst.bungee.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.plugin.Plugin;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.base.plugin.BasePlugin;
import org.anvilpowered.catalyst.bungee.listener.BungeeListener;
import org.anvilpowered.catalyst.bungee.module.BungeeModule;
import org.anvilpowered.catalyst.bungee.utils.LuckPermsUtils;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;

public class CatalystBungee extends Plugin
    implements org.anvilpowered.anvil.api.plugin.Plugin<Plugin> {

    @Inject
    private java.util.logging.Logger logger;

    private final Inner inner;
    public static CatalystBungee plugin;

    public CatalystBungee() {
        plugin = this;
        AbstractModule module = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Plugin.class).toInstance(CatalystBungee.this);
            }
        };
        Injector injector = Guice.createInjector(module);
        inner = injector.getInstance(Inner.class);
    }

    public static class Inner extends BasePlugin<Plugin> {
        @Inject
        public Inner(Injector injector) {
            super(CatalystPluginInfo.id,
                injector,
                new BungeeModule(),
                LuckPermsUtils.class
            );
        }

        @Override
        protected void whenReady(Environment environment) {
            System.out.println("Injecting listeners");
            super.whenReady(environment);
        }
    }

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this,
            inner.getPrimaryEnvironment().getInjector().getInstance(BungeeListener.class)
        );
    }

    @Override
    public Plugin getPluginContainer() {
        return this;
    }

    @Override
    public int compareTo(org.anvilpowered.anvil.api.plugin.Plugin<Plugin> o) {
        return inner.compareTo(o);
    }

    @Override
    public String getName() {
        return CatalystPluginInfo.name;
    }
}
