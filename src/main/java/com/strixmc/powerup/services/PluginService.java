package com.strixmc.powerup.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.universal.loader.LoaderManager;
import com.strixmc.universal.service.ServiceManager;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collections;

@Singleton
public class PluginService implements ServiceManager {

  @Inject private PowerUpsPlugin main;
  @Inject @Named("CommandsLoader") private LoaderManager commandsLoader;
  @Inject @Named("LangUtilityLoader") private LoaderManager langUtilityLoader;
  @Inject @Named("PowerUpsService") private ServiceManager powerUpsService;

  @Override
  public void start() {
    createConfig();
    powerUpsService.start();

    langUtilityLoader.load();
    commandsLoader.load();
  }

  @Override
  public void stop() {
    powerUpsService.stop();
  }

  @SneakyThrows
  private void createConfig() {
    main.saveDefaultConfig();
    File configFile = new File(main.getDataFolder(), "config.yml");
    ConfigUpdater.update(main, "config.yml", configFile, Collections.singletonList("NOTHING"));
    main.reloadConfig();
  }
}
