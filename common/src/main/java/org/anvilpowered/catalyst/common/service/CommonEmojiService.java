/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.EmojiService;

import java.util.Map;

public class CommonEmojiService implements EmojiService {

    @Inject
    private Registry registry;

    @Override
    public Map<String, Character> getEmojis() {
        return registry.getOrDefault(CatalystKeys.EMOJI_MAP);
    }

    @Override
    public String toEmoji(String message, String chatColor) {
        for (String key : getEmojis().keySet()) {
            message = message.replace(key, "&f" + getEmojis().get(key) + chatColor);
        }
        return message;
    }
}
