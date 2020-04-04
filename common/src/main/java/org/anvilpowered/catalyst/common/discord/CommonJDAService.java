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

package org.anvilpowered.catalyst.common.discord;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.JDAService;

import javax.security.auth.login.LoginException;

@Singleton
public class CommonJDAService<TPlayer> implements JDAService {

    private Registry registry;

    private JDA jda;

    private boolean isLoaded = false;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    public CommonJDAService(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::registryLoaded);
    }

    public void registryLoaded() {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            try {
                jda = new JDABuilder(registry.getOrDefault(CatalystKeys.BOT_TOKEN)).build().awaitReady();
                jda.getPresence()
                    .setActivity(
                        Activity.playing(registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE))
                    );
                isLoaded = true;
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setupListeners(Class<?>... listeners) {
        for (Class<?> listener : listeners) {
            jda.addEventListener(listener);
        }
    }

    @Override
    public Runnable updateTopic() {
        if (isLoaded) {
            return () -> {
                TextChannel channel = jda.getTextChannelById(registry.getOrDefault(CatalystKeys.MAIN_CHANNEL));
                String playerCount = "There are currently no players online!";
                if (userService.getOnlinePlayers().size() != 0) {
                    playerCount = Integer.toString(userService.getOnlinePlayers().size());
                }
                channel.getManager().setTopic(
                    registry.getOrDefault(CatalystKeys.TOPIC_FORMAT)
                        .replace("%players%", playerCount)
                ).queue();
            };
        } else {
            return () -> {
                throw new IllegalStateException("The discord bot has not yet loaded! Please wait till the discord bot is loaded");
            };
        }
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public boolean isEnabled() {
        return registry.getOrDefault(CatalystKeys.DISCORD_ENABLE);
    }
}
