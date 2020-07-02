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

package org.anvilpowered.catalyst.bungee.command;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.bungee.CatalystBungee;
import org.anvilpowered.catalyst.common.command.CommonCommandNode;

public class BungeeCommandNode
    extends CommonCommandNode<Command, CommandSender> {

    @Inject
    private BungeeBanCommand banCommand;

    @Inject
    private BungeeBroadcastCommand broadcastCommand;

    @Inject
    private BungeeDeleteNickNameCommand deleteNickNameCommand;

    @Inject
    private BungeeExceptionCommand exceptionCommand;

    @Inject
    private BungeeFindCommand findCommand;

    @Inject
    private BungeeIgnoreCommand ignoreCommand;

    @Inject
    private BungeeInfoCommand infoCommand;

    @Inject
    private BungeeKickCommand kickCommand;

    @Inject
    private BungeeListCommand listCommand;

    @Inject
    private BungeeMessageCommand messageCommand;

    @Inject
    private BungeeMuteCommand muteCommand;

    @Inject
    private BungeeNickNameCommand nickNameCommand;

    @Inject
    private BungeeReplyCommand replyCommand;

    @Inject
    private BungeeSocialSpyCommand socialSpyCommand;

    @Inject
    private BungeeStaffChatCommand staffChatCommand;

    @Inject
    private BungeeSwearCommand swearCommand;

    @Inject
    private BungeeTempBanCommand tempBanCommand;

    @Inject
    private BungeeTempMuteCommand tempMuteCommand;

    @Inject
    private BungeeUnBanCommand unBanCommand;

    @Inject
    private BungeeUnMuteCommand unMuteCommand;

    @Inject
    private CatalystBungee plugin;

    @Inject
    public BungeeCommandNode(Registry registry) {
        super(registry);
    }

    @Override
    public void loadCommands() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerCommand(plugin, banCommand);
        pluginManager.registerCommand(plugin, broadcastCommand);
        pluginManager.registerCommand(plugin, deleteNickNameCommand);
        pluginManager.registerCommand(plugin, exceptionCommand);
        pluginManager.registerCommand(plugin, findCommand);
        pluginManager.registerCommand(plugin, ignoreCommand);
        pluginManager.registerCommand(plugin, infoCommand);
        pluginManager.registerCommand(plugin, kickCommand);
        pluginManager.registerCommand(plugin, listCommand);
        pluginManager.registerCommand(plugin, messageCommand);
        pluginManager.registerCommand(plugin, muteCommand);
        pluginManager.registerCommand(plugin, nickNameCommand);
        pluginManager.registerCommand(plugin, replyCommand);
        pluginManager.registerCommand(plugin, socialSpyCommand);
        pluginManager.registerCommand(plugin, staffChatCommand);
        pluginManager.registerCommand(plugin, swearCommand);
        pluginManager.registerCommand(plugin, tempBanCommand);
        pluginManager.registerCommand(plugin, tempMuteCommand);
        pluginManager.registerCommand(plugin, unBanCommand);
        pluginManager.registerCommand(plugin, unMuteCommand);
    }
}
