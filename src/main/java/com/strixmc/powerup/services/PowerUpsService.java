package com.strixmc.powerup.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.powerup.PowerUp;
import com.strixmc.powerup.powerup.PowerUpBuilder;
import com.strixmc.powerup.utilities.FileCreator;
import com.strixmc.universal.cache.CacheProvider;
import com.strixmc.universal.service.ServiceManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Singleton
public class PowerUpsService implements ServiceManager {

  private File folder;
  @Inject private PowerUpsPlugin main;
  @Inject @Named("PowerUpsCache") private CacheProvider<String, PowerUp> powerUpCacheProvider;

  @Override
  public void start() {
    folder = new File(main.getDataFolder().getAbsolutePath() + "/powerups");
    if (!folder.exists()) {
      folder.mkdirs();
    }

    if (Arrays.stream(folder.listFiles()).count() == 0) return;
    for (File file : folder.listFiles()) {
      final FileCreator fileCreator = new FileCreator(main, file.getName(), "", folder);

      final String ID = file.getName().split(".yml")[0];
      final String name = fileCreator.getString("NAME");
      final String material = fileCreator.getString("MATERIAL");
      final short data = (short) fileCreator.getInt("DATA");
      final int chance = fileCreator.getInt("CHANCE");
      final boolean enabled = fileCreator.getBoolean("ENABLED");
      final List<String> hologram = fileCreator.getStringList("HOLOGRAM");
      final List<String> actions = fileCreator.getStringList("ACTIONS");
      powerUpCacheProvider.add(ID, new PowerUpBuilder(ID, name, chance, hologram, actions, enabled, material, data));
    }
  }

  @Override
  public void stop() {
    if (powerUpCacheProvider.get().isEmpty()) return;

    powerUpCacheProvider.get().forEach((s, powerUp) -> {
      final FileCreator file = new FileCreator(main, s, ".yml", folder);
      file.set("ENABLED", powerUp.isEnabled());
      file.set("NAME", powerUp.getName());
      file.set("CHANCE", powerUp.getChance());
      file.set("MATERIAL", powerUp.getMaterial());
      file.set("DATA", powerUp.getData());
      file.set("HOLOGRAM", powerUp.getHologram());
      file.set("ACTIONS", powerUp.getActions());
      file.save();
      file.reload();
    });
  }
}
