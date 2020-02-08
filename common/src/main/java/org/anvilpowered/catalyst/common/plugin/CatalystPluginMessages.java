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
import org.anvilpowered.anvil.api.util.StringResult;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class CatalystPluginMessages<TString, TCommandSource> implements PluginMessages<TString> {

    @Inject
    protected PluginInfo<TString> pluginInfo;

    @Inject
    protected StringResult<TString, TCommandSource> stringResult;

    @Override
    public TString getBroadcast(TString message) {
        return stringResult.builder()
            .green().append("[Broadcast] ", message)
            .build();
    }

    @Override
    public TString getBroadcast(String message) {
        return getBroadcast(stringResult.deserialize(message));
    }

    @Override
    public TString getNotEnoughArgs() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("Not enough arguments!")
            .build();
    }

    @Override
    public TString getNoPermission() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append(("Insufficient permissions!"))
            .build();
    }

    @Override
    public TString getNoNickColorPermission() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You do not have permission for a colored nickname!")
            .build();
    }

    @Override
    public TString getCurrentServer(String userName, String serverName) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .gold().append(userName)
            .gray().append(" is connected to ")
            .green().append(serverName, ".")
            .build();
    }

    @Override
    public TString getMuted() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You are muted!")
            .build();
    }

    @Override
    public TString getMuteExempt() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being muted.")
            .build();
    }

    @Override
    public TString getBanExempt() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being banned.")
            .build();
    }

    @Override
    public TString getKickExempt() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("This user is exempt from being kicked.")
            .build();
    }

    @Override
    public TString getSocialSpy(boolean enabled) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .green().append("Socialspy ", enabled ? "enabled" : "disabled")
            .build();
    }

    @Override
    public TString getStaffChat(boolean enabled) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .green().append("Staff chat ", enabled ? "enabled" : "disabled")
            .build();
    }

    @Override
    public TString getStaffChatMessageFormatted(String userName, TString message) {
        return stringResult.builder()
            .aqua().append("[STAFF] ")
            .light_purple().append(userName, ": ", message)
            .build();
    }

    @Override
    public TString getStaffChatMessageFormattedConsole(TString message) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .aqua().append("[STAFF] ")
            .light_purple().append("CONSOLE: ", message)
            .build();
    }

    @Override
    public TString getTeleportRequestSent(String targetUserName) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .green().append("Requested to teleport to ")
            .gold().append(targetUserName, ".")
            .build();
    }

    @Override
    public TString getTeleportRequestReceived(String requesterUserName) {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .gold().append(requesterUserName)
            .green().append(" has requested to teleport to you")
            .onHoverShowText(stringResult.builder().green().append("Click to accept"))
            .onClickRunCommand("/tpaccept")
            .build();
    }

    @Override
    public TString getTeleportToSelf() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You cannot teleport to yourself!")
            .build();
    }

    @Override
    public TString getIncompatibleServerVersion() {
        return stringResult.builder()
            .append(pluginInfo.getPrefix())
            .red().append("The server you are attempting to connect to is running a different Minecraft version!")
            .build();
    }

    private TString getForList(boolean a, boolean b, boolean c, String value) {
        String _a = a ? "swear" : "exception";
        return stringResult.builder()
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
}
