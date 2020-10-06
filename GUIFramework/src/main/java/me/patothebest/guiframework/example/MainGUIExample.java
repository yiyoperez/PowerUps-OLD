package me.patothebest.guiframework.example;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class MainGUIExample extends GUIPage {

    private final BukkitTask task;

    public MainGUIExample(Plugin plugin, Player player) {
        super(plugin, player, "GUI Test", 9);
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::refresh, 20L, 20L);
        build();
    }

    @Override
    public void buildPage() {
        Material randomMaterial = Compatibility.COMPATIBLE_MATERIALS.get((int) (Math.random() * Compatibility.COMPATIBLE_MATERIALS.size()));

        ItemStackBuilder simpleUI = new ItemStackBuilder()
                .material(Material.PAPER)
                .name(ChatColor.GREEN + "Simple GUI")
                .lore(ChatColor.WHITE + "The core of the framework,", ChatColor.WHITE + "a simple GUIPage");

        ItemStackBuilder staticPaginatedUI = new ItemStackBuilder()
                .material(randomMaterial)
                .name(ChatColor.GREEN + "Static Paginated GUI")
                .lore(ChatColor.WHITE + "This is an example of a menu", ChatColor.WHITE + "using a DynamicPaginatedUI");

        addButton(new SimpleButton(simpleUI).onClick(() -> {
            new SimpleGUIExample(plugin, player);
        }), 2);

        addButton(new SimpleButton(staticPaginatedUI).onClick(() -> {
            new StaticPaginatedGUIExample(plugin, player);
        }), 6);
    }

    @Override
    public void destroy() {
        task.cancel();
    }
}
