package rocks.milspecsg.msessentials.modules.utils;

import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import rocks.milspecsg.msessentials.MSEssentials;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    public static String getPrefix(Player player) {
        if (getMetaData(player).get().getPrefix() == null)
            return "";
        return getMetaData(player).get().getPrefix();
    }

    public static String getSuffix(Player player) {
        if (getMetaData(player).get().getSuffix() == null)
            return "&r";
        return getMetaData(player).get().getSuffix();
    }

    private static Optional<CachedMetaData> getMetaData(Player player) {
        UUID playerUUID = player.getUniqueId();
        User tempUser = MSEssentials.api.getUserManager().getUser(playerUUID);

        return Optional.ofNullable(tempUser).map(User::getCachedData)
                .map(data -> data.getMetaData(getQueryOptions(Optional.of(tempUser))))
                .filter(Objects::nonNull);
    }

    public static String getNameColor(Player player) {
        if (getMetaData(player).isPresent()) {
            return getMetaData(player).get().getMetaValue("name-color");
        }
        return "";
    }

    public static String getChatColor(Player player) {
        if (getMetaData(player).isPresent()) {
            return getMetaData(player).get().getMetaValue("chat-color");
        }
        return "&r";
    }

    private static QueryOptions getQueryOptions(Optional<User> user) {
        final ContextManager contextManager = MSEssentials.api.getContextManager();

        return user.flatMap(contextManager::getQueryOptions)
                .orElseGet(contextManager::getStaticQueryOptions);
    }
}
