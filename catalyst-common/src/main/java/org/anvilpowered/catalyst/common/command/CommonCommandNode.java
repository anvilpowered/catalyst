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

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CommonCommandNode<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> implements CommandNode<TCommandSource> {

    @Inject
    private CommonBanCommand<TString, TPlayer, TCommandSource> banCommand;

    @Inject
    private CommonTempBanCommand<TString, TPlayer, TCommandSource> tempBanCommand;

    @Inject
    private CommonUnBanCommand<TString, TCommandSource> unBanCommand;

    @Inject
    private CommonBroadcastCommand<TString, TCommandSource> broadcastCommand;

    @Inject
    private CommonDeleteNicknameCommand<TString, TPlayer, TCommandSource> deleteNickCommand;

    @Inject
    private CommonNickNameCommand<TString, TPlayer, TCommandSource> nickCommand;

    @Inject
    private CommonFindCommand<TString, TPlayer, TCommandSource> findCommand;

    @Inject
    private CommonInfoCommand<TString, TPlayer, TCommandSource> infoCommand;

    @Inject
    private CommonKickCommand<TString, TPlayer, TCommandSource> kickCommand;

    @Inject
    private CommonMessageCommand<TString, TPlayer, TCommandSource> messageCommand;

    @Inject
    private CommonReplyCommand<TString, TPlayer, TCommandSource> replyCommand;

    @Inject
    private CommonMuteCommand<TString, TPlayer, TCommandSource> muteCommand;

    @Inject
    private CommonTempMuteCommand<TString, TPlayer, TCommandSource> tempMuteCommand;

    @Inject
    private CommonUnMuteCommand<TString, TCommandSource> unMuteCommand;

    @Inject
    private CommonSocialSpyCommand<TString, TPlayer, TCommandSource> socialSpyCommand;

    @Inject
    private CommonStaffChatCommand<TString, TPlayer, TCommandSource> staffChatCommand;

    @Inject
    private CommonExceptionCommand<TString, TCommandSource> exceptionCommand;

    @Inject
    private CommonSwearCommand<TString, TCommandSource> swearCommand;

    private boolean alreadyLoaded;
    protected Map<List<String>, Function<TCommandSource, String>> descriptions;
    protected Map<List<String>, Predicate<TCommandSource>> permissions;
    protected Map<List<String>, Function<TCommandSource, String>> usages;
    protected Map<List<String>, LiteralCommandNode<TCommandSource>> commands;

    @Inject
    protected PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    protected Registry registry;
    protected Class<?> playerClass;
    protected Class<?> consoleClass;

    protected CommonCommandNode(Registry registry, Class<?> playerClass, Class<?> consoleClass) {
        this.registry = registry;
        this.playerClass = playerClass;
        this.consoleClass = consoleClass;
        registry.whenLoaded(() -> {
            if (alreadyLoaded) return;
            loadNodes();
            loadCommands();
            alreadyLoaded = true;
        }).register();
        alreadyLoaded = false;
        descriptions = new HashMap<>();
        permissions = new HashMap<>();
        usages = new HashMap<>();
    }

    protected abstract void loadCommands();

    private void loadNodes() {
        commands = new HashMap<>();
        final LiteralCommandNode<TCommandSource> ban = LiteralArgumentBuilder
            .<TCommandSource>literal("ban")
            .requires(source -> permissionService.hasPermission(source,
                registry.getOrDefault(CatalystKeys.BAN_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.banCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.string())
                .suggests(suggest())
                .executes(banCommand::withoutReason)
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "reason", StringArgumentType.greedyString())
                    .executes(banCommand::withReason)
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> tempBan = LiteralArgumentBuilder
            .<TCommandSource>literal("tempban")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.TEMP_BAN_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(textService.of(pluginMessages.tempBanCommandUsage()), e.getSource());
                return 1;
            }).then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.string())
                .executes(e -> {
                    textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                    textService.send(textService.of(pluginMessages.tempBanCommandUsage()), e.getSource());
                    return 1;
                })
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "duration", StringArgumentType.string())
                    .executes(tempBanCommand::withoutReason)
                    .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                        "reason", StringArgumentType.greedyString())
                        .executes(tempBanCommand::withReason))
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> unban = LiteralArgumentBuilder.<TCommandSource>literal("unban")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.BAN_PERMISSION)))
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(textService.of(pluginMessages.unbanCommandUsage()), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.string())
                .executes(unBanCommand::unban)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> broadcast = LiteralArgumentBuilder
            .<TCommandSource>literal("broadcast")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.BROADCAST_PERMISSION)))
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(pluginMessages.broadcastCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "message", StringArgumentType.greedyString())
                .executes(broadcastCommand::execute)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> delNick = LiteralArgumentBuilder
            .<TCommandSource>literal("delnick")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.NICKNAME_PERMISSION)))
            .executes(ctx -> deleteNickCommand.execute(ctx, playerClass))
            .then(LiteralArgumentBuilder.<TCommandSource>literal(
                "other")
                .requires(source ->
                    permissionService.hasPermission(source,
                        registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)))
                .executes(e -> {
                    textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                    textService.send(pluginMessages.nickNameCommandUsage(), e.getSource());
                    return 1;
                })
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "target", StringArgumentType.string())
                    .requires(source ->
                        permissionService.hasPermission(source,
                            registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)))
                    .suggests(suggest())
                    .executes(deleteNickCommand::executeOther)
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> nickName = LiteralArgumentBuilder
            .<TCommandSource>literal("nick")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.NICKNAME_PERMISSION)))
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(pluginMessages.nickNameCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "nickname", StringArgumentType.string())
                .executes(ctx -> nickCommand.execute(ctx, playerClass))
                .build())
            .then(LiteralArgumentBuilder.<TCommandSource>literal("other")
                .requires(source ->
                    permissionService.hasPermission(source,
                        registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)))
                .executes(e -> {
                    textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                    textService.send(pluginMessages.nickNameCommandUsage(), e.getSource());
                    return 1;
                })
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "target", StringArgumentType.word())
                    .suggests(suggest())
                    .executes(e -> {
                        textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                        textService.send(pluginMessages.nickNameCommandUsage(), e.getSource());
                        return 1;
                    })
                    .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                        "targetnick", StringArgumentType.word())
                        .executes(nickCommand::executeOther))
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> find = LiteralArgumentBuilder
            .<TCommandSource>literal("find")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.FIND_PERMISSION)))
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(pluginMessages.findCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.string())
                .suggests(suggest())
                .executes(findCommand::execute)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> info = LiteralArgumentBuilder
            .<TCommandSource>literal("info")
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(pluginMessages.infoCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument("target", StringArgumentType.string())
                .suggests(suggest())
                .executes(infoCommand::execute)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> kick = LiteralArgumentBuilder
            .<TCommandSource>literal("kick")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.KICK_PERMISSION)))
            .executes(e -> {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), e.getSource());
                textService.send(pluginMessages.kickCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument("target", StringArgumentType.word())
                .suggests(suggest())
                .executes(kickCommand::withoutReason)
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "reason", StringArgumentType.greedyString())
                    .executes(kickCommand::execute)
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> message = LiteralArgumentBuilder
            .<TCommandSource>literal("msg")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MESSAGE_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.messageCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument("target", StringArgumentType.word())
                .suggests(suggest())
                .executes(e -> {
                    textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                    textService.send(pluginMessages.messageCommandUsage(), e.getSource());
                    return 1;
                })
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "message",
                    StringArgumentType.greedyString())
                    .executes(ctx -> messageCommand.execute(ctx, consoleClass))
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> reply = LiteralArgumentBuilder
            .<TCommandSource>literal("reply")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MESSAGE_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.messageCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "message", StringArgumentType.greedyString())
                .executes(replyCommand::execute)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> mute = LiteralArgumentBuilder
            .<TCommandSource>literal("mute")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.muteCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.word())
                .suggests(suggest())
                .executes(muteCommand::withoutReason)
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "reason", StringArgumentType.greedyString())
                    .executes(muteCommand::execute)
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> tempMute = LiteralArgumentBuilder
            .<TCommandSource>literal("tempmute")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.tempMuteCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.word())
                .suggests(suggest())
                .executes(e -> {
                    textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                    textService.send(pluginMessages.tempMuteCommandUsage(), e.getSource());
                    return 1;
                })
                .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                    "duration", StringArgumentType.word())
                    .executes(tempMuteCommand::withoutReason)
                    .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                        "reason", StringArgumentType.greedyString())
                        .executes(tempMuteCommand::withReason)
                        .build())
                    .build())
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> unmute = LiteralArgumentBuilder
            .<TCommandSource>literal("unmute")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)))
            .executes(e -> {
                textService.send(pluginMessages.getNotEnoughArgs(), e.getSource());
                textService.send(pluginMessages.unMuteCommandUsage(), e.getSource());
                return 1;
            })
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "target", StringArgumentType.word())
                .suggests(suggest())
                .executes(unMuteCommand::execute)
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> socialSpy = LiteralArgumentBuilder
            .<TCommandSource>literal("socialspy")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.SOCIALSPY_PERMISSION)))
            .executes(ctx -> socialSpyCommand.execute(ctx, playerClass))
            .build();
        final LiteralCommandNode<TCommandSource> staffChat = LiteralArgumentBuilder
            .<TCommandSource>literal("staffchat")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.STAFFCHAT_PERMISSION)))
            .executes(ctx -> staffChatCommand.toggle(ctx, playerClass))
            .then(RequiredArgumentBuilder.<TCommandSource, String>argument(
                "message", StringArgumentType.greedyString())
                .executes(ctx -> staffChatCommand.execute(ctx, playerClass))
                .build())
            .build();
        final LiteralCommandNode<TCommandSource> exception = LiteralArgumentBuilder
            .<TCommandSource>literal("exception")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.LANGUAGE_LIST_PERMISSION)))
            .executes(exceptionCommand::execute)
            .build();
        final LiteralCommandNode<TCommandSource> swear = LiteralArgumentBuilder
            .<TCommandSource>literal("swear")
            .requires(source ->
                permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.LANGUAGE_LIST_PERMISSION)))
            .executes(swearCommand::execute)
            .build();

        if (registry.getOrDefault(CatalystKeys.BAN_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("tempban","ctempban"), tempBan);
            commands.put(ImmutableList.of("ban", "cban"), ban);
            commands.put(ImmutableList.of("unban", "cunban"), unban);
        }
        if (registry.getOrDefault(CatalystKeys.BROADCAST_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("broadcast", "cbroadcast"), broadcast);
        }
        if (registry.getOrDefault(CatalystKeys.NICKNAME_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("delnick", "deletenick"), delNick);
            commands.put(ImmutableList.of("nick", "nickname"), nickName);
        }
        if (registry.getOrDefault(CatalystKeys.FIND_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("find", "cfind"), find);
        }
        if (registry.getOrDefault(CatalystKeys.INFO_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("info", "cinfo"), info);
        }
        if (registry.getOrDefault(CatalystKeys.KICK_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("kick", "ckick"), kick);
        }
        if (registry.getOrDefault(CatalystKeys.MESSAGE_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("msg", "m", "w", "t", "whisper", "tell"), message);
            commands.put(ImmutableList.of("reply", "r"), reply);
        }
        if (registry.getOrDefault(CatalystKeys.MUTE_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("mute", "cmute"), mute);
            commands.put(ImmutableList.of("tempmute", "ctempmute"), tempMute);
            commands.put(ImmutableList.of("unmute", "cunmute"), unmute);
        }
        if (registry.getOrDefault(CatalystKeys.SOCIALSPY_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("socialspy", "ss"), socialSpy);
        }
        if (registry.getOrDefault(CatalystKeys.STAFFCHAT_COMMAND_ENABLED)) {
            commands.put(ImmutableList.of("staffchat", "sc"), staffChat);
        }
        if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_ENABLED)) {
            commands.put(ImmutableList.of("exception"), exception);
            commands.put(ImmutableList.of("swear"), swear);
        }
    }

    private SuggestionProvider<TCommandSource> suggest() {
        return (context, builder) -> {
            for (TPlayer player : userService.getOnlinePlayers()) {
                builder.suggest(userService.getUserName(player), () -> "target");
            }
            return builder.buildFuture();
        };
    }

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
