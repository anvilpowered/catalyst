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

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.base.plugin.BasePlugin;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;
import org.anvilpowered.catalyst.velocity.commands.CatalystCommandManager;
import org.anvilpowered.catalyst.velocity.discord.JDAHook;
import org.anvilpowered.catalyst.velocity.discord.ProxyListener;
import org.anvilpowered.catalyst.velocity.listeners.*;
import org.anvilpowered.catalyst.velocity.module.VelocityModule;
import org.anvilpowered.catalyst.velocity.tab.GlobalTab;
import org.anvilpowered.catalyst.velocity.tab.TabUtils;
import org.slf4j.Logger;


@Plugin(
    id = CatalystPluginInfo.id,
    name = CatalystPluginInfo.name,
    version = CatalystPluginInfo.version,
    authors = {"STG_Allen", "Cableguy20"},
    description = CatalystPluginInfo.description,
    url = CatalystPluginInfo.url,
    dependencies = @Dependency(id = "anvil")
)
public class Catalyst extends BasePlugin<PluginContainer> {

    @Inject
    Logger logger;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private TabUtils tabUtils;

    public static ProxyServer server;

    public static Catalyst plugin = null;

    private Injector injector = null;
    public static LuckPerms api;

    @Inject
    public Catalyst(Injector injector) {
        super(CatalystPluginInfo.id, injector, new VelocityModule(), GlobalTab.class, CatalystCommandManager.class, JDAHook.class);
    }

    @Subscribe(order = PostOrder.LAST)
    public void onInit(ProxyInitializeEvent event) {
        server = proxyServer;
        plugin = this;
        api = LuckPermsProvider.get();
    }

    public static ProxyServer getServer() {
        return server;
    }

    @Override
    protected void whenReady(Environment environment) {
        super.whenReady(environment);
        logger.info("Injecting listeners");
        proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyJoinListener.class));
        proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyLeaveListener.class));
        proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyChatListener.class));
        proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyStaffChatListener.class));
        //proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyPingEventListener.class));
        proxyServer.getEventManager().register(this, environment.getInjector().getInstance(ProxyListener.class));
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(new LegacyChannelIdentifier("GlobalTab"))) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.endsWith("Balance")) {
            String[] packet = in.readUTF().split(":");
            String username = packet[0];
            Double balance = Double.parseDouble(packet[1]);
            if (tabUtils.playerBalances.containsKey(username)) {
                tabUtils.playerBalances.replace(username, balance);
            } else {
                tabUtils.playerBalances.put(username, balance);
            }
        }
    }
}
