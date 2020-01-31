package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.data.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;

public class SwearCommand implements Command {

    @Inject
    private CommandUsageMessages commandUsage;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private ConfigurationService configurationService;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (args.length < 1) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(commandUsage.swearAddCommandUsage);
            return;
        }

        if (!source.hasPermission(PluginPermissions.LANGUAGE_ADMIN)) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        switch (args[0]) {
            case "list": {
                source.sendMessage(TextComponent.of(String.join(", ", registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_SWEARS))));
                return;
            }
            case "add": {
                if (registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_SWEARS).contains(args[1])) {
                    source.sendMessage(pluginMessages.getExistingSwear(args[1]));
                } else {
                    configurationService.add(MSEssentialsKeys.CHAT_FILTER_SWEARS, args[1]);
                    configurationService.save();
                    source.sendMessage(pluginMessages.getNewSwear(args[1]));
                }
                return;
            }
            case "remove": {
                if (!registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_SWEARS).contains(args[1])) {
                    source.sendMessage(pluginMessages.getMissingSwear(args[1]));
                } else {
                    configurationService.remove(MSEssentialsKeys.CHAT_FILTER_SWEARS, args[1]);
                    configurationService.save();
                    source.sendMessage(pluginMessages.getRemoveSwear(args[1]));
                }
            }
        }
    }
}
