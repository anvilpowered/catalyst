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

package org.anvilpowered.catalyst.velocity.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class StaffListUtils {

    @Inject
    private Registry registry;

    public List<TextComponent> staffNames = new ArrayList<>();
    public List<TextComponent> adminNames = new ArrayList<>();
    public List<TextComponent> ownerNames = new ArrayList<>();

    public void getStaffNames(Player player) {
        if (player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER))) {
            ownerNames.add(TextComponent.of(player.getUsername()));
        } else if (player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF))) {
            if (player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN))) {
                adminNames.add(TextComponent.of(player.getUsername()));
            } else {
                staffNames.add(TextComponent.of(player.getUsername()));
            }
        }
    }

    public void removeStaffNames(Player player) {
        TextComponent playerName = TextComponent.of(player.getUsername());

        ownerNames.remove(playerName);
        adminNames.remove(playerName);
        staffNames.remove(playerName);
    }
}
