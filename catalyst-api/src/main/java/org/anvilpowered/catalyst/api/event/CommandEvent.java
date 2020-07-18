package org.anvilpowered.catalyst.api.event;

public interface CommandEvent {

    Object getSourceType();

    void setSourceType(Object sourceType);

    String getCommand();

    void setCommand(String command);

    Boolean getResult();

    void setResult(boolean result);

    String getSourceName();

    void setSourceName(String name);
}
