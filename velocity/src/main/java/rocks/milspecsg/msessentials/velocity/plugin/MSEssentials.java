/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.velocity.plugin;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;
import rocks.milspecsg.msessentials.common.plugin.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.velocity.commands.MSEssentialsCommandManager;
import rocks.milspecsg.msessentials.velocity.listeners.ProxyChatListener;
import rocks.milspecsg.msessentials.velocity.listeners.ProxyJoinListener;
import rocks.milspecsg.msessentials.velocity.listeners.ProxyLeaveListener;
import rocks.milspecsg.msessentials.velocity.listeners.ProxyPingEventListener;
import rocks.milspecsg.msessentials.velocity.listeners.ProxyStaffChatListener;
import rocks.milspecsg.msessentials.velocity.module.VelocityModule;
import rocks.milspecsg.msessentials.velocity.tab.GlobalTab;
import rocks.milspecsg.msessentials.velocity.tab.TabUtils;
import rocks.milspecsg.anvil.api.Anvil;
import rocks.milspecsg.anvil.velocity.module.ApiVelocityModule;


@Plugin(
    id = MSEssentialsPluginInfo.id,
    name = MSEssentialsPluginInfo.name,
    version = MSEssentialsPluginInfo.version,
    authors = {"STG_Allen", "Cableguy20"},
    description = MSEssentialsPluginInfo.description,
    url = MSEssentialsPluginInfo.url,
    dependencies = {
        @Dependency(id = "mscore"),
    }
)
public class MSEssentials implements rocks.milspecsg.anvil.api.plugin.Plugin<PluginDescription> {

    @Override
    public String toString() {
        return MSEssentialsPluginInfo.id;
    }

    @Inject
    Logger logger;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private TabUtils tabUtils;

    @Inject
    private PluginDescription pluginDescription;

    public static ProxyServer server;

    public static MSEssentials plugin = null;
    private Injector injector = null;

    public static LuckPerms api;

    @Inject
    public MSEssentials(Injector injector) {
        plugin = this;
        Anvil.environmentBuilder()
            .setName(MSEssentialsPluginInfo.id)
            .addModules(new ApiVelocityModule(), new VelocityModule())
            .setRootInjector(injector)
            .whenReady(e -> this.injector = e.getInjector())
            .whenReady(e -> api = LuckPermsProvider.get())
            .addEarlyServices(GlobalTab.class, MSEssentialsCommandManager.class)
            .register(this);
    }

    @Override
    public String getName() {
        return MSEssentialsPluginInfo.id;
    }

    @Override
    public PluginDescription getPluginContainer() {
        return pluginDescription;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        initListeners();
        server = proxyServer;
    }

    public static ProxyServer getServer() {
        return server;
    }

    private void initListeners() {
        logger.info("Injecting listeners");
        proxyServer.getEventManager().register(this, injector.getInstance(ProxyJoinListener.class));
        proxyServer.getEventManager().register(this, injector.getInstance(ProxyLeaveListener.class));
        proxyServer.getEventManager().register(this, injector.getInstance(ProxyChatListener.class));
        proxyServer.getEventManager().register(this, injector.getInstance(ProxyStaffChatListener.class));
        proxyServer.getEventManager().register(this, injector.getInstance(ProxyPingEventListener.class));
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
