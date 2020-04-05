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

import org.anvilpowered.catalyst.api.event.ChatEvent;

public class CommonChatEvent<TString, TPlayer> implements ChatEvent<TString, TPlayer> {

    private TPlayer sender;
    private String rawMessage;
    private TString message;

    @Override
    public TPlayer getSender() {
        return this.sender;
    }

    @Override
    public String getRawMessage() {
        return this.rawMessage;
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
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public void setSender(TPlayer sender) {
        this.sender = sender;
    }
}
