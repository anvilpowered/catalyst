package org.anvilpowered.catalyst.common.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.CommandEvent;
import org.anvilpowered.catalyst.api.listener.CommandListener;
import org.slf4j.Logger;

import java.util.List;

public class CommonCommandListener implements CommandListener {

    @Inject
    private Logger logger;

    @Inject
    private Registry registry;

    @Override
    public void onCommandExecution(CommandEvent event) {
        if (registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_ENABLED)) {
            List<String> commandList = registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_FILTER);
            if ((commandList.size() == 1 && commandList.get(0).equals("*"))
                || commandList.contains(event.getCommand())) {
                logger.info(event.getSourceName() + " executed command : " + event.getCommand());
            }
        }
    }
}
