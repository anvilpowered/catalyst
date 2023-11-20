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

import net.kyori.adventure.text.Component

typealias Placeholder = String

interface MessageFormat {

    val format: Component

    interface Builder<P, M : MessageFormat> {
        fun build(block: P.() -> Component): M
    }

    interface Placeholders<M : MessageFormat>
}

// don't ask, it works
internal data class NestedFormat<
    ChildFormat : MessageFormat,
    ParentPlaceholders : MessageFormat.Placeholders<*>,
    ChildPlaceholders : MessageFormat.Placeholders<ChildFormat>,
    Builder : MessageFormat.Builder<ChildPlaceholders, ChildFormat>,
    >(
    val format: Builder,
    val placeholderResolver: (ParentPlaceholders) -> ChildPlaceholders,
)
