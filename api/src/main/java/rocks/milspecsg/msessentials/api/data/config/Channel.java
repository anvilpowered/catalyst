package rocks.milspecsg.msessentials.api.data.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class Channel {

    @Setting("id")
    public String id;

    @Setting("aliases")
    public List<String> aliases;

    @Setting("prefix")
    public String prefix;

    @Setting("servers")
    public List<String> servers;

    @Setting(value = "alwaysVisible", comment = "Whether members of this channel will always recieve messages from this channel even when they are not in it. ")
    public boolean alwaysVisible;
}
