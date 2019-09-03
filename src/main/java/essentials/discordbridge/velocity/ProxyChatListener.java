package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import net.kyori.text.TextComponent;

public class ProxyChatListener {



    @Subscribe
    public void onProxyChat(MSEssentialsChatFormedEvent event)
    {
        MSEssentials.logger.info("MSEssentialsChatFormed");
        final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));
        final String sender = TextUtil.stripString(TextUtil.toMarkdown(TextComponent.of(event.getSender().getUsername())));

        Bridge.getConfig().getOutChannels(Bridge.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(sender + msg));
    }
}
