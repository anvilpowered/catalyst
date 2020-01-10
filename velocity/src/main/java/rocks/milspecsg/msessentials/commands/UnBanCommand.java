package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

public class UnBanCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.BAN)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }
        if (!(args.length > 0)) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.unbanCommandUsage);
            return;
        }
        memberManager.unBan(args[0]).thenAcceptAsync(source::sendMessage);
    }
}
