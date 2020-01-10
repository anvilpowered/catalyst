/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.api.config.ConfigTypes;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class SwearRemoveCommand implements Command {
    @Inject
    private ConfigurationService configurationService;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!(source.hasPermission(PluginPermissions.LANGUAGE_ADMIN) || source.hasPermission(PluginPermissions.LANGUAGE_SWEAR_REMOVE))) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length < 1) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.swearAddCommandUsage);
            return;
        }
        List<String> swearList = new ArrayList<>(configurationService.getConfigList(ConfigKeys.CHAT_FILTER_SWEARS, ConfigTypes.STRINGLIST));
        System.out.println(swearList);
        if (!swearList.contains(args[0])) {
            source.sendMessage(pluginMessages.missingSwear(args[0]));
        } else {
            swearList.remove(args[0]);
            configurationService.setConfigList(ConfigKeys.CHAT_FILTER_SWEARS, swearList);
            configurationService.save();
            source.sendMessage(pluginMessages.removeSwear(args[0]));

        }
    }
}
