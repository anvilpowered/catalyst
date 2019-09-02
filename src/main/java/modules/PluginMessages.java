package modules;


import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

public class PluginMessages {

    public static String prefix = "[MSEssentials] ";

    public static TextComponent noPermissions = TextComponent.builder()
            .content(prefix + "You do not have permissions for this command!")
            .color(TextColor.RED)
            .build();

}
