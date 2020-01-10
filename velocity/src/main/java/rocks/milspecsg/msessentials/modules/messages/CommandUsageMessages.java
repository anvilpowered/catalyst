package rocks.milspecsg.msessentials.modules.messages;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class CommandUsageMessages {

    public TextComponent banCommandUsage = TextComponent.builder()
            .append(legacyColor("&4Usage:  &e/ban <user> [reason]"))
            .build();

    public TextComponent unbanCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: &e/unban <user>"))
            .build();

    public TextComponent muteCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: &e/mute <user>"))
            .build();

    public TextComponent unMuteCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: &e/unmute <player>"))
            .build();

    public TextComponent kickCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: &e/kick <user> [reason]"))
            .build();

    public TextComponent findCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /find <user>"))
            .build();

    public TextComponent sendCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /send <user> <server>"))
            .build();

    public TextComponent messageCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /message <user> <message>"))
            .build();

    public TextComponent nickNameCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /nick <nickname>"))
            .build();

    public TextComponent broadcastCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /broadcast <message>"))
            .build();

    public TextComponent infoCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /info <user>"))
            .build();
    
    public TextComponent swearAddCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /swear add <word>"))
            .build();
    
    public TextComponent swearRemoveCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /swear remove <word>"))
            .build();
    
    public TextComponent exceptionAddCommandUsage = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /exception add <word>"))
            .build();

    public TextComponent exceptionRemoveCommand = TextComponent.builder()
            .append(legacyColor("\n&4Usage: /exception remove <word>"))
            .build();
            
    public TextComponent legacyColor(String text) {
        return LegacyComponentSerializer.legacy().deserialize(text, '&');
    }

}
