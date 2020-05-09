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

package org.anvilpowered.catalyst.common.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.listener.LeaveListener;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.StaffListService;

import java.util.UUID;

public class CommonLeaveListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource,
    TEvent>
    implements LeaveListener<TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private LuckpermsService<TPlayer> luckpermsService;

    @Inject
    private StaffListService<TString> staffListService;

    @Inject
    private BroadcastService<TString> broadcastService;

    @Inject
    private LoggerService<TString> loggerService;

    @Inject
    private LeaveEvent<TPlayer> leaveEvent;

    @Inject
    private EventService<TEvent> eventService;

    @Inject
    private DiscordChatListener<TString, TPlayer> discordChatListener;

    @Inject
    private EmojiService emojiService;

    @Override
    public void onPlayerLeave(TPlayer player, UUID playerUUID) {
        staffListService.removeStaffNames(userService.getUserName((TUser) player));
        luckpermsService.removePlayerFromCache(player);

        String message = registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)
            ? emojiService.toEmoji(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE), "&f")
            : registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE);

        broadcastService.broadcast(
            textService.deserialize(
                message
                    .replace("%player%", userService.getUserName((TUser) player))
            ));

        loggerService.info(
            textService.deserialize(
                registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
                    .replace("%player%", userService.getUserName((TUser) player))
            )
        );
        leaveEvent.setPlayer(player);
        eventService.fire((TEvent) leaveEvent);
        discordChatListener.onPlayerLeaveEvent(leaveEvent);
    }
}
