package com.strixmc.powerup.menus;

import com.strixmc.powerup.powerup.PowerUp;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import me.patothebest.guiframework.gui.inventory.page.StaticPaginatedUI;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.function.Supplier;

public class SelectorMenu extends StaticPaginatedUI<PowerUp> {

  public SelectorMenu(Plugin plugin, Player player, String rawName, Supplier<Collection<? extends PowerUp>> listProvider) {
    super(plugin, player, rawName, listProvider);
    build();
  }

  @Override
  protected GUIButton createButton(PowerUp powerUp) {
    return new SimpleButton(new ItemStackBuilder(powerUp.getItem()).name(powerUp.getName()).lore(powerUp.getHologram()).addBlankLineLore().addLore(powerUp.getActions()).addBlankLineLore().addLore(ChatColor.DARK_AQUA + "Click to edit powerup.")).onClick(() -> {
      new EditMenu(plugin, player, "Editing " + powerUp.getID(), 9 * 3);
      player.sendMessage("Not implemented yet.");
    });
  }
}
