package me.patothebest.guiframework.gui.inventory.page;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.itemstack.ItemStackBuilder;
import me.patothebest.guiframework.gui.inventory.button.PlaceHolder;
import me.patothebest.guiframework.gui.inventory.button.SimpleButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class GUIMultiPage extends GUIPage {

    protected int currentPage;
    protected int pageSize = 45;

    public GUIMultiPage(Plugin plugin, Player player, String rawName) {
        this(plugin, player, rawName, 54);
    }

    public GUIMultiPage(Plugin plugin, Player player, String rawName, int size) {
        super(plugin, player, rawName, size);
    }

    public void buildPage() {
        ItemStack nextPage = new ItemStackBuilder().material(Material.PAPER).amount(currentPage + 2).name(ChatColor.YELLOW + "Click to go to next page (page " + (currentPage + 2) + ")");
        ItemStack previousPage = new ItemStackBuilder().material(Material.PAPER).amount(currentPage).name(ChatColor.YELLOW + "Click to go to previous page (page " + (currentPage) + ")");
        ItemStack currentPageItem = new ItemStackBuilder().material(Material.PAPER).amount(currentPage + 1).name(ChatColor.YELLOW + "You are currently on page " + (currentPage + 1) + "");

        if ((currentPage + 1) * pageSize < getListCount()) {
            addButton(new SimpleButton(nextPage).onClick(() -> {currentPage++;refresh();}), 53);
        }

        if (currentPage != 0) {
            addButton(new SimpleButton(previousPage).onClick(() -> {currentPage--;refresh();}), 45);
        }

        if(getListCount() != -1) {
            addButton(new PlaceHolder(currentPageItem), 49);
        }
        buildContent();
    }

    protected abstract void buildContent();

    protected abstract int getListCount();

}
