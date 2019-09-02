package essentials.modules.proxychat;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class ProxyChat {

    public static TextComponent legacyColor(String text){
        return LegacyComponentSerializer.INSTANCE.deserialize(text, '&');
    }
}
