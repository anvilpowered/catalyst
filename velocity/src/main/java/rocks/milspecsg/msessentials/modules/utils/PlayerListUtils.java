/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.modules.utils;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class PlayerListUtils {

    public List<TextComponent> playerNameList = new ArrayList<>();

    public void addPlayer(Player player) {
        playerNameList.add(TextComponent.of(player.getUsername()));
    }

    public void removePlayer (Player player) {
        playerNameList.remove(TextComponent.of(player.getUsername()));
    }
}
