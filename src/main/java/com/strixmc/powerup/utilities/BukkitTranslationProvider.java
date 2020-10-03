package com.strixmc.powerup.utilities;

import me.fixeddev.commandflow.bukkit.BukkitDefaultTranslationProvider;

public class BukkitTranslationProvider extends BukkitDefaultTranslationProvider {

  public BukkitTranslationProvider() {
    translations.put("command.subcommand.invalid", "Subcommand %s does not exists!");
    translations.put("command.no-permission", "You have no permissions to perform that..");
    translations.put("argument.no-more", "No more arguments were found, size: %s position: %s");
    translations.put("player.offline", "The player %s is offline!");
    translations.put("sender.unknown", "Command sender is unknown!");
    translations.put("sender.only-player", "This command can only be executed by players.");
  }
}
