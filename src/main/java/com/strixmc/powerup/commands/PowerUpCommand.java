package com.strixmc.powerup.commands;

import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.Utils;
import com.strixmc.powerup.utilities.lang.LangUtility;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@ACommand(names = {"powerup", "powerups", "pup"})
public class PowerUpCommand implements CommandClass {

    @Inject
    private PowerUps plugin;
    @Inject
    private PowerUpManager powerUpManager;
    @Inject
    private LangUtility lang;

    @ACommand(names = "")
    public boolean command(@Injected(true) CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        powerUpManager.spawnPowerUp("example", p.getLocation());

        return true;
    }

    @ACommand(names = "list")
    public boolean listCommand(@Injected(true) CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (!p.hasPermission("powerups.command.list")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        if (powerUpManager.getPowerUps().isEmpty()) {
            p.sendMessage(Utils.color("There are no available powerups."));
            return true;
        }

        powerUpManager.getPowerUps().forEach(powerUp -> {
            p.sendMessage(powerUp.getName() + " (" + powerUp.getID() + ")");
            p.sendMessage(powerUp.getChance() + "");
            powerUp.getHologram().forEach(s -> p.sendMessage(s));
            p.sendMessage(powerUp.getItem().getType() + "");
            powerUp.getActions().forEach(s -> p.sendMessage(s));

        });

        return true;
    }

    @ACommand(names = "reload")
    public boolean reloadCommand(@Injected(true) CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (!p.hasPermission("powerups.command.reload")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, Arrays.asList("PowerUps"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.reloadConfig();
        powerUpManager.loadPowerUps();

        p.sendMessage(Utils.color("&aConfig and powerups have been reloaded!"));

        return true;
    }
}
