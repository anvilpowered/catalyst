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

package org.anvilpowered.catalyst.velocity.discord;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import net.kyori.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.JDAService;

import java.util.Objects;

public class DiscordCommandSource implements CommandSource {

    @Inject
    private Registry registry;

    @Inject
    private JDAService jdaHook;

    @Inject
    private TextService<TextComponent, CommandSource> textService;

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void sendMessage(Component component) {
        Objects.requireNonNull(
            jdaHook.getJDA()
                .getTextChannelById(registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL)))
            .sendMessage("```" + textService.serializePlain((TextComponent) component) + "```")
            .queue();
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return Tristate.TRUE;
    }
}
