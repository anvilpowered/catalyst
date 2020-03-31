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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;
import org.anvilpowered.catalyst.api.service.LoggerService;

public class CommonDiscordListener<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject>
    extends ListenerAdapter {

    @Inject
    private Registry registry;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private ExecuteCommandService<TCommandSource> executeCommandService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private LoggerService<TString> loggerService;

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())
            || event.isWebhookMessage()
            || event.getMember().isFake()) {
            return;
        }

        if (event.getChannel().getId().equals(registry.getOrDefault(CatalystKeys.MAIN_CHANNEL))) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)
                && event.getMember().toString().contains("!cmd")) {
                String command = event.getMessage().getContentRaw().replace("!cmd", "");
                executeCommandService.executeDiscordCommand(command);
                return;
            } else {
                String message = registry.getOrDefault(CatalystKeys.DISCORD_CHAT_FORMAT)
                    .replace("%name%", event.getMember().getEffectiveName())
                    .replace("%message%", event.getMessage().getContentDisplay());
                userService.getOnlinePlayers().forEach(p ->
                    textService.builder()
                        .append(textService.deserialize(message))
                        .onClickOpenUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL))
                        .onHoverShowText(textService.of(registry.getOrDefault(CatalystKeys.DISCORD_HOVER_MESSAGE)))
                        .sendTo(p)
                );
            }
            loggerService.info("[Discord] " + event.getMember().getEffectiveName() + " : " + event.getMessage().getContentDisplay());
        }

        if (event.getChannel().getId().equals(registry.getOrDefault(CatalystKeys.STAFF_CHANNEL))) {
            String message = registry.getOrDefault(CatalystKeys.DISCORD_STAFF_FORMAT)
                .replace("%name%", event.getAuthor().getName())
                .replace("%message%", event.getMessage().getContentDisplay());
            userService.getOnlinePlayers().forEach(p -> {
                if (permissionService.hasPermission((TSubject) p, registry.getOrDefault(CatalystKeys.STAFFCHAT))) {
                    textService.builder()
                        .append(textService.deserialize(message))
                        .onClickOpenUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL))
                        .onHoverShowText(textService.of(registry.getOrDefault(CatalystKeys.DISCORD_HOVER_MESSAGE)))
                        .sendTo(p);
                }
            });
            loggerService.info("[Discord][STAFF] " + event.getMember().getEffectiveName() + " : " + event.getMessage().getContentDisplay());
        }

    }
}
