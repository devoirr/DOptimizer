package me.kervand.optimizer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class OptimizerPlugin extends JavaPlugin {

    private final Map<String, MobGroup> groupMap = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reload();

        Bukkit.getCommandMap().register("optimizer", new OptimizerCommand(this));
        getServer().getPluginManager().registerEvents(new MobListener(this), this);
    }

    public void reload() {
        groupMap.values().forEach(MobGroup::enable);
        groupMap.clear();

        reloadConfig();
        for (String groupName : getConfig().getConfigurationSection("groups").getKeys(false)) {
            groupMap.put(groupName, new MobGroup(groupName, getConfig().getStringList("groups." + groupName)));
        }
    }

    public Map<String, MobGroup> getGroupMap() {
        return groupMap;
    }

}
