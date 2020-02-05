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

package rocks.milspecsg.msessentials.common.member;

import rocks.milspecsg.mscore.api.coremember.repository.CoreMemberRepository;
import rocks.milspecsg.mscore.api.model.coremember.CoreMember;
import rocks.milspecsg.mscore.api.plugin.PluginMessages;
import rocks.milspecsg.mscore.common.plugin.MSCore;
import rocks.milspecsg.msessentials.api.chat.ChatService;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.util.CurrentServerService;
import rocks.milspecsg.msrepository.api.util.KickService;
import rocks.milspecsg.msrepository.api.plugin.PluginInfo;
import rocks.milspecsg.msrepository.api.util.StringResult;
import rocks.milspecsg.msrepository.api.util.TimeFormatService;
import rocks.milspecsg.msrepository.api.util.UserService;
import rocks.milspecsg.msrepository.common.manager.CommonManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends CommonManager<CoreMemberRepository<?, ?>>
    implements MemberManager<TString> {

    @Inject
    protected CurrentServerService currentServerService;

    @Inject
    protected KickService kickService;

    @Inject
    protected PluginInfo<TString> pluginInfo;

    // not from this injector
    protected PluginMessages<TString> pluginMessages;

    @Inject
    protected StringResult<TString, TCommandSource> stringResult;

    @Inject
    protected TimeFormatService timeFormatService;

    @Inject
    protected UserService<TUser, TPlayer> userService;

    @Inject
    protected ChatService<TString> chatService;

    @Inject
    public CommonMemberManager(Registry registry) {
        super(registry);
        pluginMessages = MSCore.getPluginMessages();
    }

    @Override
    public CoreMemberRepository<?, ?> getPrimaryComponent() {
        return MSCore.getCoreMemberRepository();
    }

    @Override
    public CompletableFuture<TString> info(String userName, boolean isOnline) {
        return getPrimaryComponent().getOneForUser(userName).thenApplyAsync(optionalMember -> {
                if (!optionalMember.isPresent()) {
                    return stringResult.fail("Could not get user data");
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
                    lastSeen = timeFormatService.format(member.getLastJoinedUtc());
                }
                if (member.isBanned()) {
                    banReason = member.getBanReason();
                } else {
                    banReason = "This user is not banned.";
                }

                return stringResult.builder()
                    .append(
                        stringResult.builder()
                            .blue().append("----------------Player Info----------------"))
                    .append(
                        stringResult.builder()
                            .blue().append("\nUsername : ")
                    )
                    .append(
                        stringResult.builder()
                            .green().append(userName))
                    .append(
                        stringResult.builder()
                            .blue().append("\nNickname : ")
                    )
                    .append(
                        stringResult
                            .deserialize(nick)
                    )
                    .append(
                        stringResult.builder()
                            .blue().append("\nIP : ")
                    )
                    .append(
                        stringResult.builder()
                            .green().append(member.getIpAddress())
                    )
                    .append(
                        stringResult.builder()
                            .blue().append("\nJoined Date : ")
                    )
                    .append(
                        stringResult.builder()
                            .green().append(member.getCreatedUtc().toString())
                    )
                    .append(
                        stringResult.builder()
                            .blue().append("\nLast Seen : ")
                    )
                    .append(
                        stringResult.builder()
                            .green().append(lastSeen))
                    .append(
                        stringResult.builder()
                            .blue().append("\nBanned : ")
                    )
                    .append(
                        stringResult.builder()
                            .green().append(banReason))
                    .append(
                        stringResult.builder()
                        .blue().append("\nChannel : ")
                    )
                    .append(
                        stringResult.builder()
                        .green().append(chatService.getChannelId(member.getUserUUID()))
                    )
                    .append(
                        stringResult.builder()
                            .blue().append("\nCurrent Server : ")
                    )
                    .append(
                        stringResult.builder()
                            .gold().append(currentServerService.getCurrentServerName(member.getUserUUID()).orElse("Offline User."))
                    )
                    .build();
            }
        );
    }

    @Override
    public CompletableFuture<TString> setNickName(String userName, String nickName) {
        return getPrimaryComponent().setNickNameForUser(userName, "~" + nickName).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Set nickname to " + nickName);
            } else {
                return stringResult.fail("Failed to set the nickname " + nickName);
            }
        });
    }

    @Override
    public CompletableFuture<TString> setNickNameForUser(String userName, String nickName) {
        return getPrimaryComponent().setNickNameForUser(userName, "~" + nickName).thenApplyAsync(result -> {
            if (result) {
                userService.getPlayer(userName).ifPresent(stringResult.builder().green().append("Your nickname was set to " + nickName)::sendTo);
                return stringResult.success("Set " + userName + "'s nickname to " + nickName);
            } else {
                return stringResult.fail("Failed to set the nickname for " + userName);
            }
        });
    }

    @Override
    public CompletableFuture<TString> deleteNickNameForUser(String userName) {
        return getPrimaryComponent().deleteNickNameForUser(userName).thenApplyAsync(result -> {
            if (result) {
                userService.getPlayer(userName).ifPresent(stringResult.builder().green().append("Your nickname was deleted.")::sendTo);
                return stringResult.success("Successfully deleted " + userName + "'s nickname.");
            } else {
                return stringResult.fail("Failed to delete " + userName + "'s nickname.");
            }
        });
    }

    @Override
    public CompletableFuture<TString> deleteNickName(String userName) {
        return getPrimaryComponent().deleteNickNameForUser(userName).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Successfully deleted your nickname.");
            } else {
                return stringResult.fail("Failed to delete your nickname.");
            }
        });
    }

    @Override
    public CompletableFuture<TString> ban(String userName, String reason) {
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600));
        return getPrimaryComponent().banUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc));
                return stringResult.success("Banned " + userName + " for " + reason);
            }
            return stringResult.fail("Invalid user.");
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
            return CompletableFuture.completedFuture(stringResult.fail("Invalid input for duration"));
        }
        Duration dur = optionalDuration.get();
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur);
        return getPrimaryComponent().banUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc));
                return stringResult.success("Banned " + userName + " for " + reason + " for " + timeFormatService.format(dur));
            }
            return stringResult.fail("Invalid user.");
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
                return stringResult.success("Unbanned " + userName);
            }
            return stringResult.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> mute(String userName, String reason) {
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600));
        return getPrimaryComponent().muteUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                userService.getPlayer(userName).ifPresent(p -> stringResult.send(pluginMessages.getMuteMessage(reason, endUtc), p));
                return stringResult.success("Muted " + userName);
            }
            return stringResult.fail("Invalid user.");
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
            return CompletableFuture.completedFuture(stringResult.fail("Invalid input for duration"));
        }
        Duration dur = optionalDuration.get();
        Instant endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur);
        return getPrimaryComponent().muteUser(userName, endUtc, reason).thenApplyAsync(b -> {
            if (b) {
                userService.getPlayer(userName).ifPresent(p -> stringResult.send(pluginMessages.getMuteMessage(reason, endUtc), p));
                return stringResult.success("Muted " + userName + " for " + reason + " for " + timeFormatService.format(dur));
            }
            return stringResult.fail("Invalid user.");
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
                    stringResult.builder()
                        .append(pluginInfo.getPrefix())
                        .yellow().append("You have been unmuted.")
                        .sendTo(p);
                });
                return stringResult.success("UnMuted " + userName);
            }
            return stringResult.fail("Invalid user.");
        });
    }
}
