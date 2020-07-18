package org.anvilpowered.catalyst.common.event;

import org.anvilpowered.catalyst.api.event.CommandEvent;

public class CommonCommandEvent implements CommandEvent {

    private Object sourceType;
    private String command;
    private boolean result;
    private String sourceName;

    @Override
    public Object getSourceType() {
        return sourceType;
    }

    @Override
    public void setSourceType(Object sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public Boolean getResult() {
        return result;
    }

    @Override
    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public void setSourceName(String name) {
        this.sourceName = name;
    }
}
