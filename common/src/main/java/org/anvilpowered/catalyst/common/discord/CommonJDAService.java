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
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.JDAService;
import org.anvilpowered.catalyst.api.service.LoggerService;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

@Singleton
public class CommonJDAService<TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject> implements JDAService {

    @Inject
    private Registry registry;

    private JDA jda;

    private boolean isLoaded = false;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private LoggerService<TString> loggerService;

    @Inject
    private EventService eventService;

    @Inject
    private CommonDiscordListener<TString, TPlayer, TCommandSource, TSubject> discordListener;

    @Override
    public void enableDiscordBot() {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            try {
                jda = new JDABuilder(registry.getOrDefault(CatalystKeys.BOT_TOKEN)).build().awaitReady();
                jda.getPresence()
                    .setActivity(
                        Activity.playing(registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE))
                    );
                jda.addEventListener(discordListener);
                isLoaded = true;
                eventService.schedule(this.updateTopic(), TimeUnit.SECONDS, 15);
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            loggerService.warn("The discord bot is disabled! Chat will not be transmitted from in-game to discord.");
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
