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

package org.anvilpowered.catalyst.common.member;

import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.core.coremember.CoreMemberManager;
import org.anvilpowered.anvil.api.core.coremember.repository.CoreMemberRepository;
import org.anvilpowered.anvil.api.core.model.coremember.CoreMember;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.KickService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.TimeFormatService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.anvil.base.datastore.BaseManager;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
    TUser,
    TPlayer,
    TString,
    TCommandSource>
    extends BaseManager<CoreMemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    protected CurrentServerService currentServerService;

    @Inject
    protected KickService kickService;

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected CatalystPluginMessages<TString, TCommandSource> pluginMessages;

    @Inject
    protected TextService<TString, TCommandSource> textService;

    @Inject
    protected TimeFormatService timeFormatService;

    @Inject
    protected UserService<TUser, TPlayer> userService;

    @Inject
    protected ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    public CommonMemberManager(Registry registry) {
        super(registry);
    }

    @Override
    public CoreMemberRepository<?, ?> getPrimaryComponent() {
        return Anvil.getEnvironmentManager().getCoreEnvironment().getInjector().getInstance(CoreMemberManager.class).getPrimaryComponent();
    }

    @Override
    public CompletableFuture<TString> info(String userName, boolean isOnline,
                                           boolean[] permissions) {
        return getPrimaryComponent().getOneForUser(userName).thenApplyAsync(optionalMember -> {
                if (!optionalMember.isPresent()) {
                    return textService.fail("Could not get user data");
                }
                CoreMember<?> member = optionalMember.get();
                String nick;
                String lastSeen;
                String banReason;
                if (member.getNickName() != null) {
                    nick = member.getNickName();
                } else {
                    nick = "No Nickname.";
                }
                if (isOnline) {
                    lastSeen = "Currently Online.";
                } else {
                    lastSeen = timeFormatService.format(member.getLastJoinedUtc()).toString();
                }
                if (member.isBanned()) {
                    banReason = member.getBanReason();
                } else {
                    banReason = "This user is not banned.";
                }

                TextService.Builder<TString, TCommandSource> message = textService.builder();
                message.append(
                    textService.builder()
                        .blue().append("----------------Player Info----------------"));
                message.append(
                    textService.builder()
                    .blue().append("\nUUID : "))
                    .append(
                        textService.builder()
                        .green().append(member.getUserUUID())
                    );
                message.append(
                    textService.builder()
                        .blue().append("\nUsername : "))
                    .append(
                        textService.builder()
                            .green().append(member.getUserName()))
                    .append(
                        textService.builder()
                            .blue().append("\nNickname : ")
                    )
                    .append(
                        textService
                            .deserialize(nick)
                    );

                if (permissions[0]) {
                    message.append(
                        textService.builder()
                            .blue().append("\nIP : ")
                    )
                        .append(
                            textService.builder()
                                .green().append(member.getIpAddress())
                        );
                }
                message.append(
                    textService.builder().blue().append("\nJoined Date : "))
                    .append(textService.builder().green().append(member.getCreatedUtc().toString()))
                    .append(textService.builder().blue().append("\nLast Seen : "))
                    .append(textService.builder().green().append(lastSeen));

                if (permissions[1]) {
                    message.append(
                        textService.builder().blue().append("\nBanned : ")
                            .append(textService.builder().green().append(banReason))
                    );
                }

                if (permissions[2]) {
                    message.append(
                        textService.builder().blue().append("\nChannel : ")
                            .append(textService.builder().green()
                                .append(chatService.getChannelIdForUser(member.getUserUUID())))
                    );
                }
                message.append(
                    textService.builder()
                        .blue().append("\nCurrent Server : ")
                        .append(
                            textService.builder()
                                .gold().append(
                                currentServerService.getName(
                                    member.getUserUUID()).orElse("Offline User.")))
                );
                return message.build();
            }
        ).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    @Override
    public CompletableFuture<TString> setNickName(String userName, String nickName) {
        return getPrimaryComponent().setNickNameForUser(userName, "~" + nickName).thenApplyAsync(result -> {
            if (result) {
                return textService.success("Set nickname to " + nickName);
            } else {
                return textService.fail("Failed to set the nickname " + nickName);
            }
        });
    }

    @Override
    public CompletableFuture<TString> setNickNameForUser(String userName, String nickName) {
        return getPrimaryComponent().setNickNameForUser(userName, registry.getOrDefault(CatalystKeys.NICKNAME_PREFIX) + nickName).thenApplyAsync(result -> {
            if (result) {
                userService.getPlayer(userName).ifPresent(p -> textService.builder().green().append("Your nickname was set to " + nickName).sendTo((TCommandSource) p));
                return textService.success("Set " + userName + "'s nickname to " + nickName);
            } else {
                return textService.fail("Failed to set the nickname for " + userName);
            }
        });
    }

    @Override
    public CompletableFuture<TString> deleteNickNameForUser(String userName) {
        return getPrimaryComponent().deleteNickNameForUser(userName).thenApplyAsync(result -> {
            if (result) {
                userService.getPlayer(userName).ifPresent(p -> textService.builder().green().append("Your nickname was deleted.").sendTo((TCommandSource) p));
                return textService.success("Successfully deleted " + userName + "'s nickname.");
            } else {
                return textService.fail("Failed to delete " + userName + "'s nickname.");
            }
        });
    }

    @Override
    public CompletableFuture<TString> deleteNickName(String userName) {
        return getPrimaryComponent().deleteNickNameForUser(userName).thenApplyAsync(result -> {
            if (result) {
                return textService.success("Successfully deleted your nickname.");
            } else {
                return textService.fail("Failed to delete your nickname.");
            }
        });
    }

    @Override
    public CompletableFuture<TString> ban(String userName, String reason) {
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600));
        return getPrimaryComponent().banUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc));
                return textService.success("Banned " + userName + " for " + reason);
            }
            return textService.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> ban(String userName) {
        return ban(userName, "The ban hammer has spoken.");
    }

    @Override
    public CompletableFuture<TString> tempBan(String userName, String duration, String reason) {
        Optional<Duration> optionalDuration = timeFormatService.parseDuration(duration);
        if (!optionalDuration.isPresent()) {
            return CompletableFuture.completedFuture(textService.fail("Invalid input for duration"));
        }
        Duration dur = optionalDuration.get();
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur);
        return getPrimaryComponent().banUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc));
                return textService.success("Banned " + userName + " for " + reason + " for " + timeFormatService.format(dur).toString());
            }
            return textService.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> tempBan(String userName, String duration) {
        return tempBan(userName, duration, "The ban hammer has spoken.");
    }

    @Override
    public CompletableFuture<TString> unBan(String userName) {
        return getPrimaryComponent().unBanUser(userName).thenApplyAsync(b -> {
            if (b) {
                return textService.success("Unbanned " + userName);
            }
            return textService.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> mute(String userName, String reason) {
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600));
        return getPrimaryComponent().muteUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                userService.getPlayer(userName).ifPresent(p -> textService.send(pluginMessages.getMuteMessage(reason, endUtc), (TCommandSource) p));
                return textService.success("Muted " + userName);
            }
            return textService.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> mute(String userName) {
        return mute(userName, "You have been muted.");
    }

    @Override
    public CompletableFuture<TString> tempMute(String userName, String duration, String reason) {
        Optional<Duration> optionalDuration = timeFormatService.parseDuration(duration);
        if (!optionalDuration.isPresent()) {
            return CompletableFuture.completedFuture(textService.fail("Invalid input for duration"));
        }
        Duration dur = optionalDuration.get();
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur);
        return getPrimaryComponent().muteUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                userService.getPlayer(userName).ifPresent(p -> textService.send(pluginMessages.getMuteMessage(reason, endUtc), (TCommandSource) p));
                return textService.success("Muted " + userName + " for " + reason + " for " + timeFormatService.format(dur).toString());
            }
            return textService.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> tempMute(String userName, String duration) {
        return tempMute(userName, duration, "You have been muted.");
    }

    @Override
    public CompletableFuture<TString> unMute(String userName) {
        return getPrimaryComponent().unMuteUser(userName).thenApplyAsync(b -> {
            if (b) {
                userService.getPlayer(userName).ifPresent(p -> {
                    textService.builder()
                        .append(pluginInfo.getPrefix())
                        .yellow().append("You have been unmuted.")
                        .sendTo((TCommandSource) p);
                });
                return textService.success("UnMuted " + userName);
            }
            return textService.fail("Invalid user.");
        });
    }
}
