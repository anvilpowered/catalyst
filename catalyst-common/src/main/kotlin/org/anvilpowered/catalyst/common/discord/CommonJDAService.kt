/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package org.anvilpowered.catalyst.common.discord

import com.google.inject.Inject
import com.google.inject.Singleton
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.discord.JDAService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.registry.Registry
import org.slf4j.Logger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.security.auth.login.LoginException

@Singleton
class CommonJDAService<TPlayer> @Inject constructor(
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val logger: Logger,
    private val discordListener: CommonDiscordListener<TPlayer>,
    private val channelService: ChannelService<TPlayer>
) : JDAService {

    private var isLoaded = false
    private var jda: JDA? = null

    override fun enableDiscordBot() {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            logger.warn("The discord bot is disabled! Chat will not be transmitted from in-game to discord.")
            return
        }
        if (isLoaded) {
            jda?.shutdownNow()
        }
        try {
            val builder = JDABuilder.createDefault(registry.getOrDefault(CatalystKeys.BOT_TOKEN))
            builder.setCompression(Compression.NONE)
            builder.setBulkDeleteSplittingEnabled(false)
            builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
            jda = builder.build()
        } catch (e: LoginException) {
            e.printStackTrace()
        }
        var playerCount = registry.getOrDefault(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS)
        val nowPlaying = registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE)
        if (userService.onlinePlayers.isNotEmpty()) {
            playerCount = userService.onlinePlayers.size.toString()
        }
        jda!!.presence.activity = Activity.playing(nowPlaying.replace("%players%".toRegex(), playerCount))
        jda!!.addEventListener(discordListener)
        isLoaded = true
        if (registry.getOrDefault(CatalystKeys.TOPIC_UPDATE_ENABLED)) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                updateTopic(),
                1,
                registry.getOrDefault(CatalystKeys.TOPIC_UPDATE_DELAY).toLong(), TimeUnit.MINUTES
            )
        }
        try {
            jda?.awaitReady()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun updateTopic(): Runnable {
        val channelId = registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL)
        if (channelId.isEmpty()) {
            throw IllegalStateException("Default chat channel must not be empty!")
        }
        return Runnable {
            val channel = jda!!.getTextChannelById(channelService.getChannelFromId(channelId)?.discordChannel ?: "")
            var playerCount = registry.getOrDefault(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS)
            val nowPlaying = registry.getOrDefault(CatalystKeys.NOW_PLAYING_MESSAGE)
            if (userService.onlinePlayers.isNotEmpty()) {
                playerCount = userService.onlinePlayers.size.toString()
            }
            if (nowPlaying.contains("%players%")) {
                jda!!.presence.activity = Activity.playing(nowPlaying.replace("%players%".toRegex(), playerCount))
            }
            if (channel == null) {
                logger.error("Could not update the main channel topic!")
            } else {
                channel.manager.setTopic(
                    registry.getOrDefault(CatalystKeys.TOPIC_FORMAT)
                        .replace("%players%", playerCount)
                ).queue()
            }
        }
    }

    override fun getJDA(): JDA {
        return jda!!
    }

    init {
        registry.whenLoaded { enableDiscordBot() }.register()
    }
}
