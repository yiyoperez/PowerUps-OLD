package com.strixmc.powerup.services;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.common.cache.CacheProvider;
import com.strixmc.common.service.ServiceManager;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.schedulers.HologramsRemoverScheduler;

@Singleton
public class ActiveHologramsService implements ServiceManager {

  @Inject private PowerUpsPlugin main;
  @Inject private HologramsRemoverScheduler hologramsRemoverScheduler;
  @Inject @Named("ActiveHologramsCache") private CacheProvider<Long, Hologram> activeHologramsCache;

  @Override
  public void start() {
    hologramsRemoverScheduler.runTaskTimer(main, 20L, 20L);
  }

  @Override
  public void stop() {
    if (activeHologramsCache.get().isEmpty()) return;
    activeHologramsCache.get().forEach((savedTime, hologram) -> {
      if (!hologram.isDeleted()) {
        hologram.delete();
      }
      activeHologramsCache.remove(savedTime);
    });
  }
}
