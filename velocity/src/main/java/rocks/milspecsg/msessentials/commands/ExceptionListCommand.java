package rocks.milspecsg.msessentials.commands;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.api.config.ConfigTypes;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class ExceptionListCommand implements Command {

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!(source.hasPermission(PluginPermissions.LANGUAGE_ADMIN) || source.hasPermission(PluginPermissions.LANGUAGE_EXCEPTION_LIST))) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        List<String> exceptionList = new ArrayList<>(configurationService.getConfigList(ConfigKeys.CHAT_FILTER_EXCEPTIONS, ConfigTypes.STRINGLIST));
        source.sendMessage(TextComponent.of(String.join(", ", exceptionList)));
    }
}
