package com.strixmc.powerup.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.common.loader.LoaderManager;
import com.strixmc.common.service.ServiceManager;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.ConfigUpdater;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collections;

@Singleton
public class PluginService implements ServiceManager {

  @Inject private PowerUpsPlugin main;
  @Inject @Named("CommandsLoader") private LoaderManager commandsLoader;
  @Inject @Named("ListenersLoader") private LoaderManager listenersLoader;
  @Inject @Named("LangUtilityLoader") private LoaderManager langUtilityLoader;
  @Inject @Named("PowerUpsService") private ServiceManager powerUpsService;
  @Inject @Named("ActiveHologramsService") private ServiceManager activeHolograms;

  @Override
  public void start() {
    createConfig();
    powerUpsService.start();
    activeHolograms.start();

    langUtilityLoader.load();
    commandsLoader.load();
    listenersLoader.load();
  }

  @Override
  public void stop() {
    activeHolograms.stop();
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
