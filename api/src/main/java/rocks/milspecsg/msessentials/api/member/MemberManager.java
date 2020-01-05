package rocks.milspecsg.msessentials.api.member;

import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.api.manager.Manager;

import java.net.Inet4Address;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberManager<TString> extends Manager<MemberRepository<?, ?, ?>> {

    @Override
    default String getDefaultIdentifierSingularUpper() {
        return "Member";
    }

    @Override
    default String getDefaultIdentifierPluralUpper() {
        return "Members";
    }

    @Override
    default String getDefaultIdentifierSingularLower() {
        return "member";
    }

    @Override
    default String getDefaultIdentifierPluralLower() {
        return "member";
    }

    CompletableFuture<TString> info(String nickname, boolean isActive);

    CompletableFuture<TString> formatMessage(String prefix, String nameColor, String username, String message, String suffix, boolean hasPermission);

    CompletableFuture<TString> setIPAddress(String username, String ipAddress);

    CompletableFuture<TString> getIPAddress(String username);

    CompletableFuture<TString> setNickName(String userName, String nickName);

    CompletableFuture<TString> deleteNickname(String username);

    CompletableFuture<TString> getNickname(String username);

    CompletableFuture<TString> setJoinedUtc(UUID userUUID, Date joinedUtc);

    CompletableFuture<TString> setLastSeenUtc(UUID userUUID, Date lastSeenUtc);

    CompletableFuture<TString> delNick(UUID userUUID);

    CompletableFuture<TString> setMutedStatus(String username, boolean muted);

    CompletableFuture<Boolean> getMutedStatus(String username);

    CompletableFuture<Void> syncPlayerInfo(UUID playerUUID, String ipAddress, String username);

    CompletableFuture<TString> ban(String username, String reason);

    CompletableFuture<TString> ban(String userName);
    CompletableFuture<TString> unBan(String userName);

}
