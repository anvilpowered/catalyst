package rocks.milspecsg.msessentials.model.core.member;

import io.jsondb.annotation.Document;
import rocks.milspecsg.msrepository.model.data.dbo.JsonDbo;

import java.util.Date;
import java.util.UUID;

@Document(collection = "members", schemaVersion = "1.0")
public class JsonMember extends JsonDbo implements Member<UUID> {

    private UUID userUUID;
    private String nickname;
    private boolean isBanned;
    private String ipAddress;
    private Date joinedUtc;
    private Date lastSeenUtc;
    private String banReason;


    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
        prePersist();
    }

    @Override
    public String getNickName() {
        return nickname;
    }

    @Override
    public void setNickName(String nickname) {
        this.nickname = nickname;
        prePersist();
    }

    @Override
    public boolean getBanStatus() {
        return isBanned;
    }

    @Override
    public void setBanStatus(boolean banned) {
        this.isBanned = banned;
    }

    @Override
    public String getIPAddress() {
        return ipAddress;
    }

    @Override
    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public Date getJoinedUtc() {
        return joinedUtc;
    }

    @Override
    public void setJoinedUtc(Date joinedUtc) {
        this.joinedUtc = joinedUtc;
    }

    @Override
    public Date getLastSeenUtc() {
        return lastSeenUtc;
    }

    @Override
    public void setLastSeenUtc(Date lastSeenUTC) {
        this.lastSeenUtc = lastSeenUTC;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getBanReason() {
        return banReason;
    }

    @Override
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    @Override
    public void setUserName(String username) {

    }

    @Override
    public void setMuteStatus(boolean muteStatus) {

    }

    @Override
    public boolean getMuteStatus() {
        return false;
    }
}
