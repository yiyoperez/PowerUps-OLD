package com.strixmc.powerup.listeners;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.strixmc.common.cache.CacheProvider;
import com.strixmc.powerup.powerup.PowerUp;
import com.strixmc.powerup.powerup.PowerUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PowerUpListeners implements Listener {

  @Inject private PowerUtilities powerUtilities;
  @Inject @Named("PowerUpsCache") private CacheProvider<String, PowerUp> powerUpCacheProvider;

  @EventHandler
  public void onPlayerKillEvent(PlayerDeathEvent event) {
    Player p = event.getEntity();

    if (powerUpCacheProvider.get().isEmpty()) return;
    List<PowerUp> available = powerUpCacheProvider.get().values().stream().filter(PowerUp::isEnabled).collect(Collectors.toList());
    if (available.isEmpty()) return;

    PowerUp powerUp = available.get(ThreadLocalRandom.current().nextInt(available.size()));
    //double chance = (powerUp.getChance() * 100.0) / 100;

/*    p.sendMessage("Chosen powerup was " + powerUp.getName());
    p.sendMessage("Normal Chance is " + powerUp.getChance());
    p.sendMessage("Chance is " + chance);*/

    //double random = BigDecimal.valueOf(new Random().nextDouble() * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
    int random = new Random().nextInt(100);
    //    p.sendMessage("Random is " + random);
    if (powerUp.getChance() >= random) {
      powerUtilities.spawnPowerUp(powerUp, p.getLocation());
    }
  }
}
