package org.anvilpowered.catalyst.velocity.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.common.command.CommonIgnoreCommand;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IgnoreCommand extends CommonIgnoreCommand<
    TextComponent,
    Player,
    CommandSource,
    PermissionSubject>
    implements Command {

    public void execute(CommandSource source, @NonNull String[] args) {
        execute(source, source, args);
    }
}
