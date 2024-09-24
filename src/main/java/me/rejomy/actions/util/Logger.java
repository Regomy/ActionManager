package me.rejomy.actions.util;

import lombok.experimental.UtilityClass;
import me.rejomy.actions.ActionsAPI;

@UtilityClass
public class Logger {
    final java.util.logging.Logger logger = ActionsAPI.INSTANCE.getPlugin().getLogger();

    public void error (String... messages) {
        for (String message : messages) {
            logger.severe(message);
        }
    }

    public void info (String... messages) {
        for (String message : messages) {
            logger.info(message);
        }
    }

    public void warn (String... messages) {
        for (String message : messages) {
            logger.warning(message);
        }
    }

}
