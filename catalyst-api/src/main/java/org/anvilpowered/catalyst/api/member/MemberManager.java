/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.api.member;


import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import org.anvilpowered.anvil.api.coremember.CoreMemberRepository;
import org.anvilpowered.anvil.api.datastore.Manager;

public interface MemberManager extends Manager<CoreMemberRepository<?, ?>> {

  CompletableFuture<Component> info(String userName, boolean isActive, boolean[] permissions);

  CompletableFuture<Component> setNickName(String userName, String nickName);

  CompletableFuture<Component> setNickNameForUser(String userName, String nickName);

  CompletableFuture<Component> deleteNickName(String userName);

  CompletableFuture<Component> deleteNickNameForUser(String userName);

  CompletableFuture<Component> ban(String userName, String reason);

  CompletableFuture<Component> ban(String userName);

  CompletableFuture<Component> tempBan(String userName, String duration, String reason);

  CompletableFuture<Component> tempBan(String userName, String duration);

  CompletableFuture<Component> unBan(String userName);

  CompletableFuture<Component> mute(String userName, String reason);

  CompletableFuture<Component> mute(String userName);

  CompletableFuture<Component> tempMute(String userName, String duration, String reason);

  CompletableFuture<Component> tempMute(String userName, String duration);

  CompletableFuture<Component> unMute(String userName);
}
