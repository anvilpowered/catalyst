/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.anvil.command

import net.kyori.adventure.audience.Audience
import org.anvilpowered.catalyst.user.GameUser
import org.anvilpowered.anvil.user.Player
import org.anvilpowered.anvil.user.Subject
import org.anvilpowered.catalyst.domain.user.User
import org.anvilpowered.catalyst.domain.user.UserFacet

interface CommandSource : UserFacet {

    /**
     * The [User] associated with the executed command, if any.
     */
    override suspend fun getUserOrNull(): User?

    val audience: Audience

    val subject: Subject

    /**
     * The [Player] associated with the executed command, if any.
     */
    val player: Player?

    /**
     * The [GameUser] user associated with the executed command, if any.
     */
    val gameUser: GameUser?
}
