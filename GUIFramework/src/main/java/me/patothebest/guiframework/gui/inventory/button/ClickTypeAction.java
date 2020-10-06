package me.patothebest.guiframework.gui.inventory.button;

import org.bukkit.event.inventory.ClickType;

@FunctionalInterface
public interface ClickTypeAction {

    void onClick(ClickType clickType);

}
