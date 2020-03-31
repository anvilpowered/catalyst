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
import org.anvilpowered.anvil.api.data.config.ConfigurationService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class CommonSwearCommand<TString, TCommandSource, TSubject> {

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private Registry registry;

    @Inject
    private ConfigurationService configurationService;

    public void execute(TCommandSource source, TSubject subject, String[] args) {

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.swearAddCommandUsage(), source);
            return;
        }
        switch (args[0]) {
            case "list": {
                if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.LANGUAGE_LIST))) {
                    textService.send(textService.of(String.join(
                        ", ",
                        registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS))),
                        source);
                    return;
                } else {
                    textService.send(pluginMessages.getNoPermission(), source);
                }
                return;
            }
            case "add": {
                if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS).isEmpty()) {
                        configurationService
                            .addToCollection(CatalystKeys.CHAT_FILTER_SWEARS, args[1]);
                        configurationService.save();
                        textService.send(pluginMessages.getNewException(args[1]), source);
                    } else if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS).contains(args[1])) {
                        textService.send(pluginMessages.getExistingException(args[1]), source);
                    } else {
                        configurationService
                            .addToCollection(CatalystKeys.CHAT_FILTER_SWEARS, args[1]);
                        configurationService.save();
                        textService.send(pluginMessages.getNewException(args[1]), source);
                    }
                } else {
                    textService.send(pluginMessages.getNoPermission(), source);
                    return;
                }
                return;
            }
            case "remove": {
                if (permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (!registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS).contains(args[1])) {
                        textService.send(pluginMessages.getMissingException(args[1]), source);
                    } else {
                        configurationService
                            .removeFromCollection(CatalystKeys.CHAT_FILTER_SWEARS, args[1]);
                        configurationService.save();
                        textService.send(pluginMessages.getRemoveException(args[1]), source);
                    }
                } else {
                    textService.send(pluginMessages.getNoPermission(), source);
                }
            }
        }
    }
}
