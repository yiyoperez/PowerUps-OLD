package me.patothebest.guiframework.gui.inventory.button;

import me.patothebest.guiframework.gui.inventory.GUIPage;
import me.patothebest.guiframework.gui.inventory.GUIButton;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class NullButton implements GUIButton {

    @Override
    public void click(ClickType clickType, GUIPage page) {

    }

    @Override
    public void destroy() { }

    @Override
    public ItemStack getItem() {
        return null;
    }
}
