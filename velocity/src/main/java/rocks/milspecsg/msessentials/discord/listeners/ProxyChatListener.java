package rocks.milspecsg.msessentials.discord;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.discord.discordutils.BridgeMessageBuilder;
import rocks.milspecsg.msessentials.discord.utils.Format;
import rocks.milspecsg.msessentials.events.ProxyChatEvent;
import rocks.milspecsg.msessentials.misc.LuckpermsHook;
import rocks.milspecsg.msessentials.service.common.config.MSEssentialsConfigurationService;

public class ProxyChatListener {
    @Inject
    private BridgeMessageBuilder bridgeMessageBuilder;

    @Inject
    private MSEssentialsConfigurationService configService;

    @Inject
    private Format format;

    @Subscribe
    public void onProxyChat(ProxyChatEvent event)
    {
        System.out.println(event.getRawMessage());
        Player player = event.getSender();

        String prefix;
        if(LuckpermsHook.getPrefix(player) == null)
        {
            prefix = "";
        }
        else
        {
            prefix = LuckpermsHook.getPrefix(player);
        }
        bridgeMessageBuilder.forChannel(configService.getConfigString(ConfigKeys.DISCORD_OUT_CHANNELS))
                .placeholder("player", player.getUsername())
                .placeholder("prefix", prefix)
                .webhook(true)
                .format(format.SERVER_TO_DISCORD)
                .send();
    }
}
