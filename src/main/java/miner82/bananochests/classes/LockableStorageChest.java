package miner82.bananochests.classes;

import java.util.Optional;

import miner82.bananochests.config.ConfigEngine;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.persistence.PersistentDataType;

public class LockableStorageChest extends BaseLockableStorage {

    private Chest chest;
    private Optional<Chest> slaveChest = Optional.empty();

    public LockableStorageChest(ConfigEngine configEngine, Chest chest) {

        super(configEngine, chest);

        splitDoubleChest(chest);

    }

    @Override
    public boolean lockToOwner(Player player) {

        if (isLocked()) {

            return false;

        }

        setOwner(player);

        boolean lockedToOwner = super.lockToOwner(player);

        if (lockedToOwner
              && slaveChest.isPresent()) {

            slaveChest.get().getPersistentDataContainer()
                    .set(ownerUUIDKey, PersistentDataType.STRING, player.getUniqueId().toString());
            slaveChest.get().update();

        }

        return lockedToOwner;

    }

    @Override
    public boolean lockToShare(Player player) {

        if (isLocked()) {

            return false;

        }

        String uuid = player.getUniqueId().toString();

        boolean lockedToShare = super.lockToShare(player);

        if (lockedToShare
              && slaveChest.isPresent()) {

            slaveChest.get().getPersistentDataContainer()
                    .set(ownerUUIDKey, PersistentDataType.STRING, uuid);
            slaveChest.get().update();

        }

        return lockedToShare;

    }

    @Override
    public boolean lock(Player player) {

        if (isLocked()) {

            return false;

        }

        boolean locked = super.lock(player);

        String uuid = player.getUniqueId().toString();

        super.lock(player);

        if (locked
             && slaveChest.isPresent()) {

            slaveChest.get().getPersistentDataContainer()
                    .set(ownerUUIDKey, PersistentDataType.STRING, uuid);
            slaveChest.get().update();

            slaveChest.get().getPersistentDataContainer()
                    .set(lockStateKey, PersistentDataType.STRING, lockState.name());
            slaveChest.get().update();

        }

        return locked;

    }

    @Override
    public boolean unlock(Player player) {

        if (!isLocked()) {
            return false;
        }

        if (!isOwner(player)) {
            return false;
        }

        boolean unlocked = super.unlock(player);

        if (unlocked
                && this.slaveChest.isPresent()) {

            this.slaveChest.get().getPersistentDataContainer().remove(ownerUUIDKey);
            this.slaveChest.get().update();

            this.slaveChest.get().getPersistentDataContainer().remove(lockStateKey);
            this.slaveChest.get().update();

        }

        return unlocked;

    }

    @Override
    public void setCreator(Player player) {

        if(player != null) {

            setCreator(player, this.chest);

            if (this.slaveChest.isPresent()) {

                setCreator(player, this.slaveChest.get());

            }

        }

    }

    @Override
    public void setOwner(Player player) {

        if(player != null) {

            String uuid = player.getUniqueId().toString();

            setOwner(player, this.chest);

            if (this.slaveChest.isPresent()) {

                setOwner(player, this.slaveChest.get());

            }

        }

    }

    private void splitDoubleChest(Chest chest) {

        if (!this.isDoubleChest(chest)) {

            this.chest = chest;
            return;

        }

        DoubleChest dchest = DoubleChest.class.cast(chest.getInventory().getHolder());
        Chest chestLeft = Chest.class.cast(dchest.getLeftSide());
        Chest chestRight = Chest.class.cast(dchest.getRightSide());

        if (chestLeft.getPersistentDataContainer().has(ownerUUIDKey, PersistentDataType.STRING)) {

            this.chest = chestLeft;
            this.slaveChest = Optional.of(chestRight);

        } else if (chestRight.getPersistentDataContainer().has(ownerUUIDKey, PersistentDataType.STRING)) {

            this.chest = chestRight;
            this.slaveChest = Optional.of(chestLeft);

        } else {

            this.chest = chestLeft;
            this.slaveChest = Optional.of(chestRight);

        }

    }

    private boolean isDoubleChest(Chest chest) {

        return chest.getInventory() instanceof DoubleChestInventory;

    }

}