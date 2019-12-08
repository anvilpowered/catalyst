package essentials.modules.tab;
/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/


import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.scheduler.ScheduledTask;
import essentials.MSEssentials;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GlobalTab {
    private static ScheduledTask task;
    private static Plugin plugin;

    public static void insertIntoTab(TabList list, TabListEntry entry, List<UUID> toKeep)
    {

        UUID player = entry.getProfile().getId();
        List<UUID> existing = new ArrayList<>();
        Map<UUID, TabListEntry> precache = new HashMap<>();

        for(TabListEntry curr : list.getEntries())
        {
            if(!existing.contains(player))
            {
                list.addEntry(entry);
                toKeep.add(player);
                return;
            }
            else
            {
                TabListEntry currentEntry = precache.get(player);
                if(!currentEntry.getDisplayName().equals(entry.getDisplayName()))
                {
                    list.removeEntry(player);
                    list.addEntry(entry);
                    toKeep.add(player);
                    return;
                }
                else
                {
                    toKeep.add(player);
                    return;
                }
            }
        }
    }



    public static void schedule()
    {
        GlobalTab.task = MSEssentials.server.getScheduler().buildTask(MSEssentials.instance, () -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerHandler(),
                    Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000,
                    Integer.parseInt((String) ConfigManager.config.get("updatedelay")) * 1000);


        }).repeat(30, TimeUnit.SECONDS).schedule();
    }

}
