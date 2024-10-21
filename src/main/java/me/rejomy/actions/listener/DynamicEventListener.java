package me.rejomy.actions.listener;

import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.EventContainer;
import me.rejomy.actions.util.EventHandler;
import me.rejomy.actions.util.data.ActionData;
import me.rejomy.actions.util.data.EventData;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynamicEventListener {

    final JavaPlugin plugin;

    public DynamicEventListener() {
        this.plugin = ActionsAPI.INSTANCE.getPlugin();

        registerAllEvents();
    }

    private void registerAllEvents() {
        // Register each event class dynamically
        for (Class<? extends Event> eventClass : EventContainer.ALL_BUKKIT_EVENTS) {
            if (canAddToListener(eventClass)) {
                registerEventHandler(eventClass);
            }
        }
    }

    private boolean canAddToListener(Class<? extends Event> eventClass) {
        // Dont add events who not used in our action.
        if (ActionsAPI.INSTANCE.getConfig().getActionsByEvent().keySet()
                .stream()
                .noneMatch(eventData -> eventData.getEvent() == eventClass)) {
            return false;
        }

        // Check there is handler list or no, if this not found, we can get errors if we are remove this check
        try {
            // Check if the class has the getHandlerList method
            eventClass.getMethod("getHandlerList");
            return true;
        } catch (NoSuchMethodException e) {
            plugin.getLogger().info("Skipping event without HandlerList: " + eventClass.getName());
            return false;  // Event class does not have getHandlerList method
        }
    }

    private <T extends Event> void registerEventHandler(Class<T> eventClass) {
        for (EventData eventData : ActionsAPI.INSTANCE.getConfig().getActionsByEvent().keySet()) {
            if (eventData.getEvent() != eventClass) {
                continue;
            }

            Bukkit.getPluginManager().registerEvent(eventClass, new Listener() {

            }, eventData.getPriority(), (listener, event) -> {
                if (eventClass.isInstance(event)) {
                    EventHandler.handleEvent(eventClass.cast(event), eventData);
                }
            }, plugin, eventData.isIgnoreCancelled());
        }
    }
}
