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

    private final List<OfflinePlayer> allowedPlayers = new ArrayList<>();
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

    }

    @Override
    public String getDescription() {

        return this.tileStateBlock.getType().toString();

    }

    protected UUID retrieveCreator(TileState tileStateBlock) {

        if (tileStateBlock.getPersistentDataContainer().has(this.creatorUUIDKey, PersistentDataType.STRING)) {

            try {

                UUID creatorUUID = UUID.fromString(tileStateBlock.getPersistentDataContainer().get(this.creatorUUIDKey, PersistentDataType.STRING));

                if(creatorUUID != null) {

                    System.out.println("Creator identified: " + creatorUUID);

                }
                else {

                    System.out.println("Creator not identifiable.");

                }

                return creatorUUID;

            }
            catch (Exception ex) {
                // Do nothing
            }

        }
        else {

            System.out.println("Creator not present in persistent data container.");

        }

        return null;

    }

    protected UUID retrieveOwner(TileState tileStateBlock) {

        if (tileStateBlock.getPersistentDataContainer().has(this.ownerUUIDKey, PersistentDataType.STRING)) {

            try {

                UUID ownerUUID = UUID.fromString(tileStateBlock.getPersistentDataContainer().get(this.ownerUUIDKey, PersistentDataType.STRING));

                if(ownerUUID != null) {

                    System.out.println("Owner identified: " + ownerUUID);

                }
                else {

                    System.out.println("Owner not identifiable.");

                }

                return ownerUUID;

            }
            catch (Exception ex) {
                // Do nothing
            }

        }
        else {

            System.out.println("Owner not present in persistent data container.");

        }

        return null;

    }

    protected LockState retrieveLockState(TileState tileStateBlock) {

        if(tileStateBlock.getPersistentDataContainer().has(this.lockStateKey, PersistentDataType.STRING)) {

            String storedLockState = tileStateBlock.getPersistentDataContainer().get(this.lockStateKey, PersistentDataType.STRING);

            try {

                LockState lockState = LockState.valueOf(storedLockState);

                System.out.println("Lock State retrieved as " + lockState.name());

                this.lockState = lockState;

            }
            catch (Exception ex) {

                System.out.println("Lock State could not be retrieved, or is invalid");
                System.out.println("Stored value: " + storedLockState);

                ex.printStackTrace();

                this.lockState = LockState.Unlocked;

            }

        }
        else {

            System.out.println("Lock State not present in persistent data container.");
            this.lockState = LockState.Unlocked;

        }

        return this.lockState;

    }

    @Override
    public OfflinePlayer getCreator() {

        if(this.creator != null) {

            return Bukkit.getOfflinePlayer(this.creator);

        }

        return null;

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

        if(this.owner != null) {

            return Bukkit.getOfflinePlayer(this.owner);

        }

        return null;

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

        return this.lockState != LockState.Unlocked;

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

        if(player == null
            || isLocked()
            || (hasCreator()
                 && !isCreator(player))) {

            return false;

        }

        String uuid = player.getUniqueId().toString();

        this.lockState = LockState.Locked;

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

        if(player == null
                || !isLocked()
                || !isOwner(player)) {

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

        return (this.creator == null
                    || this.owner == null
                    || isOwner(player)
                    || (player.isOp() && player.isSneaking()));

    }

    @Override
    public boolean share(Player requester, OfflinePlayer player) {

        if(!isOwner(requester)
            || allowedPlayers.stream().anyMatch(e -> e.getUniqueId().equals(player.getUniqueId()))) {

            return false;

        }

        this.allowedPlayers.add(player);

        return saveAllowedPlayers();

    }

    @Override
    public boolean unshare(Player requester, OfflinePlayer player) {

        if(!isOwner(requester)
            || allowedPlayers.stream().noneMatch(e -> e.getUniqueId().equals(player.getUniqueId()))) {

            return false;

        }

        this.allowedPlayers.remove(player);

        return saveAllowedPlayers();

    }

    @Override
    public boolean unshareAll(Player requester) {

        if(!isOwner(requester)) {

            return false;

        }

        this.allowedPlayers.clear();

        return saveAllowedPlayers();

    }

    private boolean saveAllowedPlayers() {

        ///// TODO

        return false;

    }

    @Override
    public List<OfflinePlayer> getShareList(Player requester) {

        if(this.allowedPlayers != null) {

            return this.allowedPlayers;

        }

        return new ArrayList<>();

    }

    @Override
    public boolean autoLockOnPlace(Player player)
    {
        return false;
    }

}
