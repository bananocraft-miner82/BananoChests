package miner82.bananochests.events;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public class OnInventoryMoveItemEvent implements Listener {

    private final ConfigEngine configEngine;

    public OnInventoryMoveItemEvent(ConfigEngine configEngine) {
        this.configEngine = configEngine;
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {

        InventoryType destinationType = event.getDestination().getType();

        // If a hopper is trying to remove items from a locked chest... cancel the event.
        if(StorageManager.IsStorageInventory(event.getSource().getType())
             && (destinationType == InventoryType.HOPPER || destinationType == InventoryType.PLAYER)) {

            InventoryHolder holder = event.getSource().getHolder();

            if(holder instanceof TileState) {

                System.out.println("It's a tilestate");
                TileState tileState = (TileState) holder;

                ILockableStorage storage = StorageManager.getLockableStorage(this.configEngine, tileState.getBlock());

                if(storage != null
                     && storage.isLocked()) {

                    if(destinationType == InventoryType.HOPPER) {

                        event.setCancelled(true);
                        return;

                    }
                    else if(destinationType == InventoryType.PLAYER
                             && event.getDestination().getHolder() instanceof Player) {

                        Player viewer = (Player) event.getDestination().getHolder();

                        if(!storage.isAllowed(viewer)) {

                            event.setCancelled(true);
                            return;

                        }

                    }

                }

            }

        }

    }

}
