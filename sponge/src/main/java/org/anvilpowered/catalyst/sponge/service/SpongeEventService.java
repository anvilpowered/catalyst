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
import org.anvilpowered.catalyst.api.service.EventService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class SpongeEventService implements EventService<Event> {

    @Inject
    PluginContainer plugin;

    @Override
    public void fire(Event event) {
        Sponge.getEventManager().post(event);
    }

    @Override
    public void schedule(Runnable runnable, TimeUnit timeUnit, int time) {
        Task.builder()
            .execute(runnable)
            .interval(time, timeUnit)
            .submit(plugin);
    }

    @Override
    public void run(Runnable runnable) {
        Task.builder()
            .execute(runnable)
            .submit(plugin);
    }
}
