package essentials.discordbridge.discord;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import net.kyori.text.Component;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class DiscordCommandSource implements CommandSource {
    @Override
    public void sendMessage(Component component)
    {
        DiscordConfig.getOutChannels(Bridge.getDiscordApi()).forEach(textChannel -> textChannel.sendMessage("```" + "Output: " + LegacyComponentSerializer.legacy().serialize(component) + "```"));
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return null;
    }
}
