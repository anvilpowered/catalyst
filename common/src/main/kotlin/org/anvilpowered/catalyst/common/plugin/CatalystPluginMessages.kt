/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.plugin

import com.google.inject.Inject
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.TimeFormatService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class CatalystPluginMessages<TString, TCommandSource> @Inject constructor(
  private val textService: TextService<TString, TCommandSource>,
  private val timeFormatService: TimeFormatService
) : PluginMessages<TString> {

  override fun getBroadcast(message: TString): TString {
    return textService.builder()
      .green().append("[Broadcast] ", message)
      .build()
  }

  override fun getBroadcast(message: String): TString = getBroadcast(textService.deserialize(message))

  override fun getNotEnoughArgs(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("Not enough arguments!")
      .build()
  }

  override fun getNoPermission(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("Insufficient permissions!")
      .build()
  }

  override fun getNoServerPermission(serverName: String): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You do not have permission to enter ")
      .gold().append(serverName)
      .red().append("!")
      .build()
  }

  override fun getNoNickColorPermission(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You do not have permission for a colored nickname!")
      .build()
  }

  override fun getNoNickMagicPermission(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You do not have permission for a magical nickname!")
      .build()
  }

  override fun getCurrentServer(userName: String, serverName: String): TString {
    return textService.builder()
      .appendPrefix()
      .gold().append(userName)
      .green().append(" is connected to ")
      .gold().append(serverName, ".")
      .build()
  }

  override fun getMuted(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You are muted!")
      .build()
  }

  override fun getMuteExempt(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("This user is exempt from being muted.")
      .build()
  }

  override fun getBanExempt(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("This user is exempt from being banned.")
      .build()
  }

  override fun getKickExempt(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("This user is exempt from being kicked.")
      .build()
  }

  override fun getSocialSpy(enabled: Boolean): TString {
    val builder = textService.builder()
      .appendPrefix()
      .yellow().append("SocialSpy ")
    return if (enabled) {
      builder.green().append("enabled").build()
    } else builder.red().append("disabled").build()
  }

  override fun getStaffChat(enabled: Boolean): TString {
    val builder = textService.builder()
      .appendPrefix()
      .yellow().append("Staff Chat ")
    return if (enabled) {
      builder.green().append("enabled").build()
    } else builder.red().append("disabled").build()
  }

  override fun getStaffChatMessageFormatted(userName: String, message: TString): TString {
    return textService.builder()
      .aqua().append("[STAFF] ")
      .light_purple().append(userName, ": ", message)
      .build()
  }

  override fun getStaffChatMessageFormattedConsole(message: TString): TString {
    return textService.builder()
      .appendPrefix()
      .aqua().append("[STAFF] ")
      .light_purple().append("CONSOLE: ", message)
      .build()
  }

  override fun getTeleportRequestSent(targetUserName: String): TString {
    return textService.builder()
      .appendPrefix()
      .green().append("Requested to teleport to ")
      .gold().append(targetUserName, ".")
      .build()
  }

  override fun getTeleportRequestReceived(requesterUserName: String): TString {
    return textService.builder()
      .appendPrefix()
      .gold().append(requesterUserName)
      .green().append(" has requested to teleport to you")
      .onHoverShowText(textService.builder().green().append("Click to accept"))
      .onClickRunCommand("/tpaccept")
      .build()
  }

  override fun getTeleportToSelf(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You cannot teleport to yourself!")
      .build()
  }

  override fun getIncompatibleServerVersion(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("The server you are attempting to connect to is running a different Minecraft version!")
      .build()
  }

  private fun getForList(a: Boolean, b: Boolean, c: Boolean, value: String): TString {
    val _a = if (a) "swear" else "exception"
    return textService.builder()
      .appendPrefix()
      .red().append("The ", _a, " ")
      .yellow().append(value)
      .red().append(" ", if (b) if (c) "is already in" else "is not in" else if (c) "was added to" else "was removed from", " the ", _a, " list.")
      .build()
  }

  override fun getExistingSwear(swear: String): TString = getForList(true, true, true, swear)
  override fun getExistingException(exception: String): TString = getForList(false, true, true, exception)
  override fun getMissingSwear(swear: String): TString = getForList(true, true, false, swear)
  override fun getMissingException(exception: String): TString = getForList(false, true, false, exception)
  override fun getNewSwear(swear: String): TString = getForList(true, false, true, swear)
  override fun getNewException(exception: String): TString = getForList(false, false, true, exception)
  override fun getRemoveSwear(swear: String): TString = getForList(true, false, false, swear)
  override fun getRemoveException(exception: String): TString = getForList(false, false, false, exception)

  override fun banCommandUsage(): TString {
    return textService.builder()
      .red().append(textService.of("Usage:"))
      .yellow().append("/ban <user> [reason]")
      .build()
  }

  override fun tempBanCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/tempban <user> <duration> [reason]")
      .build()
  }

  override fun unbanCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/unban <user>")
      .build()
  }

  override fun muteCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/mute <user> [reason]")
      .build()
  }

  override fun tempMuteCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/tempmute <user> <duration> [reason]")
      .build()
  }

  override fun unMuteCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/unmute <player>")
      .build()
  }

  override fun kickCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/kick <user> [reason]")
      .build()
  }

  override fun findCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/find <user>")
      .build()
  }

  override fun sendCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/send <user> <server>")
      .build()
  }

  override fun messageCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/message <user> <message>")
      .build()
  }

  override fun replyCommandUsage(): TString {
    return textService.deserialize("&4Usage: /reply <message>")
  }

  override fun nickNameCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/nick <nickname>")
      .build()
  }

  override fun deleteNickOtherCommandUsage(): TString {
    return textService.deserialize("&4Usage: /delnick other <player>")
  }

  override fun broadcastCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/broadcast <message>")
      .build()
  }

  override fun infoCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/info <user>")
      .build()
  }

  override fun channelEditStartUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/channel edit start <channel>")
      .build()
  }

  override fun channelEditPropertyUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/channel edit property <name> <\"value\">")
      .build()
  }

  override fun swearAddCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/swear (add|remove <word>) | list")
      .build()
  }

  override fun exceptionAddCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/exception (add|remove <word) | list")
      .build()
  }

  override fun ignoreCommandUsage(): TString {
    return textService.builder()
      .red().append("Usage: ")
      .yellow().append("/ignore <player>")
      .build()
  }

  override fun ignoreExempt(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("This user is exempt from being ignored.")
      .build()
  }

  override fun offlineOrInvalidPlayer(): TString {
    return textService.builder()
      .appendPrefix()
      .yellow().append("Invalid or offline player!")
      .build()
  }

  override fun messageSelf(): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You cannot send private messages to yourself!")
      .build()
  }

  override fun getBanMessage(reason: String, endUtc: Instant): TString {
    return textService.builder()
      .red().append("You have been banned for: ", textService.deserialize(reason))
      .yellow().append(
        "\n\nFor another ",
        timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc)).withoutNano()
      )
      .append("\n\nUntil ", timeFormatService.format(endUtc).withoutNano())
      .build()
  }

  override fun getMuteMessage(reason: String, endUtc: Instant): TString {
    return textService.builder()
      .appendPrefix()
      .red().append("You have been muted for: ", textService.deserialize(reason))
      .yellow()
      .append("\nFor another ", timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc)).withoutNano())
      .build()
  }

  override fun getInvalidServer(): TString {
    return textService.builder()
      .appendPrefix()
      .yellow().append("Invalid server!")
      .build()
  }
}

