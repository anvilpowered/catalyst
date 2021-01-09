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
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Webhook;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.discord.WebhookSender;
import org.anvilpowered.catalyst.api.discord.JDAService;
import org.json.JSONObject;

public class CommonWebhookSender<TUser, TPlayer> implements WebhookSender {

    @Inject
    private Registry registry;

    @Inject
    private JDAService jdaService;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Override
    public void sendWebhookMessage(String webHook, String player, String message,
                                   String channelId, Object source) {
        String content = message.replaceAll("&(0-9a-fA-FlkKrR)", "");
        String format = registry.getOrDefault(CatalystKeys.WEBHOOK_URL)
            .replace("%uuid%", userService.getUUID((TUser) source).toString());
        Webhook webhook = getWebhook(channelId);
        if (webHook == null) return;
        sendWebhook(webhook, org.anvilpowered.catalyst.api.discord.Webhook.of(format, removeCodes(player), removeCodes(content)));
    }

    @Override
    public void sendConsoleWebhookMessage(String webHook, String message, String channelId) {
        String content = message.replaceAll("&(0-9a-fA-FlkKrR)", "");
        Webhook webhook = getWebhook(channelId);
        if (webHook == null) return;
        sendWebhook(webhook, org.anvilpowered.catalyst.api.discord.Webhook.of("", "Console",
            content));
    }

    @Override
    public void sendWebhook(Webhook webhook, org.anvilpowered.catalyst.api.discord.Webhook webhookUtils) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", webhookUtils.message);
        jsonObject.put("username", webhookUtils.name.replaceAll("&([0-9a-fA-FlLkKrR])", ""));
        jsonObject.put("avatar_url", webhookUtils.avatarURL);
        try {
            Unirest.post(webhook.getUrl()).header("Content-Type",
                "application/json").body(jsonObject)
                .asJsonAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Webhook getWebhook(String channelID) {
        TextChannel channel = jdaService.getJDA().getTextChannelById(channelID);
        if (!channel.getGuild().getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
            throw new AssertionError("Please allow the discord bot to handle webhooks!");
        }

        Webhook webhook = channel.getGuild()
            .retrieveWebhooks().complete().stream().filter(wh ->
                wh.getName().equalsIgnoreCase("Catalyst-DB: " + channel.getName()))
            .findFirst().orElse(null);

        if (webhook == null) {
            webhook = channel.createWebhook("Catalyst-DB: " + channel.getName()).complete();
        }

        return webhook;
    }

    private String removeCodes(String message) {
        return message
            .replace("&0", "").replace("&1", "").replace("&2", "")
            .replace("&3", "").replace("&4", "").replace("&5", "")
            .replace("&6", "").replace("&7", "").replace("&8", "")
            .replace("&a", "").replace("&b", "").replace("&c", "")
            .replace("&d", "").replace("&e", "").replace("&f", "")
            .replace("&k", "").replace("&l", "").replace("&m", "")
            .replace("&n", "").replace("&o", "").replace("&r", "")
            .replace("&k", "").replace("&9", "").replaceAll("\\*", "");
    }
}
