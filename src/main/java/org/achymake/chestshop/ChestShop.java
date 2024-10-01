package org.achymake.chestshop;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chestshop.commands.ChestShopCommand;
import org.achymake.chestshop.data.Message;
import org.achymake.chestshop.handlers.ChestHandler;
import org.achymake.chestshop.handlers.ScheduleHandler;
import org.achymake.chestshop.handlers.SignHandler;
import org.achymake.chestshop.listeners.BlockBreak;
import org.achymake.chestshop.listeners.PlayerInteract;
import org.achymake.chestshop.listeners.PlayerJoin;
import org.achymake.chestshop.listeners.SignChange;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ChestShop extends JavaPlugin {
    private static ChestShop instance;
    private Message message;
    private ScheduleHandler scheduleHandler;
    private UpdateChecker updateChecker;
    private PluginManager manager;
    public Economy economy = null;
    @Override
    public void onEnable() {
        instance = this;
        message = new Message();
        scheduleHandler = new ScheduleHandler();
        updateChecker = new UpdateChecker();
        manager = getServer().getPluginManager();
        commands();
        events();
        reload();
        var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
            sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
            getUpdateChecker().getUpdate();
        } else {
            sendWarning("'Vault' does not have any 'Economy' installed");
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    @Override
    public void onDisable() {
        getScheduleHandler().cancelAll();
    }
    private void commands() {
        new ChestShopCommand();
    }
    private void events() {
        new BlockBreak();
        new PlayerInteract();
        new PlayerJoin();
        new SignChange();
    }
    public void reload() {
        var file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                sendWarning(e.getMessage());
            }
        } else {
            getConfig().addDefault("notify-update", true);
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                sendWarning(e.getMessage());
            }
        }
    }
    public Economy getEconomy() {
        return economy;
    }
    public PluginManager getManager() {
        return manager;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public SignHandler getSignHandler(Sign sign) {
        return new SignHandler(sign);
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public ChestHandler getChestHandler(Chest chest) {
        return new ChestHandler(chest);
    }
    public Message getMessage() {
        return message;
    }
    public static ChestShop getInstance() {
        return instance;
    }
    public void sendInfo(String message) {
        getLogger().info(message);
    }
    public void sendWarning(String message) {
        getLogger().warning(message);
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public boolean isSpigot() {
        return getMinecraftProvider().equals("CraftBukkit");
    }
}