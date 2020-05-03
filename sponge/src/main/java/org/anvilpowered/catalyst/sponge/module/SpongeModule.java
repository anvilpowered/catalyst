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
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.common.data.config.BackendConfigurationService;
import org.anvilpowered.catalyst.common.data.config.ProxyConfigurationService;
import org.anvilpowered.catalyst.common.module.CommonModule;
import org.anvilpowered.catalyst.sponge.data.config.SpongeConfigurationService;
import org.anvilpowered.catalyst.sponge.service.SpongeBroadcastService;
import org.anvilpowered.catalyst.sponge.service.SpongeEventService;
import org.anvilpowered.catalyst.sponge.service.SpongeExecuteCommandService;
import org.anvilpowered.catalyst.sponge.service.SpongeLoggerService;
import org.anvilpowered.catalyst.sponge.service.SpongeTeleportationService;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

public class SpongeModule extends CommonModule<User, Player, Text, CommandSource, Subject, Event> {

    @Override
    protected void configure() {
        super.configure();

        bind(BackendConfigurationService.class).to(SpongeConfigurationService.class);
        bind(new TypeLiteral<BroadcastService<Text>>() {
        }).to(SpongeBroadcastService.class);
        bind(new TypeLiteral<EventService<Event>>() {
        }).to(SpongeEventService.class);
        bind(new TypeLiteral<ExecuteCommandService<CommandSource>>() {
        }).to(SpongeExecuteCommandService.class);
        bind(new TypeLiteral<LoggerService<Text>>() {
        }).to(SpongeLoggerService.class);
    }
}
