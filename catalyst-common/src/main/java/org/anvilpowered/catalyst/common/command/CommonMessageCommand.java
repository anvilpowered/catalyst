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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;

import java.util.Optional;

public class CommonMessageCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private PrivateMessageService<TString> privateMessageService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    public int execute(CommandContext<TCommandSource> context, Class<?> consoleClass) {
        String name = context.getArgument("target", String.class);

        if (userService.getPlayer(name).isPresent()) {
            String message = context.getArgument("message", String.class);
            Optional<TPlayer> targetPlayer = userService.get(name);
            if (consoleClass.isAssignableFrom(context.getSource().getClass()) && targetPlayer.isPresent()) {
                privateMessageService.sendMessageFromConsole(
                    userService.getUserName(targetPlayer.get()),
                    message,
                    consoleClass);
                return 1;
            }

            if (targetPlayer.isPresent()) {
                if (targetPlayer.get() == context.getSource()) {
                    textService.send(pluginMessages.messageSelf(), context.getSource());
                    return 0;
                }
                privateMessageService.sendMessage(
                    userService.getUserName((TPlayer) context.getSource()),
                    name,
                    message
                );
                privateMessageService.replyMap().put(
                    userService.getUUID(targetPlayer.get()),
                    userService.getUUID((TPlayer) context.getSource())
                );
                privateMessageService.replyMap().put(
                    userService.getUUID((TPlayer) context.getSource()),
                    userService.getUUID(targetPlayer.get())
                );
            } else {
                textService.send(pluginMessages.offlineOrInvalidPlayer(), context.getSource());
            }
        }
        return 1;
    }
}
