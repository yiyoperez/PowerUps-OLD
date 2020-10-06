package me.patothebest.guiframework.gui.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public interface GUIButton {

    void click(ClickType click, GUIPage page);
    void destroy();
    ItemStack getItem();

}
