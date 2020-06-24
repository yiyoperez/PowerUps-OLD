package com.strixmc.powerup.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.strixmc.powerup.PowerUps;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.powerup.manager.PowerUpManagerProvider;
import com.strixmc.powerup.utilities.lang.LangUtility;
import com.strixmc.powerup.utilities.lang.LangUtilityManager;

public class SimpleBinderModule extends AbstractModule {

    private final PowerUps plugin;

    public SimpleBinderModule(PowerUps plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {

        this.bind(PowerUps.class).toInstance(this.plugin);

        this.bind(PowerUpManager.class).to(PowerUpManagerProvider.class).in(Scopes.SINGLETON);

        this.bind(LangUtility.class).to(LangUtilityManager.class).in(Scopes.SINGLETON);

    }
}
