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

package org.anvilpowered.catalyst.api.plugin;

public interface PluginMessages<TString> {

    TString getBroadcast(TString message);

    TString getBroadcast(String message);

    TString getNotEnoughArgs();

    TString getNoPermission();

    TString getNoNickColorPermission();

    TString getNoNickMagicPermission();

    TString getCurrentServer(String userName, String serverName);

    TString getMuted();

    TString getMuteExempt();

    TString getBanExempt();

    TString getKickExempt();

    TString getSocialSpy(boolean enabled);

    TString getStaffChat(boolean enabled);

    TString getStaffChatMessageFormatted(String userName, TString message);

    TString getStaffChatMessageFormattedConsole(TString message);

    TString getTeleportRequestSent(String targetUserName);

    TString getTeleportRequestReceived(String requesterUserName);

    TString getTeleportToSelf();

    TString getIncompatibleServerVersion();

    TString getExistingSwear(String swear);

    TString getExistingException(String exception);

    TString getMissingSwear(String swear);

    TString getMissingException(String exception);

    TString getNewSwear(String swear);

    TString getNewException(String exception);

    TString getRemoveSwear(String swear);

    TString getRemoveException(String exception);

    String removeColor(String text);

    TString banCommandUsage();

    TString tempBanCommandUsage();

    TString unbanCommandUsage();

    TString muteCommandUsage();

    TString tempMuteCommandUsage();

    TString unMuteCommandUsage();

    TString kickCommandUsage();

    TString findCommandUsage();

    TString sendCommandUsage();

    TString messageCommandUsage();

    TString nickNameCommandUsage();

    TString broadcastCommandUsage();

    TString infoCommandUsage();

    TString swearAddCommandUsage();

    TString exceptionAddCommandUsage();

    TString ignoreCommandUsage();

    TString ignoreExempt();

    TString offlineOrInvalidPlayer();
}
