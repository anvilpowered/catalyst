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

import org.anvilpowered.catalyst.api.event.JoinEvent;

import java.net.InetSocketAddress;

public class CommonJoinEvent<TPlayer> implements JoinEvent<TPlayer> {

    private TPlayer player;
    private InetSocketAddress virtualHost;


    @Override
    public TPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(TPlayer player) {
        this.player = player;
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        return virtualHost;
    }

    @Override
    public void setVirtualHost(InetSocketAddress virtualHost) {
        this.virtualHost = virtualHost;
    }
}