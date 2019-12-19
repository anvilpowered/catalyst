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
    public String getNickname() {
        return nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
        prePersist();
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
    public void setJoinDateUtc(Date joinedUtc) {
        this.joinedUtc = joinedUtc;
    }

    @Override
    public Date getLastSeenDateUtc() {
        return lastSeenUtc;
    }

    @Override
    public void setLastSeenDateUtc(Date lastSeenUTC) {
        this.lastSeenUtc = lastSeenUTC;
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public void setUserName(String username) {

    }
}
