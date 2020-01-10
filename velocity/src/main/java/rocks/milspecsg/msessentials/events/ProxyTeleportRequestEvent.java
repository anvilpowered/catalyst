package rocks.milspecsg.msessentials.events;

import com.velocitypowered.api.proxy.Player;

public class ProxyTeleportRequestEvent {

    private final Player sourcePlayer;
    private final Player targetPlayer;

    public Player getSourcePlayer() {
        return sourcePlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public ProxyTeleportRequestEvent(Player sourcePlayer, Player targetPlayer) {
        this.sourcePlayer = sourcePlayer;
        this.targetPlayer = targetPlayer;
    }
}
