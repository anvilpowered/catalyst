package essentials.modules.proxychat;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.language.MSLang;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;

import java.util.Optional;

public class ProxyChatEvent {


    @Subscribe
    public void onChat(PlayerChatEvent e)
    {
        Player player = e.getPlayer();

        /*if(StaffChat.toggledSet.contains(player.getUniqueId()))
        {
            e.setResult(PlayerChatEvent.ChatResult.denied());
            StaffChat.sendMessage(player, e.getMessage());
        }*/
        MSEssentials.logger.info("line 25");
        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);

        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix = userMeta.getPrefix();

        String message = e.getMessage();
        e.setResult(PlayerChatEvent.ChatResult.denied());

        TextComponent name = TextComponent.builder()
                .content(player.getUsername())
                .build();
        String test = "";


        if(prefix != null)
        {

         TextComponent playerPrefix = TextComponent.builder().content(prefix).build();

         for(Player p : MSEssentials.server.getAllPlayers())
         {
             p.sendMessage(playerPrefix.append(ProxyChat.legacyColor(prefix).append(TextComponent.of(" : ").append(ProxyChat.legacyColor(message)))));
         }
        }
        else {
            for (Player p : MSEssentials.server.getAllPlayers())
            {
                p.sendMessage(name.append(TextComponent.of(" : ")).append(ProxyChat.legacyColor(message)));
                MSEssentials.logger.info(ProxyChat.legacyColor(message).toString());
            }
        }
    }
}
