package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import javax.inject.Inject;

public class UnBanCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.BAN)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (!(args.length > 0)) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }

        memberManager.setBanned(args[0], false).thenAcceptAsync(source::sendMessage);
        memberManager.setBanReason(args[0], "");
    }
}
