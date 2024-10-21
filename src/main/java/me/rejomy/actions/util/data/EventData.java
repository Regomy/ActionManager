package me.rejomy.actions.util.data;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public record EventData(Class<? extends Event> getEvent, EventPriority getPriority, boolean isIgnoreCancelled) {

}
