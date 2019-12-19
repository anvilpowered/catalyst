package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.member.MemberManager;

public class ProxyJoinListener {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();
        memberManager.getPrimaryComponent().getOneOrGenerateForUser(player.getUniqueId(), player.getRemoteAddress().toString(), player.getUsername());
    }
}
