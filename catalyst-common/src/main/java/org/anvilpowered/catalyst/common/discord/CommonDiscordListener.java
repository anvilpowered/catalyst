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
import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.discord.DiscordCommandService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.registry.ChatChannel;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommonDiscordListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource>
    extends ListenerAdapter {

    @Inject
    private Registry registry;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private DiscordCommandService discordCommandService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Logger logger;

    @Inject
    private EmojiService emojiService;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot()) {
            return;
        }

        String message = EmojiParser.parseToAliases(event.getMessage().getContentDisplay());
        String messageRaw = event.getMessage().toString();

        if (event.getMember() != null
            && event.getMember().hasPermission(Permission.ADMINISTRATOR)
            && messageRaw.contains("!cmd")) {
            String command = event.getMessage().getContentRaw().replace("!cmd ", "");
            discordCommandService.setChannelId(event.getChannel().getId());
            discordCommandService.executeDiscordCommand(command);
            return;
        } else if (messageRaw.contains("!players")
            || messageRaw.contains("!online")
            || messageRaw.contains("!list")) {
            final Collection<TPlayer> onlinePlayers = userService.getOnlinePlayers();
            final String playerNames;
            if (onlinePlayers.size() == 0) {
                playerNames = "```There are currently no players online!```";
            } else {
                playerNames = "**Online Players:**```"
                    + userService.getOnlinePlayers().stream()
                    .map(p -> userService.getUserName((TUser) p))
                    .collect(Collectors.joining(", ")) + "```";
            }
            event.getChannel().sendMessage(playerNames).queue();
            return;
        }

        if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
            for (String key : registry.getOrDefault(CatalystKeys.EMOJI_MAP).keySet()) {
                message = message.replace(key, emojiService.getEmojis().get(key).toString());
            }
            sendMessage(event.getChannel().getId(), message, event.getMember().getEffectiveName());
        }
        logger.info("[Discord] " + event.getMember().getEffectiveName() + " : " + EmojiParser.parseToAliases(event.getMessage().getContentDisplay()));
}

    private void sendMessage(String channelId, String message, String userName) {
        List<ChatChannel> channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS);
        ChatChannel targetChannel = null;

        for (ChatChannel channel : channels) {
            if (channel.discordChannel == null) {
                continue;
            }
            if (channel.discordChannel.equals(channelId)) {
                targetChannel = channel;
            }
        }
        if (targetChannel == null && channelId.equals(registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL))) {
            Optional<ChatChannel> mainChannel =
                chatService.getChannelFromId(registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL));
            if (mainChannel.isPresent()) {
                targetChannel = mainChannel.get();
            } else {
                logger.error("Could not fall back to the main discord channel! Please check your configuration!");
                return;
            }
        }
        TString finalMessage = textService.builder()
            .append(textService.deserialize(registry.getOrDefault(CatalystKeys.DISCORD_CHAT_FORMAT)
                .replace("%name%", userName)
                .replace("%message%", message)))
            .onClickOpenUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL))
            .onHoverShowText(textService.of(registry.getOrDefault(CatalystKeys.DISCORD_HOVER_MESSAGE)))
            .build();

        for (TPlayer player : chatService.getUsersInChannel(targetChannel.id)) {
            textService.send(finalMessage, (TCommandSource) player);
        }
    }
}
