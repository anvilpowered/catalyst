package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.events.PlayerMessageEvent;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

public class ReplyCommand implements Command {
    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 0) {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }
        if (source instanceof Player) {
            String message = String.join(" ", args);
            Player p = (Player) source;
            UUID senderUUID = p.getUniqueId();
            if (PlayerMessageEvent.replyMap.containsKey(p.getUniqueId())) {
                UUID recipientUUID = PlayerMessageEvent.replyMap.get(senderUUID);
                PlayerMessageEvent.replyMap.put(recipientUUID, p.getUniqueId());

                Optional<Player> recipient = MSEssentials.getServer().getPlayer(recipientUUID);

                if (!recipient.isPresent()) {
                    source.sendMessage(PluginMessages.prefix.append(TextComponent.of("player not found")));
                }

                PlayerMessageEvent.sendMessage(p, recipient.get(), message);
            } else {
                source.sendMessage(PluginMessages.prefix.append(TextComponent.of("nobody to reply to!")));
                return;
            }
        }
    }
}
