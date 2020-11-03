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
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.JDAService;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class CommonJDAService<
    TUser,
    TPlayer,
    TString,
    TCommandSource> implements JDAService {

    private Registry registry;
    private boolean isLoaded = false;
    private JDA jda;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private Logger logger;

    @Inject
    private CommonDiscordListener<TUser, TString, TPlayer, TCommandSource> discordListener;

    @Inject
    public CommonJDAService(Registry registry) {
        this.registry = registry;
        registry.whenLoaded(this::enableDiscordBot).register();
    }

    @Override
    public void enableDiscordBot() {
        if (registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            if (isLoaded) {
                jda.shutdownNow();
            }
            try {
                JDABuilder builder = JDABuilder.createDefault(registry.getOrDefault(CatalystKeys.BOT_TOKEN));
                builder.setCompression(Compression.NONE);
                builder.setBulkDeleteSplittingEnabled(false);
                builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);

                jda = builder.build();
                String playerCount = registry.getOrDefault(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS);
                String nowPlaying = registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE);
                if (userService.getOnlinePlayers().size() != 0) {
                    playerCount = Integer.toString(userService.getOnlinePlayers().size());
                }
                jda.getPresence().setActivity(
                    Activity.playing(nowPlaying.replaceAll("%players%", playerCount))
                );
                jda.addEventListener(discordListener);
                isLoaded = true;
                if (registry.getOrDefault(CatalystKeys.TOPIC_UPDATE_ENABLED)) {
                    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                    executor.scheduleAtFixedRate(this.updateTopic(), 1,
                        registry.getOrDefault(CatalystKeys.TOPIC_UPDATE_DELAY), TimeUnit.MINUTES);
                }
                jda.awaitReady();
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("The discord bot is disabled! Chat will not be transmitted from in-game to discord.");
        }
    }

    @Override
    public Runnable updateTopic() {
        return () -> {
            TextChannel channel = jda.getTextChannelById(registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL));
            String playerCount = registry.getOrDefault(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS);
            String nowPlaying = registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE);
            if (userService.getOnlinePlayers().size() != 0) {
                playerCount = Integer.toString(userService.getOnlinePlayers().size());
            }
            if (nowPlaying.contains("%players%")) {
                jda.getPresence().setActivity(
                    Activity.playing(nowPlaying.replaceAll("%players%", playerCount))
                );
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
}
