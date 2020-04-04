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

package org.anvilpowered.catalyst.api.service;

public interface CommandService<TCommandSource> {

    void banCommand(TCommandSource source, String[] args);

    void broadcastCommand(TCommandSource source, String[] args);

    void channelCommand(TCommandSource source, String[] args);

    void deleteNicknameCommand(TCommandSource source, String[] args);

    void exceptionCommand(TCommandSource source, String[] args);

    void findCommand(TCommandSource source, String[] args);

    void ignoreCommand(TCommandSource source, String[] args);

    void infoCommand(TCommandSource source, String[] args);

    void kickCommand(TCommandSource source, String[] args);

    void listCommand(TCommandSource source, String[] args);

    void messageCommand(TCommandSource source, String[] args);

    void muteCommand(TCommandSource source, String[] args);

    void nickNameCommand(TCommandSource source, String[] args);

    void replyCommand(TCommandSource source, String[] args);

    void sendCommand(TCommandSource source, String[] args);

    void socialSpyCommand(TCommandSource source, String[] args);

    void staffChatCommand(TCommandSource source, String[] args);

    void staffListCommand(TCommandSource source, String[] args);

    void swearCommand(TCommandSource source, String[] args);

    void tempBanCommand(TCommandSource source, String[] args);

    void tempMuteCommand(TCommandSource source, String[] args);

    void unBanCommand(TCommandSource source, String[] args);

    void unMuteCommand(TCommandSource source, String[] args);
}
