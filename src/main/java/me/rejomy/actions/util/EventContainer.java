package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import org.bukkit.event.Event;
import org.reflections.Reflections;

import java.util.Set;

@UtilityClass
public class EventContainer {

    public final Set<Class<? extends Event>> ALL_BUKKIT_EVENTS = new Reflections("org.bukkit.event").getSubTypesOf(Event.class);
}
