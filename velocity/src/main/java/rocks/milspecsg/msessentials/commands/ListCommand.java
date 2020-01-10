package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msessentials.modules.utils.PlayerListUtils;

public class ListCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private PlayerListUtils playerListUtils;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.LIST)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }
        source.sendMessage(pluginMessages.legacyColor("&a------------------- Online Players --------------------"));
        for (TextComponent text : playerListUtils.playerNameList) {
            source.sendMessage(text.append(TextComponent.of(", ")));
        }
    }
}
