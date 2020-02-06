package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.utils.CommandUtils;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.plugin.PluginInfo;

public class MSEssentialsCommand implements Command {

    @Inject
    private CommandUtils commandUtils;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        switch (args[0]) {
            case "info": {
                commandUtils.createPluginInfoPage(source, true);
                return;
            }
            case "reload": {
                if (source.hasPermission(PluginPermissions.RELOAD)) {
                    source.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Reloading")));
                    registry.load();
                } else {
                    source.sendMessage(pluginMessages.getNoPermission());
                }
            }
        }
    }
}
