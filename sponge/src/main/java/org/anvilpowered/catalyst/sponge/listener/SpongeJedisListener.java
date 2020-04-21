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

package org.anvilpowered.catalyst.sponge.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.sponge.service.SpongeTeleportationService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import redis.clients.jedis.JedisPubSub;

import javax.inject.Named;

public class SpongeJedisListener extends JedisPubSub {

    @Inject
    @Named("anvil")
    private Environment environment;

    @Inject
    private SpongeTeleportationService teleportationService;

    @Inject
    private EventService<Event> eventService;

    @Inject
    Logger logger;

    @Override
    public void onMessage(String channel, String message) {
        logger.warn(channel);
        logger.warn(message);
        if (environment.getRegistry().getOrDefault(Keys.SERVER_NAME).equalsIgnoreCase(channel)) {
            if (message.startsWith("Teleport")) {
                message = message.replace("Teleport", "");
                System.out.println(message);
                String[] mess = message.split("\\s+");
                System.out.println(mess[1]);
                System.out.println(Sponge.getServer().getPlayer(mess[1]).isPresent());
                System.out.println(mess[2]);
                System.out.println(Sponge.getServer().getPlayer(mess[2]).isPresent());
                if (Sponge.getServer().getPlayer(mess[1]).isPresent()) {
                    logger.warn("");
                    eventService.run(() -> teleportationService.teleport(mess[1], mess[2], true));
                } else {
                    eventService.run(() -> teleportationService.teleport(mess[1], mess[2], false));
                }
            }
        }
    }
}
