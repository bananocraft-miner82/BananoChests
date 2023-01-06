package miner82.bananochests;

import miner82.bananochests.commands.*;
import miner82.bananochests.commands.tabcompleters.ChestInteractCommandTabCompleter;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BananoChestsMain extends JavaPlugin implements Listener {

    private ConfigEngine configEngine;

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        
        this.configEngine = new ConfigEngine(this);

        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new OnStoragePlaceEvent(this.configEngine), this);
        Bukkit.getPluginManager().registerEvents(new OnStorageBreakEvent(this.configEngine), this);
        Bukkit.getPluginManager().registerEvents(new OnStorageExplodeEvent(this.configEngine), this);
        Bukkit.getPluginManager().registerEvents(new OnStorageInteractEvent(this.configEngine), this);
        Bukkit.getPluginManager().registerEvents(new OnInventoryOpenEvent(this.configEngine), this);
        Bukkit.getPluginManager().registerEvents(new OnInventoryMoveItemEvent(this.configEngine), this);

        getCommand("reloadbananochestsconfig").setExecutor(new ReloadConfigurationCommand(this.configEngine));
        getCommand("securechest").setExecutor(new ChestInteractCommand(this.configEngine));

        getCommand("securechest").setTabCompleter(new ChestInteractCommandTabCompleter());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //Bukkit.getServer().getScheduler().cancelTasks(this);
    }

}
