package com.strixmc.powerup.loaders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.common.loader.LoaderManager;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.listeners.PowerUpListeners;

@Singleton
public class ListenersLoader implements LoaderManager {

  @Inject private PowerUpsPlugin main;
  @Inject private PowerUpListeners powerUpListeners;

  @Override
  public void load() {
    main.getServer().getPluginManager().registerEvents(powerUpListeners, main);
  }
}
