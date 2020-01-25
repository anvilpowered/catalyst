/*
 *     MSEssentials - MilSpecSG
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msessentials.velocity.messages;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

public class CommandUsageMessages {

    public TextComponent banCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage:  &e/ban <user> [reason]"))
        .build();

    public TextComponent tempBanCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage:  &e/tempban <user> <duration> [reason]"))
        .build();

    public TextComponent unbanCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: &e/unban <user>"))
        .build();

    public TextComponent muteCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: &e/mute <user> [reason]"))
        .build();

    public TextComponent tempMuteCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: &e/tempmute <user> <duration> [reason]"))
        .build();

    public TextComponent unMuteCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: &e/unmute <player>"))
        .build();

    public TextComponent kickCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: &e/kick <user> [reason]"))
        .build();

    public TextComponent findCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /find <user>"))
        .build();

    public TextComponent sendCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /send <user> <server>"))
        .build();

    public TextComponent messageCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /message <user> <message>"))
        .build();

    public TextComponent nickNameCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /nick <nickname>"))
        .build();

    public TextComponent broadcastCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /broadcast <message>"))
        .build();

    public TextComponent infoCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /info <user>"))
        .build();

    public TextComponent swearAddCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /swear add <word>"))
        .build();

    public TextComponent swearRemoveCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /swear remove <word>"))
        .build();

    public TextComponent exceptionAddCommandUsage = TextComponent.builder()
        .append(legacyColor("&4Usage: /exception add <word>"))
        .build();

    public TextComponent exceptionRemoveCommand = TextComponent.builder()
        .append(legacyColor("&4Usage: /exception remove <word>"))
        .build();

    public TextComponent legacyColor(String text) {
        return LegacyComponentSerializer.legacy().deserialize(text, '&');
    }

}
