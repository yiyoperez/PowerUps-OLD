package com.strixmc.powerup.menus;

import com.cryptomorin.xseries.XMaterial;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import me.patothebest.guiframework.gui.inventory.page.StaticPaginatedUI;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MaterialsMenu extends StaticPaginatedUI<XMaterial> {

  //TODO: get normal items.
  protected MaterialsMenu(Plugin plugin, Player player, String rawName) {
    super(plugin, player, rawName, () -> Arrays.stream(XMaterial.values()).filter(xMaterial -> xMaterial != null && xMaterial.isSupported() && xMaterial.parseMaterial() != Material.AIR).collect(Collectors.toList()));
    build();
  }

  @Override
  protected GUIButton createButton(XMaterial xMaterial) {

    final Material mat = xMaterial.parseMaterial();

    return new SimpleButton(new ItemStackBuilder(mat).name(mat.name()).addLore("Click to select as item.")).onClick(() -> {
      player.sendMessage("You have selected " + mat.name() + " as item.");
      new EditMenu(plugin, player, "Edit Menu", 9 * 3);
    });
  }

}
