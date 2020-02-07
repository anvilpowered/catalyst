/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.anvil.api.data.registry.Registry;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.events.ProxyMessageEvent;

import java.util.UUID;

public class SocialSpyCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private Registry registry;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            if (source.hasPermission(registry.getOrDefault(MSEssentialsKeys.SOCIALSPY)) || source.hasPermission(registry.getOrDefault(MSEssentialsKeys.SOCIALSPY_ONJOIN))) {
                UUID playerUUID = player.getUniqueId();
                if (ProxyMessageEvent.socialSpySet.contains(playerUUID)) {
                    ProxyMessageEvent.socialSpySet.remove(playerUUID);
                    source.sendMessage(pluginMessages.getSocialSpy(false));
                } else {
                    ProxyMessageEvent.socialSpySet.add(playerUUID);
                    source.sendMessage(pluginMessages.getSocialSpy(true));
                }
            }
        }
    }
}
