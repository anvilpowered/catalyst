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
import org.anvilpowered.anvil.api.plugin.Plugin;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.CompletableFuture;

public class SpongeExecuteCommandService implements ExecuteCommandService<CommandSource> {

    @Inject
    Plugin<?> plugin;

    @Inject
    private EventService<Event> eventService;

    @Override
    public void executeCommand(CommandSource commandSource, String command) {
        Task.builder()
            .execute(() -> {
                Sponge.getCommandManager().process(commandSource, command);
            })
            .submit(plugin);
    }

    @Override
    public CompletableFuture<Void> executeAsConsole(String command) {
        return CompletableFuture.runAsync(() -> {
            eventService.run(() -> executeCommand(Sponge.getServer().getConsole(), command));
        });
    }

    @Override
    public void executeDiscordCommand(String command) {
    }
}
