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

package org.anvilpowered.catalyst.bungee.service;

import com.google.common.eventbus.EventBus;
import net.md_5.bungee.api.ProxyServer;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;

import java.util.concurrent.TimeUnit;

public class BungeeEventService implements EventService {

    @Override
    public <E> void fire(E event) {
        EventBus eventBus = new EventBus();
        eventBus.post(event);
    }

    @Override
    public void schedule(Runnable runnable, TimeUnit timeUnit, int time) {
        ProxyServer.getInstance().getScheduler().schedule(CatalystBungee.plugin, runnable, time, timeUnit);
    }
}
