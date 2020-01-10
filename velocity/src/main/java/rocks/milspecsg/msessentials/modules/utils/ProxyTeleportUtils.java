package rocks.milspecsg.msessentials.modules.utils;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;

import java.util.*;

@Singleton
public class ProxyTeleportUtils {

    public Map<UUID, UUID> teleportationMap = new HashMap<>();

}
