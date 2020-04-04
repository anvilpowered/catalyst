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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.config.ConfigurationService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.CommandService;

public class CommonCommandService<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> implements CommandService<TCommandSource> {

    @Inject
    private PermissionService<TCommandSource> permissionService;

    @Inject
    private Registry registry;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private MemberManager<TString> memberManager;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private ConfigurationService configurationService;

    @Override
    public void banCommand(TCommandSource source, String[] args) {
        if (!permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.BAN))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.banCommandUsage(), source);
            return;
        }
        String userName = args[0];
        userService.getPlayer(userName).ifPresent(p -> {
            if (permissionService.hasPermission(p, registry.getOrDefault(CatalystKeys.BAN_EXEMPT))) {
                textService.send(pluginMessages.getBanExempt(), source);
                return;
            }
            if (args.length == 1) {
                memberManager.ban(userName).thenAcceptAsync(m -> textService.send(m, source));
            } else {
                String reason = String.join(" ", args).replace(userName + " ", " ");
                memberManager.ban(userName, reason).thenAcceptAsync(m -> textService.send(m, source));
            }
        });
    }

    @Override
    public void broadcastCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void channelCommand(TCommandSource source, String[] args) {
    }

    @Override
    public void deleteNicknameCommand(TCommandSource source, String[] args) {
        if (!permissionService.hasPermission(source,
            registry.getOrDefault(CatalystKeys.NICKNAME))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (args.length == 0) {
            memberManager.deleteNickName(userService.getUserName((TPlayer) source));
            return;
        }
        if (args[0].equalsIgnoreCase("other")
            && permissionService.hasPermission(
            source, registry.getOrDefault(CatalystKeys.NICKNAME_OTHER))) {
            memberManager.deleteNickNameForUser(args[1])
                .thenAcceptAsync(m -> textService.send(m, source));
        } else {
            memberManager.deleteNickName(userService.getUserName((TPlayer) source))
                .thenAcceptAsync(m -> textService.send(m, source));
        }
    }

    @Override
    public void exceptionCommand(TCommandSource source, String[] args) {

        if (args.length == 0) {
            textService.send(pluginMessages.getNotEnoughArgs(), source);
            textService.send(pluginMessages.exceptionAddCommandUsage(), source);
            return;
        }
        switch (args[0]) {
            case "list": {
                if (permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.LANGUAGE_LIST))) {
                    textService.send(textService.of(String.join(
                        ", ",
                        registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS))),
                        source);
                    return;
                } else {
                    textService.send(pluginMessages.getNoPermission(), source);
                }
                return;
            }
            case "add": {
                if (permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).isEmpty()) {
                        configurationService
                            .addToCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                        configurationService.save();
                        textService.send(pluginMessages.getNewException(args[1]), source);
                    } else if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                        textService.send(pluginMessages.getExistingException(args[1]), source);
                    } else {
                        configurationService
                            .addToCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
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
                if (permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    if (!registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).contains(args[1])) {
                        textService.send(pluginMessages.getMissingException(args[1]), source);
                    } else {
                        configurationService
                            .removeFromCollection(CatalystKeys.CHAT_FILTER_EXCEPTIONS, args[1]);
                        configurationService.save();
                        textService.send(pluginMessages.getRemoveException(args[1]), source);
                    }
                } else {
                    textService.send(pluginMessages.getNoPermission(), source);
                }
            }
        }
    }

    @Override
    public void findCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void ignoreCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void infoCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void kickCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void listCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void messageCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void muteCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void nickNameCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void replyCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void sendCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void socialSpyCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void staffChatCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void staffListCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void swearCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void tempBanCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void tempMuteCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void unBanCommand(TCommandSource source, String[] args) {

    }

    @Override
    public void unMuteCommand(TCommandSource source, String[] args) {

    }
}
