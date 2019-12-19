package rocks.milspecsg.msessentials;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import rocks.milspecsg.msrepository.PluginInfo;

import javax.inject.Singleton;

@Singleton
public final class MSEssentialsPluginInfo implements PluginInfo<TextComponent> {
    public static final String id = "msessentials";
    public static final String name = "MSEssentials";
    public static final String version = "$modVersion";
    public static final String description = "An essentials plugin for velocity";
    public static final String url = "https://github.com/MilSpecSG/MSDataSync";
    public static final String authors = "STG_Allen, Cableguy20";
    public static final TextComponent pluginPrefix = TextComponent.of("[MSEssentials] ").color(TextColor.GREEN);

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getAuthors() {
        return authors;
    }

    @Override
    public TextComponent getPrefix() {
        return pluginPrefix;
    }
}