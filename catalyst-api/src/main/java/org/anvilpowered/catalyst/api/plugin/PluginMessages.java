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

package org.anvilpowered.catalyst.api.plugin;

import java.time.Instant;
import net.kyori.adventure.text.Component;

public interface PluginMessages {

  Component getBroadcast(Component message);

  Component getBroadcast(String message);

  Component getNotEnoughArgs();

  Component getNoPermission();

  Component getNoServerPermission(String serverName);

  Component getNoNickColorPermission();

  Component getNoNickMagicPermission();

  Component getCurrentServer(String userName, String serverName);

  Component getMuted();

  Component getMuteExempt();

  Component getBanExempt();

  Component getKickExempt();

  Component getSocialSpy(boolean enabled);

  Component getStaffChat(boolean enabled);

  Component getStaffChatMessageFormatted(String userName, Component message);

  Component getStaffChatMessageFormattedConsole(Component message);

  Component getIncompatibleServerVersion();

  Component getExistingSwear(String swear);

  Component getExistingException(String exception);

  Component getMissingSwear(String swear);

  Component getMissingException(String exception);

  Component getNewSwear(String swear);

  Component getNewException(String exception);

  Component getRemoveSwear(String swear);

  Component getRemoveException(String exception);

  Component ignoreExempt();

  Component offlineOrInvalidPlayer();

  Component messageSelf();

  Component getBanMessage(String reason, Instant endUtc);

  Component getMuteMessage(String reason, Instant endUtc);

  Component getInvalidServer();
}
