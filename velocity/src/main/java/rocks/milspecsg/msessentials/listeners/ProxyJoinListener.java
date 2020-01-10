package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msessentials.modules.utils.PlayerListUtils;
import rocks.milspecsg.msessentials.modules.utils.StaffListUtils;

public class ProxyJoinListener {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private StaffListUtils staffListUtils;

    @Inject
    private PlayerListUtils playerListUtils;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(PluginPermissions.SOCIALSPYONJOIN)) {
            ProxyMessageEvent.socialSpySet.add(player.getUniqueId());
        }

        playerListUtils.addPlayer(player);
        staffListUtils.getStaffNames(player);

        memberManager.syncPlayerInfo(player.getUniqueId(), player.getRemoteAddress().toString(), player.getUsername());
    }
}
