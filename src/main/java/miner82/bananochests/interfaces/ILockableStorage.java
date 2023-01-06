package miner82.bananochests.interfaces;

import miner82.bananochests.classes.LockState;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface ILockableStorage {

    OfflinePlayer getCreator();
    void setCreator(Player player);
    void setOwner(Player player);
    OfflinePlayer getOwner();
    boolean unlock(Player player);
    boolean lock(Player player);
    void toggleLock(Player player);
    boolean isOwner(Player player);
    boolean isCreator(Player player);
    boolean isLocked();
    boolean isAllowed(Player player);
    LockState getLockState();
    String getDescription();
    boolean canBreak(Player player);

    boolean share(Player requester, OfflinePlayer player);
    boolean unshare(Player requester, OfflinePlayer player);
    boolean unshareAll(Player requester);
    List<OfflinePlayer> getShareList(Player requester);

    boolean autoLockOnPlace(Player player);



}
