package me.philip.wave;

import org.bukkit.event.Listener;

import java.util.UUID;

public class playerData implements Listener {

    private boolean isHealing;
    private UUID uuid;

    public playerData(UUID uuid, boolean isHealing){
        this.setUuid(uuid);
        this.setHealing(isHealing);
    }

    public boolean isHealing() {
        return isHealing;
    }

    public void setHealing(boolean healing) {
        isHealing = healing;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
