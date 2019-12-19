package rocks.milspecsg.msessentials.model.core.member;

import rocks.milspecsg.msrepository.model.data.dbo.ObjectWithId;

import java.util.Date;
import java.util.UUID;

public interface Member<TKey> extends ObjectWithId<TKey> {

    UUID getUserUUID();

    void setUserUUID(UUID userUUID);

    String getNickname();

    void setNickname(String nickname);

    boolean getIsBanned();

    void setBanned(boolean banned);

    String getIPAddress();

    void setIPAddress(String ipAddress);

    Date getJoinDateUtc();

    void setJoinDateUtc(Date joinDate);

    Date getLastSeenDateUtc();

    void setLastSeenDateUtc(Date lastSeen);

    String getUserName();

    String getBanReason();

    void setBanReason(String banReason);

    void setUserName(String username);
}
