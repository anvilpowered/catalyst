package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProxyJoinListener {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        memberManager.getPrimaryComponent().getOneOrGenerateForUser(player.getUniqueId(), player.getRemoteAddress().toString(), player.getUsername());

        CompletableFuture<Boolean> isBanned = memberManager.getBanStatus(player.getUsername());
        if(isBanned.get()) {
            CompletableFuture<TextComponent> banReason = memberManager.getBanReason(player.getUsername());
            player.disconnect(banReason.get());
            return;
        }

        if(player.hasPermission(PluginPermissions.SOCIALSPYONJOIN)) {
            ProxyMessageEvent.socialSpySet.add(player.getUniqueId());
        }

    }
}
