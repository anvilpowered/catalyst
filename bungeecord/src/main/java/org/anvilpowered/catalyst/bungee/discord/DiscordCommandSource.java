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

package org.anvilpowered.catalyst.bungee.discord;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.JDAService;

import java.util.Collection;
import java.util.Objects;

public class DiscordCommandSource implements CommandSender {

    @Inject
    private Registry registry;

    @Inject
    private JDAService jdaService;

    @Inject
    private TextService<TextComponent, CommandSender> textService;

    @Override
    public String getName() {
        return "discord";
    }

    @Override
    public void sendMessage(String message) {
        Objects.requireNonNull(
            jdaService.getJDA()
                .getTextChannelById(registry.getOrDefault(CatalystKeys.MAIN_CHANNEL))
                .sendMessage(message)
        ).queue();
    }

    @Override
    public void sendMessages(String... messages) {
        for (String m : messages) {
            sendMessage(m);
        }
    }

    @Override
    public void sendMessage(BaseComponent message) {
        sendMessage(textService.serializePlain(new TextComponent(message)));
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        for (BaseComponent m : message) {
            sendMessage(m);
        }
    }

    @Override
    public Collection<String> getGroups() {
        return null;
    }

    @Override
    public void addGroups(String... groups) {
    }

    @Override
    public void removeGroups(String... groups) {
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void setPermission(String permission, boolean value) {

    }

    @Override
    public Collection<String> getPermissions() {
        return null;
    }
}
