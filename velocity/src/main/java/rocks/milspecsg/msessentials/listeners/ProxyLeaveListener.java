package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.utils.PlayerListUtils;
import rocks.milspecsg.msessentials.modules.utils.StaffListUtils;

import java.util.Date;

public class ProxyLeaveListener {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private StaffListUtils staffListUtils;

    @Inject
    private PlayerListUtils playerListUtils;

    @Subscribe
    public void onPlayerJoin(DisconnectEvent event) {
        Player player = event.getPlayer();
        memberManager.setLastSeenUtc(player.getUniqueId(), new Date());
        staffListUtils.removeStaffNames(player);
        playerListUtils.removePlayer(player);
    }
}
