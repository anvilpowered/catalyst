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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import java.util.Optional

@Singleton
class CommonLuckpermsService<TPlayer> @Inject constructor(
  private val userService: UserService<TPlayer, TPlayer>
) : LuckpermsService {
  
  private val userManager: UserManager = LuckPermsProvider.get().userManager
  private val contextManager: ContextManager = LuckPermsProvider.get().contextManager

  override fun getCachedPlayerData(player: Any): Optional<CachedMetaData> {
    val lpUser = userManager.getUser(userService.getUUID(player as TPlayer)) ?: return Optional.empty()
    return Optional.of(lpUser.cachedData.getMetaData(getQueryOptions(lpUser)))
  }

  override fun getQueryOptions(user: User): QueryOptions {
    return contextManager.getQueryOptions(user).orElseGet { contextManager.staticQueryOptions }
  }

  override fun getPrefix(player: Any): String {
    val playerData = getCachedPlayerData(player)
    if (playerData.isPresent) {
      if (playerData.get().prefix != null) {
        return playerData.get().prefix ?: ""
      }
    }
    return ""
  }

  override fun getSuffix(player: Any): String {
    val playerData = getCachedPlayerData(player)
    if (playerData.isPresent) {
      if (playerData.get().suffix != null) {
        return playerData.get().suffix ?: ""
      }
    }
    return ""
  }

  override fun getChatColor(player: Any): String {
    val playerData = getCachedPlayerData(player)
    if (playerData.isPresent) {
      if (playerData.get().getMetaValue("chat-color") != null) {
        return playerData.get().getMetaValue("chat-color") ?: ""
      }
    }
    return ""
  }

  override fun getNameColor(player: Any): String {
    val playerData = getCachedPlayerData(player)
    if (playerData.isPresent) {
      if (playerData.get().getMetaValue("name-color") != null) {
        return playerData.get().getMetaValue("name-color") ?: ""
      }
    }
    return ""
  }

  override fun getGroupName(player: Any): String {
    val user = userManager.getUser(userService.getUUID(player as TPlayer)) ?: return ""
    return user.primaryGroup
  }
}

