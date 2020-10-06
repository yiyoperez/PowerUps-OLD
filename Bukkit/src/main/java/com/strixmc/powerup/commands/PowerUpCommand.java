package com.strixmc.powerup.commands;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.strixmc.common.cache.CacheProvider;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.menus.SelectorMenu;
import com.strixmc.powerup.powerup.PowerUp;
import com.strixmc.powerup.powerup.PowerUpImpl;
import com.strixmc.powerup.powerup.PowerUtilities;
import com.strixmc.powerup.utilities.Utils;
import com.strixmc.powerup.utilities.lang.LangUtility;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@Command(names = {"pu", "pup", "powerups", "powerups"}, permission = "powerups.command.main")
public class PowerUpCommand implements CommandClass {

  @Inject @Named("PowerUpsCache") private CacheProvider<String, PowerUp> powerUpCacheProvider;
  @Inject private LangUtility lang;
  @Inject private PowerUtilities powerUtilities;
  @Inject private Utils utils;
  @Inject private PowerUpsPlugin main;

  @Command(names = "")
  public boolean command(@Sender Player p, CommandContext context) {
    lang.getHelp().forEach(s -> {
      String label = String.join(" ", context.getLabels());
      s = s.replace("<command>", label);
      p.sendMessage(s);
    });
    return true;
  }

  @Command(names = "create")
  public boolean createCommand(@Sender Player p, @OptArg String name) {

    if (!p.hasPermission("powerups.command.create")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (name != null) {
      final String ID = utils.stripColor(name).toUpperCase();

      if (powerUpCacheProvider.exists(ID)) {
        p.sendMessage(lang.getAlreadyExist(ID));
        return true;
      }

      powerUpCacheProvider.add(ID, new PowerUpImpl(ID, utils.translate(name)));
      p.sendMessage(lang.getCreated(utils.translate(name), ID));
      return true;
    }

    p.sendMessage(lang.getCreateUsage());
    return true;
  }

  @Command(names = "delete")
  public boolean deleteCommand(@Sender Player p, @OptArg String name) {
    if (!p.hasPermission("powerups.command.delete")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (name != null) {
      final String ID = utils.stripColor(name).toUpperCase();

      if (!powerUpCacheProvider.exists(ID)) {
        p.sendMessage(lang.getNoExist(ID));
        return true;
      }

      powerUpCacheProvider.remove(ID);
      p.sendMessage(lang.getDeleted(ID));
      return true;
    }

    p.sendMessage(lang.getDeleteUsage());
    return true;
  }

  @Command(names = "enable")
  public boolean enableCommand(@Sender Player p, @OptArg String name) {
    if (!p.hasPermission("powerups.command.enable")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (name != null) {

      final String ID = utils.stripColor(name).toUpperCase();

      if (!powerUpCacheProvider.exists(ID)) {
        p.sendMessage(lang.getNoExist(ID));
        return true;
      }

      powerUpCacheProvider.find(ID).ifPresent(powerUp -> {
        if (powerUp.isEnabled()) {
          p.sendMessage(lang.getAlreadyEnabled(powerUp.getID()));
          return;
        }

        powerUp.setEnabled(true);
        p.sendMessage(lang.getEnabled(powerUp.getID()));
      });
      return true;
    }

    p.sendMessage(lang.getEnableUsage());
    return true;
  }

  @Command(names = "disable")
  public boolean disableCommand(@Sender Player p, @OptArg String name) {
    if (!p.hasPermission("powerups.command.disable")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (name != null) {

      final String ID = utils.stripColor(name).toUpperCase();

      if (!powerUpCacheProvider.exists(ID)) {
        p.sendMessage(lang.getNoExist(ID));
        return true;
      }

      powerUpCacheProvider.find(ID).ifPresent(powerUp -> {
        if (!powerUp.isEnabled()) {
          p.sendMessage(lang.getAlreadyDisabled(powerUp.getID()));
          return;
        }

        powerUp.setEnabled(false);
        p.sendMessage(lang.getDisabled(powerUp.getID()));
      });
      return true;
    }

    p.sendMessage(lang.getDisableUsage());
    return true;
  }

  @Command(names = "spawn")
  public boolean spawnCommand(@Sender Player p, @OptArg String name) {

    if (!p.hasPermission("powerups.command.spawn")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (name != null) {

      final String ID = utils.stripColor(name).toUpperCase();

      if (!powerUpCacheProvider.exists(ID)) {
        p.sendMessage(lang.getNoExist(ID));
        return true;
      }

      powerUpCacheProvider.find(ID).ifPresent(powerUp -> {
        powerUtilities.spawnPowerUp(powerUp, p.getLocation());
        p.sendMessage("PowerUp has been spawned.");
      });
      return true;
    }

    return false;
  }

  @Command(names = "list")
  public boolean listCommand(@Sender Player p) {
    if (!p.hasPermission("powerups.command.list")) {
      p.sendMessage(lang.getNoPermission());
      return true;
    }

    if (powerUpCacheProvider.get().isEmpty()) {
      p.sendMessage(lang.getNoAvailablePowerUp());
      return true;
    }

    new SelectorMenu(main, p, "Edit Menu", () -> powerUpCacheProvider.get().values());

    p.sendMessage(lang.getAvailablePowerUp());
    powerUpCacheProvider.get().forEach((id, powerUp) -> {

      final TextComponent tc = new TextComponent(utils.translate(powerUp.getName() + " &8| &7(" + (powerUp.isEnabled() ? "&a" : "&c") + powerUp.getID() + "&7)"));
      final StringBuilder hover = new StringBuilder();

      hover.append("\n").append(utils.translate("&bHologram"));
      powerUp.getHologram().forEach(s -> hover.append("\n").append(utils.translate(s)));
      hover.append("\n\n").append(utils.translate("&bInfo"));
      hover.append("\n").append(utils.translate("&eStatus " + powerUp.isEnabled()));
      hover.append("\n").append(utils.translate("&eChance " + powerUp.getChance()));
      hover.append("\n").append(utils.translate("&eType " + "Add methods"));
      hover.append("\n").append(utils.translate("&eChance " + powerUp.getChance()));
      hover.append("\n").append(utils.translate("&eItem " + powerUp.getItem().getType()));
      hover.append("\n\n").append(utils.translate("&bActions"));
      powerUp.getActions().forEach(s -> hover.append("\n").append(utils.translate(s)));
      hover.append("\n ");
      tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover.toString()).create()));

      p.spigot().sendMessage(tc);
    });

    return true;
  }

  //TODO FINISH SUB-COMMAND
/*
  @Command(names = "reload")
  public boolean reloadCommand(@Sender Player p) {
    if (!p.hasPermission("powerups.command.reload")) {
      p.sendMessage(utils.translate(lang.getNoPermission()));
      return true;
    }

    plugin.reloadConfig();
    plugin.saveConfig();

    File configFile = new File(plugin.getDataFolder(), "config.yml");
    try {
      ConfigUpdater.update(plugin, "config.yml", configFile, Collections.singletonList("PowerUps"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    plugin.reloadConfig();

    powerUpManager.loadPowerUps();

    p.sendMessage(utils.translate("&aConfig and powerups have been reloaded!"));

    return true;
  }*/
}
