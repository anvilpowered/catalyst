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

package org.anvilpowered.catalyst.bungee.module;

import com.google.inject.TypeLiteral;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.bungee.service.BungeeBroadcastService;
import org.anvilpowered.catalyst.bungee.service.BungeeEventService;
import org.anvilpowered.catalyst.bungee.service.BungeeExecuteCommandService;
import org.anvilpowered.catalyst.bungee.service.BungeeLoggerService;
import org.anvilpowered.catalyst.common.module.CommonModule;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;
import org.spongepowered.api.command.spec.CommandExecutor;

import java.io.File;
import java.nio.file.Paths;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public class BungeeModule extends CommonModule<
    ProxiedPlayer,
    ProxiedPlayer,
    TextComponent,
    CommandSender,
    CommandSender,
    Event> {

    @Override
    protected void configure() {
        super.configure();
        File configFilesLocation = Paths.get("plugins/" + CatalystPluginInfo.id).toFile();
        if (!configFilesLocation.exists()) {
            if (!configFilesLocation.mkdirs()) {
                throw new IllegalStateException("Unable to create config directory");
            }
        }

        bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {
        }).toInstance(HoconConfigurationLoader.builder()
            .setPath(Paths.get(configFilesLocation + "/catalyst.conf"))
            .build());

        bind(new TypeLiteral<BroadcastService<TextComponent>>() {
        }).to(BungeeBroadcastService.class);
        bind(new TypeLiteral<LoggerService<TextComponent>>() {
        }).to(BungeeLoggerService.class);
        bind(EventService.class).to(BungeeEventService.class);
        bind(new TypeLiteral<ExecuteCommandService<CommandSender>>() {
        }).to(BungeeExecuteCommandService.class);
    }
}
