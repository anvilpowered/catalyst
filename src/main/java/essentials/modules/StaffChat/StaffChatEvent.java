package essentials.modules.StaffChat;


import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.commands.NickNameCommand;

public class StaffChatEvent {

    @Subscribe(order = PostOrder.FIRST)
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(StaffChat.toggledSet.contains(player.getUniqueId()))
        {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            StaffChat.sendMessage(NickNameCommand.getNick(player.getUniqueId()), event.getMessage());

        }
    }

}
