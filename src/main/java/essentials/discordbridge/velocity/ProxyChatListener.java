package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.discord.TextUtil;
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
        MSEssentials.logger.info("MSEssentialsChatFormed");

        Player player = event.getSender();

        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);
        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix = TextUtil.stripString(userMeta.getPrefix());
        final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));
        final String sender = TextUtil.stripString(TextUtil.toMarkdown(TextComponent.of(event.getSender().getUsername())));

        Bridge.getConfig().getOutChannels(Bridge.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(prefix + sender + msg));
    }
}
