package com.strixmc.powerup.powerup;

import com.cryptomorin.xseries.XPotion;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.common.cache.CacheProvider;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class PowerUtilities {

  @Inject private Utils utils;
  @Inject @Named("ActiveHologramsCache") private CacheProvider<Long, Hologram> activeHologramsCache;

  //TODO IMPLEMENT XSERIES

  public void powerUpActions(@NotNull Player player, @NotNull List<String> actions, @NotNull PowerUp powerUp) {
    actions.forEach(action -> {

      final Pattern pattern = Pattern.compile("(?<=\\[).+?(?=\\])");
      final Matcher matcher = pattern.matcher(action);
      String actionType = null;

      while (matcher.find()) {
        actionType = matcher.group();
      }

      if (actionType == null) return;
      action = action.replace("$id", powerUp.getID()).replace("$name", powerUp.getName()).replace("$player", player.getName()).replaceFirst("(!?)" + Pattern.quote("[" + actionType + "]"), "").trim();
      //Bukkit.getLogger().info("ACTION " + action);
      //Bukkit.getLogger().info("ACTION TYPE " + actionType);
      switch (actionType.toUpperCase()) {
        case "COMMAND": {
          ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
          Bukkit.dispatchCommand(console, action);
          break;
        }
        case "MESSAGE": {
          player.sendMessage(utils.translate(action));
          break;
        }
        case "TITLE": {
          Titles.sendTitle(player, utils.translate(action), "");
          break;
        }
        case "ACTIONBAR": {
          ActionBar.sendActionBar(player, utils.translate(action));
          break;
        }
        case "SUBTITLE": {
          Titles.sendTitle(player, "", utils.translate(action));
          break;
        }
        case "FULLTITLE": {
          Titles.sendTitle(player, utils.translate(action.split(";")[0]), utils.translate(action.split(";")[1]));
          break;
        }
        case "SOUND": {
          try {
            Sound sound = Sound.valueOf(action.split(";")[0]);
            float volume = Float.parseFloat(action.split(";")[1]);
            float pitch = Float.parseFloat(action.split(";")[2]);

            XSound xSound = XSound.matchXSound(sound);

            if (xSound.isSupported()) {
              xSound.play(player.getEyeLocation(), volume, pitch);
            }
          } catch (IllegalArgumentException e) {
            for (int i = 0; i < 3; i++) {
              Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named sound (\"" + action.split(";")[0] + "\")");
            }
          }
          break;
        }
        case "PARTICLE": {
          try {
            Effect effect = Effect.valueOf(action.split(";")[0]);

            player.getWorld().playEffect(player.getLocation(), effect, null);
          } catch (IllegalArgumentException e) {
            for (int i = 0; i < 3; i++) {
              Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named particle (\"" + action.split(";")[0] + "\")");
            }
          }
          break;
        }
        case "EFFECT": {
          try {
            XPotion effect = XPotion.matchXPotion(PotionEffectType.getByName(action.split(";")[0]));
            int duration = Integer.parseInt(action.split(";")[1]);
            int amplifier = Integer.parseInt(action.split(";")[2]);

            if (effect.isSupported()) {
              effect.parsePotion(duration, amplifier).apply(player);
            }
          } catch (IllegalArgumentException e) {
            for (int i = 0; i < 3; i++) {
              Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named effect (\"" + action.split(";")[0] + "\")");
            }
          }
          break;
        }
      }
    });
  }

  public void spawnPowerUp(@NotNull PowerUp powerUp, @NotNull Location location) {

    final double i = 0.25 * powerUp.getHologram().size();
/*
    for (int j = 0; j < powerUp.getHologram().size(); j++) {
      i = i + 0.25;
    }
*/
    final double fixedY = getGroundLocation(location).getY() + 0.67 + i;
    location.setY(fixedY);
    Hologram hologram = HologramsAPI.createHologram(JavaPlugin.getPlugin(PowerUpsPlugin.class), location);
    VisibilityManager visibilityManager = hologram.getVisibilityManager();
    visibilityManager.setVisibleByDefault(false);
    powerUp.getHologram().forEach(text -> hologram.appendTextLine(utils.translate(text)));
    ItemLine itemLine = hologram.appendItemLine(powerUp.getItem());
    visibilityManager.setVisibleByDefault(true);
    long savedHologramLong = System.currentTimeMillis();
    activeHologramsCache.add(savedHologramLong, hologram);

    //todo add types.

    itemLine.setTouchHandler(player -> {
      powerUpActions(player, powerUp.getActions(), powerUp);
      activeHologramsCache.find(savedHologramLong).ifPresent(ignored -> activeHologramsCache.remove(savedHologramLong));
      hologram.delete();
    });

    itemLine.setPickupHandler(player -> {
      powerUpActions(player, powerUp.getActions(), powerUp);
      activeHologramsCache.find(savedHologramLong).ifPresent(ignored -> activeHologramsCache.remove(savedHologramLong));
      hologram.delete();
    });
  }

  public Location getGroundLocation(@NotNull Location location) {
    World world = location.getWorld();

    Block highest = world != null ? world.getHighestBlockAt(location).getRelative(BlockFace.UP) : null;

    Block block = highest != null && highest.getY() < location.getY() ? highest : location.getBlock();

    while (!block.getType().isSolid() && block.getLocation().getY() >= 0 && block.getLocation().getY() <= 255) {
      block = block.getRelative(BlockFace.DOWN);
    }

    return new Location(location.getWorld(), location.getX(), block.getY() >= 0 ? block.getY() + 1 : location.getY(), location.getZ());
  }
}
