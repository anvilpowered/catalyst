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

import rocks.milspecsg.mscore.api.coremember.repository.CoreMemberRepository;
import rocks.milspecsg.msrepository.api.manager.Manager;

import java.util.concurrent.CompletableFuture;

public interface MemberManager<TString> extends Manager<CoreMemberRepository<?, ?>> {

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

    CompletableFuture<TString> info(String userName, boolean isActive);

    CompletableFuture<TString> formatMessage(String prefix, String nameColor, String userName, String message, String suffix, boolean hasPermission);

    CompletableFuture<TString> setNickName(String userName, String nickName);
    CompletableFuture<TString> setNickNameForUser(String userName, String nickName);

    CompletableFuture<TString> deleteNickName(String userName);
    CompletableFuture<TString> deleteNickNameForUser(String userName);

    CompletableFuture<TString> ban(String userName, String reason);
    CompletableFuture<TString> ban(String userName);

    CompletableFuture<TString> tempBan(String userName, String duration, String reason);
    CompletableFuture<TString> tempBan(String userName, String duration);

    CompletableFuture<TString> unBan(String userName);

    CompletableFuture<TString> mute(String userName, String reason);
    CompletableFuture<TString> mute(String userName);

    CompletableFuture<TString> tempMute(String userName, String duration, String reason);
    CompletableFuture<TString> tempMute(String userName, String duration);

    CompletableFuture<TString> unMute(String userName);
}
