/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.api.event;

import net.kyori.adventure.text.Component;

public final class ChatEvent<TPlayer> {

  private final TPlayer player;
  private final Component message;
  private String rawMessage;

  public ChatEvent(TPlayer player, String rawMessage, Component message) {
    this.player = player;
    this.rawMessage = rawMessage;
    this.message = message;
  }

  public TPlayer getPlayer() {
    return player;
  }

  public String getRawMessage() {
    return rawMessage;
  }

  public void setRawMessage(String rawMessage) {
    this.rawMessage = rawMessage;
  }

  public Component getMessage() {
    return message;
  }
}
