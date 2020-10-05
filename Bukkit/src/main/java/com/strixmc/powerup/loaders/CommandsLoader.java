package com.strixmc.powerup.loaders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.strixmc.common.loader.LoaderManager;
import com.strixmc.powerup.PowerUpsPlugin;
import com.strixmc.powerup.commands.PowerUpCommand;
import com.strixmc.powerup.utilities.BukkitTranslationProvider;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

@Singleton
public class CommandsLoader implements LoaderManager {

  @Inject private PowerUpsPlugin main;
  @Inject private PowerUpCommand powerUpCommand;

  @Override
  public void load() {
    CommandManager commandManager = new BukkitCommandManager(main.getName());
    commandManager.getTranslator().setProvider(new BukkitTranslationProvider());
    PartInjector injector = PartInjector.create();
    injector.install(new DefaultsModule());
    injector.install(new BukkitModule());
    AnnotatedCommandTreeBuilder builder = new AnnotatedCommandTreeBuilderImpl(injector);
    commandManager.registerCommands(builder.fromClass(powerUpCommand));
  }
}
