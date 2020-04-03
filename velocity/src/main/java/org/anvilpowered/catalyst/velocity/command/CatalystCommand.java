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

package org.anvilpowered.catalyst.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.velocity.discord.JDAHook;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.velocity.utils.CommandUtils;

public class CatalystCommand implements Command {

    @Inject
    private CommandUtils commandUtils;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private JDAHook jdaHook;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        switch (args[0]) {
            case "info": {
                commandUtils.createPluginInfoPage(source, true);
                return;
            }
            case "reload": {
                if (source.hasPermission(registry.getOrDefault(CatalystKeys.RELOAD))) {
                    source.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Reloading")));
                    if(registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
                        jdaHook.getJDA().shutdown();
                    }
                    registry.load();

                } else {
                    source.sendMessage(pluginMessages.getNoPermission());
                }
            }
        }
    }
}
