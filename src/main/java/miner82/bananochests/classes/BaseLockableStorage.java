package miner82.bananochests.classes;

import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseLockableStorage implements ILockableStorage {

    protected ConfigEngine configEngine;

    protected final NamespacedKey creatorUUIDKey;
    protected final NamespacedKey ownerUUIDKey;
    protected final NamespacedKey lockStateKey;

    protected UUID creator = null;
    protected UUID owner = null;
    protected LockState lockState = LockState.Unlocked;

    private final List<Player> allowedPlayers = new ArrayList<>();
    protected TileState tileStateBlock;

    public BaseLockableStorage(ConfigEngine configEngine, TileState tileStateBlock) {

        this.configEngine = configEngine;

        this.tileStateBlock = tileStateBlock;

        this.creatorUUIDKey = this.configEngine.getCreatorUUIDKey();
        this.ownerUUIDKey = this.configEngine.getOwnerUUIDKey();
        this.lockStateKey = this.configEngine.getLockStateKey();

        creator = retrieveCreator(tileStateBlock);
        owner = retrieveOwner(tileStateBlock);
        lockState = retrieveLockState(tileStateBlock);

        if(lockState == LockState.LockedToShare) {

            //allowedPlayers = chestSigns.stream()
            //        .flatMap(s -> getPlayersFromSign(s).stream())
            //        .collect(Collectors.toList());

        }

    }

    @Override
    public String getDescription() {

        return this.tileStateBlock.getType().toString();

    }

    protected UUID retrieveCreator(TileState tileStateBlock) {

        if (tileStateBlock.getPersistentDataContainer().has(this.creatorUUIDKey, PersistentDataType.STRING)) {

            try {

                return UUID.fromString(tileStateBlock.getPersistentDataContainer().get(this.creatorUUIDKey, PersistentDataType.STRING));

            }
            catch (Exception ex) {
                // Do nothing
            }

        }

        return null;

    }

    protected UUID retrieveOwner(TileState tileStateBlock) {

        if (tileStateBlock.getPersistentDataContainer().has(this.ownerUUIDKey, PersistentDataType.STRING)) {

            try {

                return UUID.fromString(tileStateBlock.getPersistentDataContainer().get(this.ownerUUIDKey, PersistentDataType.STRING));

            }
            catch (Exception ex) {
                // Do nothing
            }

        }

        return null;

    }

    protected LockState retrieveLockState(TileState tileStateBlock) {

        if(tileStateBlock.getPersistentDataContainer().has(this.lockStateKey, PersistentDataType.STRING)) {

            return LockState.valueOf(tileStateBlock.getPersistentDataContainer().get(this.lockStateKey, PersistentDataType.STRING));

        }

        return LockState.Unlocked;

    }

    @Override
    public OfflinePlayer getCreator() {

        return Bukkit.getOfflinePlayer(this.creator);

    }

    @Override
    public void setCreator(Player player) {

        setCreator(player, this.tileStateBlock);

    }

    protected void setCreator(Player player, TileState tileStateBlock) {

        String uuid = player.getUniqueId().toString();

        tileStateBlock.getPersistentDataContainer().set(this.creatorUUIDKey, PersistentDataType.STRING, uuid);
        tileStateBlock.update();

    }

    @Override
    public void setOwner(Player player) {
        setOwner(player, this.tileStateBlock);
    }

    @Override
    public OfflinePlayer getOwner() {

        return Bukkit.getPlayer(this.owner);

    }

    @Override
    public boolean lockToOwner(Player player) {
        ////TODO
        return this.lockState == LockState.LockedToOwner;
    }

    @Override
    public boolean lockToShare(Player player) {
        ////TODO
        return this.lockState == LockState.LockedToShare;
    }

    public boolean isOwner(Player player) {

        if(owner != null
             && player != null) {

            return player.getUniqueId().equals(this.owner);

        }

        return false;
    }

    @Override
    public boolean isCreator(Player player) {
        return this.creator != null
                && player != null
                && this.creator.equals(player.getUniqueId());
    }

    protected void setOwner(Player player, TileState tileStateBlock) {

        String uuid = player.getUniqueId().toString();

        tileStateBlock.getPersistentDataContainer().set(this.configEngine.getCreatorUUIDKey(), PersistentDataType.STRING, uuid);
        tileStateBlock.update();

    }

    @Override
    public boolean isLocked() {

        return lockState != LockState.Unlocked;

    }

    @Override
    public boolean isAllowed(Player player) {

        return !this.isLocked()
                 || isOwner(player)
                 || allowedPlayers.stream().anyMatch(e -> e.getUniqueId().equals(player.getUniqueId()));

    }

    @Override
    public LockState getLockState() {
        return this.lockState;
    }

    @Override
    public boolean lock(Player player) {

        if (isLocked()) {

            return false;

        }

        if(hasCreator()
             && !isCreator(player)) {

            return false;

        }

        String uuid = player.getUniqueId().toString();

        this.lockState = LockState.LockedToOwner;

        tileStateBlock.getPersistentDataContainer().set(ownerUUIDKey, PersistentDataType.STRING, uuid);
        tileStateBlock.update();

        tileStateBlock.getPersistentDataContainer().set(lockStateKey, PersistentDataType.STRING, this.lockState.name());
        tileStateBlock.update();

        return true;

    }

    protected boolean hasCreator() {
        return this.creator != null;
    }

    @Override
    public void toggleLock(Player player) {

        if(this.isLocked()) {

            unlock(player);

        }
        else {

            lock(player);

        }

    }

    @Override
    public boolean unlock(Player player) {

        if (!isLocked()) {
            return false;
        }

        if (!isOwner(player)) {
            return false;
        }

        this.lockState = LockState.Unlocked;

        tileStateBlock.getPersistentDataContainer().remove(this.ownerUUIDKey);
        tileStateBlock.update();

        tileStateBlock.getPersistentDataContainer().remove(lockStateKey);
        tileStateBlock.update();

        return true;

    }

    @Override
    public boolean canBreak(Player player) {

        // The player can break the storage if:
        // - They are the owner of the storage
        // - They are an OP (holding shift to avoid accidents)
        // - The storage is not locked

        return !isLocked()
                && (this.creator == null
                    || this.owner == null
                    || isOwner(player)
                    || (player.isOp() && player.isSneaking()));

    }

}
