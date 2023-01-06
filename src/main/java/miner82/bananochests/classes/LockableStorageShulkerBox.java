package miner82.bananochests.classes;

import miner82.bananochests.config.ConfigEngine;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class LockableStorageShulkerBox extends BaseLockableStorage {

    public LockableStorageShulkerBox(ConfigEngine configEngine, ShulkerBox shulkerBox) {

        super(configEngine, shulkerBox);

    }

    @Override
    public boolean autoLockOnPlace(Player player)
    {
        if(player.getPersistentDataContainer().has(this.configEngine.getPlayerALShulkerKey(), PersistentDataType.SHORT)) {

            return player.getPersistentDataContainer().get(this.configEngine.getPlayerALShulkerKey(), PersistentDataType.SHORT).equals(1);

        }
        else {

            return super.configEngine.getAutoLockShulker();

        }
    }

    @Override
    public String getDescription() {

        return "Shulker Box";

    }

}
