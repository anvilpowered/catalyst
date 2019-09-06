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
        UUID in = entry.getProfile().getId();
        List<UUID> contained = new ArrayList<>();
        Map<UUID, TabListEntry> cache = new HashMap<UUID, TabListEntry>();
        for(TabListEntry current : list.getEntries())
        {
            contained.add(current.getProfile().getId());
            cache.put(current.getProfile().getId(), current);
        }

        if(!contained.contains(in))
        {
            list.addEntry(entry);
            toKeep.add(in);
            return;
        }
        else
        {
            TabListEntry currentEntry = cache.get(in);
            if(!currentEntry.getDisplayName().equals(entry.getDisplayName()))
            {
                list.removeEntry(in);
                list.addEntry(entry);
                toKeep.add(in);
            }else
            {
                toKeep.add(in);
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


        }).repeat(10, TimeUnit.MINUTES).schedule();
    }

}
