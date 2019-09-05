package essentials.modules.proxychat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import essentials.modules.events.SendMessage;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProxyChatEvent {


    @Subscribe
    public void onChat(PlayerChatEvent e)
    {
        String message = e.getMessage();
        Player player = e.getPlayer();
        e.setResult(PlayerChatEvent.ChatResult.denied());

        if(StaffChat.toggledSet.contains(player.getUniqueId())){
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        List<String> swearlist = MSEssentials.wordCatch.isswear(message);
        if(swearlist != null)
        {

            for(String swear: swearlist)
            {
                message = message.replace(swear ,"****");
            }
            sendMessage(e, checkPlayerName(message));

        }else {

            sendMessage(e, checkPlayerName(message));

        }


    }
    public static String checkPlayerName(String message)
    {
        for (Player onlinePlayer : MSEssentials.getServer().getAllPlayers()) {
            if (message.toLowerCase().contains(onlinePlayer.getUsername().toLowerCase())) {
                message = message.replaceAll(onlinePlayer.getUsername(), "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public static void sendMessage(PlayerChatEvent e, String message)
    {
        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();
        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);

        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix = userMeta.getPrefix();
        e.setResult(PlayerChatEvent.ChatResult.denied());


        TextComponent name;
        if(message.contains("&"))
        {
            MSEssentials.logger.info("line 86");
            if(!player.hasPermission(PluginPermissions.CHATCOLOR))
            {
                e.setResult(PlayerChatEvent.ChatResult.denied());
                message = Utils.removeColorCodes(message);
            }
        }

        if(PlayerConfig.hasNickName(player.getUniqueId()))
        {
            UUID playedID = player.getUniqueId();
            name = PluginMessages.legacyColor(PlayerConfig.getNickName(playedID));
            if(name.equals(""))
            {
                name = TextComponent.of(e.getPlayer().getUsername());
            }
        }else
        {
            name = TextComponent.of(player.getUsername());
        }
        MSEssentialsChatFormedEvent formedEvent = new MSEssentialsChatFormedEvent(player, message, ProxyChat.legacyColor(message));

        MSEssentials.server.getEventManager().fire(formedEvent).join();
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

            for (Player p : MSEssentials.server.getAllPlayers())
            {
                p.sendMessage(name.append(TextComponent.of(": ")).append(ProxyChat.legacyColor(message)));
                boolean cancelled = false;
                SendMessage sendMessage = new SendMessage(player, p, message, cancelled);

                p.sendMessage(TextComponent.of(message));
            }
            TextComponent finalName = name;
            String finalMessage = message;
            Bridge.getConfig().getOutChannels(Bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(finalName + finalMessage));

        }
    }
}
