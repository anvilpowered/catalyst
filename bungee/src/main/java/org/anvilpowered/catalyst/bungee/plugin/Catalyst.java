package org.anvilpowered.catalyst.bungee.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.anvilpowered.anvil.base.plugin.BasePlugin;
import org.anvilpowered.catalyst.bungee.listeners.PlayerListener;
import org.anvilpowered.catalyst.bungee.module.BungeeModule;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;

public class Catalyst extends BasePlugin<PluginDescription> {

    private Injector injector = null;

    @Inject
    public Catalyst(Injector injector) {
        super(CatalystPluginInfo.id, injector, new BungeeModule(), PlayerListener.class);
    }
}
