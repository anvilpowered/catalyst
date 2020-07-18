package org.anvilpowered.catalyst.api.listener;

import com.google.common.eventbus.Subscribe;
import org.anvilpowered.catalyst.api.event.CommandEvent;

public interface CommandListener {

    @Subscribe
    void onCommandExecution(CommandEvent event);
}
