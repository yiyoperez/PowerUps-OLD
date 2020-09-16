package com.strixmc.powerup.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.powerup.manager.PowerUpManager;
import com.strixmc.powerup.utilities.ServiceManager;

@Singleton
public class PowerUpsService implements ServiceManager {

    @Inject private PowerUpsPlugin main;
    @Inject private PowerUpManager powerUpManager;

    @Override public void start() {
        powerUpManager.loadPowerUps();
    }

    @Override public void finish() {
        powerUpManager.savePowerUps();
    }
}
