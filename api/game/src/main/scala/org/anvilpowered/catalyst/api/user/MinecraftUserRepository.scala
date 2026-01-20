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

import org.anvilpowered.anvil.core.db.MutableRepository
import org.anvilpowered.anvil.core.db.SizedIterable
import java.util.UUID
import cats.data.OptionT
import cats.effect.Async
import org.anvilpowered.anvil.core.user.Player

trait MinecraftUserRepository extends MutableRepository[MinecraftUser, MinecraftUser.CreateDto] {

  def getNickname[F[_]: Async](id: UUID): OptionT[F, String]

  def updateNickname[F[_]: Async](id: UUID, nickname: String): F[Boolean]

  def deleteNickname[F[_]: Async](id: UUID): F[Boolean]

  def getAllUsernames[F[_]: Async](startWith: String = ""): fs2.Stream[F, String]

  def getByUsername[F[_]: Async](username: String): OptionT[F, MinecraftUser]
}

object MinecraftUserRepository {
  extension (repository: MinecraftUserRepository) {
    def getOnlineUser[F[_]](player: Player)(using F: Async[F]): F[MinecraftUser.Online] =
      repository
        .findById(player.id)
        .foldF(
          F.raiseError(IllegalStateException(s"User ${player.username} with id ${player.id} is not in the database!"))
        ) { user =>
          F.pure(MinecraftUser.Online(user, player))
        }
  }
}
