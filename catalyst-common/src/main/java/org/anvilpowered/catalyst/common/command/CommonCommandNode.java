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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.Environment;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.anvil.api.command.CommandService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommonCommandNode<TCommandExecutor, TCommandSource>
    implements CommandNode<TCommandSource> {

    private boolean alreadyLoaded;
    protected Map<List<String>, Function<TCommandSource, String>> descriptions;
    protected Map<List<String>, Predicate<TCommandSource>> permissions;
    protected Map<List<String>, Function<TCommandSource, String>> usages;

    @Inject
    protected CommandService<TCommandExecutor, TCommandSource> commandService;

    @Inject
    protected Environment environment;

    protected Registry registry;

    protected CommonCommandNode(Registry registry) {
        this.registry = registry;
        registry.whenLoaded(() -> {
            if (alreadyLoaded) return;
            loadCommands();
            alreadyLoaded = true;
        }).register();
        alreadyLoaded = false;
        descriptions = new HashMap<>();
        permissions = new HashMap<>();
        usages = new HashMap<>();
    }

    protected abstract void loadCommands();

    private static final String ERROR_MESSAGE = "Catalyst commands have not been loaded yet";

    @Override
    public Map<List<String>, Function<TCommandSource, String>> getDescriptions() {
        return Objects.requireNonNull(descriptions, ERROR_MESSAGE);
    }

    @Override
    public Map<List<String>, Predicate<TCommandSource>> getPermissions() {
        return Objects.requireNonNull(permissions, ERROR_MESSAGE);
    }

    @Override
    public Map<List<String>, Function<TCommandSource, String>> getUsages() {
        return Objects.requireNonNull(usages, ERROR_MESSAGE);
    }

    @Override
    public String getName() {
        return CatalystPluginInfo.id;
    }
}
