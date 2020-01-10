package rocks.milspecsg.msessentials.modules.utils;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class PlayerListUtils {

    public List<TextComponent> playerNameList = new ArrayList<>();

    public void addPlayer(Player player) {
        playerNameList.add(TextComponent.of(player.getUsername()));
    }

    public void removePlayer (Player player) {
        playerNameList.remove(TextComponent.of(player.getUsername()));
    }
}
