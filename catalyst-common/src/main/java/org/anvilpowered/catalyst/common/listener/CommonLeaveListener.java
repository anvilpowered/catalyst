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
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.listener.LeaveListener;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.BroadcastService;
import org.anvilpowered.catalyst.api.service.EmojiService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.slf4j.Logger;

public class CommonLeaveListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource>
    implements LeaveListener<TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private StaffListService<TString> staffListService;

    @Inject
    private BroadcastService<TString> broadcastService;

    @Inject
    private Logger logger;

    @Inject
    private EmojiService emojiService;


    @Override
    public void onPlayerLeave(LeaveEvent<TPlayer> event) {
        TPlayer player = event.getPlayer();
        staffListService.removeStaffNames(userService.getUserName((TUser) player));

        String message = registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)
            ? emojiService.toEmoji(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE), "&f")
            : registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE);

        if (registry.getOrDefault(CatalystKeys.LEAVE_LISTENER_ENABLED)) {
            broadcastService.broadcast(
                textService.deserialize(
                    message
                        .replace("%player%", userService.getUserName((TUser) player))
                ));

            logger.info(
                textService.serializePlain(
                    textService.of(
                        registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
                            .replace("%player%", userService.getUserName((TUser) player))
                    )
                )
            );
        }
    }
}
