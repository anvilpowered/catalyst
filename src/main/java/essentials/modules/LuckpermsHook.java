package essentials.modules;

import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;

import java.util.Objects;
import java.util.Optional;

public class LuckpermsHook {

    private static LuckPerms api;

    public LuckpermsHook(){
        api = LuckPermsProvider.get();
    }

    public static String getPrefix(Player player)
    {
        return String.valueOf(getMetaData(player).map(CachedMetaData::getPrefix).filter(Objects::isNull));
    }

    public static Optional<CachedMetaData> getMetaData(Player player)
    {
        final Optional<User> user =
                Optional.ofNullable(api.getUserManager().getUser(player.getUniqueId()));
        final Optional<ImmutableContextSet> contexts =
                user.flatMap(api.getContextManager()::getContext);

        return user.filter(xxx -> contexts.isPresent())
                .map(User::getCachedData)
                .map(data -> data.getMetaData(getQueryOptions(contexts.get())))
                .filter(Objects::nonNull);
    }
    public String getNameColor(Player player){
        if(getMetaData(player).isPresent()) {
            return getMetaData(player).get().getMetaValue("name-color");
        }
        return null;
    }

    public String getChatColor(Player player)
    {
        if(getMetaData(player).isPresent())
        {
            return getMetaData(player).get().getMetaValue("chat-color");
        }
        return null;
    }

    private static QueryOptions getQueryOptions(ImmutableContextSet contexts) {
        return QueryOptions.builder(QueryMode.CONTEXTUAL).context(contexts).build();
    }
}
