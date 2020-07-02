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

package org.anvilpowered.catalyst.common.event;

import org.anvilpowered.catalyst.api.event.StaffChatEvent;

import java.util.UUID;

public class CommonStaffChatEvent<TString, TPlayer> implements StaffChatEvent<TString, TPlayer> {

    private TPlayer sender;
    private String rawMessage;
    private TString message;
    private boolean fromConsole;
    private UUID playerUUID;

    @Override
    public TPlayer getPlayer() {
        return this.sender;
    }

    @Override
    public void setPlayer(TPlayer sender) {
        this.sender = sender;
    }

    @Override
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public String getRawMessage() {
        return this.rawMessage;
    }

    @Override
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public TString getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(TString message) {
        this.message = message;
    }

    @Override
    public boolean getIsConsole() {
        return fromConsole;
    }

    @Override
    public void setIsConsole(boolean fromConsole) {
        this.fromConsole = fromConsole;
    }
}
