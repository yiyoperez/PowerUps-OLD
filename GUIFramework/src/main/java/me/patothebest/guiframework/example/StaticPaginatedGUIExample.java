package me.patothebest.guiframework.example;

import me.patothebest.guiframework.gui.anvil.AnvilSlot;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import me.patothebest.guiframework.gui.inventory.button.AnvilButton;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import me.patothebest.guiframework.gui.inventory.page.StaticPaginatedUI;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.stream.Collectors;

public class StaticPaginatedGUIExample extends StaticPaginatedUI<Material> {

    public StaticPaginatedGUIExample(Plugin plugin, Player player) {
        super(plugin, player, "Testing the gui framework", () -> Compatibility.COMPATIBLE_MATERIALS);
        build();
    }

    public StaticPaginatedGUIExample(Plugin plugin, Player player, String filter) {
        super(plugin, player, "Testing the gui framework", () -> Compatibility.COMPATIBLE_MATERIALS
                .stream()
                .filter(material -> material.name().contains(filter.toUpperCase()))
                .collect(Collectors.toList()));
        build();
    }

    @Override
    protected GUIButton createButton(Material material) {
        return new SimpleButton(new ItemStackBuilder(material).lore(ChatColor.GREEN + "Click to get!")).onClick(() -> {
            player.getInventory().addItem(new ItemStackBuilder(material));
        });
    }

    @Override
    protected void buildFooter() {
        addButton(new AnvilButton(new ItemStackBuilder().material(Material.BOOK).name("Filter"))
                .onConfirm((event) -> new StaticPaginatedGUIExample(plugin, player, event.getOutput()))
                .onCancel(() -> new StaticPaginatedGUIExample(plugin, player))
                .useSlot(AnvilSlot.INPUT_LEFT, new ItemStackBuilder().material(Material.BOOK).name("Dirt")), 51);
    }
}
