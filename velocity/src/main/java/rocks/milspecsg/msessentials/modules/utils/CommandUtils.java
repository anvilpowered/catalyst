package rocks.milspecsg.msessentials.modules.utils;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import rocks.milspecsg.msrepository.PluginInfo;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.UUID;

public class CommandUtils {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private ConfigurationService configurationService;

    public void createPluginInfoPage(final CommandSource source, final boolean hasPermissionForCommand)
    {
        source.sendMessage(
                TextComponent.builder()
                        .color(TextColor.GOLD)
                        .append(pluginInfo.getPrefix())
                        .append(TextComponent.of("Running Version "))
                        .color(TextColor.GREEN)
                        .append(pluginInfo.getVersion())
                        .color(TextColor.AQUA)
                        .append(TextComponent.of("\n") + "[ Plugin Page ]")
                        .hoverEvent(HoverEvent.showText(TextComponent.of(pluginInfo.getURL())))
                        .clickEvent(ClickEvent.openUrl(pluginInfo.getURL()))
                        .append(TextComponent.of("\n") + "[ MilspecSG ]")
                        .hoverEvent(HoverEvent.showText(TextComponent.of("https://www.milspecsg.rocks/")))
                        .clickEvent(ClickEvent.openUrl("https://ww.milspecsg.rocks/"))
                        .append(TextComponent.of("\n"))
                        .build()
        );
    }

    public void createPlayerInfoPage(final CommandSource source, UUID userUUID ){
        source.sendMessage(
                TextComponent.builder()
                        .color(TextColor.BLUE)
                        .build()
        );
    }
}
