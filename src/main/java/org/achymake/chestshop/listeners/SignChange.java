package org.achymake.chestshop.listeners;

import org.achymake.chestshop.ChestShop;
import org.achymake.chestshop.data.Message;
import org.achymake.chestshop.handlers.ChestHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;

public class SignChange implements Listener {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private ChestHandler getChestHandler(Chest chest) {
        return getInstance().getChestHandler(chest);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public SignChange() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();
        var sign = (Sign) block.getState();
        var chest = getChest(sign);
        if (chest == null)return;
        if (!sign.getLine(0).equalsIgnoreCase("[chestshop]"))return;
        if (!player.hasPermission("chestshop.event.create"))return;
        if (!getMessage().isInteger(sign.getLine(1)))return;
        if (!getMessage().isDouble(sign.getLine(3)))return;
        getInstance().getScheduleHandler().runLater(new Runnable() {
            @Override
            public void run() {
                var amount = Integer.parseInt(sign.getLine(1));
                var material = Material.valueOf(sign.getLine(2).toUpperCase().replace(" ", "_"));
                var cost = Double.parseDouble(sign.getLine(3));
                sign.getPersistentDataContainer().set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, player.getUniqueId().toString());
                sign.getPersistentDataContainer().set(NamespacedKey.minecraft("amount"), PersistentDataType.INTEGER, amount);
                sign.getPersistentDataContainer().set(NamespacedKey.minecraft("material"), PersistentDataType.STRING, material.toString());
                sign.getPersistentDataContainer().set(NamespacedKey.minecraft("cost"), PersistentDataType.DOUBLE, cost);
                getChestHandler(getChest(sign)).setOwner(player);
                sign.setLine(0, getMessage().addColor("&6" + player.getName()));
                sign.setLine(1, getMessage().addColor("&f" + amount));
                sign.setLine(2, getMessage().addColor("&f" + getMessage().capitalize(material.toString())));
                sign.setLine(3, getMessage().addColor("&a" + getInstance().getEconomy().currencyNamePlural() + getInstance().getEconomy().format(cost)));
                sign.setWaxed(true);
                sign.update(true);
            }
        }, 0);
    }
    private Chest getChest(Sign sign) {
        if (sign.getBlockData() instanceof WallSign wallSign) {
            if (wallSign.getFacing().equals(BlockFace.EAST)) {
                if (sign.getLocation().add(-1.0, 0.0, 0.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.NORTH)) {
                if (sign.getLocation().add(0.0, 0.0, 1.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.WEST)) {
                if (sign.getLocation().add(1.0, 0.0, 0.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.SOUTH)) {
                if (sign.getLocation().add(0.0, 0.0, -1.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else return null;
        } else return null;
    }
}