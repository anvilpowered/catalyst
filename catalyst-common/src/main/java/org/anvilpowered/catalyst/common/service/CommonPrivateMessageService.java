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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Singleton
public class CommonPrivateMessageService<TUser, TPlayer, TString, TCommandSource> implements PrivateMessageService<TString> {

    private Set<UUID> socialSpySet = new HashSet<>();
    private Map<UUID, UUID> replyMap = new HashMap<>();
    private String source;
    private String recipient;
    private String rawMessage;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Override
    public Set<UUID> socialSpySet() {
        return socialSpySet;
    }

    @Override
    public Map<UUID, UUID> replyMap() {
        return replyMap;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String sourceUserName) {
        this.source = sourceUserName;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    @Override
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String getRawMessage() {
        return rawMessage;
    }

    @Override
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public TString formatMessage(String sender, String recipient, String rawMessage) {
        return textService.deserialize(
            registry.getOrDefault(CatalystKeys.PRIVATE_MESSAGE_FORMAT)
                .replace("%sender%", sender)
                .replace("%recipient%", recipient)
                .replace("%message%", rawMessage.trim())
        );
    }

    @Override
    public CompletableFuture<Void> sendMessage(String sender, String recipient, String rawMessage) {
        return CompletableFuture.runAsync(() -> userService.get(sender).ifPresent(src -> {
            textService.send(formatMessage("Me", recipient, rawMessage), (TCommandSource) src);
            userService.get(recipient).ifPresent(rec ->
                textService.send(formatMessage(sender, "Me", rawMessage), (TCommandSource) rec));
            socialSpy(sender, recipient, rawMessage);
        }));
    }

    @Override
    public CompletableFuture<Void> sendMessageFromConsole(String recipient, String rawMessage, Class<?> console) {
        return CompletableFuture.runAsync(() -> {
            textService.send(formatMessage("Me", recipient, rawMessage), (TCommandSource) console);
            userService.get(recipient).ifPresent(r -> {
                textService.send(formatMessage("Console", "Me", rawMessage), (TCommandSource) r);
            });
        });
    }

    @Override
    public CompletableFuture<Void> socialSpy(String sender, String recipient, String rawMessage) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> {
                if (!(socialSpySet.isEmpty()) && socialSpySet.contains(userService.getUUID((TUser) p))) {
                    if (userService.getUserName((TUser) p).equalsIgnoreCase(sender)
                        || userService.getUserName((TUser) p).equalsIgnoreCase(recipient)) {
                        return;
                    }
                    textService.send(formatSocialSpyMessage(sender, recipient, rawMessage), (TCommandSource) p);
                }
            }
        ));
    }

    public TString formatSocialSpyMessage(String sender, String recipient, String rawMessage) {
        return textService.
            builder()
            .gray().append("[SocialSpy] ")
            .dark_gray().append("[")
            .blue().append(sender)
            .gold().append(" -> ")
            .blue().append(recipient)
            .dark_gray().append("] ")
            .gray().append(rawMessage.trim())
            .build();
    }
}
