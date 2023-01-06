package miner82.bananochests.commands.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ChestInteractCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> results = new ArrayList<>();

        if(args.length == 1) {

            results.add("lock");
            results.add("unlock");
            results.add("share");
            results.add("unshare");
            results.add("status");
            results.add("setautolock");

        }
        else if(args.length >= 2
                  && args[0].equalsIgnoreCase("share")
                      || args[0].equalsIgnoreCase("unshare")) {

            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                results.add(player.getName());
            }

            if(args[0].equalsIgnoreCase("unshare")) {
                results.add("@all");
            }

        }
        else if(args.length >= 2
                 && args[0].equalsIgnoreCase("setautolock")) {

            if(args.length == 2) {

                results.add("barrel");
                results.add("chest");
                results.add("shulkerbox");

            }
            else if(args.length == 3) {

                results.add("status");
                results.add("autolock");
                results.add("none");

            }

        }

        return StringUtil.copyPartialMatches(args[args.length - 1], results, new ArrayList<>());

    }
}
