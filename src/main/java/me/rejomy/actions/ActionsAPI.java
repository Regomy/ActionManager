package me.rejomy.actions;

import lombok.Getter;
import me.rejomy.actions.config.impl.Config;
import me.rejomy.actions.listener.DynamicEventListener;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Add /config reload command.
@Getter
public enum ActionsAPI {
    INSTANCE;

    private Config config;
    private JavaPlugin plugin;

    public void load(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(JavaPlugin plugin) {
        this.plugin = plugin;

        // TODO: move this to file manager.
        plugin.saveDefaultConfig();
        this.config = new Config(plugin.getConfig());

        config.load();

        new DynamicEventListener();
    }

    public void stop(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
