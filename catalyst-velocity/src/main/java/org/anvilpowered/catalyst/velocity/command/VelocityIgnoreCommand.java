package org.anvilpowered.catalyst.velocity.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.catalyst.common.command.CommonIgnoreCommand;

public class VelocityIgnoreCommand extends CommonIgnoreCommand<
    TextComponent,
    Player,
    CommandSource>
    implements Command {
}
