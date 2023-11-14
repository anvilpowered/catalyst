/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.velocity.module

import com.google.inject.Singleton
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import org.anvilpowered.anvil.api.misc.bind
import org.anvilpowered.anvil.api.misc.to
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.api.discord.WebhookSender
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.chat.BroadcastService
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.ChatService
import org.anvilpowered.catalyst.api.service.EventRegistrationService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import org.anvilpowered.catalyst.api.service.StaffListService
import org.anvilpowered.catalyst.api.service.TabService
import org.anvilpowered.catalyst.core.discord.JDAService
import org.anvilpowered.catalyst.core.discord.CommonWebhookSender
import org.anvilpowered.catalyst.common.member.CommonMemberManager
import org.anvilpowered.catalyst.common.module.CommonModule
import org.anvilpowered.catalyst.common.service.CommonChannelService
import org.anvilpowered.catalyst.common.service.CommonChatService
import org.anvilpowered.catalyst.common.service.CommonEventRegistrationService
import org.anvilpowered.catalyst.common.service.CommonLuckpermsService
import org.anvilpowered.catalyst.common.service.CommonPrivateMessageService
import org.anvilpowered.catalyst.common.service.CommonStaffListService
import org.anvilpowered.catalyst.common.service.CommonTabService
import org.anvilpowered.catalyst.velocity.service.VelocityBroadcastService
import org.anvilpowered.catalyst.velocity.service.VelocityDiscordCommandService

@Singleton
class VelocityModule : CommonModule("plugins") {
    override fun configure() {
        with(binder()) {
            bind<BroadcastService>().to<VelocityBroadcastService>()
            bind<DiscordCommandService>().to<VelocityDiscordCommandService>()
            bind<MemberManager>().to<CommonMemberManager<Player>>()
            bind<ChannelService<Player>>().to<CommonChannelService<Player>>()
            bind<ChatService<Player, CommandSource>>().to<CommonChatService<Player, CommandSource>>()
            bind<PrivateMessageService>().to<CommonPrivateMessageService<Player>>()
            bind<StaffListService>().to<CommonStaffListService<Player>>()
            bind<TabService<Player>>().to<CommonTabService<Player>>()
            bind<LuckpermsService>().to<CommonLuckpermsService<Player>>()
            bind<JDAService>().to<JDAService<Player>>()
            bind<WebhookSender>().to<CommonWebhookSender<Player>>()
            bind<EventRegistrationService>().to<CommonEventRegistrationService<Player, CommandSource>>()
        }
        super.configure()
    }
}
