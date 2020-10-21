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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.service.StaffListService;

public class StaffListCommand<TString, TCommandSource> {

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    private StaffListService<TString> staffListService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    public int execute(CommandContext<TCommandSource> context) {
        if (isStaffOnline()) {
            textService.send(getLine(), context.getSource());
            if (!staffListService.staffNames().isEmpty()) {
                textService.send(textService.builder().append("Staff:").gold().build(), context.getSource());
                for (TString tString : staffListService.staffNames()) {
                    textService.send(tString, context.getSource());
                }
            }
            if (!staffListService.adminNames().isEmpty()) {
                textService.send(textService.builder().append("Admin:").gold().build(), context.getSource());
                for (TString tString : staffListService.adminNames()) {
                    textService.send(tString, context.getSource());
                }
            }
            if (!staffListService.ownerNames().isEmpty()) {
                textService.send(textService.builder().append("Owner:").gold().build(), context.getSource());
                for (TString tString : staffListService.ownerNames()) {
                    textService.send(tString, context.getSource());
                }
            }
            textService.send(getLine(), context.getSource());
        } else {
            textService.send(
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .append("There are no staff members online!")
                    .build(),
                context.getSource());
        }
        return 1;
    }

    private boolean isStaffOnline() {
        return !staffListService.staffNames().isEmpty()
            || !staffListService.adminNames().isEmpty()
            || !staffListService.ownerNames().isEmpty();
    }

    public TString getLine() {
        return textService.builder()
            .append("-----------------------------------------------------")
            .dark_aqua()
            .build();
    }

}
