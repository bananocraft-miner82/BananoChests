package miner82.bananochests.events;

import miner82.bananochests.classes.LockMode;
import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class OnInventoryOpenEvent implements Listener {

    private final ConfigEngine configEngine;

    public OnInventoryOpenEvent(ConfigEngine configEngine) {
        this.configEngine = configEngine;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {

        if(event.getPlayer() instanceof Player) {

            Player opener = (Player)event.getPlayer();

            if(this.configEngine.getLockMode() == LockMode.Sealed
                 && StorageManager.IsStorageInventory(event.getInventory().getType()))
            {

                Block block = event.getInventory().getLocation().getBlock();
                ILockableStorage storage = StorageManager.getLockableStorage(this.configEngine, block);

                if(storage != null) {

                    if(!storage.isAllowed(opener)
                        && !(opener.isOp() && opener.isSneaking())) {

                        event.setCancelled(true);

                        opener.sendMessage(ChatColor.RED + "You cannot open this because it has been locked by " + storage.getOwner().getName());
                        System.out.println("Player [" + opener.getName() + " " + opener.getUniqueId() + "] tried to open "
                                + block.getType() + " at " + block.getX()
                                + " / " + block.getY() + " / " + block.getZ() + ".");

                        return;

                    }

                }

            }

        }

    }

}
