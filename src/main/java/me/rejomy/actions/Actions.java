package me.rejomy.actions;

import org.bukkit.plugin.java.JavaPlugin;

public class Actions extends JavaPlugin {

    @Override
    public void onLoad() {
        ActionsAPI.INSTANCE.load(this);
    }

    @Override
    public void onDisable() {
        ActionsAPI.INSTANCE.stop(this);
    }

    @Override
    public void onEnable() {
        ActionsAPI.INSTANCE.start(this);
    }
}
