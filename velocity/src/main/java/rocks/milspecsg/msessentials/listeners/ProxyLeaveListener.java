/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.utils.PlayerListUtils;
import rocks.milspecsg.msessentials.modules.utils.StaffListUtils;

import java.util.Date;

public class ProxyLeaveListener {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private StaffListUtils staffListUtils;

    @Inject
    private PlayerListUtils playerListUtils;

    @Subscribe
    public void onPlayerJoin(DisconnectEvent event) {
        Player player = event.getPlayer();
        memberManager.setLastSeenUtc(player.getUniqueId(), new Date());
        staffListUtils.removeStaffNames(player);
        playerListUtils.removePlayer(player);
    }
}
