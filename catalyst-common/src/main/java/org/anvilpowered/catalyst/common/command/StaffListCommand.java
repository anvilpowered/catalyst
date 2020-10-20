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
