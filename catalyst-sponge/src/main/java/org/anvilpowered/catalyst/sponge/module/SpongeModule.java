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

package org.anvilpowered.catalyst.sponge.module;

import com.google.inject.TypeLiteral;
import org.anvilpowered.anvil.api.command.CommandNode;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.DiscordCommandService;
import org.anvilpowered.catalyst.common.data.config.BackendConfigurationService;
import org.anvilpowered.catalyst.common.module.CommonModule;
import org.anvilpowered.catalyst.sponge.command.SpongeCommandNode;
import org.anvilpowered.catalyst.sponge.data.config.SpongeConfigurationService;
import org.anvilpowered.catalyst.sponge.service.SpongeBroadcastService;
import org.anvilpowered.catalyst.sponge.service.SpongeDiscordCommandService;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class SpongeModule extends CommonModule<User, Player, Text, CommandSource> {

    @Override
    protected void configure() {
        super.configure();

        bind(BackendConfigurationService.class).to(SpongeConfigurationService.class);
        bind(new TypeLiteral<BroadcastService<Text>>() {
        }).to(SpongeBroadcastService.class);
        bind(new TypeLiteral<CommandNode<CommandSource>>() {
        }).to(SpongeCommandNode.class);
        bind(DiscordCommandService.class).to(SpongeDiscordCommandService.class);
    }
}
