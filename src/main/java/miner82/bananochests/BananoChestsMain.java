package miner82.bananochests;

import miner82.bananochests.commands.*;
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

        //getCommand("enablebananominer").setExecutor(new EnableBananoMinerCommand(this.configEngine));
        //getCommand("removeprizeblock").setExecutor(new RemovePrizeBlockCommand(this.configEngine));
        //getCommand("setprizeblock").setExecutor(new SetPrizeBlockCommand(this.configEngine));
        //getCommand("setrandomprize").setExecutor(new SetRandomPrizeCommand(this.configEngine));
        //getCommand("setbaseprize").setExecutor(new SetBasePrizeCommand(this.configEngine));
        //getCommand("setdailyearningcap").setExecutor(new SetMaximumDailyEarnCommand(this.configEngine));
        //getCommand("setbananominerdebug").setExecutor(new SetBananoMinerDebugCommand(this.configEngine));
        //getCommand("banhammer").setExecutor(new BanHammerCommand(this.configEngine));
        //getCommand("printbmconfig").setExecutor(new PrintConfigInfoCommand(this.configEngine));
        //getCommand("showminingstats").setExecutor(new DisplayPlayerMiningStatsCommand(this.configEngine));

        //getCommand("enablebananominer").setTabCompleter(new EnableBananoMinerTabCompleter(this.configEngine));
        //getCommand("removeprizeblock").setTabCompleter(new RemovePrizeBlockTabCompleter(this.configEngine));
        //getCommand("setprizeblock").setTabCompleter(new SetPrizeBlockTabCompleter(this.configEngine));
        //getCommand("setbananominerdebug").setTabCompleter(new SetBananoMinerDebugTabCompleter(this.configEngine));
        //getCommand("banhammer").setTabCompleter(new SetBananoMinerDebugTabCompleter(this.configEngine));
        //getCommand("printbmconfig").setTabCompleter(new PrintConfigInfoTabCompleter(this.configEngine));
        //getCommand("showminingstats").setTabCompleter(new DisplayPlayerMiningStatsTabCompleter(this.configEngine));

        // Delayed to ensure vault is initialised by the time we try to call it
        //Bukkit.getScheduler().runTaskLater(this, this::setupBananoMiner, 5);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //Bukkit.getServer().getScheduler().cancelTasks(this);
    }

}
