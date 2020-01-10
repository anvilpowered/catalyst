package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;


public class NickNameCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            if (!source.hasPermission(PluginPermissions.NICKNAME)) {
                source.sendMessage(pluginMessages.noPermission);
                return;
            }

            if (args.length == 0) {
                source.sendMessage(pluginMessages.notEnoughArgs);
                source.sendMessage(commandUsage.nickNameCommandUsage);
                return;
            }

            Player player = (Player) source;
            if (args[0].contains("&") == player.hasPermission(PluginPermissions.NICKNAMECOLOR)) {
                memberManager.setNickName(player.getUsername(), args[0]).thenAcceptAsync(source::sendMessage);
            } else {
                source.sendMessage(pluginMessages.noNickColorPermission);
            }
        } else {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Player only command!")));
        }
    }
}
