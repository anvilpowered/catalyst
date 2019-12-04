package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.LuckpermsHook;
import essentials.modules.PluginMessages;
import essentials.modules.StaffChat.StaffChatEvent;
import essentials.modules.events.StaffChatFormedEvent;
import net.kyori.text.TextComponent;
import net.luckperms.api.cacheddata.CachedData;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.context.Context;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class StaffChatListener {

    @Subscribe
    public void onStaffChat(StaffChatFormedEvent event)
    {
        MSEssentials.logger.info("StaffChatFormedEvent");

        Player player = event.getSender();

        User user = MSEssentials.api.getUserManager().getUser(player.getUniqueId());

        String prefix;
        if(LuckpermsHook.getPrefix(player) != null)
        {
            prefix = LuckpermsHook.getPrefix(player);
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
