package modules.google;

import modules.PluginMessages;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MSGoogle {

    public static TextComponent clickMe = TextComponent.builder()
            .content("Click me!")
            .color(TextColor.GREEN)
            .build();

    public static TextComponent googleLink(String[] args)
    {
/*
        String s = Arrays.toString(args);
*/
        String url = "https://www.google.com/search?q=" + Stream.of(args).collect(Collectors.joining("+"));
        TextComponent urlFinal = TextComponent.builder()
                .content(PluginMessages.prefix)
                .append(url)
                .clickEvent(ClickEvent.openUrl(url))
                .hoverEvent(HoverEvent.showText(clickMe))
                .color(TextColor.GOLD)
                .build();
        return urlFinal;
    }

    public static TextComponent sendGoogleLink(String[] args, String playerName)
    {
        String url = "https://www.google.com/search?q=" + Stream.of(args).collect(Collectors.joining("+")).replace(playerName, "");

        TextComponent urlFinal = TextComponent.builder()
                .content(PluginMessages.prefix)
                .append(url)
                .color(TextColor.GOLD)
                .clickEvent(ClickEvent.openUrl(url))
                .build();

        return urlFinal;
    }

    public static TextComponent youtubeLink(String[] args){
        return null;
    }

    public static TextComponent amazonLink(String[] args){
        return null;
    }


}
