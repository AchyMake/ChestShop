package org.achymake.chestshop;

import org.achymake.chestshop.data.Message;
import org.achymake.chestshop.handlers.ScheduleHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private ChestShop getInstance() {
        return ChestShop.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private String getName() {
        return getInstance().name();
    }
    private String getVersion() {
        return getInstance().version();
    }
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update");
    }
    public void getUpdate(Player player) {
        if (player.hasPermission("chestshop.event.join.update")) {
            if (notifyUpdate()) {
                getScheduler().runLater(new Runnable() {
                    @Override
                    public void run() {
                        getLatest((latest) -> {
                            if (!getVersion().equals(latest)) {
                                getMessage().send(player, getName() + "&6 has new update:");
                                getMessage().send(player, "-&a https://www.spigotmc.org/resources/118675/");
                            }
                        });
                    }
                }, 5);
            }
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getScheduler().runAsynchronously(new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        if (!getVersion().equals(latest)) {
                            getInstance().sendInfo(getName() + " has new update:");
                            getInstance().sendInfo("- https://www.spigotmc.org/resources/118675/");
                        }
                    });
                }
            });
        }
    }
    public void getLatest(Consumer<String> consumer) {
        try (var inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 118675).openStream()) {
            var scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            } else inputStream.close();
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
}