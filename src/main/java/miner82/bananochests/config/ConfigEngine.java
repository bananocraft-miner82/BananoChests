package miner82.bananochests.config;

import miner82.bananochests.BananoChestsMain;
import miner82.bananochests.classes.LockMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ConfigEngine {

    private final String KEY_CREATORRUUID = "creatoruuid";
    private final String KEY_OWNERUUID = "owneruuid";
    private final String KEY_LOCKSTATE = "lockstate";

    private final String KEY_PLAYER_AL_BARREL = "autolockbarrel";
    private final String KEY_PLAYER_AL_CHEST = "autolockchest";
    private final String KEY_PLAYER_AL_SHULKER = "autolockshulker";

    private final BananoChestsMain main;
    private final NamespacedKey creatorUUIDKey;
    private final NamespacedKey ownerUUIDKey;
    private final NamespacedKey lockStateKey;

    private final NamespacedKey playerALBarrelKey;
    private final NamespacedKey playerALChestKey;
    private final NamespacedKey playerALShulkerKey;

    private LockMode lockMode = LockMode.Viewable;

    private boolean autoLockShulker = false;
    private boolean autoLockChest = false;
    private boolean autoLockBarrel = false;

    public ConfigEngine(BananoChestsMain main) {

        this.main = main;

        creatorUUIDKey = new NamespacedKey(main, KEY_CREATORRUUID);
        ownerUUIDKey = new NamespacedKey(main, KEY_OWNERUUID);
        lockStateKey = new NamespacedKey(main, KEY_LOCKSTATE);

        playerALBarrelKey = new NamespacedKey(main, KEY_PLAYER_AL_BARREL);
        playerALChestKey = new NamespacedKey(main, KEY_PLAYER_AL_CHEST);
        playerALShulkerKey = new NamespacedKey(main, KEY_PLAYER_AL_SHULKER);

        initialiseConfig(main.getConfig());

    }

    public NamespacedKey getCreatorUUIDKey() {
        return this.creatorUUIDKey;
    }
    public NamespacedKey getOwnerUUIDKey() {
        return this.ownerUUIDKey;
    }

    public NamespacedKey getLockStateKey() {
        return this.lockStateKey;
    }

    public NamespacedKey getPlayerALBarrelKey() { return this.playerALBarrelKey; }
    public NamespacedKey getPlayerALChestKey() { return this.playerALChestKey; }
    public NamespacedKey getPlayerALShulkerKey() { return this.playerALShulkerKey; }

    public boolean hasPlayerALBarrel(Player player) {

        if(player != null) {

            return player.getPersistentDataContainer().getOrDefault(this.playerALBarrelKey, PersistentDataType.SHORT, (short)0).equals((short)1);

        }

        return false;

    }

    public boolean hasPlayerALChest(Player player) {

        if(player != null) {

            return player.getPersistentDataContainer().getOrDefault(this.playerALChestKey, PersistentDataType.SHORT, (short)0).equals((short)1);

        }

        return false;

    }
    public boolean hasPlayerALShulker(Player player) {

        if(player != null) {

            return player.getPersistentDataContainer().getOrDefault(this.playerALShulkerKey, PersistentDataType.SHORT, (short)0).equals((short)1);

        }

        return false;

    }

    public LockMode getLockMode() {
        return this.lockMode;
    }

    public void setLockMode(LockMode newValue) {

        if(this.lockMode != newValue) {

            this.lockMode = newValue;

            save();

        }

    }

    public boolean getAutoLockBarrel() {
        return this.autoLockBarrel;
    }

    public void setAutoLockBarrel(boolean value) {

        if(this.autoLockBarrel != value) {

            this.autoLockBarrel = value;

            save();

        }

    }

    public boolean getAutoLockChest() {
        return this.autoLockChest;
    }

    public void setAutoLockChest(boolean value) {

        if(this.autoLockChest != value) {

            this.autoLockChest = value;

            save();

        }

    }

    public boolean getAutoLockShulker() {
        return this.autoLockShulker;
    }

    public void setAutoLockShulker(boolean value) {

        if(this.autoLockShulker != value) {

            this.autoLockShulker = value;

            save();

        }

    }

    public boolean save() {

        FileConfiguration config = this.main.getConfig();

        config.set("LockMode", this.lockMode.name());

        config.set("AutoLockShulker", this.autoLockShulker);
        config.set("AutoLockChest", this.autoLockChest);
        config.set("AutoLockBarrel", this.autoLockBarrel);

        this.main.saveConfig();

        return true;

    }

    public void reload() {
        main.reloadConfig();
        initialiseConfig(main.getConfig());
    }

    private void initialiseConfig(FileConfiguration configuration) {

        System.out.println("Initialising BananoChests Config...");

        if(configuration != null) {

            if(configuration.contains("LockMode")) {

                try {

                    this.lockMode = LockMode.valueOf(configuration.getString("LockMode"));

                }
                catch (Exception ex) {

                    System.out.println("Invalid LockMode value... using default instead.");

                }

            }
            else {

                this.lockMode = LockMode.Viewable;

            }

            System.out.println("- Locking mode set: " + this.lockMode.name());

            if(configuration.contains("AutoLockBarrel")) {

                try {

                    this.autoLockBarrel = configuration.getBoolean("AutoLockBarrel");

                }
                catch (Exception ex) {

                    System.out.println("Invalid AutoLockBarrel value... using default instead.");

                }

            }
            else {

                this.autoLockBarrel = false;

            }

            System.out.println("- Auto-Lock Barrel mode set: " + this.autoLockBarrel);

            if(configuration.contains("AutoLockChest")) {

                try {

                    this.autoLockChest = configuration.getBoolean("AutoLockChest");

                }
                catch (Exception ex) {

                    System.out.println("Invalid AutoLockChest value... using default instead.");

                }

            }
            else {

                this.autoLockChest = false;

            }

            System.out.println("- Auto-Lock Chest mode set: " + this.autoLockChest);

            if(configuration.contains("AutoLockShulker")) {

                try {

                    this.autoLockShulker = configuration.getBoolean("AutoLockShulker");

                }
                catch (Exception ex) {

                    System.out.println("Invalid AutoLockShulker value... using default instead.");

                }

            }
            else {

                this.autoLockShulker = false;

            }

            System.out.println("- Auto-Lock Shulker mode set: " + this.autoLockShulker);

        }

        System.out.println("BananoChests Config initialisation complete!");

    }
}
