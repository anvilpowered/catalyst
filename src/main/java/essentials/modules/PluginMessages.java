package essentials.modules;


import essentials.modules.commands.NickNameCommand;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class PluginMessages {

    public static String prefix = "[MSEssentials] ";
    public static String staffPrefix = "&b[STAFF] &r";

    public static TextComponent noPermissions = TextComponent.builder()
            .content(prefix + "You do not have permissions for this command!")
            .color(TextColor.RED)
            .build();

    public static TextComponent enabledStaffChat = TextComponent.builder()
            .append(legacyColor(staffPrefix))
            .append("enabled.")
            .build();

    public static TextComponent setNickName(String nick)
    {
        TextComponent nickmsg = TextComponent.builder()
                .append(legacyColor(prefix))
                .append("Nickname set to : ")
                .append(legacyColor(nick))
                .build();

        return nickmsg;
    }
    public static TextComponent nickColorized(String nick)
    {
        TextComponent nickColor = TextComponent.builder()
                .append(legacyColor(prefix))
                .append("Your current nick: ")
                .append(legacyColor(nick))
                .build();
        return nickColor;
    }

    public static TextComponent legacyColor(String text){
        return LegacyComponentSerializer.INSTANCE.deserialize(text, '&');
    }
}
