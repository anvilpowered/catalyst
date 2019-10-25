package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.discord.DiscordCommandSource;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.events.PlayerMessageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMessage implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 0) {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }
        if (source instanceof ConsoleCommandSource || source instanceof DiscordCommandSource) {

            if (args[0].toLowerCase().contains(MSEssentials.getServer().getPlayer(args[0]).get().getUsername().toLowerCase())) {
                Player player = MSEssentials.getServer().getPlayer(args[0]).get();
                String pName = args[0];
                args[0] = "";
                String message = String.join(" ", args);
                source.sendMessage(PlayerMessageEvent.message("Me", pName, message));
                player.sendMessage(PlayerMessageEvent.message("Console", "Me", message));
            }
        }
        if (source instanceof Player) {
            Player player = (Player) source;

            if (player.hasPermission(PluginPermissions.MESSAGE)) {
                if (MSEssentials.getServer().getPlayer(args[0]).isPresent()) {
                    Player recipient = MSEssentials.getServer().getPlayer(args[0]).get();
                    String ar = " ";
                    if (args[0].equalsIgnoreCase(MSEssentials.getServer().getPlayer(args[0]).get().getUsername())) {
                        args[0] = args[0].toLowerCase();
                        ar = String.join(" ", args).replace(MSEssentials.getServer().getPlayer(args[0]).get().getUsername().toLowerCase(), "");
                    }

                    PlayerMessageEvent playerMessageEvent = new PlayerMessageEvent(player, recipient, ar, false);
                    String finalAr = ar;
                    MSEssentials.getServer().getEventManager().fire(playerMessageEvent).thenAcceptAsync(resultEvent -> {
                        PlayerMessageEvent.MessageResult result = resultEvent.getResult();
                        if (result.isAllowed()) {
                            PlayerMessageEvent.sendMessage(player, recipient, finalAr);
                            PlayerMessageEvent.replyMap.put(recipient.getUniqueId(), player.getUniqueId());
                        }
                    });
                    for (Player player1 : MSEssentials.getServer().getAllPlayers()) {
                        if (PlayerMessageEvent.toggledSet.contains(player1.getUniqueId())) {
                            player1.sendMessage(PlayerMessageEvent.socialSpy(player.getUsername(), recipient.getUsername(), finalAr));
                        }
                    }
                }
            }
        }
    }


    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
