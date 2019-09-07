package essentials.modules.StaffChat;


import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.commands.NickNameCommand;
import essentials.modules.events.StaffChatFormedEvent;
import essentials.modules.proxychat.ProxyChat;
import net.kyori.text.TextComponent;

public class StaffChatEvent {

    @Subscribe(order = PostOrder.FIRST)
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(StaffChat.toggledSet.contains(player.getUniqueId()))
        {
            String message = event.getMessage();

            StaffChatFormedEvent formedEvent = new StaffChatFormedEvent(player, event.getMessage(), TextComponent.of(message));

            event.setResult(PlayerChatEvent.ChatResult.denied());

            StaffChat.sendMessage(NickNameCommand.getNick(player.getUsername()), event.getMessage());

            MSEssentials.server.getEventManager().fire(formedEvent).join();
        }
    }

}
