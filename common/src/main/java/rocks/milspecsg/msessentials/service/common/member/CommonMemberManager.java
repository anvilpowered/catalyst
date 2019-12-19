package rocks.milspecsg.msessentials.service.common.member;

import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.CurrentServerService;
import rocks.milspecsg.msrepository.api.UserService;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;
import rocks.milspecsg.msrepository.service.common.manager.CommonManager;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class CommonMemberManager<TUser, TString, TCommandSource> extends CommonManager<MemberRepository<?, ?, ?>> implements MemberManager<TString> {

    @Inject
    protected StringResult<TString, TCommandSource> stringResult;

    @Inject
    protected UserService<TUser> userService;

    @Inject
    protected CurrentServerService currentServerService;

    @Inject
    protected CommonMemberManager(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public CompletableFuture<TString> info(String username, boolean isOnline) {
        return getPrimaryComponent().getOneForUser(username).thenApplyAsync(optionalMember -> {
                    if (!optionalMember.isPresent()) {

                        return stringResult.fail("Could not get user data");
                    }
                    Member<?> member = optionalMember.get();
                    String nick;
                    String lastSeen;
                    if (member.getNickname() != null) {
                        nick = member.getNickname();
                    } else {
                        nick = "No Nickname.";
                    }
                    if (isOnline) {
                        lastSeen = "Currently Online.";
                    } else {
                        lastSeen = member.getLastSeenDateUtc().toString();
                    }
                    return stringResult.builder()
                            .append(
                                    stringResult.builder()
                                            .blue().append("----------------Player Info----------------"))
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nUsername : " + username)
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nNickname : ")
                            )
                            .append(
                                    stringResult.builder()
                                            .deserialize(nick)
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nIP : " + member.getIPAddress())
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nJoined Date : " + member.getJoinDateUtc().toString())
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nLast Seen : " + lastSeen)
                            )
                            .append(
                                    stringResult.builder()
                                            .blue().append("\nCurrent Server : " + currentServerService.getCurrentServerName(member.getUserUUID()).orElse("Offline User."))
                            )
                            .build();

                }
        );
    }


    @Override
    public CompletableFuture<TString> setJoinedUtc(UUID userUUID, Date joinedUtc) {
        return getPrimaryComponent().setJoinedUtcForUser(userUUID, joinedUtc).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Welcome");
            } else {
                return stringResult.fail("Failed to set joinedUtc");
            }
        });
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
    public CompletableFuture<TString> setNickname(UUID userUUID, String nickname) {
        return getPrimaryComponent().setNicknameForUser(userUUID, nickname).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("Set nickname to " + nickname);
            } else {
                return stringResult.fail("Failed to set the nickname " + nickname);
            }
        });
    }

    @Override
    public CompletableFuture<TString> setIPAddress(String username, String ipAddress) {
        return getPrimaryComponent().setIPAddressForUser(username, ipAddress).thenApplyAsync(result -> {
            if (result) {
                return stringResult.success("set ip address");
            }
            System.out.println("Failed to set ip");
            return stringResult.fail("failed to set ip");
        });
    }

    @Override
    public CompletableFuture<TString> getIPAddress(String username) {
        return getPrimaryComponent().getOneForUser(username).thenApplyAsync(optionalMember -> {
            if(!optionalMember.isPresent()) {
                return stringResult.fail("Couldn't get the ip address for " + username);
            } else {
                Member<?> member = optionalMember.get();
                return stringResult.builder().append(member.getIPAddress()).build();
            }
        });
    }

    @Override
    public CompletableFuture<TString> delNick(UUID userUUID) {
        return null;
    }

    @Override
    public CompletableFuture<Void> sync(UUID userUUID) {
        return null;
    }

    @Override
    public CompletableFuture<TString> getNickname(String username) {
        return getPrimaryComponent().getOneForUser(username).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {

                return stringResult.fail("Could not get user data");
            }
            Member<?> member = optionalMember.get();
            String nick;
            if (member.getNickname() != null) {
                nick = member.getNickname();
            } else {
                nick = username;
            }
            return stringResult.builder().deserialize(nick).build();
        });
    }


    public CompletableFuture<TString> formatMessage(String prefix, String nameColor, String name, String message, String suffix, boolean hasPermission) {
        return getPrimaryComponent().getOneForUser(name).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return stringResult.fail("Couldn't find a user matching that name!");
            }
            try {

                return stringResult
                        .builder()
                        .deserialize(prefix)
                        .append(getNickname(name).get())
                        .append(": ")
                        .deserialize(message)
                        .onHoverShowText(stringResult.builder().append(name).build())
                        .onClickRunCommand("/msg " + name)
                        .build();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public CompletableFuture<TString> setBanned(String username, boolean isBanned) {
        return getPrimaryComponent().setBannedForUser(username, isBanned).thenApplyAsync(result  -> {
            if(result) {
                if(isBanned) {
                    return stringResult.success("Banned " + username);
                } else {
                    return stringResult.success("UnBanned " + username);
                }
            } else {
                return stringResult.fail("Failed to set ban status for " + username);
            }
        });
    }

    @Override
    public CompletableFuture<TString> kick(String username) {
        return null;
    }

    public CompletableFuture<TString> setBanReason(String username, String reason) {
        return getPrimaryComponent().getOneForUser(username).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return stringResult.fail("Couldn't find a user matching that name!");
            } else {
                Member<?> member = optionalMember.get();
                member.setBanned(true);
                member.setBanReason(reason);
                return stringResult.success(username + " was banned for " + reason);
            }
        });
    }

}
