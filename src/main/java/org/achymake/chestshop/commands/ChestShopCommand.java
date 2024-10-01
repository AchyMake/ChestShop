package org.achymake.chestshop.commands;

import org.achymake.chestshop.ChestShop;
import org.achymake.chestshop.data.Message;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChestShopCommand implements CommandExecutor, TabCompleter {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public ChestShopCommand() {
        getInstance().getCommand("chestshop").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("chestshop.command.chestshop.reload")) {
                        getInstance().reload();
                        getMessage().send(player, "&6ChestShop:&f reloaded");
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getInstance().reload();
                    getMessage().send(consoleCommandSender, "ChestShop: reloaded");
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (player.hasPermission("chestshop.command.chestshop.reload")) {
                commands.add("reload");
            }
        }
        return commands;
    }
}