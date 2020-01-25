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

package rocks.milspecsg.msessentials.velocity.events;

import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProxyStaffChatEvent {

    private final Player sender;
    private final String rawMessage;
    public static Set<UUID> staffChatSet = new HashSet<>();

    public Player getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public TextComponent getMessage() {
        return message;
    }

    private final TextComponent message;

    public ProxyStaffChatEvent(Player sender, String rawMessage, TextComponent message) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = message;
    }
}
