package org.achymake.chestshop.listeners;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chestshop.ChestShop;
import org.achymake.chestshop.data.Message;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerInteract implements Listener {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private Economy getEconomy() {
        return getInstance().getEconomy();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerInteract() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        var block = event.getClickedBlock();
        if (block == null)return;
        var player = event.getPlayer();
        if (block.getState() instanceof Sign sign) {
            var signHandler = getInstance().getSignHandler(sign);
            if (!signHandler.isShop())return;
            if (getEconomy().has(player, signHandler.getCost())) {
                if (signHandler.removeItem()) {
                    getEconomy().withdrawPlayer(player, signHandler.getCost());
                    signHandler.giveItem(player);
                    getMessage().send(player, "&6You bought&f " + signHandler.getAmount() + " " + getMessage().capitalize(signHandler.getMaterial().toString()) + "&6 for&a " + getEconomy().currencyNamePlural() + getEconomy().format(signHandler.getCost()));
                } else getMessage().send(player, "&cChest is currently out of stuck!");
            } else getMessage().send(player, "&cYou do not have&a " + getEconomy().currencyNamePlural() + getEconomy().format(signHandler.getCost()) + "&c for&f " + signHandler.getAmount() + " " + getMessage().capitalize(signHandler.getMaterial().toString()));
        } else if (block.getState() instanceof Chest chest) {
            var chestHandler = getInstance().getChestHandler(chest);
            if (!chestHandler.isShop())return;
            if (chestHandler.getOwner() == player)return;
            event.setCancelled(true);
            getMessage().send(player, "&cShop is owned by&f " + chestHandler.getOwner().getName());
        }
    }
}