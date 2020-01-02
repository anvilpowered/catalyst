package rocks.milspecsg.msessentials.model.core.member;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import rocks.milspecsg.msrepository.model.data.dbo.MongoDbo;

import java.util.Date;
import java.util.UUID;

@Entity("members")
public class MongoMember extends MongoDbo implements Member<ObjectId> {

    private UUID userUUID;
    private String nickname;
    private boolean isBanned;
    private String ipAddress;
    private Date joinedUtc;
    private Date lastSeenUtc;
    private String username;
    private String banReason;
    private boolean muteStatus;

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public void setMuteStatus(boolean muteStatus) {
        this.muteStatus = muteStatus;
    }

    @Override
    public boolean getMuteStatus() {
        return muteStatus;
    }

    @Override
    public void setUserName(String username) {
        this.username = username;
    }

    @Override
    public UUID getUserUUID() {
        return userUUID;
    }

    @Override
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean getIsBanned() {
        return isBanned;
    }

    @Override
    public void setBanned(boolean banned) {
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
    public Date getJoinDateUtc() {
        return joinedUtc;
    }

    @Override
    public void setJoinDateUtc(Date joinDate) {
        this.joinedUtc = joinDate;
    }

    @Override
    public Date getLastSeenDateUtc() {
        return lastSeenUtc;
    }

    @Override
    public void setLastSeenDateUtc(Date lastSeen) {
        this.lastSeenUtc = lastSeen;
    }

    public String getBanReason() {
        return banReason;
    }

    @Override
    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }
}
