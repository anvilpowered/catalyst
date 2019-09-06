package essentials.modules;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.Config.PlayerConfig;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import java.net.InetSocketAddress;
import java.util.UUID;


public class BanListener {

    @Subscribe
    public void onPlayerConnect(LoginEvent event)
    {
        Player player = event.getPlayer();
        String playerName = player.getUsername();
        InetSocketAddress pIP = player.getRemoteAddress();


        TextComponent reason = TextComponent.builder()
                .content("There are no staff members currently online")
                .color(TextColor.DARK_RED)
                .build();

        if(PlayerConfig.checkBan(playerName) == true)
        {
            event.setResult(LoginEvent.ComponentResult.denied(PluginMessages.legacyColor(PlayerConfig.getBanReason(playerName))));
        }
        /*if(config.getIPList.contains(pIP))
        {
            event.setResult(LoginEvent.ComponentResult.denied(reason));
        }*/

    }
}
