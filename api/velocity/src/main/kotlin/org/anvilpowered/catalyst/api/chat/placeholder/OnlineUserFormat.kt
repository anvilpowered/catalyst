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

package org.anvilpowered.catalyst.api.chat.placeholder

import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.user.MinecraftUser
import org.apache.logging.log4j.Logger

@Serializable(with = OnlineUserFormat.Serializer::class)
class OnlineUserFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    suspend fun resolve(
        proxyServer: ProxyServer,
        logger: Logger,
        luckpermsService: LuckpermsService,
        user: MinecraftUser.Online,
    ): Component = resolve(proxyServer, logger, luckpermsService, format, placeholders, user)

    companion object : MessageFormat.Builder<Placeholders, OnlineUserFormat> {

        suspend fun resolve(
            proxyServer: ProxyServer,
            logger: Logger,
            luckpermsService: LuckpermsService,
            format: Component,
            placeholders: Placeholders,
            user: MinecraftUser.Online,
        ): Component = PlayerFormat.resolve(
            proxyServer,
            logger,
            luckpermsService,
            format,
            PlayerFormat.ConcretePlaceholders(placeholders.path),
            user.player,
        ).replaceText { it.match(placeholders.displayname).replacement(user.user.nickname ?: user.player.username) }

        override fun build(block: Placeholders.() -> Component): OnlineUserFormat {
            val placeholders = Placeholders()
            return OnlineUserFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<OnlineUserFormat>(::OnlineUserFormat)

    open class Placeholders internal constructor(internal val path: List<String> = listOf()) : MessageFormat.Placeholders<OnlineUserFormat>,
        PlayerFormat.Placeholders by PlayerFormat.ConcretePlaceholders(path) {

        private val pathPrefix = path.joinToString { "$it." }

        val displayname: Placeholder = "%${pathPrefix}displayname%"
    }
}
