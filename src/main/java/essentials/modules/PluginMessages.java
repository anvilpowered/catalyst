package essentials.modules;



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

    public static TextComponent noNickColorPermission = TextComponent.builder()
            .content(prefix + "You do not have permission to have a colored nickname!")
            .color(TextColor.RED)
            .build();
    public static TextComponent noNickMagicPermissions = TextComponent.builder()
            .content(prefix + "You do not have permission to have a magical nickname!")
            .color(TextColor.RED)
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

    public static String removeColor(String text){

        //text = text.replaceAll("&", "");
        text = text.replaceAll("&4", "");
        text = text.replaceAll("&c", "");
        text = text.replaceAll("&6", "");
        text = text.replaceAll("&e", "");
        text = text.replaceAll("&2", "");
        text = text.replaceAll("&a", "");
        text = text.replaceAll("&b", "");
        text = text.replaceAll("&3", "");
        text = text.replaceAll("&1", "");
        text = text.replaceAll("&9", "");
        text = text.replaceAll("&d", "");
        text = text.replaceAll("&5", "");
        text = text.replaceAll("&f", "");
        text = text.replaceAll("&7", "");
        text = text.replaceAll("&8", "");
        text = text.replaceAll("&0", "");
        return text;
    }
}
