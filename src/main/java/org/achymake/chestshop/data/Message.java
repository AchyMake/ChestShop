package org.achymake.chestshop.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Message {
    public void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(message);
    }
    public String capitalize(String string) {
        if (string.contains(" ")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split(" ")) {
                stringBuilder.append(strings.charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else if (string.contains("_")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split("_")) {
                stringBuilder.append(strings.charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else return string.charAt(0) + string.substring(1).toLowerCase();
    }
    public boolean isInteger(String value) {
        var result = Integer.parseInt(value);
        return result >= 1 && 64 >= result;
    }
    public boolean isDouble(String value) {
        var result = Double.parseDouble(value);
        return result >= 0.01 && 999999999999.99 >= result;
    }
}