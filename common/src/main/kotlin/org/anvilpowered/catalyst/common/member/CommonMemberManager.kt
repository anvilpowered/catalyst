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

package org.anvilpowered.catalyst.common.member

import com.google.inject.Inject
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.coremember.CoreMemberManager
import org.anvilpowered.anvil.api.coremember.CoreMemberRepository
import org.anvilpowered.anvil.api.misc.Named
import org.anvilpowered.anvil.api.model.coremember.CoreMember
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.KickService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.TimeFormatService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.anvil.base.datastore.BaseManager
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.registry.CatalystKeys.NICKNAME_PREFIX
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.CompletableFuture

class CommonMemberManager<TPlayer, TString, TCommandSource> @Inject constructor(
  registry: Registry,
  private val locationService: LocationService,
  private val kickService: KickService,
  private val pluginInfo: PluginInfo<TString>,
  private val pluginMessages: CatalystPluginMessages<TString, TCommandSource>,
  private val textService: TextService<TString, TCommandSource>,
  private val timeFormatService: TimeFormatService,
  private val userService: UserService<TPlayer, TPlayer>,
  private val channelService: ChannelService<TPlayer>
) : BaseManager<CoreMemberRepository<*, *>>(registry), MemberManager<TString> {

  override fun getPrimaryComponent(): CoreMemberRepository<*, *> {
    return Anvil.getEnvironmentManager().coreEnvironment.injector.getInstance(CoreMemberManager::class.java).primaryComponent
  }

  override fun info(
    userName: String, isOnline: Boolean,
    permissions: BooleanArray
  ): CompletableFuture<TString> {
    return primaryComponent.getOneForUser(userName).thenApplyAsync { optionalMember ->
      if (!optionalMember.isPresent) {
        return@thenApplyAsync textService.fail("Could not get user data")
      }
      val member: CoreMember<*> = optionalMember.get()
      val nick: String = if (member.nickName != null) {
        member.nickName
      } else {
        "No Nickname."
      }
      val lastSeen: String = if (isOnline) {
        "Currently Online."
      } else {
        timeFormatService.format(member.lastJoinedUtc).toString()
      }
      val banReason: String = if (member.isBanned) {
        member.banReason
      } else {
        "This user is not banned."
      }
      val message = textService.builder()
      message.append(
        textService.builder()
          .blue().append("----------------Player Info----------------")
      )
      message.append(textService.builder().blue().append("\nUUID : "))
        .append(textService.builder().green().append(member.userUUID))
        .append(textService.builder().blue().append("\nUsername : "))
        .append(textService.builder().green().append(member.userName))
        .append(textService.builder().blue().append("\nNickname : "))
        .append(textService.deserialize(nick))
      if (permissions[0]) {
        message.append(textService.builder().blue().append("\nIP : ")).append(textService.builder().green().append(member.ipAddress))
      }
      message.append(textService.builder().blue().append("\nJoined Date : "))
        .append(textService.builder().green().append(member.createdUtc.toString()))
        .append(textService.builder().blue().append("\nLast Seen : "))
        .append(textService.builder().green().append(lastSeen))
      if (permissions[1]) {
        message.append(textService.builder().blue().append("\nBanned : ").append(textService.builder().green().append(banReason)))
      }
      if (permissions[2]) {
        message.append(
          textService.builder().blue().append("\nChannel : ")
            .append(textService.builder().green().append(channelService.getChannelIdForUser(member.userUUID)))
        )
      }
      message.append(
        textService.builder()
          .blue().append("\nCurrent Server : ")
          .append(
            textService.builder().gold()
              .append(locationService.getServer(member.userUUID).map { obj: Named -> obj.name }.orElse("Offline User."))
          )
      )
      message.build()
    }.exceptionally { e: Throwable ->
      e.printStackTrace()
      null
    }
  }

  override fun setNickName(userName: String, nickName: String): CompletableFuture<TString> {
    return primaryComponent.setNickNameForUser(userName, registry.getOrDefault(NICKNAME_PREFIX).toString() + nickName)
      .thenApplyAsync { result: Boolean ->
        if (result) {
          textService.success("Set nickname to $nickName")
        } else {
          textService.fail("Failed to set the nickname $nickName")
        }
      }
  }

  override fun setNickNameForUser(userName: String, nickName: String): CompletableFuture<TString> {
    return primaryComponent.setNickNameForUser(userName, registry.getOrDefault(NICKNAME_PREFIX).toString() + nickName)
      .thenApplyAsync { result: Boolean ->
        if (result) {
          userService.getPlayer(userName).ifPresent {
            textService.builder().green().append("Your nickname was set to $nickName").sendTo(it as TCommandSource)
          }
          textService.success("Set $userName's nickname to $nickName")
        } else {
          textService.fail("Failed to set the nickname for $userName")
        }
      }
  }

  override fun deleteNickNameForUser(userName: String): CompletableFuture<TString> {
    return primaryComponent.deleteNickNameForUser(userName).thenApplyAsync { result: Boolean ->
      if (result) {
        userService.getPlayer(userName).ifPresent { p: TPlayer ->
          textService.builder().green().append("Your nickname was deleted.").sendTo(p as TCommandSource)
        }
        textService.success("Successfully deleted $userName's nickname.")
      } else {
        textService.fail("Failed to delete $userName's nickname.")
      }
    }
  }

  override fun deleteNickName(userName: String): CompletableFuture<TString> {
    return primaryComponent.deleteNickNameForUser(userName).thenApplyAsync { result: Boolean ->
      if (result) {
        textService.success("Successfully deleted your nickname.")
      } else {
        textService.fail("Failed to delete your nickname.")
      }
    }
  }

  override fun ban(userName: String, reason: String): CompletableFuture<TString> {
    val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600))
    return primaryComponent.banUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
      if (b) {
        kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc))
        textService.success("Banned $userName for $reason")
      }
      textService.fail("Invalid user.")
    }
  }

  override fun ban(userName: String): CompletableFuture<TString> = ban(userName, "The ban hammer has spoken.")
  override fun tempBan(userName: String, duration: String): CompletableFuture<TString> = tempBan(userName, duration, "The ban hammer has spoken.")

  override fun tempBan(userName: String, duration: String, reason: String): CompletableFuture<TString> {
    val optionalDuration = timeFormatService.parseDuration(duration)
    if (!optionalDuration.isPresent) {
      return CompletableFuture.completedFuture(textService.fail("Invalid input for duration"))
    }
    val dur = optionalDuration.get()
    val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur)
    return primaryComponent.banUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
      if (b) {
        kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc))
        textService.success("Banned " + userName + " for " + reason + " for " + timeFormatService.format(dur).toString())
      }
      textService.fail("Invalid user.")
    }
  }

  override fun unBan(userName: String): CompletableFuture<TString> {
    return primaryComponent.unBanUser(userName).thenApplyAsync { b: Boolean ->
      if (b) {
        textService.success("Unbanned $userName")
      }
      textService.fail("Invalid user.")
    }
  }

  override fun mute(userName: String): CompletableFuture<TString> = mute(userName, "You have been muted.")

  override fun mute(userName: String, reason: String): CompletableFuture<TString> {
    val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600))
    return primaryComponent.muteUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
      if (b) {
        userService.getPlayer(userName).ifPresent { textService.send(pluginMessages.getMuteMessage(reason, endUtc), it as TCommandSource) }
        textService.success("Muted $userName")
      }
      textService.fail("Invalid user.")
    }
  }

  override fun tempMute(userName: String, duration: String, reason: String): CompletableFuture<TString> {
    val optionalDuration = timeFormatService.parseDuration(duration)
    if (!optionalDuration.isPresent) {
      return CompletableFuture.completedFuture(textService.fail("Invalid input for duration"))
    }
    val dur = optionalDuration.get()
    val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(dur)
    return primaryComponent.muteUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
      if (b) {
        userService.getPlayer(userName).ifPresent { textService.send(pluginMessages.getMuteMessage(reason, endUtc), it as TCommandSource) }
        textService.success("Muted " + userName + " for " + reason + " for " + timeFormatService.format(dur).toString())
      }
      textService.fail("Invalid user.")
    }
  }

  override fun tempMute(userName: String, duration: String): CompletableFuture<TString> = tempMute(userName, duration, "You have been muted.")

  override fun unMute(userName: String): CompletableFuture<TString> {
    return primaryComponent.unMuteUser(userName).thenApplyAsync { b: Boolean ->
      if (b) {
        userService.getPlayer(userName).ifPresent {
          textService.builder()
            .append(pluginInfo.prefix)
            .yellow().append("You have been unmuted.")
            .sendTo(it as TCommandSource)
        }
        textService.success("UnMuted $userName")
      }
      textService.fail("Invalid user.")
    }
  }
}
