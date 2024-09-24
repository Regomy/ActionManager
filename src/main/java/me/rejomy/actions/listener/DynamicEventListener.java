package me.rejomy.actions.listener;

import me.rejomy.actions.ActionsAPI;
import me.rejomy.actions.util.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class DynamicEventListener {

    final JavaPlugin plugin;

    public DynamicEventListener() {
        this.plugin = ActionsAPI.INSTANCE.getPlugin();

        registerAllEvents();
    }

    private void registerAllEvents() {
        // Initialize Reflections to scan the package containing Bukkit events
        Reflections reflections = new Reflections("org.bukkit.event");

        // Find all classes that extend Bukkit's Event class
        Set<Class<? extends Event>> eventClasses = reflections.getSubTypesOf(Event.class);

        // Register each event class dynamically
        for (Class<? extends Event> eventClass : eventClasses) {
            if (canAddToListener(eventClass)) {
                registerEventHandler(eventClass);
            }
        }
    }

    private boolean canAddToListener(Class<? extends Event> eventClass) {
        // Dont add events who not used in our action.
        if (!ActionsAPI.INSTANCE.getConfig().getActionsByEvent().containsKey(eventClass)) {
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
        Bukkit.getPluginManager().registerEvent(eventClass, new Listener() {
        }, EventPriority.NORMAL, new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) {
                if (eventClass.isInstance(event)) {
                    EventHandler.handleEvent(eventClass.cast(event));
                }
            }
        }, plugin);
    }
}
