package me.patothebest.guiframework.gui.inventory.page;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import me.patothebest.guiframework.gui.inventory.button.PlaceHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FailedPage extends GUIPage {

    private String[] reason;

    public FailedPage(Plugin plugin, Player player, String... reason) {
        super(plugin, player, "Error!", 54);
        this.reason = reason;
        build();
    }

    public void buildPage() {
        ItemStack confirm = new ItemStackBuilder().material(Material.REDSTONE_BLOCK).name(ChatColor.RED + "ERROR:").lore(reason);
        for (int i = 0; i < 54; i++) {
            addButton(new PlaceHolder(confirm), i);
        }

    }

    public void destroy() {
        this.reason = null;
    }

}
