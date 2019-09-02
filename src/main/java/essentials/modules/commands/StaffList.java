package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.server.MSServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class StaffList implements Command {
    MSEssentials plugin;

    public StaffList(MSEssentials plugin)
    {
        this.plugin = plugin;
    }


    static TextComponent result = null;




    @Override
    public void execute(CommandSource source, @Nonnull String[] args)
    {
        if(!(source instanceof Player))
        {

            MSEssentials.getLogger().info("You arent a player");
        }

        if(source instanceof Player)
        {
            if(isStaffOnline())
            {
                source.sendMessage(getLine());
                if(!getStaffNames().isEmpty())
                {
                    source.sendMessage(getStaffTitle());
                    for (TextComponent text : getStaffNames())
                    {
                        source.sendMessage(text);
                    }
                }
                if(!getAdminNames().isEmpty())
                {
                    source.sendMessage(getAdminTitle());
                    for (TextComponent text : getAdminNames())
                    {
                        source.sendMessage(text);
                    }
                }
                if(!getOwnerNames().isEmpty())
                {
                    source.sendMessage(getOwnerTitle());
                    for(TextComponent text :getOwnerNames())
                    {
                        source.sendMessage(text);
                    }
                }

                source.sendMessage(getLine());
            }
            if(!isStaffOnline())
            {

                result = TextComponent.builder()
                        .content("There are no staff members currently online")
                        .color(TextColor.DARK_RED)
                        .build();
            }
        }
    }

    public static boolean isStaffOnline()
    {
        for(Player player : MSEssentials.server.getAllPlayers())
        {
            if(player.hasPermission("msstafflist.staff") || player.hasPermission("msstaffflist.admin") || player.hasPermission("msstafflist.owner"))
            {
                return true;
            }
        }
        return false;
    }

    public static List<TextComponent> getStaffNames()
    {
        List<TextComponent> staffNames = new ArrayList<TextComponent>();
        for(Player player : MSEssentials.server.getAllPlayers())
        {
            TextComponent staffName;

            if (player.hasPermission("msstafflist.staff") && !player.hasPermission("msstafflist.admin") && !player.hasPermission("msstafflist.owner"))
            {

                staffName = TextComponent.builder()
                        .content(player.getUsername())
                        .build();

                staffNames.add(staffName);
            }
        }
        return staffNames;
    }

    public static List<TextComponent> getAdminNames()
    {
        List<TextComponent> adminNames = new ArrayList<TextComponent>();
        for(Player player : MSEssentials.server.getAllPlayers())
        {
            TextComponent adminName;

            if (player.hasPermission("msstafflist.admin"))
            {

                adminName = TextComponent.builder()
                        .content(player.getUsername())
                        .build();

                adminNames.add(adminName);
            }
        }
        return adminNames;
    }

    public static List<TextComponent> getOwnerNames()
    {
        List<TextComponent> ownerNames = new ArrayList<TextComponent>();
        for(Player player : MSEssentials.server.getAllPlayers())
        {
            TextComponent ownerName;

            if(player.hasPermission("msstafflist.owner"))
            {

                ownerName = TextComponent.builder()
                        .content(player.getUsername())
                        .build();

                ownerNames.add(ownerName);
            }
        }
        return ownerNames;
    }
    public static TextComponent getLine()
    {
        result = TextComponent.builder()
                .content("-----------------------------------------------------")
                .color(TextColor.DARK_AQUA)
                .build();

        return result;
    }

    public static TextComponent getStaffTitle()
    {
        result = TextComponent.builder()
                .content("Staff:")
                .color(TextColor.GOLD)
                .build();

        return result;
    }

    public static TextComponent getAdminTitle()
    {
        result = TextComponent.builder()
                .content("Admin:")
                .color(TextColor.GOLD)
                .build();

        return result;
    }
    public static TextComponent getOwnerTitle()
    {
        result = TextComponent.builder()
                .content("Owner:")
                .color(TextColor.GOLD)
                .build();

        return result;
    }
}

