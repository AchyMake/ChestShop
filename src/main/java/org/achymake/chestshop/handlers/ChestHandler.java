package org.achymake.chestshop.handlers;

import org.achymake.chestshop.ChestShop;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public record ChestHandler(Chest getChest) {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    public PersistentDataContainer getData() {
        return getChest().getPersistentDataContainer();
    }
    public void setOwner(OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            getData().remove(NamespacedKey.minecraft("owner"));
            getChest().update();
        } else {
            getData().set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, offlinePlayer.getUniqueId().toString());
            getChest().update();
        }
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
    @Override
    public Chest getChest() {
        return getChest;
    }
}