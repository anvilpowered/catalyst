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

package org.anvilpowered.catalyst.common.plugin;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.TimeFormatService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class CatalystPluginMessages<TString, TCommandSource> implements PluginMessages<TString> {

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected TextService<TString, TCommandSource> textService;

    @Inject
    protected TimeFormatService timeFormatService;

    @Override
    public TString getBroadcast(TString message) {
        return textService.builder()
            .green().append("[Broadcast] ", message)
            .build();
    }

    @Override
    public TString getBroadcast(String message) {
        return getBroadcast(textService.deserialize(message));
    }

    @Override
    public TString getNotEnoughArgs() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("Not enough arguments!")
            .build();
    }

    @Override
    public TString getNoPermission() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append(("Insufficient permissions!"))
            .build();
    }

    @Override
    public TString getNoServerPermission() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You do not have permission to join this server!")
            .build();
    }

    @Override
    public TString getNoNickColorPermission() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You do not have permission for a colored nickname!")
            .build();
    }

    @Override
    public TString getNoNickMagicPermission() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You do not have permission for a magical nickname!")
            .build();
    }

    @Override
    public TString getCurrentServer(String userName, String serverName) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .gold().append(userName)
            .green().append(" is connected to ")
            .gold().append(serverName, ".")
            .build();
    }

    @Override
    public TString getMuted() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You are muted!")
            .build();
    }

    @Override
    public TString getMuteExempt() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being muted.")
            .build();
    }

    @Override
    public TString getBanExempt() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being banned.")
            .build();
    }

    @Override
    public TString getKickExempt() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being kicked.")
            .build();
    }

    @Override
    public TString getSocialSpy(boolean enabled) {
        TextService.Builder<TString, TCommandSource> builder = textService.builder()
            .append(pluginInfo.getPrefix())
            .yellow().append("SocialSpy ");
        if (enabled) {
            return builder.green().append("enabled").build();
        }
        return builder.red().append("disabled").build();
    }

    @Override
    public TString getStaffChat(boolean enabled) {
        TextService.Builder<TString, TCommandSource> builder = textService.builder()
            .append(pluginInfo.getPrefix())
            .yellow().append("Staff Chat ");
        if (enabled) {
            return builder.green().append("enabled").build();
        }
        return builder.red().append("disabled").build();
    }

    @Override
    public TString getStaffChatMessageFormatted(String userName, TString message) {
        return textService.builder()
            .aqua().append("[STAFF] ")
            .light_purple().append(userName, ": ", message)
            .build();
    }

    @Override
    public TString getStaffChatMessageFormattedConsole(TString message) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .aqua().append("[STAFF] ")
            .light_purple().append("CONSOLE: ", message)
            .build();
    }

    @Override
    public TString getTeleportRequestSent(String targetUserName) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .green().append("Requested to teleport to ")
            .gold().append(targetUserName, ".")
            .build();
    }

    @Override
    public TString getTeleportRequestReceived(String requesterUserName) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .gold().append(requesterUserName)
            .green().append(" has requested to teleport to you")
            .onHoverShowText(textService.builder().green().append("Click to accept"))
            .onClickRunCommand("/tpaccept")
            .build();
    }

    @Override
    public TString getTeleportToSelf() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You cannot teleport to yourself!")
            .build();
    }

    @Override
    public TString getIncompatibleServerVersion() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("The server you are attempting to connect to is running a different Minecraft version!")
            .build();
    }

    private TString getForList(boolean a, boolean b, boolean c, String value) {
        String _a = a ? "swear" : "exception";
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("The ", _a, " ")
            .yellow().append(value)
            .red().append(" ", b ? c ? "is already in" : "is not in" : c ? "was added to" : "was removed from", " the ", _a, " list.")
            .build();
    }

    @Override
    public TString getExistingSwear(String swear) {
        return getForList(true, true, true, swear);
    }

    @Override
    public TString getExistingException(String exception) {
        return getForList(false, true, true, exception);
    }

    @Override
    public TString getMissingSwear(String swear) {
        return getForList(true, true, false, swear);
    }

    @Override
    public TString getMissingException(String exception) {
        return getForList(false, true, false, exception);
    }

    @Override
    public TString getNewSwear(String swear) {
        return getForList(true, false, true, swear);
    }

    @Override
    public TString getNewException(String exception) {
        return getForList(false, false, true, exception);
    }

    @Override
    public TString getRemoveSwear(String swear) {
        return getForList(true, false, false, swear);
    }

    @Override
    public TString getRemoveException(String exception) {
        return getForList(false, false, false, exception);
    }

    public String removeColor(String in) {
        return in.replace("&0", "").replaceAll("&1", "").replaceAll("&2", "")
            .replaceAll("&3", "").replaceAll("&4", "").replaceAll("&5", "")
            .replaceAll("&6", "").replaceAll("&7", "").replaceAll("&8", "")
            .replaceAll("&a", "").replaceAll("&b", "").replaceAll("&c", "")
            .replaceAll("&d", "").replaceAll("&e", "").replaceAll("&f", "")
            .replaceAll("&k", "").replaceAll("&l", "").replaceAll("&m", "")
            .replaceAll("&n", "").replaceAll("&o", "").replaceAll("&r", "")
            .replaceAll("&k", "").replaceAll("&9", "");
    }

    @Override
    public TString banCommandUsage() {
        return textService.builder()
            .red().append(textService.of("Usage:"))
            .yellow().append("/ban <user> [reason]")
            .build();
    }

    @Override
    public TString tempBanCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/tempban <user> <duration> [reason]")
            .build();
    }

    @Override
    public TString unbanCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/unban <user>")
            .build();
    }

    @Override
    public TString muteCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/mute <user> [reason]")
            .build();
    }

    @Override
    public TString tempMuteCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/tempmute <user> <duration> [reason]")
            .build();
    }

    @Override
    public TString unMuteCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/unmute <player>")
            .build();
    }

    @Override
    public TString kickCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/kick <user> [reason]")
            .build();
    }

    @Override
    public TString findCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/find <user>")
            .build();
    }

    @Override
    public TString sendCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/send <user> <server>")
            .build();
    }

    @Override
    public TString messageCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/message <user> <message>")
            .build();
    }

    @Override
    public TString nickNameCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/nick <nickname>")
            .build();
    }

    @Override
    public TString broadcastCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/broadcast <message>")
            .build();
    }

    @Override
    public TString infoCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/info <user>")
            .build();
    }

    @Override
    public TString swearAddCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/swear (add|remove <word>) | list")
            .build();
    }

    @Override
    public TString exceptionAddCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/exception (add|remove <word) | list")
            .build();
    }

    @Override
    public TString ignoreCommandUsage() {
        return textService.builder()
            .red().append("Usage: ")
            .yellow().append("/ignore <player>")
            .build();
    }

    @Override
    public TString ignoreExempt() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being ignored.")
            .build();
    }

    @Override
    public TString offlineOrInvalidPlayer() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .yellow().append("Invalid or offline player!")
            .build();
    }

    @Override
    public TString messageSelf() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You cannot send private messages to yourself!")
            .build();
    }

    @Override
    public TString getBanMessage(String reason, Instant endUtc) {
        return textService.builder()
            .red().append("You have been banned for: ", textService.deserialize(reason))
            .yellow().append("\n\nFor another ",
                timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc)).withoutNano())
            .append("\n\nUntil ", timeFormatService.format(endUtc).withoutNano())
            .build();
    }

    @Override
    public TString getMuteMessage(String reason, Instant endUtc) {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You have been muted for: ", textService.deserialize(reason))
            .yellow().append("\nFor another ", timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc)).withoutNano())
            .build();
    }

    @Override
    public TString getInvalidServer() {
        return textService.builder()
            .append(pluginInfo.getPrefix())
            .yellow().append("Invalid server!")
            .build();
    }
}
