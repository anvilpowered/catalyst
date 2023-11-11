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

package org.anvilpowered.catalyst.core.db

import org.anvilpowered.anvil.db.user.GameUserRepositoryImpl
import org.anvilpowered.catalyst.core.db.user.DiscordUserRepositoryImpl
import org.anvilpowered.catalyst.core.db.user.UserRepositoryImpl
import org.anvilpowered.catalyst.core.user.DiscordUserRepository
import org.anvilpowered.catalyst.core.user.GameUserRepository
import org.anvilpowered.catalyst.core.user.UserRepository

interface RepositoryScope {
    val discordUserRepository: DiscordUserRepository
    val gameUserRepository: GameUserRepository
    val userRepository: UserRepository

    companion object {
        fun create(): RepositoryScope = RepositoryScopeImpl
    }
}

internal object RepositoryScopeImpl : RepositoryScope {
    override val discordUserRepository: DiscordUserRepository
        get() = DiscordUserRepositoryImpl
    override val gameUserRepository: GameUserRepository
        get() = GameUserRepositoryImpl
    override val userRepository: UserRepository
        get() = UserRepositoryImpl
}
