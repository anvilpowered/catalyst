package org.anvilpowered.catalyst.bungee.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.CommandSender;
import org.anvilpowered.anvil.api.registry.Registry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Singleton
public class BungeeCommandDispatcher {

    protected final CommandDispatcher<CommandSender> commandDispatcher = new CommandDispatcher<>();

    protected final Map<String, List<String>> commands = new HashMap<>();

    @Inject
    public BungeeCommandDispatcher(Registry registry) {

    }

    public void register(String command, LiteralCommandNode<CommandSender> node, List<String> alias) {
        commands.put(command, alias);
        commandDispatcher.getRoot().addChild(node);
    }

    public void execute(String input, CommandSender sender) throws CommandSyntaxException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(sender);
        // Prechecked by requireNonNull
        String command = Arrays.stream(input.split(" ")).findFirst().get();

        for (String internal : commands.keySet()) {
            if (internal.equalsIgnoreCase(command)) {
                commandDispatcher.execute(input, sender);
                return;
            }
        }
        // Check for aliases
        for (Map.Entry<String, List<String>> entry : commands.entrySet()) {
            if (entry.getValue().contains(command)) {
                commandDispatcher.execute(input.replace(command, entry.getKey()), sender);
            }
        }
    }
}
