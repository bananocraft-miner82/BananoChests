package miner82.bananochests.config;

import miner82.bananochests.BananoChestsMain;
import miner82.bananochests.classes.LockMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigEngine {

    private final String KEY_CREATORRUUID = "creatoruuid";
    private final String KEY_OWNERUUID = "owneruuid";
    private final String KEY_LOCKSTATE = "lockstate";

    private final BananoChestsMain main;
    private final NamespacedKey creatorUUIDKey;
    private final NamespacedKey ownerUUIDKey;
    private final NamespacedKey lockStateKey;

    private Material keyMaterial = Material.TRIPWIRE_HOOK;
    private LockMode lockMode = LockMode.Viewable;

    public ConfigEngine(BananoChestsMain main) {

        this.main = main;

        creatorUUIDKey = new NamespacedKey(main, KEY_CREATORRUUID);
        ownerUUIDKey = new NamespacedKey(main, KEY_OWNERUUID);
        lockStateKey = new NamespacedKey(main, KEY_LOCKSTATE);

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

    public Material getKeyMaterial() {
        return this.keyMaterial;
    }

    public void setKeyMaterial(Material material) {

        if(this.keyMaterial != material) {

            this.keyMaterial = material;

            save();

        }

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

    public boolean save() {

        FileConfiguration config = this.main.getConfig();

        //config.set("EnableBananoMiner", this.enableBananoMiner);

        config.set("KeyMaterial", this.keyMaterial.name());
        config.set("LockMode", this.lockMode.name());

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

            if(configuration.contains("KeyMaterial")) {

                try {

                    this.keyMaterial = Material.valueOf(configuration.getString("KeyMaterial"));

                }
                catch (Exception ex) {

                    System.out.println("Invalid KeyMaterial value... using default instead.");

                }

            }
            else {

                this.keyMaterial = Material.TRIPWIRE_HOOK;

            }

            System.out.println("- Key Material set: " + this.keyMaterial.name());

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


        }

        System.out.println("BananoChests Config initialisation complete!");

    }
}
