package org.achymake.chestshop.handlers;

import org.achymake.chestshop.ChestShop;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

public record SignHandler(Sign getSign) {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    public PersistentDataContainer getData() {
        return getSign().getPersistentDataContainer();
    }
    public int getAmount() {
        return getData().get(NamespacedKey.minecraft("amount"), PersistentDataType.INTEGER);
    }
    public double getCost() {
        return getData().get(NamespacedKey.minecraft("cost"), PersistentDataType.DOUBLE);
    }
    public Material getMaterial() {
        return Material.valueOf(getData().get(NamespacedKey.minecraft("material"), PersistentDataType.STRING));
    }
    public boolean isShop() {
        return getOwner() != null;
    }
    public OfflinePlayer getOwner() {
        if (getData().has(NamespacedKey.minecraft("owner"), PersistentDataType.STRING)) {
            var uuidString = getData().get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
            if (uuidString != null) {
                return getInstance().getServer().getOfflinePlayer(UUID.fromString(uuidString));
            } else return null;
        } else return null;
    }
    public Chest getChest() {
        if (getSign().getBlockData() instanceof WallSign wallSign) {
            if (wallSign.getFacing().equals(BlockFace.EAST)) {
                if (getSign().getLocation().add(-1.0, 0.0, 0.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.NORTH)) {
                if (getSign().getLocation().add(0.0, 0.0, 1.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.WEST)) {
                if (getSign().getLocation().add(1.0, 0.0, 0.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else if (wallSign.getFacing().equals(BlockFace.SOUTH)) {
                if (getSign().getLocation().add(0.0, 0.0, -1.0).getBlock().getState() instanceof Chest chest) {
                    return chest;
                } else return null;
            } else return null;
        } else return null;
    }
    public Inventory getInventory() {
        var chest = getChest();
        if (chest != null) {
            return chest.getBlockInventory();
        } else return null;
    }
    public boolean removeItem() {
        var inventory = getInventory();
        if (inventory != null) {
            if (inventory.contains(getMaterial())) {
                var item = inventory.getItem(inventory.first(getMaterial()));
                if (item != null) {
                    var result = item.getAmount() - getAmount();
                    if (result >= item.getAmount()) {
                        item.setAmount(result);
                        return true;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }
    public void giveItem(Player player) {
        var itemStack = new ItemStack(getMaterial(), getAmount());
        if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
            player.getInventory().addItem(itemStack);
        } else player.getWorld().dropItem(player.getLocation(), itemStack);
    }
    @Override
    public Sign getSign() {
        return getSign;
    }
}