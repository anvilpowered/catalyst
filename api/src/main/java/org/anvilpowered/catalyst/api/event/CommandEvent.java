/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

public final class CommandEvent {

  private final Object sourceType;
  private final String sourceName;
  private final String command;
  private final boolean result;

  public CommandEvent(Object sourceType, String sourceName, String command, boolean result) {
    this.sourceType = sourceType;
    this.sourceName = sourceName;
    this.command = command;
    this.result = result;
  }

  public Object getSourceType() {
    return sourceType;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getCommand() {
    return command;
  }

  public boolean getResult() {
    return result;
  }
}
