package miner82.bananochests.classes;

import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.InventoryType;

public class StorageManager {

    public static boolean IsStorageBlock(Material material) {

        return material == Material.CHEST
                || material == Material.BARREL
                || IsShulkerBox(material);

    }

    public static boolean IsStorageInventory(InventoryType inventoryType) {

        return inventoryType == InventoryType.BARREL
                || inventoryType == InventoryType.CHEST
                || inventoryType == InventoryType.SHULKER_BOX;

    }

    public static boolean IsShulkerBox(Material material) {

        switch (material) {

            case SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:

                return true;

            default:

                return false;

        }

    }

    public static ILockableStorage getLockableStorage(ConfigEngine configEngine, Block fromBlock) {

        if(fromBlock != null
             && IsStorageBlock(fromBlock.getType())) {

            ILockableStorage storage = null;

            if(fromBlock.getType() == Material.CHEST) {

                Chest chest = (Chest) fromBlock.getState();
                storage = new LockableStorageChest(configEngine, chest);

            }
            else if(fromBlock.getType() == Material.BARREL) {

                Barrel barrel = (Barrel) fromBlock.getState();
                storage = new LockableStorageBarrel(configEngine, barrel);

            }
            else if(StorageManager.IsShulkerBox(fromBlock.getType())) {

                ShulkerBox shulkerBox = (ShulkerBox) fromBlock.getState();
                storage = new LockableStorageShulkerBox(configEngine, shulkerBox);

            }

            return storage;

        }

        return null;

    }

}
