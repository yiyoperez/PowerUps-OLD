package com.strixmc.powerup.powerup;

import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Singleton
public class PowerUtilities {

  @Inject private Utils utils;

  public void powerUpActions(@NotNull Player player, @NotNull List<String> actions, @NotNull PowerUp powerUp) {
    actions.forEach(action -> {
      action = action.replace("%type%", powerUp.getID());
      if (action.startsWith("[COMMAND]")) {
        action = action.replace("[COMMAND] ", "");
        action = action.replace("%player_name%", player.getName());
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, action);
      }

      if (action.startsWith("[MESSAGE]")) {
        action = action.replace("[MESSAGE] ", "");
        action = action.replace("%player_name%", player.getName());

        player.sendMessage(utils.translate(action));
      }

      if (action.startsWith("[TITLE]")) {
        action = action.replace("[TITLE] ", "");
        action = action.replace("%player_name%", player.getName());

        Titles.sendTitle(player, utils.translate(action), "");
      }

      if (action.startsWith("[SUBTITLE]")) {
        action = action.replace("[SUBTITLE] ", "");
        action = action.replace("%player_name%", player.getName());

        Titles.sendTitle(player, "", utils.translate(action));
      }

      if (action.startsWith("[FULLTITLE]")) {
        action = action.replace("[FULLTITLE] ", "");
        action = action.replace("%player_name%", player.getName());

        Titles.sendTitle(player, utils.translate(action.split(";")[0]), utils.translate(action.split(";")[1]));
      }

      if (action.startsWith("[SOUND]")) {
        action = action.replace("[SOUND] ", "");

        try {
          Sound sound = Sound.valueOf(action.split(";")[0]);
          float volume = Float.parseFloat(action.split(";")[1]);
          float pitch = Float.parseFloat(action.split(";")[2]);

          player.playSound(player.getEyeLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
          for (int i = 0; i < 3; i++) {
            Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named sound (\"" + action.split(";")[0] + "\")");
          }
        }

      }

      if (action.startsWith("[PARTICLE]")) {
        action = action.replace("[PARTICLE] ", "");

        try {
          Effect effect = Effect.valueOf(action.split(";")[0]);

          player.playEffect(player.getLocation(), effect, null);
        } catch (IllegalArgumentException e) {
          for (int i = 0; i < 3; i++) {
            Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named particle (\"" + action.split(";")[0] + "\")");
          }
        }

      }

      if (action.startsWith("[EFFECT]")) {
        action = action.replace("[EFFECT] ", "");

        try {
          PotionEffectType effectType = PotionEffectType.getByName(action.split(",")[0]);
          int duration = Integer.parseInt(action.split(",")[1]);
          int amplifier = Integer.parseInt(action.split(",")[2]);

          player.addPotionEffect(new PotionEffect(effectType, duration, amplifier), true);
        } catch (IllegalArgumentException e) {
          for (int i = 0; i < 3; i++) {
            Bukkit.getLogger().warning(powerUp.getID() + " have a wrong named effect (\"" + action.split(";")[0] + "\")");
          }
        }
      }
    });
  }

  public void spawnPowerUp(@NotNull PowerUp powerUp, @NotNull Location location) {

    double i = 0.25 * powerUp.getHologram().size();
/*
    for (int j = 0; j < powerUp.getHologram().size(); j++) {
      i = i + 0.25;
    }
*/
    location.setY(getGroundLocation(location).getY() + 0.67 + i);
    Hologram hologram = HologramsAPI.createHologram(JavaPlugin.getPlugin(PowerUpsPlugin.class), location);
    VisibilityManager visibilityManager = hologram.getVisibilityManager();
    visibilityManager.setVisibleByDefault(false);
    powerUp.getHologram().forEach(text -> hologram.appendTextLine(utils.translate(text)));
    ItemLine itemLine = hologram.appendItemLine(powerUp.getItem());
    visibilityManager.setVisibleByDefault(true);

    itemLine.setTouchHandler(player -> {
      powerUpActions(player, powerUp.getActions(), powerUp);
      hologram.delete();
    });

    itemLine.setPickupHandler(player -> {
      powerUpActions(player, powerUp.getActions(), powerUp);
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
