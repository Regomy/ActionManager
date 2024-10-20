package me.rejomy.actions.util.data;

import me.rejomy.actions.util.command.Command;
import org.bukkit.event.Event;

import java.util.List;

public record ActionData(List<Class<? extends Event>> activators, List<ConditionData> conditions,
                         List<Command> commands) {
}
