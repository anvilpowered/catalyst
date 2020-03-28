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
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Singleton
public class CommonPrivateMessageService<TPlayer extends TCommandSource, TString, TCommandSource> implements PrivateMessageService<TString> {

    private Set<UUID> socialSpySet = new HashSet<>();
    private Map<UUID, UUID> replyMap = new HashMap<>();
    private String sender;
    private String recipient;
    private String rawMessage;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Override
    public Set<UUID> socialSpySet() {
        return socialSpySet;
    }

    @Override
    public Map<UUID, UUID> replyMap() {
        return replyMap;
    }

    @Override
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    @Override
    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @Override
    public String getRawMessage() {
        return rawMessage;
    }

    @Override
    public TString formatMessage(String sender, String recipient, String rawMessage) {
        return textService.builder()
            .dark_gray().append("[")
            .blue().append(sender)
            .gold().append(" -> ")
            .blue().append(recipient)
            .dark_gray().append("] ")
            .gray().append(rawMessage)
            .build();
    }

    @Override
    public CompletableFuture<Void> sendMessage(String sender, String recipient, String rawMessage) {
        return CompletableFuture.runAsync(() -> userService.get(sender).ifPresent(src -> {
            textService.send(formatMessage("Me", recipient, rawMessage), src);
            userService.get(recipient).ifPresent(rec -> {
                textService.send(formatMessage(sender, "Me", rawMessage), rec);
            });
            socialSpy(sender, recipient, rawMessage);
        }));
    }

    @Override
    public CompletableFuture<Void> socialSpy(String sender, String recipient, String rawMessage) {
        return CompletableFuture.runAsync(() -> userService.getOnlinePlayers().forEach(p -> {
                if (!(socialSpySet.isEmpty()) && socialSpySet.contains(userService.getUUID(p))) {
                    if (userService.getUserName(p).equalsIgnoreCase(sender) || userService.getUserName(p).equalsIgnoreCase(recipient)) {
                        return;
                    }
                    textService.send(formatSocialSpyMessage(sender, recipient, rawMessage), p);
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
            .gray().append(rawMessage)
            .build();
    }
}
