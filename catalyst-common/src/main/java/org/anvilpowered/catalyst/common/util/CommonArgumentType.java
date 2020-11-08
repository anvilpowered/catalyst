/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;

public class CommonArgumentType implements ArgumentType<String> {

    @Override
    public String parse(StringReader reader) {
        int beginning = reader.getCursor(); // Starting position of the cursor is at the beginning
        if (!reader.canRead()) {
            reader.skip();
        }

        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }

        return reader.getString().substring(beginning, reader.getCursor());
    }
}
