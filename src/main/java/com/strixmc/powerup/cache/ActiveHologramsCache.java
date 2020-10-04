package com.strixmc.powerup.cache;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.inject.Singleton;
import com.strixmc.universal.cache.CacheProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ActiveHologramsCache implements CacheProvider<Long, Hologram> {

  private Map<Long, Hologram> hologramMap;

  public ActiveHologramsCache() {
    this.hologramMap = new ConcurrentHashMap<>();
  }

  @Override
  public Map<Long, Hologram> get() {
    return this.hologramMap;
  }
}
