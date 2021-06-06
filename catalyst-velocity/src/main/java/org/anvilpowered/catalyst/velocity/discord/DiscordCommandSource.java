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
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.discord.DiscordCommandService;
import org.anvilpowered.catalyst.api.discord.JDAService;

import java.util.Objects;

public class DiscordCommandSource implements CommandSource {

    @Inject
    private JDAService jdaHook;

    @Inject
    private TextService<TextComponent, CommandSource> textService;

    @Inject
    private DiscordCommandService discordCommandService;

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void sendMessage(Identity identity, Component message, MessageType type) {
        Objects.requireNonNull(
            jdaHook.getJDA()
                .getTextChannelById(discordCommandService.getChannelId()))
            .sendMessage("```" + textService.serializePlain((TextComponent) message) + "```")
            .queue();
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return Tristate.TRUE;
    }
}
