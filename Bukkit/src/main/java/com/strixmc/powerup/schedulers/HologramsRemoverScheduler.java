package com.strixmc.powerup.schedulers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.strixmc.common.cache.CacheProvider;
import com.strixmc.powerup.PowerUpsPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramsRemoverScheduler extends BukkitRunnable {

  @Inject @Named("ActiveHologramsCache") private CacheProvider<Long, Hologram> hologramsCache;
  @Inject private PowerUpsPlugin main;

  @Override
  public void run() {
    if (hologramsCache.get().isEmpty()) return;
    final int maxLife = main.getConfig().getInt("POWERUP_LIFE");

    hologramsCache.get().forEach((savedTime, hologram) -> {
      if ((System.currentTimeMillis() - savedTime) / 1000L >= maxLife) {
        if (!hologram.isDeleted()) {
          hologram.delete();
        }
        hologramsCache.remove(savedTime);
      }
    });
  }
}
