package miner82.bananochests.events;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class OnStorageExplodeEvent implements Listener {

    private final ConfigEngine configEngine;

    public OnStorageExplodeEvent(ConfigEngine configEngine) {
        this.configEngine = configEngine;
    }

    @EventHandler
    public void onStorageExplode(EntityExplodeEvent event) {

        List<Block> blockList = new ArrayList<>(event.blockList());

        for (Block block : blockList) {

            if (StorageManager.IsStorageBlock(block.getType())) {

                ILockableStorage storage = StorageManager.getLockableStorage(this.configEngine, block);

                if (storage != null
                      && storage.isLocked()) {

                    event.blockList().remove(block);

                    System.out.println("Entity [" + event.getEntity().getName() + "] tried to explode "
                            + block.getType() + " at " + block.getX()
                            + " / " + block.getY() + " / " + block.getZ() + ".");

                }

            }

        }

    }

}
