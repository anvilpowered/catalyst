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

package rocks.milspecsg.msessentials.api.member;

import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msrepository.api.manager.Manager;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberManager<TString> extends Manager<MemberRepository<?, ?>> {

    @Override
    default String getDefaultIdentifierSingularUpper() {
        return "Member";
    }

    @Override
    default String getDefaultIdentifierPluralUpper() {
        return "Members";
    }

    @Override
    default String getDefaultIdentifierSingularLower() {
        return "member";
    }

    @Override
    default String getDefaultIdentifierPluralLower() {
        return "members";
    }

    CompletableFuture<TString> info(String nickname, boolean isActive);

    CompletableFuture<TString> formatMessage(String prefix, String nameColor, String username, String message, String suffix, boolean hasPermission);

    CompletableFuture<TString> setNickName(String userName, String nickName);

    CompletableFuture<TString> deleteNickname(String username);

    CompletableFuture<TString> setLastSeenUtc(UUID userUUID, Date lastSeenUtc);

    CompletableFuture<Void> syncPlayerInfo(UUID playerUUID, String ipAddress, String username);

    CompletableFuture<TString> ban(String username, String reason);

    CompletableFuture<TString> ban(String userName);

    CompletableFuture<TString> unBan(String userName);

    CompletableFuture<TString> mute(String userName);

    CompletableFuture<TString> unMute(String userName);
}
