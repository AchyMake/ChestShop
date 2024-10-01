package org.achymake.chestshop.listeners;

import org.achymake.chestshop.ChestShop;
import org.achymake.chestshop.data.Message;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class BlockBreak implements Listener {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public BlockBreak() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        var block = event.getBlock();
        var player = event.getPlayer();
        if (block.getState() instanceof Sign sign) {
            var signHandler = getInstance().getSignHandler(sign);
            if (!signHandler.isShop())return;
            if (signHandler.getOwner() == player) {
                getInstance().getChestHandler(signHandler.getChest()).setOwner(null);
            } else {
                event.setCancelled(true);
                getMessage().send(player, "&cShop is owned by&f " + signHandler.getOwner().getName());
            }
        } else if (block.getState() instanceof Chest chest) {
            var chestHandler = getInstance().getChestHandler(chest);
            if (!chestHandler.isShop())return;
            if (chestHandler.getOwner() == player)return;
            event.setCancelled(true);
            getMessage().send(player, "&cShop is owned by&f " + chestHandler.getOwner().getName());
        }
    }
}