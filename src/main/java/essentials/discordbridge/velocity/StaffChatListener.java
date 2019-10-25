package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.PluginMessages;
import essentials.modules.StaffChat.StaffChatEvent;
import essentials.modules.events.StaffChatFormedEvent;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;

import java.util.Optional;

public class StaffChatListener {

    @Subscribe
    public void onStaffChat(StaffChatFormedEvent event)
    {
        MSEssentials.logger.info("StaffChatFormedEvent");

        Player player = event.getSender();
        MSEssentials.logger.info(player.getUsername());

        User user = MSEssentials.api.getUser(player.getUniqueId());
        MSEssentials.logger.info(user.getName());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);
        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix;
        if(userMeta.getPrefix() != null)
        {
            prefix = userMeta.getPrefix();
        }
        else
        {
            prefix = "";
        }

        prefix = TextUtil.stripString(prefix);

        String finalPrefix = PluginMessages.removeColor(prefix);

        final String msg = TextUtil.stripString(TextUtil.toMarkdown((TextComponent) event.getMessage()));
        final String sender = TextUtil.stripString(TextUtil.toMarkdown(TextComponent.of(event.getSender().getUsername())));

        DiscordConfig.getStaffChannel(Bridge.getDiscordApi())
                .forEach(textChannel -> textChannel.sendMessage(finalPrefix + sender + msg));

     //   MSEssentials.logger.info(finalPrefix + sender + msg);
    }
}
