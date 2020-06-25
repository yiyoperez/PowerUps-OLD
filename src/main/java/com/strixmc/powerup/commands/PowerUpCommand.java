package com.strixmc.powerup.commands;

import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import com.strixmc.powerup.powerup.PowerUp;
import com.strixmc.powerup.powerup.PowerUpBuilder;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.Utils;
import com.strixmc.powerup.utilities.lang.LangUtility;
import me.fixeddev.ebcm.parametric.CommandClass;
import me.fixeddev.ebcm.parametric.annotation.ACommand;
import me.fixeddev.ebcm.parametric.annotation.Default;
import me.fixeddev.ebcm.parametric.annotation.Injected;
import me.fixeddev.ebcm.parametric.annotation.Usage;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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

    @ACommand(names = "create")
    @Usage(usage = "/<command> create <Name>")
    public boolean createCommand(@Injected(true) CommandSender sender, @Default("Empty !") String name) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;


        if (!p.hasPermission("powerups.command.create")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        String ID = ChatColor.stripColor(Utils.color(name)).toUpperCase();

        if (powerUpManager.contains(ID)) {
            p.sendMessage(Utils.color(lang.getAlreadyExist(ID)));
            return true;
        }

        if (!name.equals("Empty !")) {
            PowerUp powerUp = new PowerUpBuilder(ID, Utils.color(name), 100.0, Arrays.asList("&eDefault PowerUp", "&eHologram text."), Arrays.asList("[MESSAGE] Hey, this action works!", "[SOUND] ARROW_HIT;1.0;1.0"));
            powerUp.setItem("STONE", (short) 0);
            powerUpManager.add(powerUp);

            p.sendMessage(Utils.color(lang.getCreated(powerUp.getName(), powerUp.getID())));
            return true;
        }

        p.sendMessage(Utils.color(lang.getCreateHelp()));

        return false;
    }

    @ACommand(names = "delete")
    @Usage(usage = "/<command> delete <Name>")
    public boolean deleteCommand(@Injected(true) CommandSender sender, @Default("Empty !") String name) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;


        if (!p.hasPermission("powerups.command.delete")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        if (!name.equals("Empty !")) {

            String ID = ChatColor.stripColor(Utils.color(name)).toUpperCase();

            if (!powerUpManager.remove(ID)) {
                p.sendMessage(Utils.color(lang.getNoExist(ID)));
                return true;
            }

            p.sendMessage(Utils.color(lang.getDeleted(ID)));

            return true;
        }


        return false;
    }

    @ACommand(names = "enable")
    @Usage(usage = "/<command> delete <Name>")
    public boolean enableCommand(@Injected(true) CommandSender sender, @Default("Empty !") String name) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;


        if (!p.hasPermission("powerups.command.enable")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        if (!name.equals("Empty !")) {

            String ID = ChatColor.stripColor(Utils.color(name)).toUpperCase();

            if (!powerUpManager.contains(ID)) {
                p.sendMessage(Utils.color(lang.getNoExist(ID)));
                return true;
            }

            PowerUp powerUp = powerUpManager.getPowerUp(name);

            if (powerUp.isEnabled()) {
                p.sendMessage(Utils.color(lang.getAlreadyEnabled(powerUp.getID())));
                return true;
            }

            powerUp.setEnabled(true);
            p.sendMessage(Utils.color(lang.getEnabled(powerUp.getID())));

            return true;
        }


        return false;
    }

    @ACommand(names = "disable")
    @Usage(usage = "/<command> disable <Name>")
    public boolean disableCommand(@Injected(true) CommandSender sender, @Default("Empty !") String name) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;


        if (!p.hasPermission("powerups.command.disable")) {
            p.sendMessage(Utils.color(lang.getNoPermission()));
            return true;
        }

        if (!name.equals("Empty !")) {

            String ID = ChatColor.stripColor(Utils.color(name)).toUpperCase();

            if (!powerUpManager.contains(ID)) {
                p.sendMessage(Utils.color(lang.getNoExist(ID)));
                return true;
            }

            PowerUp powerUp = powerUpManager.getPowerUp(name);

            if (!powerUp.isEnabled()) {
                p.sendMessage(Utils.color(lang.getAlreadyDisabled(powerUp.getID())));
                return true;
            }

            powerUp.setEnabled(false);
            p.sendMessage(Utils.color(lang.getDisabled(powerUp.getID())));

            return true;
        }


        return false;
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
            p.sendMessage(Utils.color(lang.getNoAvailablePowerUp()));
            return true;
        }

        p.sendMessage(Utils.color(lang.getAvailablePowerUp()));


        powerUpManager.getPowerUps().forEach(powerUp -> {
            TextComponent tc = new TextComponent(Utils.color(powerUp.getName() + " &8| &7(" + powerUp.getID() + ")"));

            StringBuilder hover = new StringBuilder();

            hover.append("\n").append(Utils.color("&bHologram"));
            powerUp.getHologram().forEach(s -> hover.append("\n").append(Utils.color(s)));
            hover.append("\n\n").append(Utils.color("&bInfo"));
            hover.append("\n").append(Utils.color("&eChance " + powerUp.getChance()));
            hover.append("\n").append(Utils.color("&eType " + /*todo*/ "Add methods"));
            hover.append("\n").append(Utils.color("&eChance " + powerUp.getChance()));
            hover.append("\n").append(Utils.color("&eItem " + powerUp.getItem().getType()));
            hover.append("\n\n").append(Utils.color("&bActions"));
            powerUp.getActions().forEach(s -> hover.append("\n").append(Utils.color(s)));
            hover.append("\n ");
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover.toString()).create()));

            p.spigot().sendMessage(tc);
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
