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

package rocks.milspecsg.msessentials.service.common.member;

import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.CurrentServerService;
import rocks.milspecsg.msrepository.api.KickService;
import rocks.milspecsg.msrepository.api.UserService;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;
import rocks.milspecsg.msrepository.service.common.manager.CommonManager;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMemberManager<
        TUser extends TCommandSource,
        TString,
        TCommandSource>
        extends CommonManager<MemberRepository<?, ?, ?>> implements MemberManager<TString> {

    @Inject
    protected StringResult<TString, TCommandSource> stringResult;

    @Inject
    protected CurrentServerService currentServerService;

    @Inject
    protected KickService kickService;

    @Inject
    protected UserService<TUser> userService;

    @Inject
    protected CommonMemberManager(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public CompletableFuture<TString> info(String username, boolean isOnline) {
        return getPrimaryComponent().getOneForUser(username).thenApplyAsync(optionalMember -> {
            System.out.println("46");
                    if (!optionalMember.isPresent()) {
                        return stringResult.fail("Could not get user data");
                    }
                    Member<?> member = optionalMember.get();
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
                        lastSeen = member.getLastSeenUtc().toString();
                    }
                    if (member.getBanStatus()) {
                        banReason = member.getBanReason();
                    } else {
                        banReason = "This user is not banned.";
                    }
                    System.out.println(username);

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
                                            .green().append(username))
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
                                            .green().append(member.getIPAddress())
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nJoined Date : ")
                            )
                            .append(
                                    stringResult.builder()
                                            .green().append(member.getCreatedUtcDate().toString())
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
    public CompletableFuture<TString> setLastSeenUtc(UUID userUUID, Date lastSeenUtc) {
        return getPrimaryComponent().setLastSeenUtcForUser(userUUID, lastSeenUtc).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("successfully updated lastSeenUtc");
            } else {
                return stringResult.fail("Failed to update lastSeenUtc");
            }
        });
    }


    @Override
    public CompletableFuture<TString> setNickName(String userName, String nickName) {
        return getPrimaryComponent().setNickNameForUser(userName, nickName).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Set nickname to " + nickName);
            } else {
                return stringResult.fail("Failed to set the nickname " + nickName);
            }
        });
    }

    @Override
    public CompletableFuture<TString> deleteNickname(String username) {
        return getPrimaryComponent().setNickNameForUser(username, username).thenApplyAsync(result -> {
            if(result) {
                return stringResult.success("Successfully deleted your nickname.");
            } else {
                return stringResult.fail("Failed to delete your nickname.");
            }
        });
    }

    public CompletableFuture<TString> formatMessage(String prefix, String nameColor, String name, String message, String suffix, boolean hasPermission) {
        return getPrimaryComponent().getOneForUser(name).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return stringResult.fail("Couldn't find a user matching that name!");
            }
            if(optionalMember.get().getMuteStatus()) {
                return stringResult.fail("You are muted!");
            }
            String finalName = optionalMember.get().getUserName();
            if(optionalMember.get().getNickName() != finalName) {
                finalName = optionalMember.get().getNickName();
            }
            stringResult.deserialize(nameColor);
            return stringResult
                    .builder()
                    .append(stringResult.deserialize(prefix))
                    .append(stringResult.deserialize(nameColor + finalName))
                    .append(": ")
                    .append(stringResult.deserialize(message))
                    .onHoverShowText(stringResult.builder().append(name).build())
                    .onClickSuggestCommand("/msg " + name)
                    .build();
        });
    }

    @Override
    public CompletableFuture<Void> syncPlayerInfo(UUID playerUUID, String ipAddress, String username) {
        boolean[] flags = {false};
        return getPrimaryComponent().getOneOrGenerateForUser(playerUUID, ipAddress, username, flags)
                .thenAcceptAsync(optionalMember -> {
                    if (!optionalMember.isPresent()) {
                        return;
                    }
                    if (optionalMember.get().getBanStatus()) {
                        kickService.kick(playerUUID, optionalMember.get().getBanReason());
                    } else if (flags[0]) {
                        //If the player is new
                        userService.get(playerUUID).ifPresent(user -> stringResult.send(stringResult.deserialize(configurationService.getConfigString(ConfigKeys.WELCOME_MESSAGE)), user));
                    }
                });
    }

    @Override
    public CompletableFuture<TString> ban(String username, String reason) {
        return getPrimaryComponent().setBannedForUser(username, true, reason).thenApplyAsync(optionalUUID -> {
            if (optionalUUID.isPresent()) {
                kickService.kick(optionalUUID.get(), reason);
                return stringResult.success("Banned " + username + " for " + reason);
            }
            return stringResult.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> ban(String userName) {
        return ban(userName, "The ban hammer has spoken.");
    }

    @Override
    public CompletableFuture<TString> unBan(String userName) {
        return getPrimaryComponent().setBannedForUser(userName, false, "").thenApplyAsync(optionalUUID -> {
            if (optionalUUID.isPresent()) {
                return stringResult.success("Unbanned " + userName);
            }
            return stringResult.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> mute(String userName) {
        return getPrimaryComponent().setMuteStatusForUser(userName, true).thenApplyAsync(optionalUUID -> {
            if(optionalUUID.isPresent()) {
                return stringResult.success("Muted " + userName);
            }
            return stringResult.fail("Invalid user.");
        });
    }

    @Override
    public CompletableFuture<TString> unMute(String userName) {
        return getPrimaryComponent().setMuteStatusForUser(userName, false).thenApplyAsync(optionalUUID -> {
            if(optionalUUID.isPresent()) {
                return stringResult.success("UnMuted " + userName);
            }
            return stringResult.fail("Invalid user.");
        });
    }
}
