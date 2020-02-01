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

public class ExceptionCommand implements Command {

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

        if (args.length == 0) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(commandUsage.exceptionAddCommandUsage);
            return;
        }

        if (!source.hasPermission(PluginPermissions.LANGUAGE_ADMIN)) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        switch (args[0]) {
            case "list": {
                source.sendMessage(TextComponent.of(String.join(", ", registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS))));
                return;
            }
            case "add": {
                if(registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS).isEmpty()) {
                    configurationService.addToCollection(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                    configurationService.save();
                    source.sendMessage(pluginMessages.getNewException(args[1]));
                } else if (registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                    source.sendMessage(pluginMessages.getExistingException(args[1]));
                } else {
                    configurationService.addToCollection(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                    configurationService.save();
                    source.sendMessage(pluginMessages.getNewException(args[1]));
                }
                return;
            }
            case "remove": {
                if (!registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                    source.sendMessage(pluginMessages.getMissingSwear(args[1]));
                } else {
                    configurationService.removeFromCollection(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                    configurationService.save();
                    source.sendMessage(pluginMessages.getRemoveException(args[1]));
                }
            }
        }
    }
}
