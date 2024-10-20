package me.rejomy.actions.util.command;

import lombok.Getter;
import me.rejomy.actions.util.RegexUtil;

import java.util.Arrays;
import java.util.List;

@Getter
public class Command {

    String command;

    boolean iterate;
    List<String> conditions;

    public Command(String command) {
        String[] parts = command.split(" ");
        iterate = parts[0].contains("{iterate");

        if (iterate) {
            this.command = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

            String[] expressions = RegexUtil.findFirst(parts[0], "\\(([^)]+)\\)").split(",\\s?");
            this.conditions = List.of(expressions);
        } else {
            this.command = command;
        }
    }
}
