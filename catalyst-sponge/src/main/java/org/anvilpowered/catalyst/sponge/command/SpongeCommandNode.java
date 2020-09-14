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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.common.command.CommonCommandNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

@Singleton
public class SpongeCommandNode
    extends CommonCommandNode<Text, Player, CommandSource> {

    @Inject
    private SpongeSyncCommand syncCommand;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    public SpongeCommandNode(Registry registry) {
        super(registry, Player.class);
    }

    @Override
    protected void loadCommands() {
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
            .register(pluginContainer, root, "catalystsync", "cs", "commandsync");
    }
}
