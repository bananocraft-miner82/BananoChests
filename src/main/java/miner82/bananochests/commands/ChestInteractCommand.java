package miner82.bananochests.commands;

import miner82.bananochests.classes.StorageManager;
import miner82.bananochests.config.ConfigEngine;
import miner82.bananochests.interfaces.ILockableStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Optional;

public class ChestInteractCommand extends BaseCommand implements CommandExecutor {

    private final ConfigEngine configEngine;

    public ChestInteractCommand(ConfigEngine configEngine) {

        this.configEngine = configEngine;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {

            System.out.println("Command can only be used by a player, not from the console!");
            return false;

        }

        Player player = (Player) sender;

        if(args.length == 0) {

            return false;

        }

        if(args[0].equalsIgnoreCase("setautolock")) {

            if(args.length != 3) {

                SendMessage(player, "This request requires three arguments: [barrel/chest/shulkerbox] followed by [status/autolock/none]", ChatColor.RED);

                return true;

            }

            String type = args[1];
            String action = args[2];

            if(action.equalsIgnoreCase("none")) {

                if(type.equalsIgnoreCase("barrel")) {

                    player.getPersistentDataContainer().remove(this.configEngine.getPlayerALBarrelKey());

                    SendMessage(player, "You are now configured to NOT autolock barrel storage types when placing them.", ChatColor.GOLD);

                }
                else if(type.equalsIgnoreCase("chest")) {

                    player.getPersistentDataContainer().remove(this.configEngine.getPlayerALChestKey());

                    SendMessage(player, "You are now configured to NOT autolock chest storage types when placing them.", ChatColor.GOLD);

                }
                else if(type.equalsIgnoreCase("shulkerbox")) {

                    player.getPersistentDataContainer().remove(this.configEngine.getPlayerALChestKey());

                    SendMessage(player, "You are now configured to NOT autolock shulker box storage types when placing them.", ChatColor.GOLD);

                }
                else {

                    SendMessage(player, "The storage type could not be identified! It must be one of [barrel/chest/shulkerbox].", ChatColor.RED);

                }

            }
            else if(action.equalsIgnoreCase("autolock")) {

                if(type.equalsIgnoreCase("barrel")) {

                    player.getPersistentDataContainer().set(this.configEngine.getPlayerALBarrelKey(), PersistentDataType.SHORT, (short)1);

                    SendMessage(player, "You are now configured to autolock barrel storage types when placing them.", ChatColor.GOLD);

                }
                else if(type.equalsIgnoreCase("chest")) {

                    player.getPersistentDataContainer().set(this.configEngine.getPlayerALChestKey(), PersistentDataType.SHORT, (short)1);

                    SendMessage(player, "You are now configured to autolock chest storage types when placing them.", ChatColor.GOLD);

                }
                else if(type.equalsIgnoreCase("shulkerbox")) {

                    player.getPersistentDataContainer().set(this.configEngine.getPlayerALChestKey(), PersistentDataType.SHORT, (short)1);

                    SendMessage(player, "You are now configured to autolock shulker box storage types when placing them.", ChatColor.GOLD);

                }
                else {

                    SendMessage(player, "The storage type could not be identified! It must be one of [barrel/chest/shulkerbox].", ChatColor.RED);

                }

            }
            else if(action.equalsIgnoreCase("status")) {

                if(type.equalsIgnoreCase("barrel")) {

                    if(this.configEngine.hasPlayerALBarrel(player)) {

                        SendMessage(player, "You are configured to autolock barrel storage types when placing them.", ChatColor.GOLD);

                    }
                    else {

                        SendMessage(player, "You are configured to NOT autolock barrel storage types when placing them.", ChatColor.GOLD);

                    }

                }
                else if(type.equalsIgnoreCase("chest")) {

                    if(this.configEngine.hasPlayerALChest(player)) {

                        SendMessage(player, "You are configured to autolock chest storage types when placing them.", ChatColor.GOLD);

                    }
                    else {

                        SendMessage(player, "You are configured to NOT autolock chest storage types when placing them.", ChatColor.GOLD);

                    }

                }
                else if(type.equalsIgnoreCase("shulkerbox")) {

                    if(this.configEngine.hasPlayerALShulker(player)) {

                        SendMessage(player, "You are configured to autolock shulker box storage types when placing them.", ChatColor.GOLD);

                    }
                    else {

                        SendMessage(player, "You are configured to NOT autolock shulker box storage types when placing them.", ChatColor.GOLD);

                    }

                }
                else {

                    SendMessage(player, "The storage type could not be identified! It must be one of [barrel/chest/shulkerbox].", ChatColor.RED);

                }

            }
            else {

                SendMessage(player, "This request requires three arguments: [barrel/chest/shulkerbox] followed by [status/autolock/none]", ChatColor.RED);

            }

            return true;

        }

        Block targetBlock = player.getTargetBlockExact(16, FluidCollisionMode.NEVER);

        if(targetBlock == null) {

            SendMessage(player, "You must have a block targeted within 16 blocks to use this command.", ChatColor.RED);
            return true;

        }
        else if(!StorageManager.IsStorageBlock(targetBlock.getType())) {

            SendMessage(player, "You must have a storage block targeted within 16 blocks to use this command.", ChatColor.RED);
            return true;

        }

        ILockableStorage storageBlock = StorageManager.getLockableStorage(this.configEngine, targetBlock);

        if(storageBlock != null) {

            if(args[0].equalsIgnoreCase("lock")) {

                if(storageBlock.getCreator() != null
                    && !storageBlock.isCreator(player)) {

                    SendMessage(player, "This is not your " + storageBlock.getDescription(), ChatColor.RED);

                }
                else if(storageBlock.isLocked()) {

                    SendMessage(player, "This " + storageBlock.getDescription() + " is already locked.", ChatColor.RED);

                }
                else if(storageBlock.lock(player)) {

                    SendMessage(player, "This " + storageBlock.getDescription() + " has been locked", ChatColor.GOLD);

                }

            }
            else if(args[0].equalsIgnoreCase("unlock")) {

                if(!storageBlock.isOwner(player)) {

                    SendMessage(player, "This is not your " + storageBlock.getDescription(), ChatColor.RED);

                }
                else if(!storageBlock.isLocked()) {

                    SendMessage(player, "This " + storageBlock.getDescription() + " is not locked.", ChatColor.RED);

                }
                else if(storageBlock.unlock(player)) {

                    SendMessage(player, "This " + storageBlock.getDescription() + " has been unlocked", ChatColor.GOLD);

                }

            }
            else if(args[0].equalsIgnoreCase("status")) {

                String creator = "[unknown]";
                String owner = "[unknown]";

                if(storageBlock.getCreator() != null) {
                    creator = storageBlock.getCreator().getName();
                }

                if(storageBlock.getOwner() != null) {
                    owner = storageBlock.getOwner().getName();
                }

                SendMessage(player, "This storage block was placed by " + creator + " and is owned by " + owner + ". The " + storageBlock.getDescription() + (storageBlock.isLocked() ? " is locked." : " is not locked."), ChatColor.AQUA);

            }
            else if(args[0].equalsIgnoreCase("share")) {

                if(args.length >= 2) {

                    boolean success = true;
                    String successList = "";

                    for(int index = 1; index < args.length; index++) {

                        if(args[index] != null
                             && args[index].length() > 0) {

                            final String playerName = args[index];

                            Optional<OfflinePlayer> sharePlayer = Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> x.getName().equalsIgnoreCase(playerName)).findFirst();

                            if (sharePlayer.isPresent()) {

                                success &= storageBlock.share(player, sharePlayer.get());

                                if(successList.length() > 0) {

                                    successList += ", ";

                                }

                                successList += sharePlayer.get().getName();

                            } else {

                                SendMessage(player, "This storage could not be shared with '" + args[index] + "'! That player could not be identified.", ChatColor.RED);

                            }

                        }

                    }

                    if(success) {

                        SendMessage(player, "This storage was successfully shared with: " + successList, ChatColor.GREEN);

                    }
                    else {

                        SendMessage(player, "This storage could not be shared with any of the provided names. Please check they are entered correctly!", ChatColor.RED);

                    }

                }
                else {

                    SendMessage(player, "You must specify one or more player names to share this storage with.", ChatColor.RED);

                }

            }
            else if(args[0].equalsIgnoreCase("unshare")) {

                if(args.length >= 2) {

                    boolean success = true;
                    String successList = "";

                    for(int index = 1; index < args.length; index++) {

                        if(args[index] != null
                                && args[index].length() > 0) {

                            final String playerName = args[index];

                            Optional<OfflinePlayer> sharePlayer = Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> x.getName().equalsIgnoreCase(playerName)).findFirst();

                            if (sharePlayer.isPresent()) {

                                success &= storageBlock.unshare(player, sharePlayer.get());

                                if(successList.length() > 0) {

                                    successList += ", ";

                                }

                                successList += sharePlayer.get().getName();

                            } else {

                                SendMessage(player, "This storage could not be unshared with '" + args[index] + "'! That player could not be identified.", ChatColor.RED);

                            }

                        }

                    }

                    if(success) {

                        SendMessage(player, "This storage was successfully unshared with: " + successList, ChatColor.GREEN);

                    }
                    else {

                        SendMessage(player, "This storage could not be unshared with any of the provided names. Please check they are entered correctly!", ChatColor.RED);

                    }

                }
                else if(args.length == 2
                         && args[1].equalsIgnoreCase("@all")) {

                    if(storageBlock.unshareAll(player)) {

                        SendMessage(player, "This storage is now completely unshared.", ChatColor.GREEN);

                    }
                    else {

                        SendMessage(player, "This storage could not be unshared!", ChatColor.RED);

                    }

                }

            }


            return true;

        }
        else {

            SendMessage(player, "The targeted block does not appear to be a storage block.", ChatColor.RED);

            return true;

        }
    }

}
