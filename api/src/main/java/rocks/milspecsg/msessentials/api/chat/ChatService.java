package rocks.milspecsg.msessentials.api.chat;

import rocks.milspecsg.msessentials.api.data.config.Channel;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatService<TString> {

    void switchChannel(UUID userUUID, String channelId);

    String getChannelId(UUID userUUID);

    Optional<Channel> getChannel(String channelId);

    Optional<String> getChannelPrefix(String channelId);

    CompletableFuture<TString> formatMessage(String prefix, String nameColor, String userName, String message, boolean hasChatColorPermission, String suffix, String serverName, String channelId, String channelPrefix);
}
