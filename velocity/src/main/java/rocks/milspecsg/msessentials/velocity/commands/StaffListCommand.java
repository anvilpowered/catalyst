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

package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;
import rocks.milspecsg.msessentials.velocity.utils.StaffListUtils;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

//This class was initially written by LGC_McLovin of MilspecSG
public class StaffListCommand implements Command {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private StaffListUtils staffListUtils;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (!source.hasPermission(PluginPermissions.STAFFLIST_BASE)) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (isStaffOnline()) {
            source.sendMessage(getLine());
            if (!staffListUtils.staffNames.isEmpty()) {
                source.sendMessage(getStaffTitle());
                for (TextComponent text : staffListUtils.staffNames) {
                    source.sendMessage(text);
                }
            }
            if (!staffListUtils.adminNames.isEmpty()) {
                source.sendMessage(getAdminTitle());
                for (TextComponent text : staffListUtils.adminNames) {
                    source.sendMessage(text);
                }
            }
            if (!staffListUtils.ownerNames.isEmpty()) {
                source.sendMessage(getOwnerTitle());
                for (TextComponent text : staffListUtils.ownerNames) {
                    source.sendMessage(text);
                }
            }
            source.sendMessage(getLine());
        } else {
            source.sendMessage(TextComponent.builder()
                .append(pluginInfo.getPrefix())
                .append("There are no staff members currently online.")
                .build());
        }
    }

    private boolean isStaffOnline() {
        return !staffListUtils.staffNames.isEmpty() || !staffListUtils.adminNames.isEmpty() || !staffListUtils.ownerNames.isEmpty();
    }

    public TextComponent getLine() {
        return TextComponent.builder()
            .content("-----------------------------------------------------")
            .color(TextColor.DARK_AQUA)
            .build();
    }

    public TextComponent getStaffTitle() {
        return TextComponent.builder()
            .content("Staff:")
            .color(TextColor.GOLD)
            .build();
    }

    public TextComponent getAdminTitle() {
        return TextComponent.builder()
            .content("Admin:")
            .color(TextColor.GOLD)
            .build();
    }

    public TextComponent getOwnerTitle() {
        return TextComponent.builder()
            .content("Owner:")
            .color(TextColor.GOLD)
            .build();
    }
}
