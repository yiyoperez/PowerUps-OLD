package com.strixmc.powerup.menus;

import me.patothebest.guiframework.gui.anvil.AnvilSlot;
import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.gui.inventory.button.AnvilButton;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EditMenu extends GUIPage {

  public EditMenu(Plugin plugin, Player player, String rawName, int size) {
    super(plugin, player, rawName, size);
    build();
  }

  @Override
  public void buildPage() {
    ItemStackBuilder simpleActionItem = new ItemStackBuilder().material(Material.TNT).name(ChatColor.RED + "Change Material");
    ItemStackBuilder anvilItem = new ItemStackBuilder().material(Material.ANVIL).name("Renombrar");

    addButton(new SimpleButton(simpleActionItem, () -> new MaterialsMenu(plugin, player, "Materials")), 2);
    addButton(new AnvilButton(anvilItem).onConfirm((event) -> {
      player.sendMessage("You typed " + event.getOutput());
      event.setWillClose(true);
      new EditMenu(plugin, player, "Edit Menu", 9 * 3);
    }).onCancel(() -> {
      player.sendMessage("You cancelled");
      player.closeInventory();
      new EditMenu(plugin, player, "Edit Menu", 9 * 3);
    }).useSlot(AnvilSlot.INPUT_LEFT, anvilItem), 4);
  }
}
