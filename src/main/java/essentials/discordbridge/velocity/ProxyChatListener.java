package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.PluginMessages;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ProxyChatListener {



    @Subscribe
    public void onProxyChat(MSEssentialsChatFormedEvent event)
    {
        Player player = event.getSender();

        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);
        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix;
        if(userMeta.getPrefix() == null)
        {
            prefix = "";
        }
        else
        {
            prefix = PluginMessages.removeColor(userMeta.getPrefix());
        }


            String finalPrefix = TextUtil.stripString(prefix);


            final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));
        final String sender = TextUtil.stripString(TextUtil.toMarkdown(TextComponent.of(event.getSender().getUsername())));

        MSEssentials.logger.info(DiscordConfig.getOutChannels(Bridge.getDiscordApi()).toString());

        DiscordConfig.getOutChannels(Bridge.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(finalPrefix + sender + msg));
    }
}
