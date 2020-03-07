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

package org.anvilpowered.catalyst.velocity.event;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.api.data.config.Channel;

@Singleton
public class ProxyChatEvent {

    private final Player sender;
    private final String rawMessage;
    private final Component message;
    private final Channel channel;

    public ProxyChatEvent(Player sender, String rawMessage, Component message, Channel channel) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = TextComponent.of(":").append(message);
        this.channel = channel;
    }

    public Player getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public Component getMessage() {
        return message;
    }

    public Channel getChannel() {
        return channel;
    }

}
