package miner82.bananochests.classes;

import miner82.bananochests.config.ConfigEngine;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class LockableStorageBarrel extends BaseLockableStorage {

    public LockableStorageBarrel(ConfigEngine configEngine, Barrel barrel) {

        super(configEngine, barrel);

    }

    @Override
    public boolean autoLockOnPlace(Player player)
    {

        if(player.getPersistentDataContainer().has(this.configEngine.getPlayerALBarrelKey(), PersistentDataType.SHORT)) {

            return player.getPersistentDataContainer().get(this.configEngine.getPlayerALBarrelKey(), PersistentDataType.SHORT).equals(1);

        }
        else {

            return super.configEngine.getAutoLockBarrel();

        }

    }

    @Override
    public String getDescription() {

        return "Barrel";

    }

}