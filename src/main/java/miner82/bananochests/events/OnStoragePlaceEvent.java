package miner82.bananochests.events;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnStoragePlaceEvent implements Listener {

    private ConfigEngine configEngine;

    public OnStoragePlaceEvent(ConfigEngine configEngine) {

        this.configEngine = configEngine;

    }

    @EventHandler
    public void onStoragePlace(BlockPlaceEvent event) {

        if(event.getBlockPlaced() != null
             && event.getPlayer() != null
             && StorageManager.IsStorageBlock(event.getBlockPlaced().getType())
             && event.getBlockPlaced().getState() instanceof TileState) {

            Block block = event.getBlockPlaced();

            TileState placedBlock = (TileState) event.getBlockPlaced().getState();
            Player player = event.getPlayer();

            ILockableStorage storage = StorageManager.getLockableStorage(this.configEngine, block);

            if(storage != null) {

                storage.setCreator(player);

                System.out.println("Lockable storage created... assigning owner as " + player.getName());

            }

        }

    }

}
