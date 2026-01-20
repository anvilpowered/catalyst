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

package org.anvilpowered.catalyst.api.user

import org.anvilpowered.anvil.core.db.Creates
import org.anvilpowered.anvil.core.db.DomainEntity
import org.anvilpowered.anvil.core.user.Player

import java.util.UUID

/** A user of a game of the Anvil platform.
  *
  * Represents a single user of a game.
  */
case class MinecraftUser(
    override val id: UUID,
    username: String,
    ipAddress: String,
    nickname: Option[String],
) extends DomainEntity

object MinecraftUser {
  case class CreateDto(
      val uuid: UUID,
      val username: String,
      val ipAddress: String,
  ) extends Creates[MinecraftUser]

  case class Online(
      val user: MinecraftUser,
      val player: Player,
  )
}
