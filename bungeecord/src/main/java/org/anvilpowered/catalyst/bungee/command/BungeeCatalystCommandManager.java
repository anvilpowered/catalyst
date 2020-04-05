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
import net.md_5.bungee.api.ProxyServer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;

public class BungeeCatalystCommandManager {

    private Registry registry;

    @Inject
    private BanCommand banCommand;

    @Inject
    private BroadcastCommand broadcastCommand;

    @Inject
    private DeleteNickNameCommand deleteNickNameCommand;

    @Inject
    private ExceptionCommand exceptionCommand;

    @Inject
    private FindCommand findCommand;

    @Inject
    private IgnoreCommand ignoreCommand;

    @Inject
    private InfoCommand infoCommand;

    @Inject
    private KickCommand kickCommand;

    @Inject
    private ListCommand listCommand;

    @Inject
    private MessageCommand messageCommand;

    @Inject
    private MuteCommand muteCommand;

    @Inject
    private NickNameCommand nickNameCommand;

    @Inject
    private ReplyCommand replyCommand;

    @Inject
    private SocialSpyCommand socialSpyCommand;

    @Inject
    private StaffChatCommand staffChatCommand;

    @Inject
    private SwearCommand swearCommand;

    @Inject
    private TempBanCommand tempBanCommand;

    @Inject
    private TempMuteCommand tempMuteCommand;

    @Inject
    private UnBanCommand unBanCommand;

    @Inject
    private UnMuteCommand unMuteCommand;

    @Inject
    public BungeeCatalystCommandManager(Registry registry) {
        this.registry = registry;
        this.registry.whenLoaded(this::registerCommands);
    }

    public void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, banCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, broadcastCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, deleteNickNameCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, exceptionCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, findCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, ignoreCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, infoCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, kickCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, listCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, messageCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, muteCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, nickNameCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, replyCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, socialSpyCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, staffChatCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, swearCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, tempBanCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, tempMuteCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, unBanCommand);
        ProxyServer.getInstance().getPluginManager().registerCommand(CatalystBungee.plugin, unMuteCommand);
    }
}
