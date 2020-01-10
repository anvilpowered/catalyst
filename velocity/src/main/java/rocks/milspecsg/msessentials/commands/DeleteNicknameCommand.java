package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

public class DeleteNicknameCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.NICKNAME)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }
        if (source instanceof Player) {
            Player player = (Player) source;
            memberManager.deleteNickname(player.getUsername()).thenAcceptAsync(source::sendMessage);
        } else {
            source.sendMessage(TextComponent.of("Player only command!"));
        }
    }
}
