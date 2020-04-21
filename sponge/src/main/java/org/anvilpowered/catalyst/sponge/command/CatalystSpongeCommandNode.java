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

package org.anvilpowered.catalyst.sponge.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.Plugin;
import org.anvilpowered.anvil.api.redis.RedisService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.sponge.listener.SpongeJedisListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Named;

@Singleton
public class CatalystSpongeCommandNode {

    @Inject
    private CommandSyncCommand syncCommand;

    @Inject
    private RedisService redisService;

    @Inject
    private Plugin<?> plugin;

    @Inject
    private SpongeJedisListener spongeJedisListener;

    private Registry registry;
    private boolean alreadyLoaded = false;

    @Inject
    @Named("anvil")
    Environment environment;

    @Inject
    public CatalystSpongeCommandNode(Registry registry) {
        this.registry = registry;
        registry.whenLoaded(this::loadCommands);
    }

    protected void loadCommands() {
        if (alreadyLoaded) return;
        alreadyLoaded = true;
        Task.builder()
            .execute(() -> redisService.getJedisPool().getResource().subscribe(spongeJedisListener, environment.getRegistry().getOrDefault(Keys.SERVER_NAME), "all"))
            .async()
            .submit(plugin);
        CommandSpec root = CommandSpec.builder()
            .description(Text.of("Sync commands to other servers!"))
            .arguments(
                GenericArguments.string(Text.of("server")),
                GenericArguments.remainingRawJoinedStrings(Text.of("command"))
            )
            .permission(registry.getOrDefault(CatalystKeys.SYNC_COMMAND))
            .executor(syncCommand)
            .build();
        Sponge.getCommandManager()
            .register(plugin, root, "catalystsync", "cs", "commandsync");
    }
}
