package org.swallowmc;
import org.bukkit.plugin.java.JavaPlugin;


public class BedrockCommand extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new StickListener(), this);
    }
}


