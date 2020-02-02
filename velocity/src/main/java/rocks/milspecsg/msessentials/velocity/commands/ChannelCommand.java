package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.chat.ChatService;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;

public class ChannelCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ChatService<TextComponent> chatService;

    String channelId;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source.hasPermission(PluginPermissions.CHANNEL_BASE + channelId)) {
            if (args.length == 0) {
                if (source instanceof Player) {
                    Player player = (Player) source;
                    chatService.switchChannel(player.getUniqueId(), channelId);
                    source.sendMessage(TextComponent.of("Channel switched to " + channelId));
                }
            }
        } else {
            source.sendMessage(pluginMessages.getNoPermission());
        }
    }
}
