package rocks.milspecsg.msessentials.model.core.member;

import rocks.milspecsg.msrepository.model.data.dbo.ObjectWithId;

import java.util.Date;
import java.util.UUID;

public interface Member<TKey> extends ObjectWithId<TKey> {

    UUID getUserUUID();
    void setUserUUID(UUID userUUID);

    String getUserName();
    void setUserName(String username);

    String getNickName();
    void setNickName(String nickname);

    boolean getBanStatus();
    void setBanStatus(boolean banned);

    String getIPAddress();
    void setIPAddress(String ipAddress);

    Date getJoinedUtc();
    void setJoinedUtc(Date joinDate);

    Date getLastSeenUtc();
    void setLastSeenUtc(Date lastSeen);

    String getBanReason();
    void setBanReason(String banReason);

    void setMuteStatus(boolean muteStatus);
    boolean getMuteStatus();
}
