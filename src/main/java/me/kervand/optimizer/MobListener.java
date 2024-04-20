package me.kervand.optimizer;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class MobListener implements Listener {

    private final OptimizerPlugin plugin;
    public MobListener(OptimizerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        plugin.getGroupMap().values().forEach(g -> g.handle((LivingEntity) event.getEntity()));
    }

}
