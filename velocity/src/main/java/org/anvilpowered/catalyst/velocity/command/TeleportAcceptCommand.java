package org.anvilpowered.catalyst.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.common.command.CommonTeleportAcceptCommand;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeleportAcceptCommand extends CommonTeleportAcceptCommand<
    TextComponent,
    Player,
    Player,
    CommandSource,
    PermissionSubject>
implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        execute(source, source);
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0])
                .stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
