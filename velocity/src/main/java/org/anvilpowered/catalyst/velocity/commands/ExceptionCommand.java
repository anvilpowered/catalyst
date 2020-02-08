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

package org.anvilpowered.catalyst.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.velocity.messages.CommandUsageMessages;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.anvilpowered.anvil.api.data.config.ConfigurationService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class ExceptionCommand implements Command {

    @Inject
    private CommandUsageMessages commandUsage;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private ConfigurationService configurationService;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (args.length == 0) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(commandUsage.exceptionAddCommandUsage);
            return;
        }

        switch (args[0]) {
            case "list": {
                if(source.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_LIST))) {
                    source.sendMessage(TextComponent.of(String.join(", ", registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS))));
                    return;
                } else {
                    source.sendMessage(pluginMessages.getNoPermission());
                }
                return;
            }
            case "add": {
                if(source.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).isEmpty()) {
                        configurationService.addToCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                        configurationService.save();
                        source.sendMessage(pluginMessages.getNewException(args[1]));
                    } else if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                        source.sendMessage(pluginMessages.getExistingException(args[1]));
                    } else {
                        configurationService.addToCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                        configurationService.save();
                        source.sendMessage(pluginMessages.getNewException(args[1]));
                    }
                } else {
                    source.sendMessage(pluginMessages.getNoPermission());
                    return;
                }
                return;
            }
            case "remove": {
                if(source.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (!registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                        source.sendMessage(pluginMessages.getMissingSwear(args[1]));
                    } else {
                        configurationService.removeFromCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                        configurationService.save();
                        source.sendMessage(pluginMessages.getRemoveException(args[1]));
                    }
                } else {
                    source.sendMessage(pluginMessages.getNoPermission());
                }
            }
        }
    }
}
