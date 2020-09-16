package com.strixmc.powerup.utilities.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.loaders.CommandsLoader;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.powerup.manager.PowerUpManagerImplement;
import com.strixmc.powerup.services.PluginService;
import com.strixmc.powerup.services.PowerUpsService;
import com.strixmc.powerup.utilities.LoaderManager;
import com.strixmc.powerup.utilities.ServiceManager;
import com.strixmc.powerup.utilities.lang.LangUtility;
import com.strixmc.powerup.utilities.lang.LangUtilityManager;

public class BinderModule extends AbstractModule {

    private final PowerUpsPlugin plugin;

    public BinderModule(PowerUpsPlugin plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {

        this.bind(PowerUpsPlugin.class).toInstance(this.plugin);

        this.bind(PowerUpManager.class).to(PowerUpManagerImplement.class);

        this.bind(LangUtility.class).to(LangUtilityManager.class);

        this.bind(ServiceManager.class).annotatedWith(Names.named("Plugin-Service")).to(PluginService.class);

        this.bind(ServiceManager.class).annotatedWith(Names.named("PowerUps-Service")).to(PowerUpsService.class);

        this.bind(LoaderManager.class).annotatedWith(Names.named("CommandsLoader")).to(CommandsLoader.class);

    }
}
