package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;
import rocks.milspecsg.msessentials.modules.utils.StaffListUtils;

//This class was initially written by LGC_McLovin of MilspecSG
public class StaffListCommand implements Command {

    @Inject
    private StaffListUtils staffListUtils;

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (!source.hasPermission(PluginPermissions.STAFFLIST_BASE)) {
            source.sendMessage(pluginMessages.noPermission);
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
                    .append(MSEssentialsPluginInfo.pluginPrefix)
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
