package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import net.kyori.text.TextComponent;

public class ProxyChatListener {

    private final Bridge plugin;

    public ProxyChatListener(Bridge bridge)
    {
        plugin = bridge;
    }

    @Subscribe
    public void onProxyChat(MSEssentialsChatFormedEvent event)
    {
        final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));

        plugin.getConfig().getOutChannels(plugin.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(msg));
    }
}
