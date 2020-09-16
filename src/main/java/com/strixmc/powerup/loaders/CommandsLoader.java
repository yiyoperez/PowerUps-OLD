package com.strixmc.powerup.loaders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.commands.PowerUpCommand;
import com.strixmc.powerup.utilities.CustomI18n;
import com.strixmc.powerup.utilities.LoaderManager;
import me.fixeddev.ebcm.bukkit.BukkitCommandManager;
import me.fixeddev.ebcm.parametric.ParametricCommandBuilder;
import me.fixeddev.ebcm.parametric.ReflectionParametricCommandBuilder;

@Singleton
public class CommandsLoader implements LoaderManager {

    @Inject private PowerUpsPlugin main;
    @Inject private PowerUpCommand powerUpCommand;
    @Inject private CustomI18n customI18n;

    @Override public void load() {

        ParametricCommandBuilder builder = new ReflectionParametricCommandBuilder();
        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(main.getName());

        bukkitCommandManager.setI18n(customI18n);

        bukkitCommandManager.registerCommands(builder.fromClass(this.powerUpCommand));

    }
}
