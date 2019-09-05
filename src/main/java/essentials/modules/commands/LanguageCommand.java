package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class LanguageCommand implements Command {

    public MSEssentials plugin;

    public LanguageCommand(MSEssentials main)
    {
        plugin = main;
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        TextComponent denied = TextComponent.builder()
                .content("You do not have permission for this command!")
                .build();


        String argument = Arrays.toString(args);

        TextComponent usage = TextComponent.builder()
                .content("Usage: /mslang [<list> <elist> <addswear> <addexception>] args")
                .color(TextColor.YELLOW)
                .build();

        int length = args.length;

        if (args.length == 0) {
            source.sendMessage(usage);
            return;
        } else {

            if (args[length - args.length].equals("addswear")) {
                argument = argument.replace("addswear", "").replaceAll(", ", "").replaceAll("]", "").replaceAll("\\[", "").replaceAll(" ", "");

                TextComponent success = TextComponent.builder()
                        .content("Successfully added " + argument + " to the filter!")
                        .build();
                TextComponent existing = TextComponent.builder()
                        .content("That word is already existing!")
                        .build();

                if (source instanceof Player) {
                    Player player = (Player) source;
                    if (player.hasPermission(PluginPermissions.LANGUAGEADMIN)) {
                        if (!plugin.getMSLangConfig().addSwear(argument, source)) {
                            player.sendMessage(existing);
                            return;
                        }
                        player.sendMessage(success);
                        return;
                    } else {
                        player.sendMessage(denied);
                        return;
                    }
                } else {
                    plugin.getMSLangConfig().addSwear(argument, source);
                    source.sendMessage(success);
                    return;
                }
            }

            if (args[length - args.length].equals("list") && !(args.length > 1)) {
                if (!plugin.getMSLangConfig().getSwears().isEmpty()) {
                    source.sendMessage(TextComponent.of(plugin.getMSLangConfig().getSwears().toString()));
                    return;
                } else {
                    source.sendMessage(TextComponent.of("Swear list is empty."));
                }
            }

            if (args[length - args.length].equals("elist")) {
                if (!plugin.getMSLangConfig().getExceptions().isEmpty()) {
                    source.sendMessage(TextComponent.of(plugin.getMSLangConfig().getExceptions().toString()));
                    return;
                }
                source.sendMessage(TextComponent.of("Exceptions List is empty").color(TextColor.YELLOW));
            }
            if (args[length - args.length].equals("addexception")) {
                argument = argument.replace("addexception", "").replaceAll(", ", "").replaceAll("]", "").replaceAll("\\[", "").replaceAll(" ", "");

                TextComponent successexempt = TextComponent.builder()
                        .content("You added " + argument + " to the exempt list")
                        .build();
                TextComponent existingexempt = TextComponent.builder()
                        .content(argument + " is already part of the exempted words")
                        .build();
                if (source instanceof Player) {
                    Player player = (Player) source;
                    if (player.hasPermission(PluginPermissions.LANGUAGEADMIN)) {
                        if (!plugin.getMSLangConfig().addExempt(argument, source)) {
                            player.sendMessage(existingexempt);
                            return;
                        }
                        player.sendMessage(successexempt);
                        return;
                    } else {
                        player.sendMessage(denied);
                        return;
                    }
                } else {
                    if (!plugin.getMSLangConfig().addExempt(argument, source)) {
                        source.sendMessage(existingexempt);
                        return;
                    }
                    source.sendMessage(successexempt);
                }

            }

            if (args[length - args.length].equals("removeswear")) {
                argument = argument.replace("removeswear", "").replaceAll(", ", "").replaceAll("]", "").replaceAll("\\[", "").replaceAll(" ", "");

                TextComponent success = TextComponent.builder()
                        .content("Successfully removed " + argument + " from the swear list")
                        .build();
                TextComponent nonExisting = TextComponent.builder()
                        .content(argument + " isn't part of the swears list")
                        .build();

                if (!plugin.getMSLangConfig().removeSwear(argument, source)) {
                    source.sendMessage(nonExisting);
                    return;
                }
                source.sendMessage(success);
            }

            if (args[length - args.length].equals("removeexception")) {
                argument = argument.replace("removeexception", "").replaceAll(", ", "").replaceAll("]", "").replaceAll("\\[", "").replaceAll(" ", "");

                TextComponent success = TextComponent.builder()
                        .content("Successfully removed " + argument + " from the exception list")
                        .build();

                TextComponent nonExisting = TextComponent.builder()
                        .content(argument + " isn't part of the exception list")
                        .build();

                if (!plugin.getMSLangConfig().removeExempt(argument, source)) {
                    source.sendMessage(nonExisting);
                    return;
                }
                source.sendMessage(success);
            }

        }
    }
}
