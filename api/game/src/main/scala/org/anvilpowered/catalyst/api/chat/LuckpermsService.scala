/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see [https://www.gnu.org/licenses/].
 */

package org.anvilpowered.catalyst.api.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.catalyst.api.chat.placeholder.MessageContentFormat
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import java.util.UUID
import scala.jdk.OptionConverters.*

class LuckpermsService {

  extension (string: String) {
    private def toMiniComponent: Component = MiniMessage.miniMessage().deserialize(string, Seq.empty*)
  }

  private val userManager: UserManager = LuckPermsProvider.get.getUserManager
  private val contextManager: ContextManager = LuckPermsProvider.get.getContextManager

  private def cachedPlayerData(userId: UUID): Option[CachedMetaData] =
    for {
      user <- Some(userManager.getUser(userId))
      cachedData <- Some(user.getCachedData) // TODO: Check if nullable
    } yield cachedData.getMetaData(queryOptions(user))

  private def queryOptions(user: User): QueryOptions =
    contextManager.getQueryOptions(user).toScala.getOrElse { contextManager.getStaticQueryOptions }

  def prefix(userId: UUID): Component =
    cachedPlayerData(userId).map(_.getPrefix.toMiniComponent).getOrElse { Component.empty }

  def suffix(userId: UUID): Component =
    cachedPlayerData(userId).map(_.getSuffix.toMiniComponent).getOrElse { Component.empty }

  def group(userId: UUID): Component =
    cachedPlayerData(userId).map(_.getPrimaryGroup.toMiniComponent).getOrElse { Component.empty }

  def nameFormat(userId: UUID, channelId: String): Option[OnlineUserFormat] =
    cachedPlayerData(userId)
      .map(_.getMetaValue("channel.$channelId.name-format"))
      .map { x => OnlineUserFormat(x.toMiniComponent) }

  def getMessageContentFormat(userId: UUID, channelId: String): Option[OnlineUserFormat] =
    cachedPlayerData(userId)
      .map(_.getMetaValue("channel.$channelId.message-content-format"))
      .map { x => OnlineUserFormat(x.toMiniComponent) }
}
