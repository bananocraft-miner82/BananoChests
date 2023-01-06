package miner82.bananochests.events;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnStorageInteractEvent implements Listener {

    private ConfigEngine configEngine;

    public OnStorageInteractEvent(ConfigEngine configEngine) {
        this.configEngine = configEngine;
    }

    @EventHandler
    public void onStorageInteractEvent(PlayerInteractEvent event) {

        // If the chest is unlocked and the player placed the chest and is trying to lock/unlock it, toggle the lock.
        // If a player is trying to open the chest inventory, check if the player has permission to open it.

        if(event.getClickedBlock() != null) {

            Material clickedBlockType = event.getClickedBlock().getType();

            if (!StorageManager.IsStorageBlock(clickedBlockType)) {

                return;

            }

            Player player = event.getPlayer();
            ILockableStorage storageEntity = StorageManager.getLockableStorage(this.configEngine, event.getClickedBlock());

            if (storageEntity != null) {

                if(event.getItem() == null) {

                    System.out.println("No item held.");

                }

                // Lock/unlock
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                        && player.isSneaking()
                        && event.getItem() == null
                        //&& event.getItem() != null
                        //&& event.getItem().getType().equals(this.configEngine.getKeyMaterial())
                        && (storageEntity.isOwner(player)
                             || storageEntity.isCreator(player)
                             || storageEntity.getCreator() == null)) {

                    if(storageEntity.isLocked()) {

                        if(storageEntity.unlock(player)) {

                            System.out.println("[" + event.getClickedBlock().getLocation().toString() + "]" + storageEntity.getDescription() + " is now unlocked.");
                            player.sendMessage(ChatColor.GOLD + "This " + storageEntity.getDescription() + " has been unlocked.");

                        } else {

                            if(storageEntity.getOwner() != null) {

                                System.out.println("[" + event.getClickedBlock().getLocation().toString() + "]" + storageEntity.getDescription() + " cannot be unlocked. It is owned by " + storageEntity.getOwner().getName() + ".");
                                player.sendMessage(ChatColor.RED + "This " + storageEntity.getDescription() + " cannot be unlocked. It is owned by " + storageEntity.getOwner().getName() + ".");

                            }
                            else {

                                System.out.println("[" + event.getClickedBlock().getLocation().toString() + "]" + storageEntity.getDescription() + " cannot be unlocked.");
                                player.sendMessage(ChatColor.RED + "This " + storageEntity.getDescription() + " cannot be unlocked.");

                            }

                        }

                    }
                    else {

                        if(storageEntity.lock(player)) {

                            System.out.println("[" + event.getClickedBlock().getLocation().toString() + "]" + storageEntity.getDescription() + " is now locked.");
                            player.sendMessage(ChatColor.GOLD + "This " + storageEntity.getDescription() + " has been locked.");

                        } else {

                            System.out.println("[" + event.getClickedBlock().getLocation().toString() + "]" + storageEntity.getDescription() + " cannot be locked.");
                            player.sendMessage(ChatColor.RED + "This " + storageEntity.getDescription() + " cannot be locked.");

                        }

                    }

                    event.setCancelled(true);

                }

            }

        }

    }

}