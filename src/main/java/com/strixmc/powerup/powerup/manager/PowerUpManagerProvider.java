package com.strixmc.powerup.powerup.manager;

import com.cryptomorin.xseries.messages.Titles;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import com.google.inject.Inject;
import com.strixmc.powerup.PowerUps;
import com.strixmc.powerup.powerup.PowerUp;
import com.strixmc.powerup.powerup.PowerUpBuilder;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.Utils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class PowerUpManagerProvider implements PowerUpManager {

    @Inject
    private PowerUps plugin;
    private List<PowerUp> powerUps = new ArrayList<>();
    private List<PowerUp> runningHolograms = new ArrayList<>();

    @Override
    public void loadPowerUps() {
        if (!plugin.getConfig().getConfigurationSection("PowerUps").getKeys(false).isEmpty()) {
            this.powerUps.clear();
            for (String id : plugin.getConfig().getConfigurationSection("PowerUps").getKeys(false)) {

                String name = plugin.getConfig().getString("PowerUps." + id + ".Display-Name");
                boolean status = plugin.getConfig().getBoolean("PowerUps." + id + ".Enabled");
                double chance = plugin.getConfig().getDouble("PowerUps." + id + ".Chance");
                List<String> hologram = plugin.getConfig().getStringList("PowerUps." + id + ".Hologram");
                List<String> actions = plugin.getConfig().getStringList("PowerUps." + id + ".Actions");
                PowerUp powerUp = new PowerUpBuilder(id, name, chance, hologram, actions);

                String material = plugin.getConfig().getString("PowerUps." + id + ".Material");
                int data = plugin.getConfig().getInt("PowerUps." + id + ".Data");
                powerUp.setItem(material, (short) data);
                powerUp.setEnabled(status);

                add(powerUp);
            }
        }
    }

    @Override
    public void savePowerUps() {
        if (!this.powerUps.isEmpty()) {
            plugin.getConfig().set("PowerUps", null);
            this.powerUps.forEach(powerUp -> {
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Display-Name", powerUp.getName());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Chance", powerUp.getChance());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Enabled", powerUp.isEnabled());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Hologram", powerUp.getHologram());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Actions", powerUp.getActions());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Material", powerUp.getItem().getType().toString());
                plugin.getConfig().set("PowerUps." + powerUp.getID() + ".Data", powerUp.getItem().getData().getData());
            });
            plugin.saveConfig();
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            try {
                ConfigUpdater.update(plugin, "config.yml", configFile, Arrays.asList("PowerUps"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            plugin.reloadConfig();
        }
    }

    @Override
    public void spawnPowerUp(@NotNull String powerUp, @NotNull String playerName) {
        spawnPowerUp(getPowerUp(powerUp), Bukkit.getPlayer(playerName).getLocation());
    }

    @Override
    public void spawnPowerUp(@NotNull String powerUp, @NotNull Player player) {
        spawnPowerUp(getPowerUp(powerUp), player.getLocation());
    }

    @Override
    public void spawnPowerUp(@NotNull String powerUp, @NotNull Location location) {
        spawnPowerUp(getPowerUp(powerUp), location);
    }

    @Override
    public void spawnPowerUp(@NotNull PowerUp powerUp, @NotNull Location location) {

        double i = 0;
        for (int j = 0; j < powerUp.getHologram().size(); j++) {
            i = i + 0.25;
        }

        location.setY(getGroundLocation(location).getY() + 0.67 + i);
        Hologram hologram = HologramsAPI.createHologram(plugin, location);
        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.setVisibleByDefault(false);
        powerUp.getHologram().forEach(text -> hologram.appendTextLine(Utils.color(text)));
        ItemLine itemLine = hologram.appendItemLine(powerUp.getItem());
        visibilityManager.setVisibleByDefault(true);

        itemLine.setTouchHandler(player -> {
            powerUpActions(player, powerUp.getActions(), powerUp);
            player.sendMessage(powerUp.getID());
            hologram.delete();
        });

        itemLine.setPickupHandler(player -> {
            player.sendMessage(Utils.color(powerUp.getName()));
            hologram.delete();
        });
    }

    @Override
    public void powerUpActions(@NotNull Player player, @NotNull List<String> actions, @NotNull PowerUp powerUp) {
        actions.forEach(action -> {
            if (action.startsWith("[COMMAND]")) {
                action = action.replace("[COMMAND] ", "");
                action = action.replace("%player_name%", player.getName());
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                Bukkit.dispatchCommand(console, action);
            }

            if (action.startsWith("[MESSAGE]")) {
                action = action.replace("[MESSAGE] ", "");
                action = action.replace("%player_name%", player.getName());

                player.sendMessage(Utils.color(action));
            }

            if (action.startsWith("[TITLE]")) {
                action = action.replace("[TITLE] ", "");
                action = action.replace("%player_name%", player.getName());

                Titles.sendTitle(player, Utils.color(action), "");
            }

            if (action.startsWith("[SUBTITLE]")) {
                action = action.replace("[SUBTITLE] ", "");
                action = action.replace("%player_name%", player.getName());

                Titles.sendTitle(player, "", Utils.color(action));
            }

            if (action.startsWith("[FULLTITLE]")) {
                action = action.replace("[FULLTITLE] ", "");
                action = action.replace("%player_name%", player.getName());

                Titles.sendTitle(player, Utils.color(action.split(";")[0]), Utils.color(action.split(";")[1]));
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

    @Override
    public Location getGroundLocation(@NotNull Location location) {
        World world = location.getWorld();

        Block highest = world != null ? world.getHighestBlockAt(location).getRelative(BlockFace.UP) : null;

        Block block = highest != null && highest.getY() < location.getY() ? highest : location.getBlock();

        while (!block.getType().isSolid() && block.getLocation().getY() >= 0 && block.getLocation().getY() <= 255) {
            block = block.getRelative(BlockFace.DOWN);
        }


        return new Location(location.getWorld(), location.getX(), block.getY() >= 0 ? block.getY() + 1 : location.getY(), location.getZ());
    }

    @Override
    public boolean contains(@NotNull String name) {
        if (this.powerUps.size() > 0) {
            for (PowerUp powerUp : this.powerUps) {
                if (powerUp.getID().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean remove(@NotNull String name) {
        if (contains(name)) {
            for (PowerUp powerUp : this.powerUps) {
                if (powerUp.getID().equalsIgnoreCase(name)) {
                    this.powerUps.remove(powerUp);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(@NotNull PowerUp powerUp) {
        if (!contains(powerUp.getID())) {
            this.powerUps.add(powerUp);
            return true;
        }
        return false;
    }

    @Override
    public PowerUp getPowerUp(@NotNull String name) {
        if (contains(name)) {
            for (PowerUp powerUp : this.powerUps) {
                if (powerUp.getID().equalsIgnoreCase(name)) {
                    return powerUp;
                }
            }
        }
        return null;
    }
}
