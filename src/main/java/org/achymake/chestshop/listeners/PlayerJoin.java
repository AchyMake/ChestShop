package org.achymake.chestshop.listeners;

import org.achymake.chestshop.ChestShop;
import org.achymake.chestshop.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerJoin implements Listener {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private UpdateChecker getUpdateChecker() {
        return getInstance().getUpdateChecker();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerJoin() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(PlayerJoinEvent event) {
        getUpdateChecker().getUpdate(event.getPlayer());
    }
}