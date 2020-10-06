package me.patothebest.guiframework.gui.inventory.button;

import me.patothebest.guiframework.gui.inventory.GUIButton;
import me.patothebest.guiframework.gui.inventory.GUIPage;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleButton implements GUIButton {

    private ItemStack item;
    protected ButtonAction action;

    public SimpleButton(ItemStack item) {
        this.item = item;
    }

    public SimpleButton(ItemStack item, ButtonAction buttonAction) {
        this.item = item;
        this.action = buttonAction;
    }

    public SimpleButton onClick(ButtonAction action) {
        this.action = action;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public void click(ClickType clickType, GUIPage page) {
        if(action == null) {
            return;
        }

        action.onClick();
    }

    public void destroy() {
        this.action = null;
        this.item = null;
    }
}
