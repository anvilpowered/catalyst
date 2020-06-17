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

package org.anvilpowered.catalyst.sponge.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.util.UserService;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class SpongeTeleportationService {

    public Map<String, String> teleportationMap = new HashMap<>();

    @Inject
    private UserService<User, Player> userService;

    public void teleport(String sender, String recipient, boolean onSameServer) {
        final Optional<User> teleporter = userService.get(sender);
        final Optional<User> target = userService.get(recipient);
        if (onSameServer && teleporter.isPresent() && target.isPresent()) {
            target.flatMap(User::getWorldUniqueId)
                .filter(uuid -> teleporter.get().setLocation(target.get().getPosition(), uuid));
        } else {
            teleportationMap.put(sender, recipient);
        }
    }
}
