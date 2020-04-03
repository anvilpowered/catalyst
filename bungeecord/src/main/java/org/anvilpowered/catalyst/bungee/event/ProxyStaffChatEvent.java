/*
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

package org.anvilpowered.catalyst.bungee.event;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProxyStaffChatEvent extends Event {

    private final ProxiedPlayer sender;
    private final String rawMessage;
    public static Set<UUID> staffChatSet = new HashSet<>();
    private final TextComponent message;


    public ProxyStaffChatEvent(ProxiedPlayer sender, String rawMessage, TextComponent message) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = message;
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public TextComponent getMessage() {
        return message;
    }
}
