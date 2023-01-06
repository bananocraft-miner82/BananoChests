package miner82.bananochests.events;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnStorageBreakEvent implements Listener {

    private final ConfigEngine configEngine;

    public OnStorageBreakEvent(ConfigEngine configEngine) {
        this.configEngine = configEngine;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (StorageManager.IsStorageBlock(block.getType())) {

            ILockableStorage storage = StorageManager.getLockableStorage(this.configEngine, block);

            if(storage != null) {

                if(!storage.canBreak(player)) {

                    event.setCancelled(true);

                    player.sendMessage(ChatColor.RED + "You cannot break this because it does not belong to you!");
                    System.out.println("Player [" + player.getName() + " " + player.getUniqueId() + "] tried to break "
                                         + block.getType() + " at " + block.getX()
                                         + " / " + block.getY() + " / " + block.getZ() + ".");

                    return;

                }

            }

        }

    }

}
