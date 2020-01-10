package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.api.config.ConfigTypes;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class SwearAddCommand implements Command {
    @Inject
    private ConfigurationService configurationService;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!(source.hasPermission(PluginPermissions.LANGUAGE_ADMIN) || source.hasPermission(PluginPermissions.LANGUAGE_SWEAR_ADD))) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length < 1) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.swearAddCommandUsage);
            return;
        }

            List<String> swearList = new ArrayList<>(configurationService.getConfigList(ConfigKeys.CHAT_FILTER_SWEARS, ConfigTypes.STRINGLIST));

            if(swearList.contains(args[0])) {
                source.sendMessage(pluginMessages.existingSwear(args[0]));
            } else {
                swearList.add(args[0]);
                configurationService.setConfigList(ConfigKeys.CHAT_FILTER_SWEARS, swearList);
                configurationService.save();
                source.sendMessage(pluginMessages.addSwear(args[0]));
            }
        }
}
