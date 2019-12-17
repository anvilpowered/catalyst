package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.LuckpermsHook;
import essentials.modules.PluginMessages;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import net.kyori.text.TextComponent;


public class ProxyChatListener {



    @Subscribe
    public void onProxyChat(MSEssentialsChatFormedEvent event)
    {
        Player player = event.getSender();
        MSEssentials.logger.info(event.getRawMessage());
        String prefix;
        if(LuckpermsHook.getPrefix(player) == null)
        {
            prefix = "";
        }
        else
        {
            prefix = PluginMessages.removeColor(LuckpermsHook.getPrefix(player));
        }


        String finalPrefix = TextUtil.stripString(prefix);


        final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));
        final String sender = TextUtil.stripString(TextUtil.toMarkdown(TextComponent.of(event.getSender().getUsername())));

        MSEssentials.logger.info(msg);
        MSEssentials.logger.info(sender);

        MSEssentials.logger.info(finalPrefix + sender + msg);

        DiscordConfig.getOutChannels(Bridge.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(msg));
    }
}
