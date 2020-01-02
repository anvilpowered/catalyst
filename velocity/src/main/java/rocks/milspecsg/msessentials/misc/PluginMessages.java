package rocks.milspecsg.msessentials.misc;

import com.google.inject.Inject;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

public class PluginMessages {

    @Inject
    private ConfigurationService configService;

    public TextComponent notEnoughArgs = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&4not enough arguments!"))
            .build();

    public TextComponent noPermission = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&4insufficient permissions!"))
            .build();

    public TextComponent currentServer (String name, String serverName) {
        return TextComponent.builder()
                .append(MSEssentialsPluginInfo.pluginPrefix)
                .append(legacyColor(name + " is connected to " + "&a" + serverName))
                .build();
    }

    public TextComponent currentlyMuted = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&4You are muted!"))
            .build();

    public TextComponent muteExempt = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&eThis user is exempt from being muted."))
            .build();

    public TextComponent banExempt = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&eThis user is exempt from being banned."))
            .build();

    public TextComponent kickExempt = TextComponent.builder()
            .append(MSEssentialsPluginInfo.pluginPrefix)
            .append(legacyColor("&eThis user is exmpet from being kicked"))
            .build();

    public TextComponent socialSpyToggle(boolean b) {
        if(b) {
            return TextComponent.builder()
                    .append(MSEssentialsPluginInfo.pluginPrefix)
                    .append(legacyColor("&aSocialSpy enabled."))
                    .build();
        } else {
            return TextComponent.builder()
                    .append(MSEssentialsPluginInfo.pluginPrefix)
                    .append(legacyColor("&aSocialSpy disabled."))
                    .build();
        }
    }

    public TextComponent staffChatToggle(boolean b) {
        if(b) {
            return TextComponent.builder()
                    .append(MSEssentialsPluginInfo.pluginPrefix)
                    .append(legacyColor("StaffChat &aenabled."))
                    .build();
        } else {
            return TextComponent.builder()
                    .append(MSEssentialsPluginInfo.pluginPrefix)
                    .append(legacyColor("StaffChat &4disabled."))
                    .build();
        }
    }

    public TextComponent staffChatMessageFormatted (TextComponent message, String username) {
        return TextComponent.builder()
                .append(legacyColor("&b[STAFF]&5 " + username + " : ").append(message))
                .build();
    }

    public TextComponent staffChatMessageFormattedConsole (TextComponent message) {
        return TextComponent.builder()
                .append(legacyColor("&b[STAFF]&5 CONSOLE : ").append(message))
                .build();
    }

    public TextComponent legacyColor(String text) {
        return LegacyComponentSerializer.legacy().deserialize(text, '&');
    }
}
