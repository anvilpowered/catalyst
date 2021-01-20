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

package org.anvilpowered.catalyst.api.event;

import java.util.UUID;

public final class JoinEvent<TPlayer> {

    private final TPlayer player;
    private final String hostString;
    private final UUID playerUUID;

    public JoinEvent(TPlayer player, String hostString, UUID playerUUID) {
        this.player = player;
        this.hostString = hostString;
        this.playerUUID = playerUUID;
    }

    public TPlayer getPlayer() {
        return player;
    }

    public String getHostString() {
        return hostString;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
