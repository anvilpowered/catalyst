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
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.config.ChatChannel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

@Singleton
public class CatalystCommandManager {

    @Inject
    ServerCommand serverCommand;
    @Inject
    Provider<ChannelCommand> channelCommandProvider;
    @Inject
    private ProxyServer proxyServer;
    private Registry registry;
    @Inject
    private BroadcastCommand broadcastCommand;
    @Inject
    private InfoCommand infoCommand;
    @Inject
    private NickNameCommand nicknameCommand;
    @Inject
    private BanCommand banCommand;
    @Inject
    private TempBanCommand tempBanCommand;
    @Inject
    private UnBanCommand unBanCommand;
    @Inject
    private KickCommand kickCommand;
    @Inject
    private FindCommand findCommand;
    @Inject
    private SendCommand sendCommand;
    @Inject
    private MessageCommand messageCommand;
    @Inject
    private ReplyCommand replyCommand;
    @Inject
    private MuteCommand muteCommand;
    @Inject
    private TempMuteCommand tempMuteCommand;
    @Inject
    private UnMuteCommand unMuteCommand;
    @Inject
    private SocialSpyCommand socialSpyCommand;
    @Inject
    private StaffListCommand staffListCommand;
    @Inject
    private StaffChatCommand staffChatCommand;
    @Inject
    private ListCommand listCommand;
    @Inject
    private DeleteNicknameCommand deleteNicknameCommand;
    @Inject
    private SwearCommand swearCommand;
    @Inject
    private ExceptionCommand exceptionCommand;
    @Inject
    private IgnoreCommand ignoreCommand;

    @Inject
    private BaseChannelCommand baseChannelCommand;

    private boolean alreadyLoaded = false;

    @Inject
    public CatalystCommandManager(Registry registry) {
        this.registry = registry;
        this.registry.whenLoaded(this::registryLoaded);
    }

    public void registryLoaded() {
        if (alreadyLoaded) return;
        alreadyLoaded = true;
        if (registry.getOrDefault(CatalystKeys.BAN_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "ban", banCommand, "cban");
            proxyServer.getCommandManager().register(
                "tempban", tempBanCommand, "ctempban");
            proxyServer.getCommandManager().register(
                "unban", unBanCommand, "cunban", "pardon", "cpardon");
        }
        if (registry.getOrDefault(CatalystKeys.BROADCAST_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "broadcast", broadcastCommand);
        }
        if (registry.getOrDefault(CatalystKeys.NICKNAME_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "delnick", deleteNicknameCommand, "cdelnick", "deletenick");
            proxyServer.getCommandManager().register(
                "nick", nicknameCommand, "cnick");
        }
        if (registry.getOrDefault(CatalystKeys.FIND_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "find", findCommand, "cfind");
        }
        if (registry.getOrDefault(CatalystKeys.LIST_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "list", listCommand, "clist");
        }
        if (registry.getOrDefault(CatalystKeys.INFO_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "info", infoCommand, "cinfo");
        }
        if (registry.getOrDefault(CatalystKeys.KICK_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "kick", kickCommand, "ckick");
        }
        if (registry.getOrDefault(CatalystKeys.MESSAGE_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "msg", messageCommand, "w", "tell", "whisper", "m", "t", "pm");
            proxyServer.getCommandManager().register(
                "reply", replyCommand, "r");
        }
        if (registry.getOrDefault(CatalystKeys.MUTE_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "mute", muteCommand, "cmute");
            proxyServer.getCommandManager().register(
                "tempmute", tempMuteCommand, "ctempmmute");
            proxyServer.getCommandManager().register(
                "unmute", unMuteCommand, "cunmute");
        }
        if (registry.getOrDefault(CatalystKeys.SEND_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "send", sendCommand, "csend");
        }
        if (registry.getOrDefault(CatalystKeys.SOCIALSPY_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "socialspy", socialSpyCommand, "ss");
        }
        proxyServer.getCommandManager().register(
            "stafflist", staffListCommand);
        if (registry.getOrDefault(CatalystKeys.STAFFCHAT_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "staffchat", staffChatCommand, "sc");
        }
        if (registry.getOrDefault(CatalystKeys.SEND_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "server", serverCommand);
        }
        proxyServer.getCommandManager().register(
            "swear", swearCommand);
        proxyServer.getCommandManager().register(
            "exception", exceptionCommand);
        proxyServer.getCommandManager().register(
            "ignore", ignoreCommand
        );
        proxyServer.getCommandManager().register(
            "channel", baseChannelCommand
        );
        for(ChatChannel c : registry.getOrDefault(CatalystKeys.CHAT_CHANNELS)) {
            ChannelCommand command = channelCommandProvider.get();
            command.setChannelId(c.id);
            proxyServer.getCommandManager().register(
                c.id, command, c.aliases.toArray(new String[0])
            );
        }
    }
}
