package com.strixmc.powerup.powerup.manager;

import com.strixmc.powerup.powerup.PowerUp;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PowerUpManager {

    void loadPowerUps();

    void savePowerUps();

    void spawnPowerUp(@NotNull String powerUp, @NotNull String playerName);

    void spawnPowerUp(@NotNull String powerUp, @NotNull Player player);

    void spawnPowerUp(@NotNull String powerUp, @NotNull Location location);

    void spawnPowerUp(@NotNull PowerUp powerUp, @NotNull Location location);

    boolean contains(@NotNull String name);

    boolean remove(@NotNull String name);

    boolean add(@NotNull PowerUp powerUp);

    PowerUp getPowerUp(@NotNull String name);

    List<PowerUp> getPowerUps();

    Location getGroundLocation(@NotNull Location location);

    void powerUpActions(@NotNull Player player, @NotNull List<String> actions, @NotNull PowerUp powerUp);

}
