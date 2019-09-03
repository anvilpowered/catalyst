package essentials.modules.proxychat;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.velocity.MSEssentialsChatListener;
import essentials.modules.Config.NicknameConfig;
import essentials.modules.PluginMessages;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.commands.NickNameCommand;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import essentials.modules.events.SendMessage;
import essentials.modules.language.MSLang;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;

import java.util.Optional;
import java.util.UUID;

public class ProxyChatEvent {


    @Subscribe
    public void onChat(PlayerChatEvent e)
    {
        Player player = e.getPlayer();

        if(StaffChat.toggledSet.contains(player.getUniqueId())){
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }
        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);

        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix = userMeta.getPrefix();

        String message = e.getMessage();
        e.setResult(PlayerChatEvent.ChatResult.denied());


        TextComponent name;

        if(NicknameConfig.hasNickName(player.getUniqueId()))
        {
            UUID playedID = player.getUniqueId();
            name = PluginMessages.legacyColor(NicknameConfig.getNickName(playedID));
        }else
        {
            name = TextComponent.of(player.getUsername());
        }
        MSEssentialsChatFormedEvent formedEvent = new MSEssentialsChatFormedEvent(player, message, ProxyChat.legacyColor(message));

        MSEssentials.server.getEventManager().fire(formedEvent).join();
        MSEssentials.logger.info("fired and joined that shiiiit");
        if(prefix != null)
        {
         for(Player p : MSEssentials.server.getAllPlayers())
         {
             p.sendMessage(ProxyChat.legacyColor(prefix)
                     .append(name)
                     .append(TextComponent.of(": ")
                             .append(ProxyChat.legacyColor(message)))
                     .hoverEvent(HoverEvent.showText(TextComponent.of(player.getUsername()))));
         }
        }
        else {
            MSEssentials.server.getEventManager().fire(formedEvent).join();
            MSEssentials.logger.info("fired and joined that shiiiit");

            for (Player p : MSEssentials.server.getAllPlayers())
            {
                p.sendMessage(name.append(TextComponent.of(" : ")).append(ProxyChat.legacyColor(message)));
                boolean cancelled = false;
                SendMessage sendMessage = new SendMessage(player, p, message, cancelled);

                p.sendMessage(TextComponent.of(message));
                MSEssentials.logger.info(formedEvent.getRawMessage());
            }
            Bridge.getConfig().getOutChannels(Bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(name + message));

        }
    }
}
