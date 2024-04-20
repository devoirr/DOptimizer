package me.kervand.optimizer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MobGroup {

    private boolean enabled = true;
    private final LinkedList<UUID> disabled = new LinkedList<>();
    private final List<EntityType> types = new ArrayList<>();
    public MobGroup(String key, List<String> types) {
        types.forEach(type -> {
            EntityType entityType = EntityType.valueOf(type);
            if (entityType != null) {
                this.types.add(entityType);
            }
        });
    }

    public void disable() {
        if (!enabled) {
            return;
        }

        enabled = false;
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                handle(entity);
            }
        }

    }

    public void enable() {
        if (enabled) {
            return;
        }

        UUID uuid;
        Entity entity;
        while (!disabled.isEmpty()) {
            uuid = disabled.poll();
            entity = Bukkit.getEntity(uuid);
            if (entity == null) {
                continue;
            }
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }
            livingEntity.setAI(true);
        }
    }

    public List<EntityType> getTypes() {
        return types;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void handle(LivingEntity entity) {
        if (enabled)
            return;

        if (!types.contains(entity.getType())) {
            return;
        }

        disabled.add(entity.getUniqueId());
        entity.setAI(false);
    }

}
