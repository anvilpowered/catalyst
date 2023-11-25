/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2024 Contributors
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

package org.anvilpowered.catalyst.api.config

import io.leangen.geantyref.TypeToken
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PlayerFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PrivateMessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.ProxyServerFormat
import org.anvilpowered.anvil.core.config.TypeTokens as AnvilTypeTokens

@Suppress("PropertyName")
open class TypeTokens private constructor() : AnvilTypeTokens() {

    val PLAYER_FORMAT: TypeToken<PlayerFormat> = TypeToken.get(PlayerFormat::class.java)
    val ONLINE_USER_FORMAT: TypeToken<OnlineUserFormat> = TypeToken.get(OnlineUserFormat::class.java)
    val PRIVATE_MESSAGE_FORMAT: TypeToken<PrivateMessageFormat> = TypeToken.get(PrivateMessageFormat::class.java)
    val PROXY_SERVER_FORMAT: TypeToken<ProxyServerFormat> = TypeToken.get(ProxyServerFormat::class.java)
    val CHAT_CHANNEL: TypeToken<ChatChannel> = TypeToken.get(ChatChannel::class.java)

    companion object : TypeTokens()
}
