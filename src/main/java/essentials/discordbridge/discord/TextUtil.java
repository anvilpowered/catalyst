package essentials.discordbridge.discord;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextDecoration;

public class TextUtil {
    public static String toMarkdown(TextComponent component)
    {
        return toMarkdown(component, "");
    }

    private static String toMarkdown(TextComponent component, String base)
    {
        String currentSegment = component.content();
        for(TextDecoration decoration : component.decorations())
        {
            switch(decoration){
                case BOLD:
                    currentSegment = String.format("**%s**", currentSegment);
                    break;
                case ITALIC:
                    currentSegment = String.format("*%s*", currentSegment);
                    break;
                case UNDERLINED:
                    currentSegment = String.format("~~%s~~", currentSegment);
                    break;
                case OBFUSCATED:
                    currentSegment = String.format("||%s||", currentSegment);
                    break;

            }
        }

        base = base + currentSegment;

        for(Component child : component.children())
        {
            base = toMarkdown((TextComponent) child, base);
        }
        return base;
    }

    public static String stripString(final String msg){
        return msg.replaceAll("@everyone", "(at)everyone")
                .replaceAll("@here", "(at)here")
                .replaceAll("<@&[0-9]+>", "");
    }
}
