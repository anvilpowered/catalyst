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

package org.anvilpowered.catalyst.velocity.discord;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mashape.unirest.http.Unirest;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.velocity.plugin.CatalystVelocity;
import org.json.JSONObject;

@Singleton
public class WebhookSender {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Inject
    private JDAHook jdaHook;

    public void sendWebhookMessage(String webHook, String player, String message, String channelID, Player source) {
        String content = message.replaceAll("&(0-9a-fA-FlkKrR)", "");
        String format = registry.getOrDefault(CatalystKeys.WEBHOOK_URL)
            .replace("%uuid%", source.getUniqueId().toString());
        ScheduledTask task = proxyServer.getScheduler().buildTask(CatalystVelocity.plugin, () -> {
            net.dv8tion.jda.api.entities.Webhook webhook = getWebhook(channelID);
            if (webHook == null) return;
            sendWebhook(webhook, Webhook.of(format, removeCodes(player), removeCodes(content)));
        }).schedule();
    }

    public static void sendWebhook(
        net.dv8tion.jda.api.entities.Webhook webhook, Webhook webhookUtils) {

        JSONObject json = new JSONObject();
        json.put("content", webhookUtils.message);
        json.put("username", webhookUtils.name.replaceAll("&([0-9a-fA-FlLkKrR])", ""));
        json.put("avatar_url", webhookUtils.avatarURL);
        try {
            Unirest.post(webhook.getUrl()).header("Content-Type",
                "application/json").body(json)
                .asJsonAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private net.dv8tion.jda.api.entities.Webhook getWebhook(String channelId) {
        TextChannel textChannel = jdaHook.getJDA().getTextChannelById(channelId);
        if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
            System.out.println("Please allow the discord bridge to handle webhooks!");
        }

        net.dv8tion.jda.api.entities.Webhook w = textChannel.getGuild()
            .retrieveWebhooks().complete().stream().filter(wh ->
                wh.getName().equals("CatalystVelocity-DB: " + textChannel.getName()))
            .findFirst().orElse(null);

        if (w == null) {
            w = textChannel.createWebhook("CatalystVelocity-DB: " + textChannel.getName()).complete();
        }

        return w;
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
