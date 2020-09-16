package com.strixmc.powerup.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.utilities.ConfigUpdater;
import com.strixmc.powerup.utilities.LoaderManager;
import com.strixmc.powerup.utilities.ServiceManager;
import com.strixmc.powerup.utilities.lang.LangUtility;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Collections;

@Singleton
public class PluginService implements ServiceManager {

    @Inject private PowerUpsPlugin main;
    @Inject private LangUtility langUtility;
    @Inject @Named("CommandsLoader") private LoaderManager commandsLoader;
    @Inject @Named("PowerUps-Service") private ServiceManager powerUpsService;

    @Override public void start() {

        createConfig();
        powerUpsService.start();

        commandsLoader.load();

        langUtility.createLang();
        langUtility.load();

    }

    @Override public void finish() {
        powerUpsService.finish();
    }

    @SneakyThrows
    private void createConfig() {
        main.saveDefaultConfig();
        File configFile = new File(main.getDataFolder(), "config.yml");
        ConfigUpdater.update(main, "config.yml", configFile, Collections.singletonList("PowerUps"));
        main.reloadConfig();
    }
}
