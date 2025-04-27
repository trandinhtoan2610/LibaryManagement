package GUI.Component.Panel.Statistics.Components;

import com.google.common.eventbus.EventBus;

public class EventBusManager {
    private static final EventBus eventBus = new EventBus();
    public static EventBus getEventBus() {
        return eventBus;
    }
    private EventBusManager() {}
}
