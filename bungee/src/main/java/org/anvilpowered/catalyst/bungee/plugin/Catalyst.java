package org.anvilpowered.catalyst.bungee.plugin;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.anvilpowered.anvil.base.plugin.BasePlugin;
import org.anvilpowered.catalyst.bungee.commands.InfoCommand;
import org.anvilpowered.catalyst.bungee.listeners.PlayerListener;
import org.anvilpowered.catalyst.bungee.module.BungeeModule;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;

public class Catalyst extends Plugin implements org.anvilpowered.anvil.api.plugin.Plugin<Catalyst> {

    protected final InnerCatalyst innerCatalyst;

    public Catalyst() {
        innerCatalyst = new InnerCatalyst();
    }

    @Override
    public String getName() {
        return CatalystPluginInfo.name;
    }

    @Override
    public Catalyst getPluginContainer() {
        return this;
    }

    private static final class InnerCatalyst extends BasePlugin<PluginDescription> {
        protected InnerCatalyst() {
            super(CatalystPluginInfo.id, null, new BungeeModule());
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading Catalyst for Bungee!");
        getProxy().getPluginManager().registerListener(this, innerCatalyst.getPrimaryEnvironment().getInjector().getInstance(PlayerListener.class));
        getProxy().getPluginManager().registerCommand(this, innerCatalyst.getPrimaryEnvironment().getInjector().getInstance(InfoCommand.class));

    }
}
