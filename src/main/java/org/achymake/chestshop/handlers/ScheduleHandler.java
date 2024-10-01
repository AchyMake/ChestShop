package org.achymake.chestshop.handlers;

import org.achymake.chestshop.ChestShop;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleHandler {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private BukkitScheduler getScheduler() {
        return getInstance().getServer().getScheduler();
    }
    public BukkitTask runLater(Runnable runnable, long timer) {
        return getScheduler().runTaskLater(getInstance(), runnable, timer);
    }
    public BukkitTask runAsynchronously(Runnable runnable) {
        return getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
    public boolean isQueued(int taskID) {
        return getScheduler().isQueued(taskID);
    }
    public void cancel(int taskID) {
        getScheduler().cancelTask(taskID);
    }
    public void cancelAll() {
        getScheduler().cancelTasks(getInstance());
    }
}